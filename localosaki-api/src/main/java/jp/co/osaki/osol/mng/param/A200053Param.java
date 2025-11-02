package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* ユニット(取得) Eα Param クラス
*
* @autho t_hayama
*
*/
public class A200053Param extends BaseParam {

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
	 * アドレスX
	 */
	private List<Map<String, String>> addressList;


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

	public List<Map<String, String>> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Map<String, String>> addressList) {
		this.addressList = addressList;
	}

}
