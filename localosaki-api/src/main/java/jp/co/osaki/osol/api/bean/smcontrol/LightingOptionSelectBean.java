package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DeviceInfoSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.LightingOptionSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.LightingOptionSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200048Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 照明任意(取得) Bean クラス
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.LIGHTING_OPTION_SELECT)
@RequestScoped
public class LightingOptionSelectBean extends AbstractApiBean<LightingOptionSelectResult, LightingOptionSelectParameter> {

	@EJB
	private DeviceInfoSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(LightingOptionSelectParameter parameter) {
		A200048Param param = new A200048Param();

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}


	//機種依存チェック(Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
