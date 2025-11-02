package jp.co.osaki.osol.mng.param;

import java.util.Map;


/**
*
* 通信動作モード(取得) Param クラス.
*
* @autho da_yamano
*
*/
public class A200016Param extends BaseParam {


	/**
	 * 応答コード
	 */
	private String answerCd;

	/**
	 * 伝送端末状態
	 */
	private Map<String,String> transmissionTerminalState;

	/**
	 * PPP接続設定内容
	 */
	private Map<String,String> settingConnectionPPP;

	/**
	 * 伝送端末設定内容
	 */
	private Map<String,String> transmissionTerminalSetting;

	/**
	 * 通信確認先設定内容
	 */
	private Map<String,String> communicationSetting;

	/**
	 * 警報状態
	 */
	private Map<String,String> alertState;

	public String getAnswerCd() {
		return answerCd;
	}

	public void setAnswerCd(String answerCd) {
		this.answerCd = answerCd;
	}

	public Map<String, String> getTransmissionTerminalState() {
		return transmissionTerminalState;
	}

	public void setTransmissionTerminalState(Map<String, String> transmissionTerminalState) {
		this.transmissionTerminalState = transmissionTerminalState;
	}

	public Map<String, String> getSettingConnectionPPP() {
		return settingConnectionPPP;
	}

	public void setSettingConnectionPPP(Map<String, String> settingConnectionPPP) {
		this.settingConnectionPPP = settingConnectionPPP;
	}

	public Map<String, String> getTransmissionTerminalSetting() {
		return transmissionTerminalSetting;
	}

	public void setTransmissionTerminalSetting(Map<String, String> transmissionTerminalSetting) {
		this.transmissionTerminalSetting = transmissionTerminalSetting;
	}

	public Map<String, String> getCommunicationSetting() {
		return communicationSetting;
	}

	public void setCommunicationSetting(Map<String, String> communicationSetting) {
		this.communicationSetting = communicationSetting;
	}

	public Map<String, String> getAlertState() {
		return alertState;
	}

	public void setAlertState(Map<String, String> alertState) {
		this.alertState = alertState;
	}





}
