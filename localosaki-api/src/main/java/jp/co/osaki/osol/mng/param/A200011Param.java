package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 出力端末制御(設定) Param クラス
 *
 * @author da-Yamano
 */
public class A200011Param extends BaseParam {


	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * ポート1
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port1;

	/**
	 * ポート2
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port2;

	/**
	 * ポート3
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port3;

	/**
	 * ポート4
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port4;

	/**
	 * ポート5
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port5;

	/**
	 * ポート6
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port6;

	/**
	 * ポート7
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port7;

	/**
	 * ポート8
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port8;

	/**
	 * ポート9
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port9;

	/**
	 * ポート10
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port10;

	/**
	 * ポート11
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port11;

	/**
	 * ポート12
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port12;

	/**
	 * ポート13
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port13;

	/**
	 * ポート14
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port14;

	/**
	 * ポート15
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port15;

	/**
	 * ポート16
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String port16;

	public String getPort1() {
		return port1;
	}

	public void setPort1(String port1) {
		this.port1 = port1;
	}

	public String getPort2() {
		return port2;
	}

	public void setPort2(String port2) {
		this.port2 = port2;
	}

	public String getPort3() {
		return port3;
	}

	public void setPort3(String port3) {
		this.port3 = port3;
	}

	public String getPort4() {
		return port4;
	}

	public void setPort4(String port4) {
		this.port4 = port4;
	}

	public String getPort5() {
		return port5;
	}

	public void setPort5(String port5) {
		this.port5 = port5;
	}

	public String getPort6() {
		return port6;
	}

	public void setPort6(String port6) {
		this.port6 = port6;
	}

	public String getPort7() {
		return port7;
	}

	public void setPort7(String port7) {
		this.port7 = port7;
	}

	public String getPort8() {
		return port8;
	}

	public void setPort8(String port8) {
		this.port8 = port8;
	}

	public String getPort9() {
		return port9;
	}

	public void setPort9(String port9) {
		this.port9 = port9;
	}

	public String getPort10() {
		return port10;
	}

	public void setPort10(String port10) {
		this.port10 = port10;
	}

	public String getPort11() {
		return port11;
	}

	public void setPort11(String port11) {
		this.port11 = port11;
	}

	public String getPort12() {
		return port12;
	}

	public void setPort12(String port12) {
		this.port12 = port12;
	}

	public String getPort13() {
		return port13;
	}

	public void setPort13(String port13) {
		this.port13 = port13;
	}

	public String getPort14() {
		return port14;
	}

	public void setPort14(String port14) {
		this.port14 = port14;
	}

	public String getPort15() {
		return port15;
	}

	public void setPort15(String port15) {
		this.port15 = port15;
	}

	public String getPort16() {
		return port16;
	}

	public void setPort16(String port16) {
		this.port16 = port16;
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}



}
