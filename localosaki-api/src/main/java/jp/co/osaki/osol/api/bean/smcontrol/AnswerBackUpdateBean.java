package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AnswerBackUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AnswerBackUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AnswerBackUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200023Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * アンサーバック設定(設定) Bean クラス
 *
 * @autho Takemura
 *
 */
@Named(value = SmControlConstants.ANSWER_BACK_UPDATE)
@RequestScoped
public class AnswerBackUpdateBean extends AbstractApiBean<AnswerBackUpdateResult,AnswerBackUpdateParameter>{

	@EJB
	private AnswerBackUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(AnswerBackUpdateParameter parameter) {
		A200023Param param = new Gson().fromJson(parameter.getResult(), A200023Param.class);

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
		// List数チェック
		List<Map<String, String>> loadInfoList = ((A200023Param)param).getLoadInfoList();
		if(loadInfoList.size() != SmControlConstants.ANSWER_BACK_LOAD_INFO_LIST) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "loadInfoList.size()", String.valueOf(loadInfoList.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

}
