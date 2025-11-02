package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkScheduleSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BulkScheduleSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200004Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 複数建物・テナント一括 スケジュール(取得) Bean クラス.
 *
 * @author F.Takemura
 */
@Named(value = SmControlConstants.BULK_SCHEDULE_SELECT)
@RequestScoped
public class BulkScheduleSelectBean extends AbstructBulkApiBean <BulkScheduleSelectResult, BulkSmControlApiParameter> {

	@EJB
	private BulkScheduleSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	// リクエスト情報
	private List<A200004Param> PARAM_LIST =  new ArrayList<>();
	private int RECORD_NUMBER = 0;

	@Override
	protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){

		// 一括スケジュール取得
		List<A200004Param> paramList = new ArrayList<>();

		// リスト作成
		for(Map<String, String> p:parameterList) {

			A200004Param param = new A200004Param();
			String settingChangeHist = p.get("settingChangeHist");
			String pageAssignment = p.get("pageAssignment");
			boolean updateDBflg = Boolean.valueOf(p.get("updateDBflg"));

			// 未設定時、0をセット
			if(pageAssignment == null || pageAssignment.isEmpty()) {
				pageAssignment = "0";
			}

			//リクエスト仕様内、固有の値を設定
			param.setSettingChangeHist(settingChangeHist);
			param.setPageAssignment(pageAssignment);
			param.setUpdateDBflg(updateDBflg);
			paramList.add(param);
		}

		// リクエスト情報保持
		this.PARAM_LIST = paramList;

		return paramList;
	}


	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		// 対応機器チェック
		if(!super.isFV2(smPrm) &&  !super.isFVPaD(smPrm)
				&& !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			errorLogger.errorf("[%s][%s](%s) PRODUCT_CD=%s", st.getClassName(),st.getMethodName(),st.getLineNumber(),smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		return true;
	}
}

