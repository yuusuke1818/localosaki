package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewAllDevParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewParameter;
import jp.co.osaki.osol.api.request.sms.collect.dataview.ListSmsDataViewAllDevRequest;
import jp.co.osaki.osol.api.request.sms.collect.dataview.ListSmsDataViewRequest;
import jp.co.osaki.osol.api.response.sms.collect.dataview.ListSmsDataViewResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.osol.utility.PdfUtility;
import jp.co.osaki.osol.web.csv.sms.converter.DailyCsvConverter;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DATA_VIEW_FOOTER;
import jp.co.osaki.sms.SmsConstants.DATA_VIEW_HEADER;
import jp.co.osaki.sms.SmsConstants.DATA_VIEW_UNIT;
import jp.co.osaki.sms.SmsConstants.DISP_TIME_UNIT;
import jp.co.osaki.sms.SmsConstants.DISP_TYPE;
import jp.co.osaki.sms.SmsConstants.OUTPUT_KIND;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.BuildingInfoDao;
import jp.co.osaki.sms.dao.ListSmsDataViewAllDevDao;
import jp.co.osaki.sms.dao.MVariousDao;
import jp.co.osaki.sms.utility.DataViewUtility;

/**
 * データ収集装置 データ表示機能 処理クラス.
 *
 * @author ozaki.y
 */
@Dependent
public class DataViewBean extends SmsBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -2004093909599124722L;

    /** ページ表示件数 画面(web.xml未定義時). */
    private static final int PAGE_DATA_COUNT_VIEW_DEFAULT = 10;

    /** ページ表示件数 PDF(装置単体). */
    private static final int PAGE_DATA_COUNT_PDF = 15;

    /** ページ表示件数 PDF(全装置). */
    private static final int PAGE_ALL_DEV_DATA_COUNT_PDF = 10;

    /** ダウンロードファイル名フォーマット. */
    private static final String DOWNLOAD_FILE_NAME_FORMAT = "%s_%s_%s_%s";

    /** PDFテンプレートファイル名. */
    private static final String PDF_TEMPLATE_FILE_NAME = "smsDailyData.jrxml";

    /** PDFテンプレートファイル名.(全装置用) */
    private static final String PDF_TEMPLATE_FILE_NAME_ALL_DEVICE = "smsDailyDataAllDevice.jrxml";

    /** ページ表示件数 画面. */
    private int pageDataCountView;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** プロパティクラス. */
    private DataViewBeanProperty property;

    /** 検索条件(最新選択値). */
    private DataViewSearchCondition presentCondition;

    /** 検索条件(画面表示データ). */
    private DataViewSearchCondition dispCondition;

    /** データ単位(日報・月報・年報). */
    private DATA_VIEW_UNIT dataViewUnit;

    /** 表示種別. */
    private DISP_TYPE dispType;

    /** 出力種別(画面・CSV・PDF). */
    private OUTPUT_KIND outputKind;

    /** 日報・月報・年報データ画面モードフラグ. */
    private boolean isReportMode;

    /** 日報データ画面表示時フラグ. */
    private boolean isDaily;

    /** 年報データ画面表示時フラグ. */
    private boolean isMonthly;

    /** 年報データ画面表示時フラグ. */
    private boolean isYearly;

    /** 選択値 メーター表示切替(ページ番号). */
    private int pageNo;

    /** 選択値 開始時刻. */
    private String startTime;

    /** 選択値 開始日. */
    private String startDay;

    /** 選択値 開始月. */
    private String startMonth;

    /** 選択値 表示年. */
    private String dispYear;

    /** 接続先プルダウンMap. */
    private Map<String, String> devIdMap;

    /** メーター表示切替(ページ番号)プルダウンMap. */
    private Map<String, Integer> meterMngIdPageNoMap;

    /** メーター表示切替(メーター管理番号)プルダウンMap. */
    private Map<String, Long> meterMngIdMap;

    /** 表示方向プルダウンMap. */
    private Map<String, String> dispDirectMap;

    /** 表示種別プルダウンMap. */
    private Map<String, String> dispTypeMap;

    /** 開始日時(開始時刻・開始日・開始月)プルダウンList. */
    private List<SelectItem> startDateTimeList;

    /** 表示時間単位プルダウンMap. */
    private Map<String, String> dispTimeUnitMap;

    /** 表示年プルダウンMap. */
    private Map<String, String> dispYearMap;

    /** 対象日時List. */
    private List<String> dispDateList;

    /** ロードサーベイデータList. */
    private List<List<String>> loadSurveyDataList;

    /** 画面表示データList. */
    private List<DataViewRow> dispDataList;

    /** グラフ表示値ListMap. */
    private Map<String, List<String>> graphListMap;

    /** グラフ最大デマンド表示 ListMap */
    private Map<String, List<String>> graphMaxDemandListMap;

    /** グラフ期間最大デマンド ListMap */
    private Map<String, String> graphMaxDemandPeriodMap;

    /** グラフ期間最大デマンド ListMap */
    private Map<String, String> graphMaxDemandLastYearMap;

    /** グラフ期間最大デマンド ListMap */
    private Map<String, String> graphMaxDemandPastMap;

    /** ファイル名使用禁止文字エスケープMap. */
    private Map<String, String> fileNameEscapeMap;

    /** ダウンロード対象ファイルパス. */
    private String downloadFilePath;

    /** ダウンロードファイル保存名. */
    private String saveFileName;

    /** グラフアイコンの制御. */
    private boolean graphIconHidden;

    /** 選択可能限界最新日付. */
    private String selectableLimitDate;

    /** 翌年ボタンの制御. */
    private boolean nextYearButtonDisabled;

    /** 翌月ボタンの制御. */
    private boolean nextMonthButtonDisabled;

    /** 翌日ボタンの制御. */
    private boolean nextDayButtonDisabled;

    /** カレンダー表示クラス. */
    private CalendarBean calendarBean;

    /** ダウンロードボタンの制御. */
    private boolean downloadButtonDisabled;

    /** データ比較グラフ画面用表示日List. */
    private List<String> dispDayListForCompare;

    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private SmsFileDownload fileDownloader;

    @EJB
    private MVariousDao mVariousDao;

    @EJB
    private BuildingInfoDao buildingInfoDao;

    @EJB
    private ListSmsDataViewAllDevDao listSmsDataViewAllDevDao;

    public DataViewBean() {
        setPageDataCountView(changeInteger(
                getWrapped().getInitParameter(SmsConstants.PARAM_NAME_PAGE_DATA_COUNT_DATA_VIEW_SERIES), false));

        setPageNo(1);

        setCalendarBean(new CalendarBean());

        setFileNameEscapeMap(createFileNameEscapeMap());
    }

    @Override
    public String init() {
        return null;
    }

    /**
     * 初期処理(年報データ画面用).
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     */
    protected void initYearly(ListInfo buildingInfo, DataViewBeanProperty property) {
        setYearly(true);

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        Date currentDate = DateUtility.changeDateMonthFirst(mVariousDao.getSvDate());

        // 現在日付直近の年報年度開始年月日
        Date yearStartDate = mVariousDao.getYearStartDate(corpId, buildingId, currentDate);

        init(buildingInfo, property, yearStartDate);
    }

    /**
     * 初期処理(月報データ画面用).
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     * @param currentDate 現在日付
     */
    protected void initMonthly(ListInfo buildingInfo, DataViewBeanProperty property, Date currentDate) {
        setMonthly(true);

        init(buildingInfo, property, currentDate);
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     * @param currentDate 現在日付
     */
    protected void init(ListInfo buildingInfo, DataViewBeanProperty property, Date currentDate) {
        setDaily((!isMonthly() && !isYearly()));

        if (isDaily()) {
            setDataViewUnit(DATA_VIEW_UNIT.DAILY);

        } else if (isMonthly()) {
            setDataViewUnit(DATA_VIEW_UNIT.MONTHLY);

        } else if (isYearly()) {
            setDataViewUnit(DATA_VIEW_UNIT.YEARLY);
        }

        prepare(buildingInfo, property, currentDate, true, null);

        reload();
    }

    /**
     * 準備処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     * @param currentDate 現在日付
     * @param isReportMode 日報・月報・年報データ画面モードフラグ
     * @param meterKind メーター種類
     */
    protected void prepare(ListInfo buildingInfo, DataViewBeanProperty property, Date currentDate,
            boolean isReportMode, String meterKind) {

        DataViewSearchCondition presentCondition = new DataViewSearchCondition();

        presentCondition.setDispDirect(SmsConstants.DISP_DIRECT.FORWARD.getVal());
        presentCondition.setDispType(SmsConstants.DISP_TYPE.USE.getVal());

        setPresentCondition(presentCondition);

        setReportMode(isReportMode);

        List<List<String>> loadSurveyDataList = getLoadSurveyDataList();

        if (CollectionUtils.isNotEmpty(loadSurveyDataList)) {
            loadSurveyDataList.clear();
        }

        Map<String, List<String>> graphListMap = getGraphListMap();
        if (graphListMap != null) {
            graphListMap.clear();
        }

        Map<String, String> devIdMap = getDevIdMap();
        if (devIdMap != null) {
            devIdMap.clear();
        }

        Map<String, String> graphMaxDemandPeriodMap = getGraphMaxDemandPeriodMap();
        if (graphMaxDemandPeriodMap != null) {
            graphMaxDemandPeriodMap.clear();
        }

        Map<String, String> graphMaxDemandLastYearMap = getGraphMaxDemandLastYearMap();
        if (graphMaxDemandLastYearMap != null) {
            graphMaxDemandLastYearMap.clear();
        }

        Map<String, String> graphMaxDemandPastMap = getGraphMaxDemandPastMap();
        if (graphMaxDemandPastMap != null) {
            graphMaxDemandPastMap.clear();
        }

        Map<String, Integer> meterMngIdPageNoMap = getMeterMngIdPageNoMap();
        if (meterMngIdPageNoMap != null) {
            meterMngIdPageNoMap.clear();
        }

        Map<String, Long> meterMngIdMap = getMeterMngIdMap();
        if (meterMngIdMap != null) {
            meterMngIdMap.clear();
        }

        if (isDaily()) {
            // 日報データ画面表示時
            presentCondition.setDispTimeUnit(DISP_TIME_UNIT.M30.getVal());
        }

        // PDFテンプレートファイル名
        property.setPdfTemplateFileName(
                StringUtils.defaultString(property.getPdfTemplateFileName(), PDF_TEMPLATE_FILE_NAME));

        property.setPdfTemplateFileNameAllDevice(
                StringUtils.defaultString(property.getPdfTemplateFileNameAllDevice(), PDF_TEMPLATE_FILE_NAME_ALL_DEVICE));

        setProperty(property);
        setBuildingInfo(buildingInfo);

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());
        boolean isTenant = buildingInfo.getTenantFlg();

        setStartTime(SmsConstants.DISP_TYPE.USE.getStartTime());

        calendarBean.setSelectedDate(currentDate);
        calendarBean.setSelectedTime(getStartTime().substring(0, 2), getStartTime().substring(2));

        // 建物情報
        TBuilding tBuilding =
                buildingInfoDao.getBuildingInfo(
                        buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));
        boolean isAllDisplay = DataViewUtility.hasAllDevicesDisplayInBiko(tBuilding.getBiko());
        devIdMap = toolsPullDownList.getDevPrm(corpId, buildingId, isTenant);
        //建物情報備考欄に「全装置表示」の記載がある場合
        if (isAllDisplay) {
            if(isReportMode()) {
                devIdMap.put("全て", "0");
            }
        }
        setDevIdMap(devIdMap);

        setMeterMngIdMap(new HashMap<>());

        // 日報・月報・年報画面は全装置表示時はメータ台数を表示し、接続先指定時はページ先頭の管理番号を表示
        // データ比較グラフは最初の装置のメータ管理番号を表示
        Entry<String, String> devPrmMapEntry = devIdMap.entrySet().stream().filter(devId -> devId.getKey().equals("全て")).findFirst()
                                                                                    .orElse(devIdMap.entrySet().stream().findFirst()
                                                                                            .orElse(null));

        if (devPrmMapEntry != null) {
            String targetDevId = devPrmMapEntry.getValue();

            presentCondition.setDevId(targetDevId);
            updateMeterMngIdMap(targetDevId, meterKind, null);
            updateDispDirectMap(targetDevId, devIdMap);

        }

        updateDispYearMap();

        setDispTypeMap(toolsPullDownList.getDispType());

        setDispTimeUnitMap(toolsPullDownList.getDispTimeUnit());

        prepareDate(getCalendarBean().getSelectedDate());

        setGraphIconHidden(false);
    }

    /**
     * 日付準備処理.
     *
     * @param targetDate 対象日付
     */
    private void prepareDate(Date targetDate) {
        setCalButtonDisabled(targetDate);

        setDispYear(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYY));
        setStartMonth(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_MM));
        setStartDay(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_DD));
    }

    /**
     * カレンダーボタン制御.
     *
     * @param targetDate 対象日付
     */
    private void setCalButtonDisabled(Date targetDate) {
        setNextYearButtonDisabled(false);
        setNextMonthButtonDisabled(false);
        setNextDayButtonDisabled(false);

        // TODO 当面制御未実施
        //        String currentYear = DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(), DateUtility.DATE_FORMAT_YYYY);
        //
        //        setSelectableLimitDate(currentYear + "/12/31 23:59:59");
        //
        //        if (currentYear.equals(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYY))) {
        //            setNextYearButtonDisabled(true);
        //        }
        //
        //        if (isNextYearButtonDisabled()
        //                && "12".equals(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_MM))) {
        //            setNextMonthButtonDisabled(true);
        //        }
        //
        //        if (isNextMonthButtonDisabled()
        //                && "31".equals(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_DD))) {
        //            setNextDayButtonDisabled(true);
        //        }
    }

    /**
     * 開始日プルダウンList生成.
     *
     * @param targetDate 対象日付
     * @return 開始日プルダウンList
     */
    protected List<SelectItem> createStartDateList(Date targetDate) {
        String targetDateTimeFormat = DateUtility.DATE_FORMAT_YYYYMMDD;

        List<String> targetDateTimeList = createTargetDateStrList(
                DateUtility.changeDateFormat(DateUtility.changeDateMonthFirst(targetDate), targetDateTimeFormat),
                targetDateTimeFormat, "P1D", "P1M");

        List<SelectItem> startDateList = new ArrayList<>();

        for (int i = 0; i < (targetDateTimeList.size() - 1); i++) {
            String value = DateUtility.changeDateFormat(
                    DateUtility.conversionDate(targetDateTimeList.get(i), targetDateTimeFormat),
                    DateUtility.DATE_FORMAT_DD);

            SelectItem item = new SelectItem(value, value);
            startDateList.add(item);
        }

        return startDateList;
    }

    /**
     * 指定間隔及び指定日時までの対象日時文字列Listを生成.
     *
     * @param targetDateFromStr 対象日時(FROM)文字列
     * @param patternStr 日時パターン文字列
     * @param duration 刻み時間文字列
     * @param addition 取得終了までの時間数文字列
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(String targetDateFromStr, String patternStr,
            String durationStr, String additionStr) {

        return createTargetDateTimeStrList(
                LocalDateTime.parse(targetDateFromStr, DateTimeFormatter.ofPattern(patternStr)), patternStr,
                Duration.parse(durationStr), Duration.parse(additionStr));
    }

    /**
     * 指定間隔及び指定日時までの対象日時文字列Listを生成.
     *
     * @param targetDateFromStr 対象日時(FROM)文字列
     * @param patternStr 日時パターン文字列
     * @param duration 刻み時間
     * @param addition 取得終了までの時間
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(LocalDateTime targetDateFrom, String patternStr,
            Duration duration, Duration addition) {

        return createTargetDateTimeStrList(targetDateFrom, targetDateFrom.plus(addition),
                DateTimeFormatter.ofPattern(patternStr), duration);
    }

    /**
     * 対象日時文字列Listを生成.
     *
     * @param targetDateFrom 対象日時(FROM)
     * @param targetDateTo 対象日時(TO)
     * @param pattern 日時パターン
     * @param amountToAdd 加算量
     * @return 対象日時文字列List
     */
    protected List<String> createTargetDateTimeStrList(LocalDateTime targetDateFrom, LocalDateTime targetDateTo,
            DateTimeFormatter pattern, TemporalAmount amountToAdd) {

        List<String> targetDateStrList = new ArrayList<>();
        LocalDateTime targetDate = targetDateFrom;
        while (targetDate.compareTo(targetDateTo) <= 0) {
            targetDateStrList.add(pattern.format(targetDate));
            targetDate = targetDate.plus(amountToAdd);
        }

        return targetDateStrList;
    }

    /**
     * 指定間隔及び指定年月日までの対象年月日文字列Listを生成.
     *
     * @param targetDateFromStr 対象月日(FROM)文字列
     * @param patternStr 月日パターン文字列
     * @param duration 刻み時間文字列
     * @param addition 取得終了までの月日数文字列
     * @return 対象月日文字列List
     */
    protected List<String> createTargetDateStrList(String targetDateFromStr, String patternStr,
            String durationStr, String additionStr) {

        return createTargetDateStrList(
                LocalDate.parse(targetDateFromStr, DateTimeFormatter.ofPattern(DateUtility.DATE_FORMAT_YYYYMMDD)),
                patternStr,
                Period.parse(durationStr), Period.parse(additionStr));
    }

    /**
     * 指定間隔及び指定年月日までの対象年月日文字列Listを生成.
     *
     * @param targetDateFromStr 対象年月日(FROM)文字列
     * @param patternStr 月日パターン文字列
     * @param duration 刻み時間
     * @param addition 取得終了までの時間
     * @return 対象月日文字列List
     */
    protected List<String> createTargetDateStrList(LocalDate targetDateFrom, String patternStr,
            Period duration, Period addition) {

        return createTargetDateStrList(targetDateFrom, targetDateFrom.plus(addition),
                DateTimeFormatter.ofPattern(patternStr), duration);
    }

    /**
     * 対象年月日文字列Listを生成.
     *
     * @param targetDateFrom 対象年月日(FROM)
     * @param targetDateTo 対象年月日(TO)
     * @param pattern 月日パターン
     * @param amountToAdd 加算量
     * @return 対象年月日文字列List
     */
    protected List<String> createTargetDateStrList(LocalDate targetDateFrom, LocalDate targetDateTo,
            DateTimeFormatter pattern, TemporalAmount amountToAdd) {

        List<String> targetDateStrList = new ArrayList<>();
        LocalDate targetDate = targetDateFrom;
        while (targetDate.compareTo(targetDateTo) <= 0) {
            String formattedDateStr = pattern.format(targetDate);
            if (!targetDateStrList.contains(formattedDateStr)) {
                targetDateStrList.add(formattedDateStr);
            }

            targetDate = targetDate.plus(amountToAdd);
        }

        return targetDateStrList;
    }

    /**
     * データList日時Listを生成.
     *
     * @param targetDateTimeList 対象日時List
     * @return データList日時プルダウンList
     */
    private List<String> createDispDateList(List<String> targetDateTimeList) {
        DataViewBeanProperty property = getProperty();

        String targetDateTimeFormat = property.getTargetDateTimeFormat();
        String format = property.getListDateTimeFormat();

        List<String> dispDateList = new ArrayList<>();

        for (int i = 0; i < targetDateTimeList.size(); i++) {
            String targetDateTimeStr = targetDateTimeList.get(i);
            Date targetDateTime = DateUtility.conversionDate(targetDateTimeStr, targetDateTimeFormat);

            String label = DateUtility.changeDateFormat(targetDateTime, format);

            if (DateUtility.DATE_FORMAT_HHMM_COLON.equals(format)) {
                if (i > 0 && "00:00".equals(label)) {
                    label = "24:00";
                }
            }

            dispDateList.add(label);
        }

        if (!DateUtility.DATE_FORMAT_HHMM_COLON.equals(format)) {
            dispDateList.remove(dispDateList.size() - 1);
        }

        return dispDateList;
    }

    /**
     * カレンダー 日付変更時処理(表示更新なし)
     */
    protected void changeDailyDateWithoutReload() {
        calendarBean.changeSelectedDate();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 前年ボタン押下処理(表示更新なし)
     */
    protected void moveLastYearWithoutReload() {
        calendarBean.changeToLastYear();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 翌年ボタン押下処理(表示更新なし)
     */
    protected void moveNextYearWithoutReload() {
        calendarBean.changeToNextYear();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 前月ボタン押下処理(表示更新なし)
     */
    protected void moveLastMonthWithoutReload() {
        calendarBean.changeToLastMonth();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 翌月ボタン押下処理(表示更新なし)
     */
    protected void moveNextMonthWithoutReload() {
        calendarBean.changeToNextMonth();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 前日ボタン押下処理(表示更新なし)
     */
    protected void moveLastDayWithoutReload() {
        calendarBean.changeToLastDay();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 翌日ボタン押下処理(表示更新なし)
     */
    protected void moveNextDayWithoutReload() {
        calendarBean.changeToNextDay();
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 日付選択処理(表示更新なし)
     *
     * @param selectedDay 選択日
     */
    protected void selectCalDayWithoutReload(String selectedDay) {
        calendarBean.setSelectedDay(selectedDay);
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 月選択処理(表示更新なし)
     *
     * @param selectedMonth 選択月
     */
    protected void selectCalMonthWithoutReload(String selectedMonth) {
        calendarBean.setSelectedMonth(selectedMonth);
        prepareDate(calendarBean.getSelectedDate());
    }

    /**
     * カレンダー 日付変更時処理.
     */
    protected void changeDailyDate() {
        calendarBean.changeSelectedDate();
        reload();
    }

    /**
     * カレンダー 前年ボタン押下処理.
     */
    protected void moveLastYear() {
        calendarBean.changeToLastYear();
        reload();
    }

    /**
     * カレンダー 翌年ボタン押下処理.
     */
    protected void moveNextYear() {
        calendarBean.changeToNextYear();
        reload();
    }

    /**
     * カレンダー 前月ボタン押下処理.
     */
    protected void moveLastMonth() {
        calendarBean.changeToLastMonth();
        reload();
    }

    /**
     * カレンダー 翌月ボタン押下処理.
     */
    protected void moveNextMonth() {
        calendarBean.changeToNextMonth();
        reload();
    }

    /**
     * カレンダー 前日ボタン押下処理.
     */
    protected void moveLastDay() {
        calendarBean.changeToLastDay();
        reload();
    }

    /**
     * カレンダー 翌日ボタン押下処理.
     */
    protected void moveNextDay() {
        calendarBean.changeToNextDay();
        reload();
    }

    /**
     * カレンダー 日付選択処理.
     *
     * @param selectedDay 選択日
     */
    protected void selectCalDay(String selectedDay) {
        calendarBean.setSelectedDay(selectedDay);
        reload();
    }

    /**
     * カレンダー 月選択処理.
     *
     * @param selectedMonth 選択月
     */
    protected void selectCalMonth(String selectedMonth) {
        calendarBean.setSelectedMonth(selectedMonth);
        reload();
    }

    /**
     * 表示更新(日報データ用).
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param dispDirect 表示方向
     * @param startTime 開始時刻
     * @param dispTimeUnit 表示時間単位
     */
    protected void reloadDaily(String devId, Long meterMngId, String dispDirect, String startTime,
            String dispTimeUnit) {

        getCalendarBean().setSelectedTime(startTime.substring(0, 2), startTime.substring(2));
        setStartTime(startTime);

        reloadOnCompare(devId, meterMngId, dispDirect, dispTimeUnit);
    }

    /**
     * 表示更新(月報データ用).
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param dispDirect 表示方向
     * @param startDay 開始日
     * @param dispDayListForCompare データ比較グラフ画面用表示日List
     */
    protected void reloadMonthly(String devId, Long meterMngId, String dispDirect, String startDay,
            List<String> dispDayListForCompare) {

        getCalendarBean().setSelectedDay(startDay);
        setStartDay(startDay);
        setDispDayListForCompare(dispDayListForCompare);

        reloadOnCompare(devId, meterMngId, dispDirect);
    }

    /**
     * 表示更新(年報データ用).
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param dispDirect 表示方向
     * @param startMonth 開始月
     */
    protected void reloadYearly(String devId, Long meterMngId, String dispDirect, String startMonth) {
        CalendarBean calendarBean = getCalendarBean();
        calendarBean.setSelectedYear(getDispYear());
        calendarBean.setSelectedMonth(startMonth);

        setStartMonth(startMonth);

        reloadOnCompare(devId, meterMngId, dispDirect);
    }

    /**
     * 表示更新(データ比較グラフ画面用).
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param dispDirect 表示方向
     */
    protected void reloadOnCompare(String devId, Long meterMngId, String dispDirect) {
        reloadOnCompare(devId, meterMngId, dispDirect, null);
    }

    /**
     * 表示更新(データ比較グラフ画面用).
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param dispDirect 表示方向
     * @param dispTimeUnit 表示時間単位
     */
    protected void reloadOnCompare(String devId, Long meterMngId, String dispDirect, String dispTimeUnit) {
        DataViewSearchCondition condition = getPresentCondition();
        condition.setDevId(devId);
        condition.setDispDirect(dispDirect);
        condition.setDispTimeUnit(dispTimeUnit);

        // メーター管理番号
        List<Long> meterMngIdList = new ArrayList<>();
        meterMngIdList.add(meterMngId);
        condition.setMeterMngIdList(meterMngIdList);

        reload();
    }

    /**
     * 表示更新.
     */
    protected void reload() {
        DataViewSearchCondition condition = getPresentCondition();

        if(condition.getDevId() != null && condition.getDevId().equals("0")) {
            reloadAllDev();
        }else {
            reloadOnlyDev();
        }
    }

    /**
     * 全て表示ではない
     * （単一項目）
     */
    public void reloadOnlyDev() {
        setOutputKind(OUTPUT_KIND.DISP);

        ListInfo buildingInfo = getBuildingInfo();

        DataViewBeanProperty property = getProperty();

        Date selectedDate = getCalendarBean().getSelectedDate();

        prepareDate(selectedDate);

        DataViewSearchCondition condition = getPresentCondition();

        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(condition.getDispType());

        setDispType(DISP_TYPE.getTarget(condition.getDispType()));

        //選択中の装置
        String selectedDevId = condition.getDevId();

        //日報データ画面・データ比較グラフ(日報)画面の場合
        //IoT-Rだったら表示時間単位を1時間にする
        if (isDaily()) {
            setDispTimeUnitMapForIotr(selectedDevId);
        }

        List<List<String>> loadSurveyDataList = getLoadSurveyDataList();
        if (CollectionUtils.isNotEmpty(loadSurveyDataList)) {
            loadSurveyDataList.clear();
        }

        List<DataViewRow> dispDataList = getDispDataList();
        if (CollectionUtils.isNotEmpty(dispDataList)) {
            dispDataList.clear();
        }

        Map<String, List<String>> graphListMap = getGraphListMap();
        if (graphListMap != null) {
            graphListMap.clear();
        }

        Map<String, String> graphMaxDemandPeriodMap = getGraphMaxDemandPeriodMap();
        if (graphMaxDemandPeriodMap != null) {
            graphMaxDemandPeriodMap.clear();
        }

        Map<String, String> graphMaxDemandLastYearMap = getGraphMaxDemandLastYearMap();
        if (graphMaxDemandLastYearMap != null) {
            graphMaxDemandLastYearMap.clear();
        }

        Map<String, String> graphMaxDemandPastMap = getGraphMaxDemandPastMap();
        if (graphMaxDemandPastMap != null) {
            graphMaxDemandPastMap.clear();
        }

        List<String> targetDateTimeList = createTargetDateTimeList(condition);

        if (isMonthly()) {
            // 月報データ画面表示時
            setStartDateTimeList(createStartDateList(selectedDate));
        }

        setDispDateList(createDispDateList(targetDateTimeList));

        // 表示データ取得
        if (isReportMode()) {
            Map<String, Long> allMeterMngIdMap = toolsPullDownList.getMeterMngId(buildingInfo.getCorpId(),
                    Long.parseLong(buildingInfo.getBuildingId()), buildingInfo.getTenantFlg(), condition.getDevId(),
                    null, condition.getTenantName(), isReportMode());

            setMeterMngIdMap(allMeterMngIdMap);

            List<Long> meterMngIdList = toolsPullDownList.createTargetMeterMngIdList(allMeterMngIdMap, getPageNo(),
                    getPageDataCountView());
            condition.setMeterMngIdList(meterMngIdList);
        }

        String listDateTimeformat = property.getListDateTimeFormat();
        if (!DateUtility.DATE_FORMAT_HHMM_COLON.equals(listDateTimeformat)) {
            targetDateTimeList.remove(targetDateTimeList.size() - 1);
        }

        condition.setTargetDateTimeList(targetDateTimeList);

        ListSmsDataViewResult result = getResultData(buildingInfo, getProperty(), condition, getPageNo());

        setDownloadButtonDisabled(false);

        if (result == null) {
            setDownloadButtonDisabled(true);

        } else if(Objects.isNull(result.getLoadSurveyDataMap())){
        	setDownloadButtonDisabled(true);
        } else {
            Map<String, ListSmsDataViewResultData> targetDataMap = result.getLoadSurveyDataMap();

            setDownloadButtonDisabled(MapUtils.isEmpty(targetDataMap));

            int countX = result.getCountX() + 1;
            int countY = result.getCountY();

            dispDataList = createDispDataList(targetDataMap, countX, countY, getDispDateList(),
                    condition.getDispType());

            loadSurveyDataList = createResultDataStrList(
                    DATA_VIEW_HEADER.getTargetHeaderList(getDispType(), getOutputKind()),
                    DATA_VIEW_FOOTER.getTargetFooterList(getDispType(), getDataViewUnit()), dispDataList);

            // 装置名のヘッダを削除
            loadSurveyDataList.remove(0);

            setLoadSurveyDataList(loadSurveyDataList);

            setDispDataList(dispDataList);

            if (CollectionUtils.isNotEmpty(loadSurveyDataList)) {
                boolean isAllDevice = false;
                setGraphListMap(createGraphListMap(targetDataMap, countX, countY, isAllDevice));
                setGraphMaxDemandListMap(createMaxDemandGraphListMap(targetDataMap, countX, countY, isAllDevice));
            }
        }

        setDispCondition(condition);
        setGraphIconHidden(!isUse);
    }

    public void reloadAllDev() {
        setOutputKind(OUTPUT_KIND.DISP);

        ListInfo buildingInfo = getBuildingInfo();

        DataViewBeanProperty property = getProperty();

        Date selectedDate = getCalendarBean().getSelectedDate();

        prepareDate(selectedDate);

        DataViewSearchCondition condition = getPresentCondition();

        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(condition.getDispType());

        setDispType(DISP_TYPE.getTarget(condition.getDispType()));

        List<List<String>> loadSurveyDataList = getLoadSurveyDataList();
        if (CollectionUtils.isNotEmpty(loadSurveyDataList)) {
            loadSurveyDataList.clear();
        }

        List<DataViewRow> dispDataList = getDispDataList();
        if (CollectionUtils.isNotEmpty(dispDataList)) {
            dispDataList.clear();
        }

        Map<String, List<String>> graphListMap = getGraphListMap();
        if (graphListMap != null) {
            graphListMap.clear();
        }

        Map<String, String> graphMaxDemandPeriodMap = getGraphMaxDemandPeriodMap();
        if (graphMaxDemandPeriodMap != null) {
            graphMaxDemandPeriodMap.clear();
        }

        Map<String, String> graphMaxDemandLastYearMap = getGraphMaxDemandLastYearMap();
        if (graphMaxDemandLastYearMap != null) {
            graphMaxDemandLastYearMap.clear();
        }

        Map<String, String> graphMaxDemandPastMap = getGraphMaxDemandPastMap();
        if (graphMaxDemandPastMap != null) {
            graphMaxDemandPastMap.clear();
        }

        List<String> targetDateTimeList = createTargetDateTimeList(condition);

        if (isMonthly()) {
            // 月報データ画面表示時
            setStartDateTimeList(createStartDateList(selectedDate));
        }

        setDispDateList(createDispDateList(targetDateTimeList));

        // 表示データ取得
        if (isReportMode()) {
            Map<String, Long> allMeterMngIdMap = toolsPullDownList.getMeterMngId(buildingInfo.getCorpId(),
                    Long.parseLong(buildingInfo.getBuildingId()), buildingInfo.getTenantFlg(), condition.getDevId(),
                    null, condition.getTenantName(), isReportMode());

            setMeterMngIdMap(allMeterMngIdMap);

            List<Long> meterMngIdList = toolsPullDownList.createTargetMeterMngIdList(allMeterMngIdMap, getPageNo(),
                    getPageDataCountView());
            condition.setMeterMngIdList(meterMngIdList);

            // 装置毎にメーター管理番号を持つ
            Map<String, List<Long>> devIdMeterMngIdListMap = toolsPullDownList.createDevIdMeterMngIdListMap(allMeterMngIdMap, getPageNo(), getPageDataCountView());
            condition.setDevIdMeterMngIdListMap(devIdMeterMngIdListMap);
        }

        String listDateTimeformat = property.getListDateTimeFormat();
        if (!DateUtility.DATE_FORMAT_HHMM_COLON.equals(listDateTimeformat)) {
            targetDateTimeList.remove(targetDateTimeList.size() - 1);
        }

        condition.setTargetDateTimeList(targetDateTimeList);

        ListSmsDataViewResult result = getResultAllDevData(buildingInfo, getProperty(), condition, getPageNo(), getMeterMngIdMap());

        setDownloadButtonDisabled(false);
        if (result == null) {
            setDownloadButtonDisabled(true);

        } else if(Objects.isNull(result.getLoadSurveyDataMap())){
        	setDownloadButtonDisabled(true);
        }
        else {
			if (result.getCountX() > 0) {
                Map<String, ListSmsDataViewResultData> targetDataMap = result.getLoadSurveyDataMap();

                setDownloadButtonDisabled(MapUtils.isEmpty(targetDataMap));

                int countX = result.getCountX() + 1;
                int countY = result.getCountY();

                dispDataList = createDispDataAllDevList(targetDataMap, countX, countY, getDispDateList(),
                        condition.getDispType());

                loadSurveyDataList = createResultDataStrList(
                        DATA_VIEW_HEADER.getTargetHeaderList(getDispType(), getOutputKind()),
                        DATA_VIEW_FOOTER.getTargetFooterList(getDispType(), getDataViewUnit()), dispDataList);

                setLoadSurveyDataList(loadSurveyDataList);

                setDispDataList(dispDataList);

                if (CollectionUtils.isNotEmpty(loadSurveyDataList)) {
                    boolean isAllDevice = true;
                    setGraphListMap(createGraphListMap(targetDataMap, countX, countY, isAllDevice));
                    setGraphMaxDemandListMap(createMaxDemandGraphListMap(targetDataMap, countX, countY, isAllDevice));
                }
            } else {
                setDownloadButtonDisabled(true);
            }
        }

        setDispCondition(condition);
        setGraphIconHidden(!isUse);
    }

    /**
     * 装置プルダウン変更時.
     */
    protected void changeDevIdPulldown() {
        DataViewSearchCondition condition = getPresentCondition();

        String devId = condition.getDevId();
        if (CheckUtility.isNullOrEmpty(devId)) {
            return;
        }

        changeDevIdPulldown(devId, null, condition.getDispDirect(), condition.getTenantName());
    }

    /**
     * 装置プルダウン変更時.
     *
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param dispDirect 表示方向
     * @param tenantName テナント名
     */
    protected void changeDevIdPulldown(String devId, String meterKind, String dispDirect, String tenantName) {
        updateDispDirectMap(devId, getDevIdMap());

        Map<String, String> dispDirectMap = getDispDirectMap();
        if (CheckUtility.isNullOrEmpty(MapUtility.searchValueOfKeyName(dispDirectMap, dispDirect))) {
            Entry<String, String> dispDirectMapEntry = dispDirectMap.entrySet().stream().findFirst().orElse(null);
            if (dispDirectMapEntry != null) {
                dispDirect = dispDirectMapEntry.getValue();
            }
        }

        DataViewSearchCondition condition = getPresentCondition();
        condition.setDispDirect(dispDirect);

        updateMeterMngIdMap(devId, meterKind, tenantName);

        //日報データ・データ比較グラフ（日報）の場合
        // IoT-Rのときは表示時間単位を1時間にする
        if (isDaily()) {
        	changeDispTimeUnitMap(devId);
        }

    }

    /**
     * テナント名入力時.
     */
    protected void inputTenantName() {
        DataViewSearchCondition condition = getPresentCondition();

        updateMeterMngIdMap(condition.getDevId(), null, condition.getTenantName());
    }

    /**
     * テナント名入力時.
     *
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param tenantName テナント名
     */
    protected void inputTenantName(String devId, String meterKind, String tenantName) {
        updateMeterMngIdMap(devId, meterKind, tenantName);
    }

    /**
     * 表示方向プルダウンMap更新.
     *
     * @param devId 装置ID
     * @param devIdMap 装置情報Map
     */
    protected void updateDispDirectMap(String devId, Map<String, String> devIdMap) {
        setDispDirectMap(toolsPullDownList.getDispDirect(devId, devIdMap));
    }

    /**
     * メーター表示切替(ページ番号・メーター管理番号)プルダウンMap更新.
     *
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param tenantName テナント名
     */
    protected void updateMeterMngIdMap(String devId, String meterKind, String tenantName) {
        ListInfo buildingInfo = getBuildingInfo();

        setMeterMngIdMap(toolsPullDownList.getMeterMngId(buildingInfo.getCorpId(),
                Long.parseLong(buildingInfo.getBuildingId()), buildingInfo.getTenantFlg(), devId, meterKind,
                tenantName, isReportMode()));
        setMeterMngIdPageNoMap(toolsPullDownList.createMngMeterIdPageMap(getMeterMngIdMap(), getPageDataCountView(), isReportMode(), devId));
    }

    /**
     * 表示時間単位プルダウンMap切り替え
     * 装置IDプルダウンを操作したときに呼び出される
     * IoT-Rが選択される -> 表示時間単位：１時間
     * DM3など -> 表示時間単位：30分（デフォルト）、1時間
     *
     */
    protected void changeDispTimeUnitMap(String devId) {
    	Map<String, String> dispTimeUnitMap = toolsPullDownList.getDispTimeUnit();
    	DataViewSearchCondition condition = getPresentCondition();

    	if(devId.startsWith(SmsConstants.DEVICE_KIND.IOTR.getVal())) {
    		dispTimeUnitMap.remove(MapUtility.searchValueOfKeyName(dispTimeUnitMap, SmsConstants.DISP_TIME_UNIT.M30.getVal()));
    		setDispTimeUnitMap(dispTimeUnitMap);

    		condition.setDispTimeUnit(SmsConstants.DISP_TIME_UNIT.M60.getVal());
    		updateStartTimePulldown();                // 表示時間単位に合わせて開始時刻の刻みも変更しておく
    	} else {
    		setDispTimeUnitMap(dispTimeUnitMap);
    		//初期読み込みのprepareと同じ設定値
    		condition.setDispTimeUnit(SmsConstants.DISP_TIME_UNIT.M30.getVal());
    		setStartTime(SmsConstants.DISP_TYPE.USE.getStartTime());
    		updateStartTimePulldown();
    	}

    }


    /**
     * 表示時間単位プルダウンMapセット
     * 初期読み込み時とリロード時に呼び出される
     * 選択されている装置がIoT-Rだったときに
     * 表示時間単位を1時間にする
     *
     */
    protected void setDispTimeUnitMapForIotr(String devId) {
    	Map<String, String> dispTimeUnitMap = toolsPullDownList.getDispTimeUnit();
    	DataViewSearchCondition condition = getPresentCondition();

    	if(devId.startsWith(SmsConstants.DEVICE_KIND.IOTR.getVal())) {
    		dispTimeUnitMap.remove(MapUtility.searchValueOfKeyName(dispTimeUnitMap, SmsConstants.DISP_TIME_UNIT.M30.getVal()));
    		setDispTimeUnitMap(dispTimeUnitMap);

    		condition.setDispTimeUnit(SmsConstants.DISP_TIME_UNIT.M60.getVal());
    		updateStartTimePulldown();                // 表示時間単位に合わせて開始時刻の刻みも変更しておく
    	}
    }


    /**
     * 対象年度Map更新.
     */
    protected void updateDispYearMap() {
        setDispYearMap(toolsPullDownList.getDispYearMap());
    }

    /**
     * 対象日時Listを生成.
     *
     * @param condition 検索条件
     * @return 対象日時List
     */
    private List<String> createTargetDateTimeList(DataViewSearchCondition condition) {
        DataViewBeanProperty property = getProperty();

        String startDateTimeStr = getDispYear() + getStartMonth() + getStartDay();

        String targetDateTimeFormat = property.getTargetDateTimeFormat();

        List<String> targetDateTimeList = new ArrayList<>();
        if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)) {
            targetDateTimeList = createStartTimeStrList(targetDateTimeFormat, startDateTimeStr, getStartTime());

        } else {
            targetDateTimeList = createTargetDateStrList(startDateTimeStr, targetDateTimeFormat,
                    property.getTargetDateTimeDuration(), property.getTargetDateTimeAddtion());
        }

        return targetDateTimeList;
    }

    /**
     * 対象日時Listを生成(月報データ出力用).
     *
     * @param property プロパティクラス
     * @return 対象日時List
     */
    private List<String> createTargetDateTimeListForMonthly(DataViewBeanProperty property) {
        return createTargetDateTimeList(property, "yyyy/MM/dd");
    }

    /**
     * 対象日時Listを生成.
     *
     * @param property プロパティクラス
     * @param dateTimeFormat 対象日時フォーマット
     * @return 対象日時List
     */
    private List<String> createTargetDateTimeList(DataViewBeanProperty property, String dateTimeFormat) {
        List<String> targetDateTimeList = createTargetDateStrList(getDispYear() + getStartMonth() + getStartDay(),
                dateTimeFormat, property.getTargetDateTimeDuration(), property.getTargetDateTimeAddtion());

        if (CollectionUtils.isNotEmpty(targetDateTimeList)) {
            targetDateTimeList.remove(targetDateTimeList.size() - 1);
        }

        return targetDateTimeList;
    }

    /**
     * 開始時刻プルダウンを更新.
     */
    protected void updateStartTimePulldown() {
        DataViewBeanProperty property = getProperty();

        setStartDateTimeList(createStartTimeList(property.getTargetDateTimeFormat(),
                property.getListDateTimeFormat(), DateUtility.DATE_FORMAT_HHMM,
                DateUtility.changeDateFormat(mVariousDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDD), null));
    }

    /**
     * 文字列から整数に変換.
     *
     * @param target 対象文字列
     * @param isNegative 負数フラグ
     * @return 整数
     */
    private int changeInteger(String target, boolean isNegative) {
        int result = 0;
        try {
            if (isNegative) {
                result = Integer.parseInt(target);
            } else {
                result = Integer.parseUnsignedInt(target);
            }

            return result;

        } catch (Exception e) {
            return result;
        }
    }

    /**
     * ファイル名禁止文字エスケープMapを生成.
     */
    private Map<String, String> createFileNameEscapeMap() {
        Map<String, String> fileNameEscapeMap = new HashMap<>();
        fileNameEscapeMap.put("\\", "￥");
        fileNameEscapeMap.put("/", "／");
        fileNameEscapeMap.put(":", "：");
        fileNameEscapeMap.put("*", "＊");
        fileNameEscapeMap.put("?", "？");
        fileNameEscapeMap.put("\"", "”");
        fileNameEscapeMap.put("<", "＜");
        fileNameEscapeMap.put(">", "＞");
        fileNameEscapeMap.put("|", "｜");

        return fileNameEscapeMap;
    }

    /**
     * 開始時刻プルダウンList生成.
     *
     * @param startDateTimeStr 開始日時文字列
     * @param targetDateTimeFormat 対象日時フォーマット
     * @param labelFormat プルダウンラベルフォーマット
     * @param valueFormat プルダウン選択値フォーマット
     * @param startDateStr 開始日付文字列
     * @param startTimeStr 開始時刻文字列
     * @return 開始時刻プルダウンList
     */
    private List<SelectItem> createStartTimeList(String targetDateTimeFormat,
            String labelFormat, String valueFormat, String startDateStr, String startTimeStr) {

        List<String> targetDateTimeList = createStartTimeStrList(targetDateTimeFormat, startDateStr, startTimeStr);
        List<SelectItem> startDateTimeList = new ArrayList<>();

        for (int i = 0; i < targetDateTimeList.size(); i++) {
            Date targetDateTime = DateUtility.conversionDate(targetDateTimeList.get(i), targetDateTimeFormat);

            SelectItem item = new SelectItem(DateUtility.changeDateFormat(targetDateTime, valueFormat),
                    DateUtility.changeDateFormat(targetDateTime, labelFormat));
            startDateTimeList.add(item);
        }

        return startDateTimeList;
    }

    /**
     * 開始時刻文字列Listを生成.
     *
     * @param targetDateTimeFormat 対象日時フォーマット
     * @param startDateStr 開始日付文字列
     * @param startTimeStr 開始時刻文字列
     * @return 開始時刻文字列List
     */
    private List<String> createStartTimeStrList(String targetDateTimeFormat, String startDateStr, String startTimeStr) {
        DataViewSearchCondition condition = getPresentCondition();

        // 表示種別
        DISP_TYPE dispTypeEnum = DISP_TYPE.getTarget(condition.getDispType());
        // 表示時間単位
        DISP_TIME_UNIT dispTimeUnitEnum = DISP_TIME_UNIT.getTarget(condition.getDispTimeUnit());

        String addition = dispTypeEnum.getAddition();
        if (DISP_TIME_UNIT.M60 == dispTimeUnitEnum) {
            // 表示時間単位が1時間の場合
            addition = "PT23H";
        }

        if (CheckUtility.isNullOrEmpty(startTimeStr)) {
            startTimeStr = dispTypeEnum.getStartTime();
        }

        return createTargetDateTimeStrList((startDateStr + startTimeStr), targetDateTimeFormat,
                dispTimeUnitEnum.getDuration(), addition);
    }

    /**
     * 帳票出力(CSV).
     */
    protected void downloadCsvFile() {
        downloadCsvFile(null);
    }

    /**
     * 帳票出力(CSV).
     *
     * @param downloadFileName ダウンロードファイル名
     */
    protected void downloadCsvFile(String downloadFileName) {
        setOutputKind(OUTPUT_KIND.CSV);

        setSaveFileName(null);

        DataViewSearchCondition condition = getDispCondition();

        DataViewBeanProperty property = getProperty();

        ListInfo buildingInfo = getBuildingInfo();

        // 出力データ取得
        ListSmsDataViewResult result = null;
        if(condition.getDevId() != null && condition.getDevId().equals("0")) {
            result = getResultAllDevData(buildingInfo, property, condition, null, getMeterMngIdMap());
        } else {
            result = getResultData(buildingInfo, getProperty(), condition, null);
        }
        if (result == null) {
            return;
        }

        Map<String, ListSmsDataViewResultData> resultDataMap = result.getLoadSurveyDataMap();
        int countX = result.getCountX() + 1;
        int countY = result.getCountY();

        List<String> outputDateList = getDispDateList();
        if (isMonthly()) {
            // 月報データ出力時
            outputDateList = createTargetDateTimeListForMonthly(property);
        }

        // 建物情報
        TBuilding tBuilding =
                buildingInfoDao.getBuildingInfo(
                        buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));

        boolean cloudFlg = hasBuildingNoInBiko(buildingInfo.getBuildingNo(), tBuilding.getBiko());

        List<DATA_VIEW_HEADER> headerList = DATA_VIEW_HEADER.getTargetHeaderList(getDispType(), getOutputKind());
        if (!cloudFlg) {
            headerList.remove(DATA_VIEW_HEADER.METER_ID);
        }

        // 全部以外の場合
        if(!presentCondition.getDevId().equals("0")) {
            headerList.remove(DATA_VIEW_HEADER.DEV_NAME);
        }

        List<List<String>> csvDataList = createResultDataStrList(
                headerList,
                DATA_VIEW_FOOTER.getTargetFooterList(getDispType(), getDataViewUnit()), createDispDataList(
                        resultDataMap, countX, countY, outputDateList, condition.getDispType()));

        if (CheckUtility.isNullOrEmpty(downloadFileName)) {
            downloadFileName = createDownloadFileName(cloudFlg) + ".csv";
        }

        setSaveFileName(downloadFileName);

        String outputDir = Paths
                .get(getCsvOutputDir(),
                        DateUtility.changeDateFormat(mVariousDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDD))
                .toString();

        setDownloadFilePath(Paths.get(outputDir, getSaveFileName()).toString());

        new DailyCsvConverter().csvPrint(outputDir, getSaveFileName(), csvDataList);
    }

    /**
     * 印刷出力(PDF).
     */
    protected void downloadPdfFile() {
        String pdfPath ="";
        if(presentCondition.getDevId().equals("0")) {
            pdfPath = getProperty().getPdfTemplateFileNameAllDevice();
        } else {
            pdfPath = getProperty().getPdfTemplateFileName();
        }
        downloadPdfFile(createDownloadFileName(true) + ".pdf", pdfPath);
    }

    /**
     * 印刷出力(PDF).
     *
     * @param downloadFileName ダウンロードファイル名
     * @param templateFileName テンプレートファイル名
     */
    protected void downloadPdfFile(String downloadFileName, String templateFileName) {
        setOutputKind(OUTPUT_KIND.PDF);

        setSaveFileName(null);

        DataViewSearchCondition condition = getDispCondition();

        DataViewBeanProperty property = getProperty();

        Predicate<SelectItem> filterCont = startDateTimeObj -> ((isDaily()
                && startDateTimeObj.getValue().equals(getStartTime()))
                || (isMonthly() && startDateTimeObj.getValue().equals(getStartDay()))
                || (isYearly() && startDateTimeObj.getValue().equals(getStartMonth())));

        SelectItem selectedDateTime = getStartDateTimeList().stream().filter(filterCont).findFirst().orElse(null);

        String startDateTimeLabel = "";
        if (selectedDateTime != null) {
            startDateTimeLabel = selectedDateTime.getLabel();
        }

        // 出力データ取得
        ListSmsDataViewResult result = null;
        int pagecount = PAGE_DATA_COUNT_PDF;
        if(condition.getDevId() != null && condition.getDevId().equals("0")) {
            result = getResultAllDevData(buildingInfo, property, condition, null, getMeterMngIdMap());
            pagecount = PAGE_ALL_DEV_DATA_COUNT_PDF;
        } else {
            result = getResultData(buildingInfo, getProperty(), condition, null);

        }
        if (result == null) {
            return;
        }

        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("devName", MapUtility.searchValueOfKeyName(getDevIdMap(), condition.getDevId()));
        paramMap.put("dispDirect", MapUtility.searchValueOfKeyName(getDispDirectMap(), condition.getDispDirect()));
        paramMap.put("dispType", MapUtility.searchValueOfKeyName(getDispTypeMap(), condition.getDispType()));
        paramMap.put("startDateTime", startDateTimeLabel);
        paramMap.put("title", property.getTitleReport());
        paramMap.put("dispDateLabel", property.getTitleDispDate());
        paramMap.put("dispDate",
                DateUtility.changeDateFormat(getCalendarBean().getSelectedDate(), property.getDispDateTimeFormat()));
        paramMap.put("startDateTimeLabel", property.getTitleStartDate());

        if (isDaily()) {
            paramMap.put("dispTimeUnit",
                    MapUtility.searchValueOfKeyName(getDispTimeUnitMap(), condition.getDispTimeUnit()));
        }

        List<String> outputDateList = getDispDateList();
        if (isMonthly()) {
            // 月報データ出力時
            outputDateList = createTargetDateTimeListForMonthly(property);
        }


        Collection<Map<String, ?>> detailClct = createPdfDataMapClct(
                DATA_VIEW_HEADER.getTargetHeaderList(getDispType(), getOutputKind()),
                DATA_VIEW_FOOTER.getTargetFooterList(getDispType(), getDataViewUnit()), result.getLoadSurveyDataMap(),
                result.getCountX(), result.getCountY(), outputDateList, condition.getDispType(), pagecount);

        setSaveFileName(downloadFileName);

        String downloadFilePath = PdfUtility.createPdf(paramMap, detailClct,
                this.getLoginUserId().toString() + "_" + getSaveFileName(),
                Paths.get(getPdfTemplateDir(), templateFileName).toString(), mVariousDao.getSvDate(),
                getPdfOutputDir());

        setDownloadFilePath(downloadFilePath);
    }

    /**
     * 建物備考内に建物番号が含まれるかチェック.
     *
     * @param buildingNo 建物番号
     * @param buildingBiko 建物備考
     * @return true:含まれる
     */
    private boolean hasBuildingNoInBiko(String buildingNo, String buildingBiko) {
        if (!CheckUtility.isNullOrEmpty(buildingBiko) && buildingBiko.contains(buildingNo)) {
            return true;
        }

        return false;
    }

    /**
     * ダウンロードファイル名を生成.
     *
     * @param dateByUnitFlg 日月年報ごとのファイル名分岐フラグ
     * @return ダウンロードファイル名
     */
    private String createDownloadFileName(boolean dateByUnitFlg) {
        ListInfo buildingInfo = getBuildingInfo();
        DataViewBeanProperty property = getProperty();
        DataViewSearchCondition condition = getDispCondition();

        Set<Entry<String, String>> fileNameEscapeMapEntrySet = getFileNameEscapeMap().entrySet();
        List<String> escapeTargetList = new ArrayList<>();
        List<String> replacementList = new ArrayList<>();
        for (Entry<String, String> fileNameEscapeMapEntry : fileNameEscapeMapEntrySet) {
            escapeTargetList.add(fileNameEscapeMapEntry.getKey());
            replacementList.add(fileNameEscapeMapEntry.getValue());
        }

        String[] escapeTargetArray = escapeTargetList.stream().toArray(String[]::new);
        String[] replacementArray = replacementList.stream().toArray(String[]::new);

        String buildingName = StringUtils.replaceEach(buildingInfo.getBuildingName(), escapeTargetArray,
                replacementArray);
        String devName = StringUtils.replaceEach(MapUtility.searchValueOfKeyName(getDevIdMap(), condition.getDevId()),
                escapeTargetArray, replacementArray);

        // 日付(yyyymmddhhmmss)
        String fileNameSuffix =
                DateUtility.changeDateFormat(mVariousDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        if (dateByUnitFlg) {
            fileNameSuffix = createTargetDateSuffix();
        }

        // 画面名_施設名称_装置名_接尾辞
        return String.format(
                DOWNLOAD_FILE_NAME_FORMAT, property.getTitleReport(), buildingName, devName, fileNameSuffix);
    }

    /**
     * 出力対象年月日接尾辞を生成.
     *
     * @return 出力対象年月日接尾辞
     */
    private String createTargetDateSuffix() {
        String targetMonth = getStartMonth();
        String targetDay = getStartDay();

        String targetDateSuffix = getDispYear();
        if (isDaily()) {
            // 日報出力時
            targetDateSuffix += targetMonth + targetDay;

        } else if (isMonthly()) {
            // 月報出力時
            targetDateSuffix += targetMonth;
        }

        return targetDateSuffix;
    }

    /**
     * ファイルダウンロード開始.
     */
    protected void downloadFileStart() {
        String saveFileName = getSaveFileName();
        if (CheckUtility.isNullOrEmpty(saveFileName)) {
            return;
        }

        fileDownloader.fileDownload(getDownloadFilePath(), saveFileName);
    }

    /**
     * 結果データを取得.
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     * @param condition 検索条件
     * @param pageNo ページ番号
     * @return 結果データ
     */
    private ListSmsDataViewResult getResultData(ListInfo buildingInfo, DataViewBeanProperty property,
            DataViewSearchCondition condition, Integer pageNo) {

        // 企業ID
        String corpId = buildingInfo.getCorpId();
        // 建物ID
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());
        // テナントフラグ
        boolean isTenant = buildingInfo.getTenantFlg();

        List<Long> meterMngIdList = condition.getMeterMngIdList();
        if (CollectionUtils.isEmpty(meterMngIdList)) {
            return null;
        }

        if (pageNo == null) {
            // ファイルダウンロード時
            Map<String, Long> allMeterMngIdMap = toolsPullDownList.getMeterMngId(buildingInfo.getCorpId(),
                    Long.parseLong(buildingInfo.getBuildingId()), buildingInfo.getTenantFlg(), condition.getDevId(),
                    null, condition.getTenantName(), isReportMode());

            meterMngIdList = new ArrayList<>(allMeterMngIdMap.values());
        }

        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(condition.getDispType());
        String dispTimeUnit = condition.getDispTimeUnit();

        ListSmsDataViewRequest request = new ListSmsDataViewRequest();
        request.setMeterMngIdList(meterMngIdList);
        request.setTargetDateTimeList(
                createTargetDateTimeListForSearch(condition.getTargetDateTimeList(), isUse, dispTimeUnit));

        ListSmsDataViewParameter parameter = new ListSmsDataViewParameter();
        parameter.setCorpId(corpId);
        parameter.setBuildingId(buildingId);
        parameter.setTenant(isTenant);
        parameter.setDevId(condition.getDevId());
        parameter.setForwardDiract(SmsConstants.DISP_DIRECT.FORWARD.getVal().equals(condition.getDispDirect()));
        parameter.setUse(isUse);
        parameter.setDispTimeUnit(dispTimeUnit);
        parameter.setTargetDateTimeFormat(property.getTargetDateTimeFormat());
        parameter.setRequest(request);

        return callApiPost(parameter, ListSmsDataViewResponse.class).getResult();
    }

    /**
     * 結果データを取得.
     *
     * @param buildingInfo 建物・テナント情報
     * @param property プロパティクラス
     * @param condition 検索条件
     * @param pageNo ページ番号
     * @param devIdMeterMngIdMap 装置名に紐づくメータ管理番号 Map
     * @return 結果データ
     */
    private ListSmsDataViewResult getResultAllDevData(ListInfo buildingInfo, DataViewBeanProperty property,
            DataViewSearchCondition condition, Integer pageNo, Map<String, Long> devIdMeterMngIdMap) {

        // 企業ID
        String corpId = buildingInfo.getCorpId();
        // 建物ID
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());
        // テナントフラグ
        boolean isTenant = buildingInfo.getTenantFlg();
        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(condition.getDispType());
        String dispTimeUnit = condition.getDispTimeUnit();
        List<Long> sizeList = new ArrayList<>();


        // 表示する範囲を選択
        Long fromMeterNumber = 0L;
        Long toMeterNumber = 0L;
        List<Long> meterMngIdList = condition.getMeterMngIdList();
        Map<String, List<Long>> devIdMeterMngIdListMap = condition.getDevIdMeterMngIdListMap();

        // pdf, csv出力の場合
        if(pageNo == null) {
            fromMeterNumber = 0L;
            toMeterNumber = (long) devIdMeterMngIdMap.size();
            Map<String, Long> allMeterMngIdMap = toolsPullDownList.getMeterMngId(buildingInfo.getCorpId(),
                    Long.parseLong(buildingInfo.getBuildingId()), buildingInfo.getTenantFlg(), condition.getDevId(),
                    null, condition.getTenantName(), isReportMode());
            meterMngIdList = new ArrayList<>(allMeterMngIdMap.values());
            devIdMeterMngIdListMap = toolsPullDownList.createDevIdMeterMngIdListMap(allMeterMngIdMap, null, getPageDataCountView());
        } else {
            fromMeterNumber = getPresentPageNoKey(getPageNo()) - 1L;
            toMeterNumber = getPresentPageNoKey(getPageNo() + 1) != null ? getPresentPageNoKey(getPageNo() + 1) - 1 : fromMeterNumber + condition.getMeterMngIdList().size();
        }

        sizeList.add(fromMeterNumber);
        sizeList.add(toMeterNumber);

        List<String> devIdList = new ArrayList<>();
        for(Map.Entry<String, String> entry : devIdMap.entrySet()) {
            devIdList.add(entry.getValue());
        }

        ListSmsDataViewAllDevRequest request = new ListSmsDataViewAllDevRequest();
        request.setMeterSizeList(sizeList);
        request.setMeterMngIdList(meterMngIdList);
        request.setTargetDateTimeList(
                createTargetDateTimeListForSearch(condition.getTargetDateTimeList(), isUse, dispTimeUnit));

        ListSmsDataViewAllDevParameter parameter = new ListSmsDataViewAllDevParameter();
        parameter.setCorpId(corpId);
        parameter.setBuildingId(buildingId);
        parameter.setTenant(isTenant);
        parameter.setDevIdList(devIdList);
        parameter.setForwardDiract(SmsConstants.DISP_DIRECT.FORWARD.getVal().equals(condition.getDispDirect()));
        parameter.setUse(isUse);
        parameter.setDispTimeUnit(dispTimeUnit);
        parameter.setTargetDateTimeFormat(property.getTargetDateTimeFormat());
        parameter.setDevIdMeterMngIdMap(devIdMeterMngIdMap);
        parameter.setDevIdMeterMngIdListMap(devIdMeterMngIdListMap);
        parameter.setRequest(request);
        ListSmsDataViewResult result = new ListSmsDataViewResult();
        try {
            result = listSmsDataViewAllDevDao.query(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    /**
     * 検索用対象日時Listを生成.
     *
     * @param targetDateTimeList 対象日時List
     * @param isUse 使用量フラグ
     * @param dispTimeUnit 表示時間単位
     * @return 検索用対象日時List
     */
    private List<String> createTargetDateTimeListForSearch(List<String> targetDateTimeList, boolean isUse,
            String dispTimeUnit) {
        if (CollectionUtils.isNotEmpty(targetDateTimeList)) {
            List<String> resultTargetDateTimeList = new ArrayList<>();

            targetDateTimeList.forEach(targetDateTime -> {
                int targetDateTimeLength = targetDateTime.length();
                String resultTargetDateTime = targetDateTime;
                if (targetDateTimeLength < 12) {
                    if (targetDateTimeLength == 6) {
                        resultTargetDateTime += "01";
                    }

                    resultTargetDateTime += "0000";

                } else {
                    if (isUse && SmsConstants.DISP_TIME_UNIT.M30.getVal().equals(dispTimeUnit)) {
                        // 日報30分使用量取得時
                        resultTargetDateTime = DateUtility.changeDateFormat(
                                DateUtility.plusMinute(DateUtility.conversionDate(resultTargetDateTime,
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM), 30),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                    }
                }

                resultTargetDateTimeList.add(resultTargetDateTime);
            });

            targetDateTimeList = resultTargetDateTimeList;
        }

        return targetDateTimeList;
    }

    /**
     * 表示データListを生成.
     *
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @param dispDateClct 表示日時Collection
     * @param dispType 表示種別
     * @return 表示データList
     */
    private List<DataViewRow> createDispDataList(
            Map<String, ListSmsDataViewResultData> resultDataMap, int countX, int countY,
            Collection<String> dispDateClct, String dispType) {

        List<DataViewRow> dispDataList = new ArrayList<>();

        if (MapUtils.isEmpty(resultDataMap)) {
            return dispDataList;
        }

        List<String> dispDateList = new ArrayList<>(dispDateClct);

        // 使用量選択時フラグ
        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(dispType);

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length;

        DataViewRow dataRow = null;
        BidiMap<String, String> bidiMap = new DualHashBidiMap<String, String>(devIdMap);
        for (int y = 0; y < countY; y++) {
            int rowIndex = y + startRowNo;

            String title = dispDateList.get(y);
            if (isDaily() && isUse) {
                // 日報データ表示時かつ使用量選択時
                title += "～";
            }

            dataRow = new DataViewRow(title);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, rowIndex));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDevId(resultData.getDevId());
                dataColumn.setDevName(bidiMap.getKey(resultData.getDevId()));
                dataColumn.setMeterMngIdDisp(resultData.getMeterMngIdDisp());
                dataColumn.setMeterId(resultData.getMeterId());
                dataColumn.setTenantName(resultData.getTenantName());
                dataColumn.setMeterTypeName(resultData.getMeterTypeName());
                dataColumn.setUnit(resultData.getUnitUsageBased());

                String dispValue = resultData.getKwh30Disp();
                if (!isUse) {
                    // 指針値選択時
                    dispValue = resultData.getDmvKwhDisp();
                }

                dataColumn.setDispValue(dispValue);

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);
        }

        if (isUse) {
            // 使用量選択時
            // 最大デマンド
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.MAX_DEMAND.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getMaxDemandSpecifiedPeriodDisp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

            // 使用量選択時
            // CO2換算値
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.CO2_CONVERT.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getCo2ConvertValueDisp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

            if (isDaily()) {
                // 日報データ表示時
                // 乗率
                dispDataList.add(createDispMultiRow(DATA_VIEW_FOOTER.MULTI.getDispName(), resultDataMap, startRowNo,
                        startColumnNo, countX, keyFormat));
            }

            // 合計電力量
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.TOTAL_AMOUNT.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getTotalKwh30Disp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

        } else {
            // 指針値選択時

            if (isDaily()) {
                // 日報データ表示時
                // 乗率
                dispDataList.add(createDispMultiRow(DATA_VIEW_FOOTER.MULTI.getDispName(), resultDataMap, startRowNo,
                        startColumnNo, countX, keyFormat));
            }
        }

        return dispDataList;
    }

    /**
     * 表示データListを生成.
     *
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @param dispDateClct 表示日時Collection
     * @param dispType 表示種別
     * @return 表示データList
     */
    private List<DataViewRow> createDispDataAllDevList(
            Map<String, ListSmsDataViewResultData> resultDataMap, int countX, int countY,
            Collection<String> dispDateClct, String dispType) {

        List<DataViewRow> dispDataList = new ArrayList<>();

        if (MapUtils.isEmpty(resultDataMap)) {
            return dispDataList;
        }

        List<String> dispDateList = new ArrayList<>(dispDateClct);
        BidiMap<String, String> bidiMap = new DualHashBidiMap<String, String>(devIdMap);

        // 使用量選択時フラグ
        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(dispType);

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length;

        DataViewRow dataRow = null;
        for (int y = 0; y < countY; y++) {
            int rowIndex = y + startRowNo;

            String title = dispDateList.get(y);
            if (isDaily() && isUse) {
                // 日報データ表示時かつ使用量選択時
                title += "～";
            }

            dataRow = new DataViewRow(title);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, rowIndex));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDevId(resultData.getDevId());
                dataColumn.setDevName(bidiMap.getKey(resultData.getDevId()));
                dataColumn.setMeterMngIdDisp(resultData.getMeterMngIdDisp());
                dataColumn.setMeterId(resultData.getMeterId());
                dataColumn.setTenantName(resultData.getTenantName());
                dataColumn.setMeterTypeName(resultData.getMeterTypeName());
                dataColumn.setUnit(resultData.getUnitUsageBased());

                String dispValue = resultData.getKwh30Disp();
                if (!isUse) {
                    // 指針値選択時
                    dispValue = resultData.getDmvKwhDisp();
                }

                dataColumn.setDispValue(dispValue);

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);
        }

        if (isUse) {
            // 使用量選択時
            // 最大デマンド
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.MAX_DEMAND.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getMaxDemandSpecifiedPeriodDisp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

            // 使用量選択時
            // CO2換算値
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.CO2_CONVERT.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getCo2ConvertValueDisp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

            if (isDaily()) {
                // 日報データ表示時
                // 乗率
                dispDataList.add(createDispMultiRow(DATA_VIEW_FOOTER.MULTI.getDispName(), resultDataMap, startRowNo,
                        startColumnNo, countX, keyFormat));
            }

            // 合計電力量
            dataRow = new DataViewRow(DATA_VIEW_FOOTER.TOTAL_AMOUNT.getDispName());
            dataRow.setFooter(true);

            for (int x = startColumnNo; x <= countX; x++) {
                ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
                if (resultData == null) {
                    continue;
                }

                DataViewColumn dataColumn = new DataViewColumn();
                dataColumn.setDispValue(resultData.getTotalKwh30Disp());

                dataRow.addColumnData(dataColumn);
            }

            dispDataList.add(dataRow);

        } else {
            // 指針値選択時

            if (isDaily()) {
                // 日報データ表示時
                // 乗率
                dispDataList.add(createDispMultiRow(DATA_VIEW_FOOTER.MULTI.getDispName(), resultDataMap, startRowNo,
                        startColumnNo, countX, keyFormat));
            }
        }

        return dispDataList;
    }

    /**
     * 乗率行を生成.
     *
     * @param title タイトル
     * @param resultDataMap 結果データMap
     * @param rowNo 行番号
     * @param startColumnNo 開始列番号
     * @param columnCount 列数
     * @param keyFormat 結果データMapキーフォーマット
     * @return 乗率行
     */
    private DataViewRow createDispMultiRow(String title, Map<String, ListSmsDataViewResultData> resultDataMap,
            int rowNo, int startColumnNo, int columnCount, String keyFormat) {

        DataViewRow dataRow = new DataViewRow(title);
        dataRow.setFooter(true);

        for (int x = startColumnNo; x <= columnCount; x++) {
            ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, rowNo));
            if (resultData == null) {
                continue;
            }

            DataViewColumn dataColumn = new DataViewColumn();
            dataColumn.setDispValue(resultData.getMultiDisp());

            dataRow.addColumnData(dataColumn);
        }

        return dataRow;
    }

    /**
     * 表示・出力データListを生成.
     *
     * @param headerList ヘッダーEnumのList
     * @param footerList フッターEnumのList
     * @param dateViewRowList 画面表示データList
     * @return 出力データList
     */
    private List<List<String>> createResultDataStrList(List<DATA_VIEW_HEADER> headerList,
            List<DATA_VIEW_FOOTER> footerList, List<DataViewRow> dateViewRowList) {

        List<List<String>> resultDataStrList = new ArrayList<>();

        // ヘッダーデータMap
        Map<DATA_VIEW_HEADER, List<String>> headerDataStrMap = new HashMap<>();

        List<DataViewColumn> columnDataList = dateViewRowList.get(0).getColumnDataList();
        BidiMap<String, String> bidiMap = new DualHashBidiMap<String, String>(devIdMap);

        for (DataViewColumn columnData : columnDataList) {
            for (DATA_VIEW_HEADER header : headerList) {
                List<String> headerColumnStrList = headerDataStrMap.getOrDefault(header, new ArrayList<>());

                String headerValue = null;
                if (header == DATA_VIEW_HEADER.DEV_NAME) {
                    // 装置名
                    headerValue = bidiMap.getKey(columnData.getDevId());

                } else if (header == DATA_VIEW_HEADER.METER_MNG_NO) {
                    // 管理番号
                    headerValue = columnData.getMeterMngIdDisp();

                } else if (header == DATA_VIEW_HEADER.METER_ID) {
                    // 計器ID
                    headerValue = columnData.getMeterId();

                } else if (header == DATA_VIEW_HEADER.TENANT_NAME) {
                    // テナント名
                    headerValue = columnData.getTenantName();

                } else if (header == DATA_VIEW_HEADER.METER_TYPE_NAME) {
                    // メーター種別
                    headerValue = columnData.getMeterTypeName();

                } else if (header == DATA_VIEW_HEADER.UNIT) {
                    // 単位
                    headerValue = columnData.getUnit();
                }

                headerColumnStrList.add(headerValue);
                headerDataStrMap.put(header, headerColumnStrList);
            }
        }

        // -- 1列目 --
        // タイトル及び対象日時列
        for (DATA_VIEW_HEADER header : headerList) {
            resultDataStrList.add(new ArrayList<>(Arrays.asList(header.getDispName())));
        }

        for (DataViewRow dateViewRow : dateViewRowList) {
            resultDataStrList.add(new ArrayList<>(Arrays.asList(dateViewRow.getTitle())));
        }

        // -- 2列目以降 --
        int headerCount = headerList.size();
        for (int rowIndex = 0; rowIndex < resultDataStrList.size(); rowIndex++) {
            List<String> resultDataStrColumnList = resultDataStrList.get(rowIndex);

            if (rowIndex < headerCount) {
                // ヘッダー行
                resultDataStrColumnList.addAll(headerDataStrMap.get(headerList.get(rowIndex)));

            } else {
                dateViewRowList.get(rowIndex - headerCount).getColumnDataList()
                .forEach(columnData -> resultDataStrColumnList.add(columnData.getDispValue()));
            }
        }

        return resultDataStrList;
    }

    /**
     * PDF出力用データMapCollectionを生成.
     *
     * @param headerList ヘッダーEnumのList
     * @param footerList フッターEnumのList
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @param dispDateList 表示日時List
     * @param dispType 表示種別
     * @param pageDataCount ページ表示件数
     * @return PDF出力用データMapCollection
     */
    private Collection<Map<String, ?>> createPdfDataMapClct(List<DATA_VIEW_HEADER> headerList,
            List<DATA_VIEW_FOOTER> footerList,
            Map<String, ListSmsDataViewResultData> resultDataMap, int countX, int countY,
            List<String> dispDateList, String dispType, int pageDataCount) {

        // 使用量選択時フラグ
        boolean isUse = SmsConstants.DISP_TYPE.USE.getVal().equals(dispType);

        Collection<Map<String, ?>> pdfDataMapClct = new ArrayList<>();
        Map<String, String> pdfDataMap = new LinkedHashMap<>();

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length + 1;

        int firstColumnNo = 1;

        // 1列目
        Map<String, String> firstColumnDataMap = new LinkedHashMap<>();
        int dispTimeRowNo = startRowNo;
        for (String dispDate : dispDateList) {

            if (isDaily() && isUse) {
                // 日報データ出力時かつ使用量選択時
                dispDate += "～";
            }

            firstColumnDataMap.put(String.format(keyFormat, firstColumnNo, dispTimeRowNo), dispDate);
            dispTimeRowNo++;
        }

        for (DATA_VIEW_FOOTER footer : footerList) {
            firstColumnDataMap.put(String.format(keyFormat, firstColumnNo, dispTimeRowNo++), footer.getDispName());
        }

        int rowCount = dispDateList.size() + startRowNo;

        int dataCount = 0;
        int currentColumnNo = startColumnNo;
        for (int x = 0; x < countX; x++) {
            if (dataCount == 0) {
                // 1列目
                pdfDataMap.putAll(firstColumnDataMap);
            }

            // 2列目以降
            ListSmsDataViewResultData targetData = resultDataMap
                    .get(String.format(keyFormat, (x + startColumnNo), startRowNo));

            // ヘッダー
            for (int i = 0; i < headerList.size(); i++) {
                int rowNo = i + 1;
                DATA_VIEW_HEADER header = headerList.get(i);

                String headerValue = null;
                if(header == DATA_VIEW_HEADER.DEV_NAME) {
                    for(Map.Entry<String, String> entry : getDevIdMap().entrySet()) {
                        if(entry.getValue().equals(targetData.getDevId())) {
                            headerValue = entry.getKey();
                        }
                    }
                }
                else if (header == DATA_VIEW_HEADER.METER_MNG_NO) {
                    // メーター管理番号
                    headerValue = targetData.getMeterMngIdDisp();

                } else if (header == DATA_VIEW_HEADER.METER_ID) {
                    // 計器ID
                    headerValue = targetData.getMeterId();

                } else if (header == DATA_VIEW_HEADER.TENANT_NAME) {
                    // テナント名
                    headerValue = targetData.getTenantName();

                } else if (header == DATA_VIEW_HEADER.METER_TYPE_NAME) {
                    // メーター種別
                    headerValue = targetData.getMeterTypeName();

                } else if (header == DATA_VIEW_HEADER.UNIT) {
                    // 単位
                    headerValue = targetData.getUnitUsageBased();
                }

                pdfDataMap.put(String.format(keyFormat, currentColumnNo, rowNo),
                        StringUtils.defaultIfEmpty(headerValue, " "));
            }

            // フッター
            for (int i = 0; i < footerList.size(); i++) {
                int rowNo = rowCount + i;
                DATA_VIEW_FOOTER footer = footerList.get(i);

                String footerValue = null;
                if(footer == DATA_VIEW_FOOTER.MAX_DEMAND) {
                    // 最大デマンド
                    footerValue = targetData.getMaxDemandSpecifiedPeriodDisp();
                }else if (footer == DATA_VIEW_FOOTER.CO2_CONVERT) {
                    // CO2換算値
                    footerValue = targetData.getCo2ConvertValueDisp();

                } else if (footer == DATA_VIEW_FOOTER.MULTI) {
                    // 乗率
                    footerValue = targetData.getMultiDisp();

                } else if (footer == DATA_VIEW_FOOTER.TOTAL_AMOUNT) {
                    // 合計量
                    footerValue = targetData.getTotalKwh30Disp();
                }

                pdfDataMap.put(String.format(keyFormat, currentColumnNo, rowNo),
                        StringUtils.defaultIfEmpty(footerValue, " "));
            }

            for (int y = 0; y < countY; y++) {
                ListSmsDataViewResultData targetDataValue = resultDataMap
                        .get(String.format(keyFormat, (x + startColumnNo), (y + (startRowNo - 1))));

                if (targetDataValue == null) {
                    continue;
                }

                String value = null;

                if (isUse) {
                    // 使用量選択時
                    // 30分使用電力量
                    value = targetDataValue.getKwh30Disp();

                } else {
                    // 指針値選択時
                    // 指針値データ
                    value = targetDataValue.getDmvKwhDisp();
                }

                pdfDataMap.put(String.format(keyFormat, currentColumnNo, (y + startRowNo)), value);
            }

            dataCount++;
            currentColumnNo++;
            if (pageDataCount == dataCount) {
                // 改ページ
                pdfDataMapClct.add(pdfDataMap);

                pdfDataMap = new LinkedHashMap<>();
                dataCount = 0;
                currentColumnNo = startColumnNo;
            }
        }

        if (!pdfDataMap.isEmpty()) {
            pdfDataMapClct.add(pdfDataMap);
        }

        return pdfDataMapClct;
    }

    /**
     * グラフ表示値ListMapを生成.
     *
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @param isAllDevice 全装置フラグ
     * @return グラフ表示値ListMap
     */
    private Map<String, List<String>> createGraphListMap(Map<String, ListSmsDataViewResultData> resultDataMap,
            int countX, int countY, boolean isAllDevice ) {

        Map<String, List<String>> amountMap = new HashMap<>();
        Map<String, List<String>> maxDemandAmountMap = new HashMap<>();
        Map<String, Map<String, String>> amountDayMap = new HashMap<>();
        Map<String, Map<String, String>> maxDemandAmountDayMap = new HashMap<>();

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length;

        List<String> dispDayListForCompare = getDispDayListForCompare();
        boolean dispDayCheckFlg = CollectionUtils.isNotEmpty(dispDayListForCompare);
        Integer valueScale = null;

        for (int x = startColumnNo; x <= countX; x++) {
            ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));
            if(resultData == null) {
                System.out.println("x = " + x);
            }

            // 全装置: メータID, 装置単体: メーター管理番号
            String meterInfo = isAllDevice ?  resultData.getMeterId() : resultData.getMeterMngIdDisp();

            List<String> amountList = amountMap.getOrDefault(meterInfo, new ArrayList<>());
            List<String> maxDeandAmountList = maxDemandAmountMap.getOrDefault(meterInfo, new ArrayList<>());
            Map<String, String> amountDayInnerMap = amountDayMap.getOrDefault(meterInfo, new HashMap<>());
            Map<String, String> maxDemandAmountDayInnerMap = maxDemandAmountDayMap.getOrDefault(meterInfo, new HashMap<>());
            for (int y = 0; y < countY; y++) {
                ListSmsDataViewResultData resultDataValue = resultDataMap
                        .get(String.format(keyFormat, x, (y + startRowNo)));
                if (resultDataValue == null) {
                    continue;
                }

                if (valueScale == null) {
                    valueScale = resultDataValue.getValueScale();
                }

                String amount = resultDataValue.getKwh30ForGraph();
                String maxDemandAmount = resultDataValue.getMaxDemandForGraph();


                amountList.add(amount);
                maxDeandAmountList.add(maxDemandAmount);

                if (dispDayCheckFlg) {
                    // データ比較グラフ(月報)画面表示時
                    String targetDay = DateUtility.changeDateFormat(
                            DateUtility.conversionDate(resultDataValue.getTargetDateTime(),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                            DateUtility.DATE_FORMAT_DD);

                    amountDayInnerMap.put(targetDay, amount);
                    maxDemandAmountDayInnerMap.put(targetDay, maxDemandAmount);
                }
            }

            amountMap.put(meterInfo, amountList);
            maxDemandAmountMap.put(meterInfo, maxDeandAmountList);

            if (!amountDayInnerMap.isEmpty()) {
                amountDayMap.put(meterInfo, amountDayInnerMap);
            }

            if (!maxDemandAmountDayInnerMap.isEmpty()) {
                maxDemandAmountDayMap.put(meterInfo, maxDemandAmountDayInnerMap);
            }
        }

        if (!amountDayMap.isEmpty()) {
            // データ比較グラフ(月報)画面表示時
            amountMap.clear();
            Set<Entry<String, Map<String, String>>> amountDayMapEntrySet = amountDayMap.entrySet();
            for (Entry<String, Map<String, String>> amountDayMapEntry : amountDayMapEntrySet) {
                String meterId = amountDayMapEntry.getKey();
                Map<String, String> amountDayInnerMap = amountDayMapEntry.getValue();
                List<String> amountList = amountMap.getOrDefault(meterId, new ArrayList<>());

                for (String dispDay : dispDayListForCompare) {
                    String amount = amountDayInnerMap.get(dispDay);
                    if (CheckUtility.isNullOrEmpty(amount)) {
                        // 当該日のデータが存在しない場合
                        amount = BigDecimal.ZERO.setScale(valueScale).toPlainString();
                    }

                    amountList.add(amount);
                }

                amountMap.put(meterId, amountList);
            }
        }

        return amountMap;
    }

    /**
     * グラフ表示値ListMapを生成.
     *
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @return グラフ表示値ListMap
     */
    private Map<String, List<String>> createMaxDemandGraphListMap(Map<String, ListSmsDataViewResultData> resultDataMap,
            int countX, int countY, boolean isAllDevice) {

        Map<String, List<String>> amountMap = new HashMap<>();
        Map<String, Map<String, String>> amountDayMap = new HashMap<>();

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length;

        Integer valueScale = null;

        for (int x = startColumnNo; x <= countX; x++) {
            ListSmsDataViewResultData resultData = resultDataMap.get(String.format(keyFormat, x, startRowNo));

            // 全装置: メータID, 装置単体: メーター管理番号
            String meterInfo = isAllDevice ? resultData.getMeterId() : resultData.getMeterMngIdDisp();

            List<String> amountList = amountMap.getOrDefault(meterInfo, new ArrayList<>());
            Map<String, String> amountDayInnerMap = amountDayMap.getOrDefault(meterInfo, new HashMap<>());
            String demandPeriod = "ー";
            String demandLastYear = "ー";
            String demandPast = "ー";

            for (int y = 0; y < countY; y++) {
                ListSmsDataViewResultData resultDataValue = resultDataMap
                        .get(String.format(keyFormat, x, (y + startRowNo)));
                if (resultDataValue == null) {
                    continue;
                }

                if (valueScale == null) {
                    valueScale = resultDataValue.getValueScale();
                }

                String amount = resultDataValue.getMaxDemandForGraph();

                if(resultDataValue.getMaxDemandSpecifiedPeriodDisp() != null && !resultDataValue.getMaxDemandSpecifiedPeriodDisp().equals("-")) {
                    demandPeriod = resultDataValue.getMaxDemandSpecifiedPeriodDisp();
                }

                if(resultDataValue.getMaxDemandLastYearDisp() != null) {
                    demandLastYear = resultDataValue.getMaxDemandLastYearDisp();
                }

                if(resultDataValue.getMaxDemandPastDisp() != null) {
                    demandPast = resultDataValue.getMaxDemandPastDisp() + " (" + resultDataValue.getMaxDemandPastYmdDisp() + ")";
                }

                amountList.add(amount);

            }

            amountMap.put(meterInfo, amountList);
            graphMaxDemandPeriodMap.put(meterInfo, demandPeriod);
            graphMaxDemandLastYearMap.put(meterInfo, demandLastYear);
            graphMaxDemandPastMap.put(meterInfo, demandPast);


            if (!amountDayInnerMap.isEmpty()) {
                amountDayMap.put(meterInfo, amountDayInnerMap);
            }

        }

        return amountMap;
    }

    /**
     * グラフ表示値ListMapを生成.
     *
     * @param resultDataMap 結果データMap
     * @param countX データ列数(メーター管理番号数)
     * @param countY データ行数(対象日時数)
     * @return
     * @return グラフ表示値ListMap
     */
    private Long getPresentPageNoKey(int targetValue) {
        for (Map.Entry<String, Integer> entry : getMeterMngIdPageNoMap().entrySet()) {
            if (targetValue == entry.getValue()) {
                return Long.parseLong(entry.getKey());
            }
        }
        return null;
    }

    public int getPageDataCountView() {
        return pageDataCountView;
    }

    public void setPageDataCountView(int pageDataCountView) {
        if (pageDataCountView == 0) {
            pageDataCountView = PAGE_DATA_COUNT_VIEW_DEFAULT;
        }

        this.pageDataCountView = pageDataCountView;
    }

    public void setBuildingInfo(ListInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public ListInfo getBuildingInfo() {
        return buildingInfo;
    }

    public DataViewBeanProperty getProperty() {
        return property;
    }

    public void setProperty(DataViewBeanProperty property) {
        this.property = property;
    }

    public DataViewSearchCondition getPresentCondition() {
        return presentCondition;
    }

    public void setPresentCondition(DataViewSearchCondition presentCondition) {
        this.presentCondition = presentCondition;
    }

    public DataViewSearchCondition getDispCondition() {
        return dispCondition;
    }

    public void setDispCondition(DataViewSearchCondition presentCondition) {
        DataViewSearchCondition dispCondition = new DataViewSearchCondition();
        dispCondition.setDevId(presentCondition.getDevId());
        dispCondition.setDispDirect(presentCondition.getDispDirect());
        dispCondition.setDispType(presentCondition.getDispType());
        dispCondition.setDispTimeUnit(presentCondition.getDispTimeUnit());
        dispCondition.setTenantName(presentCondition.getTenantName());
        dispCondition.setMeterMngIdList(presentCondition.getMeterMngIdList());
        dispCondition.setDevIdMeterMngIdListMap(presentCondition.getDevIdMeterMngIdListMap());
        dispCondition.setTargetDateTimeList(presentCondition.getTargetDateTimeList());
        dispCondition.setTargetDateTimeFormat(presentCondition.getTargetDateTimeFormat());

        this.dispCondition = dispCondition;
    }

    public DATA_VIEW_UNIT getDataViewUnit() {
        return dataViewUnit;
    }

    public void setDataViewUnit(DATA_VIEW_UNIT dataViewUnit) {
        this.dataViewUnit = dataViewUnit;
    }

    public DISP_TYPE getDispType() {
        return dispType;
    }

    public void setDispType(DISP_TYPE dispType) {
        this.dispType = dispType;
    }

    public OUTPUT_KIND getOutputKind() {
        return outputKind;
    }

    public void setOutputKind(OUTPUT_KIND outputKind) {
        this.outputKind = outputKind;
    }

    public boolean isReportMode() {
        return isReportMode;
    }

    public void setReportMode(boolean isReportMode) {
        this.isReportMode = isReportMode;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean isDaily) {
        this.isDaily = isDaily;
    }

    public boolean isMonthly() {
        return isMonthly;
    }

    public void setMonthly(boolean isMonthly) {
        this.isMonthly = isMonthly;
    }

    public boolean isYearly() {
        return isYearly;
    }

    public void setYearly(boolean isYearly) {
        this.isYearly = isYearly;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public CalendarBean getCalendarBean() {
        return calendarBean;
    }

    public void setCalendarBean(CalendarBean calendarBean) {
        this.calendarBean = calendarBean;
    }

    public boolean isDownloadButtonDisabled() {
        return downloadButtonDisabled;
    }

    public void setDownloadButtonDisabled(boolean downloadButtonDisabled) {
        this.downloadButtonDisabled = downloadButtonDisabled;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getDispYear() {
        return dispYear;
    }

    public void setDispYear(String dispYear) {
        this.dispYear = dispYear;
    }

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public void setDevIdMap(Map<String, String> devIdMap) {
        this.devIdMap = devIdMap;
    }

    public Map<String, Integer> getMeterMngIdPageNoMap() {
        return meterMngIdPageNoMap;
    }

    public void setMeterMngIdPageNoMap(Map<String, Integer> meterMngIdPageNoMap) {
        this.meterMngIdPageNoMap = meterMngIdPageNoMap;
    }

    public Map<String, Long> getMeterMngIdMap() {
        return meterMngIdMap;
    }

    public void setMeterMngIdMap(Map<String, Long> meterMngIdMap) {
        this.meterMngIdMap = meterMngIdMap;
    }

    public Map<String, String> getDispDirectMap() {
        return dispDirectMap;
    }

    public Map<String, String> getDispTypeMap() {
        return dispTypeMap;
    }

    public void setDispTypeMap(Map<String, String> dispTypeMap) {
        this.dispTypeMap = dispTypeMap;
    }

    public List<SelectItem> getStartDateTimeList() {
        return startDateTimeList;
    }

    public void setStartDateTimeList(List<SelectItem> startDateTimeList) {
        this.startDateTimeList = startDateTimeList;
    }

    public Map<String, String> getDispTimeUnitMap() {
        return dispTimeUnitMap;
    }

    public void setDispTimeUnitMap(Map<String, String> dispTimeUnitMap) {
        this.dispTimeUnitMap = dispTimeUnitMap;
    }

    public Map<String, String> getDispYearMap() {
        return dispYearMap;
    }

    public void setDispYearMap(Map<String, String> dispYearMap) {
        this.dispYearMap = dispYearMap;
    }

    public List<String> getDispDateList() {
        return dispDateList;
    }

    public void setDispDateList(List<String> dispDateList) {
        this.dispDateList = dispDateList;
    }

    public List<List<String>> getLoadSurveyDataList() {
        return loadSurveyDataList;
    }

    public void setLoadSurveyDataList(List<List<String>> loadSurveyDataList) {
        this.loadSurveyDataList = loadSurveyDataList;
    }

    public List<DataViewRow> getDispDataList() {
        return dispDataList;
    }

    public void setDispDataList(List<DataViewRow> dispDataList) {
        this.dispDataList = dispDataList;
    }

    public Map<String, List<String>> getGraphListMap() {
        return graphListMap;
    }

    public void setGraphListMap(Map<String, List<String>> graphListMap) {
        this.graphListMap = graphListMap;
    }

    public Map<String, String> getFileNameEscapeMap() {
        return fileNameEscapeMap;
    }

    public void setFileNameEscapeMap(Map<String, String> fileNameEscapeMap) {
        this.fileNameEscapeMap = fileNameEscapeMap;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public boolean isGraphIconHidden() {
        return graphIconHidden;
    }

    public void setGraphIconHidden(boolean graphIconHidden) {
        this.graphIconHidden = graphIconHidden;
    }

    public void setDispDirectMap(Map<String, String> dispDirectMap) {
        this.dispDirectMap = dispDirectMap;
    }

    public String getSelectableLimitDate() {
        return selectableLimitDate;
    }

    public void setSelectableLimitDate(String selectableLimitDate) {
        this.selectableLimitDate = selectableLimitDate;
    }

    public boolean isNextYearButtonDisabled() {
        return nextYearButtonDisabled;
    }

    public void setNextYearButtonDisabled(boolean nextYearButtonDisabled) {
        this.nextYearButtonDisabled = nextYearButtonDisabled;
    }

    public boolean isNextMonthButtonDisabled() {
        return nextMonthButtonDisabled;
    }

    public void setNextMonthButtonDisabled(boolean nextMonthButtonDisabled) {
        this.nextMonthButtonDisabled = nextMonthButtonDisabled;
    }

    public boolean isNextDayButtonDisabled() {
        return nextDayButtonDisabled;
    }

    public void setNextDayButtonDisabled(boolean nextDayButtonDisabled) {
        this.nextDayButtonDisabled = nextDayButtonDisabled;
    }

    public List<String> getDispDayListForCompare() {
        return dispDayListForCompare;
    }

    public void setDispDayListForCompare(List<String> dispDayListForCompare) {
        this.dispDayListForCompare = dispDayListForCompare;
    }

    public Map<String, List<String>> getGraphMaxDemandListMap() {
        return graphMaxDemandListMap;
    }

    public void setGraphMaxDemandListMap(Map<String, List<String>> graphMaxDemandListMap) {
        this.graphMaxDemandListMap = graphMaxDemandListMap;
    }

    public Map<String, String> getGraphMaxDemandPeriodMap() {
        if(graphMaxDemandPeriodMap == null) {
            graphMaxDemandPeriodMap = new HashMap<>();
        }
        return graphMaxDemandPeriodMap;
    }

    public void setGraphMaxDemandPeriodMap(Map<String, String> graphMaxDemandPeriodMap) {
        this.graphMaxDemandPeriodMap = graphMaxDemandPeriodMap;
    }

    public Map<String, String> getGraphMaxDemandLastYearMap() {
        if(graphMaxDemandLastYearMap == null) {
            graphMaxDemandLastYearMap = new HashMap<>();
        }
        return graphMaxDemandLastYearMap;
    }

    public void setGraphMaxDemandLastYearMap(Map<String, String> graphMaxDemandLastYearMap) {
        this.graphMaxDemandLastYearMap = graphMaxDemandLastYearMap;
    }

    public Map<String, String> getGraphMaxDemandPastMap() {
        if(graphMaxDemandPastMap == null) {
            graphMaxDemandPastMap = new HashMap<>();
        }
        return graphMaxDemandPastMap;
    }

    public void setGraphMaxDemandPastMap(Map<String, String> graphMaxDemandPastMap) {
        this.graphMaxDemandPastMap = graphMaxDemandPastMap;
    }
}
