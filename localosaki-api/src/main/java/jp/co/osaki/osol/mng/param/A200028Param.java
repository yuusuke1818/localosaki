package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
 * 温度制御履歴(取得) Paramクラス
 *
 * @author da-yamano
 */
public class A200028Param extends BaseParam {

	/**
	 * 設定変更履歴
	 */
	@Pattern(regexp="[0-9]{0,1}")
	private String settingChangeHist;

	/**
	 * 負荷制御履歴X
	 */
	private List<Map<String,String>> loadCtrlHistList;

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

	public List<Map<String, String>> getLoadCtrlHistList() {
		return loadCtrlHistList;
	}

	public void setLoadCtrlHistList(List<Map<String, String>> loadCtrlHistList) {
		this.loadCtrlHistList = loadCtrlHistList;
	}



}
