package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * FVPα用追加基本(設定) Param クラス
 *
 * @author t_sakamoto
 *
 */
public class A200019Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)(週含む)
	 */
	private String settingDate;

	/**
	 * 出力機能選択
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String outputFunctionSelect;

	/**
	 * 時限パルス出力
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String timeLimitPulseOutput;

	/**
	 * 時限パルス出力遅延
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,2}")
	private String timeLimitPulseOutputLag;

	/**
	 * 時限パルス出力時間
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,3}")
	private String timeLimitPulseOutputTime;

	/**
	 * 時限パルス出力時間帯開始時間
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,2}")
	private String timeLimitPulseOutputStart;

	/**
	 * 時限パルス出力時間帯終了時間
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,2}")
	private String timeLimitPulseOutputEnd;

	/**
	 * CO2換算係数
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,3}")
	private String convartCoefficientCO2;

	/**
	 * 電力基本料金
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,4}")
	private String powerRate;

	/**
	 * 使用料金単価
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,4}")
	private String useUnitCost;

	/**
	 * RS485待ち時間
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,4}")
	private String waitTimeRS485;

	/**
	 * ブザー
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String buzzer;

	/**
	 * 無計量警報
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String nonMeasureAlert;

	/**
	 * スケジュール管理指定
	 */
	private String scheduleCtrl;

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getOutputFunctionSelect() {
		return outputFunctionSelect;
	}

	public void setOutputFunctionSelect(String outputFunctionSelect) {
		this.outputFunctionSelect = outputFunctionSelect;
	}

	public String getTimeLimitPulseOutput() {
		return timeLimitPulseOutput;
	}

	public void setTimeLimitPulseOutput(String timeLimitPulseOutput) {
		this.timeLimitPulseOutput = timeLimitPulseOutput;
	}

	public String getTimeLimitPulseOutputLag() {
		return timeLimitPulseOutputLag;
	}

	public void setTimeLimitPulseOutputLag(String timeLimitPulseOutputLag) {
		this.timeLimitPulseOutputLag = timeLimitPulseOutputLag;
	}

	public String getTimeLimitPulseOutputTime() {
		return timeLimitPulseOutputTime;
	}

	public void setTimeLimitPulseOutputTime(String timeLimitPulseOutputTime) {
		this.timeLimitPulseOutputTime = timeLimitPulseOutputTime;
	}

	public String getTimeLimitPulseOutputStart() {
		return timeLimitPulseOutputStart;
	}

	public void setTimeLimitPulseOutputStart(String timeLimitPulseOutputStart) {
		this.timeLimitPulseOutputStart = timeLimitPulseOutputStart;
	}

	public String getTimeLimitPulseOutputEnd() {
		return timeLimitPulseOutputEnd;
	}

	public void setTimeLimitPulseOutputEnd(String timeLimitPulseOutputEnd) {
		this.timeLimitPulseOutputEnd = timeLimitPulseOutputEnd;
	}

	public String getConvartCoefficientCO2() {
		return convartCoefficientCO2;
	}

	public void setConvartCoefficientCO2(String convartCoefficientCO2) {
		this.convartCoefficientCO2 = convartCoefficientCO2;
	}

	public String getPowerRate() {
		return powerRate;
	}

	public void setPowerRate(String powerRate) {
		this.powerRate = powerRate;
	}

	public String getUseUnitCost() {
		return useUnitCost;
	}

	public void setUseUnitCost(String useUnitCost) {
		this.useUnitCost = useUnitCost;
	}

	public String getWaitTimeRS485() {
		return waitTimeRS485;
	}

	public void setWaitTimeRS485(String waitTimeRS485) {
		this.waitTimeRS485 = waitTimeRS485;
	}

	public String getBuzzer() {
		return buzzer;
	}

	public void setBuzzer(String buzzer) {
		this.buzzer = buzzer;
	}

	public String getNonMeasureAlert() {
		return nonMeasureAlert;
	}

	public void setNonMeasureAlert(String nonMeasureAlert) {
		this.nonMeasureAlert = nonMeasureAlert;
	}

	public String getScheduleCtrl() {
		return scheduleCtrl;
	}

	public void setScheduleCtrl(String scheduleCtrl) {
		this.scheduleCtrl = scheduleCtrl;
	}

}
