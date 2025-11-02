package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
*
* 無線温度ロガー履歴(取得) Param クラス
*
* @author t_sakamoto
*
*/
public class A200037Param extends BaseParam {

	/**
	 * 無線温度ロガー履歴No.X
	 */
	private List<Map<String, String>> temperatureLoggerHistList;

	public List<Map<String, String>> getTemperatureLoggerHistList() {
		return temperatureLoggerHistList;
	}

	public void setTemperatureLoggerHistList(List<Map<String, String>> temperatureLoggerHistList) {
		this.temperatureLoggerHistList = temperatureLoggerHistList;
	}

}
