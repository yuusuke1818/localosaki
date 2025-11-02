package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SchedulePatternSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SchedulePatternSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.SchedulePatternSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200061Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * スケジュールパターン(取得) Bean クラス
 *
 * @author t_hayama
 *
 */
@Named(value = SmControlConstants.SCHEDULE_PATTERN_SELECT)
@RequestScoped
public class SchedulePatternSelectBean extends AbstractApiBean<SchedulePatternSelectResult, SchedulePatternSelectParameter>{

	@EJB
	private SchedulePatternSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200061Param> res = (FvpCtrlMngResponse<A200061Param>) response;

        //if DBフラグがOFFなら処理無し
        if (!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // dao呼出
        dao.updateSchedule(res, super.loginUserId);
	}

	@Override
	protected <T extends BaseParam> T initParam(SchedulePatternSelectParameter parameter) {
		A200061Param param = new A200061Param();

		if (parameter != null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		// コマンド設定
		String history = param.getSettingChangeHist();
		if (history == null) {
			history = "0";
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		return true;
	}

}
