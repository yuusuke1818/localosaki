package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;


/**
 *
 * スケジュール(取得) Param クラス
 *
 * @author Takemura
 *
 */
public class A200004Param extends BaseParam {


	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]")
	private String settingChangeHist;

	/**
	 * ページ指定
	 */
	@Pattern(regexp="[0-9]")
	private String pageAssignment;


	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * スケジュール管理指定
	 */
	private String scheduleManageAssignment;

	/**
	 * スケジュール設定パターンXX
	 */
	private List<Map<String,String>> settingSchedulePatternList;

	/**
	 * 負荷
	 */
	private List<Map<String,Object>> loadList;

	/**
	 * MM月スケジュール設定
	 */
	private List<Map<String,String>> settingMonthScheduleList;

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getScheduleManageAssignment() {
		return scheduleManageAssignment;
	}

	public void setScheduleManageAssignment(String scheduleManageAssignment) {
		this.scheduleManageAssignment = scheduleManageAssignment;
	}

	public List<Map<String, String>> getSettingSchedulePatternList() {
		return settingSchedulePatternList;
	}

	public void setSettingSchedulePatternList(List<Map<String, String>> settingSchedulePatternList) {
		this.settingSchedulePatternList = settingSchedulePatternList;
	}

	public List<Map<String, Object>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<Map<String, Object>> loadList) {
		this.loadList = loadList;
	}

	public List<Map<String, String>> getSettingMonthScheduleList() {
		return settingMonthScheduleList;
	}

	public void setSettingMonthScheduleList(List<Map<String, String>> settingMonthScheduleList) {
		this.settingMonthScheduleList = settingMonthScheduleList;
	}

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

	public String getPageAssignment() {
		return pageAssignment;
	}

	public void setPageAssignment(String pageAssignment) {
		this.pageAssignment = pageAssignment;
	}

}
