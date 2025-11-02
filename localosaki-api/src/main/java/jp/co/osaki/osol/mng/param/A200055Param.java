package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 *
 * 計測ポイント(取得) Param クラス
 *
 * @author t_hayama
 *
 */
public class A200055Param extends BaseParam {

	/**
	 * 計測ポイント情報
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String measurePointInfo;

	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]")
	private String settingChangeHist;

	/**
	 * 設定変更日時
	 */
	private String settingDate;

	/**
	 * 計測ポイント情報
	 */
	private List<Map<String, Object>> measurePointInfoList;

	/**
	 * プロット用ポイント No.1
	 */
	private String plotPoint1;

	/**
	 * プロット用ポイント No.2
	 */
	private String plotPoint2;


	public String getMeasurePointInfo() {
		return measurePointInfo;
	}

	public void setMeasurePointInfo(String measurePointInfo) {
		this.measurePointInfo = measurePointInfo;
	}

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public List<Map<String, Object>> getMeasurePointInfoList() {
		return measurePointInfoList;
	}

	public void setMeasurePointInfoList(List<Map<String, Object>> measurePointInfoList) {
		this.measurePointInfoList = measurePointInfoList;
	}

	public String getPlotPoint1() {
		return plotPoint1;
	}

	public void setPlotPoint1(String plotPoint1) {
		this.plotPoint1 = plotPoint1;
	}

	public String getPlotPoint2() {
		return plotPoint2;
	}

	public void setPlotPoint2(String plotPoint2) {
		this.plotPoint2 = plotPoint2;
	}

}
