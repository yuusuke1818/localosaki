package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
 * 無線装置情報(取得) Param クラス
 *
 * @author Takemura
 */
public class A200032Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)
	 */
	private String settingDate;

	/**
	 * 無線温度ユニット情報 No.XX
	 */
	private List<Map<String, String>> wirelessTemperatureUnitInfoList;

	/**
	 * 無線温度センサ親機YY
	 */
	private List<Map<String, String>> wirelessTemperatureSensorPdList;

	public String getSettingDate() {
		return settingDate;
	}
	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}
	public List<Map<String, String>> getWirelessTemperatureUnitInfoList() {
		return wirelessTemperatureUnitInfoList;
	}
	public void setWirelessTemperatureUnitInfoList(List<Map<String, String>> wirelessTemperatureUnitInfoList) {
		this.wirelessTemperatureUnitInfoList = wirelessTemperatureUnitInfoList;
	}
	public List<Map<String, String>> getWirelessTemperatureSensorPdList() {
		return wirelessTemperatureSensorPdList;
	}
	public void setWirelessTemperatureSensorPdList(List<Map<String, String>> wirelessTemperatureSensorPdList) {
		this.wirelessTemperatureSensorPdList = wirelessTemperatureSensorPdList;
	}


}
