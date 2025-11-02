package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
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
 * データ収集装置 データ表示機能 データ比較グラフ（月報）画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewCompareGraphMonthlyBean")
@ConversationScoped
public class CompareGraphMonthlyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -49999983640276800L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "compareGraphMonthly";

    /** 比較表示タイトル. */
    private static final String TITLE_COMPARE = "比較月";

    /** 対象日時取得間隔. */
    private static final String TARGET_DATE_TIME_DURATION = "P1D";

    /** 対象日時取得時間数. */
    private static final String TARGET_DATE_TIME_ADDITION = "P1M";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMMdd";

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

    /** 選択値 開始日. */
    private String startDay;

    /** 入力値 テナント名. */
    private String tenantName;

    /** メータータイププルダウンMap. */
    private Map<String, String> meterKindMap;

    /** 選択月List. */
    private List<Integer> selectMonthList;

    /** 開始日プルダウンList. */
    private List<SelectItem> startDateTimeList;

    /** 対象日List. */
    private List<String> dispDayList;

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

        Date currentDate = DateUtility.changeDateMonthFirst(mDevPrmListDao.getSvDate());

        Date targetDate1 = DateUtility.plusMonth(currentDate, -1);
        Date targetDate2 = currentDate;

        setSelectMonthList(createSelectMonthList());

        Map<String, String> meterKindMap = toolsPullDownList.getMeterKind();
        meterKindMap.remove(MapUtility.searchValueOfKeyName(meterKindMap, SmsConstants.METER_KIND.HANDY.getVal()));
        setMeterKindMap(meterKindMap);

        setMeterKind(new ArrayList<String>(getMeterKindMap().values()).get(0));

        String meterKind = getMeterKind();

        setStartDateTimeList(createStartDateList(targetDate1, targetDate2));

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setTargetDateTimeDuration(TARGET_DATE_TIME_DURATION);
        property.setTargetDateTimeAddtion(TARGET_DATE_TIME_ADDITION);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_MMDD_SLASH);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMM_SLASH);

        compareBean1.prepare(buildingInfo, property, targetDate1, false, meterKind);
        compareBean2.prepare(buildingInfo, property, targetDate2, false, meterKind);

        Map<String, String> devIdMap = compareBean1.getDevIdMap();
        if (devIdMap != null && !devIdMap.isEmpty()) {
            setDevId(new ArrayList<>(devIdMap.values()).get(0));
        }

        Map<String, Long> meterMngIdMap = compareBean1.getMeterMngIdMap();
        if (meterMngIdMap != null && !meterMngIdMap.isEmpty()) {
            setMeterMngId(new ArrayList<>(meterMngIdMap.values()).get(0));
        }

        Map<String, String> dispDirectMap = compareBean1.getDispDirectMap();
        if (MapUtils.isNotEmpty(dispDirectMap)) {
            setDispDirect(new ArrayList<>(dispDirectMap.values()).get(0));
        }

        setStartDay(getStartDateTimeList().get(0).getValue().toString());

        return init();
    }

    /**
     * 比較月1 カレンダー 月選択時
     */
    public void selectCalMonthCompare1(String selectedMonth) {
        // アクセスログ出力
        exportAccessLog("selectCalMonthCompare1", "月リンク押下");

        compareBean1.selectCalMonthWithoutReload(selectedMonth);
        updateStartDateList();
    }

    /**
     * 比較月1 カレンダー 前年ボタン押下時
     */
    public void moveLastYearCompare1() {
        // アクセスログ出力
        exportAccessLog("moveLastYearCompare1", "ボタン「前年」押下");

        compareBean1.moveLastYearWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月1 カレンダー 翌年ボタン押下時
     */
    public void moveNextYearCompare1() {
        // アクセスログ出力
        exportAccessLog("moveNextYearCompare1", "ボタン「翌年」押下");

        compareBean1.moveNextYearWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月1 カレンダー 前月ボタン押下時
     */
    public void moveLastMonthCompare1() {
        // アクセスログ出力
        exportAccessLog("moveLastMonthCompare1", "ボタン「前月」押下");

        compareBean1.moveLastMonthWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月1 カレンダー 翌月ボタン押下時
     */
    public void moveNextMonthCompare1() {
        // アクセスログ出力
        exportAccessLog("moveNextMonthCompare1", "ボタン「翌月」押下");

        compareBean1.moveNextMonthWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月2 カレンダー 月選択時
     */
    public void selectCalMonthCompare2(String selectedMonth) {
        // アクセスログ出力
        exportAccessLog("selectCalMonthCompare2", "月リンク押下");

        compareBean2.selectCalMonthWithoutReload(selectedMonth);
        updateStartDateList();
    }

    /**
     * 比較月2 カレンダー 前年ボタン押下時
     */
    public void moveLastYearCompare2() {
        // アクセスログ出力
        exportAccessLog("moveLastYearCompare2", "ボタン「前年」押下");

        compareBean2.moveLastYearWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月2 カレンダー 翌年ボタン押下時
     */
    public void moveNextYearCompare2() {
        // アクセスログ出力
        exportAccessLog("moveNextYearCompare2", "ボタン「翌年」押下");

        compareBean2.moveNextYearWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月2 カレンダー 前月ボタン押下時
     */
    public void moveLastMonthCompare2() {
        // アクセスログ出力
        exportAccessLog("moveLastMonthCompare2", "ボタン「前月」押下");

        compareBean2.moveLastMonthWithoutReload();
        updateStartDateList();
    }

    /**
     * 比較月2 カレンダー 翌月ボタン押下時
     */
    public void moveNextMonthCompare2() {
        // アクセスログ出力
        exportAccessLog("moveNextMonthCompare2", "ボタン「翌月」押下");

        compareBean2.moveNextMonthWithoutReload();
        updateStartDateList();
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
     * 開始日プルダウン変更時
     */
    public void changeStartDayPulldown() {
        // アクセスログ出力
        exportAccessLog("changeStartDayPulldown", "「開始日」選択");
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
        String startDay = getStartDay();

        List<String> dispDayList = createDispDayList(startDay);

        compareBean1.reloadMonthly(devId, meterMngId, dispDirect, startDay, dispDayList);
        compareBean2.reloadMonthly(devId, meterMngId, dispDirect, startDay, dispDayList);

        setDispDayList(createGraphDispDayList(dispDayList));

        return FORWARD_STR;
    }

    /**
     * 選択月List生成.
     *
     * @return 選択月List
     */
    private List<Integer> createSelectMonthList() {
        List<Integer> selectMonthList = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            selectMonthList.add(month);
        }

        return selectMonthList;
    }

    /**
     * 開始日プルダウンList更新.
     */
    private void updateStartDateList() {
        List<SelectItem> startDateTimeList = createStartDateList(compareBean1.getCalendarBean().getSelectedDate(),
                compareBean2.getCalendarBean().getSelectedDate());
        setStartDateTimeList(startDateTimeList);

        int targetMonthDayCount = startDateTimeList.size();
        if (Integer.parseInt(getStartDay()) > targetMonthDayCount) {
            setStartDay(startDateTimeList.get(targetMonthDayCount - 1).getValue().toString());
        }
    }

    /**
     * 開始日プルダウンList生成.
     *
     * @param targetDate1 対象日付1
     * @param targetDate2 対象日付2
     * @return 開始日プルダウンList
     */
    private List<SelectItem> createStartDateList(Date targetDate1, Date targetDate2) {
        return compareBean1.createStartDateList(compareMonthDayCount(targetDate1, targetDate2, true));
    }

    /**
     * 表示日List生成.
     *
     * @param startDay 開始日
     * @return 表示日List
     */
    private List<String> createDispDayList(String startDay) {
        Date targetDate = compareMonthDayCount(compareBean1.getCalendarBean().getSelectedDate(),
                compareBean2.getCalendarBean().getSelectedDate(), false);

        List<String> dispDayList = compareBean1.createTargetDateStrList(
                DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYYMM) + startDay,
                DateUtility.DATE_FORMAT_DD, TARGET_DATE_TIME_DURATION, TARGET_DATE_TIME_ADDITION);

        return dispDayList;
    }

    /**
     * グラフ用表示日List生成.
     *
     * @param dispDayList 表示日List
     * @return グラフ用表示日List
     */
    private List<String> createGraphDispDayList(List<String> dispDayList) {
        List<String> graphDispDayList = new ArrayList<>();
        for (String dispDay : dispDayList) {
            graphDispDayList.add(dispDay + "日");
        }

        return graphDispDayList;
    }

    /**
     * 指定日付の月日数を比較.
     *
     * @param targetDate1 対象日付1
     * @param targetDate2 対象日付2
     * @param shorterDateFlg 月日数の長い方の日付を返すフラグ
     * @return shorterDateFlgがtrue:月日数の短い方の日付を返す, false:長い方の日付を返す
     */
    private Date compareMonthDayCount(Date targetDate1, Date targetDate2, boolean shorterDateFlg) {
        int targetMonth1DayCount = Integer.parseInt(DateUtility.changeDateFormat(
                DateUtility.changeDateMonthLast(DateUtility.changeDateMonthFirst(targetDate1)),
                DateUtility.DATE_FORMAT_D));

        int targetMonth2DayCount = Integer.parseInt(DateUtility.changeDateFormat(
                DateUtility.changeDateMonthLast(DateUtility.changeDateMonthFirst(targetDate2)),
                DateUtility.DATE_FORMAT_D));

        Date targetDate = targetDate1;
        if (targetMonth1DayCount > targetMonth2DayCount) {
            if (shorterDateFlg) {
                targetDate = targetDate2;
            }
        } else {
            if (!shorterDateFlg) {
                targetDate = targetDate2;
            }
        }

        return targetDate;
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

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
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

    public List<Integer> getSelectMonthList() {
        return selectMonthList;
    }

    public void setSelectMonthList(List<Integer> selectMonthList) {
        this.selectMonthList = selectMonthList;
    }

    public List<SelectItem> getStartDateTimeList() {
        return startDateTimeList;
    }

    public void setStartDateTimeList(List<SelectItem> startDateTimeList) {
        this.startDateTimeList = startDateTimeList;
    }

    public List<String> getDispDayList() {
        return dispDayList;
    }

    public void setDispDayList(List<String> dispDayList) {
        this.dispDayList = dispDayList;
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
