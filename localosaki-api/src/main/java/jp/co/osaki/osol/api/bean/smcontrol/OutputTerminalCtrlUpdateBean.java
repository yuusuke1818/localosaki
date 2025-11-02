package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.OutputTerminalCtrlUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.OutputTerminalCtrlUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.OutputTerminalCtrlUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200011Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 出力端末制御(設定) Bean クラス
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.OUTPUT_TERMINAL_CTRL_UPDATE)
@RequestScoped
public class OutputTerminalCtrlUpdateBean extends AbstractApiBean<OutputTerminalCtrlUpdateResult,OutputTerminalCtrlUpdateParameter>{

	@EJB
	private OutputTerminalCtrlUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(OutputTerminalCtrlUpdateParameter parameter) {
		A200011Param param = new Gson().fromJson(parameter.getResult(), A200011Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if (!super.isFV2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
