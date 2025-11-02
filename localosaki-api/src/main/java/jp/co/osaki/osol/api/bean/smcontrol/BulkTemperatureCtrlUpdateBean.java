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
import jp.co.osaki.osol.api.dao.smcontrol.BulkTemperatureCtrlUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkTempratureUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.BulkTemperatureCtrlUpdateResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.utility.smcontrol.BulkAPIMailSendCallUtility;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200042Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 複数建物・テナント一括 温度制御(設定) Bean クラス
 *
 * @author f_takemura
 */

@Named(value = SmControlConstants.BULK_TEMPERATURE_CTRL_UPDATE)
@RequestScoped
public class BulkTemperatureCtrlUpdateBean extends AbstructBulkApiBean <BulkTemperatureCtrlUpdateResult, BulkTempratureUpdateParameter> {

	@EJB
	private BulkTemperatureCtrlUpdateDao dao;

	@Inject
	private BulkAPIMailSendCallUtility bulkAPIMailSendCallUtility;

	// メールAPI用
	private List<A200042Param> newDataList = new ArrayList<>();		// 設定情報
	private List<A200042Param> oldDataList = new ArrayList<>();		// 設定前情報

	// 冷暖房設定
	private String SETTING_CONDITON = "0";	// 冷暖房判定

	// コマンド取得
	private String COMMAND_CHECK = "";			// コマンド判定

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	@Override
	protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){

		// 一括温度制御設定
		List<A200042Param> paramList = new ArrayList<>();
		//リクエスト仕様内、固有の値を設定
		for(Map<String, String> p:parameterList) {

			// nullチェック
			String newResultSetObj = p.get("newResultSet");	// 設定情報
			String oldResultSetObj = p.get("oldResultSet");	// 設定前情報

			// 必須項目がnullの場合、ListにNullを格納する。
			if(newResultSetObj == null || oldResultSetObj == null ) {
				paramList.add(null);
				this.newDataList.add(null);
				this.oldDataList.add(null);
				continue;
			}

			// Gsonパース
			A200042Param newParam = new Gson().fromJson(newResultSetObj.toString(), A200042Param.class);
			A200042Param oldParam = new Gson().fromJson(oldResultSetObj.toString(), A200042Param.class);

			// Listに追加
			paramList.add(newParam);

			// 子側で保持
			this.newDataList.add(newParam);
			this.oldDataList.add(oldParam);
		}

		// 冷暖房設定
		this.SETTING_CONDITON = super.apiParameter.getSettingCondition().replace("\"", "");

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

		//リストサイズチェック
		if ((super.isFV2(smPrm)) || (super.isFVPD(smPrm)) || (super.isFVPaD(smPrm))) {

			// List数チェック
			List<Map<String, String>> ctrlTimeZoneTHList = ((A200042Param)param).getCtrlTimeZoneTHList();
			if(ctrlTimeZoneTHList.size() != SmControlConstants.CTRL_TIME_ZONE_LIST) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "ctrlTimeZoneTHList.size()", String.valueOf(ctrlTimeZoneTHList.size()));
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}

			List<Map<String, String>> settingCtrlPortList = ((A200042Param)param).getSettingCtrlPortList();
			if(settingCtrlPortList.size() != SmControlConstants.CTRL_PORT_LIST) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "settingCtrlPortList.size()", String.valueOf(settingCtrlPortList.size()));
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}

		}
		//FV2、FVP(D)機種依存項目チェック
		if ((super.isFV2(smPrm)) || (super.isFVPD(smPrm))) {

			//list内の機種依存の項目をバリデーションチェック
			List<Map<String, String>> settingCtrlPortList = ((A200042Param)param).getSettingCtrlPortList();
			for(Map<String, String> map : settingCtrlPortList) {
				String demandGangCtrlPermission = map.get("demandGangCtrlPermission");
				//桁数チェックとnullチェックを行う
				if(demandGangCtrlPermission==null || !(demandGangCtrlPermission.matches("[0-9]"))) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "demandGangCtrlPermission", demandGangCtrlPermission);
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}

		}
		//FVPa(D)機種依存項目チェック
		if (super.isFVPaD(smPrm)) {
			List<Map<String, String>> list = ((A200042Param)param).getSettingCtrlPortList();

			//list内の機種依存の項目をバリデーションチェック
			for(Map<String, String> map : list) {
				String switchChoiceCW = map.get("switchChoiceCW");
				//桁数チェックとnullチェックを行う
				if(switchChoiceCW == null || !(switchChoiceCW.matches("[0-9]"))) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "switchChoiceCW", switchChoiceCW);
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}

		}

		if (super.isFVPaG2(smPrm)){
			// List数チェック
			List<Map<String, Object>> loadList = ((A200042Param)param).getLoadList();
			if(loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST) {
				StackTraceElement st = Thread.currentThread().getStackTrace()[1];
				super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
				throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
			}

			for(Map<String, Object> load : loadList) {
				Object strSettingEventList = load.get("settingEventCtrlList");
				// 入れ子リスト取得
				List<Map<String,Object>> eventList =
						new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,Object>>>(){}.getType());
				if(eventList.size() != SmControlConstants.SETTING_EVENT_LIST) {
					StackTraceElement st = Thread.currentThread().getStackTrace()[1];
					super.loggingError(st, "eventList.size()", String.valueOf(eventList.size()));
					throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
				}
			}
		}

		return true;
	}

	@Override
	protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {

		// メール内容設定
		List<SmControlVerocityResult> targetList = mailBodySetting(fvpResList);

		// メール送信API呼出
		try {
			bulkAPIMailSendCallUtility.bulkMailSend(targetList , SmControlConstants.BULK_TEMPERATURE_COMMAND, SETTING_CONDITON);
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
			A200042Param newRecord = this.newDataList.get(i);
			// リクエスト旧設定情報レコード取得
			A200042Param oldRecord = this.oldDataList.get(i);

			// 新旧設定情報のどちらかでも存在しない場合スキップ
			if (oldRecord == null || newRecord == null) {
				continue;
			}

			// 温度制御,イベント制御判定
			checkCommand(newRecord);

			target.setSmId(fvpRes.getSmId());
			target.setCorpId(super.apiParameter.getLoginCorpId());
			target.setPersonId(String.valueOf(super.apiParameter.getLoginPersonId()));
			target.setSmAddress(fvpRes.getSmAddress());
			target.setIpAddress(fvpRes.getIpAddress());
			target.setCommand(SmControlConstants.BULK_TEMPERATURE_COMMAND);


			// 設定前後情報格納
			if (oldRecord != null && newRecord != null) {
			    if(settingOldData(oldRecord) == null || settingOldData(oldRecord).isEmpty()) {
			        target.setOldData("-");
			    } else {
    				target.setOldData(settingOldData(oldRecord));
			    }
                if(settingNewData(newRecord) == null || settingNewData(newRecord).isEmpty()) {
                    target.setNewData("-");
                } else {
    				target.setNewData(settingNewData(newRecord));
                }
			}

			// 新旧レコードチェック
			if (fvpRes.getFvpResultCd() == SmControlConstants.RECORD_NO_CHANGE) {
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

	// コマンド判定メソッド
	private void checkCommand(A200042Param newRecord) {
		// 設定情報の項目により判定
		if (newRecord.getLoadList() == null) {
			this.COMMAND_CHECK = SmControlConstants.TEMPERATURE_COMMAND;
		}else{
			this.COMMAND_CHECK = SmControlConstants.EVENT_COMMAND;
		}
	}

	// 設定前情報を格納メソッド
	private String settingOldData(A200042Param oldRecord) {
		String oldData = null;

		// 冷房設定かつイベント制御
		if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
			for (Map<String,Object> load :oldRecord.getLoadList()) {
				Object strSettingEventList = load.get("settingEventCtrlList");
				// 制御ポートが有効な値を設定
				String strValue = String.valueOf(load.get("eventCtrlFlg"));
				int intEventCtrlFlg = (int)Double.parseDouble(strValue);
				if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)) {
					// List内のsettingEventCtrlListの値がStringのJson形式となっているので、Gsonパース
					List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
					String eventTerms = settingEventCtrlList.get(0).get("eventTerms");
					if(eventTerms != null && eventTerms.equals("2")) { // イベント条件が温度制御
    					// 制御閾値
    					oldData = settingEventCtrlList.get(0).get("ctrlThreshold");
    					break;
					}
				}
			}
			// 制御ポートがすべて無効なら任意の値を設定する
			if (oldData == null ) {
				Object strSettingEventList = oldRecord.getLoadList().get(0).get("settingEventCtrlList");
				List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
				oldData = settingEventCtrlList.get(0).get("ctrlThreshold");
			}
		}
		// 冷房設定かつ温度制御
		else if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)) {
			// 制御ポートが有効な値を設定
			for (Map<String,String> settingCtrlPort :oldRecord.getSettingCtrlPortList()) {
				if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
					// 温度上限
					oldData = settingCtrlPort.get("temperatureMax");
                    break;
				}
			}
			// 制御ポートがすべて無効なら任意の値を設定する
			if (oldData == null ) {
				oldData = oldRecord.getSettingCtrlPortList().get(0).get("temperatureMax");
			}
		}
		// 暖房設定かつイベント制御
		else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
			// 制御ポートが有効な値を設定
			for (Map<String,Object> load :oldRecord.getLoadList()) {
				Object strSettingEventList = load.get("settingEventCtrlList");
				String strValue = String.valueOf(load.get("eventCtrlFlg"));
				int intEventCtrlFlg = (int)Double.parseDouble(strValue);
				if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)) {
					// List内のsettingEventCtrlListの値がStringのJson形式となっているので、Gsonパース
					List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
					String eventTerms = settingEventCtrlList.get(0).get("eventTerms");
                    if(eventTerms != null && eventTerms.equals("2")) { // イベント条件が温度制御
    					// 制御閾値
    					oldData = settingEventCtrlList.get(1).get("ctrlThreshold");
    					break;
                    }
				}
			}
			// 制御ポートがすべて無効なら任意の値を設定する
			if (oldData == null ) {
				Object strSettingEventList = oldRecord.getLoadList().get(0).get("settingEventCtrlList");
				List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
				oldData = settingEventCtrlList.get(1).get("ctrlThreshold");
			}
		}
		// 暖房設定かつ温度制御
		else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)){
			// 制御ポートが有効な値を設定
			for (Map<String,String> settingCtrlPort :oldRecord.getSettingCtrlPortList()) {
				if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
					// 温度下限
					oldData = settingCtrlPort.get("temperatureMin");
                    break;
				}
			}
			// 制御ポートがすべて無効なら任意の値を設定する
			if (oldData == null ) {
				oldData = oldRecord.getSettingCtrlPortList().get(0).get("temperatureMin");
			}
		}

		return oldData;
	}

	// 設定情報を格納メソッド
	private String settingNewData(A200042Param newRecord) {
		String newData = null;

		// 冷房設定かつイベント制御
		if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
			for (Map<String,Object> load :newRecord.getLoadList()) {
				Object strSettingEventList = load.get("settingEventCtrlList");
				// 制御ポートが有効な値を設定
				String strValue = String.valueOf(load.get("eventCtrlFlg"));
				int intEventCtrlFlg = (int)Double.parseDouble(strValue);
				if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)) {
					// List内のsettingEventCtrlListの値がStringのJson形式となっているので、Gsonパース
					List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                    String eventTerms = settingEventCtrlList.get(0).get("eventTerms");
                    if(eventTerms != null && eventTerms.equals("2")) { // イベント条件が温度制御
    					// 制御閾値
    					newData = settingEventCtrlList.get(0).get("ctrlThreshold");
    					break;
                    }
				}
			}
		}
		// 冷房設定かつ温度制御
		else if(SETTING_CONDITON.equals(SmControlConstants.COOLER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)) {
			// 制御ポートが有効な値を設定
			for (Map<String, String> settingCtrlPort :newRecord.getSettingCtrlPortList()) {
				if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
					// 温度上限
					newData = settingCtrlPort.get("temperatureMax");
                    break;
				}
			}
		}
		// 暖房設定かつイベント制御
		else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.EVENT_COMMAND)) {
			// 制御ポートが有効な値を設定
			for (Map<String,Object> load :newRecord.getLoadList()) {
				Object strSettingEventList = load.get("settingEventCtrlList");
				String strValue = String.valueOf(load.get("eventCtrlFlg"));
				int intEventCtrlFlg = (int)Double.parseDouble(strValue);
				if (String.valueOf(intEventCtrlFlg).equals(SmControlConstants.ABLE_PORT)) {
					// List内のsettingEventCtrlListの値がStringのJson形式となっているので、Gsonパース
					List<Map<String,String>> settingEventCtrlList = new Gson().fromJson(String.valueOf(strSettingEventList), new TypeToken<Collection<Map<String,String>>>(){}.getType());
                    String eventTerms = settingEventCtrlList.get(0).get("eventTerms");
                    if(eventTerms != null && eventTerms.equals("2")) { // イベント条件が温度制御
    					// 制御閾値
    					newData = settingEventCtrlList.get(1).get("ctrlThreshold");
    					break;
                    }
				}
			}
		}
		// 暖房設定かつ温度制御
		else if(SETTING_CONDITON.equals(SmControlConstants.HEATER) && COMMAND_CHECK.equals(SmControlConstants.TEMPERATURE_COMMAND)){
			// 制御ポートが有効な値を設定
			for (Map<String,String> settingCtrlPort :newRecord.getSettingCtrlPortList()) {
				if (settingCtrlPort.get("ctrlPermissionTH").equals(SmControlConstants.ABLE_PORT)) {
					// 温度下限
					newData = settingCtrlPort.get("temperatureMin");
					break;
				}
			}
		}
		return newData;
	}
}

