package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ContinuousMonitorringSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ContinuousMonitorringSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.ContinuousMonitorringSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200031Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 常時監視(取得) Bean クラス
 *
 * @autho Takemura
 *
 */

@Named(value = SmControlConstants.COMMUNICATION_MONITORRING_SELECT)
@RequestScoped
public class ContinuousMonitorringSelectBean extends AbstractApiBean<ContinuousMonitorringSelectResult,ContinuousMonitorringSelectParameter>{

	@EJB
	private ContinuousMonitorringSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(ContinuousMonitorringSelectParameter parameter) {
		A200031Param param = new A200031Param();

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2,FVP(D),FVPa(D),FVPa(G2),Ea,Ea2以外はエラー)
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
