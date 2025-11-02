package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;

/**
*
* アンサーバック設定(設定) Param クラス.
*
* @autho Takemura
*
*/
public class A200023Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)(週含む)
	 */
	private String settingDate;

	/**
	 * 負荷情報
	 */
	private List<Map<String, String>> loadInfoList;

	@AssertTrue
	public boolean isValidateLoadInfoList() {
		return loadInfoList!=null && validateList(loadInfoList,  new HashMap<String, String>(){{
			put("loadPoint",  "[0-9]{1,2}");		// 状態入力ポイント
			put("loadBlockDirection", "[0-9]");		// 遮断状態方向

		}});
	}

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
}
