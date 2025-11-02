package jp.co.osaki.osol.mng.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * ユニット(設定) Param クラス
 *
 * @autho d_yamano
 *
 */
public class A200013Param extends BaseParam {

	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * アドレスX
	 */
	@NotNull
	@Size(max=20,min=20)
	private List<Map<String, String>> addressList;

	@AssertTrue
	public boolean isValidateAddressList() {
		return addressList != null && validateList(addressList, new HashMap<String, String>() {
			{
				put("terminalKind", "[a-zA-Z0-9]");		// 端末種別一覧
				put("addressSituation", "[0-9]");		// アドレス状況一覧
				put("neuronsID", "[a-zA-Z0-9]{1,12}");		// ニューロンID
			}
		});
	}

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
	@NotNull
	@Pattern(regexp = "[0-9]{1,2}")
	private String plotAnalogPoint1;

	/**
	 * プロット用アナログポイント2
	 */
	@NotNull
	@Pattern(regexp = "[0-9]{1,2}")
	private String plotAnalogPoint2;

	/**
	 * 記録ポイント設定No.Y
	 */
	private List<Map<String, String>> recordPointSetList;

	@AssertTrue
	public boolean isValidateRecordPointSetList() {
		return recordPointSetList!=null && validateList(recordPointSetList,  new HashMap<String, String>(){{
			put("terminalAddress","[0-9]{1,2}");		// 端末アドレス
			put("internalAddress","[0-9]{1,2}");        // 内部アドレス
			put("pointType","[0-9]{1,2}");              // ポイント種別
			put("sensorType","[0-9]{1,2}");             // センサ種別
//	          put("pulseWeight","[0-9]{1,6}");			// パルス重み【無線周波数チャンネル】          ※ 機種依存項目
//	          put("headScale","[-+0-9]{1,6}");          // ヘッドスケール【ポーリング周期(秒)】        ※ 機種依存項目
//	          put("bottomScale","[-+0-9]{1,6}");        // ボトムスケール【無線ID下4桁(ID:0001～FFFF)】※ 機種依存項目
//	          put("function1","[0-9]{1,6}");            // 機能1                                       ※ 機種依存項目
//	          put("function2","[-+0-9]{1,6}");          // 機能2                                       ※ 機種依存項目
//	          put("function3","[-+0-9]{1,6}");          // 機能3                                       ※ 機種依存項目


		}});
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

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

}
