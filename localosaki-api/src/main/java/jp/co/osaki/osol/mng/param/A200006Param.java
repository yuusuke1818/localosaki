package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* デマンド(取得) param クラス
*
* @autho da_yamano
*
*/
public class A200006Param extends BaseParam {

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
	 * 時限方式
	 */
	private String timeLimitMethod;

	/**
	 * パルス重み
	 */
	private String pulseWeight;

	/**
	 * 目標電力
	 */
	private String targetPower;

	/**
	 * 日報出力時
	 */
	private String dayReportOutputHour;

	/**
	 * 月報出力日
	 */
	private String monthReportOutputDay;

	/**
	 * 年報出力月
	 */
	private String yearReportOutputMonth;

	/**
	 * 時計同期方式
	 */
	private String clockMirrorMethod;

	/**
	 * 停電時時限
	 */
	private String blockoutTimeLimit;

	/**
	 * 初期電力
	 */
	private String initialPower;

	/**
	 * 高負荷容量
	 */
	private String highLoadCapacity;

	/**
	 * 限界電力
	 */
	private String limitPower;

	/**
	 * 遮断電力
	 */
	private String blockPower;

	/**
	 * 復帰電力
	 */
	private String returnPower;

	/**
	 * 警報ロック時間
	 */
	private String alertLockTime;

	/**
	 * 複数遮断時間
	 */
	private String multiBlockTime;

	/**
	 * 複数遮断時間リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	private List<String> minLoadBlockTimeList;

	/**
	 * 遮断方式
	 */
	private String blockMethod;

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
	 * 目標値切替
	 */
	private String switchTarget;

	/**
	 * 目標電力乗率
	 */
	private String targetPowerMultiplier;

	/**
	 * 月最大デマンドリスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	public List<Map<String,String>> _maxDemandMonthList;

	/**
	 * X月リスト
	 *
	 * 機器からのレスポンス一時格納用リスト
	 */
	public  List<Map<String,String>> _monthList;

	/**
	 * ファジィ
	 */
	private String fuzzy;

	/**
	 * デマンド端数
	 */
	private String demandFraction;

	/**
	 * Result用の負荷情報リスト
	 *
	 * XML内にあるリストを負荷情報リストに追加して返却
	 */
	public List<Map<String,String>> getLoadInfoList(){
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

	public String getSettingChangeHist() {
		return settingChangeHist;
	}

	public void setSettingChangeHist(String settingChangeHist) {
		this.settingChangeHist = settingChangeHist;
	}


}
