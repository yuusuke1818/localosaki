package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * データ収集装置 データ表示機能 年報データ画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewYearlyBean")
@ConversationScoped
public class YearlyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -6112935295500548313L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "yearly";

    /** 対象日時取得間隔. */
    private static final String TARGET_DATE_TIME_DURATION = "P1M";

    /** 対象日時取得時間数. */
    private static final String TARGET_DATE_TIME_ADDITION = "P1Y";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMM";

    @Inject
    private DataViewBean dataViewBean;

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
        dataViewBean.setStartDateTimeList(createStartMonthList());

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setTargetDateTimeDuration(TARGET_DATE_TIME_DURATION);
        property.setTargetDateTimeAddtion(TARGET_DATE_TIME_ADDITION);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMM_SLASH);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYY);
        property.setTitleReport("年報データ");
        property.setTitleDispDate("表示年");
        property.setTitleStartDate("開始月");

        dataViewBean.initYearly(buildingInfo, property);

        return init();
    }

    /**
     * 装置プルダウン変更時
     */
    public void changeDevIdPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDevIdPulldown", "「接続先」選択");

        dataViewBean.changeDevIdPulldown();
    }

    /**
     * テナント名入力時.
     */
    public void inputTenantName() {
        // アクセスログ出力
        exportAccessLog("inputTenantName", "「テナント名」入力");

        dataViewBean.changeDevIdPulldown();
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
     * 表示種別プルダウン変更時
     */
    public void changeDispTypePulldown() {
        // アクセスログ出力
        exportAccessLog("changeDispTypePulldown", "「表示種別」選択");
    }

    /**
     * 表示年プルダウン変更時
     */
    public void changeDispYearPulldown() {
        // アクセスログ出力
        exportAccessLog("changeDispYearPulldown", "「表示年」選択");
    }

    /**
     * 開始月プルダウン変更時
     */
    public void changeStartMonthPulldown() {
        // アクセスログ出力
        exportAccessLog("changeStartMonthPulldown", "「開始月」選択");
    }

    /**
     * 数値(表)形式 または グラフ形式選択時
     */
    public void selectDispTableOrGraph() {
        // TODO 両方の形式を独立してアクセスログ出力したいが想定通り動作しないため、一旦はどちらかでも選択したことがわかるように実装
        // アクセスログ出力
        exportAccessLog("selectDispTableOrGraph", "「数値形式またはグラフ形式」選択");
    }

    /**
     * 表示更新.
     *
     * @return 遷移先
     */
    public String reload() {
        // アクセスログ出力
        exportAccessLog("reload", "ボタン「表示更新」押下");

        CalendarBean calendarBean = dataViewBean.getCalendarBean();
        calendarBean.setSelectedYear(dataViewBean.getDispYear());
        calendarBean.setSelectedMonth(dataViewBean.getStartMonth());
        dataViewBean.reload();

        return FORWARD_STR;
    }

    /**
     * 帳票出力(CSV).
     *
     * @return 遷移先
     */
    public String downloadCsvFile() {
        // アクセスログ出力
        exportAccessLog("downloadCsvFile", "ボタン「帳票出力」押下");

        dataViewBean.downloadCsvFile();

        return STR_EMPTY;
    }

    /**
     * 印刷出力(PDF).
     *
     * @return 遷移先
     */
    public String downloadPdfFile() {
        // アクセスログ出力
        exportAccessLog("downloadPdfFile", "ボタン「印刷出力」押下");

        dataViewBean.downloadPdfFile();

        return STR_EMPTY;
    }

    /**
     * ファイルダウンロード開始.
     *
     * @return 遷移先
     */
    public String downloadFileStart() {
        dataViewBean.downloadFileStart();

        return STR_EMPTY;
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

    public DataViewBean getDataViewBean() {
        return dataViewBean;
    }

    public void setDataViewBean(DataViewBean dataViewBean) {
        this.dataViewBean = dataViewBean;
    }
}
