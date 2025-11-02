package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkTargetPowerUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BulkTargetPowerUpdateResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.BulkAPIMailSendCallUtility;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200007Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;



/**
 *
 * 複数建物・テナント一括 目標電力(設定) Bean クラス
 *
 * @author yasu_shimizu
 *
 */
@Named(value = SmControlConstants.BULK_TARGET_POWER_UPDATE)
@RequestScoped
public class BulkTargetPowerUpdateBean extends AbstructBulkApiBean<BulkTargetPowerUpdateResult, BulkSmControlApiParameter> {

	@EJB
	private BulkTargetPowerUpdateDao dao;

	@Inject
	private BulkAPIMailSendCallUtility bulkAPIMailSendCallUtility;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	// リクエスト情報
	private List<A200007Param> newDataList = new ArrayList<>();		// 設定情報
	private List<A200007Param> oldDataList = new ArrayList<>();		// 設定前情報
	private List<Boolean> updateDBflgList = new ArrayList<>();		// DBフラグリスト


	@Override
	protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){

		// 一括目標電力設定
		List<A200007Param> paramList = new ArrayList<>();

		for(Map<String, String> p:parameterList) {

			// nullチェック
			String newResultSetObj = p.get("newResultSet");	// 設定情報
			String oldResultSetObj = p.get("oldResultSet");	// 設定前情報

			// 必須項目がnullの場合、ListにNullを格納する。
			if(newResultSetObj == null || oldResultSetObj == null ) {
				paramList.add(null);

				// リクエスト情報保持
				this.newDataList.add(null);
				this.oldDataList.add(null);
				String updateDBflg = p.get("updateDBflg");
				if(updateDBflg == null || updateDBflg.isEmpty()) {
					updateDBflg = "FALSE";
				}else {
					updateDBflg = updateDBflg.replace("\"", "");
				}
				this.updateDBflgList.add(Boolean.valueOf(updateDBflg));
				continue;
			}

			// Gsonパース
			A200007Param newParam = new Gson().fromJson(newResultSetObj.toString(), A200007Param.class);
			A200007Param oldParam = new Gson().fromJson(oldResultSetObj.toString(), A200007Param.class);

			// Listに追加
			paramList.add(newParam);

			// リクエスト情報保持
			this.newDataList.add(newParam);
			this.oldDataList.add(oldParam);

			String updateDBflg = p.get("updateDBflg");
			if(updateDBflg == null || updateDBflg.isEmpty()) {
				updateDBflg = "FALSE";
			}else {
				updateDBflg = updateDBflg.replace("\"", "");
			}
			this.updateDBflgList.add(Boolean.valueOf(updateDBflg));
		}

		return paramList;
	}


	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
		// 対応機器チェック
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		List<Map<String, String>> list = ((A200007Param)param).getLoadInfoList();
		//listサイズチェック
		if(super.isFV2(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FV2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPD(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVPD)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaD(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_D)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}else if(super.isFVPaG2(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_FVP_ALPHA_G2)){
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "LoadInfoList", String.valueOf(list.size()));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return true;
	}

	@Override
	protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {

		for (int i = 0; i < fvpResList.size(); i++) {

			// DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
			if(!this.updateDBflgList.get(i)
					|| !(fvpResList.get(i).getFvpResultCd().equals(OsolApiResultCode.API_OK))){
				continue;
			}

			FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
			A200007Param res = (A200007Param) fvpRes.getParam();

			// 登録用ResultSet
			BuildingDmResult param = new BuildingDmResult();

			param.setSmId(fvpRes.getSmId());

			BigDecimal targetPower = new BigDecimal((String) res.getTargetPower());

			param.setTargetPower(targetPower);
			param.setUpdateUserId(super.loginUserId);

			// dao呼出
			try {
				dao.updateBuildingDM(param);
			} catch (Exception e) {
				// 更新エラー時はfvpResListの結果コードに例外を格納
				fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
			}
		}

		// メールAPI呼出処理
		// メール内容設定
		List<SmControlVerocityResult> targetList = mailBodySetting(fvpResList);

		// メール送信API呼出
		try {
			bulkAPIMailSendCallUtility.bulkMailSend(targetList , SmControlConstants.BULK_TARGET_POWER_COMMAND);
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
			A200007Param newRecord = this.newDataList.get(i);
			// リクエスト旧設定情報レコード取得
			A200007Param oldRecord = this.oldDataList.get(i);

			// 新旧設定情報のどちらかでも存在しない場合スキップ
			if (oldRecord == null || newRecord == null) {
				continue;
			}

			target.setSmId(fvpRes.getSmId());

			target.setCorpId(super.apiParameter.getLoginCorpId());
			target.setPersonId(String.valueOf(super.apiParameter.getLoginPersonId()));
			target.setSmAddress(fvpRes.getSmAddress());
			target.setIpAddress(fvpRes.getIpAddress());
			target.setCommand(SmControlConstants.BULK_TARGET_POWER_COMMAND);

			// 設定前後情報格納
			if (oldRecord != null && newRecord != null) {
                if(oldRecord.getTargetPower() == null || oldRecord.getTargetPower().isEmpty()) {
                    target.setOldData("-");
                } else {
                    target.setOldData(oldRecord.getTargetPower());
                }
                if(newRecord.getTargetPower() == null || newRecord.getTargetPower().isEmpty()) {
                    target.setNewData("-");
                } else {
                    target.setNewData(newRecord.getTargetPower());
                }
			}

			// 新旧レコードチェック
			if (fvpRes.getFvpResultCd().equals(SmControlConstants.RECORD_NO_CHANGE)) {
				target.setResult(SmControlConstants.MAIL_SETTING_NO_CHANGE);
				targetList.add(target);
				continue;
			}
			// 例外が発生している場合はRECORD_NG
			else if (fvpRes.getFvpResultCd() != OsolApiResultCode.API_OK) {
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
