package jp.co.osaki.osol.api.bean.sms.manulinsp.exe;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.manualinsp.exe.ManualInspExeDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.exe.ManualInspExeParameter;
import jp.co.osaki.osol.api.request.sms.manualinsp.exe.ManualInspExeRequestSet;
import jp.co.osaki.osol.api.response.sms.manualinsp.exe.ManualInspExeResponse;
import jp.co.osaki.osol.api.result.sms.manualinsp.exe.ManualInspExeMeterResult;
import jp.co.osaki.osol.api.result.sms.manualinsp.exe.ManualInspExeResult;
import jp.co.osaki.osol.api.resultdata.sms.manualinsp.exe.ManualInspExeResultData;
import jp.co.osaki.osol.api.utility.sms.manualinsp.exe.ManualInspExeAPIMailSendCallUtility;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 任意検針 任意検針実行API Beanクラス.
 *
 * @author tominaga.d
 */
@Named(value = "ExeSmsManualInspBean")
@RequestScoped
public class ExeSmsManualInspBean extends OsolApiBean<ManualInspExeParameter>
        implements BaseApiBean<ManualInspExeParameter, ManualInspExeResponse> {

    /**
     * エラー用ログ
     */
    protected static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    @EJB
    private ManualInspExeDao dao;

    @Inject
    private ManualInspExeAPIMailSendCallUtility manualInspExeAPIMailSendCallUtility;

    private ManualInspExeParameter parameter = new ManualInspExeParameter();

    private ManualInspExeResponse response = new ManualInspExeResponse();

    @Override
    public ManualInspExeParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ManualInspExeParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ManualInspExeResponse execute() throws Exception {
        // 引数設定
        String corpId = this.parameter.getCorpId();
        Long buildingId = this.parameter.getBuildingId();
        List<ManualInspExeRequestSet> exeList = new ArrayList<>();
        if (this.parameter.getManualInspExes() != null) {
            exeList = this.parameter.getManualInspExes().getManualInspList();
        }
        String loginCorpId = this.parameter.getLoginCorpId();
        String loginPersonId = this.parameter.getLoginPersonId();
        // 戻り値初期設定
        ManualInspExeResult result = new ManualInspExeResult();
        result.setResult(false);
        response.setResultCode(OsolApiResultCode.API_ERROR_BEAN_APPLICATION);
        List<ManualInspExeResultData> resDataList = new ArrayList<>();

        // 任意検針実施チェック
        resDataList = listDisableManualInsp(exeList);
        result.setManualInspExeResultDataList(resDataList);

        // 実施日時取得
        Timestamp svDate = dao.getServerDateTime();
        // 検針年・検針月 取得
        String inspYear = DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY);
        String inspMonth = DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_M);

        // 任意検針実施対象メーターリスト を 装置ID 毎にまとめる
        Map<String, List<ManualInspExeRequestSet>> exeListMap = new HashMap<>(); // Map<装置ID, List<任意検針実施対象メーター情報>>
        for (ManualInspExeRequestSet info : exeList) {
            if (!exeListMap.containsKey(info.getDevId())) {
                exeListMap.put(info.getDevId(), (new ArrayList<ManualInspExeRequestSet>()));
            }
            exeListMap.get(info.getDevId()).add(info);
        }

        for (Map.Entry<String, List<ManualInspExeRequestSet>> entry : exeListMap.entrySet()) {
            String devId = entry.getKey(); // 装置ID
            List<ManualInspExeRequestSet> exeOnlyDevList = entry.getValue(); // 任意検針実施対象メーターリスト(単一装置ID分)

            // 月検診連番取得
            Long inspMonthNo = dao.getInspMonthNo(devId, inspYear, inspMonth);

            // 検針日時の12時間前（リトライの時間分）までに自動か予約の検針未完了のデータが存在した場合、検針確定せずにメッセージを表示する。
            List<TInspectionMeterSvr> timsList = checkInspectionMeterSvr(exeOnlyDevList, inspYear, inspMonth, inspMonthNo, svDate);
            if (timsList.size() != 0) {
                // 検針日時をメッセージに設定するためresultに設定して返却
                result.setTimsList(timsList);
                response.setResult(result);
                return response;
            }
        }

        // 任意検針実施OK？
        if (resDataList.size() == 0) {
            // 小数・整数確認フラグ取得
            BigDecimal chkInt = dao.getChkInt(corpId, buildingId);

            // 装置種類分ルール
            boolean isSuccess = true;
            for (Map.Entry<String, List<ManualInspExeRequestSet>> entry : exeListMap.entrySet()) {
                String devId = entry.getKey(); // 装置ID
                List<ManualInspExeRequestSet> exeOnlyDevList = entry.getValue(); // 任意検針実施対象メーターリスト(単一装置ID分)

                // 月検診連番取得
                Long inspMonthNo = dao.getInspMonthNo(devId, inspYear, inspMonth);

                // 任意検針メール送信
                mailSendManualInsp(devId, exeOnlyDevList, inspMonthNo, svDate);
                // 任意検針実行
                if (!exeManualInsp(chkInt, exeOnlyDevList, inspYear, inspMonth, inspMonthNo, svDate, loginCorpId, loginPersonId)) {
                    isSuccess = false;
                }
            }
            if(isSuccess) {
                response.setResultCode(OsolApiResultCode.API_OK);
            }
            result.setResult(isSuccess);
        } else {
            response.setResultCode(OsolApiResultCode.API_ERROR_BEAN_PREV_INSP_INCOMPLETE);
        }
        response.setResult(result);
        return response;
    }

    /**
     * 前回検針データ未完了チェック処理.
     *
     * @param List<ManualInspExeRequestSet> exelist
     * @return 前回検針データ未完了リスト
     */
    private List<ManualInspExeResultData> listDisableManualInsp(List<ManualInspExeRequestSet> exeList) throws Exception {
        List<ManualInspExeResultData> resDataList = new ArrayList<>();
        String devWk = "";
        Long meterTypeWk = null;
        // 選択メーター毎にループ
        for (ManualInspExeRequestSet info : exeList) {
            String devId = info.getDevId();
            Long meterType = info.getMeterType();
            Long meterMngId = info.getMeterMngId();
            if (devWk.equals(devId) && meterTypeWk == meterType) {
                // 同装置ID、同メーター種別は処理しない
                continue;
            } else {
                // 検針データ未完了情報取得
                ManualInspExeResultData resInfo = dao.getDisableManualInsp(devId, meterMngId, meterType);
                if (resInfo != null) {
                    resDataList.add(resInfo);
                }
            }
        }
        return resDataList;
    }

    /**
     * 任意検針メール送信.
     *
     * @param devId 装置ID
     * @param exeOnlyDevList 任意検針実施対象メーターリスト(単一装置ID分)
     * @param inspMonthNo 月検針連番
     * @param svDate 実施日時
     */
    public void mailSendManualInsp(String devId, List<ManualInspExeRequestSet> exeOnlyDevList, Long inspMonthNo, Timestamp svDate) throws Exception {

        //メール停止フラグ取得
        boolean flag = dao.getPauseMailFlag(devId);
        if(flag) {
            return;
        }

        // マンション名称取得
        String buildingName = dao.getBuildName(devId);
        // 装置名称取得
        String devName = dao.getDevName(devId);
        // メールアドレス取得
        List<String> mailList = dao.getMailList(devId);

        List<ManualInspExeMeterResult> meterMailList = new ArrayList<>();
        // 任意検針実施対象メーターリスト(単一装置ID)分ループ
        for (ManualInspExeRequestSet manualInspExe : exeOnlyDevList) {
            Long meterMngId = manualInspExe.getMeterMngId(); // メーター管理番号

            // 計器ID取得
            MMeter minfo = dao.getMeter(devId, meterMngId);
            if (minfo == null) {
                continue;
            }
            ManualInspExeMeterResult meterMailInfo = new ManualInspExeMeterResult();
            // 管理番号
            meterMailInfo.setMeterMngId(String.format("%04d", meterMngId));
            // 計器ID
            String meterId = "--";
            if (minfo.getMeterId() != null && !minfo.getMeterId().equals("")) {
                meterId = minfo.getMeterId();
            }
            meterMailInfo.setMeterId(meterId);
            // コメント
            String memo = "--";
            if (minfo.getMemo() != null && !minfo.getMemo().equals("")) {
                memo = minfo.getMemo();
            }
            meterMailInfo.setMemo(memo);
            // リストに追加
            meterMailList.add(meterMailInfo);

        }
        try {
            // メール送信
            manualInspExeAPIMailSendCallUtility.manulInspExeMailSend(
                    buildingName, devName, devId,
                    mailList, inspMonthNo, meterMailList,
                    DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
        } catch (Exception e) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            errorLogger.errorf("[%s][%s](%s) %s", st.getClassName(), st.getMethodName(), st.getLineNumber(), "メール送信に失敗しました");
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

    }

    /**
     * 任意検針実行.
     *
     * @param chkInt 小数・整数確認フラグ
     * @param exeOnlyDevList 任意検針実施対象メーターリスト(単一装置ID分)
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 月検針連番
     * @param svDate 実施日時
     * @param loginCorpId String
     * @param loginPersonId String
     * @return 任意検針実行結果 true：正常 false：異常
     */
    public Boolean exeManualInsp(BigDecimal chkInt, List<ManualInspExeRequestSet> exeOnlyDevList,
            String inspYear, String inspMonth, Long inspMonthNo, Timestamp svDate,
            String loginCorpId, String loginPersonId) throws Exception {
        Boolean ret = true;

        // 選択メーター分ループ
        for (ManualInspExeRequestSet info : exeOnlyDevList) {
            if(info.getDevId().startsWith("OC")) {
                continue;
            }
            // 任意検針実施
            dao.exeManualInspMeter(info.getDevId(), info.getMeterMngId(),
                    chkInt, inspYear, inspMonth, inspMonthNo, svDate,
                    loginCorpId, loginPersonId);
        }
        return ret;
    }

    /**
     * 検針日時の12時間前（リトライの時間分）までに自動か予約の検針未完了のデータを取得.
     *
     * @param exeOnlyDevList 任意検針実施対象メーターリスト(単一装置ID分)
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 月検針連番
     * @param svDate 実施日時
     * @return 検針結果データ(サーバ用)
     */
    public List<TInspectionMeterSvr> checkInspectionMeterSvr(List<ManualInspExeRequestSet> exeOnlyDevList,
            String inspYear, String inspMonth, Long inspMonthNo, Timestamp svDate) throws Exception {

        List<TInspectionMeterSvr> timsList = new ArrayList<TInspectionMeterSvr>();
        TInspectionMeterSvr tims = new TInspectionMeterSvr();

        // 選択メーター分ループ
        for (ManualInspExeRequestSet info : exeOnlyDevList) {
            // 検針結果データ(サーバ用)取得
            tims = dao.getInspectionMeterSvr(info.getDevId(), info.getMeterMngId(), inspYear, inspMonth, inspMonthNo, svDate);
            if (tims != null) {
                timsList.add(tims);
            }
        }
        return timsList;
    }
}
