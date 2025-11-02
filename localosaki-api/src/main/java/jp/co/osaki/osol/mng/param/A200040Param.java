package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
*
* 最大デマンド取得 Param クラス
*
* @author da_yamano
*
*/
public class A200040Param extends BaseParam {

	/**
	 * データ履歴
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,3}")
	private String dataHist;

	/**
	 * 計測日時
	 */
	private String recordDayTime;

	/**
	 * 初期電力
	 */
	private String initialPower;

	/**
	 * 警報ロック時間
	 */
	private String alertLockTime;

	/**
	 * 複数負荷遮断時間
	 */
	private String multipleLoadBlTime;

	/**
	 * 目標電力
	 */
	private String targetPower;

	/**
	 * 日最大デマンド
	 */
	private String dayMaxDemand;

	/**
	 * 無制御時平均電力
	 */
	private String nonCtrlAvePower;

	/**
	 * 制御遮断電力
	 */
	private String ctrlBlPower;


	/**
	 * プロットデータPoint01
	 */
	private String plotDataPoint1;


	/**
	 * プロットデータPoint02
	 */
	private String plotDataPoint2;


	/**
	 * プロットデータPoint03
	 */
	private String plotDataPoint3;


	/**
	 * プロットデータPoint04
	 */
	private String plotDataPoint4;


	/**
	 * プロットデータPoint05
	 */
	private String plotDataPoint5;


	/**
	 * プロットデータPoint06
	 */
	private String plotDataPoint6;


	/**
	 * プロットデータPoint07
	 */
	private String plotDataPoint7;


	/**
	 * プロットデータPoint08
	 */
	private String plotDataPoint8;


	/**
	 * プロットデータPoint09
	 */
	private String plotDataPoint9;


	/**
	 * プロットデータPoint10
	 */
	private String plotDataPoint10;


	/**
	 * プロットデータPoint11
	 */
	private String plotDataPoint11;


	/**
	 * プロットデータPoint12
	 */
	private String plotDataPoint12;


	/**
	 * プロットデータPoint13
	 */
	private String plotDataPoint13;


	/**
	 * プロットデータPoint14
	 */
	private String plotDataPoint14;


	/**
	 * プロットデータPoint15
	 */
	private String plotDataPoint15;


	/**
	 * プロットデータPoint16
	 */
	private String plotDataPoint16;


	/**
	 * プロットデータPoint17
	 */
	private String plotDataPoint17;


	/**
	 * プロットデータPoint18
	 */
	private String plotDataPoint18;


	/**
	 * プロットデータPoint19
	 */
	private String plotDataPoint19;


	/**
	 * プロットデータPoint20
	 */
	private String plotDataPoint20;


	/**
	 * プロットデータPoint21
	 */
	private String plotDataPoint21;


	/**
	 * プロットデータPoint22
	 */
	private String plotDataPoint22;


	/**
	 * プロットデータPoint23
	 */
	private String plotDataPoint23;


	/**
	 * プロットデータPoint24
	 */
	private String plotDataPoint24;


	/**
	 * プロットデータPoint25
	 */
	private String plotDataPoint25;


	/**
	 * プロットデータPoint26
	 */
	private String plotDataPoint26;


	/**
	 * プロットデータPoint27
	 */
	private String plotDataPoint27;


	/**
	 * プロットデータPoint28
	 */
	private String plotDataPoint28;


	/**
	 * プロットデータPoint29
	 */
	private String plotDataPoint29;


	/**
	 * プロットデータPoint30
	 */
	private String plotDataPoint30;


	public String getRecordDayTime() {
		return recordDayTime;
	}


	public void setRecordDayTime(String recordDayTime) {
		this.recordDayTime = recordDayTime;
	}


	public String getInitialPower() {
		return initialPower;
	}


	public void setInitialPower(String initialPower) {
		this.initialPower = initialPower;
	}


	public String getAlertLockTime() {
		return alertLockTime;
	}


	public void setAlertLockTime(String alertLockTime) {
		this.alertLockTime = alertLockTime;
	}


	public String getMultipleLoadBlTime() {
		return multipleLoadBlTime;
	}


	public void setMultipleLoadBlTime(String multipleLoadBlTime) {
		this.multipleLoadBlTime = multipleLoadBlTime;
	}


	public String getTargetPower() {
		return targetPower;
	}


	public void setTargetPower(String targetPower) {
		this.targetPower = targetPower;
	}


	public String getDayMaxDemand() {
		return dayMaxDemand;
	}


	public void setDayMaxDemand(String dayMaxDemand) {
		this.dayMaxDemand = dayMaxDemand;
	}


	public String getNonCtrlAvePower() {
		return nonCtrlAvePower;
	}


	public void setNonCtrlAvePower(String nonCtrlAvePower) {
		this.nonCtrlAvePower = nonCtrlAvePower;
	}


	public String getCtrlBlPower() {
		return ctrlBlPower;
	}


	public void setCtrlBlPower(String ctrlBlPower) {
		this.ctrlBlPower = ctrlBlPower;
	}


	public String getPlotDataPoint1() {
		return plotDataPoint1;
	}


	public void setPlotDataPoint1(String plotDataPoint1) {
		this.plotDataPoint1 = plotDataPoint1;
	}


	public String getPlotDataPoint2() {
		return plotDataPoint2;
	}


	public void setPlotDataPoint2(String plotDataPoint2) {
		this.plotDataPoint2 = plotDataPoint2;
	}


	public String getPlotDataPoint3() {
		return plotDataPoint3;
	}


	public void setPlotDataPoint3(String plotDataPoint3) {
		this.plotDataPoint3 = plotDataPoint3;
	}


	public String getPlotDataPoint4() {
		return plotDataPoint4;
	}


	public void setPlotDataPoint4(String plotDataPoint4) {
		this.plotDataPoint4 = plotDataPoint4;
	}


	public String getPlotDataPoint5() {
		return plotDataPoint5;
	}


	public void setPlotDataPoint5(String plotDataPoint5) {
		this.plotDataPoint5 = plotDataPoint5;
	}


	public String getPlotDataPoint6() {
		return plotDataPoint6;
	}


	public void setPlotDataPoint6(String plotDataPoint6) {
		this.plotDataPoint6 = plotDataPoint6;
	}


	public String getPlotDataPoint7() {
		return plotDataPoint7;
	}


	public void setPlotDataPoint7(String plotDataPoint7) {
		this.plotDataPoint7 = plotDataPoint7;
	}


	public String getPlotDataPoint8() {
		return plotDataPoint8;
	}


	public void setPlotDataPoint8(String plotDataPoint8) {
		this.plotDataPoint8 = plotDataPoint8;
	}


	public String getPlotDataPoint9() {
		return plotDataPoint9;
	}


	public void setPlotDataPoint9(String plotDataPoint9) {
		this.plotDataPoint9 = plotDataPoint9;
	}


	public String getPlotDataPoint10() {
		return plotDataPoint10;
	}


	public void setPlotDataPoint10(String plotDataPoint10) {
		this.plotDataPoint10 = plotDataPoint10;
	}


	public String getPlotDataPoint11() {
		return plotDataPoint11;
	}


	public void setPlotDataPoint11(String plotDataPoint11) {
		this.plotDataPoint11 = plotDataPoint11;
	}


	public String getPlotDataPoint12() {
		return plotDataPoint12;
	}


	public void setPlotDataPoint12(String plotDataPoint12) {
		this.plotDataPoint12 = plotDataPoint12;
	}


	public String getPlotDataPoint13() {
		return plotDataPoint13;
	}


	public void setPlotDataPoint13(String plotDataPoint13) {
		this.plotDataPoint13 = plotDataPoint13;
	}


	public String getPlotDataPoint14() {
		return plotDataPoint14;
	}


	public void setPlotDataPoint14(String plotDataPoint14) {
		this.plotDataPoint14 = plotDataPoint14;
	}


	public String getPlotDataPoint15() {
		return plotDataPoint15;
	}


	public void setPlotDataPoint15(String plotDataPoint15) {
		this.plotDataPoint15 = plotDataPoint15;
	}


	public String getPlotDataPoint16() {
		return plotDataPoint16;
	}


	public void setPlotDataPoint16(String plotDataPoint16) {
		this.plotDataPoint16 = plotDataPoint16;
	}


	public String getPlotDataPoint17() {
		return plotDataPoint17;
	}


	public void setPlotDataPoint17(String plotDataPoint17) {
		this.plotDataPoint17 = plotDataPoint17;
	}


	public String getPlotDataPoint18() {
		return plotDataPoint18;
	}


	public void setPlotDataPoint18(String plotDataPoint18) {
		this.plotDataPoint18 = plotDataPoint18;
	}


	public String getPlotDataPoint19() {
		return plotDataPoint19;
	}


	public void setPlotDataPoint19(String plotDataPoint19) {
		this.plotDataPoint19 = plotDataPoint19;
	}


	public String getPlotDataPoint20() {
		return plotDataPoint20;
	}


	public void setPlotDataPoint20(String plotDataPoint20) {
		this.plotDataPoint20 = plotDataPoint20;
	}


	public String getPlotDataPoint21() {
		return plotDataPoint21;
	}


	public void setPlotDataPoint21(String plotDataPoint21) {
		this.plotDataPoint21 = plotDataPoint21;
	}


	public String getPlotDataPoint22() {
		return plotDataPoint22;
	}


	public void setPlotDataPoint22(String plotDataPoint22) {
		this.plotDataPoint22 = plotDataPoint22;
	}


	public String getPlotDataPoint23() {
		return plotDataPoint23;
	}


	public void setPlotDataPoint23(String plotDataPoint23) {
		this.plotDataPoint23 = plotDataPoint23;
	}


	public String getPlotDataPoint24() {
		return plotDataPoint24;
	}


	public void setPlotDataPoint24(String plotDataPoint24) {
		this.plotDataPoint24 = plotDataPoint24;
	}


	public String getPlotDataPoint25() {
		return plotDataPoint25;
	}


	public void setPlotDataPoint25(String plotDataPoint25) {
		this.plotDataPoint25 = plotDataPoint25;
	}


	public String getPlotDataPoint26() {
		return plotDataPoint26;
	}


	public void setPlotDataPoint26(String plotDataPoint26) {
		this.plotDataPoint26 = plotDataPoint26;
	}


	public String getPlotDataPoint27() {
		return plotDataPoint27;
	}


	public void setPlotDataPoint27(String plotDataPoint27) {
		this.plotDataPoint27 = plotDataPoint27;
	}


	public String getPlotDataPoint28() {
		return plotDataPoint28;
	}


	public void setPlotDataPoint28(String plotDataPoint28) {
		this.plotDataPoint28 = plotDataPoint28;
	}


	public String getPlotDataPoint29() {
		return plotDataPoint29;
	}


	public void setPlotDataPoint29(String plotDataPoint29) {
		this.plotDataPoint29 = plotDataPoint29;
	}


	public String getPlotDataPoint30() {
		return plotDataPoint30;
	}


	public void setPlotDataPoint30(String plotDataPoint30) {
		this.plotDataPoint30 = plotDataPoint30;
	}


	public String getDataHist() {
		return dataHist;
	}

	public void setDataHist(String dataHist) {
		this.dataHist = dataHist;
	}



}
