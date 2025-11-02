package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DemandStartUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DemandStartUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.DemandStartUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200025Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * デマンドスタート(設定) Bean クラス
 *
 * @autho t_sakamoto
 *
 */

@Named(value = SmControlConstants.DEMAND_START_UPDATE)
@RequestScoped
public class DemandStartUpdateBean extends AbstractApiBean<DemandStartUpdateResult, DemandStartUpdateParameter> {

	@EJB
	private DemandStartUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(DemandStartUpdateParameter parameter) {
		A200025Param param = new Gson().fromJson(parameter.getResult(), A200025Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FVP(D), FVPa(D), FVPa(G2), Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
                && !super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
