package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.MeterDataFilterDao;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.exe.ManualInspExeParameter;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspParameter;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.search.ListManualInspectionParameter;
import jp.co.osaki.osol.api.request.sms.manualinsp.exe.ManualInspExeRequest;
import jp.co.osaki.osol.api.request.sms.manualinsp.exe.ManualInspExeRequestSet;
import jp.co.osaki.osol.api.request.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspRequest;
import jp.co.osaki.osol.api.request.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspRequestSet;
import jp.co.osaki.osol.api.request.sms.manualinsp.search.ListManualInspectionRequest;
import jp.co.osaki.osol.api.response.sms.manualinsp.exe.ManualInspExeResponse;
import jp.co.osaki.osol.api.response.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspResponse;
import jp.co.osaki.osol.api.response.sms.manualinsp.search.ListManualInspectionResponse;
import jp.co.osaki.osol.api.resultdata.sms.manualinsp.search.ListManualInspectionResultData;
import jp.co.osaki.osol.entity.MAlertMailSetting;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.sms.collect.setting.concentrator.ConcentratorManagementBean;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MAlertMailSettingDao;
import jp.co.osaki.sms.deviceCtrl.dao.MAutoInspDao;
import jp.skygroup.enl.webap.base.BaseSearchCondition;
import jp.skygroup.enl.webap.base.BaseSearchInterface;

/**
 * データ収集装置 機器管理 検針設定 任意検針画面
 *
 * @author yonezawa.a
 *
 */
@Named(value = "smsCollectSettingMeterreadingManualInspectionBean")
@ConversationScoped
public class ManualInspectionBean extends SmsConversationBean implements Serializable, BaseSearchInterface<Condition> {

    // シリアライズID
    private static final long serialVersionUID = -6642016127719540516L;

    private static final String manualMail = "ocr_manual_insp";

    private static final String reserveMail = "ocr_reserve_insp";

    // チェックボックスで選択できる最大件数
    private static final int CHECK_MAX_COUNT = 1000;

    /** 予約検針日時 時プルダウン 最小値. */
    private static final int RESERVATION_HOUR_MIN = 0;

    /** 予約検針日時 時プルダウン 最大値. */
    private static final int RESERVATION_HOUR_MAX = 23;

    // 当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    @Inject
    private SearchBeanProperty searchBeanProperty;

    @EJB
    private MAlertMailSettingDao mAlertMailSettingDao;

    @EJB
    private MAutoInspDao mAutoInspDao;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    /**
     * メーターフィルター
     */
    @EJB
    private MeterDataFilterDao meterDataFilterDao;

    // 検索条件リスト
    private List<Condition> conditionList;

    // 一覧出力用
    private List<ManualInspectionDataList> manualInspectionDataList = new ArrayList<>();

    // チェックボックス押下時のエラーチェック
    private boolean chkResult;

    // 一括更新用 予約検針日
    private Date updReserveInspYMD = null;
    // 一括更新用 予約検針時
    private String updReserveInspH = null;
    // 一括更新用 予約検針分
    private String updReserveInspM = null;

    public SearchBeanProperty getSearchBeanProperty() {
        return searchBeanProperty;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public List<ManualInspectionDataList> getManualInspectionDataList() {
        return manualInspectionDataList;
    }

    public void setManualInspectionDataList(List<ManualInspectionDataList> manualInspectionDataList) {
        this.manualInspectionDataList = manualInspectionDataList;
    }

    public boolean isChkResult() {
        return chkResult;
    }

    public void setChkResult(boolean chkResult) {
        this.chkResult = chkResult;
    }

    /**
     * 初期表示
     *
     * @return 任意検針画面
     */
    @Override
    @Logged
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:init():START"));

        conversationStart();
        beanMessages.initMessages();
        chkResult = false;

        // 検索未実行
        searchBeanProperty.setSearchedFlg(false);

        // 検索条件作成
        conditionList = searchBeanProperty.getConditionList(this);

        // セレクトボックス作成
        Map<String, String> userCdMap = toolsPullDownList
                .getUserCd(topBean.getTopBeanProperty().getListInfo().getCorpId(), topBean.getTopBeanProperty().getListInfo().getBuildingId());
        searchBeanProperty.setUserCdMap(userCdMap);

        Map<String, String> meterTypeNameMap = toolsPullDownList.getMeterTypeName(
                topBean.getTopBeanProperty().getListInfo().getCorpId(),
                Long.valueOf(topBean.getTopBeanProperty().getListInfo().getBuildingId()));
        searchBeanProperty.setMeterTypeNameMap(meterTypeNameMap);

        // 予約検針日 セレクトボックス作成
        searchBeanProperty.setReserveInspHMap(createReserveInspHMap("%02d"));

        // 予約検針時 セレクトボックス作成
        searchBeanProperty.setReserveInspMMap(createReserveInspMMap("%02d"));

        // 検索実行 任意検針取得
        executeSearch(conditionList);


        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:init():END"));
        return "manualInspection";
    }

    /**
     * 検索処理
     * 「表示更新」ボタンの処理
     *
     * @return 任意検針画面
     */
    @Logged
    public String search() {

        eventLogger.debug(packageName.concat("smsCollectSettingMeterreadingManualInspectionBean:search():START"));

        List<Condition> conditionList = searchBeanProperty.getConditionList(this);
        if (conditionList == null) {
            conditionList = new ArrayList<>();
        }

        executeSearch(conditionList);

        eventLogger.debug(packageName.concat("smsCollectSettingMeterreadingManualInspectionBean:search():END"));

        return "manualInspection";
    }

    /**
     * 検索実処理
     *
     * @param conditionList 検索条件リスト
     */
    private void executeSearch(List<Condition> conditionList) {

        // メーター種別設定取得
        List<ListManualInspectionResultData> list = searchManualInspectionInfo(conditionList);

        // 画面クリア
        manualInspectionDataList.clear();

        // 検索実行済
        searchBeanProperty.setSearchedFlg(true);

        if (list == null || list.size() == 0) {
            searchBeanProperty.setResultCount(0);
            return;
        }

        // 画面表示項目設定
        for (ListManualInspectionResultData info : list) {
            ManualInspectionDataList dataList = new ManualInspectionDataList();
            dataList.setMeterMngId(info.getMeterMngId());
            dataList.setMeterType(info.getMeterType());
            if (CheckUtility.isNullOrEmpty(info.getMeterId())) {
                dataList.setMeterId("-");
            } else {
                dataList.setMeterId(info.getMeterId());
            }
            if (CheckUtility.isNullOrEmpty(info.getMeterTypeName())) {
                dataList.setMeterTypeName("-");
            } else {
                dataList.setMeterTypeName(info.getMeterTypeName());
            }
            if (info.getTenantId() == null) {
                dataList.setUserCd("-");
            } else {
                dataList.setUserCd(String.valueOf(info.getTenantId()));
            }
            if (CheckUtility.isNullOrEmpty(info.getBuildingName())) {
                dataList.setBuildingName("-");
            } else {
                dataList.setBuildingName(info.getBuildingName());
            }
            if (info.getReserveInspDate() == null) {
                dataList.setReserveInspDate("-");
            } else {
                dataList.setReserveInspDate(DateUtility.changeDateFormat(info.getReserveInspDate(), DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER + " " + DateUtility.DATE_FORMAT_HHMM_COLON));
            }
            dataList.setCheckBox(false);
            dataList.setDevName(info.getDevName());
            // 画面では使用しないが、API実行時に使用
            dataList.setDevId(info.getDevId());
            dataList.setVersion(info.getVersion());
            manualInspectionDataList.add(dataList);
        }
        searchBeanProperty.setResultCount(manualInspectionDataList.size());
    }

    /**
     *  メーター種別設定取得
     *
     * @param conditionList 検索条件リスト
     * @return 検索結果
     */
    private List<ListManualInspectionResultData> searchManualInspectionInfo(List<Condition> conditionList) {

        String targetUserName = "";
        String targetUserCd = "";
        String targetMeterTypeName = "";
        String targetReserveInspDate = "";

        // パラメータ用 検索条件作成
        for (Condition condition : conditionList) {
            if (condition == null) {
                continue;
            }

            String selectConditionCd = condition.getSelectConditionCd();
            if (CheckUtility.isNullOrEmpty(selectConditionCd)
                    || OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(selectConditionCd)) {
                continue;
            }

            eventLogger.debug(packageName.concat(" selectConditionCd(" + selectConditionCd + ")"));

            switch (condition.getSelectConditionCd()) {
            case (OsolConstants.SEARCH_CONDITION_USER_NAME):
                if (!CheckUtility.isNullOrEmpty(condition.getConditionKeyword())) {
                    targetUserName = condition.getConditionKeyword();
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" UserName(" + condition.getConditionKeyword() + ")"));
                break;

            case (OsolConstants.SEARCH_CONDITION_USER_CD):
                if (!CheckUtility.isNullOrEmpty(condition.getUserCd())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getUserCd())) {
                    targetUserCd = condition.getUserCd();
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" UserCd(" + condition.getUserCd() + ")"));
                break;
            case (OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME):
                if (!CheckUtility.isNullOrEmpty(condition.getMeterTypeName())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getMeterTypeName())) {
                    targetMeterTypeName = condition.getMeterTypeName();
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" MeterTypeName(" + condition.getMeterTypeName() + ")"));
                break;
            case (OsolConstants.SEARCH_CONDITION_RESERVATION_DT):
                if (condition.getReserveInspYMD() != null
                        && !CheckUtility.isNullOrEmpty(condition.getReserveInspH())
                        && !CheckUtility.isNullOrEmpty(condition.getReserveInspM())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getReserveInspH())
                        && !OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(condition.getReserveInspM())) {
                    targetReserveInspDate = DateUtility.changeDateFormat(condition.getReserveInspYMD(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            + " " + condition.getReserveInspH() + ":" + condition.getReserveInspM();
                }
                eventLogger.debug(this.getClass().getPackage().getName()
                        .concat(" ReserveInspYMD(" + condition.getReserveInspYMD() + ")"
                              + " ReserveInspH(" + condition.getReserveInspH() + ")"
                              + " ReserveInspM(" + condition.getReserveInspM() + ")"));
                break;
            default:
                break;
            }
        }

        // 任意検針取得
        ListManualInspectionResponse response = new ListManualInspectionResponse();
        ListManualInspectionParameter parameter = new ListManualInspectionParameter();
        ListManualInspectionRequest request = new ListManualInspectionRequest();

        request.setUserName(targetUserName);
        request.setUserCd(targetUserCd);
        request.setMeterTypeName(targetMeterTypeName);
        request.setReserveInspDate(targetReserveInspDate);
        request.setCorpId(topBean.getTopBeanProperty().getListInfo().getCorpId());
        request.setBuildingId(topBean.getTopBeanProperty().getListInfo().getBuildingId());
        parameter.setBean("ListManualInspectionBean");
        parameter.setRequest(request);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListManualInspectionResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        List<ListManualInspectionResultData> list = null;
        if (Objects.nonNull(response.getResult())) {
            // 画面表示用リストに乗せ換え
            list = response.getResult().getResultList();
        }
        return list;
    }

    /**
     * 検索条件追加
     *
     * @param orderNo 選択された検索条件
     */
    @Logged
    public void changeConditionCd(String orderNo) {

        searchBeanProperty.updateCondition(this, Integer.parseInt(orderNo));

        boolean userCdFlg = false;
        boolean meterTypeNameFlg = false;
        boolean reservationDtFlg = false;
        Map<String, String> dropdown = conditionList.get(conditionList.size() - 1).getSelectConditionMap();

        for (Condition condition : conditionList) {
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_USER_CD)) {
                dropdown.remove(OsolConstants.SEARCH_CONDITION_USER_CD);
                userCdFlg = true;
            }
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME)) {
                dropdown.remove(OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME);
                meterTypeNameFlg = true;
            }
            if (condition.getSelectConditionCd().equals(OsolConstants.SEARCH_CONDITION_RESERVATION_DT)) {
                dropdown.remove(OsolConstants.SEARCH_CONDITION_RESERVATION_DT);
                reservationDtFlg = true;
            }
        }

        //ドロップダウンリストを手動で削除した影響で条件追加のが消えないため、削除制御をここで実施。
        if(userCdFlg && meterTypeNameFlg && reservationDtFlg && searchBeanProperty.getSelectConditionMap(this).size() == 3){
            //ドロップダウンリストから項目が無くなったら、ドロップダウン自体を削除する
            conditionList.remove(conditionList.size()-1);
        }
    }

    /**
     * 追加条件削除
     *
     * @param orderNo 選択された検索条件
     */
    @Logged
    public void deleteCondition(String orderNo) {

        // ユーザーコード選択、種別選択が削除された場合は内容を初期化する
        List<Condition> conditionList = searchBeanProperty.getConditionList(this);

        int chkCnt = 0;
        boolean userCdDelFlg = false;
        boolean meterTypeNameDelFlg = false;
        boolean reservationDtDelFlg = false;

        // 両検索とも追加されているか確認
        for (Condition condition : conditionList) {
            if (condition.isUserCdSelectEnable()) {
                chkCnt++;
            }
            if (condition.isMeterTypeNameSelectEnable()) {
                chkCnt++;
            }
        }

        // 両検索とも追加されている場合、削除ボタンが押下された検索条件を特定
        if (conditionList.get(Integer.parseInt(orderNo)).getSelectConditionCd()
                .equals(OsolConstants.SEARCH_CONDITION_USER_CD) && chkCnt == 2) {
            userCdDelFlg = true;
        }
        if (conditionList.get(Integer.parseInt(orderNo)).getSelectConditionCd()
                .equals(OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME) && chkCnt == 2) {
            meterTypeNameDelFlg = true;
        }
        if (conditionList.get(Integer.parseInt(orderNo)).getSelectConditionCd()
                .equals(OsolConstants.SEARCH_CONDITION_RESERVATION_DT) && chkCnt == 2) {
            reservationDtDelFlg = true;
        }

        //条件削除
        searchBeanProperty.deleteCondition(this, orderNo);

        //手動でドロップダウンリストの項目を削除した影響で条件追加復活の処理が実施されないため、ここで実施する。
        if (searchBeanProperty.getSelectConditionMap(this).size() == 3
                || searchBeanProperty.getSelectConditionMap(this).size() == 4) {
            //選択肢を使い果たした状態から1個削除したら「条件を追加」を復活させる
            Condition condition = conditionList.get(conditionList.size() - 1);
            if (condition instanceof BaseSearchCondition) {
                BaseSearchCondition base = (BaseSearchCondition) condition;
                if (!base.isDefaultCondition()) {
                    conditionList.add(createDefaultCondition());
                }
            }
        }

        // 検索条件プルダウン更新
        searchBeanProperty.resetOrder();
        searchBeanProperty.updateConditionMap(this, conditionList.size() - 1);

        // 追加済の検索条件条件をプルダウンから削除
        if (userCdDelFlg) {
            conditionList.get(conditionList.size() - 1).getSelectConditionMap()
                    .remove(OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME);
        }
        if (meterTypeNameDelFlg) {
            conditionList.get(conditionList.size() - 1).getSelectConditionMap()
                    .remove(OsolConstants.SEARCH_CONDITION_USER_CD);
        }
        if (reservationDtDelFlg) {
            conditionList.get(conditionList.size() - 1).getSelectConditionMap()
                    .remove(OsolConstants.SEARCH_CONDITION_RESERVATION_DT);
        }
    }

    /**
     * 任意検針ボタンなど押下時の確認ダイアログメッセージ
     * @param flg 確認メッセージ種別(1:登録, 2:更新, 3:削除)
     * @return メッセージ
     */
    public String getBeforeRegisterMessage(Long flg) {
        if (flg == 1) {
            // この内容で登録します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
        } else if (flg == 2) {
            // 変更を保存します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeUpdateMessage");
        } else { // if (flg == 3) {
            // 削除します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeDeleteMessage");
        }
    }

    /**
     * 選択ボタン押下時（全件選択または解除）
     *
     * ※1000件以上選択されている状態で
     *   再度チェックを外した時にパラメータエラーになるので、
     *   チェックを入れる段階で1000件以上の場合はエラーメッセージを表示する。
     */
    @Logged
    public void selectAllCheckbox() {

        boolean allChk = false;

        eventLogger.debug(ManualInspectionBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterreadingManualInspectionBean:selectAllCheckbox():START"));

        // すべてチェックされている場合はエラーチェック対象外
        for (ManualInspectionDataList list : manualInspectionDataList) {
            if (list.getCheckBox()) {
                allChk = true;
            } else {
                allChk = false;
                break;
            }
        }

        // 一件以上チェック済が存在し、1000件以上の場合はチェックエラー
        if (!allChk && manualInspectionDataList.size() >= CHECK_MAX_COUNT) {
            chkResult = false;
            addErrorMessage(beanMessages
                    .getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.selectedCheckBox"));
            return;
        }

        //checkboxがすでにすべてチェック済みの場合は、すべてを未チェックにする
        Boolean check = !manualInspectionDataList.stream().allMatch(x -> x.getCheckBox());
        for (ManualInspectionDataList info : manualInspectionDataList) {
            info.setCheckBox(check);
        }

        chkResult = true;
        eventLogger.debug(ConcentratorManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterreadingManualInspectionBean:selectAllCheckbox():END"));
    }

    /**
     * チェックボックス選択または解除時
     *
     * ※選択時に1000件以上場合はエラーメッセージを表示する。
     */
    @Logged
    public void checkSelectedCheckBox() {

        eventLogger.debug(ManualInspectionBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterreadingManualInspectionBean:checkSelectedCheckBox():START"));

        if (manualInspectionDataList.size() >= CHECK_MAX_COUNT) {
           // エラーチェック
            if (checkBoxOnCount() >= CHECK_MAX_COUNT && !chkResult) {
               return;
           }
        }

        chkResult = true;
        eventLogger.debug(ManualInspectionBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterreadingManualInspectionBean:checkSelectedCheckBox():END"));
    }

    /**
     * チェックボックス入力時の件数チェック
     */
    private int checkBoxOnCount() {

        int cnt = 0;

        for (ManualInspectionDataList list : manualInspectionDataList) {
            if (list.getCheckBox()) {
                cnt++;
            }
            if (cnt >= CHECK_MAX_COUNT) {
                chkResult = false;
                addErrorMessage(beanMessages
                        .getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.selectedCheckBox"));
                return cnt;
            }
        }
        return cnt;
    }

    /**
     * 「予約検針」ボタン
     *
     * @return 任意検針画面
     * @throws Exception
     */
    @Logged
    public String bulkUpdReserveInspButton() throws Exception {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkUpdReserveInspButton():START"));

        // 入力チェック

        boolean isError = false;

        // 任意検針対象取得
        List<ManualInspectionDataList> target = getTarget();

        if (target == null || target.size() == 0) {
            // 入力チェックエラー
            isError = true;
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.noData"));
        }

        if (this.updReserveInspYMD == null) {
            // 入力チェックエラー
            isError = true;
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.reserveInspDate"));
        }

        if (this.updReserveInspH == null || this.updReserveInspM == null
                || DateUtility.conversionDate(this.updReserveInspH + this.updReserveInspM, DateUtility.DATE_FORMAT_HHMM) == null) {
            // 入力チェックエラー
            isError = true;
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.reserveInspTime"));
        }

        Date reserveInspDate = null; // 予約検針日時
        if (!isError) {
            // チェックエラーなし
            // 入力された「予約検針日」「予約検針時」「予約検針分」をまとめた「予約検針日時」を生成
            reserveInspDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(this.updReserveInspYMD, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) // yyyy/MM/dd
                    + " " + this.updReserveInspH + ":" + this.updReserveInspM,  // HH:mm
                    DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH);
            if ((new Date()).compareTo(reserveInspDate) > 0) {
                // 過去日時が指定されている
                // 入力チェックエラー
                isError = true;
                addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.reserveInspDate.past"));
            }

            // 処理起点の日付を日付ベースで保持
          Date currentDate = DateUtility.conversionDate(
                  DateUtility.changeDateFormat(mAutoInspDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHH),
                  DateUtility.DATE_FORMAT_YYYYMMDDHH);

            // 同一時刻にて自動検針が設定されている場合
            for (ManualInspectionDataList dataList : target) {

                // 自動検針対象リスト取得
                MAutoInsp entity = new MAutoInsp();
                MAutoInspPK id = new MAutoInspPK();
                id.setDevId(dataList.getDevId());
                id.setMeterType(dataList.getMeterType());
                entity.setId(id);

                List<MAutoInsp> autoinsplist = mAutoInspDao.getAutoInspList(entity);
                // 検針対象リスト分ループ
                for (MAutoInsp info : autoinsplist) {

                    // 無効インプットがあれば次へ
                    if (info.getMonth() == null
                            || info.getMonth().length() == 0
                            || info.getDay() == null
                            || info.getDay().length() == 0
                            || info.getHour() == null) {
                        continue;
                    }

                    // 月毎の自動検針実施有無で年月日時を生成(編集前)
                    List<String> exec_day_list = createPreDayString(
                            info.getMonth(), info.getDay(), info.getHour().toString(),
                            DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDDHH));

                    // 対象日時なしの場合は次へ
                    if (exec_day_list.size() == 0) {
                        // batchLogger.info(this.batchName.concat(" 対象日なし:" + info.getDevId()));
                        continue;
                    }

                    // 月毎の自動検針実施有無で年月日時を生成(編集後)
                    List<String> exec_day_mod_list = createAftDayString(exec_day_list);

                    // 予約検針日時取得(yyyyMMddHHmm)
                    String reservationInspDate = DateUtility.changeDateFormat(this.updReserveInspYMD, DateUtility.DATE_FORMAT_YYYYMMDD).concat(this.updReserveInspH).concat(this.updReserveInspM);
                    // 予約検針日時と自動検針日時との比較
                    boolean isDuplicateAutoInspDate = checkReserveInspDateAndAutoInspDate(exec_day_mod_list, reservationInspDate);

                    // 予約検針日時と自動検針日時にて同一時刻に検針日時の設定がある場合
                    if (isDuplicateAutoInspDate) {
                        chkResult = false;
                        eventLogger.debug(packageName.concat(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.sameAutoInspTime")));
                        return "";
                    }
                }
            }
        }

        if (isError) {
            // 入力エラー
            chkResult = false;
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkUpdReserveInspButton():END (getTargetCnt error)"));
            return "";
        }

        // 一括更新  ※予約検針ダイアログ 入力値クリアを含む
        updateReserveInspDate(target, reserveInspDate);

        if (conditionList != null) { // 必ず true になるはず
            // 再検索 任意検針取得
            executeSearch(conditionList);
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkUpdReserveInspButton():END"));
        return "";
    }

    /**
     * 月毎の自動検針実施有無で年月日時を生成(編集前)
     *
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param nowTime
     * @return List<String>
     */
    private List<String> createPreDayString(String month, String day, String hour, String nowTime) {
        List<String> ret = new ArrayList<>();
        String[] monthArr = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
        int dayWk = Integer.valueOf(day);
        int hourWk = Integer.valueOf(hour);
        int baseYear = Integer.valueOf(nowTime.substring(0, 4));

        for (int plusYear = 0; plusYear < 2; plusYear++) { // 現在年と翌年の計2年分生成
            int targetYear = baseYear + plusYear;
            for (int i = 0; i < monthArr.length; i++) {
                String monthEna = month.substring(i, i + 1);
                if (monthEna.equals("1")) {
                    String execData = targetYear +
                            monthArr[i] +
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, dayWk) +
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, hourWk);
                    ret.add(execData);
                }
            }
        }
        return ret;
    }

    /**
     * 月毎の自動検針実施有無で年月日時を生成(編集後)
     *
     * @param List<String> preDayString
     * @return List<String>  書式:yyyyMMddHH
     */
    private List<String> createAftDayString(List<String> preDayString) {
        List<String> ret = new ArrayList<>();

        String wk_exec_day = "";
        for (String execDayWk : preDayString) {
            String yearWk = execDayWk.substring(0, 4);
            String monthWk = execDayWk.substring(4, 6);
            String dayWk = execDayWk.substring(6, 8);
            String hourWk = execDayWk.substring(8, 10);

            if (day_exists(yearWk, monthWk, dayWk)) {
                if (dayWk.equals("01") && hourWk.equals("00")) {
                    wk_exec_day = yearWk + monthWk + "0100";
                } else {
                    wk_exec_day = execDayWk;
                }
            } else {
                // 該当年月の月末日を取得
                Date dateWk = DateUtility.conversionDate(yearWk + monthWk + "01", DateUtility.DATE_FORMAT_YYYYMMDD);
                String endDays = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, DateUtility.getNumberOfDays(dateWk));
                // 対象日の修正
                wk_exec_day = yearWk + monthWk + endDays + hourWk;
            }
            ret.add(wk_exec_day);
        }
        return ret;
    }

    /**
     * 予約検針日時と自動検針日時との比較
     * @param List<String> exec_day_mod_list 自動検針の設定日時 書式:yyyyMMddHH
     * @param String reserveInspDate 予約検針の設定日時 yyyyMMddHHmm
     * @return 予約検針日時と自動検針日時との比較結果 （true:同一時刻に検針日時が設定あり false:同一時刻に検針日時が設定なし）
     */
    private boolean checkReserveInspDateAndAutoInspDate(List<String> exec_day_mod_list, String reserveInspDate) {

        // 現在時刻との比較
        for (String execDayModWk : exec_day_mod_list) {
            // 分を追加 自動検針単位は1時間単位のため「00」を追加
            execDayModWk = execDayModWk.concat("00");
            if (execDayModWk.equals(reserveInspDate)) {
                addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.sameAutoInspTime"));
                return true;
            }
        }
        return false;
    }

    /**
     * 日付存在チェック
     *
     * @param String yearWk
     * @param String dayWk
     * @param String hourWk
     * @return Boolean
     */
    private Boolean day_exists(String yearWk, String monthWk, String dayWk) {
        Integer[] mlast = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int yearNum = Integer.valueOf(yearWk);
        int monthNum = Integer.valueOf(monthWk);
        int dayNum = Integer.valueOf(dayWk);
        if (monthNum < 1 || 12 < monthNum) {
            return false;
        }

        if (monthNum == 2) {
            if (((yearNum % 4 == 0) && (yearNum % 100 != 0))
                    || (yearNum % 400 == 0)) {
                mlast[1]++;
            }
        }

        if (dayNum < 1 || mlast[monthNum - 1] < dayNum) {
            return false;
        }
        return true;
    }

    /**
     * 「予約検針」ボタン
     *
     * @return 任意検針画面
     */
    @Logged
    public String bulkDelReserveInspButton() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkDelReserveInspButton():START"));

        // 入力チェック

        boolean isError = false;

        // 任意検針対象取得
        List<ManualInspectionDataList> target = getTarget();

        if (target == null || target.size() == 0) {
            // 入力チェックエラー
            isError = true;
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.noData"));
        }

        if (isError) {
            // 入力エラー
            chkResult = false;
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkDelReserveInspButton():END (getTargetCnt error)"));
            return "";
        }

        // 一括更新(予約検針日時 削除)  ※予約検針ダイアログ 入力値クリアを含む
        updateReserveInspDate(target, null);

        if (conditionList != null) { // 必ず true になるはず
            // 再検索 任意検針取得
            executeSearch(conditionList);
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:bulkDelReserveInspButton():END"));
        return "";
    }

    /**
     * (選択行の)予約検針日時を一括更新.
     * ※予約検針ダイアログ 入力値クリアを含む
     * @param target 選択行情報
     * @param reserveInspDate 予約検針日時  ※null:予約検針日時削除
     */
    private void updateReserveInspDate(List<ManualInspectionDataList> target, Date reserveInspDate) { // 任意検針画面
        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        UpdateManualInspectionReserveInspResponse response = new UpdateManualInspectionReserveInspResponse();
        UpdateManualInspectionReserveInspParameter parameter = new UpdateManualInspectionReserveInspParameter();
        UpdateManualInspectionReserveInspRequest request = new UpdateManualInspectionReserveInspRequest();
        List<UpdateManualInspectionReserveInspRequestSet> reqSetList = new ArrayList<>();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateManualInspectionReserveInspBean");
        parameter.setCorpId(listInfo.getCorpId());
        parameter.setBuildingId(new Long(listInfo.getBuildingId()));
        for (ManualInspectionDataList info : target) {
            reqSetList.add(new UpdateManualInspectionReserveInspRequestSet(info.getDevId(), info.getMeterMngId(), info.getVersion()));
        }
        request.setReserveInspDate(reserveInspDate);
        request.setRequestList(reqSetList);
        parameter.setRequest(request);

        // API実行
        response = (UpdateManualInspectionReserveInspResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            // 実行エラー
            chkResult = false;
            addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }
        // 正常終了
        if (chkResult) {
            if (reserveInspDate != null) {
                // 更新
                addMessage(beanMessages.getMessage("osol.info.UpdateSuccess"));
                sendReserveInspMail(listInfo, target, reserveInspDate);
            } else {
                // 削除
                addMessage(beanMessages.getMessage("osol.info.DeleteSuccess"));
            }
        }

        // 予約検針ダイアログ 入力値クリア
        this.updReserveInspYMD = null;
        this.updReserveInspH = null;
        this.updReserveInspM = null;
    }


    /**
     * 任意検針ボタン押下時の処理
     *
     * @return 任意検針画面
     */
    @Logged
    public String regist() {

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:regist():START"));

        beanMessages.initMessages();

        // 任意検針対象取得
        List<ManualInspectionDataList> target = getTarget();

        if (target == null || target.size() == 0) {
            // 入力チェックエラー
            chkResult = false;
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.noData"));
            eventLogger.debug(packageName
                    .concat(" smsCollectSettingMeterreadingManualInspectionBean:regist():END (getTargetCnt error)"));
            return "";
        }

        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        ManualInspExeResponse response = new ManualInspExeResponse();
        ManualInspExeParameter parameter = new ManualInspExeParameter();
        ManualInspExeRequest request = new ManualInspExeRequest();
        List<ManualInspExeRequestSet> reqSetList = new ArrayList<>();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("ExeSmsManualInspBean");
        parameter.setCorpId(listInfo.getCorpId());
        parameter.setBuildingId(new Long(listInfo.getBuildingId()));
        for (ManualInspectionDataList info : target) {
            ManualInspExeRequestSet reqSet = new ManualInspExeRequestSet();
            reqSet.setDevId(info.getDevId());
            reqSet.setMeterMngId(info.getMeterMngId());
            reqSet.setMeterType(info.getMeterType());
            reqSetList.add(reqSet);
        }
        request.setManualInspList(reqSetList);
        parameter.setManualInspExes(request);
        // API実行
        response = (ManualInspExeResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode()) || !response.getResult().getResult()) {
            // 実行エラー
            chkResult = false;
            addErrorMessage(beanMessages
                    .getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));

            // 検針日時の12時間前（リトライの時間分）までに自動か予約の検針未完了のデータが存在した場合、検針確定せずにメッセージを表示する。
            if (response.getResult() != null && response.getResult().getTimsList() != null) {

                for (TInspectionMeterSvr tims : response.getResult().getTimsList()) {
                    Timestamp latestInspDate = tims.getLatestInspDate();
                    String inspType = tims.getInspType(); // 自動:"a", 手動:"m", 予約:"s", 定期:"r", 臨時:"t"

                    addErrorMessage(beanMessages.getMessageFormat("smsCollectSettingMeterreadingManualInspectionBean.error.inspProcessingRetry",
                            new String[] { DateUtility.changeDateFormat(latestInspDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH),
                                    SmsConstants.INSP_KIND.MANUAL.getVal().equals(inspType) ? "手動" :
                                        SmsConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType) ? "予約" :
                                            SmsConstants.INSP_KIND.REGULAR.getVal().equals(inspType) ? "定期" :
                                                SmsConstants.INSP_KIND.TEMPORARY.getVal().equals(inspType) ? "臨時" :"自動",
                                    DateUtility.changeDateFormat(latestInspDate, DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH),
                                    tims.getId().getDevId(),
                                    String.valueOf(tims.getId().getMeterMngId()),
                                    tims.getId().getInspYear(),
                                    tims.getId().getInspMonth(),
                                    String.valueOf(tims.getId().getInspMonthNo())}));
                }
            }
        }
        // 正常終了
        if (chkResult) {
            //任意検針実行メールの送信
            sendManualInspMail(listInfo, getTarget());
            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingManualInspectionBean:regist():END"));
        return "";
    }


    /**
     * mail送信
     * OCRで任意検針が行われたときのみ
     * @param buildingInfo
     * @param request
     */
    public void sendManualInspMail(ListInfo buildingInfo, List<ManualInspectionDataList> dataList) {
        List<String> toAddresses = new ArrayList<>();

        MAlertMailSetting entity = new MAlertMailSetting();
        entity.setAlertCd(manualMail);
        MAlertMailSetting mailSendAddress = mAlertMailSettingDao.find(entity);

        toAddresses = addAddress(toAddresses, mailSendAddress.getMail1());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail2());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail3());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail4());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail5());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail6());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail7());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail8());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail9());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail10());

        List<ManualInspectionDataList> ocrDevList = new ArrayList<>();
        for(ManualInspectionDataList ret : dataList) {
            if(ret.getDevId() != null && ret.getDevId().startsWith("OC")) {
                ocrDevList.add(ret);
            }
        }

        String svDate = getSeverDate();
        String dayOfWeek = getDayOfWeek(svDate);
        if(ocrDevList != null && ocrDevList.size() > 0) {
            SendOcrManualInspMail mail = new SendOcrManualInspMail();
            mail.ocrManualInsp(toAddresses, buildingInfo, ocrDevList, svDate, dayOfWeek);
        }
    }

    /**
     * 曜日取得
     * @param svDate
     * @return svDateの曜日
     */
    private String getDayOfWeek(String svDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = dateFormat.parse(svDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:     // Calendar.SUNDAY:1
                //日曜日
                return "（日）";
            case Calendar.MONDAY:     // Calendar.MONDAY:2
                //月曜日
                return "（月）";
            case Calendar.TUESDAY:    // Calendar.TUESDAY:3
                //火曜日
                return "（火）";
            case Calendar.WEDNESDAY:  // Calendar.WEDNESDAY:4
                //水曜日
                return "（水）";
            case Calendar.THURSDAY:   // Calendar.THURSDAY:5
                //木曜日
                return "（木）";
            case Calendar.FRIDAY:     // Calendar.FRIDAY:6
                //金曜日
                return "（金）";
            case Calendar.SATURDAY:   // Calendar.SATURDAY:7
                //土曜日
                return "（土）";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * mail送信
     * OCRで予約検針が行われたときのみ
     * @param buildingInfo
     * @param request
     */
    public void sendReserveInspMail(ListInfo buildingInfo, List<ManualInspectionDataList> dataList, Date reserveInspDate) {
        List<String> toAddresses = new ArrayList<>();

        MAlertMailSetting entity = new MAlertMailSetting();
        entity.setAlertCd(reserveMail);
        MAlertMailSetting mailSendAddress = mAlertMailSettingDao.find(entity);

        toAddresses = addAddress(toAddresses, mailSendAddress.getMail1());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail2());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail3());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail4());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail5());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail6());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail7());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail8());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail9());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail10());

        List<ManualInspectionDataList> ocrDevList = new ArrayList<>();
        for(ManualInspectionDataList ret : dataList) {
            if(ret.getDevId() != null && ret.getDevId().startsWith("OC")) {
                ocrDevList.add(ret);
            }
        }

        String reserveDateStr = getConvertDate(reserveInspDate);
        String dayOfWeek = getDayOfWeek(reserveDateStr);
        if(ocrDevList != null && ocrDevList.size() > 0) {
            SendOcrManualInspMail mail = new SendOcrManualInspMail();
            mail.ocrReserveInsp(toAddresses, buildingInfo, ocrDevList, reserveDateStr, dayOfWeek);
        }
    }

    private String getConvertDate(Date reserveDate) {
        String dateStr = "-";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(reserveDate != null) {
            dateStr = sdf.format(reserveDate);
        }
        return dateStr;
    }

    private String getSeverDate() {
        String svDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        svDate = sdf.format(meterDataFilterDao.getSvDate());
        return svDate;
    }

    /**
     * null or 空文字をチェックしてリストにadd
     *
     * @param List<String> addressList
     * @param String address
     * @return アドレスリスト
     */
    private List<String> addAddress(List<String> addressList, String address) {
        if (!CheckUtility.isNullOrEmpty(address)) {
            addressList.add(address);
        }
        return addressList;
    }

    /**
     * チェックされている任意検針データを取得
     *
     * @return チェックされている任意検針データ
     */
    private List<ManualInspectionDataList> getTarget() {

        List<ManualInspectionDataList> target = new ArrayList<>();

        for (ManualInspectionDataList info : manualInspectionDataList) {
            if (info.getCheckBox()) {
                target.add(info);
            }
        }
        return target;
    }

    @Override
    public void initialConditionList(List<Condition> conditionList) {
        // 最初に選択済み状態で表示しておく条件の一覧
        Condition condition = new Condition();
        condition.setSearchSubjectConjunctionEnable(true);
        condition.setSelectConditionCd(OsolConstants.SEARCH_CONDITION_USER_NAME);
        condition.setKeywordSelectEnable(true);
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
        condition.setDeleteButtonEnable(false);
        conditionList.add(condition);
    }

    @Override
    public Condition createDefaultCondition() {
        // 未選択状態の条件（「条件を追加」）
        Condition condition = new Condition();
        condition.setSelectEnable(true);
        condition.setSearchSubjectConjunctionEnable(false);
        condition.setDeleteButtonEnable(false);
        condition.setSelectConditionCd(OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        return condition;
    }

    @Override
    public void updateCondition(Condition condition) {
        // 一致条件表示
        condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_EQUAL);

        // 選ばれた検索条件によって表示するコンポーネントを変更する
        switch (condition.getSelectConditionCd()) {
        case (OsolConstants.SEARCH_CONDITION_USER_CD):
            // ユーザコード検索
            condition.setSearchSubjectConjunctionEnable(true);
            condition.setMultiSelectEnable(true);
            condition.setKeywordSelectEnable(false);
            condition.setUserCdSelectEnable(true);
            condition.setMeterTypeNameSelectEnable(false);
            condition.setReservationDtSelectEnable(false);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            break;

        case (OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME):
            // 種別選択検索
            condition.setSearchSubjectConjunctionEnable(true);
            condition.setMultiSelectEnable(true);
            condition.setKeywordSelectEnable(false);
            condition.setUserCdSelectEnable(false);
            condition.setMeterTypeNameSelectEnable(true);
            condition.setReservationDtSelectEnable(false);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            break;

        case (OsolConstants.SEARCH_CONDITION_RESERVATION_DT):
            // 予約検針日時
            condition.setSearchSubjectConjunctionEnable(true);
            condition.setMultiSelectEnable(true);
            condition.setKeywordSelectEnable(false);
            condition.setUserCdSelectEnable(false);
            condition.setMeterTypeNameSelectEnable(false);
            condition.setReservationDtSelectEnable(true);
            condition.setDeleteButtonEnable(true);
            condition.setSelectEnable(false);
            break;

        default:
            // キーワード検索
            condition.setSearchSubjectConjunctionEnable(false);
            condition.setMultiSelectEnable(true);
            condition.setKeywordSelectEnable(true);
            condition.setUserCdSelectEnable(true);
            condition.setMeterTypeNameSelectEnable(true);
            condition.setReservationDtSelectEnable(true);
            condition.setMutchingTypeCd(OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE);
            condition.setSelectEnable(true);
            break;
        }
    }

    @Override
    public Map<String, String> initialConditionMap() {
        // 選択肢の設定
        Map<String, String> selectConditionMap = new LinkedHashMap<>();
        selectConditionMap.put(OsolConstants.DEFAULT_SELECT_BOX_KEY, OsolConstants.DEFAULT_SELECT_BOX_VALUE);
        List<String> selectConditionList = new ArrayList<>();
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_USER_CD);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_METER_TYPE_NAME);
        selectConditionList.add(OsolConstants.SEARCH_CONDITION_RESERVATION_DT);
        for (String selectCondition : selectConditionList) {
            selectConditionMap.put(selectCondition, selectCondition);
        }

        return selectConditionMap;
    }

    /**
     * 予約検針日時 時プルダウンMapを生成.
     * @return 自動検針日時 時プルダウンMap
     */
    private Map<String, String> createReserveInspHMap(String hourDispFormat) {
        Map<String, String> reserveInspHMap = new LinkedHashMap<>();

        for (int i = RESERVATION_HOUR_MIN; i <= RESERVATION_HOUR_MAX; i++) {
            reserveInspHMap.put(String.format(hourDispFormat, i), String.format("%02d", i));
        }

        return reserveInspHMap;
    }

    /**
     * 予約検針日時 分プルダウンMapを生成.
     * @return 自動検針日時 分プルダウンMap
     */
    private Map<String, String> createReserveInspMMap(String minuteDispFormat) {
        Map<String, String> reserveInspMMap = new LinkedHashMap<>();

        reserveInspMMap.put(String.format(minuteDispFormat, 0), String.format("%02d", 0));
        reserveInspMMap.put(String.format(minuteDispFormat, 30), String.format("%02d", 30));

        return reserveInspMMap;
    }

    public Date getUpdReserveInspYMD() {
        return updReserveInspYMD;
    }

    public void setUpdReserveInspYMD(Date updReserveInspYMD) {
        this.updReserveInspYMD = updReserveInspYMD;
    }

    public String getUpdReserveInspH() {
        return updReserveInspH;
    }

    public void setUpdReserveInspH(String updReserveInspH) {
        this.updReserveInspH = updReserveInspH;
    }

    public String getUpdReserveInspM() {
        return updReserveInspM;
    }

    public void setUpdReserveInspM(String updReserveInspM) {
        this.updReserveInspM = updReserveInspM;
    }

    public String getUpdReserveInspYMDFmt() {
        if (this.updReserveInspYMD == null) {
            return "----年--月--日";
        }
        return DateUtility.changeDateFormat(this.updReserveInspYMD, DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER);
    }
}
