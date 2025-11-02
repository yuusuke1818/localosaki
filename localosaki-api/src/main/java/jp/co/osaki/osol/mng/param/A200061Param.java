package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;


/**
 *
 * スケジュールパターン(取得) Param クラス
 *
 * @author t_hayama
 *
 */
public class A200061Param extends BaseParam {

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
	 * スケジュール設定パターンリスト
	 */
	private List<Map<String, Object>> settingSchedulePatternList;

	/**
	 * 時間帯リスト
	 */
	private List<Map<String, String>> timeZoneList;


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

	public List<Map<String, Object>> getSettingSchedulePatternList() {
		return settingSchedulePatternList;
	}

	public void setSettingSchedulePatternList(List<Map<String, Object>> settingSchedulePatternList) {
		this.settingSchedulePatternList = settingSchedulePatternList;
	}

	public List<Map<String, String>> getTimeZoneList() {
		return timeZoneList;
	}

	public void setTimeZoneList(List<Map<String, String>> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}

}
