package jp.co.osaki.osol.mng.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* ユニット(取得) Param クラス.
*
* @autho d_yamano
*
*/
public class A200012Param extends BaseParam {

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
	 * アドレスX
	 */
	private List<Map<String,String>> addressList;

	/**
	 * 端末種別一覧リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> terminalKindList;

	/**
	 * アドレス状況一覧リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> addressSituationList;

	/**
	 * ニューロンID一覧リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> neuronsIDList;

	/**
	 * Result用のアドレスXリスト
	 *
	 * XML内にあるリストをアドレスXリストに追加して返却
	 */
	public List<Map<String,String>> getAddressList(){

		ArrayList<Map<String, String>> _addressList = new ArrayList<>();

		if(addressList != null) {
			return addressList;
		}
		for(int i=0;i<terminalKindList.size();i++) {

			Map<String, String> map = new HashMap<String, String>();
			map.put("terminalKind", terminalKindList.get(i));
			map.put("addressSituation", addressSituationList.get(i));
			map.put("neuronsID", neuronsIDList.get(i));

			_addressList.add(map);
		}


		return _addressList;
	}

	/**
	 * プロット用アナログポイント1
	 */
	private String plotAnalogPoint1;

	/**
	 * プロット用アナログポイント2
	 */
	private String plotAnalogPoint2;

	/**
	 * 記録ポイント設定No.Y
	 */
	private List<Map<String,String>> recordPointSetList;

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public void setAddressList(List<Map<String, String>> addressList) {
		this.addressList = addressList;
	}

	public String getPlotAnalogPoint1() {
		return plotAnalogPoint1;
	}

	public void setPlotAnalogPoint1(String plotAnalogPoint1) {
		this.plotAnalogPoint1 = plotAnalogPoint1;
	}

	public String getPlotAnalogPoint2() {
		return plotAnalogPoint2;
	}

	public void setPlotAnalogPoint2(String plotAnalogPoint2) {
		this.plotAnalogPoint2 = plotAnalogPoint2;
	}

	public List<Map<String, String>> getRecordPointSetList() {
		return recordPointSetList;
	}

	public void setRecordPointSetList(List<Map<String, String>> recordPointSetList) {
		this.recordPointSetList = recordPointSetList;
	}

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}

	public List<String> getTerminalKindList() {
		return terminalKindList;
	}

	public void setTerminalKindList(List<String> terminalKindList) {
		this.terminalKindList = terminalKindList;
	}

	public List<String> getAddressSituationList() {
		return addressSituationList;
	}

	public void setAddressSituationList(List<String> addressSituationList) {
		this.addressSituationList = addressSituationList;
	}

	public List<String> getNeuronsIDList() {
		return neuronsIDList;
	}

	public void setNeuronsIDList(List<String> neuronsIDList) {
		this.neuronsIDList = neuronsIDList;
	}




}
