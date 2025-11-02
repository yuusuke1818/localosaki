package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * スケジュール(設定) Param クラス
 *
 * @autho Sakamoto
 *
 */
public class A200005Param extends BaseParam {

	/**
	 * ページ指定
	 */
	@Pattern(regexp = "[0-9]")
	private String pageAssignment;
	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * スケジュール管理指定
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
	private String scheduleManageAssignment;

	/**
	 * スケジュール設定パターンXX
	 */
	@NotNull
	@Size(max=16,min=16)
	private List<Map<String, String>> settingSchedulePatternList;

	@AssertTrue
	public boolean isValidateSettingSchedulePatternList() {
		return settingSchedulePatternList != null
				&& validateList(settingSchedulePatternList, new HashMap<String, String>() {
					{
						put("startHour1","[0-9]{1,2}");		// 開始時間1(時)
						put("startMinute1","[0-9]{1,2}");	// 開始時間1(分)
						put("endHour1","[0-9]{1,2}");		// 終了時間1(時)
						put("endMinute1","[0-9]{1,2}");		// 終了時間1(分)
						put("startHour2","[0-9]{1,2}");		// 開始時間2(時)
						put("startMinute2","[0-9]{1,2}");	// 開始時間2(分)
						put("endHour2","[0-9]{1,2}");		// 終了時間2(時)
						put("endMinute2","[0-9]{1,2}");		// 終了時間2(分)
						put("startHour3","[0-9]{1,2}");		// 開始時間3(時)
						put("startMinute3","[0-9]{1,2}");	// 開始時間3(分)
						put("endHour3","[0-9]{1,2}");		// 終了時間3(時)
						put("endMinute3","[0-9]{1,2}");		// 終了時間3(分)
						put("startHour4","[0-9]{1,2}");		// 開始時間4(時)
						put("startMinute4","[0-9]{1,2}");	// 開始時間4(分)
						put("endHour4","[0-9]{1,2}");		// 終了時間4(時)
						put("endMinute4","[0-9]{1,2}");		// 終了時間4(分)
						put("dutyOnTime","[0-9]{1,2}");		// デューティON時間(分)
						put("dutyOffTime","[0-9]{1,2}");	// デューティOFF時間(分)
//						put("dutySelect","[0-9]{1,2}"); 	// デューティ選択 ※機種依存項目
					}
				});
	}

	/**
	 * 負荷
	 */
	@NotNull
	private List<Map<String, Object>> loadList;

	@AssertTrue
	public boolean isValidateLoadList() {
		return loadList != null && validateSettingMonthScheduleList(loadList, new HashMap<String, String>() {
			{
				put("settingMonthScheduleList", "");		// MM月スケジュール設定
			}
		});
	}

	private boolean validateSettingMonthScheduleList(List<Map<String, Object>> list, HashMap<String, String> validationPattern) {
		for(Map<String, Object> obj : list) {
			for(Entry<String, String> ent : validationPattern.entrySet()) {

				String key = ent.getKey();

				Object value = obj.get(ent.getKey());
				if(value == null) {
					return false;
				}

				if(value instanceof String) {
					if(!((String)value).matches(ent.getValue())) {
						return false;
					}
				}else if(value instanceof List) {
					// 入れ子リストバリデーション
					@SuppressWarnings("unchecked")
					List<Map<String,String>> valueList = (List<Map<String,String>>)value;
					if(!this.validateList(valueList, new HashMap<String, String>(){{
						put("sundayPattern","[0-9]{1,2}");					//  日曜パターン
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

					}})){
						return false;
					}
				}
			}
		}
		return true;
	}


	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getScheduleManageAssignment() {
		return scheduleManageAssignment;
	}

	public void setScheduleManageAssignment(String scheduleManageAssignment) {
		this.scheduleManageAssignment = scheduleManageAssignment;
	}



	public List<Map<String, String>> getSettingSchedulePatternList() {
		return settingSchedulePatternList;
	}

	public void setSettingSchedulePatternList(List<Map<String, String>> settingSchedulePatternList) {
		this.settingSchedulePatternList = settingSchedulePatternList;
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
		// スケジュール管理指定比較
		return this.scheduleManageAssignment.equals(((A200005Param)oldData).getScheduleManageAssignment());
	}

	public String getPageAssignment() {
		return pageAssignment;
	}

	public void setPageAssignment(String pageAssignment) {
		this.pageAssignment = pageAssignment;
	}

}
