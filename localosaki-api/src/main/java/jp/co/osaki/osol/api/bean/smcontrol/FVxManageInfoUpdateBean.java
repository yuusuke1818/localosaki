package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.FVxManageInfoUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.FVxManageInfoUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.FVxManageInfoUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200015Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * FVx間管理情報(設定) Bean クラス
 *
 * @author da_yamano
 *
 */

@Named(value = SmControlConstants.FVX_MANAGE_INFO_UPDATE)
@RequestScoped
public class FVxManageInfoUpdateBean
		extends AbstractApiBean<FVxManageInfoUpdateResult, FVxManageInfoUpdateParameter> {

	@EJB
	private FVxManageInfoUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(FVxManageInfoUpdateParameter parameter) {
		A200015Param param = new Gson().fromJson(parameter.getResult(), A200015Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FV2、FVP(D)、FVPa(D)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

//		//listサイズチェック
		Map<String, Object> setManageInfoFVPx = ((A200015Param)param).getSetManageInfoFVPx();
		Object errorAlertMeasurePointXList =setManageInfoFVPx.get("errorAlertMeasurePointXList");
		List<Map<String,String>> errorAlertMeasurePointList =
				new Gson().fromJson(String.valueOf(errorAlertMeasurePointXList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
		if(errorAlertMeasurePointList.size() != SmControlConstants.ERROR_ALERT_MEASUREPOINT_LIST) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "errorAlertMeasurePointList.size()", String.valueOf(errorAlertMeasurePointList.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}


		return true;
	}

}
