package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
*
* イベント制御履歴(FVP)(取得) Param クラス
*
* @autho da_yamano
*
*/
public class A200035Param extends BaseParam {

	/**
	 * 制御負荷指定
	 */
	@NotNull
	private String loadCtrlAssignment;

	/**
	 * イベント制御履歴X
	 */
	private List<Map<String,String>> eventCtrlHistList;

	/**
	 * イベント制御履歴EαX
	 */
	private List<Map<String,Object>> eventCtrlHistEaList;


	public String getLoadCtrlAssignment() {
		return loadCtrlAssignment;
	}

	public void setLoadCtrlAssignment(String loadCtrlAssignment) {
		this.loadCtrlAssignment = loadCtrlAssignment;
	}

	public List<Map<String, String>> getEventCtrlHistList() {
		return eventCtrlHistList;
	}

	public void setEventCtrlHistList(List<Map<String, String>> eventCtrlHistList) {
		this.eventCtrlHistList = eventCtrlHistList;
	}

	public List<Map<String,Object>> getEventCtrlHistEaList() {
		return eventCtrlHistEaList;
	}

	public void setEventCtrlHistEaList(List<Map<String,Object>> eventCtrlHistEaList) {
		this.eventCtrlHistEaList = eventCtrlHistEaList;
	}

}
