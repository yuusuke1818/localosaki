package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkScheduleUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BulkScheduleUpdateResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.BulkAPIMailSendCallUtility;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200005Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 複数建物・テナント一括 スケジュール(設定) Bean クラス.
 *
 * @author F.Takemura
 */
@Named(value = SmControlConstants.BULK_SCHEDULE_UPDATE)
@RequestScoped
public class BulkScheduleUpdateBean extends AbstructBulkApiBean <BulkScheduleUpdateResult, BulkSmControlApiParameter> {

	@EJB
	private BulkScheduleUpdateDao dao;

	@Inject
	private BulkAPIMailSendCallUtility bulkAPIMailSendCallUtility;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	// リクエスト情報
	private List<A200005Param> newDataList = new ArrayList<>();		// 設定情報
	private List<A200005Param> oldDataList = new ArrayList<>();		// 設定前情報

	@Override
	protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){

		// 一括スケジュール設定
		List<A200005Param> paramList = new ArrayList<>();

		for(Map<String, String> p:parameterList) {

			// nullチェック
			String newResultSetObj = p.get("newResultSet");	// 設定情報
			String oldResultSetObj = p.get("oldResultSet");	// 設定前情報

			// 必須項目がnullの場合、ListにNullを格納する。
			if(newResultSetObj == null || oldResultSetObj == null ) {
				// リクエスト情報保持
				this.newDataList.add(null);
				this.oldDataList.add(null);
				paramList.add(null);
				continue;
			}
			// Gsonパース
			A200005Param newParam = new Gson().fromJson(newResultSetObj.toString(), A200005Param.class);
			A200005Param oldParam = new Gson().fromJson(oldResultSetObj.toString(), A200005Param.class);

			String pageAssignment = p.get("pageAssignment");
			boolean updateDBflg = Boolean.valueOf(p.get("updateDBflg"));

			if(pageAssignment == null || pageAssignment.isEmpty()) {
				pageAssignment = "0";
			}else {
				pageAssignment = pageAssignment.replace("\"", "");
			}


			newParam.setPageAssignment(pageAssignment);
			newParam.setUpdateDBflg(updateDBflg);

			// リクエスト情報保持
			this.newDataList.add(newParam);
			this.oldDataList.add(oldParam);

			paramList.add(newParam);
		}

		return paramList;
	}


	//機種依存チェック(FV2、FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		if(!super.isFV2(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		//listサイズチェック
		List<Map<String, Object>> loadList = ((A200005Param)param).getLoadList();

		if(super.isFV2(smPrm) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FV2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaD(smPrm) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FVP_ALPHA_D)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaG2(smPrm) && !(loadList.size() == SmControlConstants.SCHEDULE_LOAD_LIST_FVP_ALPHA_G2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		if(super.isFV2(smPrm) || super.isFVPaD(smPrm) || super.isFVPaG2(smPrm)) {
			for(Map<String, Object> load : loadList) {
				// 入れ子リスト取得
				Object strSettingMonthScheduleList = load.get("settingMonthScheduleList");
				List<Map<String,String>> mmScheduleList =
						new Gson().fromJson(String.valueOf(strSettingMonthScheduleList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
				if(mmScheduleList.size() != SmControlConstants.SCHEDULE_LOAD_LIST_MM_SCHEDULELIST) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "mmScheduleList.size()", String.valueOf(mmScheduleList.size()));
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}
		}

		//FVPa(D)、FVPa(G2)機種依存項目チェック
		if (super.isFVPaD(smPrm) || super.isFVPaG2(smPrm)) {
			//list内の機種依存の項目をバリデーションチェック
			List<Map<String, String>> list = ((A200005Param)param).getSettingSchedulePatternList();

			for(Map<String, String> map : list) {

				String dutySelect = map.get("dutySelect");
				//桁数チェックとnullチェックを行う
				if(dutySelect==null || !(dutySelect.matches("[0-9A-Za-z]"))) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "dutySelect", dutySelect);
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}
		}

		return true;
	}

	@Override
	protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {
		// メールAPI呼出処理
		// メール内容設定
		List<SmControlVerocityResult> targetList = mailBodySetting(fvpResList);

		// メール送信API呼出
		try {
			bulkAPIMailSendCallUtility.bulkMailSend(targetList , SmControlConstants.BULK_SCHEDULE_COMMAND);
		} catch (Exception e) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "メール送信に失敗しました");
			errorLogger.error(BaseUtility.getStackTraceMessage(e));
		}

	}

	// メール内容設定メソッド
	private List<SmControlVerocityResult> mailBodySetting(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {
		// 検索用リスト生成
		List<SmControlVerocityResult> targetList = new ArrayList<>();

		for (int i = 0; i < fvpResList.size(); i++) {

			// メールAPI用 Resultクラス
			SmControlVerocityResult target = new SmControlVerocityResult();

			// 設定情報レコード取得
			FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
			// 機器IDが未存在でエラーの場合、処理しない
			if(fvpRes.getSmId() == null ) {
				continue;
			}

			// リクエスト新設定情報レコード取得
			A200005Param newRecord = this.newDataList.get(i);
			// リクエスト旧設定情報レコード取得
			A200005Param oldRecord = this.oldDataList.get(i);
			// 新旧設定情報のどちらかでも存在しない場合スキップ
			if (oldRecord == null || newRecord == null) {
				continue;
			}

			target.setSmId(fvpRes.getSmId());
			target.setCorpId(super.apiParameter.getLoginCorpId());
			target.setPersonId(String.valueOf(super.apiParameter.getLoginPersonId()));
			target.setSmAddress(fvpRes.getSmAddress());
			target.setIpAddress(fvpRes.getIpAddress());
			target.setCommand(SmControlConstants.BULK_SCHEDULE_COMMAND);
			// 設定前後情報格納

			if (oldRecord != null && newRecord != null) {
				// スケジュール管理
			    if (oldRecord.getScheduleManageAssignment() == null || oldRecord.getScheduleManageAssignment().isEmpty()) {
                    target.setOldData("-");
			    }else if (oldRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
					target.setOldData(SmControlConstants.NUM_TO_WORD_NO_SET);
				}else if(oldRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
					target.setOldData(SmControlConstants.NUM_TO_WORD_WITH_SET);
				}
                if (newRecord.getScheduleManageAssignment() == null || newRecord.getScheduleManageAssignment().isEmpty()) {
                    target.setNewData("-");
                }else if (newRecord.getScheduleManageAssignment().equals( SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_NO_SET)) {
					target.setNewData(SmControlConstants.NUM_TO_WORD_NO_SET);
				}else if(newRecord.getScheduleManageAssignment().equals(SmControlConstants.SCHEDULE_MANAGE_ASSINGMENT_WITH_SET)) {
					target.setNewData(SmControlConstants.NUM_TO_WORD_WITH_SET);
				}
			}

			// 新旧レコードチェック
			if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
				target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
				targetList.add(target);
				continue;
			}
			// 例外が発生している場合はRECORD_NG
			else if (!(fvpRes.getFvpResultCd().equals(OsolApiResultCode.API_OK))) {
				target.setResult(SmControlConstants.MAIL_SETTING_NG);
				targetList.add(target);
				continue;
			}

			target.setResult(SmControlConstants.MAIL_SETTING_OK);
			targetList.add(target);
		}
		return targetList;
	}


}

