package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* SmartLEDZ情報(取得) param クラス
*
* @autho t_hayama
*
*/
public class A200057Param extends BaseParam {

	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]")
	private String settingChangeHist;

	/**
	 * 設定変更日時
	 */
	private String settingDate;

	/**
	 * SmartLEDZ接続
	 */
	private String connectionSmartLedz;

	/**
	 * 照明制御切替
	 */
	private String switchLightControl;

	/**
	 * システムID
	 */
	private String systemId;

	/**
	 * ゲートウェイIP
	 */
	private Map<String, Object> gatewayIp;

	/**
	 * グループリスト
	 */
	private List<Map<String, String>> groupList;

	/**
	 * ゾーンリスト
	 */
	private List<Map<String, String>> zoneList;

	/**
	 * パターンリスト
	 */
	private List<Map<String,Object>> patternList;


	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getConnectionSmartLedz() {
		return connectionSmartLedz;
	}

	public void setConnectionSmartLedz(String connectionSmartLedz) {
		this.connectionSmartLedz = connectionSmartLedz;
	}

	public String getSwitchLightControl() {
		return switchLightControl;
	}

	public void setSwitchLightControl(String switchLightControl) {
		this.switchLightControl = switchLightControl;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public Map<String, Object> getGatewayIp() {
		return gatewayIp;
	}

	public void setGatewayIp(Map<String, Object> gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public List<Map<String, String>> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Map<String, String>> groupList) {
		this.groupList = groupList;
	}

	public List<Map<String, String>> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<Map<String, String>> zoneList) {
		this.zoneList = zoneList;
	}

	public List<Map<String,Object>> getPatternList() {
		return patternList;
	}

	public void setPatternList(List<Map<String,Object>> patternList) {
		this.patternList = patternList;
	}

}
