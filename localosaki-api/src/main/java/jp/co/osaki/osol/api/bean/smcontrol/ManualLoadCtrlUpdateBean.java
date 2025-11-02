package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ManualLoadCtrlUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ManualLoadCtrlUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.ManualLoadCtrlUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200009Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 手動負荷制御(設定) Bean クラス
 *
 * @autho da_yamano
 *
 */
@Named(value = SmControlConstants.MANUAL_LOAD_CTRL_UPDATE)
@RequestScoped
public class ManualLoadCtrlUpdateBean
		extends AbstractApiBean<ManualLoadCtrlUpdateResult, ManualLoadCtrlUpdateParameter> {

	@EJB
	private ManualLoadCtrlUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}


	@Override
	protected <T extends BaseParam> T initParam(ManualLoadCtrlUpdateParameter parameter) {
		A200009Param param = new Gson().fromJson(parameter.getResult(), A200009Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	// 機種依存チェック(FV2, FVP(D), FVPa(D), FVPa(G2), Ea, Ea2以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
				&& !super.isEa(smPrm) && !super.isEa2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		List<Map<String, String>> list = ((A200009Param)param).getLoadInfoList();
		//listサイズチェック
		if(super.isFV2(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_FV2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPD(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_FVPD)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaD(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_FVP_ALPHA_D)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaG2(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_FVP_ALPHA_G2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isEa(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_E_ALPHA)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isEa2(smPrm) && !(list.size() == SmControlConstants.MANUAL_CTRL_LOAD_LIST_E_ALPHA_2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		//FV2、FVP(D)機種依存項目チェック
		if (super.isFVPaD(smPrm) || super.isFVPaG2(smPrm))
		{
			String cautionManualCtrlState = ((A200009Param)param).getCautionManualCtrlState();
			//桁数チェックとnullチェックを行う
			if(cautionManualCtrlState==null || !(cautionManualCtrlState.matches("[0-9]"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "cautionManualCtrlState", cautionManualCtrlState);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}

			String blockManualCtrlState = ((A200009Param)param).getBlockManualCtrlState();
			//桁数チェックとnullチェックを行う
			if(blockManualCtrlState==null || !(blockManualCtrlState.matches("[0-9]"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "blockManualCtrlState", blockManualCtrlState);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}

			String limitManualCtrlState = ((A200009Param)param).getLimitManualCtrlState();
			//桁数チェックとnullチェックを行う
			if(limitManualCtrlState==null || !(limitManualCtrlState.matches("[0-9]"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "limitManualCtrlState", limitManualCtrlState);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}
		return true;
	}
}
