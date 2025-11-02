package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DemandUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DemandUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.DemandUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200007Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * デマンド(設定) Bean クラス
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.DEMAND_UPDATE)
@RequestScoped
public class DemandUpdateBean extends AbstractApiBean<DemandUpdateResult, DemandUpdateParameter> {

	@EJB
	private DemandUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(DemandUpdateParameter parameter) {

		A200007Param param = new Gson().fromJson(parameter.getResult(), A200007Param.class);
		param.setUpdateDBflg(parameter.isUpdateDBflg());

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2、FVP(D)、FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		List<Map<String, String>> list = ((A200007Param)param).getLoadInfoList();
		//listサイズチェック
		if(super.isFV2(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FV2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPD(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVPD)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaD(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_D)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaG2(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_G2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}


	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
		// param取得
		@SuppressWarnings("unchecked")
		FvpCtrlMngResponse<A200007Param> res = (FvpCtrlMngResponse<A200007Param>) response;
		A200007Param resParam = res.getParam();

		//if DBフラグがOFFなら処理無し
		if(!super.apiParameter.isUpdateDBflg()) {
			return;
		}

		// 登録用ResultSet
		BuildingDmResult param = new BuildingDmResult();

		param.setSmId(res.getSmId());

		BigDecimal targetPower = new BigDecimal((String) resParam.getTargetPower());

		param.setTargetPower(targetPower);
		param.setUpdateUserId(super.loginUserId);

		// dao呼出
		dao.updateBuildingDM(param,res);
	}

}
