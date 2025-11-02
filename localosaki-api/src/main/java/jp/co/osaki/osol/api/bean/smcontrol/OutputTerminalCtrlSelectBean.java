package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.OutputTerminalCtrlSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.OutputTerminalCtrlSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.OutputTerminalCtrlSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200010Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 出力端末制御(取得) Bean クラス
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.OUTPUT_TERMINAL_CTRL_SELECT)
@RequestScoped
public class OutputTerminalCtrlSelectBean
		extends AbstractApiBean<OutputTerminalCtrlSelectResult, OutputTerminalCtrlSelectParameter> {

	@EJB
	private OutputTerminalCtrlSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(OutputTerminalCtrlSelectParameter parameter) {
		A200010Param param = new A200010Param();

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
