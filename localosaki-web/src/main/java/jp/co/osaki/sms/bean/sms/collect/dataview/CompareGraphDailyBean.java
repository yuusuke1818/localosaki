package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MDevPrmListDao;

/**
 * データ収集装置 データ表示機能 データ比較グラフ（日報）画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewCompareGraphDailyBean")
@ConversationScoped
public class CompareGraphDailyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = 721956737699370376L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "compareGraphDaily";

    /** 比較表示タイトル. */
    private static final String TITLE_COMPARE = "比較日";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMMddHHmm";

    /** 比較表示タイトル. */
    private String titleCompare;

    /** 選択値 接続先(装置ID). */
    private String devId;

    /** 選択値 メーター管理番号. */
    private Long meterMngId;

    /** 選択値 メータータイプ. */
    private String meterKind;

    /** 選択値 表示方向. */
    private String dispDirect;

    /** 選択値 開始時刻. */
    private String startTime;

    /** 選択値 表示時間単位. */
    private String dispTimeUnit;

    /** 入力値 テナント名. */
    private String tenantName;

    /** メータータイププルダウンMap. */
    private Map<String, String> meterKindMap;

    @Inject
    private DataViewBean compareBean1;

    @Inject
    private DataViewBean compareBean2;

    @Inject
    private PullDownList toolsPullDownList;

    @EJB
    private MDevPrmListDao mDevPrmListDao;

    /**
     * 初期処理.
     *
     * @return 遷移先
     */
    @Override
    public String init() {
        conversationStart();

        return FORWARD_STR;
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        setTitleCompare(TITLE_COMPARE);

        Date currentDate = mDevPrmListDao.getSvDate();

        Date targetDate1 = DateUtility.plusDay(currentDate, -1);
        Date targetDate2 = currentDate;

        Map<String, String> meterKindMap = toolsPullDownList.getMeterKind();
        meterKindMap.remove(MapUtility.searchValueOfKeyName(meterKindMap, SmsConstants.METER_KIND.HANDY.getVal()));
        setMeterKindMap(meterKindMap);

        setMeterKind(new ArrayList<String>(getMeterKindMap().values()).get(0));

        String meterKind = getMeterKind();

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_HHMM_COLON);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);

        compareBean1.setDaily(true);
        compareBean1.prepare(buildingInfo, property, targetDate1, false, meterKind);
        compareBean1.updateStartTimePulldown();

        compareBean2.setDaily(true);
        compareBean2.prepare(buildingInfo, property, targetDate2, false, meterKind);
        compareBean1.updateStartTimePulldown();

        Map<String, String> devIdMap = compareBean1.getDevIdMap();
        if (devIdMap != null && !devIdMap.isEmpty()) {
        	String targetDevId = new ArrayList<>(devIdMap.values()).get(0);
            setDevId(targetDevId);
            //IoT-Rだったら表示時間単位を1時間に
            compareBean1.setDispTimeUnitMapForIotr(targetDevId);

        }
        compareBean1.updateStartTimePulldown();
        compareBean2.updateStartTimePulldown();

        Map<String, Long> meterMngIdMap = compareBean1.getMeterMngIdMap();
        if (meterMngIdMap != null && !meterMngIdMap.isEmpty()) {
            setMeterMngId(new ArrayList<>(meterMngIdMap.values()).get(0));
        }

        Map<String, String> dispDirectMap = compareBean1.getDispDirectMap();
        if (MapUtils.isNotEmpty(dispDirectMap)) {
            setDispDirect(new ArrayList<>(dispDirectMap.values()).get(0));
        }

        return init();
    }

    /**
     * 比較日1 カレンダー 日付変更時
     */
    public void clickCalendarCompare1() {
        // アクセスログ出力
        exportAccessLog("clickCalendarCompare1", "カレンダーにて日付選択");

        compareBean1.changeDailyDateWithoutReload();
    }

    /**
     * 比較日1 カレンダー 前月ボタン押下時
     */
    public void moveLastMonthCompare1() {
        // アクセスログ出力
        exportAccessLog("moveLastMonthCompare1", "ボタン「前月」押下");

        compareBean1.moveLastMonthWithoutReload();
    }

    /**
     * 比較日1 カレンダー 翌月ボタン押下時
     */
    public void moveNextMonthCompare1() {
        // アクセスログ出力
        exportAccessLog("moveNextMonthCompare1", "ボタン「翌月」押下");

        compareBean1.moveNextMonthWithoutReload();
    }

    /**
     * 比較日1 カレンダー 前日ボタン押下時
     */
    public void moveLastDayCompare1() {
        // アクセスログ出力
        exportAccessLog("moveLastDayCompare1", "ボタン「前日」押下");

        compareBean1.moveLastDayWithoutReload();
    }

    /**
     * 比較日1 カレンダー 翌日ボタン押下時
     */
    public void moveNextDayCompare1() {
        // アクセスログ出力
        exportAccessLog("moveNextDayCompare1", "ボタン「翌日」押下");

        compareBean1.moveNextDayWithoutReload();
    }

    /**
     * 比較日1 カレンダー 日付選択時
     *
     * @param selectedDay 選択日
     */
    public void selectCalDayCompare1(String selectedDay) {
        // アクセスログ出力
        exportAccessLog("selectCalDayCompare1", "カレンダー日付リンク押下");

        compareBean1.selectCalDayWithoutReload(selectedDay);
    }

    /**
     * 比較日2 カレンダー 日付変更時
     */
    public void clickCalendarCompare2() {
        // アクセスログ出力
        exportAccessLog("clickCalendarCompare2", "カレンダーにて日付選択");

        compareBean2.changeDailyDateWithoutReload();
    }

    /**
     * 比較日2 カレンダー 前月ボタン押下時
     */
    public void moveLastMonthCompare2() {
        // アクセスログ出力
        exportAccessLog("moveLastMonthCompare2", "ボタン「前月」押下");

        compareBean2.moveLastMonthWithoutReload();
    }

    /**
     * 比較日2 カレンダー 翌月ボタン押下時
     */
    public void moveNextMonthCompare2() {
        // アクセスログ出力
        exportAccessLog("moveNextMonthCompare2", "ボタン「翌月」押下");

        compareBean2.moveNextMonthWithoutReload();
    }

    /**
     * 比較日2 カレンダー 前日ボタン押下時
     */
    public void moveLastDayCompare2() {
        // アクセスログ出力
        exportAccessLog("moveLastDayCompare2", "ボタン「前日」押下");

        compareBean2.moveLastDayWithoutReload();
    }

    /**
     * 比較日2 カレンダー 翌日ボタン押下時
     */
    public void moveNextDayCompare2() {
        // アクセスログ出力
        exportAccessLog("moveNextDayCompare2", "ボタン「翌日」押下");

        compareBean2.moveNextDayWithoutReload();
    }

    /**
     * 比較日2 カレンダー 日付選択時
     *
     * @param selectedDay 選択日
     */
    public void selectCalDayCompare2(String selectedDay) {
        // アクセスログ出力
        exportAccessLog("selectCalDayCompare2", "カレンダー日付リンク押下");

        compareBean2.selectCalDayWithoutReload(selectedDay);
    }

    /**
     * 装置プルダウン変更時
     */
    public void changeDevIdPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDevIdPulldown", "「接続先」選択");

        String devId = getDevId();
        if (CheckUtility.isNullOrEmpty(devId)) {
            return;
        }

        compareBean1.changeDevIdPulldown(devId, getMeterKind(), getDispDirect(), getTenantName());

        setDispDirect(compareBean1.getPresentCondition().getDispDirect());
    }

    /**
     * メータータイプラジオボタン変更時
     */
    public void changeMeterKindRadio() {
        // アクセスログ出力
        exportAccessLog("changeMeterKindRadio", "「メータータイプ」選択");

        compareBean1.updateMeterMngIdMap(getDevId(), getMeterKind(), getTenantName());
    }

    /**
     * テナント名入力時.
     */
    public void inputTenantName() {
        // アクセスログ出力
        exportAccessLog("inputTenantName", "「テナント名」入力");

        changeDevIdPulldown();
    }

    /**
     * メーター表示切替プルダウン変更時
     */
    public void changePageNoPulldown() {
        // アクセスログ出力
        exportAccessLog("changePageNoPulldown", "「メーター表示切替」選択");
    }

    /**
     * 表示方向プルダウン変更時
     */
    public void changeDispDirectPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDispDirectPulldown", "「表示方向」選択");
    }

    /**
     * 開始時刻プルダウン変更時
     */
    public void changeStartTimePulldown() {
        // アクセスログ出力
        exportAccessLog("changeStartTimePulldown", "「開始時刻」選択");
    }

    /**
     * 表示時間単位プルダウン変更時
     */
    public void changeDispTimeUnitPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDispTimeUnitPulldown", "「表示時間単位」選択");

        compareBean1.updateStartTimePulldown();
    }

    /**
     * 表示更新.
     *
     * @return 遷移先
     */
    public String reload() {
        // アクセスログ出力
        exportAccessLog("reload", "ボタン「表示」押下");

        String devId = getDevId();
        Long meterMngId = getMeterMngId();

        String dispDirect = getDispDirect();
        String startTime = getStartTime();
        String dispTimeUnit = getDispTimeUnit();

        compareBean1.reloadDaily(devId, meterMngId, dispDirect, startTime, dispTimeUnit);
        compareBean2.reloadDaily(devId, meterMngId, dispDirect, startTime, dispTimeUnit);

        return FORWARD_STR;
    }

    public String getTitleCompare() {
        return titleCompare;
    }

    public void setTitleCompare(String titleCompare) {
        this.titleCompare = titleCompare;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getMeterKind() {
        return meterKind;
    }

    public void setMeterKind(String meterKind) {
        this.meterKind = meterKind;
    }

    public String getDispDirect() {
        return dispDirect;
    }

    public void setDispDirect(String dispDirect) {
        this.dispDirect = dispDirect;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDispTimeUnit() {
        return dispTimeUnit;
    }

    public void setDispTimeUnit(String dispTimeUnit) {
        this.dispTimeUnit = dispTimeUnit;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Map<String, String> getMeterKindMap() {
        return meterKindMap;
    }

    public void setMeterKindMap(Map<String, String> meterKindMap) {
        this.meterKindMap = meterKindMap;
    }

    public DataViewBean getCompareBean1() {
        return compareBean1;
    }

    public void setCompareBean1(DataViewBean compareBean1) {
        this.compareBean1 = compareBean1;
    }

    public DataViewBean getCompareBean2() {
        return compareBean2;
    }

    public void setCompareBean2(DataViewBean compareBean2) {
        this.compareBean2 = compareBean2;
    }
}
