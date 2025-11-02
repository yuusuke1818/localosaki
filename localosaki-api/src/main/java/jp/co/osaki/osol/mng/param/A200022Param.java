package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
 * アンサーバック設定(取得) Paramクラス
 *
 * @author Takemura
 */
public class A200022Param extends BaseParam {

	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]")
	private String settingChangeHist;

	/**
	 * 取得日時(設定変更日時)(週含む)
	 */
	private String settingDate;

	/**
	 * 負荷情報
	 */
	private List<Map<String, String>> loadInfoList;

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public List<Map<String, String>> getLoadInfoList() {
		return loadInfoList;

	}

	public void setLoadInfoList(List<Map<String, String>> loadInfoList) {
		this.loadInfoList = loadInfoList;
	}

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

}
