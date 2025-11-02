package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DemandSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DemandSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.DemandSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200006Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * デマンド(取得) Bean クラス.
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.DEMAND_SELECT)
@RequestScoped
public class DemandSelectBean extends AbstractApiBean<DemandSelectResult, DemandSelectParameter> {

	@EJB
	private DemandSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(DemandSelectParameter parameter) {
		A200006Param param = new A200006Param();
		if (parameter != null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FVP(D)、FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
		// param取得
		@SuppressWarnings("unchecked")
		FvpCtrlMngResponse<A200006Param> res = (FvpCtrlMngResponse<A200006Param>) response;
		A200006Param resParam = res.getParam();

		//if DBフラグがOFFなら処理無し
		if(!super.apiParameter.isUpdateDBflg()){
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
