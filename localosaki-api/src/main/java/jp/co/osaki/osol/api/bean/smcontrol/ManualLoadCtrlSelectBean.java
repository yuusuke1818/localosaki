package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ManualLoadCtrlSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ManualLoadCtrlSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.ManualLoadCtrlSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200008Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 手動負荷制御(取得) Bean クラス
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.MANUAL_LOAD_CTRL_SELECT)
@RequestScoped
public class ManualLoadCtrlSelectBean
		extends AbstractApiBean<ManualLoadCtrlSelectResult, ManualLoadCtrlSelectParameter> {
	@EJB
	private ManualLoadCtrlSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(ManualLoadCtrlSelectParameter parameter) {
		A200008Param param = new A200008Param();

		if(param!=null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	// 機種依存チェック(FV2, FVP(D), FVPa(D), FVPa(G2), Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
				&& !super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
