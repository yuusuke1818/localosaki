package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
 *
 * 常時監視(取得) Param クラス
 *
 * @autho Takemura
 *
 */
public class A200031Param extends BaseParam {

	/**
	 * デマンド運転中
	 */
	private String demandInOperation;

	/**
	 * デマンド警報フラグ
	 */
	private String demandAlertFlg;

	/**
	 * RS485回線異常フラグ
	 */
	private String circuitErrorFlgRS485;

	/**
	 * LON回線異常フラグ
	 */
	private String circuitErrorFlgLON;

	/**
	 * デマンド設定変更フラグ
	 */
	private String settingDemandChangeFlg;

	/**
	 * ユニット設定変更フラグ
	 */
	private String settingUnitChangeFlg;

	/**
	 * 移報警報(冷暖房切替)
	 */
	private String transferAlert;

	/**
	 * 電池異常
	 */
	private String batteryError;

	/**
	 * 電池異常(停電補償用)
	 */
	private String batteryErrorCo;

	/**
	 * 電池異常(停電動作補償用)
	 */
	private String batteryErrorMo;

	/**
	 * 出力端末異常フラグ
	 */
	private String outputTerminalErrorFlg;

	/**
	 * 計測ポイントX点
	 */
	private List<Map<String, String>> measurePointList;

	public String getDemandInOperation() {
		return demandInOperation;
	}
	public void setDemandInOperation(String demandInOperation) {
		this.demandInOperation = demandInOperation;
	}
	public String getDemandAlertFlg() {
		return demandAlertFlg;
	}
	public void setDemandAlertFlg(String demandAlertFlg) {
		this.demandAlertFlg = demandAlertFlg;
	}
	public String getCircuitErrorFlgRS485() {
		return circuitErrorFlgRS485;
	}
	public void setCircuitErrorFlgRS485(String circuitErrorFlgRS485) {
		this.circuitErrorFlgRS485 = circuitErrorFlgRS485;
	}
	public String getCircuitErrorFlgLON() {
		return circuitErrorFlgLON;
	}
	public void setCircuitErrorFlgLON(String circuitErrorFlgLON) {
		this.circuitErrorFlgLON = circuitErrorFlgLON;
	}
	public String getSettingDemandChangeFlg() {
		return settingDemandChangeFlg;
	}
	public void setSettingDemandChangeFlg(String settingDemandChangeFlg) {
		this.settingDemandChangeFlg = settingDemandChangeFlg;
	}
	public String getSettingUnitChangeFlg() {
		return settingUnitChangeFlg;
	}
	public void setSettingUnitChangeFlg(String settingUnitChangeFlg) {
		this.settingUnitChangeFlg = settingUnitChangeFlg;
	}
	public String getTransferAlert() {
		return transferAlert;
	}
	public void setTransferAlert(String transferAlert) {
		this.transferAlert = transferAlert;
	}
	public String getBatteryError() {
		return batteryError;
	}
	public void setBatteryError(String batteryError) {
		this.batteryError = batteryError;
	}
	public String getBatteryErrorCo() {
		return batteryErrorCo;
	}
	public void setBatteryErrorCo(String batteryErrorCo) {
		this.batteryErrorCo = batteryErrorCo;
	}
	public String getBatteryErrorMo() {
		return batteryErrorMo;
	}
	public void setBatteryErrorMo(String batteryErrorMo) {
		this.batteryErrorMo = batteryErrorMo;
	}
	public String getOutputTerminalErrorFlg() {
		return outputTerminalErrorFlg;
	}
	public void setOutputTerminalErrorFlg(String outputTerminalErrorFlg) {
		this.outputTerminalErrorFlg = outputTerminalErrorFlg;
	}
	public List<Map<String, String>> getMeasurePointList() {
		return measurePointList;
	}
	public void setMeasurePointList(List<Map<String, String>> measurePointList) {
		this.measurePointList = measurePointList;
	}


}
