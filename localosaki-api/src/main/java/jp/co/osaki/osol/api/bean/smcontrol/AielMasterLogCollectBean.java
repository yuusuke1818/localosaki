package jp.co.osaki.osol.api.bean.smcontrol;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterLogCollectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterLogCollectParameter;
import jp.co.osaki.osol.api.response.smcontrol.SmControlApiResponse;
import jp.co.osaki.osol.api.result.smcontrol.BaseSmControlApiResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.AielMasterLogFileUtility;
import jp.co.osaki.osol.api.utility.smcontrol.FileUploadUtility;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.mng.FvpCtrlMngClient;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210010Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * AielMasterログ収集 Bean クラス
 *
 * @author t_hayama
 *
 */
@Named(value = SmControlConstants.AIELMASTER_LOG_COLLECT)
@RequestScoped
public class AielMasterLogCollectBean extends OsolApiBean<AielMasterLogCollectParameter>
            implements BaseApiBean<AielMasterLogCollectParameter, SmControlApiResponse<BaseSmControlApiResult>> {

    private static final String AIEL_MASTER_CMD_MOMENT_DATA = "DM10X";

    private static final String AIEL_MASTER_CMD_NAC_CORRECTION = "DM20X";

    private static final String AIEL_MASTER_CMD_MANUAL_CONTROL_TABLE = "DM30X";

    private static final String AIEL_MASTER_CMD_AI_CALC_LOG = "DM40X";

    private static final String AIEL_MASTER_CMD_ACT_LOG = "DM50X";


    private AielMasterLogCollectParameter parameter = new AielMasterLogCollectParameter();

    private SmControlApiResponse<BaseSmControlApiResult> response = new SmControlApiResponse<>();

    @Inject
    private FileUploadUtility fileUploadUtility;

    @Inject
    private FvpCtrlMngClient<BaseParam> fvpCtrlMngClient;

    @EJB
    AielMasterLogCollectDao aielMasterLogCollectDao;


    @Override
    public AielMasterLogCollectParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AielMasterLogCollectParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SmControlApiResponse<BaseSmControlApiResult> execute() throws Exception {
        AielMasterLogCollectParameter param = new AielMasterLogCollectParameter();
        String dateTime = this.parameter.getDateTime();

        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());

        // 日付が指定されていない場合は、サーバー時刻を取得して設定
        if (CheckUtility.isNullOrEmpty(dateTime)) {
            dateTime = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(aielMasterLogCollectDao.getServerDateTime());
        }
        param.setDateTime(dateTime);

        // 処理対象のフラグを設定
        param.setAielMasterMomentDataCollectFlg(this.parameter.isAielMasterMomentDataCollectFlg());
        param.setAielMasterNacCorrectionValueCollectFlg(this.parameter.isAielMasterNacCorrectionValueCollectFlg());
        param.setAielMasterManualControlTableCollectFlg(this.parameter.isAielMasterManualControlTableCollectFlg());
        param.setAielMasterAiCalculationResultLogCollectFlg(this.parameter.isAielMasterAiCalculationResultLogCollectFlg());
        param.setAielMasterActionLogCollectFlg(this.parameter.isAielMasterActionLogCollectFlg());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // ログ収集実行
        String result = executeLogCollect(param);

        response.setResultCode(result);

        return response;
    }

    private String executeLogCollect(AielMasterLogCollectParameter param) {
        String ret = OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTFAILED;
        boolean resultMomentData = false;
        boolean resultNacCorrection = false;
        boolean resultManualControlTable = false;
        boolean resultAiCalcLog = false;
        boolean resultActLog = false;
        MPerson mPerson = null;
        Long userId = null;

        //処理する対象が１つか判別するためのカウント
        int targetCount = 0;

        // 機器情報を取得
        SmPrmResultData smPrm = null;
        try {
            smPrm = aielMasterLogCollectDao.findSmPrm(param.getSmId());
        } catch (SmControlException e) {
            return ret;
        }

        // ユーザー情報を取得
        mPerson = getPerson(param);
        if (mPerson == null) {
            return ret;
        }
        userId = mPerson.getUserId();

        if(param.isAielMasterMomentDataCollectFlg()) {
            // 瞬時データを実行
            resultMomentData = executeLogCollectImple(smPrm, param, userId, AIEL_MASTER_CMD_MOMENT_DATA);
            targetCount ++;
        }

        if(param.isAielMasterNacCorrectionValueCollectFlg()) {
            // NAC補正値を実行
            resultNacCorrection = executeLogCollectImple(smPrm, param, userId, AIEL_MASTER_CMD_NAC_CORRECTION);
            targetCount++;
        }

        if(param.isAielMasterManualControlTableCollectFlg()) {
            // 手動制御テーブルを実行
            resultManualControlTable = executeLogCollectImple(smPrm, param, userId, AIEL_MASTER_CMD_MANUAL_CONTROL_TABLE);
            targetCount++;
        }

        if(param.isAielMasterAiCalculationResultLogCollectFlg()) {
            // AI演算結果ログを実行
            resultAiCalcLog = executeLogCollectImple(smPrm, param, userId, AIEL_MASTER_CMD_AI_CALC_LOG);
            targetCount++;
        }

        if(param.isAielMasterActionLogCollectFlg()) {
            // 動作ログを実行
            resultActLog = executeLogCollectImple(smPrm, param, userId, AIEL_MASTER_CMD_ACT_LOG);
            targetCount++;
        }

        // レスポンスコードを設定
        if (resultMomentData && resultNacCorrection && resultManualControlTable && resultAiCalcLog && resultActLog) {
            ret = OsolApiResultCode.API_OK;
        } else if (!resultMomentData && !resultNacCorrection && !resultManualControlTable && !resultAiCalcLog && !resultActLog) {
            // 処理対象が１つで処理失敗の場合
            if(targetCount == 1) {
                ret = OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE;
            }
            else {
                ret = OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTFAILED;
            }
        } else {
            // 処理対象が１つで処理成功の場合
            if(targetCount == 1) {
                ret = OsolApiResultCode.API_OK;
            }
            else {
                ret = OsolApiResultCode.API_ERROR_SMCONTROL_COLLECTWARN;
            }
        }

        return ret;
    }

    private boolean executeLogCollectImple(SmPrmResultData smPrm, AielMasterLogCollectParameter param, Long userId, String command) {
        boolean ret = false;
        AielMasterLogFileUtility outputFile = createAielMasterLogFile(param, command);

        // 機器制御を実行
        ret = executeSmControl(smPrm, param, userId, command, outputFile);
        if (ret) {
            // S3アップロードを実行
            ret = executeS3Upload(outputFile);
        }

        return ret;
    }

    private boolean executeSmControl(SmPrmResultData smPrm, AielMasterLogCollectParameter param, Long userId, String command, AielMasterLogFileUtility outputFile) {
        boolean ret = false;
        String corpId = param.getLoginCorpId();
        String personId = param.getLoginPersonId();
        A210010Param reqParam = new A210010Param();

        // マネージャリクエスト生成
        reqParam.setCommand(command);
        reqParam.setDateTime(param.getDateTime());
        FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(smPrm, corpId, personId, userId);
        req.setParam(reqParam);

        // 機器制御
        try {
            FvpCtrlMngResponse<BaseParam> res = fvpCtrlMngClient.excute(req, outputFile);
            if (OsolApiResultCode.API_OK.equals(res.getFvpResultCd())) {
                ret = true;
            }
        } catch (Exception e) {
            // NOT PROCESS
        }

        return ret;
    }

    private boolean executeS3Upload(AielMasterLogFileUtility fileInfo) {
        String outputFileDir = null;
        String outputFileName = null;
        File outputFile = null;

        // 所定の場所にファイルが出力されているかチェック
        outputFileDir = fileUploadUtility.getAielMasterLogUploadDir(fileInfo);
        outputFileName = fileUploadUtility.getAielMasterLogFileName(fileInfo);
        outputFile = new File(outputFileDir.concat(File.separator).concat(outputFileName));
        if (!outputFile.exists()) {
            return false;
        }

        // S3へアップロード
        fileUploadUtility.S3fileUpload(outputFile.getPath());

        return true;
    }

    private AielMasterLogFileUtility createAielMasterLogFile(AielMasterLogCollectParameter param, String command) {
        AielMasterLogFileUtility ret = new AielMasterLogFileUtility();

        ret.setExtension("gzip");
        ret.setSmId(param.getSmId());
        ret.setReqDayTime(param.getDateTime());
        ret.setCommand(command);

        return ret;
    }

}
