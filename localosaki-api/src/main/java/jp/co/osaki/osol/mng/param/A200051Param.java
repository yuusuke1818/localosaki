package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* 祝日(取得) param クラス
*
* @autho t_hayama
*
*/
public class A200051Param extends BaseParam {

	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]")
	private String settingChangeHist;

	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * X月リスト
	 *
	 */
	public  List<Map<String,String>> monthList;


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

	public List<Map<String, String>> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<Map<String, String>> monthList) {
		this.monthList = monthList;
	}

}
