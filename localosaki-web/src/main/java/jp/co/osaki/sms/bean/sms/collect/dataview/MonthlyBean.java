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
 * データ収集装置 データ表示機能 月報データ画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectDataviewMonthlyBean")
@ConversationScoped
public class MonthlyBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -8841201911656187439L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "monthly";

    /** 対象日時取得間隔. */
    private static final String TARGET_DATE_TIME_DURATION = "P1D";

    /** 対象日時取得時間数. */
    private static final String TARGET_DATE_TIME_ADDITION = "P1M";

    /** 日時パターン文字列 対象日時検索条件. */
    private static final String TARGET_DATE_TIME_PATTERN_SEARCH = "yyyyMMdd";

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
        Date currentDate = DateUtility.changeDateMonthFirst(mDevPrmListDao.getSvDate());

        DataViewBeanProperty property = new DataViewBeanProperty();
        property.setTargetDateTimeFormat(TARGET_DATE_TIME_PATTERN_SEARCH);
        property.setTargetDateTimeDuration(TARGET_DATE_TIME_DURATION);
        property.setTargetDateTimeAddtion(TARGET_DATE_TIME_ADDITION);
        property.setListDateTimeFormat(DateUtility.DATE_FORMAT_MMDD_SLASH);
        property.setDispDateTimeFormat(DateUtility.DATE_FORMAT_YYYYMM_SLASH);
        property.setTitleReport("月報データ");
        property.setTitleDispDate("表示月");
        property.setTitleStartDate("開始日");

        dataViewBean.initMonthly(buildingInfo, property, currentDate);

        return init();
    }

    /**
     * カレンダー 前年ボタン押下時
     *
     * @return 遷移先
     */
    public String moveLastYear() {
        // アクセスログ出力
        exportAccessLog("moveLastYear", "ボタン「前年」押下");

        dataViewBean.moveLastYear();

        return FORWARD_STR;
    }

    /**
     * カレンダー 翌年ボタン押下時
     *
     * @return 遷移先
     */
    public String moveNextYear() {
        // アクセスログ出力
        exportAccessLog("moveNextYear", "ボタン「翌年」押下");

        dataViewBean.moveNextYear();

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
     * カレンダー 月選択時
     *
     * @param selectedMonth 選択月
     * @return 遷移先
     */
    public String selectCalMonth(String selectedMonth) {
        // アクセスログ出力
        exportAccessLog("selectCalMonth", "カレンダー月リンク押下");

        dataViewBean.selectCalMonth(selectedMonth);

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
     * 開始日プルダウン変更時
     */
    public void changeStartDayPulldown() {
        // アクセスログ出力
        exportAccessLog("changeStartDayPulldown", "「開始日」選択");
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

        dataViewBean.getCalendarBean().setSelectedDay(dataViewBean.getStartDay());
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
