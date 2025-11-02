package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.WirelessDeviceInfoSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.WirelessDeviceInfoSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.WirelessDeviceInfoSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200032Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 無線装置情報(取得) Bean クラス
 *
 * @author Takemura
 *
 */
@Named(value = SmControlConstants.WIRELESS_DEVICE_INFO_SELECT)
@RequestScoped
public class WirelessDeviceInfoSelectBean extends AbstractApiBean<WirelessDeviceInfoSelectResult,WirelessDeviceInfoSelectParameter>{

	@EJB
	private WirelessDeviceInfoSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(WirelessDeviceInfoSelectParameter parameter) {
		A200032Param param = new A200032Param();

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2, FVP(D), FVPa(D), FVPa(G2), Ea, Ea2以外はエラー)
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
