package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.MaxDemandSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.MaxDemandSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.MaxDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200040Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 最大デマンド(取得) Bean クラス
 *
 * @autho da_yamano
 *
 */

@Named(value = SmControlConstants.MANUAL_DEMAND_SELECT)
@RequestScoped
public class MaxDemandSelectBean extends AbstractApiBean<MaxDemandSelectResult, MaxDemandSelectParameter> {

	@EJB
	private MaxDemandSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(MaxDemandSelectParameter parameter) {
		A200040Param param = new A200040Param();
		if(param!=null) {
			param.setDataHist(parameter.getDataHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2、FVP(D)、FVPa(D)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
