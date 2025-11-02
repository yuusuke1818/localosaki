package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SchedulePatternUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SchedulePatternUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.SchedulePatternUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200062Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * スケジュールパターン(設定) Bean クラス
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.SCHEDULE_PATTERN_UPDATE)
@RequestScoped
public class SchedulePatternUpdateBean extends AbstractApiBean<SchedulePatternUpdateResult, SchedulePatternUpdateParameter>{

	@EJB
	private SchedulePatternUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(SchedulePatternUpdateParameter parameter) {
		A200062Param param = new Gson().fromJson(parameter.getResult(), A200062Param.class);

		param.setUpdateDBflg(parameter.isUpdateDBflg());

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

		//listサイズチェック
		List<Map<String, Object>> settingSchedulePatternList = ((A200062Param)param).getSettingSchedulePatternList();

		for (Map<String, Object> settingSchedulePattern : settingSchedulePatternList) {
			// 入れ子リスト取得
			Object strTimeZoneList = settingSchedulePattern.get("timeZoneList");
			List<Map<String,String>> timeZoneList =
					new Gson().fromJson(String.valueOf(strTimeZoneList), new TypeToken<Collection<Map<String, String>>>(){}.getType());
			if (timeZoneList.size() != SmControlConstants.SCHEDULE_PATTERN_UPDATE_TIME_ZONE_LIST) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "timeZoneList.size()", String.valueOf(timeZoneList.size()));
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}

		return true;
	}


	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
	     // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200062Param> res = (FvpCtrlMngResponse<A200062Param>) response;

        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // dao呼出
        dao.updateSchedule(res, super.loginUserId);
	}

}
