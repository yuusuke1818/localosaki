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
import jp.co.osaki.sms.dao.MVariousDao;

/**
 * データ収集装置 データ表示機能 データ比較グラフ（年報）画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewCompareGraphYearlyBean")
@ConversationScoped
public class CompareGraphYearlyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = 2848703376371864767L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "compareGraphYearly";

    /** 比較表示タイトル. */
    private static final String TITLE_COMPARE = "比較年";

    /** 対象日時取得間隔. */
    private static final String TARGET_DATE_TIME_DURATION = "P1M";

    /** 対象日時取得時間数. */
    private static final String TARGET_DATE_TIME_ADDITION = "P1Y";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMM";

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

    /** 選択値 開始月. */
    private String startMonth;

    /** 入力値 テナント名. */
    private String tenantName;

    /** メータータイププルダウンMap. */
    private Map<String, String> meterKindMap;

    /** 選択月List. */
    private List<Integer> selectMonthList;

    /** 開始月プルダウンList. */
    private List<SelectItem> startDateTimeList;

    /** 対象月List. */
    private List<String> dispMonthList;

    @Inject
    private DataViewBean compareBean1;

    @Inject
    private DataViewBean compareBean2;

    @Inject
    private PullDownList toolsPullDownList;

    @EJB
    private MVariousDao mVariousDao;

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

        Date currentDate = mVariousDao.getSvDate();

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        // 現在日付直近の年報年度開始年月日
        Date yearStartDate = mVariousDao.getYearStartDate(corpId, buildingId, currentDate);

        Date targetDate1 = DateUtility.plusYear(yearStartDate, -1);
        Date targetDate2 = yearStartDate;

        compareBean1.setStartDateTimeList(createStartMonthList());
        compareBean2.setStartDateTimeList(createStartMonthList());

        Map<String, String> meterKindMap = toolsPullDownList.getMeterKind();
        meterKindMap.remove(MapUtility.searchValueOfKeyName(meterKindMap, SmsConstants.METER_KIND.HANDY.getVal()));
        setMeterKindMap(meterKindMap);

        setMeterKind(new ArrayList<String>(getMeterKindMap().values()).get(0));

        String meterKind = getMeterKind();

        setStartDateTimeList(createStartMonthList());

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setTargetDateTimeDuration(TARGET_DATE_TIME_DURATION);
        property.setTargetDateTimeAddtion(TARGET_DATE_TIME_ADDITION);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_MM);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYY);

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

        setStartMonth(compareBean1.getStartMonth());

        return init();
    }

    /**
     * 装置プルダウン変更時
     */
    public void changeDevIdPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDevIdPulldown", "「接続先」選択");

        String devId = getDevId();
        String meterKind = getMeterKind();
        String dispDirect = getDispDirect();
        String tenantName = getTenantName();

        if (CheckUtility.isNullOrEmpty(devId)) {
            return;
        }

        compareBean1.changeDevIdPulldown(devId, meterKind, dispDirect, tenantName);
        compareBean2.changeDevIdPulldown(devId, meterKind, dispDirect, tenantName);

        setDispDirect(compareBean1.getPresentCondition().getDispDirect());
    }

    /**
     * メータータイプラジオボタン変更時
     */
    public void changeMeterKindRadio() {
        // アクセスログ出力
        exportAccessLog("changeMeterKindRadio", "「メータータイプ」選択");

        String devId = getDevId();
        String meterKind = getMeterKind();
        String tenantName = getTenantName();

        compareBean1.updateMeterMngIdMap(devId, meterKind, tenantName);
        compareBean2.updateMeterMngIdMap(devId, meterKind, tenantName);
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
     * 開始月プルダウン変更時
     */
    public void changeStartMonthPulldown() {
        // アクセスログ出力
        exportAccessLog("changeStartMonthPulldown", "「開始月」選択");
    }

    /**
     * 比較年1プルダウン変更時
     */
    public void changeCompareYear1Pulldown() {
        // アクセスログ出力
        exportAccessLog("changeCompareYear1Pulldown", "「比較年1」選択");
    }

    /**
     * 比較年2プルダウン変更時
     */
    public void changeCompareYear2Pulldown() {
        // アクセスログ出力
        exportAccessLog("changeCompareYear2Pulldown", "「比較年2」選択");
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
        String startMonth = getStartMonth();

        compareBean1.reloadYearly(devId, meterMngId, dispDirect, startMonth);
        compareBean2.reloadYearly(devId, meterMngId, dispDirect, startMonth);

        setDispMonthList(createGraphDispMonthList(compareBean1.getDispDateList()));

        return FORWARD_STR;
    }

    /**
     * 開始月プルダウンList生成.
     *
     * @return 開始月プルダウンList
     */
    private List<SelectItem> createStartMonthList() {
        List<SelectItem> startMonthList = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String value = String.format("%02d", i);
            SelectItem item = new SelectItem(value, value);
            startMonthList.add(item);
        }

        return startMonthList;
    }

    /**
     * グラフ用表示月List生成.
     *
     * @param dispDayList 表示日List
     * @return グラフ用表示日List
     */
    private List<String> createGraphDispMonthList(List<String> dispMonthList) {
        List<String> graphDispMonthList = new ArrayList<>();
        for (String dispMonth : dispMonthList) {
            graphDispMonthList.add(dispMonth + "月");
        }

        return graphDispMonthList;
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

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
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

    public List<String> getDispMonthList() {
        return dispMonthList;
    }

    public void setDispMonthList(List<String> dispMonthList) {
        this.dispMonthList = dispMonthList;
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
