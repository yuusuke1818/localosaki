package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
 *
 * 手動負荷制御(取得) Param クラス.
 *
 * @autho da_yamano
 *
 */
public class A200008Param extends BaseParam {

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
	 * 負荷情報
	 */
	private List<Map<String,String>> loadInfoList;

	/**
	 * 負荷情報を結合する際に使うリスト
	 */
	private List<Map<String,String>> _loadInfoList;

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
	private String errorManualCtrlState;


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

	}//_loadInfoListがnullでなければloadInfoListに_loadInfoListを追加

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

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}



}
