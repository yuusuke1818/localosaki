package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
*
* RS485回線異常履歴(取得) Param クラス.
*
* @author t_sakamoto
*
*/
public class A200036Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)
	 */
	private String settingDate;

	/**
	 *  LON回線異常履歴No.X
	 *
	 *  機器からのレスポンス一時格納用リスト
	 */
	private List<Map<String, String>> circuitErrorHistListLON;

	/**
	 *  RS485回線異常履歴No.X
	 *
	 *  機器からのレスポンス一時格納用リスト
	 */
	private List<Map<String, String>> circuitErrorHistListRS485;

	/**
	 *  回線異常履歴No.X
	 */
	private List<Map<String, String>> circuitErrorHistList;

	public String getSettingDate() {
		return settingDate;
	}
	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}
	public List<Map<String, String>> getCircuitErrorHistListLON() {
		return circuitErrorHistListLON;
	}
	public void setCircuitErrorHistListLON(List<Map<String, String>> circuitErrorHistListLON) {
		this.circuitErrorHistListLON = circuitErrorHistListLON;
	}
	public List<Map<String, String>> getCircuitErrorHistListRS485() {
		return circuitErrorHistListRS485;
	}
	public void setCircuitErrorHistListRS485(List<Map<String, String>> circuitErrorHistListRS485) {
		this.circuitErrorHistListRS485 = circuitErrorHistListRS485;
	}

	public List<Map<String, String>> getCircuitErrorHistList() {
		// 機器からのレスポンスで有効なリストを回線異常履歴No.Xとして返却
		if(circuitErrorHistListLON != null) {
			return circuitErrorHistListLON;
		}

		else if(circuitErrorHistListRS485 != null) {
			return circuitErrorHistListRS485;
		}

		return circuitErrorHistList;
	}
	public void setCircuitErrorHistList(List<Map<String, String>> circuitErrorHistList) {
		this.circuitErrorHistList = circuitErrorHistList;
	}






}
