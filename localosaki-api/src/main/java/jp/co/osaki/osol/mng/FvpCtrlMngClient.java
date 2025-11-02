package jp.co.osaki.osol.mng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.beanio.InvalidRecordException;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmConnectStatusDao;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectControlSettingResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmConnectStatusResultData;
import jp.co.osaki.osol.api.utility.smcontrol.AielMasterLogFileUtility;
import jp.co.osaki.osol.api.utility.smcontrol.FactoryLoaderUtility;
import jp.co.osaki.osol.api.utility.smcontrol.FileUploadUtility;
import jp.co.osaki.osol.api.utility.smcontrol.SmConnectControlSetting;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 機器制御用クライアント
 *
 * @author shimizu
 */
@Dependent
public class FvpCtrlMngClient<T extends BaseParam>{

    /**
     * エラー用ログ
     */
    private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * 一括制御カウンタ
     */
    private static AtomicInteger controlCount = new AtomicInteger(0);

    /**
     * リクエストパラメータをレスポンスとして詰めるコマンド
     */
    private static final List<String> LIST_CMD_OF_CONV_REQPARAM_TO_RESPONSE = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add(SmControlConstants.DEMAND_COMMAND_E_ALPHA);             // デマンド設定 Eα
            add(SmControlConstants.NATIONAL_HOLIDAY_UPDATE_COMMAND);    // 祝日設定
            add(SmControlConstants.SCHEDULE_UPDATE_COMMAND_E_ALPHA);    // スケジュール設定 Eα
            add(SmControlConstants.SCHEDULE_PATTERN_UPDATE_COMMAND);    // スケジュールパターン設定
            add(SmControlConstants.SETTING_EVENT_COMMAND);              // イベント制御設定
        }
    });

    /**
     * 一時領域へのファイル出力が必要なコマンド
     */
    private static final List<String> LIST_CMD_OF_OUTPUT_DATA_TO_TEMPDIR = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add(SmControlConstants.AIEL_MASTER_LOG_COLLECT_COMMAND);          // AielMasterログ収集
        }
    });

    /**
     * BeanIO マッピング定義Loader
     */
    @Inject
    FactoryLoaderUtility factoryLoaderUtility;

    /**
     * 機器通信制御設定
     */
    @Inject
    SmConnectControlSetting smConnexctControlSetting;

    /**
     * コンフィグ
     */
    @Inject
    private OsolConfigs osolConfigs;

    /**
     * ファイルアップロード Utiltiy
     */
    @Inject
    private FileUploadUtility fileUploadUtility;

    /**
     * 機器通信ステータス Dao
     */
    @EJB
    SmConnectStatusDao dao;

    /**
     * 機器からの応答コード
     */
    private byte smResponseCode = SmControlConstants.SM_COMM_RESPONSE_ERROR;

    /**
     * 出力ファイル
     */
    private AielMasterLogFileUtility outputFile;

    /**
     *      一括系リクエスト時の処理
     *
     * @param reqList
     * @return 機器制御レスポンスList
     * @throws SmControlException
     */
    public List<FvpCtrlMngResponse<T>> excute(List<FvpCtrlMngRequest<T>> reqList) throws SmControlException{

        // 一括制御中の件数を取得。許容値以上の場合は処理を戻す。
        if (controlCount.get() >= SmControlConstants.BULK_CONTROL_MAX) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s)API_ERROR_SMCONTROL_BUSY",st.getClassName(),st.getMethodName(),st.getLineNumber());
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BUSY,"API_ERROR_SMCONTROL_BUSY");
        }

        // 制御カウンタをインクリメント
        controlCount.incrementAndGet();

        List<FvpCtrlMngResponse<T>> resList = new ArrayList<>();
        try {
            ExecutorService threadpool = Executors.newFixedThreadPool(reqList.size());
            Collection<SmControlTask> tasks = new ArrayList<>();

            // リクエストの数だけ子スレッド生成
            for(FvpCtrlMngRequest<T> req : reqList) {
                tasks.add(new SmControlTask(req));
            }

            // スレッド実行
            List<Future<FvpCtrlMngResponse<T>>> futures = threadpool.invokeAll(tasks);
            threadpool.shutdown();

            Iterator<FvpCtrlMngRequest<T>> itrReqList = reqList.iterator();
            for(Future<FvpCtrlMngResponse<T>> f : futures) {
                FvpCtrlMngRequest<T> req = itrReqList.next();

                FvpCtrlMngResponse<T> res = null;
                try {
                    // 子スレッドからレスポンスを取得
                    res = f.get();
                } catch (ExecutionException e) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(e));
                    // 子スレッドでのエラー時はエラーコード用にレスポンスを生成
                    res = new FvpCtrlMngResponse<>();
                    if(req!=null) {
                        res.setSmId(req.getSmId());
                    }
                    String errCode;
                    if(e.getCause() instanceof SmControlException) {
                        errCode = ((SmControlException)e.getCause()).getErrorCode();
                    }else {
                        e.printStackTrace();
                        errCode = OsolApiResultCode.API_ERROR_UNKNOWN;
                    }
                    res.setFvpResultCd(errCode);
                }finally {
                    resList.add(res);
                }
            }
        } catch (InterruptedException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN,"API_ERROR_UNKNOWN");
        }  finally {
            // 制御カウンタをデクリメント
            controlCount.decrementAndGet();
        }
        return resList;
    }

    /**
     * 機器制御 実行 (ファイル出力あり)
     *
     * @param req
     * @param outputFileName
     * @return
     * @throws Exception
     */
    public FvpCtrlMngResponse<T> excute(FvpCtrlMngRequest<T> req, AielMasterLogFileUtility outputFile) throws Exception {
        this.outputFile = outputFile;
        return this.excute(req);
    }

    /**
     * Bean-固定長変換
     *
     * @param req
     * @return
     * @throws SmControlException
     */
    public FvpCtrlMngResponse<T> excute(FvpCtrlMngRequest<T> req) throws Exception{

        // Facroty取得
        StreamFactory factory = factoryLoaderUtility.getFacrory(req.getCommandCd());

        // プロダクトコードからマッピング定義名を取得
        String reqStreamName = req.getProductCd() + SmControlConstants.TO_FIXEDSTRING_STREAM_NAME_POSTFIX;
        String resStreamName = req.getProductCd() + SmControlConstants.FROM_FIXEDSTRING_STREAM_NAME_POSTFIX;

        // Bean->固定長変換
        Marshaller marshaller = factory.createMarshaller(reqStreamName);
        String fixedString = marshaller.marshal(req.getParam()).toString();

        // 機器通信
        String result = this.send(req.getSmId(), req.getProductCd(), req.getIpAddress(), fixedString,
                            req.getCorpId(), req.getPersonId(), req.getUserId(), req.isUpdateDBflg(), req.getCommandCd());

        // レスポンス生成
        FvpCtrlMngResponse<T> res = new FvpCtrlMngResponse<>();

        // Ea/Ea2 且つ 機器からの応答コードが「応答データなし」の場合、専用の変換マップに置き換え
        if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(req.getProductCd()) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(req.getProductCd())) &&
                this.smResponseCode == SmControlConstants.SM_COMM_RESPONSE_NONE) {
            resStreamName = SmControlConstants.MAPPING_NAME_SM_RESPONSE_NONE;
        }

        // 固定長->Bean変換

        try {
            Unmarshaller unmarshaller = factory.createUnmarshaller(resStreamName);

            @SuppressWarnings("unchecked")
            T recParam = (T) unmarshaller.unmarshal(result);

            if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(req.getProductCd()) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(req.getProductCd())) &&
                    LIST_CMD_OF_CONV_REQPARAM_TO_RESPONSE.contains(req.getCommandCd())) {
                res.setParam(req.getParam());
            } else {
                res.setParam(recParam);
            }

            // param内smAddressがNullの場合、リクエストからのsmアドレスを挿入する。
            if (res.getParam().getSmAddress() == null ) {
                res.getParam().setSmAddress(req.getSmAddress());
            }

        } catch (InvalidRecordException e) {
            // 新旧電文対応（新電文に対応していない場合、旧電文情報と紐づけ
            T recParam = oldResponseUnmarshalling(req.getProductCd(), result, factory);

            if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(req.getProductCd()) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(req.getProductCd())) &&
                    LIST_CMD_OF_CONV_REQPARAM_TO_RESPONSE.contains(req.getCommandCd())) {
                res.setParam(req.getParam());
            } else {
                res.setParam(recParam);
            }

            // param内smAddressがNullの場合、リクエストからのsmアドレスを挿入する。
            if (res.getParam().getSmAddress() == null ) {
                res.getParam().setSmAddress(req.getSmAddress());
            }
        }catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_RESPONSEDATA,"API_ERROR_SMCONTROL_RESPONSEDATA");
        }

        res.setCommandCd(req.getCommandCd());
        res.setSmId(req.getSmId());

        if ((SmControlConstants.PRODUCT_CD_E_ALPHA.equals(req.getProductCd()) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(req.getProductCd())) &&
                this.smResponseCode == SmControlConstants.SM_COMM_RESPONSE_NONE) {
            res.setFvpResultCd(SmControlConstants.RESPONSE_NO_DATA);
        } else {
            res.setFvpResultCd(OsolApiResultCode.API_OK);
        }

        return res;
    }

    /**
     * 機器通信※任意電文呼び出し
     * @param smId
     * @param productCd
     * @param ip
     * @param fixedString
     * @param corpId
     * @param personId
     * @param userId
     * @return
     * @throws SmControlException
     */
    public String excute(Long smId, String productCd, String ip, String fixedString,
            String corpId, String personId, Long userId, String commandCd) throws Exception {
        return this.send(smId, productCd, ip, fixedString, corpId, personId, userId, false, commandCd);
    }


    /**
     * 電文コマンド依存設定値の読み出し
     * @param commandCd
     * @param rateType
     * @return -1:設定なし
     */
    private int getSocketSettingsRate(String commandCd, String rateType) {
        int socIntervalRate = 0;

        if (commandCd == null || rateType == null) {
            // Invalid Parameter
            return 0;
        }

        // 電文コマンド依存の個別設定処理
        try {
            String rateStr = osolConfigs.getConfig(commandCd + rateType);

            if (rateStr != null ) {
                socIntervalRate = Integer.parseInt(rateStr);
            }
        } catch (Exception e) {
            // 読み出し失敗
            //errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        return socIntervalRate;
    }

    /**
     *  機器通信:Dao利用処理
     *
     * @param smId
     * @param productCd
     * @param ip
     * @param fixedString
     * @param corpId
     * @param personId
     * @param userId
     * @param updateDBFlag
     * @return
     * @throws SmControlException
     */
    private String send(Long smId, String productCd, String ip, String fixedString, String corpId,
            String personId, Long userId, boolean updateDBFlag, String commandCd) throws Exception {

        // 通信結果
        String result;

        // 機器通信制御設定
        SmConnectControlSettingResultData setting = smConnexctControlSetting.getSmConnectControlSettingSetting(productCd);

        // 機器通信制御設定ID
        Long settingId = setting.getSmConnectControlSettingId();

        // 通信上限数
        int parallelConnectMax = setting.getParallelConnectControlMaxCount();

        // 待機時間
        int waitTime = setting.getSmConnectWaitTime() * 1000;

        // リトライ上限回数
        int retryMax = setting.getSmConnectRetryCount();

        // リトライ回数
        int retryCount = 0;

        //API個別の機器通信待機時間/リトライ回数の倍率調整をしない
        //必要な場合は、コメントアウトを外す
        /*if (commandCd != null) {
            // 待機時間の個別設定処理
            int waitTimeRate = getSocketSettingsRate(commandCd, OsolConstants.SMCONTROL_SOCKET_INTERVAL_RATE);

            if (waitTimeRate > 0) {
                waitTime = waitTime * waitTimeRate;
            }

            int retryMaxRate = getSocketSettingsRate(commandCd, OsolConstants.SMCONTROL_SOCKET_RETRYCNT_RATE);
            if (retryMaxRate > 0) {
                retryMax = retryMax * retryMaxRate;
            }
        }*/


        /* 待機ループ */
        SmConnectStatusResultData status = null;
        while (retryCount < retryMax) {

            // 機器ステータスを主キー(smId)検索
            status = dao.findSmConnectStatus(smId);
            if(status==null) {
                // 見つからなければinsert処理
                status = new SmConnectStatusResultData();
                status.setSmId(smId);
                status.setUpdateUserId(userId);
                dao.insertSmConnectStatus(status);
            }
            status.setSmConnectControlSettingId(settingId);
            status.setIpAddress(ip);

            boolean wait = true;
            try {
                // 機器状態判定
                if (dao.checkParallelConnect(status, parallelConnectMax, updateDBFlag, userId)) {
                    // 通信OKなら待機ループから抜ける
                    wait = false;
                    break;
                }

            }catch (SmControlException e) {
                // SmControlExceptionの場合はリトライ
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }catch (Exception e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                wait = false;
                throw e;
            }finally {
                if(wait) {
                    try {
                        // 一定時間待機
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        continue;
                    }
                }
            }
            retryCount++;
        }

        // 一定時間通信ロックがかかっていた場合、タイムアウトを返却
        if (retryCount >= retryMax) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s)API_ERROR_SMCONTROL_THREADTIMEOUT smId=%s",st.getClassName(),st.getMethodName(),st.getLineNumber(),smId);
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_THREADTIMEOUT, "API_ERROR_SMCONTROL_THREADTIMEOUT");
        }

        try {
            FvpCtrlMng mng = new FvpCtrlMng(setting.getSocketConnectRetryCount(), setting.getSocketConnectWaitTime(),
                                                                                                corpId, personId, smId, productCd, fileUploadUtility, outputFile);
            int port = getUDPPort(fixedString);
            int opt = 0;

            // 機器制御 Option判定
            if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
                opt = getFvpCtrlMngOption(commandCd);
            }

            // 機器通信
            result = mng.sendUDP(ip, port, fixedString, opt);

            // 機器からの応答コードを保持
            this.smResponseCode = mng.getSmResponseCode();
        } catch (SmControlException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw e;
        } finally {
            // 接続フラグを戻す
            status.setConnectActiveFlg(OsolConstants.FLG_OFF);
            status.setUpdateUserId(userId);
            dao.updateSmConnectStatus(status);
        }

        return result;
    }

    /**
     * 送信先のUDPポートを電文から判別する
     *
     * @param fixedString
     * @return 送信先のUDPポート
     */
    private int getUDPPort(String fixedString) {
        // 電文が'T'から始まる場合はT系のUDPポート
        return (fixedString.charAt(0)=='T' ? SmControlConstants.SM_CONTROL_UDP_PORT_T : SmControlConstants.SM_CONTROL_UDP_PORT);
    }

    /**
     * 機器制御 Option判定
     *
     * @param commandCd
     * @return
     */
    private int getFvpCtrlMngOption(String commandCd) {
        int ret = 0;

        // 一時領域へのファイル出力 有無判定
        if (LIST_CMD_OF_OUTPUT_DATA_TO_TEMPDIR.contains(commandCd)) {
            ret |= SmControlConstants.FVP_CTRL_MNG_OPT_OUTPUT_TEMP_FILE;
        }

        return ret;
    }

    /**
     * 旧電文とテンプレートを紐づける
     * @param productCd
     * @param result
     * @param factory
     * @return
     * @throws Exception
     */
    private T oldResponseUnmarshalling(String productCd, String result, StreamFactory factory) throws Exception{
        // 新旧電文対応（新電文に対応していない場合、旧電文情報とマッピング
        String resStreamNameOld = productCd + SmControlConstants.FROM_FIXEDSTRING_STREAM_NAME_OLD_POSTFIX;
        try {
            Unmarshaller unmarshaller = factory.createUnmarshaller(resStreamNameOld);
            @SuppressWarnings("unchecked")
            T recParam = (T) unmarshaller.unmarshal(result);
            return recParam;
        } catch (IllegalArgumentException e) {
            // StreamFactory.createUnmarshallerができない場合（旧電文テンプレートが存在しない）
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_RESPONSEDATA,"API_ERROR_SMCONTROL_RESPONSEDATA");
        } catch (Exception e) {
            // 旧電文との変換でエラー
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_RESPONSEDATA,"API_ERROR_SMCONTROL_RESPONSEDATA");
        }
    }


    /**
     * 一括処理スレッド用 インナークラス
     *
     */
    private class SmControlTask implements Callable<FvpCtrlMngResponse<T>>{

        private FvpCtrlMngRequest<T> req;

        public SmControlTask(FvpCtrlMngRequest<T> req) {
            super();
            this.req = req;
        }

        @Override
        public FvpCtrlMngResponse<T> call() throws Exception {
            if(this.req.getParam() == null) {
                // リクエストがnullの場合は、通信しない
                throw new SmControlException(req.getCommandCd(),null);
            }
            return excute(this.req);
        }
    }
}
