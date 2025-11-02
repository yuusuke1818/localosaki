package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 *
 * スケジュール(取得) Eα Param クラス
 *
 * @author t_hayama
 *
 */
public class A200059Param extends BaseParam {

	/**
	 * スケジュール制御情報
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String scheduleControlInfo;

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
	 * スケジュール制御
	 */
	private String scheduleControl;

	/**
	 * 負荷
	 */
	private List<Map<String,Object>> loadList;

	/**
	 * MM月スケジュール設定
	 */
	private List<Map<String,String>> settingMonthScheduleList;


	public String getScheduleControlInfo() {
		return scheduleControlInfo;
	}

	public void setScheduleControlInfo(String scheduleControlInfo) {
		this.scheduleControlInfo = scheduleControlInfo;
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

	public String getScheduleControl() {
		return scheduleControl;
	}

	public void setScheduleControl(String scheduleControl) {
		this.scheduleControl = scheduleControl;
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

}
