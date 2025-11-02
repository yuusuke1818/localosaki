package jp.co.osaki.osol.mng.param;

import java.util.List;

/**
*
* 警報メール送信 Param クラス.
*
* @author yasu_shimizu
*
*/
public class A200202Param extends BaseParam {

	/**
	 * シーケンス番号
	 */
	private String sequence;

	/**
	 * 応答コード
	 */
	private String responseCd;

	/**
	 * FVPaナンバー
	 */
	private String smNo;

//////////////////////////////////////////
// T9 コマンド
//////////////////////////////////////////
	/**
	 * FVPx運転状態
	 */
	private String stateFVPx;

	/**
	 * デマンド警報
	 */
	private String demandAlert;

	/**
	 * LON回線異常
	 */
	private String errorCircuitLON;

	/**
	 * デマンド設定変更
	 */
	private String settingDemand;

	/**
	 * ユニット設定変更
	 */
	private String settingUnit;

	/**
	 * 移報警報
	 */
	private String transferAlert;

	/**
	 * FVPx電池異常警報
	 */
	private String batteryErrorAlertFVPx;

	/**
	 * 予備
	 */
	private String reserve;

	/**
	 * 計測ポイント異常
	 */
	private List<String> pointList;

//////////////////////////////////////////
// HA コマンド
//////////////////////////////////////////

	/**
	 * 計測日時
	 */
	private String recordDayTime;

	/**
	 * 警報状態
	 */
	private String alertState;

	/**
	 * 目標電力
	 */
	private String targetPowerOrSettingNo;

	/**
	 * 目標現在電力
	 */
	private String nowTargetPower;

	/**
	 * 予測電力
	 */
	private String forecastPower;

	/**
	 * 残り時間分
	 */
	private String leftMinute;

	/**
	 * 残り時間秒
	 */
	private String leftSeconds;

	/**
	 * 計測ポイント異常
	 */
	private List<String> loadCtrlList;

//////////////////////////////////////////
//HM コマンド
//////////////////////////////////////////

	/**
	 * 復旧／異常フラグ(先頭)
	 */
	private String stateFlg1;

	/**
	 * 復旧／異常フラグ（警報状態3)
	 */
	private String stateFlgAfter;

	/**
	 * 復旧／異常フラグ（警報状態5)
	 */
	private String stateFlgAfterState5;

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getResponseCd() {
		return responseCd;
	}

	public void setResponseCd(String responseCd) {
		this.responseCd = responseCd;
	}

	public String getSmNo() {
		return smNo;
	}

	public void setSmNo(String smNo) {
		this.smNo = smNo;
	}

	public String getStateFVPx() {
		return stateFVPx;
	}

	public void setStateFVPx(String stateFVPx) {
		this.stateFVPx = stateFVPx;
	}

	public String getDemandAlert() {
		return demandAlert;
	}

	public void setDemandAlert(String demandAlert) {
		this.demandAlert = demandAlert;
	}

	public String getErrorCircuitLON() {
		return errorCircuitLON;
	}

	public void setErrorCircuitLON(String errorCircuitLON) {
		this.errorCircuitLON = errorCircuitLON;
	}

	public String getSettingDemand() {
		return settingDemand;
	}

	public void setSettingDemand(String settingDemand) {
		this.settingDemand = settingDemand;
	}

	public String getSettingUnit() {
		return settingUnit;
	}

	public void setSettingUnit(String settingUnit) {
		this.settingUnit = settingUnit;
	}

	public String getTransferAlert() {
		return transferAlert;
	}

	public void setTransferAlert(String transferAlert) {
		this.transferAlert = transferAlert;
	}

	public String getBatteryErrorAlertFVPx() {
		return batteryErrorAlertFVPx;
	}

	public void setBatteryErrorAlertFVPx(String batteryErrorAlertFVPx) {
		this.batteryErrorAlertFVPx = batteryErrorAlertFVPx;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public List<String> getPointList() {
		return pointList;
	}

	public void setPointList(List<String> pointList) {
		this.pointList = pointList;
	}

	public String getRecordDayTime() {
		return recordDayTime;
	}

	public void setRecordDayTime(String recordDayTime) {
		this.recordDayTime = recordDayTime;
	}

	public String getAlertState() {
		return alertState;
	}

	public void setAlertState(String alertState) {
		this.alertState = alertState;
	}

	public String getTargetPowerOrSettingNo() {
		return targetPowerOrSettingNo;
	}

	public void setTargetPowerOrSettingNo(String targetPowerOrSettingNo) {
		this.targetPowerOrSettingNo = targetPowerOrSettingNo;
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

	public List<String> getLoadCtrlList() {
		return loadCtrlList;
	}

	public void setLoadCtrlList(List<String> loadCtrlList) {
		this.loadCtrlList = loadCtrlList;
	}

	public String getStateFlg1() {
		return stateFlg1;
	}

	public void setStateFlg1(String stateFlg1) {
		this.stateFlg1 = stateFlg1;
	}

	public String getStateFlgAfter() {
		return stateFlgAfter;
	}

	public void setStateFlgAfter(String stateFlgAfter) {
		this.stateFlgAfter = stateFlgAfter;
	}

	public String getStateFlgAfterState5() {
		return stateFlgAfterState5;
	}

	public void setStateFlgAfterState5(String stateFlgAfterState5) {
		this.stateFlgAfterState5 = stateFlgAfterState5;
	}

}
