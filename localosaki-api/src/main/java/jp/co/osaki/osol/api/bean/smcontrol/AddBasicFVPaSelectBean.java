package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AddBasicFVPaSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AddBasicFVPaSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AddBasicFVPaSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200018Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * FVPα用追加基本(取得) Bean クラス
 *
 * @author t_sakamoto
 *
 */
@Named(value = SmControlConstants.ADD_BASIC_FVPA_SELECT)
@RequestScoped
public class AddBasicFVPaSelectBean
		extends AbstractApiBean<AddBasicFVPaSelectResult, AddBasicFVPaSelectParameter> {

	@EJB
	private AddBasicFVPaSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(AddBasicFVPaSelectParameter parameter) {
		A200018Param param = new A200018Param();

		if (param != null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if (!(super.isFVPaD(smPrm) || super.isFVPaG2(smPrm))) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		return true;
	}

}
