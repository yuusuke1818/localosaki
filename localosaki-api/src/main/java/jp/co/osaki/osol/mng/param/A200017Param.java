package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
*
* 通信動作モード(設定) Param クラス.
*
* @autho da_yamano
*
*/
public class A200017Param extends BaseParam {

	/**
	 * 応答コード
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String answerCd;

	/**
	 * 伝送モード設定
	 */
	private Map<String,String> settingTransmissionMode;

	@AssertTrue
	public boolean isValidateSettingTransmissionMode() {
		return settingTransmissionMode!=null && validateObj(settingTransmissionMode,  new HashMap<String, String>(){{
			//オブジェクト内のバリデーションチェック
			put("transmissionMode",  "[0-9]");			// 伝送モード
			put("transmissionSpeed", "[0-9]");			// 伝送速度
			put("pollingPeriod",     "[0-9]{1,3}");		// ポーリング周期


		}});
	}

	private boolean validateObj(Map<String, String> obj, Map<String,String> validationPattern) {
			for(Entry<String, String> ent : validationPattern.entrySet()) {
				Object value = obj.get(ent.getKey());
				if(value==null || !((String) value).matches(ent.getValue())) {
					return false;
				}
			}
		//}
		return true;
	}

	/**
	 * 警報発呼設定
	 */
	private Map<String,String> settingAlertCall;

	@AssertTrue
	public boolean isValidateSettingAlertCall() {
		return settingAlertCall!=null && validateObj(settingAlertCall,  new HashMap<String, String>(){{
			//オブジェクト内のバリデーションチェック
			put("callIpAddress",  "[0-9]{1,12}");		// 発呼先IPアドレス
			put("powerActivation", "[0-9]");			// 電源投入
			put("communicationError",     "[0-9]");		// FVPx間通信異常
			put("equipmentError",     "[0-9]");			// 機器異常
			put("timeoutCall",     "[0-9]{1,3}");		// 発呼タイムアウト

		}});
	}

	public String getAnswerCd() {
		return answerCd;
	}

	public void setAnswerCd(String answerCd) {
		this.answerCd = answerCd;
	}

	public Map<String, String> getSettingTransmissionMode() {
		return settingTransmissionMode;
	}

	public void setSettingTransmissionMode(Map<String, String> settingTransmissionMode) {
		this.settingTransmissionMode = settingTransmissionMode;
	}

	public Map<String, String> getSettingAlertCall() {
		return settingAlertCall;
	}

	public void setSettingAlertCall(Map<String, String> settingAlertCall) {
		this.settingAlertCall = settingAlertCall;
	}


}
