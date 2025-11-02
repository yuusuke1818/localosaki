package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.EventCtrlHistFVPSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.EventCtrlHistFVPSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.EventCtrlHistFVPSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200035Param;
import jp.co.osaki.osol.mng.param.BaseParam;


/**
 *
 * イベント制御履歴(FVP)(取得) Bean クラス
 *
 * @author da_yamano
 *
 */
@Named(value = SmControlConstants.EVENT_CTRL_HIST_FVP_SELECT)
@RequestScoped
public class EventCtrlHistFVPSelectBean extends AbstractApiBean<EventCtrlHistFVPSelectResult,EventCtrlHistFVPSelectParameter>{

	@EJB
	private EventCtrlHistFVPSelectDao dao;


	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(EventCtrlHistFVPSelectParameter parameter) {
		A200035Param param = new A200035Param();

		if(parameter != null) {
			param.setLoadCtrlAssignment(parameter.getLoadCtrlAssignment());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェックFVPa(G2), Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFVPaG2(smPrm)
				&& !super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}
}
