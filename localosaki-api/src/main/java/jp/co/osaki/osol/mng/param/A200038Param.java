package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
*
* 負荷制御実績(取得) Param クラス.
*
* @author t_sakamoto
*
*/
public class A200038Param extends BaseParam {

	/**
	 * 対象月日
	 */
	private String targetDate;

	/**
	 * 取得対象
	 */
	private String selectTarget;

	/**
	 * 履歴
	 */
	private String hist;

	/**
	 * 計測日時
	 */
	private String measureDayTime;

	/**
	 * 集計月日時
	 */
	private String aggregateHour;

	/**
	 * 負荷情報
	 */
	private List<Map<String, String>> loadInfoList;

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public String getSelectTarget() {
		return selectTarget;
	}

	public void setSelectTarget(String selectTarget) {
		this.selectTarget = selectTarget;
	}

	public String getHist() {
		return hist;
	}

	public void setHist(String hist) {
		this.hist = hist;
	}

	public String getMeasureDayTime() {
		return measureDayTime;
	}

	public void setMeasureDayTime(String measureDayTime) {
		this.measureDayTime = measureDayTime;
	}

	public String getAggregateHour() {
		return aggregateHour;
	}

	public void setAggregateHour(String aggregateHour) {
		this.aggregateHour = aggregateHour;
	}

	public List<Map<String, String>> getLoadInfoList() {
		return loadInfoList;
	}

	public void setLoadInfoList(List<Map<String, String>> loadInfoList) {
		this.loadInfoList = loadInfoList;
	}

	public List<Map<String, String>> getLoadCtrlHistList() {
		return loadInfoList;
	}

	public void setLoadCtrlHistList(List<Map<String, String>> loadCtrlHistList) {
		loadInfoList = loadCtrlHistList;
	}

}
