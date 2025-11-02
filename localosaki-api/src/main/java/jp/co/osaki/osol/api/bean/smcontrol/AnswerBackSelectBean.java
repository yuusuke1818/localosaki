package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AnswerBackSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AnswerBackSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AnswerBackSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200022Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * アンサーバック設定(取得) Bean クラス
 *
 * @autho f_takemura
 *
 */
@Named(value = SmControlConstants.ANSWER_BACK_SELECT)
@RequestScoped
public class AnswerBackSelectBean extends AbstractApiBean<AnswerBackSelectResult,AnswerBackSelectParameter>{

	@EJB
	private AnswerBackSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(AnswerBackSelectParameter parameter) {
		A200022Param param = new A200022Param();

		if(param!=null) {
			param.setSettingChangeHist(parameter.getSettingChangeHist());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェックFVPa(G2)以外はエラー)
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
