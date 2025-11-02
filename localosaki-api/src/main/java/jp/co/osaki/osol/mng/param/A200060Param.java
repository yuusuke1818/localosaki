package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * スケジュール(設定) Eα Param クラス
 *
 * @autho t_hayama
 *
 */
public class A200060Param extends BaseParam {

	/**
	 * スケジュール制御情報
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String scheduleControlInfo;

	/**
	 * スケジュール制御
	 */
	@Pattern(regexp="[0-9]")
	private String scheduleControl;

	/**
	 * 負荷
	 */
	@NotNull
	private List<Map<String, Object>> loadList;


	@AssertTrue
	public boolean isValidateLoadList() {
		return loadList != null && validateSettingMonthScheduleList(loadList, new HashMap<String, String>() {
			{
				put("settingMonthScheduleList", "");    // 月スケジュール設定
			}
		});
	}

	private boolean validateSettingMonthScheduleList(List<Map<String, Object>> list, HashMap<String, String> validationPattern) {
		for (Map<String, Object> obj : list) {
			for (Entry<String, String> ent : validationPattern.entrySet()) {

				Object value = obj.get(ent.getKey());
				if (value == null) {
					return false;
				}

				if (value instanceof String) {
					if (!((String)value).matches(ent.getValue())) {
						return false;
					}
				} else if (value instanceof List) {
					// 入れ子リストバリデーション
					@SuppressWarnings("unchecked")
					List<Map<String,String>> valueList = (List<Map<String,String>>)value;
					if (!this.validateList(valueList, new HashMap<String, String>() {{
						put("sundayPattern","[0-9]{1,2}");                  //  日曜パターン
						put("mondayPattern","[0-9]{1,2}");                  //  月曜パターン
						put("tuesdayPattern","[0-9]{1,2}");                 //  火曜パターン
						put("wednesdayPattern","[0-9]{1,2}");               //  水曜パターン
						put("thursdayPattern","[0-9]{1,2}");                //  木曜パターン
						put("fridayPattern","[0-9]{1,2}");                  //  金曜パターン
						put("saturdayPattern","[0-9]{1,2}");                //  土曜パターン
						put("nationalHolidayPattern","[0-9]{1,2}");         //  祝日パターン
						put("specificDayAssignment1","[0-9]{1,2}");         //  特定日指定1
						put("specificDayAssignment2","[0-9]{1,2}");         //  特定日指定2
						put("specificDayAssignment3","[0-9]{1,2}");         //  特定日指定3
						put("specificDayAssignment4","[0-9]{1,2}");         //  特定日指定4
						put("specificDayAssignment5","[0-9]{1,2}");         //  特定日指定5
						put("specificDayAssignment6","[0-9]{1,2}");         //  特定日指定6
						put("specificDayAssignment7","[0-9]{1,2}");         //  特定日指定7
						put("specificDayAssignment8","[0-9]{1,2}");         //  特定日指定8
						put("specificDayAssignment9","[0-9]{1,2}");         //  特定日指定9
						put("specificDayAssignment10","[0-9]{1,2}");        //  特定日指定10
						put("specificDayPattern1","[0-9]{1,2}");            //  特定日パターン1
						put("specificDayPattern2","[0-9]{1,2}");            //  特定日パターン2
						put("specificDayPattern3","[0-9]{1,2}");            //  特定日パターン3
						put("specificDayPattern4","[0-9]{1,2}");            //  特定日パターン4
						put("specificDayPattern5","[0-9]{1,2}");            //  特定日パターン5
						put("specificDayPattern6","[0-9]{1,2}");            //  特定日パターン6
						put("specificDayPattern7","[0-9]{1,2}");            //  特定日パターン7
						put("specificDayPattern8","[0-9]{1,2}");            //  特定日パターン8
						put("specificDayPattern9","[0-9]{1,2}");            //  特定日パターン9
						put("specificDayPattern10","[0-9]{1,2}");           //  特定日パターン10
					}})) {
						return false;
					}
				}
			}
		}
		return true;
	}


	public String getScheduleControlInfo() {
		return scheduleControlInfo;
	}

	public void setScheduleControlInfo(String scheduleControlInfo) {
		this.scheduleControlInfo = scheduleControlInfo;
	}

	public String getScheduleControl() {
		return scheduleControl;
	}

	public void setScheduleControl(String scheduleControl) {
		this.scheduleControl = scheduleControl;
	}

	public List<Map<String, Object>> getLoadList() {
		return loadList;
	}

	public void setLoadList(List<Map<String, Object>> loadList) {
		this.loadList = loadList;
	}

	@Override
	public boolean partDataComparison(Object oldData) {
		// 一括機器制御系：比較処理
		// スケジュール制御比較
		return this.scheduleControl.equals(((A200060Param)oldData).getScheduleControl());
	}

}
