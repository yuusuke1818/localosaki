package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.TemperatureCtrlHistSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.TemperatureCtrlHistSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.TempHumidControlLogResult;
import jp.co.osaki.osol.api.result.smcontrol.TemperatureCtrlHistSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200028Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 温度制御履歴(取得) Bean クラス.
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.TEMPERATURE_CTRL_HIST_SELECT)
@RequestScoped
public class TemperatureCtrlHistSelectBean
        extends AbstractApiBean<TemperatureCtrlHistSelectResult, TemperatureCtrlHistSelectParameter> {

    @EJB
    private TemperatureCtrlHistSelectDao dao;

    // 機器情報
    private SmPrmResultData smPrm;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
	protected <T extends BaseParam> T initParam(TemperatureCtrlHistSelectParameter parameter) {
        A200028Param param = new A200028Param();

        if (parameter != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        if (param.isUpdateDBflg()) {
            // DBフラグがTrueの場合、設定変更履歴の指定がある場合NG (履歴系固有処理)
            if (parameter.getSettingChangeHist() == null || parameter.getSettingChangeHist().isEmpty()) {
				@SuppressWarnings("unchecked")
				T ret = (T) param;

				return ret;
            } else {
                //親クラスで入力バリデーションの処理に入るため設定変更履歴に想定しない値を設定
                param.setSettingChangeHist("ERROR_VALUE");

				@SuppressWarnings("unchecked")
				T ret = (T) param;

				return ret;
            }
        }

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
    }

    //機種依存チェック(FV2、FVPD、FVPa(D)以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if (!super.isFV2(smPrm) && !super.isFVPD(smPrm) && !super.isFVPaD(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        // 機器情報を保持
        this.smPrm = smPrm;

        return true;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        //if DBフラグがOFFなら処理無し
        if (!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // 負荷制御履歴
        List<Map<String, String>> loadCtrlHistList = ((A200028Param) response.getParam()).getLoadCtrlHistList();

        // 設定変更履歴
        int settingHist = 1;

        // 機器から取得した履歴が前回履歴に遡るまで繰り返し行う
        for (; settingHist <= SmControlConstants.TEMPERATURE_CONTROL_LOG_RETRY_CNT; settingHist++) {

            // 取得した100件中、最古データ取得
            Map<String, String> loadCtrlHist = loadCtrlHistList.get(loadCtrlHistList.size() - 1);

            // DB検索用ResultSet
            TempHumidControlLogResult target = new TempHumidControlLogResult();
            target.setSmId(this.smPrm.getSmId());
            String recordDayTimeSeconds = loadCtrlHist.get("recordDayTimeSeconds");
            //記録日時は、西暦が2桁のため、4桁に修正して、主キー検索を行う
            recordDayTimeSeconds = DateUtility.changeDateFormat(
                    DateUtility.conversionDate(recordDayTimeSeconds, DateUtility.DATE_FORMAT_YYMMDDHHMMSS),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
            target.setRecordYMDHM(recordDayTimeSeconds);
            target.setPortOutStatus(loadCtrlHist.get("portOutputState"));

            // 最古レコードチェック
            if (target.getPortOutStatus().trim().isEmpty() || dao.SelectTempHumidCntlLog(target) != null) {
                // 空文字が格納されている場合、機器に登録されている履歴を全て取得したとし、forを抜ける
                // dao処理後、値が返却される場合、機器から取得した100件中に履歴差分が収まっているため、forを抜ける
                break;
            }

            // Fvpリクエスト作成
            FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(this.smPrm,
                    super.apiParameter.getLoginCorpId(), super.apiParameter.getLoginPersonId(), super.loginUserId);

            // Paramセット
            A200028Param reParam = new A200028Param();
            reParam.setSettingChangeHist(String.valueOf(settingHist)); // 設定変更履歴
            reParam.setUpdateDBflg(super.apiParameter.isUpdateDBflg()); // DB登録フラグ
            req.setParam(reParam);

            // 機器通信呼び出し
            FvpCtrlMngResponse<? extends BaseParam> reRes;
            try {
                reRes = fvpCtrlMngClient.excute(req);
            } catch (SmControlException e) {
                super.loggingError(Thread.currentThread().getStackTrace()[1], e);
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                throw e;
            }

            // レスポンスに過去データを追加
            loadCtrlHistList.addAll(((A200028Param) reRes.getParam()).getLoadCtrlHistList());
        }
    }

}
