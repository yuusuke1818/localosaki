package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.FVxManageInfoSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.FVxManageInfoSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.FVxManageInfoSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200014Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * FVx間管理情報(取得) Bean クラス
 *
 * @author da_yamano
 *
 */
@Named(value = SmControlConstants.FVX_MANAGE_INFO_SELECT)
@RequestScoped
public class FVxManageInfoSelectBean
		extends AbstractApiBean<FVxManageInfoSelectResult, FVxManageInfoSelectParameter> {

	@EJB
	private FVxManageInfoSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(FVxManageInfoSelectParameter parameter) {
		A200014Param param = new A200014Param();

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
