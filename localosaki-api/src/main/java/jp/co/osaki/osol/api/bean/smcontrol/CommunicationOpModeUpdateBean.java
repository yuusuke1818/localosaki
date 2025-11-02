package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.CommunicationOpModeUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.CommunicationOpModeUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.CommunicationOpModeUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200017Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 通信動作モード(設定) Bean クラス
 *
 * @author da_yamano
 *
 */
@Named(value = SmControlConstants.COMMUNICATION_OP_MODE_UPDATE)
@RequestScoped
public class CommunicationOpModeUpdateBean extends AbstractApiBean<CommunicationOpModeUpdateResult,CommunicationOpModeUpdateParameter>{

	@EJB
	private CommunicationOpModeUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(CommunicationOpModeUpdateParameter parameter) {
		A200017Param param = new Gson().fromJson(parameter.getResult(), A200017Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2,FVP(D),FVPa(D)以外はエラー)
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
