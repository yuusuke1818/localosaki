package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * デマンド日報(取得) Param クラス
 *
 * @autho Takemura
 *
 */
public class A200029Param extends BaseParam {

	/**
	 * 日報履歴
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String reportHist;

	/**
	 * 計測日時
	 */
	private String measureDayTime;

	/**
	 * 集計時
	 */
	private String aggregateHour;

	/**
	 * 計測ポイント数
	 */
	private String measurePointNum;

	/**
	 * 第XX時間
	 */
	private List<Map<String,String>> mesuareTimeList;

	/**
	 * 日最大デマンド
	 */
	private String dayMaxDemand;

	/**
	 * 日最大デマンド発生日時
	 */
	private String timeOccurDayMaxDemand;

	/**
	 * 月最大デマンド
	 */
	private String monthMaxDemand;

	/**
	 * 月最大デマンド発生日時
	 */
	private String timeOccurMonthMaxDemand;

	/**
	 * 年最大デマンド
	 */
	private String yearMaxDemand;

	/**
	 * 年最大デマンド発生日時
	 */
	private String timeOccurYearMaxDemand;

	/**
	 * ポイントNo.YY
	 */
	private List<Map<String,String>> measurePointList;

	/**
	 * xmlで利用
	 * ポイントNo.YYのサイズを指定
	 */
	private Integer occursPointNum;

	public Integer getOccursPointNum() {
		return occursPointNum;
	}

	//計測ポイント数をxmlで参照するための処理
	public void setOccursPointNum(Integer occursPointNum) {
		this.occursPointNum = occursPointNum;
		// 計測ポイント数に値をセット
		this.measurePointNum = String.format("%02d",occursPointNum);
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

	public String getMeasurePointNum() {
		return measurePointNum;
	}

	public void setMeasurePointNum(String measurePointNum) {
		this.measurePointNum = measurePointNum;
	}

	public List<Map<String, String>> getMesuareTimeList() {
		return mesuareTimeList;
	}

	public void setMesuareTimeList(List<Map<String, String>> mesuareTimeList) {
		this.mesuareTimeList = mesuareTimeList;
	}

	public String getDayMaxDemand() {
		return dayMaxDemand;
	}

	public void setDayMaxDemand(String dayMaxDemand) {
		this.dayMaxDemand = dayMaxDemand;
	}

	public String getTimeOccurDayMaxDemand() {
		return timeOccurDayMaxDemand;
	}

	public void setTimeOccurDayMaxDemand(String timeOccurDayMaxDemand) {
		this.timeOccurDayMaxDemand = timeOccurDayMaxDemand;
	}

	public String getMonthMaxDemand() {
		return monthMaxDemand;
	}

	public void setMonthMaxDemand(String monthMaxDemand) {
		this.monthMaxDemand = monthMaxDemand;
	}

	public String getTimeOccurMonthMaxDemand() {
		return timeOccurMonthMaxDemand;
	}

	public void setTimeOccurMonthMaxDemand(String timeOccurMonthMaxDemand) {
		this.timeOccurMonthMaxDemand = timeOccurMonthMaxDemand;
	}

	public String getYearMaxDemand() {
		return yearMaxDemand;
	}

	public void setYearMaxDemand(String yearMaxDemand) {
		this.yearMaxDemand = yearMaxDemand;
	}

	public String getTimeOccurYearMaxDemand() {
		return timeOccurYearMaxDemand;
	}

	public void setTimeOccurYearMaxDemand(String timeOccurYearMaxDemand) {
		this.timeOccurYearMaxDemand = timeOccurYearMaxDemand;
	}

	public List<Map<String, String>> getMeasurePointList() {
		return measurePointList;
	}

	public void setMeasurePointList(List<Map<String, String>> measurePointList) {
		this.measurePointList = measurePointList;
	}

	public String getReportHist() {
		return reportHist;
	}

	public void setReportHist(String reportHist) {
		this.reportHist = reportHist;
	}

}
