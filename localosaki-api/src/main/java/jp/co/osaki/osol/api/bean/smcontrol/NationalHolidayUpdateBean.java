package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.NationalHolidayUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.NationalHolidayUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.NationalHolidayUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200052Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 祝日(設定) Bean クラス
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.NATIONAL_HOLIDAY_UPDATE)
@RequestScoped
public class NationalHolidayUpdateBean extends AbstractApiBean<NationalHolidayUpdateResult, NationalHolidayUpdateParameter> {

	@EJB
	private NationalHolidayUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(NationalHolidayUpdateParameter parameter) {

		A200052Param param = new Gson().fromJson(parameter.getResult(), A200052Param.class);
		param.setUpdateDBflg(parameter.isUpdateDBflg());

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(Ea,Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
		}

		return true;
	}


	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
		// param取得
		@SuppressWarnings("unchecked")
		FvpCtrlMngResponse<A200052Param> res = (FvpCtrlMngResponse<A200052Param>) response;

		//if DBフラグがOFFなら処理無し
		if(!super.apiParameter.isUpdateDBflg()) {
			return;
		}

		// 登録用ResultSet
		BuildingDmResult param = new BuildingDmResult();

		param.setSmId(res.getSmId());
		param.setUpdateUserId(super.loginUserId);

		// dao呼出
		dao.updateBuildingDM(param,res);
	}

}
