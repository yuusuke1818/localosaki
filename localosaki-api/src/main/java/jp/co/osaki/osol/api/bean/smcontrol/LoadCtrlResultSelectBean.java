package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.LoadCtrlResultSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.LoadCtrlResultSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.LoadCtrlResultSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200038Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 負荷制御実績(取得) Bean クラス
 *
 * @autho t_sakamoto
 *
 */

@Named(value = SmControlConstants.LOAD_CTRL_RESULT_SELECT)
@RequestScoped
public class LoadCtrlResultSelectBean
extends AbstractApiBean<LoadCtrlResultSelectResult, LoadCtrlResultSelectParameter> {

	@EJB
	private LoadCtrlResultSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected <T extends BaseParam> T initParam(LoadCtrlResultSelectParameter parameter) {
		A200038Param param = new A200038Param();
		if (parameter != null) {
			param.setTargetDate(parameter.getTargetDate());
			param.setSelectTarget(parameter.getSelectTarget());
			param.setHist(parameter.getHist());
			param.setUpdateDBflg(parameter.isUpdateDBflg());
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}


	//機種依存チェック(FV2,FVP(D),FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		//FV2、FVP(D)、FVPa(D)機種依存項目チェック
		if (super.isFV2(smPrm) || super.isFVPD(smPrm) || super.isFVPaD(smPrm))
		{
			String targetDate = ((A200038Param)param).getTargetDate();
			//桁数チェックとnullチェックを行う
			if(targetDate==null || !(targetDate.matches("[0-9]{4}"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "targetDate", targetDate);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}

		//FVPa(G2)機種依存項目チェック
		if (super.isFVPaG2(smPrm))
		{
			String selectTarget = ((A200038Param)param).getSelectTarget();
			//桁数チェックとnullチェックを行う
			if(selectTarget==null || !(selectTarget.matches("[0-9a-zA-Z]{1,2}"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "selectTarget", selectTarget);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}

		//FVPa(G2)機種依存項目チェック
		if (super.isFVPaG2(smPrm))
		{
			String hist = ((A200038Param)param).getHist();
			//桁数チェックとnullチェックを行う
			if(hist==null || !(hist.matches("[0-9]{1,2}"))) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "hist", hist);
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}
		}
		return true;
	}

}
