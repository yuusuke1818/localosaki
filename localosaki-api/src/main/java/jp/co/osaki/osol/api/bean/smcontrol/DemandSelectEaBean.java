package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DemandSelectEaDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DemandSelectEaParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.DemandSelectEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200049Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * デマンド(取得) Eα Bean クラス.
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.DEMAND_SELECT_EA)
@RequestScoped
public class DemandSelectEaBean extends AbstractApiBean<DemandSelectEaResult, DemandSelectEaParameter> {

	@EJB
	private DemandSelectEaDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(DemandSelectEaParameter parameter) {
		A200049Param param = new A200049Param();
		if (parameter != null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(Ea,Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
		// param取得
		@SuppressWarnings("unchecked")
		FvpCtrlMngResponse<A200049Param> res = (FvpCtrlMngResponse<A200049Param>) response;
		A200049Param resParam = res.getParam();

		//if DBフラグがOFFなら処理無し
		if (!super.apiParameter.isUpdateDBflg()) {
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
