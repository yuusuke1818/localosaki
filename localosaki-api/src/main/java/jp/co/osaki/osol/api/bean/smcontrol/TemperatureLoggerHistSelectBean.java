package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.TemperatureLoggerHistSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.TemperatureLoggerHistSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.TemperatureLoggerHistSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200037Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 無線温度ロガー履歴(取得) Bean クラス
 *
 * @autho t_sakamoto
 *
 */

@Named(value = SmControlConstants.TEMPERATURE_LOGGER_HIST_SELECT)
@RequestScoped
public class TemperatureLoggerHistSelectBean
		extends AbstractApiBean<TemperatureLoggerHistSelectResult, TemperatureLoggerHistSelectParameter> {

	@EJB
	private TemperatureLoggerHistSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(TemperatureLoggerHistSelectParameter parameter) {
		A200037Param param = new A200037Param();

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if (!super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
