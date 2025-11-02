package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
*
* 温度制御(設定) Param クラス
*
* @author da_yamano
*
*/
public class A200003Param extends BaseParam {

	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * 温湿度制御
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String ctrlTH;

	/**
	 * 温湿度制御条件
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String ctrlTermsTH;

	/**
	 * 冷暖房切替用4点端末器アドレス
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String terminalEquipaddressCW;

	/**
	 *デマンド連動制御条件(超過)
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandGangCtrlTermsEx;

	/**
	 * デマンド連動制御条件(注意)
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandGangCtrlTermsCa;

	/**
	 * デマンド連動制御条件(遮断)
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandGangCtrlTermsBl;

	/**
	 * デマンド連動制御条件(限界)
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandGangCtrlTermsLi;

	/**
	 *デマンド連動制御条件(高負荷)
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandGangCtrlTermsHi;


	/**
	 * 温湿度制御時間帯No.X
	 */
	@NotNull
	@Size(max=6,min=6)
	private List<Map<String, String>> ctrlTimeZoneTHList;

	/**
	 * 制御ポートNo.Y設定
	 */
	@NotNull
	@Size(max=16,min=16)
	private List<Map<String, String>> settingCtrlPortList;

	@AssertTrue
	public boolean isValidateCtrlTimeZoneTHList() {
		return ctrlTimeZoneTHList!=null && validateList(ctrlTimeZoneTHList,  new HashMap<String, String>(){{
			put("startHour",  "[0-9]{1,2}");		// 開始時間時
			put("startMinute", "[0-9]{1,2}");		// 開始時間分
			put("endHour",     "[0-9]{1,2}");		// 終了時間時
			put("endMinute",   "[0-9]{1,2}");		// 終了時間分
		}});
	}//List内のバリデーションチェックを行うメソッド


	@AssertTrue
	public boolean isValidateSettingCtrlPortList() {
		return settingCtrlPortList!=null && validateList(settingCtrlPortList,  new HashMap<String, String>(){{
			put("ctrlPermissionTH",  "[0-9]");				// 温湿度制御許可
//	          put("demandGangCtrlPermission", "[0-9]"); 	// デマンド連動制御許可 ※機種依存項目
//	          put("switchChoiceCW",     "[0-9]"); 			// 冷暖房切替SW選択 	※機種依存項目
			put("sensoraddressTH",   "[0-9]{1,2}");			// 温湿度センサアドレス
			put("temperatureMax",   "[0-9]{1,2}");			// 温度上限
			put("temperatureMin",   "[0-9]{1,2}");			// 温度下限
			put("humidityMax",   "[0-9]{1,2}");				// 湿度上限
			put("humidityMin",   "[0-9]{1,2}");				// 湿度下限
			put("temperatureReturnWidth",   "[0-9]{1,3}");	// 温度復帰幅
			put("humidityReturnWidth",   "[0-9]{1,3}");		// 湿度復帰幅
			put("loadBlockHoldTime",   "[0-9]{1,2}");		// 負荷遮断保持時間分
			put("loadReturnHoldTime",   "[0-9]{1,2}");		// 負荷復帰保持時間分

		}});
	}

	public List<Map<String, String>> getCtrlTimeZoneTHList() {
		return ctrlTimeZoneTHList;
	}

	public void setCtrlTimeZoneTHList(List<Map<String, String>> ctrlTimeZoneTHList) {
		this.ctrlTimeZoneTHList = ctrlTimeZoneTHList;
	}

	public List<Map<String, String>> getSettingCtrlPortList() {
		return settingCtrlPortList;
	}

	public void setSettingCtrlPortList(List<Map<String, String>> settingCtrlPortList) {
		this.settingCtrlPortList = settingCtrlPortList;
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getCtrlTH() {
		return ctrlTH;
	}

	public void setCtrlTH(String ctrlTH) {
		this.ctrlTH = ctrlTH;
	}

	public String getCtrlTermsTH() {
		return ctrlTermsTH;
	}

	public void setCtrlTermsTH(String ctrlTermsTH) {
		this.ctrlTermsTH = ctrlTermsTH;
	}

	public String getTerminalEquipaddressCW() {
		return terminalEquipaddressCW;
	}

	public void setTerminalEquipaddressCW(String terminalEquipaddressCW) {
		this.terminalEquipaddressCW = terminalEquipaddressCW;
	}

	public String getDemandGangCtrlTermsEx() {
		return demandGangCtrlTermsEx;
	}

	public void setDemandGangCtrlTermsEx(String demandGangCtrlTermsEx) {
		this.demandGangCtrlTermsEx = demandGangCtrlTermsEx;
	}

	public String getDemandGangCtrlTermsCa() {
		return demandGangCtrlTermsCa;
	}

	public void setDemandGangCtrlTermsCa(String demandGangCtrlTermsCa) {
		this.demandGangCtrlTermsCa = demandGangCtrlTermsCa;
	}

	public String getDemandGangCtrlTermsBl() {
		return demandGangCtrlTermsBl;
	}

	public void setDemandGangCtrlTermsBl(String demandGangCtrlTermsBl) {
		this.demandGangCtrlTermsBl = demandGangCtrlTermsBl;
	}

	public String getDemandGangCtrlTermsLi() {
		return demandGangCtrlTermsLi;
	}

	public void setDemandGangCtrlTermsLi(String demandGangCtrlTermsLi) {
		this.demandGangCtrlTermsLi = demandGangCtrlTermsLi;
	}

	public String getDemandGangCtrlTermsHi() {
		return demandGangCtrlTermsHi;
	}

	public void setDemandGangCtrlTermsHi(String demandGangCtrlTermsHi) {
		this.demandGangCtrlTermsHi = demandGangCtrlTermsHi;
	}

}
