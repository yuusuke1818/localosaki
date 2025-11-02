package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.TemperatureCtrlUpdateDao;
import jp.co.osaki.osol.api.parameter.smcontrol.TemperatureCtrlUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.TemperatureCtrlUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200003Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 温度制御(設定) Bean クラス
 *
 * @author da_yamano
 *
 */
@Named(value = SmControlConstants.TEMPERATURE_CTRL_UPDATE)
@RequestScoped
public class TemperatureCtrlUpdateBean
extends AbstractApiBean<TemperatureCtrlUpdateResult, TemperatureCtrlUpdateParameter> {

	@EJB
	private TemperatureCtrlUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(TemperatureCtrlUpdateParameter parameter) {
		A200003Param param = new Gson().fromJson(parameter.getResult(), A200003Param.class);

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

		//list内の機種依存の項目をバリデーションチェック
		List<Map<String, String>> list = ((A200003Param)param).getSettingCtrlPortList();
		for(Map<String, String> map : list) {

			//FV2、FVP(D)機種依存項目チェック
			if (super.isFV2(smPrm) || super.isFVPD(smPrm))
			{
				String demandGangCtrlPermission = (String)map.get("demandGangCtrlPermission");
				//桁数チェックとnullチェックを行う
				if(demandGangCtrlPermission==null || !(demandGangCtrlPermission.matches("[0-9]"))) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "demandGangCtrlPermission", demandGangCtrlPermission);
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}
			//FVPa(D)機種依存項目チェック
			if (super.isFVPaD(smPrm))
			{
				String switchChoiceCW = (String)map.get("switchChoiceCW");
				//桁数チェックとnullチェックを行う
				if(switchChoiceCW==null || !(switchChoiceCW.matches("[0-9]"))) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "switchChoiceCW", switchChoiceCW);
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}
		}

		return true;
	}



}
