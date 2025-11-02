package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.RadioWaveStateFOMASelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.RadioWaveStateFOMASelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.RadioWaveStateFOMASelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200033Param;
import jp.co.osaki.osol.mng.param.BaseParam;


/**
 *
 * FOMA電波状態(取得) Bean クラス
 *
 * @autho t_sakamoto
 *
 */

@Named(value = SmControlConstants.RADIO_WAVE_STATE_FOMA_SELECT)
@RequestScoped
public class RadioWaveStateFOMASelectBean extends AbstractApiBean<RadioWaveStateFOMASelectResult, RadioWaveStateFOMASelectParameter> {

	@EJB
	private RadioWaveStateFOMASelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(RadioWaveStateFOMASelectParameter parameter) {
		A200033Param param = new A200033Param();

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}


	//機種依存チェック(FVPa(D),FVPa(G2),Ea,Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if(!super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
				&& !super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		return true;
	}
}
