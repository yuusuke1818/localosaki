package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspMonthNoParameter;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.request.sms.meterreading.UpdateSmsInspectionMeterBefRequest;
import jp.co.osaki.osol.api.request.sms.meterreading.UpdateSmsInspectionMeterBefRequestSet;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsInspMonthNoResponse;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsInspectionMeterBefResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * 確定前検針データ確定登録画面
 * @author kobayashi.sho
 */
@Named(value = "smsCollectSettingMeterreadingInspectionMeterBefBean")
@ConversationScoped
public class InspectionMeterBefBean extends SmsBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 9167495115470961829L;

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
    private InspectionMeterBefListBean inspectionMeterBefListBean;

    // プルダウンリストクラス
    @Inject
    private PullDownList toolsPullDownList;

    // エラーリスト（背景色用）
    private List<String> invalidComponent;

    /** プルダウンリスト：検針種別. */
    private Map<String, String> inspTypeMap;

    /** プルダウンリスト：連番. */
    private Map<String, String> inspMonthNoMap; // null:「連番」欄 非活性

    /** 空文字. */
    private static final String BLANK = "";

    // 検針種別：定期検針
    private static final String INSP_TYPE_REGULAR = "r";
    //// 検針種別：臨時検針
    //private static final String INSP_TYPE_TEMPORARY = "t";

    /** 接続先 */
    private String devId;

    /** 接続名. */
    private String devName;

    /** （確定前検針データ一覧表示画面 で選択した）確定前検針データ. */
    private List<InspectionMeterBef> meterBefList;

    /** 検針年月. */
    private String inspYearMonth;

    /** 検針種別. */
    private String inspType;

    /** 月検針連番 (範囲：1～999). */
    private String inspMonthNo;

    /** 登録時にすでに登録されていた"メーター管理番号"のリスト(登録確認メッセージ表示用). */
    private List<Long> warnExistingIdList;

    /** 強制実行フラグ(false:[登録]ボタン押下時  true:強制実行確認ダイアログで[OK]ボタン押下時(登録済み"メーター管理番号"チェックを行わずに書き込みを行う)). */
    private boolean isForcedWrite;

    /**
     * この内容で登録します。よろしいですか？
     * @return 確認メッセージ
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    /**
     * 「登録」ボタン操作を行い、書き込み対象のメーター管理番号がすでに存在するした場合の確認メッセージ
     * メッセージ：
     *      同月に検針確定が行われたデータがあります。
     *      （管理番号:{0}）
     *      選択されたメーターにて確定処理を実行しますが、よろしいですか？
     * @return 確認メッセージ  null:警告なし正常)  null以外:確認ダイアログを表示する
     */
    public String getConfReWriteOfExisting() {
        if (this.warnExistingIdList == null || this.warnExistingIdList.isEmpty()) {
            return null;
        }

        // メーター管理番号 が複数ある場合、","区切りに整形
        String existingIds = this.warnExistingIdList.stream().map(meterMngId -> meterMngId.toString()).collect(Collectors.joining(","));

        return beanMessages.getMessageFormat("smsCollectSettingMeterreadingInspectionMeterBefBean.error.confReWriteOfExisting",
                new String[] { existingIds });
    }

    /**
     * 画面初期化.
     */
    @Override
    public String init() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:init():START"));

        // --- 画面値セット ---

        // エラーリスト（背景色用）初期化
        invalidComponent = new ArrayList<>();

        // 検針種別：定期検針
        this.inspType = INSP_TYPE_REGULAR;

        // 検針年月
        this.inspYearMonth = DateUtility.changeDateFormat(new Date(), DateUtility.DATE_FORMAT_YYYYMM_SLASH);

        // ※連番は「changeMonthNoPulldown()」内でセット

        // --- プルダウンリストのMap生成 ---

        // 検針種別
        this.inspTypeMap = toolsPullDownList.getInspType(false, null);

        // 連番
        changeMonthNoPulldown();

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:init():END"));
        return "smsCollectSettingMeterreadingInspectionMeterBef";
    }

    /**
     * 確定前検針データ一覧表示画面からの画面遷移時の処理
     * @param devId 接続先
     * @param devName 接続先名称
     * @param meterBefList 確定前検針データ一覧表示画面で選択した行の情報
     * @return 画面Beanページ
     */
    public String initEdit(String devId, String devName, List<InspectionMeterBef> meterBefList) {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:initType():START"));

        // 接続先セット
        this.devId = devId;

        // 接続先名称セット
        this.devName = devName;

        // 確定前検針データ一覧表示画面 で選択した、 確定前検針データ
        this.meterBefList = meterBefList;

        // 画面初期化
        init();

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:initType():END"));
        return "smsCollectSettingMeterreadingInspectionMeterBef";
    }

    /**
     * 検針年月(inspYearMonth)の書式をチェックして、書式:"yyyy/MM" に整形する
     */
    private void formatInspYearMonth() {
        if (this.inspYearMonth == null) {
            // 未入力
            return;
        }

        int len = this.inspYearMonth.length();
        if (len == 7 && CheckUtility.checkRegDateYmd(this.inspYearMonth + "/01")) {
            // 正常(正しい書式)
            return;
        } else if (len < 5) { // 最短の書式:"yyyyM"に満たない
            // 整形不可(桁数不足)
            return;
        }

        // 書式ミスがある → 補正できるなら補正する
        Date tmpInspYearMonth = DateUtility.conversionDate(this.inspYearMonth, DateUtility.DATE_FORMAT_YYYYMM_SLASH);
        if (tmpInspYearMonth == null) {
            tmpInspYearMonth = DateUtility.conversionDate(this.inspYearMonth, DateUtility.DATE_FORMAT_YYYYMM);
        }
        if (tmpInspYearMonth != null) {
            // 書式変換:yyyy/MM
            this.inspYearMonth = DateUtility.changeDateFormat(tmpInspYearMonth, DateUtility.DATE_FORMAT_YYYYMM_SLASH);
        }
    }

    /**
     * 「検針年月」または「検針種別」編集時の処理.
     * 「検針年月」と「検針種別」を条件に「連番」のプルダウンリストを取得.
     * ※事前に「装置ID(devId)」「検針年月(inspYearMonth)」「検針種別(inspType)」をセットすること.
     */
    @Logged
    public void changeMonthNoPulldown() {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:changeMonthNoPulldown():START"));

        formatInspYearMonth(); // 検針年月(inspYearMonth)の書式("yyyy/MM")補正

        // 必須チェック・書式チェック → 必須未設定または書式エラーの場合は、以下の処理を行わない
        if (this.inspYearMonth == null || this.inspType == null || !DateUtility.checkRegDateYmSlash(this.inspYearMonth)) {
            return;
        }

        ListSmsInspMonthNoResponse response = new ListSmsInspMonthNoResponse();
        ListSmsInspMonthNoParameter parameter = new ListSmsInspMonthNoParameter();
        parameter.setBean("ListSmsInspMonthNoBean");
        parameter.setDevId(this.devId); // 装置ID
        parameter.setInspYear(convYear(this.inspYearMonth)); // 検針年
        parameter.setInspMonth(convMonth(this.inspYearMonth)); // 検針月
        parameter.setInspType(this.inspType); // 検針種別：R:定期検針 T:臨時検針

        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsInspMonthNoResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // 「連番」のプルダウンリスト一覧を取得した
            List<Long> inspMonthNoList = response.getResult().getInspMonthNoList();

            if (!inspMonthNoList.isEmpty()) {
                Map<String, String> tmpInspMonthNoMap = new LinkedHashMap<>();
                tmpInspMonthNoMap.put(BLANK, BLANK); // 空行追加
                inspMonthNoList.stream().forEach(row -> {
                    String inspMonthNo  = String.valueOf(row);
                    tmpInspMonthNoMap.put(inspMonthNo, inspMonthNo);
                });
                this.inspMonthNoMap = tmpInspMonthNoMap;
            } else {
                // 検索結果0件
                this.inspMonthNoMap = null;
            }
        } else {
            // 取得失敗
            this.inspMonthNoMap = null;
        }

        this.inspMonthNo = BLANK;

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:changeMonthNoPulldown():END"));
    }

    /**
     * 登録ボタン押下時の処理
     * @return 画面Beanページ
     */
    @Logged
    public String regist() throws ParseException {
        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:regist():START"));

        // 企業ID・建物IDチェック
        ListInfo listInfo = topBean.getTopBeanProperty().getListInfo();

        formatInspYearMonth(); // 検針年月(inspYearMonth)の書式("yyyy/MM")補正

        if (!validate()) {
            // 入力チェック・エラー
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:regist():END (validate error)"));
            return "";
        }

        String inspYear = convYear(this.inspYearMonth);   // 検針年
        String inspMonth = convMonth(this.inspYearMonth);  // 検針月

        // 確定前検針データ(複数行) ※Listを直接かけないのでワンクッション
        UpdateSmsInspectionMeterBefRequest meters = new UpdateSmsInspectionMeterBefRequest( this.meterBefList.stream()
                .map(row -> new UpdateSmsInspectionMeterBefRequestSet(row.getMeterMngId(), row.getLatestInspDate(), row.getMulti(), row.getLatestInspVal(), row.getVersion()))
                .collect(Collectors.toList()));

        UpdateSmsInspectionMeterBefResponse response = new UpdateSmsInspectionMeterBefResponse();
        UpdateSmsInspectionMeterBefParameter parameter = new UpdateSmsInspectionMeterBefParameter();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSmsInspectionMeterBefBean");
        parameter.setCorpId(listInfo.getCorpId());  // 企業ID
        parameter.setBuildingId(new Long(listInfo.getBuildingId())); // 建物ID
        parameter.setIsForcedWrite(this.isForcedWrite); // 強制実行フラグ(false:[登録]ボタン押下時  true:強制実行確認ダイアログで[OK]ボタン押下時)
        parameter.setDevId(this.devId);             // 装置ID(接続先)(画面項目)
        parameter.setInspYear(inspYear);            // 検針年(画面項目)
        parameter.setInspMonth(inspMonth);          // 検針月(画面項目)
        parameter.setInspType(this.inspType);       // 検針種別(画面項目)：R:定期検針 T:臨時検針
        parameter.setInspMonthNo(convLong(this.inspMonthNo)); // 月検針連番 (範囲：1～999)
        parameter.setMeters(meters);

        // 登録・更新
        response = (UpdateSmsInspectionMeterBefResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        UpdateSmsInspectionMeterBefResult result = response.getResult();

        // 登録済み"メーター管理番号"チェック → 強制実行確認ダイアログを表示
        this.warnExistingIdList = result == null ? null : result.getWarnExistingIdList(); // 登録時にすでに登録されていた"メーター管理番号"のリスト(登録確認メッセージ表示用)
        if (this.warnExistingIdList != null && !this.warnExistingIdList.isEmpty()) {
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:regist():END (existing meterMngId)"));
            return "";
        }

        // 全件正常終了か？
        if (OsolApiResultCode.API_OK.equals(response.getResultCode())
            && result != null && result.getSuccessCnt() > 0 && result.getFailCnt() == 0) {
            // 全件正常終了 → 一覧画面に戻る
            eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:regist():END (success)"));
            return inspectionMeterBefListBean.initReturn(); // 一覧画面に戻る
        }

        if (result != null && result.getMsgList() != null && !result.getMsgList().isEmpty()) {
            // アラートメッセージあり（異常終了 または 一部正常終了）
            if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
                // エラー終了している場合 → メッセージを表示
                addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            }
            // アラートメッセージ表示
            result.getMsgList().stream().forEach(msg -> addWarningMessage(msg));
        } else {
            // アラートメッセージなし → 通常のメッセージを表示
            if (OsolApiResultCode.API_OK.equals(response.getResultCode())) {
                // 正常
                addMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            } else {
                addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            }
        }

        // 確定前検針データ(未処理データ)を取得
        if (result != null && result.getSuccessCnt() > 0) {
            // 正常処理が１件以上ある → 確定前検針データ を更新する (確定前検針データが処理された分、減るため)
            // ※未処理のデータなくなったときは 登録ボタン を非活性にする
            if (result.getFailList() != null) {
                this.meterBefList = result.getFailList().stream()
                        .map(row -> new InspectionMeterBef(row))
                        .collect(Collectors.toList());
            }
        }

        eventLogger.debug(packageName.concat(" smsCollectSettingMeterreadingInspectionMeterBefBean:regist():END"));
        return "";
    }

    /**
     * 入力チェック.
     * @return true:入力OK false:入力NG
     */
    private boolean validate() {
        boolean isChkOk = true;
        invalidComponent = new ArrayList<>();

        // 検針年月
        if (CheckUtility.isNullOrEmpty(this.inspYearMonth) || !CheckUtility.checkRegDateYmd(this.inspYearMonth + "/01")) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefBean.error.inspYearMonth"));
            invalidComponent.add("smsCollectSettingMeterreadingInspectionMeterBefBean:inspYearMonth");
            isChkOk = false;
        }

        // 検針種別
        if (CheckUtility.isNullOrEmpty(this.inspType)) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefBean.error.inspType"));
            isChkOk = false;
        }

        // 確定前検針データ
        if (this.meterBefList == null || this.meterBefList.size() == 0) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingInspectionMeterBefBean.error.meterBefList"));
            isChkOk = false;
        }

        return isChkOk;
    }

    /**
     * StringをLongに変換
     * @param val String型の数値
     * @return Long型の数値
     */
    private Long convLong(String val) {
        if (val == null || val.isEmpty() || !StringUtility.isNumeric(val)) {
            return null;
        }
        return new Long(val);
    }

    /**
     * 年月から年を取得
     * @param yearMonth 年月
     * @return 年
     */
    private String convYear (String yearMonth) {
        if (yearMonth == null || yearMonth.length() < 4) {
            return null;
        }
        return yearMonth.substring(0, 4);
    }

    /**
     * 年月から月を取得
     * ※formatInspYearMonth()後に使用すること
     * @param yearMonth 年月
     * @return 月 ※1～9月は1桁 10～12月は2桁 で返す
     */
    private String convMonth(String yearMonth) {
        if (yearMonth == null || yearMonth.length() < 6) {
            return null;
        }
        return yearMonth.substring(yearMonth.length() - 2).replaceAll("^0+([1-9])", "$1");
    }

    /**
     * デザイン指定
     */
    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    public List<InspectionMeterBef> getMeterBefList() {
        return meterBefList;
    }

    public Map<String, String> getInspTypeMap() {
        return inspTypeMap;
    }

    public Map<String, String> getInspMonthNoMap() {
        return inspMonthNoMap;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getInspYearMonth() {
        return inspYearMonth;
    }

    public void setInspYearMonth(String inspYearMonth) {
        this.inspYearMonth = inspYearMonth;
    }

    public String getInspType() {
        return inspType;
    }

    public void setInspType(String inspType) {
        this.inspType = inspType;
    }

    public String getInspMonthNo() {
        return inspMonthNo;
    }

    public void setInspMonthNo(String inspMonthNo) {
        this.inspMonthNo = inspMonthNo;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Boolean getIsForcedWrite() {
        return isForcedWrite;
    }

    public void setIsForcedWrite(Boolean isForcedWrite) {
        this.isForcedWrite = isForcedWrite;
    }

}
