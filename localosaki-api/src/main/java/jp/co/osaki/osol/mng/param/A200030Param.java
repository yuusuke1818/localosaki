package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
 *
 * デマンド任意(取得) Param クラス
 *
 * @autho Takemura
 *
 */
public class A200030Param extends BaseParam {


	/**
	 * 計測日時
	 */
	private String measureDayTime;

	/**
	 * 目標電力
	 */
	private String targetPower;

	/**
	 * 目標現在電力
	 */
	private String nowTargetPower;

	/**
	 * 予測電力
	 */
	private String forecastPower;

	/**
	 * 調整電力±記号
	 */
	private String adjustPowerPlusMinus;

	/**
	 * 調整電力
	 */
	private String adjustPower;

	/**
	 * 現在電力
	 */
	private String nowPower;

	/**
	 * 節電可能予測電力
	 */
	private String saveForecastPower;

	/**
	 * 節電可能現在電力
	 */
	private String saveNowPower;

	/**
	 * 残り時間分
	 */
	private String leftMinute;

	/**
	 * 残り時間秒
	 */
	private String leftSeconds;

	/**
	 * 前回デマンド
	 */
	private String lastPower;

	/**
	 * 前回節電可能電力
	 */
	private String lastSavePower;

	/**
	 * 日最大デマンド
	 */
	private String dayMaxDemand;

	/**
	 * 日累計電力量
	 */
	private String dayTotalPowerAmount;

	/**
	 * 月最大デマンド
	 */
	private String monthMaxDemand;

	/**
	 * 月累計電力量
	 */
	private String monthTotalPowerAmount;

	/**
	 * 余裕/超過
	 */
	private String allowanceExceed;

	/**
	 * 注意警報
	 */
	private String cautionAlert;

	/**
	 * 遮断警報
	 */
	private String blockAlert;

	/**
	 * 限界警報
	 */
	private String limitAlert;

	/**
	 * 高負荷警報
	 */
	private String highLoadAlert;

	/**
	 * 負荷情報
	 */
	private List<Map<String,String>> loadInfoList;

	/**
	 * 負荷情報(Bit)
	 */
	private List<Map<String,String>> bitLoadInfoList;

	/**
	 * 計測ポイントNo.X
	 */
	private List<Map<String,String>> measurePointList;


	public String getMeasureDayTime() {
		return measureDayTime;
	}
	public void setMeasureDayTime(String measureDayTime) {
		this.measureDayTime = measureDayTime;
	}
	public String getTargetPower() {
		return targetPower;
	}
	public void setTargetPower(String targetPower) {
		this.targetPower = targetPower;
	}
	public String getNowTargetPower() {
		return nowTargetPower;
	}
	public void setNowTargetPower(String nowTargetPower) {
		this.nowTargetPower = nowTargetPower;
	}
	public String getForecastPower() {
		return forecastPower;
	}
	public void setForecastPower(String forecastPower) {
		this.forecastPower = forecastPower;
	}
	public String getAdjustPowerPlusMinus() {
		return adjustPowerPlusMinus;
	}
	public void setAdjustPowerPlusMinus(String adjustPowerPlusMinus) {
		this.adjustPowerPlusMinus = adjustPowerPlusMinus;
	}
	public String getAdjustPower() {
		return adjustPower;
	}
	public void setAdjustPower(String adjustPower) {
		this.adjustPower = adjustPower;
	}
	public String getNowPower() {
		return nowPower;
	}
	public void setNowPower(String nowPower) {
		this.nowPower = nowPower;
	}
	public String getSaveForecastPower() {
		return saveForecastPower;
	}
	public void setSaveForecastPower(String saveForecastPower) {
		this.saveForecastPower = saveForecastPower;
	}
	public String getSaveNowPower() {
		return saveNowPower;
	}
	public void setSaveNowPower(String saveNowPower) {
		this.saveNowPower = saveNowPower;
	}
	public String getLeftMinute() {
		return leftMinute;
	}
	public void setLeftMinute(String leftMinute) {
		this.leftMinute = leftMinute;
	}
	public String getLeftSeconds() {
		return leftSeconds;
	}
	public void setLeftSeconds(String leftSeconds) {
		this.leftSeconds = leftSeconds;
	}
	public String getLastPower() {
		return lastPower;
	}
	public void setLastPower(String lastPower) {
		this.lastPower = lastPower;
	}
	public String getLastSavePower() {
		return lastSavePower;
	}
	public void setLastSavePower(String lastSavePower) {
		this.lastSavePower = lastSavePower;
	}
	public String getDayMaxDemand() {
		return dayMaxDemand;
	}
	public void setDayMaxDemand(String dayMaxDemand) {
		this.dayMaxDemand = dayMaxDemand;
	}
	public String getDayTotalPowerAmount() {
		return dayTotalPowerAmount;
	}
	public void setDayTotalPowerAmount(String dayTotalPowerAmount) {
		this.dayTotalPowerAmount = dayTotalPowerAmount;
	}
	public String getMonthMaxDemand() {
		return monthMaxDemand;
	}
	public void setMonthMaxDemand(String monthMaxDemand) {
		this.monthMaxDemand = monthMaxDemand;
	}
	public String getMonthTotalPowerAmount() {
		return monthTotalPowerAmount;
	}
	public void setMonthTotalPowerAmount(String monthTotalPowerAmount) {
		this.monthTotalPowerAmount = monthTotalPowerAmount;
	}
	public String getAllowanceExceed() {
		return allowanceExceed;
	}
	public void setAllowanceExceed(String allowanceExceed) {
		this.allowanceExceed = allowanceExceed;
	}
	public String getCautionAlert() {
		return cautionAlert;
	}
	public void setCautionAlert(String cautionAlert) {
		this.cautionAlert = cautionAlert;
	}
	public String getBlockAlert() {
		return blockAlert;
	}
	public void setBlockAlert(String blockAlert) {
		this.blockAlert = blockAlert;
	}
	public String getLimitAlert() {
		return limitAlert;
	}
	public void setLimitAlert(String limitAlert) {
		this.limitAlert = limitAlert;
	}
	public String getHighLoadAlert() {
		return highLoadAlert;
	}
	public void setHighLoadAlert(String highLoadAlert) {
		this.highLoadAlert = highLoadAlert;
	}
	public List<Map<String, String>> getLoadInfoList() {
		return loadInfoList;
	}
	public void setLoadInfoList(List<Map<String, String>> loadInfoList) {
		this.loadInfoList = loadInfoList;
	}

	public List<Map<String, String>> getBitLoadInfoList() {
		return bitLoadInfoList;
	}
	public void setBitLoadInfoList(List<Map<String, String>> bitLoadInfoList) {
		this.bitLoadInfoList = bitLoadInfoList;
	}
	public List<Map<String, String>> getMeasurePointList() {
		return measurePointList;
	}
	public void setMeasurePointList(List<Map<String, String>> measurePointList) {
		this.measurePointList = measurePointList;
	}

}
