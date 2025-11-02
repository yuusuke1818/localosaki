package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

/**
*
* 祝日(設定) Param クラス
*
* @autho t_hayama
*
*/
public class A200052Param extends BaseParam {

	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * X月リスト
	 */
	@Size(max=12,min=12)
	private List<Map<String,String>> monthList;

	@AssertTrue
	public boolean isValidateMonthList() {
		return monthList!=null && validateList(monthList,  new HashMap<String, String>(){{
			put("nationalHoliday1", "[0-9]{1,2}"); 			// 祝日No1
			put("nationalHoliday2", "[0-9]{1,2}"); 			// 祝日No2
			put("nationalHoliday3", "[0-9]{1,2}"); 			// 祝日No3
			put("nationalHoliday4", "[0-9]{1,2}"); 			// 祝日No4
			put("nationalHoliday5", "[0-9]{1,2}"); 			// 祝日No5
			put("nationalHoliday6", "[0-9]{1,2}"); 			// 祝日No6
			put("nationalHoliday7", "[0-9]{1,2}"); 			// 祝日No7
			put("nationalHoliday8", "[0-9]{1,2}"); 			// 祝日No8
			put("nationalHoliday9", "[0-9]{1,2}"); 			// 祝日No9
			put("nationalHoliday10", "[0-9]{1,2}");			// 祝日No10
		}});
	}


	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public List<Map<String,String>> getMonthList() {
		return monthList;
	}

}
