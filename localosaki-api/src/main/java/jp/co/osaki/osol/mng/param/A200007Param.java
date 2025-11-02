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
* デマンド(設定) Param クラス.
*
* @autho da_yamano
*
*/
public class A200007Param extends BaseParam {

	/**
	 * 取得日時
	 */
	private String settingDate;

	/**
	 * 時限方式
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String timeLimitMethod;

	/**
	 * パルス重み
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,8}")
	private String pulseWeight;

	/**
	 * 目標電力
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,4}")
	private String targetPower;

	/**
	 * 日報出力時
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String dayReportOutputHour;

	/**
	 * 月報出力日
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String monthReportOutputDay;

	/**
	 * 年報出力月
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String yearReportOutputMonth;

	/**
	 * 時計同期方式
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String clockMirrorMethod;

	/**
	 * 停電時時限
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String blockoutTimeLimit;

	/**
	 * 初期電力
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String initialPower;

	/**
	 * 高負荷容量
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String highLoadCapacity;

	/**
	 * 限界電力
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String limitPower;

	/**
	 * 遮断電力
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,4}")
	private String blockPower;

	/**
	 * 復帰電力
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,4}")
	private String returnPower;

	/**
	 * 警報ロック時間
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String alertLockTime;

	/**
	 * 複数遮断時間
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,2}")
	private String multiBlockTime;

	/**
	 * 遮断方式
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String blockMethod;

	/**
	 * 目標値切替
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String switchTarget;

	/**
	 * 目標電力乗率
	 */
	@NotNull
	@Pattern(regexp="[0-9]{1,3}")
	private String targetPowerMultiplier;

	/**
	 * ファジィ
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String fuzzy;

	/**
	 * デマンド端数
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String demandFraction;

	/**
	 * 負荷情報リスト
	 */
	private List<Map<String, String>> loadInfoList;

	/**
	 * X月リスト
	 */
	@Size(max=12,min=12)
	private List<Map<String,String>> monthList;

	/**
	 * 複数遮断時間リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> minLoadBlockTimeList;

	/**
	 * 負荷情報リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	public  List<Map<String,String>> _loadInfoList;

	/**
	 * 負荷遮断容量リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> loadBlockCapacityList;

	/**
	 * X月リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	public  List<Map<String,String>> _monthList;

	/**
	 * 月最大デマンドリスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	public List<Map<String,String>> _maxDemandMonthList;

	/**
	 * Result用の負荷情報リスト
	 *
	 * XML内にあるリストを負荷情報リストに追加して返却
	 */
	public List<Map<String,String>> getLoadInfoList(){
		if(loadInfoList != null) {
			return loadInfoList;
		}
		for(int i=0;i<_loadInfoList.size();i++) {
			_loadInfoList.get(i).put("minLoadBlockTime", minLoadBlockTimeList.get(i));
			_loadInfoList.get(i).put("loadBlockCapacity", loadBlockCapacityList.get(i));
		}
		return _loadInfoList;
	}

	/**
	 * Result用のX月リスト
	 *
	 * XML内にあるリストをX月リストに追加して返却
	 */
	public List<Map<String,String>> getMonthList(){
		for(int i=0;i<_monthList.size();i++) {
			_monthList.get(i).putAll(_maxDemandMonthList.get(i));
		}
		return _monthList;
	}

	@AssertTrue
	public boolean isValidateLoadInfo() {
		return loadInfoList!=null && validateList(loadInfoList,  new HashMap<String, String>(){{
			put("minLoadBlockTime", 	"[0-9]{1,2}");		// 最小負荷遮断時間
			put("loadBlockMethod", 		"[0-9]");           // 遮断方法
			put("loadBlockRank", 		"[0-9]{1,2}");		// 遮断順位
			put("loadBlockCapacity", 	"[0-9]{1,4}");		// 負荷遮断容量
		}});
	}

	@AssertTrue
	public boolean isValidateMonthList() {
		return monthList!=null && validateList(monthList,  new HashMap<String, String>(){{
			put("maxDemandMonth", "[0-9]{1,4}");   			// 月最大デマンド kW
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

	public List<String> getMinLoadBlockTimeList() {
		return minLoadBlockTimeList;
	}

	public void setMinLoadBlockTimeList(List<String> minLoadBlockTimeList) {
		this.minLoadBlockTimeList = minLoadBlockTimeList;
	}

	public List<Map<String, String>> get_loadInfoList() {
		return _loadInfoList;
	}

	public void set_loadInfoList(List<Map<String, String>> _loadInfoList) {
		this._loadInfoList = _loadInfoList;
	}

	public List<String> getLoadBlockCapacityList() {
		return loadBlockCapacityList;
	}

	public void setLoadBlockCapacityList(List<String> loadBlockCapacityList) {
		this.loadBlockCapacityList = loadBlockCapacityList;
	}

	public List<Map<String, String>> get_maxDemandMonthList() {
		return _maxDemandMonthList;
	}

	public void set_maxDemandMonthList(List<Map<String, String>> _maxDemandMonthList) {
		this._maxDemandMonthList = _maxDemandMonthList;
	}

	public List<Map<String, String>> get_monthList() {
		return _monthList;
	}

	public void set_monthList(List<Map<String, String>> _monthList) {
		this._monthList = _monthList;
	}

	public String getTimeLimitMethod() {
		return timeLimitMethod;
	}

	public void setTimeLimitMethod(String timeLimitMethod) {
		this.timeLimitMethod = timeLimitMethod;
	}

	public String getPulseWeight() {
		return pulseWeight;
	}

	public void setPulseWeight(String pulseWeight) {
		this.pulseWeight = pulseWeight;
	}

	public String getTargetPower() {
		return targetPower;
	}

	public void setTargetPower(String targetPower) {
		this.targetPower = targetPower;
	}

	public String getDayReportOutputHour() {
		return dayReportOutputHour;
	}

	public void setDayReportOutputHour(String dayReportOutputHour) {
		this.dayReportOutputHour = dayReportOutputHour;
	}

	public String getMonthReportOutputDay() {
		return monthReportOutputDay;
	}

	public void setMonthReportOutputDay(String monthReportOutputDay) {
		this.monthReportOutputDay = monthReportOutputDay;
	}

	public String getYearReportOutputMonth() {
		return yearReportOutputMonth;
	}

	public void setYearReportOutputMonth(String yearReportOutputMonth) {
		this.yearReportOutputMonth = yearReportOutputMonth;
	}

	public String getClockMirrorMethod() {
		return clockMirrorMethod;
	}

	public void setClockMirrorMethod(String clockMirrorMethod) {
		this.clockMirrorMethod = clockMirrorMethod;
	}

	public String getBlockoutTimeLimit() {
		return blockoutTimeLimit;
	}

	public void setBlockoutTimeLimit(String blockoutTimeLimit) {
		this.blockoutTimeLimit = blockoutTimeLimit;
	}

	public String getInitialPower() {
		return initialPower;
	}

	public void setInitialPower(String initialPower) {
		this.initialPower = initialPower;
	}

	public String getHighLoadCapacity() {
		return highLoadCapacity;
	}

	public void setHighLoadCapacity(String highLoadCapacity) {
		this.highLoadCapacity = highLoadCapacity;
	}

	public String getLimitPower() {
		return limitPower;
	}

	public void setLimitPower(String limitPower) {
		this.limitPower = limitPower;
	}

	public String getBlockPower() {
		return blockPower;
	}

	public void setBlockPower(String blockPower) {
		this.blockPower = blockPower;
	}

	public String getReturnPower() {
		return returnPower;
	}

	public void setReturnPower(String returnPower) {
		this.returnPower = returnPower;
	}

	public String getAlertLockTime() {
		return alertLockTime;
	}

	public void setAlertLockTime(String alertLockTime) {
		this.alertLockTime = alertLockTime;
	}

	public String getMultiBlockTime() {
		return multiBlockTime;
	}

	public void setMultiBlockTime(String multiBlockTime) {
		this.multiBlockTime = multiBlockTime;
	}

	public String getBlockMethod() {
		return blockMethod;
	}

	public void setBlockMethod(String blockMethod) {
		this.blockMethod = blockMethod;
	}

	public String getSwitchTarget() {
		return switchTarget;
	}

	public void setSwitchTarget(String switchTarget) {
		this.switchTarget = switchTarget;
	}

	public String getTargetPowerMultiplier() {
		return targetPowerMultiplier;
	}

	public void setTargetPowerMultiplier(String targetPowerMultiplier) {
		this.targetPowerMultiplier = targetPowerMultiplier;
	}

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}

	public String getDemandFraction() {
		return demandFraction;
	}

	public void setDemandFraction(String demandFraction) {
		this.demandFraction = demandFraction;
	}

	@Override
	public boolean partDataComparison(Object oldData) {
		// 一括機器制御系：比較処理
		// 目標電力
		return this.targetPower.equals(((A200007Param)oldData).getTargetPower());
	}

}
