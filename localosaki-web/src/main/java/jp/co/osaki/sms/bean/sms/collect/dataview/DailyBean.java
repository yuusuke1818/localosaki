package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.dao.MDevPrmListDao;

/**
 * データ収集装置 データ表示機能 日報データ画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewDailyBean")
@ConversationScoped
public class DailyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = 721956737699370376L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "daily";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMMddHHmm";

    @Inject
    private DataViewBean dataViewBean;

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
        Date currentDate = mDevPrmListDao.getSvDate();

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_HHMM_COLON);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        property.setTitleReport("日報データ");
        property.setTitleDispDate("表示日");
        property.setTitleStartDate("開始時刻");

        dataViewBean.init(buildingInfo, property, currentDate);

        dataViewBean.updateStartTimePulldown();

        return init();
    }

    /**
     * カレンダー 日付変更時
     *
     * @return 遷移先
     */
    public String changeDailyDate() {
        // アクセスログ出力
        exportAccessLog("changeDailyDate", "カレンダーにて日付選択");

        dataViewBean.changeDailyDate();

        return FORWARD_STR;
    }

    /**
     * カレンダー 前月ボタン押下時
     *
     * @return 遷移先
     */
    public String moveLastMonth() {
        // アクセスログ出力
        exportAccessLog("moveLastMonth", "ボタン「前月」押下");

        dataViewBean.moveLastMonth();

        return FORWARD_STR;
    }

    /**
     * カレンダー 翌月ボタン押下時
     *
     * @return 遷移先
     */
    public String moveNextMonth() {
        // アクセスログ出力
        exportAccessLog("moveNextMonth", "ボタン「翌月」押下");

        dataViewBean.moveNextMonth();

        return FORWARD_STR;
    }

    /**
     * カレンダー 前日ボタン押下時
     *
     * @return 遷移先
     */
    public String moveLastDay() {
        // アクセスログ出力
        exportAccessLog("moveLastDay", "ボタン「前日」押下");

        dataViewBean.moveLastDay();

        return FORWARD_STR;
    }

    /**
     * カレンダー 翌日ボタン押下時
     *
     * @return 遷移先
     */
    public String moveNextDay() {
        // アクセスログ出力
        exportAccessLog("moveNextDay", "ボタン「翌日」押下");

        dataViewBean.moveNextDay();

        return FORWARD_STR;
    }

    /**
     * カレンダー 日付選択時
     *
     * @param selectedDay 選択日
     * @return 遷移先
     */
    public String selectCalDay(String selectedDay) {
        // アクセスログ出力
        exportAccessLog("selectCalDay", "カレンダー日付リンク押下");

        dataViewBean.selectCalDay(selectedDay);

        return FORWARD_STR;
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

        dataViewBean.updateStartTimePulldown();
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

        String startTime = dataViewBean.getStartTime();
        dataViewBean.getCalendarBean().setSelectedTime(startTime.substring(0, 2), startTime.substring(2));
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

    public DataViewBean getDataViewBean() {
        return dataViewBean;
    }

    public void setDataViewBean(DataViewBean dataViewBean) {
        this.dataViewBean = dataViewBean;
    }
}
