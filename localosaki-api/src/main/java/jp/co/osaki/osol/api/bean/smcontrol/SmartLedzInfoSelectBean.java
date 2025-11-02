package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmartLedzInfoSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SmartLedzInfoSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.SmartLedzInfoSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200057Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * SmartLEDZ情報(取得) Bean クラス.
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.SMART_LEDZ_INFO_SELECT)
@RequestScoped
public class SmartLedzInfoSelectBean extends AbstractApiBean<SmartLedzInfoSelectResult, SmartLedzInfoSelectParameter> {

	@EJB
	private SmartLedzInfoSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(SmartLedzInfoSelectParameter parameter) {
		A200057Param param = new A200057Param();
		if (parameter != null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(Ea,Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
		}

		return true;
	}

}
