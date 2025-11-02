package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
 *
 * 照明任意(取得) Param クラス
 *
 * @autho t_hayama
 *
 */
public class A200048Param extends BaseParam {

	/**
	 * 計測日時
	 */
	private String measureDayTime;

	/**
	 * グループリスト
	 */
	private List<Map<String, String>> groupList;

	/**
	 * ゾーンリスト
	 */
	private List<Map<String, String>> zoneList;


	public String getMeasureDayTime() {
		return measureDayTime;
	}
	public void setMeasureDayTime(String measureDayTime) {
		this.measureDayTime = measureDayTime;
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

}
