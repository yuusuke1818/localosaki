package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
*
* 手動負荷制御(設定) Param クラス
*
* @autho da_yamano
*
*/
public class A200009Param extends BaseParam {

	/**
	 * 取得日時
	 */
	private String settingDate;
	/**
	 * 負荷情報
	 */
	private List<Map<String,String>> loadInfoList;

	/**
	 * 負荷情報リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<Map<String,String>> _loadInfoList;

	/**
	 * 負荷情報リスト(追加)
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<Map<String,String>> addLoadInfoList;

	/**
	 * 注意警報の手動制御状態
	 */
	private String cautionManualCtrlState;

	/**
	 * 遮断警報の手動制御状態
	 */
	private String blockManualCtrlState;

	/**
	 * 限界警報の手動制御状態
	 */
	private String limitManualCtrlState;

	/**
	 * 異常警報の手動制御状態
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String errorManualCtrlState;

	@AssertTrue
	public boolean isValidateLoadInfoList() {
		return loadInfoList!=null && validateList(loadInfoList,  new HashMap<String, String>(){{
			put("manualCtrlState",  "[0-9]");		// 手動制御状態
		}});
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public List<Map<String, String>> getLoadInfoList() {
		if(_loadInfoList != null) {
		loadInfoList.addAll(_loadInfoList);
		}
		return loadInfoList;

	}//負荷情報リストが値がでなければ負荷情報に負荷情報リストを追加

	public void setLoadInfoList(List<Map<String, String>> loadInfoList) {
		this.loadInfoList = loadInfoList;
	}

	public String getCautionManualCtrlState() {
		return cautionManualCtrlState;
	}

	public void setCautionManualCtrlState(String cautionManualCtrlState) {
		this.cautionManualCtrlState = cautionManualCtrlState;
	}

	public String getBlockManualCtrlState() {
		return blockManualCtrlState;
	}

	public void setBlockManualCtrlState(String blockManualCtrlState) {
		this.blockManualCtrlState = blockManualCtrlState;
	}

	public String getLimitManualCtrlState() {
		return limitManualCtrlState;
	}

	public void setLimitManualCtrlState(String limitManualCtrlState) {
		this.limitManualCtrlState = limitManualCtrlState;
	}

	public String getErrorManualCtrlState() {
		return errorManualCtrlState;
	}

	public void setErrorManualCtrlState(String errorManualCtrlState) {
		this.errorManualCtrlState = errorManualCtrlState;
	}

	public List<Map<String, String>> get_loadInfoList() {
		return _loadInfoList;
	}

	public void set_loadInfoList(List<Map<String, String>> _loadInfoList) {
		this._loadInfoList = _loadInfoList;
	}

	public List<Map<String, String>> getAddLoadInfoList() {
		List<Map<String, String>> addLoadInfoList = loadInfoList.subList(12, 36);

		return addLoadInfoList;
	}//負荷情報の分裂部を負荷情報リスト(追加)に置き換え

	public void setAddLoadInfoList(List<Map<String, String>> addLoadInfoList) {
		this.addLoadInfoList = addLoadInfoList;
	}

}
