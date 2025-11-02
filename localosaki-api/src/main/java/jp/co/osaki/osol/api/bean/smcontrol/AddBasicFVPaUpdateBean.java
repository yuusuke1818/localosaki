package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AddBasicFVPaUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AddBasicFVPaUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AddBasicFVPaUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200019Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * FVPα用追加基本(設定) Bean クラス
 *
 * @author t_sakamoto
 *
 */

@Named(value = SmControlConstants.ADD_BASIC_FVPA_UPDATE)
@RequestScoped
public class AddBasicFVPaUpdateBean
		extends AbstractApiBean<AddBasicFVPaUpdateResult, AddBasicFVPaUpdateParameter> {

	@EJB
	private AddBasicFVPaUpdateDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(AddBasicFVPaUpdateParameter parameter) {

		A200019Param param = new Gson().fromJson(parameter.getResult(), A200019Param.class);

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	//機種依存チェック(FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if (!(super.isFVPaD(smPrm) || super.isFVPaG2(smPrm))) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		//FVPa(G2)機種依存項目チェック
		if (super.isFVPaG2(smPrm))
		{
			String scheduleCtrl = ((A200019Param)param).getScheduleCtrl();
			//桁数チェックとnullチェックを行う
			if(scheduleCtrl==null || !(scheduleCtrl.matches("[0-9]"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "scheduleCtrl", scheduleCtrl);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}

		return true;
	}

}
