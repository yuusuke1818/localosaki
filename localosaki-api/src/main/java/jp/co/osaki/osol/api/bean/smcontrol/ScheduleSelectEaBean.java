package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ScheduleSelectEaDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ScheduleSelectEaParameter;
import jp.co.osaki.osol.api.result.smcontrol.ScheduleSelectEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200059Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * スケジュール(取得) Eα Bean クラス
 *
 * @author t_hayama
 *
 */
@Named(value = SmControlConstants.SCHEDULE_SELECT_EA)
@RequestScoped
public class ScheduleSelectEaBean extends AbstractApiBean<ScheduleSelectEaResult, ScheduleSelectEaParameter>{

	@EJB
	private ScheduleSelectEaDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200059Param> res = (FvpCtrlMngResponse<A200059Param>) response;

        //if DBフラグがOFFなら処理無し
        if (!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // dao呼出
        dao.updateSchedule(res, super.loginUserId);
	}

	@Override
	protected <T extends BaseParam> T initParam(ScheduleSelectEaParameter parameter) {
		A200059Param param = new A200059Param();

		if (parameter!=null) {
			param.setScheduleControlInfo(parameter.getScheduleControlInfo());
			param.setSettingChangeHist(parameter.getSettingChangeHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		// コマンド設定
		String history = param.getSettingChangeHist();
		if (history == null) {
			history = "0";
		}
		if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(param.getScheduleControlInfo())) {
			param.setCommand("V" + history + SmControlConstants.SCHEDULE_CONTROL_INFO_1_CMD);
		} else if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(param.getScheduleControlInfo())) {
			param.setCommand("V" + history + SmControlConstants.SCHEDULE_CONTROL_INFO_2_CMD);
		} else {
			// Not Process
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

		String scheduleControlInfo = ((A200059Param)param).getScheduleControlInfo();
		if (!SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)
				&& !SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControlInfo)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "scheduleControlInfo", scheduleControlInfo);
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		return true;
	}

}
