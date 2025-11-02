package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang.StringUtils;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.function.dao.MCorpFunctionUseDao;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.DeleteSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsMeterNowValParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsWirelessIdHistoryParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterNowValCommandParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateBulkSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterAlertParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.DeleteSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.DeleteSmsMeterRequestSet;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.InsertSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateBulkSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateBulkSmsMeterRequestSet;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterAlertRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterAlertRequestSet;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.DeleteSmsMeterResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.GetLteMNowValResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.GetSmsMeterNowValResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.GetSmsWirelessIdHistoryResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.InsertSmsMeterNowValCommandResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.InsertSmsMeterResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListSmsMeterResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateBulkSmsMeterResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateSmsMeterAlertResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetLteMNowValResult;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.LteMMeterExecResult;
import jp.co.osaki.osol.api.resultdata.sms.meter.GetSmsWirelessIdHistoryResultData;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterInfo;
import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MMeterLoadlimitPK;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.osol.web.csv.sms.converter.MeterManagementCsvConverter;
import jp.co.osaki.osol.web.csv.sms.converter.MeterReadingCsvConverter;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.co.osaki.sms.SmsConstants.METER_UPDATE_PATTERN;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsFileUpload;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterManagementConstants.DISP_UPDATE_STATUS;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterManagementConstants.RATED_CURRENT_CHECK;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MDevPrmListDao;
import jp.co.osaki.sms.dao.MGenericTypeDao;
import jp.co.osaki.sms.dao.MMeterListDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevPrmDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterInfoDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterLoadlimitDao;

/**
 * メーター管理画面.
 *
 * @author kimura.m
 */
@Named("smsCollectSettingMeterMeterManagementBean")
@ConversationScoped
public class MeterManagementBean extends SmsConversationBean implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 7715685535332632828L;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 選択値 接続先(上位連携装置ID). */
    private String devId;

    /** 選択値 接続先名. */
    private String devName;

    /** 装置種別のフラグ */
    private String devType;

    /** 接続先プルダウンMap. */
    private Map<String, String> devIdMap;

    /** 選択値 メーター表示切替(ページ). */
    private int page;

    /** メーター表示切替(ページャー)プルダウンMap. */
    private Map<Long, Integer> pagerMap;

    /** メーター管理番号. */
    private Long meterMngId;

    /** 一括登録にてメータID(計器ID)の重複確認用 */
    private List<String> meterIdList = new ArrayList<>();

    /** メーター管理番号プルダウンMap. */
    private Map<Long, Long> meterMngIdMap;

    /** ユーザコード. */
    private Long tenantId;

    /** テナント番号 */
    private String buildingNo;

    /** 負荷制限プルダウンMap. */
    private Map<String, String> loadlimitMap;

    /** 負荷制限_定数 */
    public static enum LOAD_LIMIT {
        INVALID("無効"),
        VALID("有効"),
        NONE("機能なし");

        private final String val;

        private LOAD_LIMIT(final String val) {
            this.val = val;
        }
        public String getVal() {
            return this.val;
        }
    }
    /** 負荷制限_定数(画面用) */
    public static enum LOAD_LIMIT_MODE {
        INVALID("0"), // なし
        VALID("1"), // あり
        TEMP_VALID("A"), // 臨時設定
        TEMP_INVALID("R"); // 臨時解除

        private final String val;

        private LOAD_LIMIT_MODE(final String val) {
            this.val = val;
        }
        public String getVal() {
            return this.val;
        }
    }

    /** 負荷制限. */
    private String loadlimit;

    /** 負荷電流プルダウンMap. */
    private Map<String, String> curlimitMap;

    /** 負荷電流_基本. */
    private String basicCurlimit;

    /** 負荷電流_臨時. */
    private String tmpCurlimit;

    /** 自動投入_基本. */
    private String basicAutorec;

    /** 自動投入_臨時. */
    private String tmpAutorec;

    /** 開閉器動作カウントプルダウンMap. */
    private Map<String, String> occountMap;

    /** 開閉器動作カウント_基本. */
    private String basicOccount;

    /** 開閉器動作カウント_臨時. */
    private String tmpOccount;

    /** 開閉器カウントクリアプルダウンMap. */
    private Map<String, String> cntclrMap;

    /** 開閉器カウントクリア_基本. */
    private String basicCntclr;

    /** 開閉器カウントクリア_臨時. */
    private String tmpCntclr;

    /** 開閉区分プルダウンMap. */
    private Map<String, String> ocstateMap;

    /** 開閉区分. */
    private String ocstate;

    /** 計器ID. */
    private String equipId;

    /** コメント */
    private String comment;

    /** メーター種別プルダウンMap. */
    private Map<String, Long> metertypeMap;

    /** メーター種別. */
    private String metertype;

    /** 検満年月_年種別ラジオボタンMap. */
    private Map<String, String> dispYearMap;

    /** 検満年月_年種別. */
    private String dispYear;

    /** 検満年月_元号プルダウンMap. */
    private Map<String, String> gengoMap;

    /** 検満年月_元号. */
    private String gengo;

    /** 検満年月_年. */
    private String year;

    /** 検満年月_月. */
    private String month;

    /** 検満通知月. */
    private String examNoticeMonth;

    /** 検満通知プルダウンMap. */
    private Map<String, String> examnoticeMap;

    /** 検満通知. */
    private String examnotice;

    /** メーター状況プルダウンMap. */
    private Map<String, String> meterPresSituMap;

    /** メーター状況.  */
    private BigDecimal meterPresentSituation;

    /** アラート停止期間 開始日. */
    private String alertPauseStart;

    /** アラート停止期間 終了日. */
    private String alertPauseEnd;

    /** アラート停止フラグ. */
    private BigDecimal alertPauseFlg;

    /** メーター状態備考 */
    private String meterStatusMemo;

    /** アップロード日時 */
    private String uploadDate;

    /** アップロードファイル */
    private Part uploadFile;

    /** アップロードファイル名 */
    private String uploadFileName;

    /** アップロードファイルパス */
    private String uploadFilePath;

    /** ファイルアップロードエラー */
    private List<String> fileUploadErrorList = new ArrayList<>();

    /** アップロードCSV内容 */
    private List<List<String>> uploadCsvContent = new ArrayList<>();

    /** 一括登録時エラーリスト */
    private List<String> bulkErrorList = new ArrayList<>();

    /** 汎用区分マスタDao. */
    @EJB
    private MGenericTypeDao mGenericTypeDao;

    /** [共通] 装置情報Dao. */
    @EJB
    private MDevPrmListDao mDevPrmListDao;

    /** メータ登録用 Dao */
    @EJB
    private MMeterListDao mMeterListDao;
    @EJB
    private MMeterDao mMeterDao;

    /** メータ負荷制限設定 Dao */
    @EJB
    private MMeterLoadlimitDao mMeterLoadlimitDao;

    /** メーター詳細情報 Dao */
    @EJB
    private MMeterInfoDao mMeterInfoDao;

    /** 企業機能利用マスタ Dao */
    @EJB
    private MCorpFunctionUseDao MCorpFunctionUseDao;

    @Inject
    private PullDownList toolsPullDownList;

    /** 一覧表示用メーター情報リスト */
    private List<MeterInfo> meterList;

    /** スマメ登録用プロパティ */
    @Inject
    MeterManagementInputProperty smartInsertProperty;

    /** スマメ更新用プロパティ */
    @Inject
    MeterManagementInputProperty smartUpdateProperty;

    /** スマメ設定更新用プロパティ */
    @Inject
    MeterManagementInputProperty smartSettingUpdateProperty;

    /** パルス登録用プロパティ */
    @Inject
    MeterManagementInputProperty pulseInsertProperty;

    /** パルス更新用プロパティ */
    @Inject
    MeterManagementInputProperty pulseUpdateProperty;

    /** パルス設定更新用プロパティ */
    @Inject
    MeterManagementInputProperty pulseSettingUpdateProperty;

    /** IoT-R登録用プロパティ */
    @Inject
    MeterManagementInputProperty iotRInsertProperty;

    /** IoT-R更新用プロパティ */
    @Inject
    MeterManagementInputProperty iotRUpdateProperty;

    /** IoT-R設定更新用プロパティ */
    @Inject
    MeterManagementInputProperty iotRSettingUpdateProperty;

    /** ハンディ登録用プロパティ */
    @Inject
    MeterManagementInputProperty handyInsertProperty;

    /** ハンディ更新用プロパティ */
    @Inject
    MeterManagementInputProperty handyUpdateProperty;

    /** OCR登録用プロパティ */
    @Inject
    MeterManagementInputProperty ocrInsertProperty;

    /** OCR更新用プロパティ */
    @Inject
    MeterManagementInputProperty ocrUpdateProperty;

    /** スマメ画面共通のプロパティ */
    @Inject
    MeterManagementCommonProperty smartProperty;

    /** パルメ画面共通のプロパティ */
    @Inject
    MeterManagementCommonProperty pulseProperty;

    /** IoT-R画面共通のプロパティ */
    @Inject
    MeterManagementCommonProperty iotRProperty;

    /** ハンディ画面共通のプロパティ */
    @Inject
    MeterManagementCommonProperty handyProperty;

    /** OCR画面共通のプロパティ */
    @Inject
    MeterManagementCommonProperty ocrProperty;

    /** リレー無線ID履歴ポップアップ用プロパティ */
    @Inject
    HopIdHistoryProperty hopIdHistoryProperty;

    /** メーター状態登録用プロパティ(スマート) */
    @Inject
    MeterStatusProperty smartMeterStatusProperty;

    /** メーター状態登録用プロパティ(パルス) */
    @Inject
    MeterStatusProperty pulseMeterStatusProperty;

    /** メーター状態登録用プロパティ(IoT-R) */
    @Inject
    MeterStatusProperty iotRMeterStatusProperty;

    /** 現在値用プロパティ */
    private NowValueProperty nowValProperty;

    /** LTE-M 現在値表示用リスト*/
    private List<GetLteMNowValResponse> lteMNowValResponseList;

    /** LTE-M 現在値取得 帳票出力用取得ID */
    private String currentDownloadId;
    /** 帳票出力用 状態表示更新ステータス */
    private String dispUpdateStatus; // 取得中/取得成功/取得失敗
    /** 状態取得実行時刻 */
    private String currentValueDispDate;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private SmsMessages beanMessages;

    @Inject
    private SmsFileUpload fileUploader;

    @Inject
    private LteMParallelExecutor lteMParallelExecutor;

    @Resource
    private ManagedScheduledExecutorService managedScheduledExecutorService;

    /**
     * 汎用区分
     */
    @Inject
    private GenericTypeList genericTypeList;

    /** メーター管理番号 パルス現在値取得画面用 */
    private Long pulseCurrentValueMeterMngId;

    /** メーター管理番号 IoT-R現在値取得画面用 */
    private Long iotRCurrentValueMeterMngId;

    /** メーター管理番号 ハンディ現在値取得画面用 */
    private Long handyCurrentValueMeterMngId;

    /** テーブル表示件数 */
    private int dispCnt;

    /** 現在値取得画面のメーター管理番号プルダウンMap. */
    private Map<Long, Long> nowValMeterMngIdMap;

    /** 現在値取得画面のメーター管理番号プルダウン 選択値(スマート) */
    private Long nowValMeterMngIdSmart;

    /** 現在値取得画面のメーター管理番号プルダウン 選択値(パルス) */
    private Long nowValMeterMngIdPulse;

    /** 現在値取得画面のメーター管理番号プルダウン 選択値(IoT-R) */
    private Long nowValMeterMngIdIotR;

    /** 登録・更新後にリロード→同じ装置を表示できるように保持しておく用 */
    private String keepDevId = null;

    /** タブ切り替え時に接続先とメーター表示切替を保持しておく用 */
    private Map<String, MeterDispChangeInfo> changeSmartTabKeepDevIdMeterDispChangeMap;
    private Map<String, MeterDispChangeInfo> changePulseTabKeepDevIdMeterDispChangeMap;
    private Map<String, MeterDispChangeInfo> changeIotRTabKeepDevIdMeterDispChangeMap;
    private Map<String, MeterDispChangeInfo> changeHandyTabKeepDevIdMeterDispChangeMap;
    private Map<String, MeterDispChangeInfo> changeOcrTabKeepDevIdMeterDispChangeMap;

    /** タブ切り替え時にスマートメーターとパルスメーターの装置IDを保持しておく用 */
    private String changeSmartPulseTabKeepDevId = null;

    /** タブ切り替え時にIoT-R連携用メーターの装置IDを保持しておく用 */
    private String changeIotRTabKeepDevId = null;

    /** タブ切り替え時にハンディ検針用メーターの装置IDを保持しておく用 */
    private String changeHandyTabKeepDevId = null;

    /**
     * タブ切り替え時にAieLink用メーターの装置IDを保持しておく用
     * 「OCR検針」→「AieLink」へ変更
     */
    private String changeOcrTabKeepDevId = null;

    /** メーター種類 */
    private String meterKind = SmsConstants.METER_KIND.SMART.getVal();

    /** パルス種別Map */
    private Map<String, String> pulseTypeMap;

    /** IoT-R種別Map */
    private Map<String, String> iotRTypeMap;

    /** ハンディ種別Map */
    private Map<String, String> handyTypeMap;

    /** スマート・パルスタブ表示フラグ */
    private boolean smartPulseTabViewFlg;

    /** スマート・パルスタブ表示フラグ */
    private boolean iotRTabViewFlg;

    /** ハンディタブ表示フラグ */
    private boolean handyTablViewFlg;

    /**
     * AieLink用メータータブ表示フラグ
     * 「OCR検針」→「AieLink」へ変更
     */
    private boolean ocrTablViewFlg;

    /** 切替元タブ */
    private String prevMeterKind;

    /** メーターリスト件数が最大件数に達しているか */
    private boolean isListMax;

    /** メーター登録状態 アラート停止期間開始日エラーフラグ */
    boolean apsErrorFlg = false;

    /** メーター登録状態 アラート停止期間終了日エラーフラグ */
    boolean apeErrorFlg = false;

    /** 送信フラグ(一括登録) */
    private boolean sendLumpFlg;

    /** 機器への設定送信(一括登録)_定数 */
    private static enum SEND_DEVICE {
        NOT_SEND("送信しない"),
        SEND("送信する");

        private final String val;

        private SEND_DEVICE(final String val) {
            this.val = val;
        }
        public String getVal() {
            return this.val;
        }
    }

    /** 送信フラグ(削除) */
    private boolean sendDelFlg;

    /** 更新区分 */
    private Map<String, String> updateKbn;

    /** 更新対象行数 */
    private int updateRowNum;
    /** 登録対象行数 */
    private int insertRowNum;

    private List<String> invalidComponent;

    /** 変更_定数 */
    private static enum CHECK_CHANGE {
        NOT_CHANGE("変更しない"),
        CHANGE("変更する");

        private final String val;

        private CHECK_CHANGE(final String val) {
            this.val = val;
        }
        public String getVal() {
            return this.val;
        }
    }

    // 動的にxhtmlファイル内の値を設定する
    private String reflectControl;

    public String getReflectControl() {
        return reflectControl;
    }

    @Logged
    public void setReflectControl(String reflectControl) {
        this.reflectControl = reflectControl;
    }

    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    private boolean lteMDeviceFlg = false;

    public boolean isLteMDeviceFlg() {
        return lteMDeviceFlg;
    }

    /**
     * 機器がLTE-M かつ 現在値取得画面を表示していることの判定フラグ
     */
    private boolean lteMgetCurrentValueFlg = false;

    public boolean isLteMgetCurrentValueFlg() {
        return lteMgetCurrentValueFlg;
    }

    public void setLteMgetCurrentValueFlg(boolean lteMgetCurrentValueFlg) {
        this.lteMgetCurrentValueFlg = lteMgetCurrentValueFlg;
    }

    /**
     * 機器がLTE-M かつ 操作権限が付与されていることの判定フラグ
     */
    private boolean lteMFunctionUseAuthFlg = true;

    public boolean isLteMFunctionUseAuthFlg() {
        return lteMFunctionUseAuthFlg;
    }

    public void setLteMFunctionUseAuthFlg(boolean lteMFunctionUseAuthFlg) {
        this.lteMFunctionUseAuthFlg = lteMFunctionUseAuthFlg;
    }

    /** LTE-M API成功フラグ(true:API実行成功  false:API実行失敗 もしくは API実行対象が無し) */
    private boolean ltemApiExecSuccessFlg = false;

    public boolean isLtemApiExecSuccessFlg() {
        return ltemApiExecSuccessFlg;
    }

    public void setLtemApiExecSuccessFlg(boolean ltemApiExecSuccessFlg) {
        this.ltemApiExecSuccessFlg = ltemApiExecSuccessFlg;
    }

    /**
     * LTE-Mの設定APIと同期APIにて使用 API実行にて成功した数
     */
    private int apiSuccessCount;
    /**
     * LTE-Mの設定APIと同期APIにて使用 API実行にて失敗した数
     */
    private int apiFailedCount;
    /**
     * LTE-Mの設定APIと同期APIにて使用 API実行にて成功したメーター管理番号
     */
    private String apiSuccessCsv;
    /**
     * LTE-Mの設定APIと同期APIにて使用 API実行にて失敗したメーター管理番号
     */
    private String apiFailedCsv;
    /**
     * LTE-Mの現在値取得のDB取得にて成功した数
     */
    private int dbSuccessCount;
    /**
     * LTE-Mの現在値取得のDB取得にて失敗した数
     */
    private int dbFailedCount;
    /**
     * LTE-Mの現在値取得のDB取得にて成功したメーター管理番号
     */
    private String dbSuccessCsv;
    /**
     * LTE-Mの現在値取得のDB取得にて失敗したメーター管理番号
     */
    private String dbFailedCsv;
    /**
     * LTE-Mの設定APIと同期APIにてボタン「状態表示更新」の活性/非活性
     * true:活性 false:非活性
     */
    private boolean dispUpdateButtonEnabled;

    /**
     * 設定内容変更 負荷制限 制御用
     */
    public boolean isSettingBasicEnabled() {
        String mode = smartSettingUpdateProperty.getLoadlimitMode();
        return LOAD_LIMIT_MODE.INVALID.getVal().equals(mode)
                || LOAD_LIMIT_MODE.VALID.getVal().equals(mode); // 「なし」「あり」の場合、基本設定を活性化
    }
    public boolean isSettingTempEnabled() {
        String mode = smartSettingUpdateProperty.getLoadlimitMode();
        return LOAD_LIMIT_MODE.TEMP_VALID.getVal().equals(mode); // 「臨時設定」の場合、臨時設定を活性化
    }

    public int getApiSuccessCount() {
        return apiSuccessCount;
    }

    public void setApiSuccessCount(int apiSuccessCount) {
        this.apiSuccessCount = apiSuccessCount;
    }

    public int getApiFailedCount() {
        return apiFailedCount;
    }

    public void setApiFailedCount(int apiFailedCount) {
        this.apiFailedCount = apiFailedCount;
    }

    public String getApiSuccessCsv() {
        return apiSuccessCsv;
    }

    public void setApiSuccessCsv(String apiSuccessCsv) {
        this.apiSuccessCsv = apiSuccessCsv;
    }

    public String getApiFailedCsv() {
        return apiFailedCsv;
    }

    public void setApiFailedCsv(String apiFailedCsv) {
        this.apiFailedCsv = apiFailedCsv;
    }
    public int getDbSuccessCount() {
        return dbSuccessCount;
    }

    public void setDbSuccessCount(int dbSuccessCount) {
        this.dbSuccessCount = dbSuccessCount;
    }

    public int getDbFailedCount() {
        return dbFailedCount;
    }

    public void setDbFailedCount(int dbFailedCount) {
        this.dbFailedCount = dbFailedCount;
    }

    public String getDbSuccessCsv() {
        return dbSuccessCsv;
    }

    public void setDbSuccessCsv(String dbSuccessCsv) {
        this.dbSuccessCsv = dbSuccessCsv;
    }

    public String getDbFailedCsv() {
        return dbFailedCsv;
    }

    public void setDbFailedCsv(String dbFailedCsv) {
        this.dbFailedCsv = dbFailedCsv;
    }

    public boolean isDispUpdateButtonEnabled() {
        return dispUpdateButtonEnabled;
    }
    public void setDispUpdateButtonEnabled(boolean dispUpdateButtonEnabled) {
        this.dispUpdateButtonEnabled = dispUpdateButtonEnabled;
    }

    /**
     * LTE-MのAPI実行結果格納用Map
     */
    private final Map<String, List<LteMMeterExecResult>> currentExecResults = new ConcurrentHashMap<>();

    /**
     * LTE-MのAPI実行にて最後にリクエストしたリクエストID
     */
    private volatile String lastRequestId;

    /** LTE-M 定格電流未取得時のフラグ */
    private boolean ratedCurrentMissingFlg = false;

    public boolean isRatedCurrentMissingFlg() {
        return ratedCurrentMissingFlg;
    }

    public void setRatedCurrentMissingFlg(boolean ratedCurrentMissingFlg) {
        this.ratedCurrentMissingFlg = ratedCurrentMissingFlg;
    }

    /** LTE-M 定格電流未取得時のメーター管理番号 */
    private String ratedCurrentMeterMngId;

    public String getRatedCurrentMeterMngId() {
        return ratedCurrentMeterMngId;
    }

    public void setRatedCurrentMeterMngId(String ratedCurrentMeterMngId) {
        this.ratedCurrentMeterMngId = ratedCurrentMeterMngId;
    }

    /** LTE-M 定格電流関連のエラーメッセージ */
    private Set<String> ratedCurrentMessageSet = new LinkedHashSet<>();

    public Set<String> getRatedCurrentMessageSet() {
        return ratedCurrentMessageSet;
    }

    public void setRatedCurrentMessageSet(Set<String> ratedCurrentMessageSet) {
        this.ratedCurrentMessageSet = ratedCurrentMessageSet;
    }

    /** 非同期処理にてエラー時格納するキュー */
    private final ConcurrentLinkedQueue<String> asyncUiErrors = new ConcurrentLinkedQueue<>();

    /** 一括登録ボタンの活性/非活性フラグ */
    private boolean batchRegistEnabled = true;

    public boolean isBatchRegistEnabled() {
        return batchRegistEnabled;
    }

    public void setBatchRegistEnabled(boolean batchRegistEnabled) {
        this.batchRegistEnabled = batchRegistEnabled;
    }

    @Override
    public String init() {
        conversationStart();
        initLteMApiInfo();
        // 遷移先
        return "meterManagement";
    }

    @Logged
    public String init(ListInfo buildingInfo) throws IllegalAccessException, InvocationTargetException {

        this.initInvalidComponent();

        this.buildingInfo = buildingInfo;
        meterKind = SmsConstants.METER_KIND.SMART.getVal();
        prevMeterKind = "";
        keepDevId = "";
        page = 0;
        changeSmartPulseTabKeepDevId = smartProperty.getDevId();
        this.uploadCsvContent.clear();
        this.fileUploadErrorList.clear();
        this.bulkErrorList.clear();

        // 接続先の初期化
        changeSmartPulseTabKeepDevId = "";
        changeIotRTabKeepDevId = "";
        changeHandyTabKeepDevId = "";
        changeOcrTabKeepDevId = "";

        // 接続先とメーター表示切替を保持するMapの初期化
        changeSmartTabKeepDevIdMeterDispChangeMap = new LinkedHashMap<>();
        changePulseTabKeepDevIdMeterDispChangeMap = new LinkedHashMap<>();
        changeIotRTabKeepDevIdMeterDispChangeMap = new LinkedHashMap<>();
        changeHandyTabKeepDevIdMeterDispChangeMap = new LinkedHashMap<>();
        changeOcrTabKeepDevIdMeterDispChangeMap = new LinkedHashMap<>();

        // 現在値表示リストの初期化
        lteMNowValResponseList = new ArrayList<GetLteMNowValResponse>();

        eventLogger.debug(MeterManagementBean.class.getPackage().getName().concat(" MeterManagementBean:init():START"));

        String ret = reload();

        // LTE-M判定
        if (devId != null && devId.startsWith(MeterManagementConstants.LTE_M_DEVICE_PREFIX)
                && Objects.equals(meterKind, METER_KIND.SMART.getVal())) {
            lteMDeviceFlg = true;
        } else {
            lteMDeviceFlg = false;
        }
        // LTE-M かつ 権限が付与されていない場合、フラグOFF
        lteMFunctionUseAuthFlg = true;
        String lteMDeviceFunctionCode = MeterManagementConstants.LTE_M_DEVICE_FUNCTION_CODE;
        MCorpFunctionUse mCorpFunctionUse = MCorpFunctionUseDao.find(lteMDeviceFunctionCode, getLoginCorpId());
        if (lteMDeviceFlg && mCorpFunctionUse == null) {
            lteMFunctionUseAuthFlg = false;
        }

        checkBatchRegistrationEnabled();

        eventLogger.debug(MeterManagementBean.class.getPackage().getName().concat(" MeterManagementBean:init():END"));

        return ret;
    }
    /**
     * 初期処理.
     * ※「タブ」切り替え時の処理を含む
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public String reload() throws IllegalAccessException, InvocationTargetException {

        hopIdHistoryProperty.setHopIdList(new ArrayList<>());

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        smartPulseTabViewFlg = false;
        iotRTabViewFlg = false;
        handyTablViewFlg = false;
        ocrTablViewFlg = false;

        // 機器への送信フラグ
        sendLumpFlg = false;

        this.uploadCsvContent.clear();
        this.fileUploadErrorList.clear();
        this.bulkErrorList.clear();
        this.uploadDate = "";
        this.uploadFile = null;
        this.uploadFileName = "";
        this.uploadFilePath = "";

        // 接続先  装置情報List
        List<MDevPrm> devPrmList = mDevPrmListDao.getDevPrmList(corpId, buildingId);
        List<MDevPrm> devListSmart = new ArrayList<>();
        List<MDevPrm> devListIotR = new ArrayList<>();
        List<MDevPrm> devListHandy = new ArrayList<>();
        List<MDevPrm> devListOcr = new ArrayList<>();
        List<MDevPrm> devList = new ArrayList<>();
        for (MDevPrm obj : devPrmList) {
            if (Objects.nonNull(obj.getDevId())) {
                if (obj.getDevId().startsWith(SmsConstants.DEVICE_KIND.IOTR.getVal())) {
                    devListIotR.add(obj);
                } else if (obj.getDevId().startsWith(SmsConstants.DEVICE_KIND.HANDY.getVal())) {
                    devListHandy.add(obj);
                } else if (obj.getDevId().startsWith(SmsConstants.DEVICE_KIND.OCR.getVal())) {
                    devListOcr.add(obj);
                } else {
                    devListSmart.add(obj);
                }
            }
        }

        // 装置IDチェック(スマートメーター)
        if (!devListSmart.isEmpty()) {
            // 装置ID に スマートメーターのデータがある → スマートメーター系のタブを表示(Visible)する
            smartPulseTabViewFlg = true;
        } else if (SmsConstants.METER_KIND.SMART.getVal().equals(meterKind) || SmsConstants.METER_KIND.PULSE.getVal().equals(meterKind)) {
            // 装置ID に スマートメーターのデータがない 且つ スマートメーター系のタブが選択されている → (強制的に)選択タブを変更する
            if (!devListHandy.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.HANDY.getVal();
            } else if (!devListOcr.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.OCR.getVal();
            } else if (!devListIotR.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.IOTR.getVal();
            }
        }

        // 装置IDチェック(IoT-R連携用メーター )
        if (!devListIotR.isEmpty()) {
            // 装置ID に IoT-R連携用メーターのデータがある → IoT-R連携用メーター タブを表示(Visible)する
            iotRTabViewFlg = true;
        } else if (SmsConstants.METER_KIND.IOTR.getVal().equals(meterKind)) {
            // 装置ID に IoT-R連携用メーターのデータがない 且つ IoT-R連携用メーター タブが選択されている → (強制的に)選択タブを変更する
            if (!devListSmart.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.SMART.getVal();
            } else if (!devListHandy.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.HANDY.getVal();
            } else if (!devListOcr.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.OCR.getVal();
            }
        }

        // 装置IDチェック(ハンディー検針用メーター)
        if (!devListHandy.isEmpty()) {
            // 装置ID に ハンディー検針用メーターのデータがある → ハンディー検針用メーター タブを表示(Visible)する
            handyTablViewFlg = true;
        } else if (SmsConstants.METER_KIND.HANDY.getVal().equals(meterKind)) {
            // 装置ID に ハンディー検針用メーターのデータがない 且つ ハンディー検針用メーター タブが選択されている → (強制的に)選択タブを変更する
            if (!devListSmart.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.SMART.getVal();
            } else if (!devListIotR.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.IOTR.getVal();
            } else if (!devListOcr.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.OCR.getVal();
            }
        }

        // 「OCR検針」→「AieLink」へ変更
        // 装置IDチェック(AieLink用メーター)
        if (!devListOcr.isEmpty()) {
            // 装置ID に AieLink用メーターのデータがある → AieLink用メーター タブを表示(Visible)する
            ocrTablViewFlg = true;
        } else if (SmsConstants.METER_KIND.OCR.getVal().equals(meterKind)) {
            // 装置ID に AieLink用メーターのデータがない 且つ AieLink用メーター タブが選択されている → (強制的に)選択タブを変更する
            if (!devListSmart.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.SMART.getVal();
            } else if (!devListHandy.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.HANDY.getVal();
            } else if (!devListIotR.isEmpty()) {
                meterKind = SmsConstants.METER_KIND.IOTR.getVal();
            }
        }

        if (SmsConstants.METER_KIND.HANDY.getVal().equals(meterKind)) {
            devList.addAll(devListHandy);
        } else if (SmsConstants.METER_KIND.IOTR.getVal().equals(meterKind)) {
            devList.addAll(devListIotR);
        } else if (SmsConstants.METER_KIND.OCR.getVal().equals(meterKind)) {
            devList.addAll(devListOcr);
        } else {
            devList.addAll(devListSmart);
        }
        setDevIdMap(toolsPullDownList.createDevPrmPullDownMap(devList));

        smartProperty = new MeterManagementCommonProperty();
        pulseProperty = new MeterManagementCommonProperty();
        iotRProperty = new MeterManagementCommonProperty();
        handyProperty = new MeterManagementCommonProperty();
        ocrProperty = new MeterManagementCommonProperty();

        if (devList != null && devList.size() > 0) {
            MDevPrm targetDev = devList.get(0);
            String tmpDevId = null;
            if (prevMeterKind.equals(meterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 切替元タブと切替先タブが同じ場合
                tmpDevId = keepDevId;
            } else {
                tmpDevId = targetDev.getDevId();
            }

            // タブ切り替え時に接続先(装置ID)とメーター表示切替を保持する
            MeterDispChangeInfo meterDispChangeInfo = new MeterDispChangeInfo();
            meterDispChangeInfo.setPagerMap(pagerMap);
            meterDispChangeInfo.setPage(page);
            if (SmsConstants.METER_KIND.SMART.getVal().equals(prevMeterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 切替元がタブ「スマートメーター」の場合
                changeSmartPulseTabKeepDevId = keepDevId;
                changeSmartTabKeepDevIdMeterDispChangeMap.put(keepDevId, meterDispChangeInfo);

            } else if (SmsConstants.METER_KIND.PULSE.getVal().equals(prevMeterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 切替元がタブ「パルスメーター」の場合
                changeSmartPulseTabKeepDevId = keepDevId;
                changePulseTabKeepDevIdMeterDispChangeMap.put(keepDevId, meterDispChangeInfo);

            } else if (SmsConstants.METER_KIND.IOTR.getVal().equals(prevMeterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 切替元がタブ「IoT-R連携用メーター」の場合
                changeIotRTabKeepDevId = keepDevId;
                changeIotRTabKeepDevIdMeterDispChangeMap.put(keepDevId, meterDispChangeInfo);

            } else if (SmsConstants.METER_KIND.HANDY.getVal().equals(prevMeterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 切替元がタブ「ハンディ検針用メーター」の場合
                changeHandyTabKeepDevId = keepDevId;
                changeHandyTabKeepDevIdMeterDispChangeMap.put(keepDevId, meterDispChangeInfo);

            } else if (SmsConstants.METER_KIND.OCR.getVal().equals(prevMeterKind) && (keepDevId != null && !keepDevId.isEmpty())) {
                // 「OCR検針」→「AieLink」へ変更
                // 切替元がタブ「AieLink用メーター」の場合
                changeOcrTabKeepDevId = keepDevId;
                changeOcrTabKeepDevIdMeterDispChangeMap.put(keepDevId, meterDispChangeInfo);

            }

            // 切替先のメーターのページを設定
            if (SmsConstants.METER_KIND.SMART.getVal().equals(meterKind)) {
                if (!( Objects.isNull(changeSmartTabKeepDevIdMeterDispChangeMap)|| Objects.isNull(changeSmartTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId)))) {
                    // Mapから接続先とメーター表示切替を取得
                    pagerMap = changeSmartTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId).getPagerMap();
                    page = changeSmartTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId).getPage();
                } else {
                    page = 0;
                }
            } else if (SmsConstants.METER_KIND.PULSE.getVal().equals(meterKind)) {
                if (!( Objects.isNull(changePulseTabKeepDevIdMeterDispChangeMap) || Objects.isNull(changePulseTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId)))) {
                    // Mapから接続先とメーター表示切替を取得
                    pagerMap = changePulseTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId).getPagerMap();
                    page = changePulseTabKeepDevIdMeterDispChangeMap.get(changeSmartPulseTabKeepDevId).getPage();
                } else {
                    page = 0;
                }
            } else if (SmsConstants.METER_KIND.IOTR.getVal().equals(meterKind)) {
                if (!( Objects.isNull(changeIotRTabKeepDevIdMeterDispChangeMap) || Objects.isNull(changeIotRTabKeepDevIdMeterDispChangeMap.get(changeIotRTabKeepDevId)))) {
                    // Mapから接続先とメーター表示切替を取得
                    pagerMap = changeIotRTabKeepDevIdMeterDispChangeMap.get(changeIotRTabKeepDevId).getPagerMap();
                    page = changeIotRTabKeepDevIdMeterDispChangeMap.get(changeIotRTabKeepDevId).getPage();
                } else {
                    page = 0;
                }
            } else if (SmsConstants.METER_KIND.HANDY.getVal().equals(meterKind)) {
                if (!( Objects.isNull(changeHandyTabKeepDevIdMeterDispChangeMap) || Objects.isNull(changeHandyTabKeepDevIdMeterDispChangeMap.get(changeHandyTabKeepDevId)))) {
                    // Mapから接続先とメーター表示切替を取得
                    pagerMap = changeHandyTabKeepDevIdMeterDispChangeMap.get(changeHandyTabKeepDevId).getPagerMap();
                    page = changeHandyTabKeepDevIdMeterDispChangeMap.get(changeHandyTabKeepDevId).getPage();
                } else {
                    page = 0;
                }
            } else if (SmsConstants.METER_KIND.OCR.getVal().equals(meterKind)) {
                if (!( Objects.isNull(changeOcrTabKeepDevIdMeterDispChangeMap) || Objects.isNull(changeOcrTabKeepDevIdMeterDispChangeMap.get(changeOcrTabKeepDevId)))) {
                    // Mapから接続先とメーター表示切替を取得
                    pagerMap = changeOcrTabKeepDevIdMeterDispChangeMap.get(changeOcrTabKeepDevId).getPagerMap();
                    page = changeOcrTabKeepDevIdMeterDispChangeMap.get(changeOcrTabKeepDevId).getPage();
                } else {
                    page = 0;
                }
            }

            // 保持している接続先がある場合に設定
            if ((SmsConstants.METER_KIND.SMART.getVal().equals(meterKind) || SmsConstants.METER_KIND.PULSE.getVal().equals(meterKind))
                            && !CheckUtility.isNullOrEmpty(changeSmartPulseTabKeepDevId)) {
                // 切替先がタブ「スマートメーター」かタブ「パルスメーター」の場合
                tmpDevId = changeSmartPulseTabKeepDevId;

            } else if (SmsConstants.METER_KIND.IOTR.getVal().equals(meterKind) && !CheckUtility.isNullOrEmpty(changeIotRTabKeepDevId)) {
                // 切替先がタブ「IoT-R連携用メーター」の場合
                tmpDevId = changeIotRTabKeepDevId;

            } else if (SmsConstants.METER_KIND.HANDY.getVal().equals(meterKind) && !CheckUtility.isNullOrEmpty(changeHandyTabKeepDevId)) {
                // 切替先がタブ「ハンディ検針用メーター」の場合
                tmpDevId = changeHandyTabKeepDevId;

            } else if (SmsConstants.METER_KIND.OCR.getVal().equals(meterKind) && !CheckUtility.isNullOrEmpty(changeOcrTabKeepDevId)) {
                // 「OCR検針」→「AieLink」へ変更
                // 切替先がタブ「AieLink用メーター」の場合
                tmpDevId = changeOcrTabKeepDevId;
            }

            smartProperty.setDevId(tmpDevId);
            smartProperty.setDevName(getKey(devIdMap, tmpDevId));
            pulseProperty.setDevId(tmpDevId);
            pulseProperty.setDevName(getKey(devIdMap, tmpDevId));
            iotRProperty.setDevId(tmpDevId);
            iotRProperty.setDevName(getKey(devIdMap, tmpDevId));
            handyProperty.setDevId(tmpDevId);
            handyProperty.setDevName(getKey(devIdMap, tmpDevId));
            ocrProperty.setDevId(tmpDevId);
            ocrProperty.setDevName(getKey(devIdMap, tmpDevId));

            // 検満通知月
            updateExamNoticeMonth();
        }

        // 負荷制限
        loadlimitMap = genericTypeList.getSmsLoadLimit();

        // 負荷電流
        curlimitMap = genericTypeList.getSmsLoadCurrent();

        // 開閉器動作カウント
        occountMap = new LinkedHashMap<>();
        for (int i = MeterManagementConstants.MIN_OCCOUNT; i <= MeterManagementConstants.MAX_OCCOUNT; i++) {
            occountMap.put(Integer.toString(i), Integer.toString(i));
        }

        // 開閉区分
        ocstateMap = genericTypeList.getSmsOpenMode();

        // 開閉器カウントクリア
        cntclrMap = new LinkedHashMap<>();
        for (int i = MeterManagementConstants.MIN_CNTCLR; i <= MeterManagementConstants.MAX_CNTCLR; i++) {
            String val = String.format("%02d", i);
            cntclrMap.put(val, val);
        }

        // 更新区分
        updateKbn = genericTypeList.getUpdateKbn();

        // メーター種別
        metertypeMap = getMeterTypeAll();

        // 検満年月_年種別
        dispYearMap = genericTypeList.getSmsDispYear();

        // 検満年月_元号
        gengoMap = genericTypeList.getSmsGengo();

        // 検満通知
        examnoticeMap = genericTypeList.getSmsExamNotic();

        // メーター状況
        meterPresSituMap = genericTypeList.getSmsMeterPresSitu();

        // パルス種別
        pulseTypeMap = genericTypeList.getSmsPulseType();

        // ハンディ種別
        handyTypeMap = genericTypeList.getSmsHandyType();

        if (devList != null && devList.size() > 0) {
            // 一覧取得
            searchMeterInfo();
        }

        // 一括登録の実行ボタン押下時の確認ダイアログのメッセージを設定
        setConfirmMsg();

        // 入力項目をリセットする
        inputReset();

        prevMeterKind = meterKind;

        // 現在値取得のメーター管理番号のプルダウンの設定を初期化
        nowValMeterMngIdSmart = 0L;

        checkBatchRegistrationEnabled();

        return init();
    }

    /**
     * メッセージエリアのメッセージを初期化
     */
    public void clearFacesMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        // すべての FacesMessageを破棄
        for (Iterator<FacesMessage> it = ctx.getMessages(); it.hasNext();) {
            it.next();
            it.remove();
        }
    }

    /**
     * 一括登録ボタンの活性/非活性を判定する
     * 大崎権限（corp_id が osaki または osakitest00）の場合は活性化
     * それ以外で m_corp_function_use に function_cd='610' が存在する場合は非活性化
     */
    private void checkBatchRegistrationEnabled() {
        String corpId = getLoginCorpId();
        
        if ("osaki".equals(corpId) || "osakitest00".equals(corpId)) {
            batchRegistEnabled = true;
            return;
        }
        
        MCorpFunctionUse functionUse = MCorpFunctionUseDao.find("610", corpId);
        if (functionUse != null) {
            batchRegistEnabled = false;
        } else {
            batchRegistEnabled = true;
        }
    }

    /**
     * 「表示更新」ボタン押下時の処理
     * メーター情報検索
     * @param conditionList
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public String searchMeterInfo() throws IllegalAccessException, InvocationTargetException {

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:searchMeterInfo():START"));
        this.dispCnt = 0;

        // メーター情報検索
        List<MeterInfo> meterList = executeSearch(true);
        if (meterList != null) {
            this.meterList = meterList;
            // 取得と同時に表示件数をここに入れて保持する
            this.dispCnt = meterList.size();
        }

        // 登録可能メーター管理番号をセットする
        meterMngIdMap = getNotRegisteredMeterMngId();

        // 共通でもってる装置IDと装置名も更新
        devId = smartProperty.getDevId();
        devName = smartProperty.getDevName();

        keepDevId = smartProperty.getDevId();

        // アップロードファイル名を初期化
        uploadFileName = "";

        // LTE-M判定
        if (devId != null && devId.startsWith(MeterManagementConstants.LTE_M_DEVICE_PREFIX)
                && Objects.equals(meterKind, METER_KIND.SMART.getVal())) {
            lteMDeviceFlg = true;
        } else {
            lteMDeviceFlg = false;
        }
        // LTE-Mの場合、設定内容変更画面の初期値を設定
        if (lteMDeviceFlg) {
            initSmartSettingUpdateProperty();
            smartSettingUpdateProperty.setLoadlimitMode(null);
            smartSettingUpdateProperty.setLoadCurrent(null);
            smartSettingUpdateProperty.setTempLoadCurrent(null);
            smartSettingUpdateProperty.setAutoInjection(null);
            smartSettingUpdateProperty.setTempAutoInjection(null);
            smartSettingUpdateProperty.setBreakerActCount(null);
            smartSettingUpdateProperty.setTempBreakerActCount(null);
            smartSettingUpdateProperty.setCountClear(null);
            smartSettingUpdateProperty.setTempCountClear(null);
            smartSettingUpdateProperty.setOpenMode(null);
        }

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:searchMeterInfo():END"));
        return "";
    }

    /**
     * メーター情報検索.
     */
    public List<MeterInfo> executeSearch(boolean isNarrowDownPage) throws IllegalAccessException, InvocationTargetException {
        return executeSearch(isNarrowDownPage, null);
    }

    /**
     * メーター情報検索.
     * クラス変数(I) buildingInfo 建物・テナント情報 (企業ID, 建物ID など)
     * クラス変数(I) smartProperty スマメ画面共通のプロパティ (装置ID など)
     * クラス変数(I) meterKind メーター種類
     * クラス変数(I) page メーター表示切替
     * クラス変数(I) pulseTypeMap パルス種別Map
     * クラス変数(I) iotRTypeMap IoT-R種別Map
     * クラス変数(I) ocstateMap 開閉区分プルダウンMap
     * クラス変数(O) pagerMap メーター表示切替プルダウンの選択肢Map
     * @param isNarrowDownPage メーター表示切替絞り込みフラグ  true:メーター表示切替プルダウンで選択された情報に制限する
     * @param devId 現在表示している一覧の装置ID
     * @return メーター情報検索結果  null:検索エラー
     * @throws InvocationTargetException BeanUtils.copyProperties(～, ～) エラー
     * @throws IllegalAccessException BeanUtils.copyProperties(～, ～) エラー
     */
    public List<MeterInfo> executeSearch(boolean isNarrowDownPage, String devId) throws IllegalAccessException, InvocationTargetException {
        // ページ初期化
        List<MeterInfo> meterList = null;

        initInvalidComponent();
        // 更新が必要な画面項目の値を更新
        dispUpdate();

        // 検索
        ListSmsMeterResponse response = new ListSmsMeterResponse();
        ListSmsMeterParameter parameter = new ListSmsMeterParameter();

        // APIBeanの指定
        parameter.setBean("ListSmsMeterSearchBean");
        parameter.setCorpId(buildingInfo.getCorpId());  // 企業ID
        parameter.setBuildingId(Long.valueOf(buildingInfo.getBuildingId())); // 建物ID
        parameter.setDevId(devId == null ? smartProperty.getDevId() : devId);   // 装置ID
        // メーター種類を選択タブにあわせて動的に変更する
        parameter.setMeterKind(meterKind);              // メーター種類

        // DBから値取得
        response = callApiPost(parameter, ListSmsMeterResponse.class);

        //ページ初期化
        pagerMap = new LinkedHashMap<>();

        // 画面表示用にリストの詰め替え
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {

            List<ListSmsMeterResultData> orglist = response.getResult().getList();

            // メーター表示切替（ページャー）をセットする
            createPagerMapFromResult(orglist); // pagerMap にセットする

            if (isNarrowDownPage) {
                // データを表示件数分に調整
                adjustmentMeterList(orglist);
            }

            meterList = new ArrayList<>();
            // DBから取得できた件数分ループして詰め替えていく
            for (ListSmsMeterResultData getData : orglist) {
                MeterInfo info = new MeterInfo();
                // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
                ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
                ConvertUtils.register(new DateConverter(null), Date.class);
                ConvertUtils.register(new LongConverter(null), Long.class);
                // 内容をコピー
                BeanUtils.copyProperties(info, getData);
                // 回避するやつを解除
                ConvertUtils.deregister();

                // 個別でコピーする必要があるものを処理
                info.setConcentratorName(getData.getName());

                // 検満
                String tmpExamEndYm = getData.getExamEndYm();
                if (tmpExamEndYm != null && !tmpExamEndYm.isEmpty()) {
                    // 西暦を整形して(yyyy.MM）セット(MMはゼロパディング)
                    info.setExamEndYm(tmpExamEndYm.substring(0, 4) + "." + tmpExamEndYm.substring(4, 6));
                    // 和暦を整形して(Gyy.MM)セット(MMはゼロパディング)
                    String warei[] = convertToJpn(tmpExamEndYm);
                    info.setExamEndYmWareki(warei[0] + warei[1] + "." + warei[2]);
                }

                // パルス種別名称
                info.setPulseTypeName(getKey(pulseTypeMap, getData.getPulseType()));

                // 開閉区分を表示用にセット
                if (ocstateMap.values().contains(getData.getOpenMode())) {
                    // 0か1の場合
                    info.setOpenModeDisp(getKey(ocstateMap, getData.getOpenMode()));
                } else {
                    // 0または1以外の場合
                    info.setOpenModeDisp("-");
                }

                // 操作状態
                // commandFlgとsrvEntから算出
                String cf = MeterManagementConstants.COMMAND_FLG_DISP_MAP.get(getData.getCommandFlg());
                String se = MeterManagementConstants.SRV_ENT_DISP_MAP.get(getData.getSrvEnt());
                info.setOperationStatus((cf == null ? "" : cf) + (se == null ? "" : se));
                info.setSrvEnt(getData.getSrvEnt());

                // 無線種別
                info.setHandyType(genericTypeList.getSmsHandyTypeByName(getData.getWirelessType()));

                //メーター状態項目
                if (METER_KIND.SMART.getVal().equals(meterKind) || METER_KIND.PULSE.getVal().equals(meterKind) || METER_KIND.IOTR.getVal().equals(meterKind)) {
                    // NULL落ち回避
                    if (getData.getMeterPresentSituation() == null) {
                        getData.setMeterPresentSituation(new BigDecimal(0));
                    }
                    if (getData.getAlertPauseFlg() == null) {
                        getData.setAlertPauseFlg(new BigDecimal(0));
                    }

                    // メーター状況を表示用にセット
                    if (meterPresSituMap.values().contains(getData.getMeterPresentSituation().toString())) {
                        info.setMeterPresentSituationDisp(
                                getKey(meterPresSituMap, getData.getMeterPresentSituation().toString()));
                    }

                    //アラート停止期間
                    String aps = getData.getAlertPauseStart();
                    if (aps != null && !aps.isEmpty()) {
                        info.setAlertPauseStart(aps.substring(0, 4) + "/" + aps.substring(4, 6)
                                + "/" + aps.substring(6, 8));
                    }

                    String ape = getData.getAlertPauseEnd();
                    if (ape != null && !ape.isEmpty()) {
                        info.setAlertPauseEnd(ape.substring(0, 4) + "/" + ape.substring(4, 6)
                                + "/" + ape.substring(6, 8));
                    }

                    //アラート停止状態を表示用にセット
                    String ap = MeterManagementConstants.ALERT_PAUSE_DISP_MAP
                            .get(getData.getAlertPauseFlg().intValue());
                    info.setAlertPauseDisp(ap == null ? "" : ap);
                }

                // 現在値を取得
//                NowValueProperty nowVal = getNowValue(info.getMeterMngId());
//                info.setNowVal(nowVal);

                info.setCurrentDataHidden(info.getCurrentData());
                info.setPulseTypeHidden(info.getPulseType());
                info.setPulseWeightHidden(info.getPulseWeight());
                if (info.getPulseWeight() == null) {
                    info.setPulseWeightCal(null);
                } else {
                    info.setPulseWeightCal(info.getPulseWeight().divide(BigDecimal.valueOf(100)));
                }
                // 詰め
                meterList.add(info);
            }
        } else {
            addErrorMessage(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        }

        return meterList;
    }

    /**
     * 全件取得データを
     * ページ単位の件数に変更する
     *
     * @param orglist
     * @return
     */
    private List<ListSmsMeterResultData> adjustmentMeterList(List<ListSmsMeterResultData> orglist) {
        if (!CollectionUtils.isEmpty(orglist)) {
            List<ListSmsMeterResultData> list = new ArrayList<ListSmsMeterResultData>();
            for (int i = page; i < (page + SmsConstants.METER_PER_PAGE); i++) {
                if (i < orglist.size()) {
                    list.add(orglist.get(i));
                } else {
                    break;
                }
            }
            orglist.clear();
            orglist.addAll(list);
        }
        return orglist;
    }

    /**
     * 現在値の取得処理
     * @param paramMeterMngId
     * @return 現在値
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private NowValueProperty getNowValue(Long paramMeterMngId)
            throws IllegalAccessException, InvocationTargetException {

        // 検索
        GetSmsMeterNowValResponse response = new GetSmsMeterNowValResponse();
        GetSmsMeterNowValParameter parameter = new GetSmsMeterNowValParameter();

        // APIBeanの指定
        parameter.setBean("GetSmsMeterNowValBean");
        parameter.setMeterMngId(paramMeterMngId);
        parameter.setDevId(smartProperty.getDevId());

        // DBから値取得
        response = callApiPost(parameter, GetSmsMeterNowValResponse.class);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            NowValueProperty ret = new NowValueProperty();
            // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
            ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
            ConvertUtils.register(new DateConverter(null), Date.class);
            // 内容をコピー
            BeanUtils.copyProperties(ret, response.getResult());
            // 回避するやつを解除
            ConvertUtils.deregister();
            return ret;
        }

        return null;
    }

    /**
     * 一括登録
     */
    @Logged
    public void execLumpRegist()  throws IllegalAccessException, InvocationTargetException , ParseException {
        System.out.println("------------------------- execLumpRegist Call -------------------------");
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execLumpRegist():START"));

        List<String> errorList = new ArrayList<>();
        this.bulkErrorList.clear();

        //CSVファイル未登録
        if (this.fileUploadErrorList.isEmpty() && (uploadFile == null || CheckUtility.isNullOrEmpty(uploadFileName))) {
            fileUploadErrorList.add(beanMessages.getMessage("energyInputYearSearchBean.error.notInputFile"));
        }

        //CSV内容無し
        if (this.uploadFile != null && this.uploadCsvContent.isEmpty()) {
            fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.recodeNotFound"));
        }

        //エラー内容を追加する
        if (!this.fileUploadErrorList.isEmpty()) {
            for (String error : this.fileUploadErrorList) {
                addErrorMessage(error);
            }
            uploadFileName = null;
            uploadFile = null;
            return;
        }

        // メーター情報取得
        List<MeterInfo> meterList = executeSearch(false, devId);

        meterIdList = new ArrayList<>();
        updateRowNum = 0;
        insertRowNum = 0;
        List<List<String>> targetUpdateRecordList = new ArrayList<>();
        List<List<String>> targetInsertRecordList = new ArrayList<>();

        for (List<String> recordList : uploadCsvContent) {
            // スマートメーターの場合
            if (METER_KIND.SMART.getVal().equals(meterKind) && recordList.size() != MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.values().length) {
                fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.csvFormatWrong"));
            }

            // パルスメーターの場合
            if (METER_KIND.PULSE.getVal().equals(meterKind) && recordList.size() != MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.values().length) {
                fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.csvFormatWrong"));
            }

            // Iot-R連携用メーターの場合
            if (METER_KIND.IOTR.getVal().equals(meterKind) && recordList.size() != MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.values().length) {
                fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.csvFormatWrong"));
            }

            // ハンディ検針用メーターの場合
            if (METER_KIND.HANDY.getVal().equals(meterKind) && recordList.size() != MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.values().length) {
                fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.csvFormatWrong"));
            }

            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーターの場合
            if (METER_KIND.OCR.getVal().equals(meterKind) && recordList.size() != MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.values().length) {
                fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.csvFormatWrong"));
            }

            //エラー内容を追加する
            if (!this.fileUploadErrorList.isEmpty()) {
                for (String error : this.fileUploadErrorList) {
                    addErrorMessage(error);
                }
                uploadFileName = null;
                uploadFile = null;
                return;
            }

            // 更新区分が「更新なし」以外の場合
            String errorStr = null;
            if (!genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()).equals(recordList.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()))) {

                // 必須チェック、値チェック
                errorStr = bulkValidation(recordList);
                if (!CheckUtility.isNullOrEmpty(errorStr)) {
                    errorList.add(errorStr);
                }

                // 相関チェック
                errorStr = complexValidation(recordList, meterList, devId, uploadCsvContent);
                if (!CheckUtility.isNullOrEmpty(errorStr)) {
                    errorList.add(errorStr);
                }

                // 更新区分が「新規登録」の場合
                if (genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()).equals(recordList.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()))) {
                    // メータID(計器ID)重複チェック
                    errorStr = meterIdDuplicateCheck(recordList);
                    if (!CheckUtility.isNullOrEmpty(errorStr)) {
                        // エラーの場合
                        errorList.add(errorStr);
                    } else {
                        // エラーではない場合
                        insertRowNum++;
                        targetInsertRecordList.add(recordList);
                        continue;
                    }
                }

                if (errorList.isEmpty()) {
                    updateRowNum++;
                    targetUpdateRecordList.add(recordList);
                    continue;
                }
            }
        }

        // 新規登録の対象行のメーター管理番号を取得
        String insertMeterMngIdCsv = targetInsertRecordList.stream()
                .map(recordList -> recordList.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex()))
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .collect(Collectors.joining(","));

        // 更新の対象行のメーター管理番号を取得
        String updateMeterMngIdCsv = targetUpdateRecordList.stream()
                .map(recordList -> recordList.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex()))
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .collect(Collectors.joining(","));

        boolean apiResult = false; // API実行結果
        if (errorList.isEmpty() && (updateRowNum != 0 || insertRowNum != 0)) {

            // 実行ログ出力
            if (updateRowNum == 0 && insertRowNum == 0) {
                logAndNotifyInfo("【一括(新規登録・更新)対象件数】 新規登録0件 (管理番号:-) , 更新0件 (管理番号:-)");
            } else if (insertRowNum == 0) {
                logAndNotifyInfo("【一括(新規登録・更新)対象件数】 新規登録0件 (管理番号:-) , 更新%d件 (管理番号:%s)", updateRowNum, updateMeterMngIdCsv);
            } else if (updateRowNum == 0) {
                logAndNotifyInfo("【一括(新規登録・更新)対象件数】 新規登録%d件 (管理番号:%s) , 更新0件 (管理番号:-)", insertRowNum, insertMeterMngIdCsv);
            } else {
                logAndNotifyInfo("【一括(新規登録・更新)対象件数】 新規登録%d件 (管理番号:%s) , 更新%d件 (管理番号:%s)", insertRowNum, insertMeterMngIdCsv, updateRowNum, updateMeterMngIdCsv);
            }

            UpdateBulkSmsMeterParameter parameter = createParameter(uploadCsvContent);
            parameter.setBean("UpdateBulkSmsMeterBean");

            if (lteMDeviceFlg) {
                // CSV取得情報を更新対象リストに変換
                List<MeterManagementInputProperty> meterManagementInputPropertyList = convertToTargets(targetUpdateRecordList);
                // API同時実行数取得
                final Semaphore semaphore = new Semaphore(MeterManagementConstants.MAX_CONCURRENCY_LUMP_REGIST_API, true); // true:順番待ちが公正になり、先に来たリクエストが先に通るように設定
                // メーター一覧と更新対象リストを比較して一致した場合にAPI実行  CSV取得項目の内「開閉区分」から「開閉器カウントクリア(臨時)」まではAPI実行
                apiResult = execLumpRegistPerRow(meterManagementInputPropertyList, meterList, semaphore);

                // 上記項目以外は既存の更新処理実行  API側にLTE-M一括登録であることを知らせるためにフラグを設定
                parameter.setLteMLumpRegistExecFlg(true); // LTE-M一括登録実行フラグtrue
            }
            parameter.setUpdatePattern(METER_UPDATE_PATTERN.ALL.getVal());
            parameter.setFromDeviceCtrl(!this.isSendLumpFlg());
            parameter.setMeterKind(meterKind);

            UpdateBulkSmsMeterResponse updateResponse = new UpdateBulkSmsMeterResponse();
            SmsApiGateway gateway = new SmsApiGateway();
            updateResponse = (UpdateBulkSmsMeterResponse) gateway.osolApiPost(
                    osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                    SmsApiGateway.PATH.JSON,
                    parameter,
                    updateResponse);
            if (!OsolApiResultCode.API_OK.equals(updateResponse.getResultCode())) {
                addErrorMessage(
                        beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(updateResponse.getResultCode())));
                uploadFileName = null;
                uploadFile = null;
                return;
            }
            errorList.addAll(updateResponse.getErrorList());
        }

        if (!errorList.isEmpty()) {
            this.bulkErrorList.addAll(errorList);
            /*for (String error : errorList) {
                addErrorMessage(error);
            }*/
            errorList.clear();
            return;
        }

        // 登録・更新対象行が存在しない場合
        if(updateRowNum == 0 && insertRowNum == 0) {
            fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.recodeNotFound"));
        }

        //エラー内容を追加する
        if (!this.fileUploadErrorList.isEmpty()) {
            for (String error : this.fileUploadErrorList) {
                addErrorMessage(error);
            }
            uploadFileName = null;
            uploadFile = null;
            return;
        }

        // API実行結果が成功の場合
        if (apiResult) {
            // サーバ保存ファイル名変更
            renameUploadFile();

            reload();

            // メーター切替のMap更新
            createPagerMap(getAllMeterMngId());
        }

        if (lteMDeviceFlg) {
            // TODO 中村 プロパティファイルより取得する
            if (updateRowNum == 0 && insertRowNum == 0) {
                // 何も出力しない
            } else if (insertRowNum == 0) {
                addMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.LteMLumpRegistNonApiSuccessMessage"));
            } else if (updateRowNum == 0) {
                addMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.LteMLumpRegistInsertSuccessMessage"));
            } else {
                addMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.LteMLumpRegistInsertAndNonApiSuccessMessage"));
            }
        } else {
            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        }

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execLumpRegist():END"));
    }

    /**
     * 対象オブジェクトから文字列生成（型差/空白差を吸収）
     *
     * @param obj 対象オブジェクト
     * @return 対象オブジェクトの変換後文字列
     */
    private static String trim(Object obj) {
        return obj == null ? "" : String.valueOf(obj).trim();
    }

    /**
     * メーター管理番号とメーターIDをセットで生成
     *
     * @param meterMngId メーター管理番号
     * @param meterId メーターID
     * @return 生成された文字列
     */
    private static String key(Object meterMngId, Object meterId) {
        return trim(meterMngId) + "|" + trim(meterId);
    }

    /**
     * LTE-M一括登録 CSVファイルより取得して非同期処理でAPI実行
     *
     * @param meterManagementInputPropertyList 更新対象リスト
     * @param meterList メーター一覧リスト
     * @param semaphore API同時実行数
     * @return 実行結果(true:全てのAPI実行成功の場合 false:API実行が一つでも失敗した場合)
     */
    private boolean execLumpRegistPerRow(
            List<MeterManagementInputProperty> meterManagementInputPropertyList,
            List<MeterInfo> meterList, Semaphore semaphore) {

        if (meterManagementInputPropertyList == null || meterManagementInputPropertyList.isEmpty()) {
            logAndNotifyError(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.LteMLumpRegistNoTargetRecord"));
            return true;
        }
        logAndNotifyInfo(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.LteMLumpRegistExecApiMessage"));

        Map<String, MeterInfo> meterIndex = meterList.stream()
            .collect(Collectors.toMap(
                meterInfo -> key(meterInfo.getMeterMngId(), meterInfo.getMeterId()),
                Function.identity(),
                (existingValue, duplicateValue) -> existingValue, // 既存を優先
                LinkedHashMap::new
            ));

        final int total = meterManagementInputPropertyList.size();
        final Set<Long> successIds = ConcurrentHashMap.newKeySet();
        final List<CompletableFuture<Boolean>> futures = new ArrayList<>(total);

        for (MeterManagementInputProperty property : meterManagementInputPropertyList) {
            MeterInfo meterInfo = meterIndex.get(key(property.getMeterMngId(), property.getMeterId()));
            if (meterInfo == null) {
                logAndNotifyError("API実行結果 スキップ: メーター一覧に存在しない (管理番号:%s, メーターID:%s)",
                        trim(property.getMeterMngId()), trim(property.getMeterId()));
                continue;
            }

            // 開閉制御APIと負荷制限APIを実行
            final String loginPersonId = getLoginPersonId();
            final String requestId = UUID.randomUUID().toString(); // リクエストの判別のためIDを採番
            final MeterManagementInputProperty newProperty = copyFor(property, true, true);
            final MeterInfo mi = meterInfo;

            // 非同期実行
            CompletableFuture<List<LteMMeterExecResult>> switchAndLoadlimitApi =
                    CompletableFuture.supplyAsync(() -> {
                        boolean acquired = false;
                        try {
                            acquired = semaphore.tryAcquire(
                                MeterManagementConstants.SEMAPHORE_TIMEOUT_LUMP_REGIST_API,
                                TimeUnit.SECONDS
                            );
                            if (!acquired) {
                                String msg = String.format("同時実行上限により待機超過: 管理番号:%s, メーターID:%s",
                                        trim(mi.getMeterMngId()), trim(mi.getMeterId()));
                                System.out.println(msg);
                                eventLogger.warn(msg);
                                asyncUiErrors.add(msg);
                                return semaphoreTimeoutFail("Semaphore timeout");
                            }

                            return lteMParallelExecutor.executeAsyncForMeters(
                                Collections.singletonList(mi),
                                newProperty,
                                MeterManagementConstants.LTEM_API_TYPE.SET.getCode(),
                                requestId,
                                loginPersonId,
                                mMeterInfoDao
                            );

                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return semaphoreTimeoutFail("Interrupted while waiting semaphore");

                        } finally {
                            if (acquired) {
                                semaphore.release();
                            }
                        }
                    }, lteMParallelExecutor.getExecutor());

            // List<LteMMeterExecResult>をBooleanへ畳み込み（全部成功なら true）
            final MeterInfo info = meterInfo;
            CompletableFuture<Boolean> allOk =
                switchAndLoadlimitApi
                    .thenApply(results -> {
                        boolean ok = results != null
                                && !results.isEmpty()
                                && results.stream().allMatch(r -> r != null && r.isSuccess());
                        if (ok) {
                            Long mngId = info.getMeterMngId();
                            if (mngId != null) {
                                successIds.add(mngId);
                            }
                        }
                        return ok;
                    })
                    .exceptionally(ex -> {
                        String msg = String.format("一括API実行で例外: %s",
                                ex.getCause() != null ? ex.getCause().getMessage() : ex.toString());
                        System.out.println(msg);
                        eventLogger.error(msg);
                        asyncUiErrors.add(msg);
                        return false; // 例外は失敗扱い
                    });
            futures.add(allOk);
        }

        // 全行の完了待ち
        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        all.join();

        // 画面にエラー内容を出力する
        for (String m : asyncUiErrors) {
            addErrorMessage(m);
        }
        asyncUiErrors.clear();

        int executed = (int) futures.stream().filter(f -> Boolean.TRUE.equals(f.join())).count();
        String okIdsStr = successIds.isEmpty()
                ? "-"
                : successIds.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

        logAndNotifyInfo("API実行結果サマリ: API実行件数:%d件, API成功:%d件 (管理番号:%s)", total, executed, okIdsStr);
        // API実行件数と成功件数が異なる場合
        if (total != executed) {
            logAndNotifyError(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.LteMLumpRegistApiCountAndSuccessCountDifferentMessage"));
        }

        return executed == total;
    }

    /**
     * セマフォタイムアウト時失敗メッセージを格納してリスト返却
     * @param msg 失敗メッセージ
     * @return 失敗メッセージを格納したリスト
     */
    private static List<LteMMeterExecResult> semaphoreTimeoutFail(String msg) {
        LteMMeterExecResult result = new LteMMeterExecResult();
        result.fail(msg);
        return Collections.singletonList(result);
    }

    /**
     * 入力オブジェクトのディープコピー
     *
     * @param src コピー前オブジェクト
     * @param sendSwitch 開閉制御API実行フラグ
     * @param sendLoadlimit 負荷制限API実行フラグ
     * @return コピー後オブジェクト
     */
    private static MeterManagementInputProperty copyFor(MeterManagementInputProperty src, boolean sendSwitch, boolean sendLoadlimit) {
        MeterManagementInputProperty dst = new MeterManagementInputProperty(src);
        dst.setSendFlgSwitch(sendSwitch);
        dst.setSendFlgLoadlimit(sendLoadlimit);
        return dst;
    }

    /**
     * CSV取得情報をMeterManagementInputPropertyリストに変換
     *
     * @param csvList CSV取得情報
     * @return 更新対象リスト
     */
    public List<MeterManagementInputProperty> convertToTargets(List<List<String>> csvList) {
        List<MeterManagementInputProperty> targets = new ArrayList<>();

        for (List<String> record : csvList) {
            MeterManagementInputProperty meterManagementInputProperty = new MeterManagementInputProperty();

            // 管理番号
            meterManagementInputProperty.setMeterMngId(Long.valueOf(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex())));
            // 計器ID
            meterManagementInputProperty.setMeterId(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()));
            // 開閉区分
            meterManagementInputProperty.setOpenMode(ocstateMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex())));
            // 負荷制限
            if (LOAD_LIMIT.INVALID.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                meterManagementInputProperty.setLoadlimitMode(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal());
            } else if (LOAD_LIMIT.VALID.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                meterManagementInputProperty.setLoadlimitMode(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal());
            } else {
                meterManagementInputProperty.setLoadlimitMode(loadlimitMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex())));
            }
            // 負荷電流(基本)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))) {
                meterManagementInputProperty.setLoadCurrent(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()));
            }
            // 自動投入(基本)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))) {
                meterManagementInputProperty.setAutoInjection(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()));
            }
            // 開閉器動作カウント(基本)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))) {
                meterManagementInputProperty.setBreakerActCount(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()));
            }
            // 開閉器カウントクリア(基本)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))) {
                meterManagementInputProperty.setCountClear(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()));
            }
            // 負荷電流(臨時)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))) {
                meterManagementInputProperty.setTempLoadCurrent(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()));
            }
            // 自動投入(臨時)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))) {
                meterManagementInputProperty.setTempAutoInjection(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()));
            }
            // 開閉器動作カウント(臨時)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))) {
                meterManagementInputProperty.setTempBreakerActCount(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()));
            }
            // 開閉器カウントクリア(臨時)
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))) {
                meterManagementInputProperty.setTempCountClear(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()));
            }
            targets.add(meterManagementInputProperty);
        }
        return targets;
    }

    /**
     * 一括登録(AieLink用メーター用)
     * 「OCR検針」→「AieLink」へ変更
     */
    @Logged
    public void execGetApiOCR()  throws IllegalAccessException, InvocationTargetException  {
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():START"));

        List<String> errorList = new ArrayList<>();
        this.bulkErrorList.clear();

        if (this.devId == null
                || !this.devId.startsWith(SmsConstants.DEVICE_KIND.OCR.getVal())
                || !StringUtils.isNumeric(this.devId.substring(2))  ) {
            // ＯＣＲ検針の接続先(書式:"OC999999")を選択してください。
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.ocr.devId"));

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END error"));
            return;
        }

        // 施設・エリアID  ※SMSの 装置ID から先頭の"OC"を除外したコード
        String facilityId = this.devId.substring(2);

        OcrReadingApi ocrReadingApi = new OcrReadingApi(eventLogger, errorLogger);

        // COR検針サーバーからトークン取得

        OcrServerGetTokenResDto tokenResDto = ocrReadingApi.getToken();

        if (tokenResDto == null || tokenResDto.getHttpResponseCode() != HttpServletResponse.SC_OK
                || !OcrReadingApi.RESPONS_CODE_SUCCESS.equals(tokenResDto.getCode()) || tokenResDto.getData() == null) {
            // トークン取得失敗
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.ocr.token.failure"));

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END error"));
            return;
        }


        // COR検針サーバーからメーター情報リスト取得

        OcrServerGetMeterListResDto meterListResDto = ocrReadingApi.getMeterList(
                facilityId, null, null, tokenResDto.getData().getAccessToken());

        if (meterListResDto == null || meterListResDto.getHttpResponseCode() != HttpServletResponse.SC_OK
                || !OcrReadingApi.RESPONS_CODE_SUCCESS.equals(meterListResDto.getCode()) || meterListResDto.getData() == null) {
            // TOKEN取得失敗
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.ocr.dataList.failure"));

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END error"));
            return;
        }

        // 取得結果が０件
        if (meterListResDto.getData().isEmpty()) {
            addMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.ocr.noData"));

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END no data"));
            return;
        }

        UpdateBulkSmsMeterParameter parameter = new UpdateBulkSmsMeterParameter();
        UpdateBulkSmsMeterRequest request = new UpdateBulkSmsMeterRequest();
        request.setDevId(this.devId);
        for (OcrServerGetMeterListResDto.Data row : meterListResDto.getData()) {

            UpdateBulkSmsMeterRequestSet requestSet = new UpdateBulkSmsMeterRequestSet();

            // ①メータ管理番号：１から自動採番 ※DAOクラス内で自動採番。未設定では必須エラーになるので0を仮セット
            requestSet.setMeterMngId(0L);
            // ②メータID：APIで取得したmanage_numberに対応 ※計器ID(必須)
            requestSet.setMeterId(row.getManage_number());
            // ③検満年月：APIで取得
            requestSet.setDispYearFlg("0"); // "0":西暦
            requestSet.setExamEndYm(convYyyyMm(row.getExpiration_date())); // 検満年月
            // ④検満通知：検満年月が取得できた場合のみ有効？
            requestSet.setExamNotice(requestSet.getExamEndYm() != null
                    ? MeterManagementConstants.FLG_ON : MeterManagementConstants.FLG_OFF);
            // ⑤乗率：1固定
            requestSet.setMulti(BigDecimal.ONE);
            // ⑥メーターの種別：デフォルト値をセット
            requestSet.setMeterType(null); // ※(新規時)nullにすると、DAOクラスでデフォルト値("1")がセットされる  (更新時)nullにすると更新対象外

            // その他の項目は null または 初期値 を設定

            requestSet.setVersion(null);                // 排他制御用カラム
            requestSet.setTenantId(null);               // ユーザーコード(テナントコード)
            requestSet.setMemo(null);                   // コメント

            // (スマートのみ)
            requestSet.setOpenMode(null);               // 開閉区分
            requestSet.setLoadlimitMode(null);          // 負荷制限
            requestSet.setLoadCurrent(null);            // 負荷電流(基本)
            requestSet.setAutoInjection(null);          // 自動投入(基本)
            requestSet.setBreakerActCount(null);        // 開閉器動作カウント(基本)
            requestSet.setCountClear(null);             // 開閉器カウントクリア(基本)
            requestSet.setTempLoadCurrent(null);        // 負荷電流(臨時)
            requestSet.setTempAutoInjection(null);      // 自動投入(臨時)
            requestSet.setTempBreakerActCount(null);    // 開閉器動作カウント(臨時)
            requestSet.setTempCountClear(null);         // 開閉器カウントクリア(臨時)

            // Validationチェック
            StringJoiner errorSb = new StringJoiner(" ");
            boolean errorFlg = false;
            //計器ID 必須チェック、文字数チェック、開始文字チェック
            if (CheckUtility.isNullOrEmpty(requestSet.getMeterId()) ||
                    requestSet.getMeterId().length() != 10) {
                errorSb.add("計器ID:".concat(requestSet.getMeterId()));
                errorFlg = true;
            }
            if (errorFlg) {
                // チェックエラーあり
                errorList.add(errorSb.toString());
            } else {
                // 正常
                request.getRequestSetList().add(requestSet);
            }
        }

        // Validationエラーがあるか？
        if (!errorList.isEmpty()) {
            this.bulkErrorList.addAll(errorList);

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END error"));
            return;
        }

        parameter.setBean("UpdateBulkSmsMeterBean");
        parameter.setUpdatePattern(METER_UPDATE_PATTERN.ALL.getVal());
        parameter.setFromDeviceCtrl(true);
        parameter.setMeterKind(METER_KIND.OCR.getVal());
        parameter.setRequest(request);
        UpdateBulkSmsMeterResponse updateResponse = new UpdateBulkSmsMeterResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        updateResponse = (UpdateBulkSmsMeterResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                updateResponse);
        if (!OsolApiResultCode.API_OK.equals(updateResponse.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(updateResponse.getResultCode())));
            uploadFileName = null;
            uploadFile = null;
            return;
        }

        errorList.addAll(updateResponse.getErrorList());

        reload();
        addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execGetApiOCR():END"));
    }

    private String bulkValidation(List<String> csvRecord) {

        StringJoiner errorSb = new StringJoiner(System.lineSeparator()); // 結合には改行を使用

        boolean errorFlg = false;

        // CSVファイルより登録予定値を取得するため共通項目のINDEX設定
        // デフォルト（スマートメーター）
        int updateKbnIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex();
        int meterMngIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
        int meterIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        int meterTypeIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_TYPE.getIndex();
        int tenantIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TENANT_ID.getIndex();
        int dispYearFlgIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex();
        int examEndYmIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex();
        int examNoticeIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex();
        int memoIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.MEMO.getIndex();

        if(METER_KIND.PULSE.getVal().equals(meterKind)) {
            // パルスメーター
            updateKbnIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex();
            meterMngIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_ID.getIndex();
            meterTypeIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_TYPE.getIndex();
            tenantIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.TENANT_ID.getIndex();
            dispYearFlgIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex();
            examEndYmIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex();
            examNoticeIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex();
            memoIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MEMO.getIndex();

        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // IoT-R連携用メーター
            updateKbnIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex();
            meterMngIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
            meterTypeIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_TYPE.getIndex();
            tenantIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.TENANT_ID.getIndex();
            dispYearFlgIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex();
            examEndYmIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex();
            examNoticeIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex();
            memoIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.MEMO.getIndex();
        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            // ハンディ検針用メーター
            updateKbnIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex();
            meterMngIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_ID.getIndex();
            meterTypeIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_TYPE.getIndex();
            tenantIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.TENANT_ID.getIndex();
            dispYearFlgIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex();
            examEndYmIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex();
            examNoticeIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex();
            memoIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MEMO.getIndex();
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーター
            updateKbnIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex();
            meterMngIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
            meterTypeIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_TYPE.getIndex();
            tenantIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.TENANT_ID.getIndex();
            dispYearFlgIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex();
            examNoticeIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex();
            memoIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MEMO.getIndex();
        }

        // -------------------------------------------------
        // 共通の項目
        // -------------------------------------------------
        // 更新区分 必須チェック、値チェック
        if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーター または LTE-Mの場合は更新のみ実施
            if (CheckUtility.isNullOrEmpty(csvRecord.get(updateKbnIndex))
                    || !(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()).equals(csvRecord.get(updateKbnIndex))
                            || genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()).equals(csvRecord.get(updateKbnIndex)))) {
                String[] param = { genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal())
                                    ,genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.updateKbnOcr.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(updateKbnIndex))
                        .concat(")"));
                errorFlg = true;
            }
        } else {
            if (CheckUtility.isNullOrEmpty(csvRecord.get(updateKbnIndex))
                    || !updateKbn.containsKey(csvRecord.get(updateKbnIndex))) {
                String[] param = { genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal())
                                    , genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal())
                                    , genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.updateKbn.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(updateKbnIndex))
                        .concat(")"));
                errorFlg = true;
            }
        }

        // 管理番号 必須チェック
        if (CheckUtility.isNullOrEmpty(csvRecord.get(meterMngIdIndex))) {
            errorSb.add("管理番号:--");
            errorFlg = true;
        } else {
            errorSb.add("管理番号:".concat(csvRecord.get(meterMngIdIndex)));
        }

        // 管理番号 範囲チェック
        if (!CheckUtility.checkIntegerRange(csvRecord.get(meterMngIdIndex), MeterManagementConstants.METERM_MNG_ID_INT_MIN, MeterManagementConstants.METERM_MNG_ID_INT_MAX)) {
            String[] param = { String.valueOf(MeterManagementConstants.METERM_MNG_ID_INT_MIN), String.valueOf(MeterManagementConstants.METERM_MNG_ID_INT_MAX) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterMngId.range", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(meterMngIdIndex))
                    .concat(")"));
            errorFlg = true;
        }

        // 計器ID 必須チェック、文字数チェック
        if (CheckUtility.isNullOrEmpty(csvRecord.get(meterIdIndex)) ||
                csvRecord.get(meterIdIndex).length() != MeterManagementConstants.CHECK_METER_ID_CNT) {
            String[] param = { String.valueOf(MeterManagementConstants.CHECK_METER_ID_CNT) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterId.count", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(meterIdIndex))
                    .concat(")"));
            errorFlg = true;
        }

        // 計器ID 開始文字チェックはスマートメーターとパルスメーターのみ実施
        if(METER_KIND.SMART.getVal().equals(meterKind)) {
            if (!csvRecord.get(meterIdIndex).startsWith(meterKind)) {
                String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_A };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern12", parm)
                .concat("(現在入力値: ")
                .concat(csvRecord.get(meterIdIndex))
                .concat(")"));
                errorFlg = true;
            }
        } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
            if (!csvRecord.get(meterIdIndex).startsWith(meterKind)) {
                String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_P };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern12", parm)
                .concat("(現在入力値: ")
                .concat(csvRecord.get(meterIdIndex))
                .concat(")"));
                errorFlg = true;
            }
        }

        // メーター種別 必須チェック、範囲チェック
        if (CheckUtility.isNullOrEmpty(csvRecord.get(meterTypeIndex))
                || !CheckUtility.checkIntegerRange(csvRecord.get(meterTypeIndex), MeterManagementConstants.CHECK_METER_TYPE_MIN, MeterManagementConstants.CHECK_METER_TYPE_MAX)) {
            String[] param = { String.valueOf(MeterManagementConstants.CHECK_METER_TYPE_MIN), String.valueOf(MeterManagementConstants.CHECK_METER_TYPE_MAX) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterType.range", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(meterTypeIndex))
                    .concat(")"));
            errorFlg = true;
        }

        // ユーザーコード 範囲チェック
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(tenantIdIndex)) &&
                !CheckUtility.checkIntegerRange(csvRecord.get(tenantIdIndex), MeterManagementConstants.CHECK_BUILDING_ID_MIN, MeterManagementConstants.CHECK_BUILDING_ID_MAX)) {
            String[] param = { Integer.toString(MeterManagementConstants.CHECK_BUILDING_ID_MIN), Integer.toString(MeterManagementConstants.CHECK_BUILDING_ID_MAX) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.buildingId.range", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(tenantIdIndex))
                    .concat(")"));
            errorFlg = true;
        }

        if(METER_KIND.SMART.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // スマートメーターの項目
            // -------------------------------------------------
            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 機器への設定送信 値チェック LTE-Mの場合は空欄のみ許可
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))) {
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.sendDeviceLteM.value")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // LTE-M以外の場合
                // 機器への設定送信 必須チェック、値チェック
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))
                        || !(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()).equals(SEND_DEVICE.NOT_SEND.getVal())
                                || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()).equals(SEND_DEVICE.SEND.getVal()))) {
                    String[] param = { SEND_DEVICE.NOT_SEND.getVal(), SEND_DEVICE.SEND.getVal() };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.sendDevice.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 開閉区分 値チェック
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                        && !ocstateMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))) {
                    String[] param = { MeterManagementConstants.CHECK_OPEN_MODE_CLOSE, MeterManagementConstants.CHECK_OPEN_MODE_OPEN };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.openModeLteM.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                } else if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                               && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.openModeLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }

                // 開閉区分 新規登録以外で値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時で値が入力されている場合エラー
                    String[] param = { MeterManagementConstants.CHECK_OPEN_MODE_CLOSE, MeterManagementConstants.CHECK_OPEN_MODE_OPEN };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.openModeLteM.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // LTE-M以外の場合
                // 開閉区分 必須チェック、値チェック
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                        || (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                        && !ocstateMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex())))) {
                    String[] param = { MeterManagementConstants.CHECK_OPEN_MODE_CLOSE, MeterManagementConstants.CHECK_OPEN_MODE_OPEN };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.openMode.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            }

            boolean switchCodeBasicOrWithTsFlg = false; // switch_code=1 もしくは 3の場合true
            // メーターの定格電流を基に負荷電流の判定
            // LTE-M かつ 「新規登録以外」の場合
            if (lteMDeviceFlg && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                MMeterInfo mMeterInfo = new MMeterInfo();
                mMeterInfo.setMeterId(csvRecord.get(meterIdIndex));
                List<MMeterInfo> mMeterInfoList = mMeterInfoDao.getMMeterInfoList(mMeterInfo);
                // メータID(計器ID)でのDB取得結果を判定
                if (mMeterInfoList == null || mMeterInfoList.isEmpty()) {
                    errorSb.add(String.format("計器%s は、サーバと計器間の通信が確立されていないため更新できません。", csvRecord.get(meterIdIndex)));
                    errorFlg = true;
                } else if (mMeterInfoList.size() > 1) {
                    errorSb.add(String.format("計器の定格電流の情報が重複しております。 メーター管理番号:%s 計器ID:%s", csvRecord.get(meterMngIdIndex), csvRecord.get(meterIdIndex)));
                    errorFlg = true;
                } else if (mMeterInfoList.size() == 1) {
                    boolean loadlimitErrorFlg = false; // 負荷制限の値にてエラーがあるか判定するフラグ
                    // カラム「switch_code(開閉コード)」で判定
                    BigDecimal sw = mMeterInfoList.get(0).getSwitchCode();
                    if (sw == null) {
                        errorSb.add(String.format("計器の開閉コード情報が存在しないため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                , csvRecord.get(meterMngIdIndex)
                                , csvRecord.get(meterIdIndex)
                                , sw));
                        errorFlg = true;
                    } else if (sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.NONE.getCode())) == 0) {
                        errorSb.add(String.format("計器の開閉器がないため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                , csvRecord.get(meterMngIdIndex)
                                , csvRecord.get(meterIdIndex)
                                , sw));
                        errorFlg = true;
                    } else if ((sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                            || sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                        // 負荷制限の値にてエラーがあるか判定
                        loadlimitErrorFlg = checkLoadlimitError(csvRecord, errorSb);
                        if (loadlimitErrorFlg) {
                            errorSb.add(String.format("更新対象の負荷制限項目にてエラーがあるため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                    , csvRecord.get(meterMngIdIndex)
                                    , csvRecord.get(meterIdIndex)
                                    , sw));
                            errorFlg = loadlimitErrorFlg;
                        }
                        switchCodeBasicOrWithTsFlg = true; // switch_code=1 もしくは 3の場合
                    }

                    // 負荷制限の値にてエラーが無い場合
                    if (!loadlimitErrorFlg) {
                        // 定格電流を取得
                        BigDecimal ratedCurrent = mMeterInfoList.get(0).getRatedCurrent();
                        if (ratedCurrent == null) {
                            errorSb.add(String.format("計器の定格電流が取得できません。 メーター管理番号:%s 計器ID:%s", csvRecord.get(meterMngIdIndex), csvRecord.get(meterIdIndex)));
                            errorFlg = true;
                        }
                        // 負荷制限を取得
                        String loadlimitValue = csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex());
                        if (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())) {
                            // 「有効」の場合
                            loadlimitValue = MeterManagementConstants.LOADLIMIT_MODE_LUMP_REGIST.BASIC.getValue();
                        } else if (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal())
                                || CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                            // 「無効」 または 空欄 の場合
                            loadlimitValue = MeterManagementConstants.LOADLIMIT_MODE_LUMP_REGIST.DISABLED.getValue();
                        }
                        // 定格電流を基に負荷電流の判定実施
                        boolean loadCurrentErrorFlg = false;
                        if (Objects.equals(LOAD_LIMIT_MODE.TEMP_VALID.getVal(), loadlimitMap.get(loadlimitValue))
                                || Objects.equals(LOAD_LIMIT_MODE.TEMP_INVALID.getVal(), loadlimitMap.get(loadlimitValue))) {
                            // 負荷制限「臨時設定」「臨時解除」の場合
                            loadCurrentErrorFlg = isLoadCurrentError(MeterManagementConstants.LOADLIMIT_MODE_DISP.TEMP.getValue()
                                    , csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex())
                                    , ratedCurrent
                                    , csvRecord.get(meterMngIdIndex)
                                    , csvRecord.get(meterIdIndex)
                                    , "s5TmpCurlimitSelectLteM"
                                    , errorSb
                                    , true);
                        } else if (Objects.equals(LOAD_LIMIT_MODE.INVALID.getVal(), loadlimitMap.get(loadlimitValue))
                                || Objects.equals(LOAD_LIMIT_MODE.VALID.getVal(), loadlimitMap.get(loadlimitValue))) {
                            // 負荷制限「なし」「あり」の場合
                            loadCurrentErrorFlg = isLoadCurrentError(MeterManagementConstants.LOADLIMIT_MODE_DISP.BASIC.getValue()
                                    , csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex())
                                    , ratedCurrent
                                    , csvRecord.get(meterMngIdIndex)
                                    , csvRecord.get(meterIdIndex)
                                    , "s5BasicCurlimitSelectLteM"
                                    , errorSb
                                    , true);
                        }
                        if (loadCurrentErrorFlg) {
                            errorFlg = true;
                        }
                    }
                } else {
                    errorSb.add(String.format("メータID(計器ID)を基にDBのテーブル「m_meter_info」を検索しましたが、想定外の取得結果となりました。 メーター管理番号:%s メータID:%s", csvRecord.get(meterMngIdIndex), csvRecord.get(meterIdIndex)));
                    errorFlg = true;
                }
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 負荷制限 値チェック
                // 「無効」「有効」の場合
                if ((csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                        || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                    if (!loadlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                            ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal())
                            : genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal()))) {
                        String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                                , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                        errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitModeLteM.value", param)
                                .concat("(現在入力値: ")
                                .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                                .concat(")"));
                        errorFlg = true;
                    }
                } else {
                    if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                            && !loadlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                        String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                                , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                        errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitModeLteM.value", param)
                                .concat("(現在入力値: ")
                                .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                                .concat(")"));
                        errorFlg = true;
                    }
                }

                // 負荷制限 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.loadlimitModeLteM.insertValue")
                         .concat("(現在入力値: ")
                         .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                         .concat(")"));
                    errorFlg = true;
                }
                // 負荷制限 「開閉コード「switch_code」=1or3」 または 「新規登録」 以外で値が入力されていない場合エラー
                if (!switchCodeBasicOrWithTsFlg
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                            , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                            , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitModeLteM.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // LTE-M以外の場合
                // 負荷制限 必須チェック、値チェック
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                    String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                                        , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                        , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                } else {
                    // 「無効」「有効」の場合
                    if ((csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                            || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                        if (!loadlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                                ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal())
                                : genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal()))) {
                            String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                                    , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.value", param)
                                    .concat("(現在入力値: ")
                                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                                    .concat(")"));
                            errorFlg = true;
                        }
                    } else {
                        if (!loadlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                            String[] param = { LOAD_LIMIT.INVALID.getVal(), LOAD_LIMIT.VALID.getVal()
                                    , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    , genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) };
                            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.value", param)
                                    .concat("(現在入力値: ")
                                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                                    .concat(")"));
                            errorFlg = true;
                        }
                    }
                }
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 負荷電流(基本) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.loadCurrentLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 負荷電流(基本) 新規登録以外で負荷制限が「有効」「無効」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                                || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                    String[] param = { MeterManagementConstants.CHECK_LOAD_CURRENT };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadCurrentLteM.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 負荷電流(基本) 必須チェック 負荷制限が「有効」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))) {
                    String[] param = { LOAD_LIMIT.VALID.getVal() };
                    errorSb.add("負荷電流(基本):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 負荷電流(基本) 値チェック 1桁の場合は先頭に「0」を付与する
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                    && !curlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()).length() == 1 ? "0" + csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()) : csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))) {
                String[] param = { MeterManagementConstants.CHECK_LOAD_CURRENT };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadCurrent.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 自動投入(基本) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.autoInjectionLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 自動投入(基本) 新規登録以外で負荷制限が「有効」「無効」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                                || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MIN), Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.autoInjectionLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 自動投入(基本) 必須チェック 負荷制限が「有効」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))) {
                    String[] param = { LOAD_LIMIT.VALID.getVal() };
                    errorSb.add("自動投入(基本):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 自動投入(基本) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                    && !(MeterManagementConstants.CHECK_AUTO_INJECTION_ZERO.equals(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                        || CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()), MeterManagementConstants.CHECK_AUTO_INJECTION_MIN, MeterManagementConstants.CHECK_AUTO_INJECTION_MAX))) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MIN), Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.autoInjection.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 開閉器動作カウント(基本) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.breakerActCountLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 開閉器動作カウント(基本) 新規登録以外で負荷制限が「有効」「無効」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                                || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN), Integer.toString(MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.breakerActCountLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 開閉器動作カウント(基本) 必須チェック 負荷制限が「有効」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))) {
                    String[] param = { LOAD_LIMIT.VALID.getVal() };
                    errorSb.add("開閉器動作カウント(基本):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 開閉器動作カウント(基本) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex())) &&
                    !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()), MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN, MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN), Integer.toString(MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.breakerActCount.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 開閉器カウントクリア(基本) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.countClearLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 開閉器カウントクリア(基本) 新規登録以外で負荷制限が「有効」「無効」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && (csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                                || csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.INVALID.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_COUNT_CLEAR_MIN), Integer.toString(MeterManagementConstants.CHECK_COUNT_CLEAR_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.countClearLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 開閉器カウントクリア(基本) 必須チェック 負荷制限が「有効」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(LOAD_LIMIT.VALID.getVal())
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))) {
                    String[] param = { LOAD_LIMIT.VALID.getVal() };
                    errorSb.add("開閉器カウントクリア(基本):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 開閉器カウントクリア(基本) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex())) &&
                    !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()), MeterManagementConstants.CHECK_COUNT_CLEAR_MIN, MeterManagementConstants.CHECK_COUNT_CLEAR_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_COUNT_CLEAR_MIN), Integer.toString(MeterManagementConstants.CHECK_COUNT_CLEAR_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.countClear.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 負荷電流(臨時) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.tempLoadCurrentLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 負荷電流(臨時) 新規登録以外で負荷制限が「臨時設定」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))) {
                    String[] param = { MeterManagementConstants.CHECK_LOAD_CURRENT };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempLoadCurrentLteM.value", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 負荷電流(臨時) 必須チェック 負荷制限が「臨時設定」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))) {
                    String[] param = { genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) };
                    errorSb.add("負荷電流(臨時):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 負荷電流(臨時) 値チェック 1桁の場合は先頭に「0」を付与する
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex())) &&
                    !curlimitMap.containsKey(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()).length() == 1 ? "0" + csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()) : csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))) {
                String[] param = { MeterManagementConstants.CHECK_LOAD_CURRENT };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempLoadCurrent.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 自動投入(臨時) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.tempAutoInjectionLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 自動投入(臨時) 新規登録以外で負荷制限が「臨時設定」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempAutoInjectionLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 自動投入(臨時) 必須チェック 負荷制限が「臨時設定」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))) {
                    String[] param = { genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) };
                    errorSb.add("自動投入(臨時):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 自動投入(臨時) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex())) &&
                    !(MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_ZERO.equals(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                            || CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()), MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MIN, MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MAX))) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_AUTO_INJECTION_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempAutoInjection.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 開閉器動作カウント(臨時) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.tempBreakerActCountLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 開閉器動作カウント(臨時) 新規登録以外で負荷制限が「臨時設定」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempBreakerActCountLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 開閉器動作カウント(臨時) 必須チェック 負荷制限が「臨時設定」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))) {
                    String[] param = { genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) };
                    errorSb.add("開閉器動作カウント(臨時):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 開閉器動作カウント(臨時) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex())) &&
                    !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()), MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MIN, MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_BREAKER_ACT_COUNT_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempBreakerActCount.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            if (lteMDeviceFlg) {
                // LTE-Mの場合
                // 開閉器カウントクリア(臨時) 新規登録時で値が入力されている場合エラー
                if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                        && Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))) {
                    // 新規登録時の値チェック
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.tempCountClearLteM.insertValue")
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
                // 開閉器カウントクリア(臨時) 新規登録以外で負荷制限が「臨時設定」かつ値が入力されていない場合エラー
                if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                        && !Objects.equals(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()), csvRecord.get(updateKbnIndex))
                        && csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))) {
                    String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MAX) };
                    errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempCountClearLteM.range", param)
                            .concat("(現在入力値: ")
                            .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                            .concat(")"));
                    errorFlg = true;
                }
            } else {
                // 開閉器カウントクリア(臨時) 必須チェック 負荷制限が「臨時設定」の場合は必須
                if(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()).equals(genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()))
                        && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))) {
                    String[] param = { genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) };
                    errorSb.add("開閉器カウントクリア(臨時):".concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitMode.required", param)));
                    errorFlg = true;
                }
            }

            // 開閉器カウントクリア(臨時) 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex())) &&
                    !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()), MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MIN, MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MIN), Integer.toString(MeterManagementConstants.CHECK_TEMP_COUNT_CLEAR_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempCountClear.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }
        }

        // -------------------------------------------------
        // パルスメーターの項目
        // -------------------------------------------------
        if(METER_KIND.PULSE.getVal().equals(meterKind)) {
            // 機器への設定送信 必須チェック、値チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))
                    || !(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()).equals(SEND_DEVICE.NOT_SEND.getVal())
                            || csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()).equals(SEND_DEVICE.SEND.getVal()))) {
                String[] param = { SEND_DEVICE.NOT_SEND.getVal(), SEND_DEVICE.SEND.getVal() };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.sendDevice.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // 乗率 必須チェック、範囲チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                    || !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MULTI.getIndex()), MeterManagementConstants.CHECK_MULTI_MIN, MeterManagementConstants.CHECK_MULTI_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_MULTI_MIN), Integer.toString(MeterManagementConstants.CHECK_MULTI_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.multi.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // パルス種別変更フラグ 必須チェック、値チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()))
                    || !(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()).equals(CHECK_CHANGE.NOT_CHANGE.getVal())
                            || csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal()))) {
                String[] param = { CHECK_CHANGE.NOT_CHANGE.getVal(), CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("パルス種別変更フラグ:"
                        .concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.change.value", param))
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // パルス種別 必須チェック パルス種別変更フラグが「変更する」の場合は必須
            if(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal())
                    && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE.getIndex()))) {
                String[] param = { CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("パルス種別:".concat((beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseTypeChg.required", param))));
                errorFlg = true;
            }

            // パルス種別 値チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE.getIndex()))
                    && !pulseTypeMap.containsKey(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE.getIndex()))) {
                String[] param = { genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.INVALID.getVal())
                                    , genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.SHORT_PULSE.getVal())
                                    , genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.LONG_PULSE.getVal())
                                    , genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.STATUS.getVal()) };
                errorSb.add((beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseType.value", param))
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // パルス重み変更フラグ 必須チェック、値チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT_CHG.getIndex()))
                    || !(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT_CHG.getIndex()).equals(CHECK_CHANGE.NOT_CHANGE.getVal())
                            || csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal()))) {
                String[] param = { CHECK_CHANGE.NOT_CHANGE.getVal(), CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("パルス重み変更フラグ:"
                        .concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.change.value", param))
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // パルス重み 必須チェック パルス重み変更フラグが「変更する」の場合は必須
            if(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal())
                    && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex()))) {
                String[] param = { CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("パルス重み:".concat((beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseTypeChg.required", param))));
                errorFlg = true;
            }

            // パルス重み 範囲チェック 機器にてパルス重みを(×0.01)した値を保持するが、サーバサイドではDBのパルス重みに応じた範囲とする
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex()))
                    && !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex()), MeterManagementConstants.CHECK_PULSE_WEIGHT_MIN, MeterManagementConstants.CHECK_PULSE_WEIGHT_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_PULSE_WEIGHT_MIN), Integer.toString(MeterManagementConstants.CHECK_PULSE_WEIGHT_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseWeight.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // 指針値変更フラグ 必須チェック、値チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA_CHG.getIndex()))
                    || !(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA_CHG.getIndex()).equals(CHECK_CHANGE.NOT_CHANGE.getVal())
                            || csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal()))) {
                String[] param = { CHECK_CHANGE.NOT_CHANGE.getVal(), CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("指針値変更フラグ:"
                        .concat(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.change.value", param))
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // 指針値 必須チェック 指針値変更フラグが「変更する」の場合は必須
            if(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA_CHG.getIndex()).equals(CHECK_CHANGE.CHANGE.getVal())
                    && CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex()))) {
                String[] param = { CHECK_CHANGE.CHANGE.getVal() };
                errorSb.add("指針値:".concat((beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseTypeChg.required", param))));
                errorFlg = true;
            }

            // 指針値 範囲チェック 機器にて指針値を(×0.01)した値を保持するが、サーバサイドではDBの指針値に応じた範囲とする
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex()))
                    && !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex()), MeterManagementConstants.CHECK_CURRENT_DATA_MIN, MeterManagementConstants.CHECK_CURRENT_DATA_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_CURRENT_DATA_MIN), Integer.toString(MeterManagementConstants.CHECK_CURRENT_DATA_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.currentData.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }
        }

        if(METER_KIND.IOTR.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // IoT-R連携用メーターの項目
            // -------------------------------------------------
            // IoT-R連携用メーターとしてチェックが必要な項目は無し
        }

        if(METER_KIND.HANDY.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // ハンディ検針用メーターの項目
            // -------------------------------------------------
            // 乗率 必須チェック、範囲チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                    || !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MULTI.getIndex()), MeterManagementConstants.CHECK_MULTI_MIN, MeterManagementConstants.CHECK_MULTI_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_MULTI_MIN), Integer.toString(MeterManagementConstants.CHECK_MULTI_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.multi.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // 無線種別 必須チェック
            if(CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_TYPE.getIndex()))) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.wirelessType.required"));
                errorFlg = true;
            }

            // 無線種別 値チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_TYPE.getIndex()))
                    && !handyTypeMap.containsKey(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_TYPE.getIndex()))) {
                String[] param = { genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.HYBRID.getVal())
                                    , genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.CURRENT.getVal())
                                    , genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.ELECTRONIC.getVal()) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.wirelessType.value", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_TYPE.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // 無線ID 必須チェック
            if(CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_ID.getIndex()))) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.wirelessId.required"));
                errorFlg = true;
            }

            // 無線ID 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_ID.getIndex()))
                    && !CheckUtility.checkRegex(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_ID.getIndex()), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM_12_digits)) {
                String[] param = { String.valueOf(MeterManagementConstants.CHECK_WIRELESS_ID) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.wirelessId.count", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // リレー無線ID1 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()))
                    && !CheckUtility.checkRegex(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM_12_digits)) {
                String[] param = { String.valueOf(MeterManagementConstants.CHECK_WIRELESS_ID) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop1Id.count", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // リレー無線ID2 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()))
                    && !CheckUtility.checkRegex(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM_12_digits)) {
                String[] param = { String.valueOf(MeterManagementConstants.CHECK_WIRELESS_ID) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop2Id.count", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // リレー無線ID3 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()))
                    && !CheckUtility.checkRegex(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM_12_digits)) {
                String[] param = { String.valueOf(MeterManagementConstants.CHECK_WIRELESS_ID) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop3Id.count", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // リレー無線IDの重複チェック
            final String hop1Id = csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex());
            final String hop2Id = csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex());
            final String hop3Id = csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex());
            if ((!CheckUtility.isNullOrEmpty(hop1Id) && hop1Id.equals(hop2Id))
                    || (!CheckUtility.isNullOrEmpty(hop2Id) && hop2Id.equals(hop3Id))
                    || (!CheckUtility.isNullOrEmpty(hop3Id) && hop3Id.equals(hop1Id))) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.hopId.duplicate")
                        .concat("(現在入力値: ")
                        .concat(" リレー1無線ID:")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()))
                        .concat(" リレー2無線ID:")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()))
                        .concat(" リレー3無線ID:")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }

            // ポーリンググループNo 範囲チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.POLLING_ID.getIndex()))
                    && !CheckUtility.checkRegex(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.POLLING_ID.getIndex()), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM_12_digits)) {
                String[] param = { String.valueOf(MeterManagementConstants.CHECK_WIRELESS_ID) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pollingId.count", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.POLLING_ID.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }
        }

        if(METER_KIND.OCR.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // AieLink用メーターの項目
            // 「OCR検針」→「AieLink」へ変更
            // -------------------------------------------------
            // 乗率 必須チェック、範囲チェック
            if (CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                    || !CheckUtility.checkIntegerRange(csvRecord.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MULTI.getIndex()), MeterManagementConstants.CHECK_MULTI_MIN, MeterManagementConstants.CHECK_MULTI_MAX)) {
                String[] param = { Integer.toString(MeterManagementConstants.CHECK_MULTI_MIN), Integer.toString(MeterManagementConstants.CHECK_MULTI_MAX) };
                errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.multi.range", param)
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MULTI.getIndex()))
                        .concat(")"));
                errorFlg = true;
            }
        }

        // -------------------------------------------------
        // 共通の項目
        // -------------------------------------------------
        // 検満西暦・和暦 値チェック
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(dispYearFlgIndex))
                && !dispYearMap.containsKey(csvRecord.get(dispYearFlgIndex))) {
            String[] param = { genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal())
                                , genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.dispYearFlg.value", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(dispYearFlgIndex))
                    .concat(")"));
            errorFlg = true;
        }

        // 「OCR検針」→「AieLink」へ変更
        // AieLink用メーター以外の場合
        if(!METER_KIND.OCR.getVal().equals(meterKind)) {
            // 検満年月 値チェック
            if (!CheckUtility.isNullOrEmpty(csvRecord.get(examEndYmIndex))
                    && !CheckUtility.checkRegex(csvRecord.get(examEndYmIndex), MeterManagementConstants.CHECK_PATTERN_REGEX_EXAM_END_YM)) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYm.value")
                        .concat("(現在入力値: ")
                        .concat(csvRecord.get(examEndYmIndex))
                        .concat(")"));
                errorFlg = true;
            }
        }

        // 検満通知 値チェック
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(examNoticeIndex))
                && !examnoticeMap.containsKey(csvRecord.get(examNoticeIndex))) {
            String[] param = { genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal())
                    , genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.examNotice.value", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(examNoticeIndex))
                    .concat(")"));
            errorFlg = true;
        }

        // コメント 文字数チェック
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(memoIndex))
                && csvRecord.get(memoIndex).length() > MeterManagementConstants.CHECK_MEMO_CNT) {
            String[] param = { String.valueOf(MeterManagementConstants.CHECK_MEMO_CNT) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.memo.range", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(memoIndex))
                    .concat(")"));
            errorFlg = true;
        }

        return errorFlg ? errorSb.toString() : "";
    }

    /**
     * 負荷制限の値にてエラーがあるか判定
     *
     * @param csvRecord 更新予定レコード値
     * @param errorSb エラー内容
     * @return true:エラーがある false:エラーがない
     */
    private boolean checkLoadlimitError(List<String> csvRecord, StringJoiner errorSb) {

        boolean errorFlg = false;
        // 負荷制限
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadlimitModeLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 負荷電流(基本)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.loadCurrentLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 自動投入(基本)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.autoInjectionLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 開閉器動作カウント(基本)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.breakerActCountLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 開閉器カウントクリア(基本)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.countClearLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 負荷電流(臨時)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempLoadCurrentLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 自動投入(臨時)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempAutoInjectionLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 開閉器動作カウント(臨時)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempBreakerActCountLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        // 開閉器カウントクリア(臨時)
        if (!CheckUtility.isNullOrEmpty(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))) {
            String[] param = { csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()) };
            errorSb.add(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.tempCountClearLteM.loadlimitErrorValue", param)
                    .concat("(現在入力値: ")
                    .concat(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))
                    .concat(")"));
            errorFlg = true;
        }

        return errorFlg;
    }

    /**
     * 相関チェック DB登録されている値と登録（更新）予定値を比較
     * @param csvRecord 登録（更新）予定値
     * @param meterList DB登録されている値
     * @param devId 登録（更新）予定の装置ID
     * @param uploadCsvContent アップロードCSVファイル内容
     * @return エラー文字列
     */
    private String complexValidation(List<String> csvRecord, List<MeterInfo> meterList, String devId, List<List<String>> uploadCsvContent) {

        StringJoiner errorSb = new StringJoiner("　"); // 半角空白は反映されないため全角空白を使用

        // CSVファイルより登録（更新）予定値を取得するため共通項目のINDEX設定
        // デフォルト（スマートメーター）
        int meterMngIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
        int meterIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex();

        if(METER_KIND.PULSE.getVal().equals(meterKind)) {
            // パルスメーター
            meterMngIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_ID.getIndex();

        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // IoT-R連携用メーター
            meterMngIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            // ハンディ検針用メーター
            meterMngIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーター
            meterMngIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        }

        boolean updateErrorFlg = true;

        // 更新区分が「更新あり」の場合
        if(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()).equals(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()))) {

            // DB登録されている値と更新予定値を比較
            for(MeterInfo info : meterList) {

                // メーター管理番号(meter_mng_id)と計器ID(meter_id)の両方一致している場合FALSE
                if (String.valueOf(info.getMeterMngId()).equals(csvRecord.get(meterMngIdIndex))
                        && info.getMeterId().equals(csvRecord.get(meterIdIndex))) {
                    updateErrorFlg = false;
                    break;
                }
            }

            if(updateErrorFlg) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.complexValidationUpdate.value")
                        .concat("(更新予定値: ")
                        .concat("装置ID: ")
                        .concat(devId)
                        .concat(" 管理番号: ")
                        .concat(csvRecord.get(meterMngIdIndex))
                        .concat(" 計器ID: ")
                        .concat(csvRecord.get(meterIdIndex))
                        .concat(")"));
            } else {
                int updateCount = 0; // アップロードCSVファイル内のメーター管理番号(meter_mng_id)のカウント
                // 更新予定値とアップロードCSVファイル内容を比較
                for(List<String> list : uploadCsvContent) {

                    // 更新予定値とアップロードCSVファイル内容のメーター管理番号(meter_mng_id)が一致している場合カウント+1
                    if (list.get(meterMngIdIndex).equals(csvRecord.get(meterMngIdIndex))) {
                        updateCount++;
                    }
                }

                // 更新予定のメーター管理番号(meter_mng_id)がアップロードCSVファイルの中に複数ある場合
                if(updateCount != 1) {
                    updateErrorFlg = true;
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.complexValidationDuplicationUpdate.value")
                            .concat("(更新予定値: ")
                            .concat("装置ID: ")
                            .concat(devId)
                            .concat(" 管理番号: ")
                            .concat(csvRecord.get(meterMngIdIndex))
                            .concat(" 計器ID: ")
                            .concat(csvRecord.get(meterIdIndex))
                            .concat(")"));
                }
            }
        }

        boolean insertErrorFlg = false;

        // 更新区分が「新規登録」の場合
        if(genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal()).equals(csvRecord.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()))) {

            // DB登録されている値と登録予定値を比較
            for(MeterInfo info : meterList) {
                /// 登録予定値とDB登録のメーター管理番号(meter_mng_id)が一致している場合TRUE
                if (String.valueOf(info.getMeterMngId()).equals(csvRecord.get(meterMngIdIndex))) {
                    insertErrorFlg = true;
                    break;
                }
            }

            if (insertErrorFlg) {
                errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.complexValidationInsert.value")
                        .concat("(登録予定値: ")
                        .concat("装置ID: ")
                        .concat(devId)
                        .concat(" 管理番号: ")
                        .concat(csvRecord.get(meterMngIdIndex))
                        .concat(")"));
            } else {
                int insertCount = 0; // アップロードCSVファイル内のメーター管理番号(meter_mng_id)のカウント
                // 新規登録予定値とアップロードCSVファイル内容を比較
                for(List<String> list : uploadCsvContent) {

                    // 新規登録予定値とアップロードCSVファイル内容のメーター管理番号(meter_mng_id)が一致している場合カウント+1
                    if (list.get(meterMngIdIndex).equals(csvRecord.get(meterMngIdIndex))) {
                        insertCount++;
                    }
                }

                // 新規登録予定のメーター管理番号(meter_mng_id)がアップロードCSVファイルの中に複数ある場合
                if(insertCount != 1) {
                    insertErrorFlg = true;
                    errorSb.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.complexValidationDuplicationInsert.value")
                            .concat("(登録予定値: ")
                            .concat("装置ID: ")
                            .concat(devId)
                            .concat(" 管理番号: ")
                            .concat(csvRecord.get(meterMngIdIndex))
                            .concat(")"));
                }
            }
        }

        if (updateErrorFlg || insertErrorFlg) {
            return errorSb.toString();
        } else {
            return "";
        }
    }

    /**
     * メータID(計器ID)重複チェック DB登録されているメータID(計器ID)と登録予定メータID(計器ID)を比較
     * @param csvRecord 登録予定値
     * @return エラー文字列
     */
    private String meterIdDuplicateCheck(List<String> csvRecord) {

        // CSVファイルより登録予定値を取得するため共通項目のINDEX設定
        // デフォルト（スマートメーター）
        int meterMngIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
        int meterIdIndex = MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex();

        if(METER_KIND.PULSE.getVal().equals(meterKind)) {
            // パルスメーター
            meterMngIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.METER_ID.getIndex();

        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // IoT-R連携用メーター
            meterMngIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            // ハンディ検針用メーター
            meterMngIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーター
            meterMngIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex();
            meterIdIndex = MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.METER_ID.getIndex();
        }

        return meterIdDuplicateCheck(csvRecord.get(meterMngIdIndex), csvRecord.get(meterIdIndex));
    }

    /**
     * メータID(計器ID)重複チェック DB登録されているメータID(計器ID)と登録予定メータID(計器ID)を比較
     * @param meterMngId メータ管理番号
     * @param meterId メーターID
     * @return エラー文字列
     */
    private String meterIdDuplicateCheck(String meterMngId, String meterId) {

        StringJoiner errorSb = new StringJoiner("　"); // 半角空白は反映されないため全角空白を使用

        boolean errorFlg = false;

        // メータID(計器ID)に該当するレコードをDBより取得
        List<MMeter> mMeterList = mMeterListDao.getMeterIdList(meterId);

        // 該当するレコードが存在する場合
        if (!mMeterList.isEmpty()) {
            errorFlg = true;
            errorSb.add(beanMessages
                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.meterIdDuplicateCheck.value")
                    .concat("(登録予定値: ")
                    .concat(" 管理番号: ")
                    .concat(meterMngId)
                    .concat(" 計器ID: ")
                    .concat(meterId)
                    .concat(")"));
        } else {
            // 新規登録予定レコードの中でメータID(計器ID)が一致している場合
            for (String id : meterIdList) {
                if (id.equals(meterId)) {
                    errorFlg = true;
                    errorSb.add(beanMessages
                            .getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterIdDuplicateCheckInsert.value")
                            .concat("(登録予定値: ")
                            .concat(" 管理番号: ")
                            .concat(meterMngId)
                            .concat(" 計器ID: ")
                            .concat(meterId)
                            .concat(")"));
                }
            }
            meterIdList.add(meterId);
        }

        if (errorFlg) {
            return errorSb.toString();
        } else {
            return "";
        }
    }

    private UpdateBulkSmsMeterParameter createParameter(List<List<String>> csvList) throws ParseException {
        UpdateBulkSmsMeterParameter parameter = new UpdateBulkSmsMeterParameter();
        UpdateBulkSmsMeterRequest request = new UpdateBulkSmsMeterRequest();
        request.setDevId(this.devId);
        for (List<String> record : csvList) {
            UpdateBulkSmsMeterRequestSet requestSet = new UpdateBulkSmsMeterRequestSet();
            // -------------------------------------------------
            // 共通の項目
            // -------------------------------------------------
            //更新区分
            requestSet.setUpdateKbn(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()));
            //管理番号
            requestSet.setMeterMngId(Long.valueOf(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_MNG_ID.getIndex())));
            //計器ID
            requestSet.setMeterId(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_ID.getIndex()));
            //メーター種別
            requestSet.setMeterType(Long.valueOf(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.METER_TYPE.getIndex())));
            //ユーザーコード
            if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TENANT_ID.getIndex()))) {
                requestSet.setTenantId(Long.valueOf(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TENANT_ID.getIndex())));
            }

            if(METER_KIND.SMART.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // スマートメーターの項目
                // -------------------------------------------------
                //機器への設定送信 メーター管理画面にて設定
                requestSet.setSendLumpFlg(isSendLumpFlg());
                //機器への設定送信 CSVファイル内で設定
                requestSet.setSendDeviceFlg(SEND_DEVICE.SEND.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex())));
                //開閉区分
                requestSet.setOpenMode(ocstateMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.OPEN_MODE.getIndex())));
                //負荷制限
                if (LOAD_LIMIT.INVALID.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                    requestSet.setLoadlimitMode(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal());
                } else if (LOAD_LIMIT.VALID.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex()))) {
                    requestSet.setLoadlimitMode(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal());
                } else {
                    requestSet.setLoadlimitMode(loadlimitMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOADLIMIT_MODE.getIndex())));
                }
                //負荷電流(基本)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()))) {
                    requestSet.setLoadCurrent(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.LOAD_CURRENT.getIndex()));
                }
                //自動投入(基本)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()))) {
                    requestSet.setAutoInjection(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.AUTO_INJECTION.getIndex()));
                }
                //開閉器動作カウント(基本)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()))) {
                    requestSet.setBreakerActCount(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.BREAKER_ACT_COUNT.getIndex()));
                }
                //開閉器カウントクリア(基本)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()))) {
                    requestSet.setCountClear(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.COUNT_CLEAR.getIndex()));
                }
                //負荷電流(臨時)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()))) {
                    requestSet.setTempLoadCurrent(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_LOAD_CURRENT.getIndex()));
                }
                //自動投入(臨時)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()))) {
                    requestSet.setTempAutoInjection(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_AUTO_INJECTION.getIndex()));
                }
                //開閉器動作カウント(臨時)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()))) {
                    requestSet.setTempBreakerActCount(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_BREAKER_ACT_COUNT.getIndex()));
                }
                //開閉器カウントクリア(臨時)
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()))) {
                    requestSet.setTempCountClear(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.TEMP_COUNT_CLEAR.getIndex()));
                }
                //検満西暦・和暦
                requestSet.setDispYearFlg(dispYearMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex())));
                //検満年月
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()))) {
                    requestSet.setExamEndYm(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()));
                }
                //検満通知
                requestSet.setExamNotice(examnoticeMap.get(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex())));
                //コメント
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.MEMO.getIndex()))) {
                    requestSet.setMemo(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.MEMO.getIndex()));
                }

                request.getRequestSetList().add(requestSet);
            }
            if(METER_KIND.PULSE.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // パルスメーターの項目
                // -------------------------------------------------
                //機器への設定送信 メーター管理画面にて設定
                requestSet.setSendLumpFlg(isSendLumpFlg());
                //機器への設定送信 CSVファイル内で設定
                requestSet.setSendDeviceFlg(SEND_DEVICE.SEND.getVal().equals(record.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.SEND_DEVICE.getIndex())));
                // 乗率
                requestSet.setMulti(StringUtility.toBigDecimal(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MULTI.getIndex())));
                // パルス種別変更フラグ
                requestSet.setPulseTypeChg(String.valueOf((CHECK_CHANGE.CHANGE.getVal().equals(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE_CHG.getIndex()))
                        ? OsolConstants.FLG_ON
                        : OsolConstants.FLG_OFF)));
                // パルス種別
                requestSet.setPulseType(pulseTypeMap.get(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_TYPE.getIndex())));
                // パルス重み変更フラグ
                requestSet.setPulseWeightChg(String.valueOf((CHECK_CHANGE.CHANGE.getVal().equals(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT_CHG.getIndex()))
                        ? OsolConstants.FLG_ON
                        : OsolConstants.FLG_OFF)));
                // パルス重み 機器にてパルス重みを(×0.01)した値を保持するが、サーバサイドではDBのパルス重みに応じた値を保持する
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex()))) {
                    requestSet.setPulseWeight(StringUtility.toBigDecimal(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.PULSE_WEIGHT.getIndex())));
                }
                // 指針値変更フラグ
                requestSet.setCurrentDataChg(String.valueOf((CHECK_CHANGE.CHANGE.getVal().equals(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA_CHG.getIndex()))
                        ? OsolConstants.FLG_ON
                        : OsolConstants.FLG_OFF)));
                // 指針値 機器にて指針値を(×0.01)した値を保持するが、サーバサイドではDBの指針値に応じた値を保持する
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex()))) {
                    requestSet.setCurrentData(StringUtility.toBigDecimal(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.CURRENT_DATA.getIndex())));
                }
                // 検満西暦・和暦
                requestSet.setDispYearFlg(dispYearMap.get(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex())));
                // 検満年月
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()))) {
                    requestSet.setExamEndYm(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()));
                }
                // 検満通知
                requestSet.setExamNotice(examnoticeMap.get(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex())));
                // コメント
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MEMO.getIndex()))) {
                    requestSet.setMemo(record.get(MeterBulkCsvIndex.PULSE_METER_BULK_CSV_INDEX.MEMO.getIndex()));
                }

                request.getRequestSetList().add(requestSet);
            }
            if(METER_KIND.IOTR.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // IoT-R連携用メーターの項目
                // -------------------------------------------------
                // 検満西暦・和暦
                requestSet.setDispYearFlg(dispYearMap.get(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex())));
                // 検満年月
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()))) {
                    requestSet.setExamEndYm(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()));
                }
                // 検満通知
                requestSet.setExamNotice(examnoticeMap.get(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex())));
                // コメント
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.MEMO.getIndex()))) {
                    requestSet.setMemo(record.get(MeterBulkCsvIndex.IOTR_METER_BULK_CSV_INDEX.MEMO.getIndex()));
                }

                request.getRequestSetList().add(requestSet);
            }
            if(METER_KIND.HANDY.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // ハンディ検針用メーターの項目
                // -------------------------------------------------
                // 乗率
                requestSet.setMulti(StringUtility.toBigDecimal(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MULTI.getIndex())));
                // 無線種別
                requestSet.setWirelessType(handyTypeMap.get(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_TYPE.getIndex())));
                // 無線ID
                requestSet.setWirelessId(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.WIRELESS_ID.getIndex()));
                // リレー無線ID1
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()))) {
                    requestSet.setHop1Id(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP1_ID.getIndex()));
                }
                // リレー無線ID2
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()))) {
                    requestSet.setHop2Id(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP2_ID.getIndex()));
                }
                // リレー無線ID3
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()))) {
                    requestSet.setHop3Id(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.HOP3_ID.getIndex()));
                }
                // ポーリンググループNo
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.POLLING_ID.getIndex()))) {
                    requestSet.setPollingId(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.POLLING_ID.getIndex()));
                }
                // 検満西暦・和暦
                requestSet.setDispYearFlg(dispYearMap.get(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex())));
                // 検満年月
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()))) {
                    requestSet.setExamEndYm(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.EXAM_END_YM.getIndex()));
                }
                // 検満通知
                requestSet.setExamNotice(examnoticeMap.get(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex())));
                // コメント
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MEMO.getIndex()))) {
                    requestSet.setMemo(record.get(MeterBulkCsvIndex.HANDY_METER_BULK_CSV_INDEX.MEMO.getIndex()));
                }

                request.getRequestSetList().add(requestSet);
            }
            if(METER_KIND.OCR.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // AieLink用メーターの項目
                // 「OCR検針」→「AieLink」へ変更
                // -------------------------------------------------
                // 乗率
                requestSet.setMulti(StringUtility.toBigDecimal(record.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MULTI.getIndex())));
                // 検満西暦・和暦
                requestSet.setDispYearFlg(dispYearMap.get(record.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.DISP_YEAR_FLG.getIndex())));
                // 検満通知
                requestSet.setExamNotice(examnoticeMap.get(record.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.EXAM_NOTICE.getIndex())));
                // コメント
                if (!CheckUtility.isNullOrEmpty(record.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MEMO.getIndex()))) {
                    requestSet.setMemo(record.get(MeterBulkCsvIndex.OCR_METER_BULK_CSV_INDEX.MEMO.getIndex()));
                }

                request.getRequestSetList().add(requestSet);
            }
        }
        parameter.setRequest(request);

        return parameter;
    }

    public void renameUploadFile() {
        // アップロードファイルが存在する場合にファイル名変更
        if (!CheckUtility.isNullOrEmpty(uploadFilePath)) {

            // 変更前ファイル情報取得
            String beforeUploadFilePath = uploadFilePath;
            File beforeFile = new File(beforeUploadFilePath);

            // 変更後ファイル情報取得
            String now = DateUtility.changeDateFormat(getCurrentDateTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);

            // スマートメーターとパルスメーターのみ画面送信チェック有無をファイル名に付与
            String fileName = null;
            if (METER_KIND.SMART.getVal().equals(meterKind) || METER_KIND.PULSE.getVal().equals(meterKind)) {
                fileName = String.format("%s_登録完了_画面送信チェック%s_%d_%s", now, isSendLumpFlg() ? "あり" : "なし", getLoginUserId(), uploadFileName);
            } else {
                fileName = String.format("%s_登録完了_%d_%s", now, getLoginUserId(), uploadFileName);
            }

            String afterUploadFilePath = uploadFilePath.replace(String.format("%s_登録前_%d_%s", uploadDate, getLoginUserId(), uploadFileName), fileName);
            File afterFile = new File(afterUploadFilePath);

            // ファイル名変更
            boolean isRenamed = beforeFile.renameTo(afterFile);

            // 結果表示
            if(isRenamed) {
                eventLogger.info(MeterManagementBean.class.getPackage().getName().concat(" smsCollectSettingMeterMeterManagementBean:renameUploadFile():"
                        + "ファイル名変更完了 変更前ファイル:" + beforeUploadFilePath + " 変更後ファイル:" + afterUploadFilePath));
            } else {
                eventLogger.error(MeterManagementBean.class.getPackage().getName().concat(" smsCollectSettingMeterMeterManagementBean:renameUploadFile():"
                        + "ファイル名変更失敗 変更前ファイル:" + beforeUploadFilePath + " 変更後ファイル:" + afterUploadFilePath));
            }
        }
    }

    public void execFileUpload() {
        this.uploadCsvContent.clear();
        this.fileUploadErrorList.clear();
        this.bulkErrorList.clear();
        // バリデートチェック(falseの場合処理終了)
        if (uploadFile != null) {
            uploadFileName = uploadFile.getSubmittedFileName();
        }
        if (!validateUploadFile(uploadFile)) {
            uploadFile = null;
            uploadFilePath = "";
            return ;
        }

        //ファイルのアップロード
        uploadDate = DateUtility.changeDateFormat(getCurrentDateTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        String fileName = String.format("%s_登録前_%d_%s", uploadDate, getLoginUserId(), uploadFileName);
        uploadFilePath = fileUploader.meterBulkTempFileUpload(uploadFile, getDevId(), fileName);

        //アップロードしたファイルの内容を取得
        MeterManagementCsvConverter csvConverter = new MeterManagementCsvConverter();
        try {
            this.uploadCsvContent = csvConverter.csvParse(uploadFilePath, "MS932");
        } catch (IOException e) {
            uploadFileName = null;
            uploadFile = null;
            fileUploadErrorList.add(
                    beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.templateNotExist"));
            return ;
        }

        //コメント部を除去(先頭が#の行)
        this.uploadCsvContent = this.uploadCsvContent.stream()
                .filter(x -> !x.get(MeterBulkCsvIndex.SMART_METER_BULK_CSV_INDEX.UPDATE_KBN.getIndex()).trim().startsWith("#"))
                .collect(Collectors.toList());

        // 処理後アップロードした一時ファイルを削除する
        // サーバにログとして残すためコメントアウト
//        fileUploader.meterBulkTempDirDelete(getLoginUserId());
    }

    // メーター種類はファイル名に入れる対応のためコメントアウト
//    /**
//     * ダウンロード・アップロード時のメーター別ディレクトリ取得
//     * @return メーター別ディレクトリ名
//     */
//    private String selectMeterTypeDirectory() {
//        meterTypeDirectory = MeterManagementConstants.UPDATE_DIRECTORY_SMART;
//
//        if (METER_KIND.PULSE.getVal().equals(meterKind)) {
//            meterTypeDirectory = MeterManagementConstants.UPDATE_DIRECTORY_PULSE;
//        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
//            meterTypeDirectory = MeterManagementConstants.UPDATE_DIRECTORY_IOTR;
//        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
//            meterTypeDirectory = MeterManagementConstants.UPDATE_DIRECTORY_HANDY;
//        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
//            meterTypeDirectory = MeterManagementConstants.UPDATE_DIRECTORY_OCR;
//        }
//        return meterTypeDirectory;
//    }

    /**
     * アップロード時のバリデーション
     *
     * @param uploadFile
     * @return
     */
    private boolean validateUploadFile(Part uploadFile) {
        if (uploadFile == null || CheckUtility.isNullOrEmpty(uploadFileName)) {
            fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.notInputFile"));
            return false;
        }

        String extension = fileUploader.getFileName(uploadFile).substring(
                fileUploader.getFileName(uploadFile).lastIndexOf(".") + 1, fileUploader.getFileName(uploadFile).length());
        if (CheckUtility.isNullOrEmpty(extension) || !extension.equals("csv")) {
            fileUploadErrorList.add(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.notInputFileType"));
            return false;
        }
        return true;
    }

    /**
     * 新規登録画面の「登録」ボタン押下時の処理
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public void execRegist(MeterManagementInputProperty input)
            throws IllegalAccessException, InvocationTargetException {
        System.out.println("------------------------- execRegist Call -------------------------");
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execRegist():START"));
        InsertSmsMeterResponse response = new InsertSmsMeterResponse();
        InsertSmsMeterParameter parameter = new InsertSmsMeterParameter();

        parameter.setBean("InsertSmsMeterBean");
        InsertSmsMeterRequest request = new InsertSmsMeterRequest();

        // エラーがある場合処理終了
        if (checkValidation(input, true, null)) {
            return;
        }
        // LTE-Mの場合、値をnullに設定
        if (lteMDeviceFlg) {
            input.setLoadlimitMode(null);        // 負荷制限
            input.setLoadCurrent(null);          // 負荷電流
            input.setTempLoadCurrent(null);      // (臨時)負荷電流
            input.setAutoInjection(null);        // 自動投入
            input.setTempAutoInjection(null);    // (臨時)自動投入
            input.setBreakerActCount(null);      // 開閉器動作カウント
            input.setTempBreakerActCount(null);  // (臨時)開閉器動作カウント
            input.setCountClear(null);           // 開閉器カウントクリア
            input.setTempCountClear(null);       // (臨時)開閉器カウントクリア
            input.setOpenMode(null);             // 開閉区分
        }

        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        ConvertUtils.register(new LongConverter(null), Long.class);
        // 中身を詰める
        BeanUtils.copyProperties(request, input);
        // 回避するやつを解除
        ConvertUtils.deregister();
        request.setMeterMngId(input.getMeterMngId());
        request.setMeterType(input.getMeterType());
        request.setDevId(smartProperty.getDevId());
        request.setExamEndYm(
                convertToExamEndYm(input.getDispYearFlg(),
                        input.getExamEndYmGengo(),
                        input.getExamEndYear(),
                        input.getExamEndYearWareki(),
                        input.getExamEndMonth()));
        // パルスの登録なら
        if (METER_KIND.PULSE.getVal().equals(meterKind)) {
            // 現在値変更フラグ
            if (input.getCurrentDataChg()) {
                request.setCurrentDataChg(MeterManagementConstants.FLG_ON);
            } else {
                request.setCurrentDataChg(MeterManagementConstants.FLG_OFF);
            }
            // パルス種別変更フラグ
            if (input.getPulseTypeChg()) {
                request.setPulseTypeChg(MeterManagementConstants.FLG_ON);
            } else {
                request.setPulseTypeChg(MeterManagementConstants.FLG_OFF);
            }
            // パルス重み変更フラグ
            if (input.getPulseWeightChg()) {
                request.setPulseWeightChg(MeterManagementConstants.FLG_ON);
            } else {
                request.setPulseWeightChg(MeterManagementConstants.FLG_OFF);
            }
        }

        final String tenantIdStr = input.getTenantId();
        if (tenantIdStr != null && !tenantIdStr.equals("")) {
            request.setTenantId(Long.parseLong(tenantIdStr));
        }

        // 機器に送信する
        parameter.setFromDeviceCtrl(!input.isSendFlg());

        parameter.setResult(request);

        SmsApiGateway gateway = new SmsApiGateway();

        response = (InsertSmsMeterResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            // 登録失敗→メッセージをセットしてreturn
            if (CheckUtility.isNullOrEmpty(response.getError())) {
                addErrorMessage(
                        beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            } else {
                addErrorMessage(response.getError());
            }
            return;
        }
        keepDevId = smartProperty.getDevId();
        // 画面初期化
        reload();

        addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execRegist():END"));
//        return "meterManagement";
    }

    /**
     * 入力項目系propertyとして使用している変数をリセットする処理
     */
    private void inputReset() {
        // スマメ登録用プロパティ
        smartInsertProperty = new MeterManagementInputProperty();

        smartInsertProperty.setMeterMngId(minMeterMngId); // 管理番号
        smartInsertProperty.setLoadlimitMode("1");        // 負荷制限
        smartInsertProperty.setLoadCurrent("60");         // 負荷電流
        smartInsertProperty.setTempLoadCurrent("60");     // (臨時)負荷電流
        smartInsertProperty.setBreakerActCount("0");      // 開閉器動作カウント
        smartInsertProperty.setTempBreakerActCount("0");  // (臨時)開閉器動作カウント
        smartInsertProperty.setCountClear("0");           // 開閉器カウントクリア
        smartInsertProperty.setTempCountClear("0");       // (臨時)開閉器カウントクリア
        smartInsertProperty.setOpenMode("1");             // 開閉区分
        smartInsertProperty.setMeterType(firstMeterType); // メーター種別
        smartInsertProperty.setDispYearFlg("0");          // 西暦・和暦表示確認用フラグ
        smartInsertProperty.setExamEndYmGengo("0");       // 検満年月_元号
        smartInsertProperty.setExamNotice("1");           // 検満通知フラグ

        // スマメ更新用プロパティ
        smartUpdateProperty = new MeterManagementInputProperty();

        // スマメ設定更新用プロパテ
        smartSettingUpdateProperty = new MeterManagementInputProperty();

        // スマメメーター状態登録用プロパティ
        smartMeterStatusProperty = new MeterStatusProperty();

        // パルス登録用プロパティ
        pulseInsertProperty = new MeterManagementInputProperty();
        pulseInsertProperty.setMeterMngId(minMeterMngId);
        pulseInsertProperty.setPulseType("1");
        pulseInsertProperty.setPulseTypeHidden("1");
        pulseInsertProperty.setMeterType(firstMeterType);
        pulseInsertProperty.setDispYearFlg("0");
        pulseInsertProperty.setExamEndYmGengo("0");
        pulseInsertProperty.setExamNotice("1");

        // パルス更新用プロパティ
        pulseUpdateProperty = new MeterManagementInputProperty();

        // パルス設定更新用プロパテ
        pulseSettingUpdateProperty = new MeterManagementInputProperty();

        // パルスメーター状態登録用プロパティ
        pulseMeterStatusProperty = new MeterStatusProperty();

        // IoT-R登録用プロパティ
        iotRInsertProperty = new MeterManagementInputProperty();
        iotRInsertProperty.setMeterMngId(minMeterMngId);
        iotRInsertProperty.setMeterType(firstMeterType);
        iotRInsertProperty.setDispYearFlg("0");
        iotRInsertProperty.setExamEndYmGengo("0");
        iotRInsertProperty.setExamNotice("1");

        // IoT-R更新用プロパティ
        iotRUpdateProperty = new MeterManagementInputProperty();

        // IoT-R設定更新用プロパテ
        iotRSettingUpdateProperty = new MeterManagementInputProperty();

        // IoT-R連携用メーター状態登録用プロパティ
        iotRMeterStatusProperty = new MeterStatusProperty();

        // ハンディ登録用プロパティ
        handyInsertProperty = new MeterManagementInputProperty();
        handyInsertProperty.setMeterMngId(minMeterMngId);
        handyInsertProperty.setDispYearFlg("0");
        handyInsertProperty.setExamNotice("1");

        // ハンディ更新用プロパティ
        handyUpdateProperty = new MeterManagementInputProperty();

        // OCR登録用プロパティ
        ocrInsertProperty = new MeterManagementInputProperty();
        ocrInsertProperty.setMeterMngId(minMeterMngId); // 管理番号
        ocrInsertProperty.setMeterType(firstMeterType); // メーター種別
        ocrInsertProperty.setDispYearFlg("0");          // 西暦・和暦表示確認用フラグ
        ocrInsertProperty.setExamEndYmGengo("0");       // 検満年月_元号

        // OCR更新用プロパティ
        ocrUpdateProperty = new MeterManagementInputProperty();
    }

    /**
     * 変更・削除画面の変更ボタン押下時の処理・設定変更画面の実行ボタン押下時の処理
     * @param input 登録/更新時のインプット情報
     * @param 更新タイプ  "1":登録  "2":更新
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws Exception
     */
    @Logged
    public void execEdit(MeterManagementInputProperty input, String updateType)
            throws IllegalAccessException, InvocationTargetException, Exception {
        System.out.println("------------------------- execEdit Call -------------------------");
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execEdit():START"));

        // 「LTE-M かつ 設定変更」の場合
        if (lteMDeviceFlg && MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType)) {

            // エラーがある場合処理終了
            if (checkValidation(input, false, updateType)) {
                return;
            }

            // 初期化
            initLteMApiInfo();

            // 対象のメーターリストを取得
            List<MeterInfo> tempList = (meterList == null) ? Collections.emptyList() : meterList;
            List<MeterInfo> targetMeterList = tempList.stream()
                    .filter(m -> Boolean.TRUE.equals(m.getCheckBox()))
                    .collect(Collectors.toList());

            // 非同期でAPI実行
            // input.getLoadlimitMode()="0"(=無効)の場合、
            // input.setLoadlimitTarget()="temp"でinput.getLoadlimitMode()="R"(=臨時解除)として2回目の負荷制限APIを実行（1回目を送ってから設定時間経過後）
            String requestId = UUID.randomUUID().toString(); // リクエストの判別のためIDを採番
            List<LteMMeterExecResult> results = lteMParallelExecutor.executeAsyncForMeters(
                        targetMeterList,
                        input,
                        MeterManagementConstants.LTEM_API_TYPE.SET.getCode(),
                        requestId,
                        getLoginPersonId(),
                        mMeterInfoDao
                        );

            // リクエスト単位で結果を保持
            currentExecResults.clear();
            currentExecResults.put(requestId, results);
            lastRequestId = requestId;

            // API実行結果取得
            getExecResult(results);

            // ログ出力
            logAndNotifyInfo("設定変更 API実行 成功: %d件%s", apiSuccessCount,
                apiSuccessCount == 0 ? "" : " [管理番号：" + apiSuccessCsv + "]"
            );
            logAndNotifyError("設定変更 API実行 失敗: %d件%s", apiFailedCount,
                apiFailedCount == 0 ? "" : " [管理番号：" + apiFailedCsv + "]"
            );

            // 全て成功ではない場合
            if (apiFailedCount > 0) {
                return;
            } else {
                addMessage(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.LteMSettingUpdateSuccessMessage"));
            }
        } else {
            // 「LTE-M かつ 設定変更」以外の場合
            UpdateSmsMeterResponse response = new UpdateSmsMeterResponse();
            UpdateSmsMeterParameter parameter = new UpdateSmsMeterParameter();

            parameter.setBean("UpdateSmsMeterBean");
            UpdateSmsMeterRequest request = new UpdateSmsMeterRequest();

            // エラーがある場合処理終了
            if (checkValidation(input, false, updateType)) {
                return;
            }

            // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
            ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
            ConvertUtils.register(new DateConverter(null), Date.class);
            ConvertUtils.register(new LongConverter(null), Long.class);
            // 中身を詰める
            BeanUtils.copyProperties(request, input);
            // 回避するやつを解除
            ConvertUtils.deregister();
            // copyPropertiesでコピーできないことろを設定
            request.setDevId(smartProperty.getDevId());
            request.setMeterMngId(input.getMeterMngId());
            request.setMeterType(input.getMeterType());
            request.setExamEndYm(
                    convertToExamEndYm(input.getDispYearFlg(),
                            input.getExamEndYmGengo(),
                            input.getExamEndYear(),
                            input.getExamEndYearWareki(),
                            input.getExamEndMonth()));
            // パルスの登録なら
            if (METER_KIND.PULSE.getVal().equals(meterKind)) {
                // 現在値変更フラグ
                if (input.getCurrentDataChg()) {
                    request.setCurrentDataChg(MeterManagementConstants.FLG_ON);
                } else {
                    request.setCurrentDataChg(MeterManagementConstants.FLG_OFF);
                }
                // パルス種別変更フラグ
                if (input.getPulseTypeChg()) {
                    request.setPulseTypeChg(MeterManagementConstants.FLG_ON);
                } else {
                    request.setPulseTypeChg(MeterManagementConstants.FLG_OFF);
                }
                // パルス重み変更フラグ
                if (input.getPulseWeightChg()) {
                    request.setPulseWeightChg(MeterManagementConstants.FLG_ON);
                } else {
                    request.setPulseWeightChg(MeterManagementConstants.FLG_OFF);
                }
            }
            final String tenantIdStr = input.getTenantId();
            if (tenantIdStr != null && !tenantIdStr.equals("")) {
                request.setTenantId(Long.parseLong(tenantIdStr));
            }

            if (MeterManagementConstants.UPDATE_TYPE_REGIST.equals(updateType)) {
                // 登録内容 変更
                parameter.setUpdatePattern(SmsConstants.METER_UPDATE_PATTERN.REGIST.getVal());
            } else if (MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType)) {
                // 設定内容 変更
                parameter.setUpdatePattern(SmsConstants.METER_UPDATE_PATTERN.SETTING.getVal());

                //自動投入がnullの場合、デフォルトで0を登録
                if(CheckUtility.isNullOrEmpty(request.getAutoInjection())) {
                    request.setAutoInjection("0");
                }

                if(CheckUtility.isNullOrEmpty(request.getTempAutoInjection())) {
                    request.setTempAutoInjection("0");
                }

                // 更新条件を取得 (複数選択あり)
                List<UpdateSmsMeterRequestSet> requestSetList = new ArrayList<>();
                for (MeterInfo meterInfo : meterList) {
                    if (meterInfo.getCheckBox()) {
                        UpdateSmsMeterRequestSet requestSet = new UpdateSmsMeterRequestSet();
                        requestSet.setMeterMngId(meterInfo.getMeterMngId());
                        requestSet.setDevId(smartProperty.getDevId());
                        requestSet.setVersion(meterInfo.getVersion());
                        requestSetList.add(requestSet);
                    }
                }
                request.setRequestSetList(requestSetList);
            }

            if(request.getDevId().startsWith("OC")) {
                // OCR装置なら送信フラグを立てない
                parameter.setSendFlg(false);
            }else {
                // 機器に送信する
                parameter.setSendFlg(input.isSendFlg());

            }

            parameter.setResult(request);

            SmsApiGateway gateway = new SmsApiGateway();

            response = (UpdateSmsMeterResponse) gateway.osolApiPost(
                    osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                    SmsApiGateway.PATH.JSON,
                    parameter,
                    response);

            if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
                // 更新失敗→メッセージをセットしてreturn
                if (CheckUtility.isNullOrEmpty(response.getError())) {
                    addErrorMessage(
                            beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
                } else {
                    addErrorMessage(response.getError());
                }
                return;
            }
        }

        // 「LTE-M かつ 設定内容変更」以外の場合
        if (!(lteMDeviceFlg && MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType))) {
            keepDevId = smartProperty.getDevId();
            // 画面初期化
            reload();

            addMessage(beanMessages.getMessage("osol.info.UpdateSuccess"));
        }

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execEdit():END"));
    }

    /**
     * 文字列(String)をBigDecimalへ変換
     *
     * @param value 負荷電流値(String)
     * @return 負荷電流値(BigDecimal)
     */
    private static BigDecimal safeParseAmpDecimal(String value) {
        if (value == null) {
            return null;
        }
        String t = value.trim().replaceAll("[aAＡ]|[,，]", "");
        if (!t.matches("^[+-]?\\d+(\\.\\d+)?$")) {
            return null;
        }
        try {
            return new BigDecimal(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 定格電流の制限の判定
     *
     * @param label 基本/臨時
     * @param loadCurrent 負荷電流値
     * @param ratedCurrent 定格電流
     * @param meterMngId メーター管理番号
     * @param meterId 計器ID
     * @param componentClientId 対象画面のクライアントID
     * @param StringJoiner エラー表示
     * @param lumpRegistFlg 一括登録フラグ(true:一括登録 false:一括登録以外)
     * @return 判定結果 (true:エラー false:正常)
     */
    private boolean isLoadCurrentError(String label, String loadCurrent, BigDecimal ratedCurrent, String meterMngId, String meterId, String componentClientId, StringJoiner errorSb, boolean lumpRegistFlg) {

        BigDecimal loadCurrentValue = safeParseAmpDecimal(loadCurrent);
        if (ratedCurrent == null) {
            return exportError(lumpRegistFlg, errorSb, componentClientId
                    , String.format("メーターの定格電流が存在しないため、開閉・負荷の設定はできません。メーター管理番号:%s メータID:%s", meterMngId, meterId));
        }
        RATED_CURRENT_CHECK check = RATED_CURRENT_CHECK.fromRated(ratedCurrent);
        if (check == null) {
            // 未定義の定格電流は今回の判定対象外
            return exportError(lumpRegistFlg, errorSb, componentClientId
                    , String.format("メーターの定格電流が未定義のため、開閉・負荷の設定はできません。メーター管理番号:%s メータID:%s", meterMngId, meterId));
        }

        // 定格電流(5A, 250A)は全禁止
        if (check.mode() == RATED_CURRENT_CHECK.Mode.FORBID_ALL) {
            return exportError(lumpRegistFlg, errorSb, componentClientId
                    , String.format("メーターの定格電流が%sAのため、開閉・負荷の設定はできません。メーター管理番号:%s メータID:%s", ratedCurrent.toPlainString(), meterMngId, meterId));
        }

        // 定格電流30Aの場合、負荷電流「30Aまで可」= 超過でエラー
        if (check.mode() == RATED_CURRENT_CHECK.Mode.LIMIT) {
            BigDecimal limit = check.limitLoadCurrent();
            if (limit != null && limit.compareTo(loadCurrentValue) < 0) {
                return exportError(lumpRegistFlg, errorSb, componentClientId
                        , String.format("メーターの定格電流をもとに判定した結果、設定された負荷電流(%s)%sAは設定不可です。設定可能上限値:%sA メーター管理番号:%s メータID:%s", label, loadCurrentValue.toPlainString(), limit.toPlainString(), meterMngId, meterId));
            }
        }
        // 上記以外の場合、全許可（エラーなし）
        return false;
    }

    /**
     * 一括登録の有無を判定してエラー出力
     *
     * @param lumpRegistFlg 一括登録判定フラグ
     * @param errorSb 一括登録でポップアップ画面に出力するエラー表示
     * @param componentClientId 対象画面のクライアントID
     * @param message エラー表示
     * @return エラー出力結果
     */
    private boolean exportError(boolean lumpRegistFlg, StringJoiner errorSb, String componentClientId, String message) {
        if (lumpRegistFlg) {
            errorSb.add(message);
        } else {
            logAndNotifyError(message);
            if (componentClientId != null) {
                invalidComponent.add("smsCollectSettingMeterMeterManagementBean:smartSettingEditForm:" + componentClientId);
            }
        }
        return true;
    }

    /**
     * エラーログと画面の両方に出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logAndNotifyError(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.error(msg);
        addErrorMessage(msg);
    }

    /**
     * イベントログと画面の両方に出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logAndNotifyInfo(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.info(msg);
        addMessage(msg);
    }

    /**
     * メーター状態登録画面 登録ボタン押下時の処理
     * @param meterStatusProperty
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    @Logged
    public void execMeterStatusRegist(MeterStatusProperty meterStatusProperty)
        throws IllegalAccessException, InvocationTargetException, ParseException {
            System.out.println("------------------------- execMeterStatusRegist -------------------------");
            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execMeterStatusRegist():START"));
            UpdateSmsMeterAlertResponse response = new UpdateSmsMeterAlertResponse();
            UpdateSmsMeterAlertParameter parameter = new UpdateSmsMeterAlertParameter();

            parameter.setBean("UpdateSmsMeterAlertBean");
            UpdateSmsMeterAlertRequest request = new UpdateSmsMeterAlertRequest();

            //メーター状況をセット
            meterPresentSituation = new BigDecimal(meterStatusProperty.getMeterPresentSituationDisp());
            request.setMeterPresSitu(meterPresentSituation);

            //アラート停止フラグ、停止期間をセット
            if (meterStatusProperty.getAlertPauseFlgBool()) {
                alertPauseFlg = new BigDecimal("1");
                request.setAlertPauseFlg(alertPauseFlg);
                //エラーがある場合処理終了
                if (checkMeterStatusValidation(meterStatusProperty)) {
                    String firstStr = meterStatusProperty.getMeterId().substring(0, 1);
                    String componentSmart = "smsCollectSettingMeterMeterManagementBean:smartMeterStatusForm:s7";
                    String componentPulse = "smsCollectSettingMeterMeterManagementBean:pulseMeterStatusForm:p7";
                    String componentIotR = "smsCollectSettingMeterMeterManagementBean:iotRMeterStatusForm:w7";
                    // エラーならカレンダーの日付選択をリセットする
                    if (apsErrorFlg) {
                        meterStatusProperty.setCalAlertPauseStart(null);
                        if (METER_KIND.SMART.getVal().equals(firstStr)) {
                            invalidComponent.add(componentSmart + "AlertPauseStart");
                        } else if (METER_KIND.PULSE.getVal().equals(firstStr)) {
                            invalidComponent.add(componentPulse + "AlertPauseStart");
                        } else {
                            invalidComponent.add(componentIotR + "AlertPauseStart");
                        }
                    }
                    if (apeErrorFlg) {
                        meterStatusProperty.setCalAlertPauseEnd(null);
                        if (METER_KIND.SMART.getVal().equals(firstStr)) {
                            invalidComponent.add(componentSmart + "AlertPauseEnd");
                        } else if (METER_KIND.PULSE.getVal().equals(firstStr)){
                            invalidComponent.add(componentPulse + "AlertPauseEnd");
                        } else {
                            invalidComponent.add(componentIotR + "AlertPauseEnd");
                        }
                    }
                    return;
                }
                request.setAlertPauseStart(alertPauseStart);
                request.setAlertPauseEnd(alertPauseEnd);

            } else {
                alertPauseFlg = new BigDecimal("0");
                request.setAlertPauseFlg(alertPauseFlg);
                request.setAlertPauseStart(null);
                request.setAlertPauseEnd(null);
            }

            request.setMeterStaMemo(meterStatusProperty.getMeterStatusMemo());

            //チェックを付けたメーター全てに変更を行う
            List<UpdateSmsMeterAlertRequestSet> requestSetList = new ArrayList<>();
            for (MeterInfo meterInfo : meterList) {
                if (meterInfo.getCheckBox()) {
                    UpdateSmsMeterAlertRequestSet requestSet = new UpdateSmsMeterAlertRequestSet();
                    requestSet.setMeterMngId(meterInfo.getMeterMngId());
                    requestSet.setDevId(smartProperty.getDevId());
                    requestSet.setVersion(meterInfo.getVersion());
                    requestSetList.add(requestSet);
                }
            }
            request.setRequestList(requestSetList);

            parameter.setResult(request);

            SmsApiGateway gateway = new SmsApiGateway();

            response = (UpdateSmsMeterAlertResponse) gateway.osolApiPost(
                    osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                    SmsApiGateway.PATH.JSON,
                    parameter,
                    response);

            if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
                // 登録失敗→メッセージをセットしてreturn
                if (CheckUtility.isNullOrEmpty(response.getError())) {
                    addErrorMessage(
                            beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
                } else {
                    addErrorMessage(response.getError());
                }
                return;
            }

            //keepDevId = smartProperty.getDevId();
            // 画面初期化
            reload();

            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));

            eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                    .concat(" smsCollectSettingMeterMeterManagementBean:execMeterStatusRegist():END"));
    }

    /**
     * 入力チェックを行う
     * @param input チェック対象のオブジェクト
     * @param registFlg 登録フラグ  true:新規登録  false:更新
     * @param updateType
     * @return チェックエラー true:あり, false:なし
     */
    private boolean checkValidation(MeterManagementInputProperty input, boolean registFlg, String updateType) {
        boolean errorFlg = false;
        invalidComponent.clear();
        String formName = "";
        String itemNameInit = "";
        if (METER_KIND.SMART.getVal().equals(meterKind)) {
            formName = "smart";
            itemNameInit = "s";
        } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
            formName = "pulse";
            itemNameInit = "p";
        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            formName = "iotR";
            itemNameInit = "w";
        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            formName = "handy";
            itemNameInit = "h";
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            formName = "ocr";
            itemNameInit = "o";
        }
        if (registFlg) {
            formName = formName + "RegistForm:";
            itemNameInit = itemNameInit + "2";
        } else {
            if (MeterManagementConstants.UPDATE_TYPE_REGIST.equals(updateType)) {
                formName = formName + "EditForm:";
                itemNameInit = itemNameInit + "4";
            } else if (MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType)) {
                formName = formName + "SettingEditForm:";
                itemNameInit = itemNameInit + "5";
            }
        }
        String componentName = "smsCollectSettingMeterMeterManagementBean:" + formName + itemNameInit;

        if (registFlg || MeterManagementConstants.UPDATE_TYPE_REGIST.equals(updateType)) {
            // ■ユーザコード
            if (!CheckUtility.isNullOrEmpty(input.getTenantId())) {
                // 文字種別チェック
                if (!CheckUtility.checkRegex(input.getTenantId(), MeterManagementConstants.CHECK_PATTERN_NUM)) {
                    addErrorMessage(
                            beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.buildingId.type"));
                    invalidComponent.add(componentName + "UserCode");
                    errorFlg = true;
                } else {
                    // 範囲チェック
                    if (!CheckUtility.checkIntegerRange(input.getTenantId(), MeterManagementConstants.CHECK_BUILDING_ID_MIN,
                            MeterManagementConstants.CHECK_BUILDING_ID_MAX)) {
                        String[] parm = { Integer.toString(MeterManagementConstants.CHECK_BUILDING_ID_MIN),
                                Integer.toString(MeterManagementConstants.CHECK_BUILDING_ID_MAX) };
                        addErrorMessage(beanMessages
                                .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.buildingId.range", parm));
                        invalidComponent.add(componentName + "UserCode");
                        errorFlg = true;
                    }
                }
            }

            // ■計器ID
            // 未入力チェック
            if (CheckUtility.isNullOrEmpty(input.getMeterId())) {
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.meterId.required"));
                invalidComponent.add(componentName + "EquipId");
                errorFlg = true;
            } else {
                // 新規登録の場合
                if(registFlg) {
                    // 重複チェック
                    String errorStr = meterIdDuplicateCheck(String.valueOf(input.getMeterMngId()), input.getMeterId());
                    if (!CheckUtility.isNullOrEmpty(errorStr)) {
                        addErrorMessage(errorStr);
                        invalidComponent.add(componentName + "EquipId");
                        errorFlg = true;
                    }
                }

                // 文字種別チェック
                if (!CheckUtility.checkRegex(input.getMeterId(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                    addErrorMessage(
                            beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.meterId.type"));
                    invalidComponent.add(componentName + "EquipId");
                    errorFlg = true;
                }

                // 文字数チェック
                if (input.getMeterId().length() != MeterManagementConstants.CHECK_METER_ID_CNT) {
                    String[] parm = { Integer.toString(MeterManagementConstants.CHECK_METER_ID_CNT) };
                    addErrorMessage(beanMessages
                            .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.meterId.count", parm));
                    invalidComponent.add(componentName + "EquipId");
                    errorFlg = true;
                }

                // 頭文字チェック
                String firstStr = input.getMeterId().substring(0, 1);
                // スマートの登録/更新時のチェック
                if (METER_KIND.SMART.getVal().equals(meterKind)) {
                    if (!MeterManagementConstants.CHECK_METER_ID_INITIAL_A.equals(firstStr)) {
                        if (MeterManagementConstants.CHECK_METER_ID_INITIAL_R.equals(firstStr)) {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_R };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern11", parm));
                            addErrorMessage(beanMessages.getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern21"));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        } else if (MeterManagementConstants.CHECK_METER_ID_INITIAL_P.equals(firstStr)) {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_P };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern11", parm));
                            addErrorMessage(beanMessages.getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern22"));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        } else {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_A };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern12", parm));
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern23", parm));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        }

                    }
                }

                // パルスの登録/更新時のチェック
                if (METER_KIND.PULSE.getVal().equals(meterKind)) {
                    if (!MeterManagementConstants.CHECK_METER_ID_INITIAL_P.equals(firstStr)) {
                        if (MeterManagementConstants.CHECK_METER_ID_INITIAL_R.equals(firstStr)) {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_R };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern11", parm));
                            addErrorMessage(beanMessages.getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern21"));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        } else if (MeterManagementConstants.CHECK_METER_ID_INITIAL_A.equals(firstStr)) {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_A };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern11", parm));
                            addErrorMessage(beanMessages.getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern24"));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        } else {
                            String[] parm = { MeterManagementConstants.CHECK_METER_ID_INITIAL_P };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern12", parm));
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.meterId.initial.pattern23", parm));
                            invalidComponent.add(componentName + "EquipId");
                            errorFlg = true;
                        }
                    }
                }
            }
        }

        // 「スマートメーター」のチェック処理
        if (METER_KIND.SMART.getVal().equals(meterKind)) {

            // LTE-M かつ 更新の場合
            if (lteMDeviceFlg & !registFlg) {
                // メーターの定格電流を基に負荷電流の判定
                List<MeterInfo> targetMeterList = meterList.stream()
                        .filter(m -> Boolean.TRUE.equals(m.getCheckBox())) // チェック付与したメーターリストを取得
                        .collect(Collectors.toList());

                for (MeterInfo info : targetMeterList) {
                    // 負荷制限API実行の場合、入力欄のNULLチェック
                    if (input.isSendFlgLoadlimit() && checkNullOrEmptyLteM(input, info)) {
                        errorFlg = true;
                        continue;
                    }

                    MMeterInfo mMeterInfo = new MMeterInfo();
                    mMeterInfo.setMeterId(info.getMeterId());
                    List<MMeterInfo> mMeterInfoList = mMeterInfoDao.getMMeterInfoList(mMeterInfo);
                    // メータID(計器ID)でのDB取得結果を判定
                    if (mMeterInfoList == null || mMeterInfoList.isEmpty()) {
                        logAndNotifyError("計器%s は、サーバと計器間の通信が確立されていないため実行できません。下記、一覧表にて該当メーターのチェックを外して再度「実行」ボタンを押下してください。", info.getMeterId());
                        errorFlg = true;
                    } else if (mMeterInfoList.size() > 1) {
                        logAndNotifyError("計器の定格電流の情報が重複しております。 メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                        errorFlg = true;
                    } else if (mMeterInfoList.size() == 1) {
                        // カラム「switch_code(開閉コード)」で判定
                        BigDecimal sw = mMeterInfoList.get(0).getSwitchCode();
                        if (sw == null) {
                            logAndNotifyError(String.format("計器の開閉コード情報が存在しないため設定内容変更できません。  メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                    , info.getMeterMngId()
                                    , info.getMeterId()
                                    , sw));
                            errorFlg = true;
                            continue;
                        } else if (sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.NONE.getCode())) == 0) {
                            logAndNotifyError(String.format("開閉器のない計器のため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                    , info.getMeterMngId()
                                    , info.getMeterId()
                                    , sw));
                            errorFlg = true;
                            continue;
                        } else if ((sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                                || sw.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)
                                && getSmartSettingUpdateProperty().isSendFlgLoadlimit()) {
                            logAndNotifyError(String.format("計器の負荷制限の設定が有効になっていないため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                                    , info.getMeterMngId()
                                    , info.getMeterId()
                                    , sw));
                            errorFlg = true;
                            continue;
                        }

                        // 定格電流を取得
                        BigDecimal ratedCurrent = mMeterInfoList.get(0).getRatedCurrent();
                        if (ratedCurrent == null) {
                            logAndNotifyError("計器の定格電流が取得できません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                            errorFlg = true;
                        }
                        // 定格電流を基に負荷電流の判定実施
                        boolean loadCurrentErrorFlg = false;
                        if (Objects.equals(LOAD_LIMIT_MODE.TEMP_VALID.getVal(), input.getLoadlimitMode())) {
                            // 負荷制限「臨時設定」の場合
                            loadCurrentErrorFlg = isLoadCurrentError(MeterManagementConstants.LOADLIMIT_MODE_DISP.TEMP.getValue()
                                    , input.getTempLoadCurrent()
                                    , ratedCurrent
                                    , String.valueOf(info.getMeterMngId())
                                    , info.getMeterId()
                                    , "s5TmpCurlimitSelectLteM"
                                    , null
                                    , false);
                        } else if (Objects.equals(LOAD_LIMIT_MODE.INVALID.getVal(), input.getLoadlimitMode())
                                || Objects.equals(LOAD_LIMIT_MODE.VALID.getVal(), input.getLoadlimitMode())) {
                            // 負荷制限「なし」「あり」の場合
                            loadCurrentErrorFlg = isLoadCurrentError(MeterManagementConstants.LOADLIMIT_MODE_DISP.BASIC.getValue()
                                    , input.getLoadCurrent()
                                    , ratedCurrent
                                    , String.valueOf(info.getMeterMngId())
                                    , info.getMeterId()
                                    , "s5BasicCurlimitSelectLteM"
                                    , null
                                    , false);
                        } else {
                            // 負荷制限「臨時解除」の場合 負荷電流は固定値(1A)を設定
                            loadCurrentErrorFlg = isLoadCurrentError(MeterManagementConstants.LOADLIMIT_MODE_DISP.TEMP.getValue()
                                    , String.valueOf(MeterManagementConstants.FIXED_LOAD_CURRENT)
                                    , ratedCurrent
                                    , String.valueOf(info.getMeterMngId())
                                    , info.getMeterId()
                                    , "s5TmpCurlimitSelectLteM"
                                    , null
                                    , false);
                        }
                        if (loadCurrentErrorFlg) {
                            errorFlg = true;
                        }
                    } else {
                        logAndNotifyError("メータID(計器ID)を基にDBのテーブル「m_meter_info」を検索しましたが、想定外の取得結果です。 メーター管理番号:%s メータID:%s", info.getMeterMngId(), info.getMeterId());
                        errorFlg = true;
                    }
                }
            }

            if (registFlg || MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType)) {
                // ■自動投入(基本設定)
                if (!CheckUtility.isNullOrEmpty(input.getAutoInjection())) {
                    // 文字種別チェック
                    if (!CheckUtility.checkRegex(input.getAutoInjection(), MeterManagementConstants.CHECK_PATTERN_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.autoInjection.type"));
                        invalidComponent.add(componentName + (lteMDeviceFlg ? "BasicAutorecLteM" : "BasicAutorec"));
                        errorFlg = true;
                    } else {
                        // 範囲チェック
                        if (!"0".equals(input.getAutoInjection()) &&
                                !CheckUtility.checkIntegerRange(input.getAutoInjection(),
                                        MeterManagementConstants.CHECK_AUTO_INJECTION_MIN,
                                        MeterManagementConstants.CHECK_AUTO_INJECTION_MAX)) {
                            String[] parm = { Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MIN),
                                    Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MAX) };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.autoInjection.range", parm));
                            invalidComponent.add(componentName + (lteMDeviceFlg ? "BasicAutorecLteM" : "BasicAutorec"));
                            errorFlg = true;
                        }
                    }
                }

                // ■自動投入(臨時設定)
                if (!CheckUtility.isNullOrEmpty(input.getTempAutoInjection())) {
                    // 文字種別チェック
                    if (!CheckUtility.checkRegex(input.getTempAutoInjection(),
                            MeterManagementConstants.CHECK_PATTERN_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.tempAutoInjection.type"));
                        invalidComponent.add(componentName + (lteMDeviceFlg ? "TmpAutorecLteM" : "TmpAutorec"));
                        errorFlg = true;
                    } else {
                        // 範囲チェック
                        if (!"0".equals(input.getTempAutoInjection()) &&
                                !CheckUtility.checkIntegerRange(input.getTempAutoInjection(),
                                        MeterManagementConstants.CHECK_AUTO_INJECTION_MIN,
                                        MeterManagementConstants.CHECK_AUTO_INJECTION_MAX)) {
                            String[] parm = { Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MIN),
                                    Integer.toString(MeterManagementConstants.CHECK_AUTO_INJECTION_MAX) };
                            addErrorMessage(beanMessages.getMessageFormat(
                                    "smsCollectSettingMeterMeterManagementBean.error.tempAutoInjection.range", parm));
                            invalidComponent.add(componentName + (lteMDeviceFlg ? "TmpAutorecLteM" : "TmpAutorec"));
                            errorFlg = true;
                        }
                    }
                }
            }
        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // 「OCR検針」→「AieLink」へ変更
            // 「AieLink用メーター」のチェック処理
            // ■乗率
            if (!CheckUtility.isNullOrEmpty(input.getMulti())) {
                if (!CheckUtility.checkRegex(input.getMulti().toString(), MeterManagementConstants.CHECK_PATTERN_NUM)) {
                    addErrorMessage(
                            beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.multi.type"));
                    invalidComponent.add(componentName + "Multi");
                    errorFlg = true;
                } else {
                    // 範囲チェック
                    if (!CheckUtility.checkIntegerRange(input.getMulti().toString(),
                            MeterManagementConstants.CHECK_MULTI_MIN,
                            MeterManagementConstants.CHECK_MULTI_MAX)) {
                        String[] parm = { Integer.toString(MeterManagementConstants.CHECK_MULTI_MIN),
                                Integer.toString(MeterManagementConstants.CHECK_MULTI_MAX) };
                        addErrorMessage(beanMessages
                                .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.multi.range", parm));
                        invalidComponent.add(componentName + "Multi");
                        errorFlg = true;
                    }
                }
            }

        }  else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // 「IoT-R連携用メーター」のチェック処理
            // 「ユーザーコード」「計器ID」「検満年月」以外のチェック項目がある場合は記載する

        } else {
            // 「パルスメーター」または「ハンディー検針用メーター」のチェック処理
            if (
                    registFlg ||
                    (
                        MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType) &&
                        METER_KIND.PULSE.getVal().equals(meterKind)
                    ) ||
                    (
                        MeterManagementConstants.UPDATE_TYPE_REGIST.equals(updateType) &&
                        METER_KIND.HANDY.getVal().equals(meterKind)
                    )
                ) {
                // スマート以外の項目
                // ■乗率
                if (Objects.isNull(input.getMulti())) {
                        // パルスの登録/更新時のチェック
                        // 必須チェック
                        addErrorMessage(
                                beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.multi.required"));
                        invalidComponent.add(componentName + "Multi");
                        errorFlg = true;
                } else {// 文字種別チェック
                    if (!CheckUtility.checkRegex(input.getMulti().toString(), MeterManagementConstants.CHECK_PATTERN_NUM)) {
                        addErrorMessage(
                                beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.multi.type"));
                        invalidComponent.add(componentName + "Multi");
                        errorFlg = true;
                    } else {
                        // 範囲チェック
                        if (!CheckUtility.checkIntegerRange(input.getMulti().toString(),
                                MeterManagementConstants.CHECK_MULTI_MIN,
                                MeterManagementConstants.CHECK_MULTI_MAX)) {
                            String[] parm = { Integer.toString(MeterManagementConstants.CHECK_MULTI_MIN),
                                    Integer.toString(MeterManagementConstants.CHECK_MULTI_MAX) };
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.multi.range", parm));
                            invalidComponent.add(componentName + "Multi");
                            errorFlg = true;
                        }
                    }
                }
            }
        }

        if (registFlg || MeterManagementConstants.UPDATE_TYPE_SETTING.equals(updateType)) {
            // ■パルス重み
            if (input.getPulseWeightChg()) {
                if (!Objects.isNull(input.getPulseWeight())) {
                    // 文字種別チェック
                    if (!CheckUtility.checkRegex(input.getPulseWeight().toString(),
                            MeterManagementConstants.CHECK_PATTERN_NUM)) {
                        addErrorMessage(
                                beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.pulseWeight.type"));
                        invalidComponent.add(componentName + "PulseWeight");
                        errorFlg = true;
                    } else {
                        // 範囲チェック
                        if (!CheckUtility.checkIntegerRange(input.getPulseWeight().toString(),
                                MeterManagementConstants.CHECK_PULSE_WEIGHT_MIN, MeterManagementConstants.CHECK_PULSE_WEIGHT_MAX)) {
                            String[] parm = { Integer.toString(MeterManagementConstants.CHECK_PULSE_WEIGHT_MIN),
                                    Integer.toString(MeterManagementConstants.CHECK_PULSE_WEIGHT_MAX) };
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pulseWeight.range", parm));
                            invalidComponent.add(componentName + "PulseWeight");
                            errorFlg = true;
                        }
                    }
                }
            }

            // ■指針値
            if (input.getCurrentDataChg()) {
                if (!Objects.isNull(input.getCurrentData())) {
                    // 文字種別チェック
                    if (!CheckUtility.checkRegex(input.getCurrentData().toString(),
                            MeterManagementConstants.CHECK_PATTERN_NUM)) {
                        addErrorMessage(
                                beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.currentData.type"));
                        invalidComponent.add(componentName + "CurrentData");
                        errorFlg = true;
                    } else {
                        // 範囲チェック
                        if (!CheckUtility.checkIntegerRange(input.getCurrentData().toString(),
                                MeterManagementConstants.CHECK_CURRENT_DATA_MIN, MeterManagementConstants.CHECK_CURRENT_DATA_MAX)) {
                            String[] parm = { Integer.toString(MeterManagementConstants.CHECK_CURRENT_DATA_MIN),
                                    Integer.toString(MeterManagementConstants.CHECK_CURRENT_DATA_MAX) };
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.currentData.range", parm));
                            invalidComponent.add(componentName + "CurrentData");
                            errorFlg = true;
                        }
                    }
                }
            }
        }

        if (registFlg || MeterManagementConstants.UPDATE_TYPE_REGIST.equals(updateType)) {

            // handyのみ
            if (METER_KIND.HANDY.getVal().equals(meterKind)) {
                // 無線ID
                // 必須チェック
                if (CheckUtility.isNullOrEmpty(input.getWirelessId())) {
                    addErrorMessage(beanMessages
                            .getMessage("smsCollectSettingMeterMeterManagementBean.error.wirelessId.required"));
                    invalidComponent.add(componentName + "WirelessId");
                    errorFlg = true;
                } else {
                    // 文字種チェック
                    if (!CheckUtility.checkRegex(input.getWirelessId(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.wirelessId.type"));
                        invalidComponent.add(componentName + "WirelessId");
                        errorFlg = true;
                    } else {
                        // 文字数チェック
                        if (input.getWirelessId().length() != 12) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.wirelessId.count", "12"));
                            invalidComponent.add(componentName + "WirelessId");
                            errorFlg = true;
                        }
                    }
                }

                boolean errHopId = false;
                // リレー無線ID1
                if (!CheckUtility.isNullOrEmpty(input.getHop1Id())) {
                    // 文字種チェック
                    if (!CheckUtility.checkRegex(input.getHop1Id(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.hop1Id.type"));
                        invalidComponent.add(componentName + "Hop1Id");
                        errorFlg = true;
                        errHopId = true;
                    } else {
                        // 文字数チェック
                        if (input.getHop1Id().length() != 12) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop1Id.count", "12"));
                            invalidComponent.add(componentName + "Hop1Id");
                            errorFlg = true;
                            errHopId = true;
                        }
                    }
                }

                // リレー無線ID2
                if (!CheckUtility.isNullOrEmpty(input.getHop2Id())) {
                    // 文字種チェック
                    if (!CheckUtility.checkRegex(input.getHop2Id(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.hop2Id.type"));
                        invalidComponent.add(componentName + "Hop2Id");
                        errorFlg = true;
                        errHopId = true;
                    } else {
                        // 文字数チェック
                        if (input.getHop2Id().length() != 12) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop2Id.count", "12"));
                            invalidComponent.add(componentName + "Hop2Id");
                            errorFlg = true;
                            errHopId = true;
                        }
                    }
                }

                // リレー無線ID3
                if (!CheckUtility.isNullOrEmpty(input.getHop3Id())) {
                    // 文字種チェック
                    if (!CheckUtility.checkRegex(input.getHop3Id(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.hop3Id.type"));
                        invalidComponent.add(componentName + "Hop3Id");
                        errorFlg = true;
                        errHopId = true;
                    } else {
                        // 文字数チェック
                        if (input.getHop3Id().length() != 12) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.hop3Id.count", "12"));
                            invalidComponent.add(componentName + "Hop3Id");
                            errorFlg = true;
                            errHopId = true;
                        }
                    }
                }

                // リレー無線IDの重複チェック
                if (!errHopId) {
                    boolean errDuplicateHopId = false;
                    if (!CheckUtility.isNullOrEmpty(input.getHop1Id()) && !CheckUtility.isNullOrEmpty(input.getHop2Id()) && input.getHop1Id().equals(input.getHop2Id())) {
                        invalidComponent.add(componentName + "Hop1Id");
                        invalidComponent.add(componentName + "Hop2Id");
                        errDuplicateHopId = true;
                        errorFlg = true;
                    }
                    if (!CheckUtility.isNullOrEmpty(input.getHop1Id()) && !CheckUtility.isNullOrEmpty(input.getHop3Id()) && input.getHop1Id().equals(input.getHop3Id())) {
                        invalidComponent.add(componentName + "Hop1Id");
                        invalidComponent.add(componentName + "Hop3Id");
                        errDuplicateHopId = true;
                        errorFlg = true;
                    }
                    if (!CheckUtility.isNullOrEmpty(input.getHop2Id()) && !CheckUtility.isNullOrEmpty(input.getHop3Id()) && input.getHop2Id().equals(input.getHop3Id())) {
                        invalidComponent.add(componentName + "Hop2Id");
                        invalidComponent.add(componentName + "Hop3Id");
                        errDuplicateHopId = true;
                        errorFlg = true;
                    }

                    if (errDuplicateHopId) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.hopId.duplicate"));
                    }
                }

                // ポーリンググループNo
                if (!CheckUtility.isNullOrEmpty(input.getPollingId())) {
                    // 文字種チェック
                    if (!CheckUtility.checkRegex(input.getPollingId(), MeterManagementConstants.CHECK_PATTERN_ALPHABET_NUM)) {
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.pollingId.type"));
                        invalidComponent.add(componentName + "PollingId");
                        errorFlg = true;
                    } else {
                        // 文字数チェック
                        if (input.getPollingId().length() != 12) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.pollingId.count", "12"));
                            invalidComponent.add(componentName + "PollingId");
                            errorFlg = true;
                        }
                    }
                }
            }


            // ■検満年月
            String year = input.getExamEndYear();
            String month = input.getExamEndMonth();


            //ラジオボタンがアクティブであればチェックする
            if(!CheckUtility.isNullOrEmpty(input.getDispYearFlg())) {
                if (MeterManagementConstants.FLG_OFF.equals(input.getDispYearFlg())) {
                    // 西暦選択時のチェック
                    boolean checkFlgYear = false;
                    boolean checkFlgMonth = false;
                    if (CheckUtility.isNullOrEmpty(year) && !CheckUtility.isNullOrEmpty(month)) {
                        // 年が未入力
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYear.required"));
                        invalidComponent.add(componentName + "Year");
                        checkFlgMonth = true;
                        errorFlg = true;
                    } else if (!CheckUtility.isNullOrEmpty(year) && CheckUtility.isNullOrEmpty(month)) {
                        // 月が未入力
                        addErrorMessage(beanMessages
                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.required"));
                        invalidComponent.add(componentName + "Month");
                        checkFlgYear = true;
                        errorFlg = true;
                    } else if (!CheckUtility.isNullOrEmpty(year) && !CheckUtility.isNullOrEmpty(month)) {
                        // 年月どちらも入力済
                        checkFlgYear = true;
                        checkFlgMonth = true;
                    }

                    // 年のチェックが必要
                    if (checkFlgYear) {
                        // 文字種別チェック
                        if (!CheckUtility.checkRegex(year, MeterManagementConstants.CHECK_PATTERN_NUM)) {
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYear.type"));
                            invalidComponent.add(componentName + "Year");
                            errorFlg = true;
                        }
                        // 範囲チェック
                        int minYear = 1989;
                        int maxYear = 9999;
                        if (!CheckUtility.checkIntegerRange(year, minYear, maxYear)) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat("smsCollectSettingMeterMeterManagementBean.error.examEndYear.range0", new String[] {String.valueOf(minYear), String.valueOf(maxYear)}));
                            invalidComponent.add(componentName + "Year");
                            errorFlg = true;
                        }
                    }

                    // 月のチェックが必要
                    if (checkFlgMonth) {
                        // 文字種別チェック
                        if (!CheckUtility.checkRegex(month, MeterManagementConstants.CHECK_PATTERN_NUM)) {
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.type"));
                            invalidComponent.add(componentName + "Month");
                            errorFlg = true;
                        }
                        // 範囲チェック
                        if (!CheckUtility.checkIntegerRange(month, 1, 12)) {
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.range"));
                            invalidComponent.add(componentName + "Month");
                            errorFlg = true;
                        }
                    }
                } else {
                    // 和暦選択時のチェック
                    year = input.getExamEndYearWareki();
                    String gengo = input.getExamEndYmGengo();
                    boolean warekiYearErrFlg = false;
                    boolean warekiMonthErrFlg = false;

                    if (!MeterManagementConstants.FLG_OFF.equals(gengo)) {
                        // 元号が選択されている場合
                        // ◇ 年のチェック
                        if (CheckUtility.isNullOrEmpty(year)) {
                            // 未入力チェック
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYear.required"));
                            invalidComponent.add(componentName + "YearWareki");
                            errorFlg = true;
                            warekiYearErrFlg = true;
                        } else {
                            if (!CheckUtility.checkRegex(year, MeterManagementConstants.CHECK_PATTERN_NUM)) {
                                addErrorMessage(beanMessages
                                        .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYear.type"));
                                invalidComponent.add(componentName + "YearWareki");
                                errorFlg = true;
                                warekiYearErrFlg = true;
                            }
                            // 範囲チェック
                            if (!warekiYearErrFlg && !CheckUtility.checkIntegerRange(year, 1, 99)) {
                                addErrorMessage(beanMessages
                                        .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYear.range1"));
                                invalidComponent.add(componentName + "YearWareki");
                                errorFlg = true;
                                warekiYearErrFlg = true;
                            }
                        }
                        // ◇ 月のチェック
                        if (CheckUtility.isNullOrEmpty(month)) {
                            // 未入力チェック
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.required"));
                            invalidComponent.add(componentName + "Month");
                            errorFlg = true;
                            warekiMonthErrFlg = true;
                        } else {
                            // 文字種別チェック
                            if (!CheckUtility.checkRegex(month, MeterManagementConstants.CHECK_PATTERN_NUM)) {
                                addErrorMessage(beanMessages
                                        .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.type"));
                                invalidComponent.add(componentName + "Month");
                                errorFlg = true;
                                warekiMonthErrFlg = true;
                            }
                            // 範囲チェック
                            if (!warekiMonthErrFlg && !CheckUtility.checkIntegerRange(month, 1, 12)) {
                                addErrorMessage(beanMessages
                                        .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndMonth.range"));
                                invalidComponent.add(componentName + "Month");
                                errorFlg = true;
                                warekiMonthErrFlg = true;
                            }
                        }
                        if (!warekiYearErrFlg && !warekiMonthErrFlg) {
                            // 年月が入力されている場合、元号 × 年月 の妥当性チェックを実施する
                            if (!CheckUtility.isNullOrEmpty(year) && !CheckUtility.isNullOrEmpty(month)) {
                                if (MeterManagementConstants.DISP_YEAR_GENGO_CODE_H.equals(gengo)) {
                                    if ((Integer.parseInt(year) == 31 && Integer.parseInt(month) > 4) || Integer.parseInt(year) > 31) {
                                        // 平成の場合、31年4月がMAX
                                        addErrorMessage(beanMessages
                                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYm.range1"));
                                        invalidComponent.add(componentName + "GengoSelect");
                                        invalidComponent.add(componentName + "YearWareki");
                                        invalidComponent.add(componentName + "Month");
                                        errorFlg = true;
                                    }
                                } else {
                                    if (Integer.parseInt(year) == 1 && Integer.parseInt(month) < 5) {
                                        // 令和の場合、1年5月がMIN
                                        addErrorMessage(beanMessages
                                                .getMessage("smsCollectSettingMeterMeterManagementBean.error.examEndYm.range2"));
                                        invalidComponent.add(componentName + "GengoSelect");
                                        invalidComponent.add(componentName + "YearWareki");
                                        invalidComponent.add(componentName + "Month");
                                        errorFlg = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if(!CheckUtility.isNullOrEmpty(year) || !CheckUtility.isNullOrEmpty(month)) {
                            addErrorMessage(beanMessages
                                    .getMessage("smsCollectSettingMeterMeterManagementBean.error.wareki.required"));
                            invalidComponent.add(componentName + "GengoSelect");
                            errorFlg = true;
                        }
                    }
                }
            }
        }
        return errorFlg;
    }

    /**
     * 設定内容変更にて画面入力値のNULLチェック
     *
     * @param input 画面入力値
     * @param info 更新対象値
     * @retur true:エラー  false:エラーではない
     */
    private boolean checkNullOrEmptyLteM(MeterManagementInputProperty input, MeterInfo info) {

        boolean errorFlg = false;
        if (Objects.equals(LOAD_LIMIT_MODE.TEMP_VALID.getVal(), input.getLoadlimitMode())) {
            // 負荷制限「臨時設定」の場合
            // 負荷電流(臨時設定)
            if (CheckUtility.isNullOrEmpty(input.getTempLoadCurrent())) {
                logAndNotifyError("入力された負荷電流(臨時設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 自動投入(臨時設定)
            if (CheckUtility.isNullOrEmpty(input.getTempLoadCurrent())) {
                logAndNotifyError("入力された自動投入(臨時設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 開閉器動作カウント(臨時設定)
            if (CheckUtility.isNullOrEmpty(input.getTempLoadCurrent())) {
                logAndNotifyError("入力された開閉器動作カウント(臨時設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 開閉器カウントクリア(臨時設定)
            if (CheckUtility.isNullOrEmpty(input.getTempLoadCurrent())) {
                logAndNotifyError("入力された開閉器カウントクリア(臨時設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
        } else if (Objects.equals(LOAD_LIMIT_MODE.INVALID.getVal(), input.getLoadlimitMode())
                || Objects.equals(LOAD_LIMIT_MODE.VALID.getVal(), input.getLoadlimitMode())) {
            // 負荷制限「なし」「あり」の場合
            // 負荷電流(基本設定)
            if (CheckUtility.isNullOrEmpty(input.getLoadCurrent())) {
                logAndNotifyError("入力された負荷電流(基本設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 自動投入(基本設定)
            if (CheckUtility.isNullOrEmpty(input.getLoadCurrent())) {
                logAndNotifyError("入力された自動投入(基本設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 開閉器動作カウント(基本設定)
            if (CheckUtility.isNullOrEmpty(input.getLoadCurrent())) {
                logAndNotifyError("入力された開閉器動作カウント(基本設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
            // 開閉器カウントクリア(基本設定)
            if (CheckUtility.isNullOrEmpty(input.getLoadCurrent())) {
                logAndNotifyError("入力された開閉器カウントクリア(基本設定)の値がありません。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
                errorFlg = true;
            }
        } else if (Objects.equals(LOAD_LIMIT_MODE.TEMP_INVALID.getVal(), input.getLoadlimitMode())) {
            // 負荷制限「臨時解除」の場合
            // 固定値を送るためそのまま継続
        } else {
            // 負荷制限が上記以外の場合
            logAndNotifyError("負荷制限を選択してください。  メーター管理番号:%s 計器ID:%s", info.getMeterMngId(), info.getMeterId());
            errorFlg = true;
        }
        return errorFlg;
    }

    /**
     * 入力チェックを行う(アラート停止期間)
     * @param meterStatusProperty
     * @return errorFlg
     * @throws ParseException
     */
    private boolean checkMeterStatusValidation(MeterStatusProperty meterStatusProperty) throws ParseException {
        invalidComponent.clear();
        boolean errorFlg = false;
        apsErrorFlg = false;
        apeErrorFlg = false;

        // ●アラート停止期間
        alertPauseStart = meterStatusProperty.getAlertPauseStart();
        alertPauseEnd = meterStatusProperty.getAlertPauseEnd();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        if (!CheckUtility.isNullOrEmpty(alertPauseStart) || !CheckUtility.isNullOrEmpty(alertPauseEnd)) {
            if (CheckUtility.isNullOrEmpty(alertPauseStart) && !CheckUtility.isNullOrEmpty(alertPauseEnd)) {
                // 開始日未入力チェック
                addErrorMessage(
                        beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.required"));
                errorFlg = true;
                apsErrorFlg = true;
            } else {
                // アラート停止期間 開始日
                try {
                    Date apsDate = sdf.parse(alertPauseStart);
                    alertPauseStart = alertPauseStart.replace("/", "");
                    String apsYear = alertPauseStart.substring(0, 4);
                    String apsMonth = alertPauseStart.substring(4, 6);
                    String apsDay = alertPauseStart.substring(6, 8);
                    // 月の範囲チェック
                    if (!errorFlg) {
                        if (!CheckUtility.checkIntegerRange(apsMonth, 1, 12)) {
                            addErrorMessage(
                                    beanMessages.getMessage(
                                            "smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.range1"));
                            errorFlg = true;
                            apsErrorFlg = true;
                        } else {
                            // 日の範囲チェック
                            int maxDay[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
                            if (!CheckUtility.checkIntegerRange(apsDay, 1, maxDay[Integer.parseInt(apsMonth) - 1])) {
                                addErrorMessage(
                                        beanMessages.getMessage(
                                                "smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.range1"));
                                errorFlg = true;
                                apsErrorFlg = true;
                            } else {
                                // 2月末日チェック
                                if (apsMonth.equals("02") && apsDay.equals("29")) {
                                    int apsYearInt = Integer.parseInt(apsYear);
                                    boolean leapChk = false;
                                    if (apsYearInt % 4 == 0) {
                                        if ((apsYearInt % 100) == 0) {
                                            if ((apsYearInt % 400) == 0) {
                                                leapChk = true;
                                            }
                                        } else {
                                            leapChk = true;
                                        }
                                    }
                                    if (!leapChk) {
                                        addErrorMessage(
                                                beanMessages.getMessage(
                                                        "smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.range1"));
                                        errorFlg = true;
                                        apsErrorFlg = true;
                                    }
                                }
                            }
                        }
                    }
                    if (!errorFlg) {
                        // 年の範囲チェック
                        int minYear = 1989;
                        int maxYear = 9999;
                        if (!CheckUtility.checkIntegerRange(apsYear, minYear, maxYear)) {
                            addErrorMessage(beanMessages
                                    .getMessageFormat(
                                            "smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.range2",
                                            new String[] { String.valueOf(minYear), String.valueOf(maxYear) }));
                            errorFlg = true;
                            apsErrorFlg = true;
                        }
                    }
                } catch (ParseException e) {
                    // 日付変換エラー→フォーマットチェック
                    addErrorMessage(
                            beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.alertPauseStart.format"));
                    errorFlg = true;
                    apsErrorFlg = true;
                }
            }

            if (!CheckUtility.isNullOrEmpty(alertPauseEnd)) {
                // アラート停止期間 終了日
                try {
                    Date apeDate = sdf.parse(alertPauseEnd);
                    alertPauseEnd = alertPauseEnd.replace("/", "");
                    String apeYear = alertPauseEnd.substring(0, 4);
                    String apeMonth = alertPauseEnd.substring(4, 6);
                    String apeDay = alertPauseEnd.substring(6, 8);
                    // 月の範囲チェック
                    if (!apeErrorFlg) {
                        if (!CheckUtility.checkIntegerRange(apeMonth, 1, 12)) {
                            addErrorMessage(
                                beanMessages.getMessage(
                                        "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.range1"));
                            errorFlg = true;
                            apeErrorFlg = true;
                        } else {
                            // 日の範囲チェック
                            int maxDay[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
                            if (!CheckUtility.checkIntegerRange(apeDay, 1,
                                maxDay[Integer.parseInt(apeMonth) - 1])) {
                                addErrorMessage(
                                beanMessages.getMessage(
                                        "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.range1"));
                                errorFlg = true;
                                apeErrorFlg = true;
                            } else {
                                // 2月末日チェック
                                if (apeMonth.equals("02") && apeDay.equals("29")) {
                                    int apeYearInt = Integer.parseInt(apeYear);
                                    boolean leapChk = false;
                                    if (apeYearInt % 4 == 0) {
                                        if ((apeYearInt % 100) == 0) {
                                            if ((apeYearInt % 400) == 0) {
                                                leapChk = true;
                                            }
                                        } else {
                                            leapChk = true;
                                        }
                                    }
                                    if (!leapChk) {
                                        addErrorMessage(
                                                beanMessages.getMessage(
                                                        "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.range1"));
                                        errorFlg = true;
                                        apeErrorFlg = true;
                                    }
                                }
                            }
                        }
                    }
                    if (!apeErrorFlg) {
                        // 年の範囲チェック
                        int minYear = 1989;
                        int maxYear = 9999;
                        if (!CheckUtility.checkIntegerRange(apeYear, minYear, maxYear)) {
                            addErrorMessage(
                                    beanMessages.getMessageFormat(
                                            "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.range2",
                                            new String[] { String.valueOf(minYear), String.valueOf(maxYear) }));
                            errorFlg = true;
                            apeErrorFlg = true;
                        }
                    }
                    if (!apeErrorFlg) {
                        // 現在日時との比較チェック
                        Date today = new Date();
                        String todayStr = sdf.format(today);
                        today = sdf.parse(todayStr);
                        if (apeDate.before(today) && !apeDate.equals(today)) {
                            addErrorMessage(
                                    beanMessages.getMessage(
                                            "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.range3"));
                            errorFlg = true;
                            apeErrorFlg = true;
                        }
                    }
                } catch (ParseException e) {
                    // 日付変換エラー→フォーマットチェック
                    addErrorMessage(
                            beanMessages.getMessage(
                                    "smsCollectSettingMeterMeterManagementBean.error.alertPauseEnd.format"));
                    errorFlg = true;
                    apeErrorFlg = true;
                }
            }

            if (!errorFlg) {
                if (!CheckUtility.isNullOrEmpty(alertPauseStart) && !CheckUtility.isNullOrEmpty(alertPauseEnd)) {
                    sdf = new SimpleDateFormat("yyyyMMdd");
                    Date apsDate = sdf.parse(alertPauseStart);
                    Date apeDate = sdf.parse(alertPauseEnd);
                    // 終了日が開始日以前の場合
                    if (apeDate.before(apsDate)) {
                        addErrorMessage(
                                beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.error.alertPause.range"));
                        errorFlg = true;
                        apsErrorFlg = true;
                        apeErrorFlg = true;
                    }
                }
            }
        }
        return errorFlg;
    }

    /**
     * 変更・削除画面の削除ボタン押下時の処理
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public void execDelete() throws IllegalAccessException, InvocationTargetException {
        System.out.println("------------------------- execDelete Call -------------------------");

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDelete():START"));
        DeleteSmsMeterResponse response = new DeleteSmsMeterResponse();
        DeleteSmsMeterParameter parameter = new DeleteSmsMeterParameter();

        parameter.setBean("DeleteSmsMeterBean");

        List<DeleteSmsMeterRequestSet> requestSetList = new ArrayList<>();
        for (MeterInfo meterInfo : meterList) {
            if (meterInfo.getCheckBox()) {
                DeleteSmsMeterRequestSet requestSet = new DeleteSmsMeterRequestSet();
                requestSet.setMeterMngId(meterInfo.getMeterMngId());
                requestSet.setDevId(smartProperty.getDevId());
                requestSet.setVersion(meterInfo.getVersion());
                requestSetList.add(requestSet);
            }
        }
        DeleteSmsMeterRequest request = new DeleteSmsMeterRequest();
        request.setRequestSetList(requestSetList);

        // 機器に送信する
        parameter.setSendFlg(this.isSendDelFlg());

        parameter.setResult(request);

        SmsApiGateway gateway = new SmsApiGateway();

        response = (DeleteSmsMeterResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            // 更新失敗→メッセージをセットしてreturn
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return;
        }
        keepDevId = smartProperty.getDevId();
        // 画面初期化
        reload();

        addMessage(beanMessages.getMessage("osol.info.DeleteSuccess"));

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDelete():END"));
    }

    /**
     * 現在値取得画面の現在値取得ボタン押下時の処理
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws Exception
     * @throws IOException
     */
    @Logged
    public void execGetCurrentValue(Long nowValMeterMngId) throws IllegalAccessException, InvocationTargetException, IOException, Exception {
        System.out.println("------------------------- execGetCurrentValue Call -------------------------");

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execGetCurrentValue():START"));

        // スマートメーター かつ LTE-Mの場合
        if (Objects.equals(meterKind, METER_KIND.SMART.getVal()) && lteMDeviceFlg) {
            // 初期化
            initLteMApiInfo();
            // 現在値取得のフラグON
            lteMgetCurrentValueFlg = true;
            // LTE-M 現在値表示用リストを初期化
            lteMNowValResponseList = new ArrayList<GetLteMNowValResponse>();
            // 帳票出力時に使用するIDを発行
            this.currentDownloadId = UUID.randomUUID().toString();
            // 状態表示更新ステータスを「取得中」に設定
            setDispUpdateStatus(DISP_UPDATE_STATUS.IN_PROGRESS.getValue());
            // 状態取得実行時刻取得
            this.currentValueDispDate = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH).format(getCurrentDateTime());

            // 対象のメーターリストを取得
            List<MeterInfo> tempList = (meterList == null) ? Collections.emptyList() : meterList;
            List<MeterInfo> targetMeterList = tempList.stream()
                    .filter(m -> Boolean.TRUE.equals(m.getCheckBox()))
                    .collect(Collectors.toList());

            // 対象のメーターリストが存在しない場合
            if (targetMeterList.isEmpty()) {
                logAndNotifyError("対象のメーターリストが存在しません。");
                ltemApiExecSuccessFlg = false;
                dispUpdateButtonEnabled = true;
            }

            // meterIdを基にDBから情報取得
            Function<String, List<MMeterInfo>> fetchByMeterId = meterId -> {
                MMeterInfo cond = new MMeterInfo();
                cond.setMeterId(meterId);
                return mMeterInfoDao.getMMeterInfoList(cond);
            };

            // 対象meterId一覧
            List<String> ids = targetMeterList.stream()
                .map(MeterInfo::getMeterId)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

            Set<String> missingMeterIds = ids.stream()
                    .filter(mid -> {
                        List<MMeterInfo> list = fetchByMeterId.apply(mid);
                        return list == null || list.isEmpty();
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            // 定格情報が取得不可の場合
            if (!missingMeterIds.isEmpty()) {
                ratedCurrentMeterMngId = String.join(", ", missingMeterIds);
                logAndNotifyError("計器IDの定格電流の情報が取得できません。 計器ID:%s", ratedCurrentMeterMngId);
                ratedCurrentMissingFlg = true;
                return;
            }

            MeterManagementInputProperty input = getSmartSettingUpdateProperty();

            // 非同期でAPI実行
            String requestId = UUID.randomUUID().toString(); // リクエストの判別のためIDを採番
            List<LteMMeterExecResult> results = lteMParallelExecutor.executeAsyncForMeters(
                    targetMeterList,
                    input,
                    MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode(),
                    requestId,
                    getLoginPersonId(),
                    mMeterInfoDao
                    );

            // リクエスト単位で結果を保持
            currentExecResults.clear();
            currentExecResults.put(requestId, results);
            lastRequestId = requestId;

            // API成功フラグを初期化
            ltemApiExecSuccessFlg = false;

            // API実行結果取得
            getExecResult(results);

            // ログ出力
            logAndNotifyInfo("現在値取得 API実行 成功: %d件%s", apiSuccessCount,
                apiSuccessCount == 0 ? "" : " [管理番号：" + apiSuccessCsv + "]"
            );
            logAndNotifyError("現在値取得 API実行 失敗: %d件%s", apiFailedCount,
                apiFailedCount == 0 ? "" : " [管理番号：" + apiFailedCsv + "]"
            );

            // 全て成功ではない場合
            if (apiFailedCount > 0) {
                dispUpdateButtonEnabled = true;
            } else {
                // 全て成功の場合
                ltemApiExecSuccessFlg = true;
            }

            // DBより情報取得してLTE-M現在値表示用リストへ格納
            lteMNowValResponseList = loadLteMCurrentValueListFromDb(targetMeterList, mMeterInfoDao);
            if (targetMeterList.size() == 0 || dbFailedCount > 0
                   || !ratedCurrentMessageSet.isEmpty()) {
                // DB取得結果で失敗がある かつ 定格電流が未取得の場合(現在値取得APIのDBへの同期がまだ完了していない もしくは API以外の別の更新が入った可能性がある場合)
                dispUpdateButtonEnabled = true;
            } else {
                // DB取得にて失敗が無い場合
                dispUpdateButtonEnabled = false;
                ltemApiExecSuccessFlg = true;
            }

            if (lteMNowValResponseList.isEmpty()) {
                ltemApiExecSuccessFlg = false;
                dispUpdateButtonEnabled = true;
                return;
            }

        } else {
            InsertSmsMeterNowValCommandResponse response = new InsertSmsMeterNowValCommandResponse();
            InsertSmsMeterNowValCommandParameter parameter = new InsertSmsMeterNowValCommandParameter();

            parameter.setBean("InsertSmsMeterNowValCommandBean");

            // 中身を詰める
            parameter.setDevId(smartProperty.getDevId());
            parameter.setMeterMngId(nowValMeterMngId.toString());

            SmsApiGateway gateway = new SmsApiGateway();

            response = (InsertSmsMeterNowValCommandResponse) gateway.osolApiPost(
                    osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                    SmsApiGateway.PATH.JSON,
                    parameter,
                    response);

            if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
                // 取得失敗→メッセージをセットしてreturn
                addErrorMessage(
                        beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
                alertMsg = beanMessages.getMessage("osol.error.getCurrentValueError");
                return;
            }
        }

        // LTE-M以外の場合
        if (!lteMDeviceFlg) {
            keepDevId = smartProperty.getDevId();
            // 画面初期化
            reload();

            alertMsg = beanMessages.getMessage("osol.info.getCurrentValueSuccess");
        }
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execGetCurrentValue():END"));
    }

    /**
     * API実行結果取得
     *
     * @param results API実行結果リスト
     */
    private void getExecResult(List<LteMMeterExecResult> results) {

        if (results == null) {
            results = Collections.emptyList();
        }

        Map<Boolean, List<Long>> part = results.stream()
                .filter(LteMMeterExecResult::isApiExecFlg) // API実行対象に限定
                .collect(Collectors.partitioningBy(
                    LteMMeterExecResult::isSuccess,
                    Collectors.mapping(LteMMeterExecResult::getMeterMngId, Collectors.toList())
                ));

        // メッセージ格納
        for (LteMMeterExecResult r : results) {
            if (r.getMessages() != null && !r.getMessages().isEmpty()) {
                ratedCurrentMessageSet.addAll(r.getMessages());
            }
        }
        for (String msg : ratedCurrentMessageSet) {
            FacesContext.getCurrentInstance().addMessage(
                "s6Messages",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null)
            );
        }
        List<Long> successIds = part.getOrDefault(true, Collections.emptyList());
        List<Long> failedIds  = part.getOrDefault(false, Collections.emptyList());

        // 合計を取得
        apiSuccessCount = successIds.size();
        apiFailedCount  = failedIds.size();

        // CSV文字列へ変換
        apiSuccessCsv = successIds.stream().sorted()
            .map(String::valueOf).collect(Collectors.joining(", "));
        apiFailedCsv = failedIds.stream().sorted()
            .map(String::valueOf).collect(Collectors.joining(", "));
    }

    /**
     * DBより情報取得してLTE-M現在値表示用リストへ格納
     *
     * @param targetList 対象リスト
     * @param mMeterInfoDao メーターの詳細情報Dao
     * @return LTE-M現在値表示用リスト
     */
    private List<GetLteMNowValResponse> loadLteMCurrentValueListFromDb(List<MeterInfo> targetList, MMeterInfoDao mMeterInfoDao) {

        List<GetLteMNowValResponse> lteMNowValResponseList = new ArrayList<GetLteMNowValResponse>();

        for (MeterInfo meterInfo : targetList) {
            String meterId = meterInfo.getMeterId();

            // DBテーブル「m_meter_info」カラム「switch_code(開閉コード)」の判定
            MMeterInfo mMeterInfo = new MMeterInfo();
            mMeterInfo.setMeterId(meterId);
            List<MMeterInfo> mMeterInfoList = mMeterInfoDao.getMMeterInfoList(mMeterInfo);

            if (mMeterInfoList == null || mMeterInfoList.isEmpty()) {
                // DB取得結果を設定
                dbFailedCount++;
                if (!dbFailedCsv.isEmpty()) {
                    dbFailedCsv = dbFailedCsv.concat(", ");
                }
                dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

            } else if (mMeterInfoList.size() > 1) {
                // DB取得結果を設定
                dbFailedCount++;
                if (!dbFailedCsv.isEmpty()) {
                    dbFailedCsv = dbFailedCsv.concat(", ");
                }
                dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

            } else if (mMeterInfoList.size() == 1) {
                BigDecimal switchCode = mMeterInfoList.get(0).getSwitchCode();

                // メータID(計器ID)に該当するレコードをDBより取得
                MMeter mMeter = new MMeter();
                MMeterPK mMeterPK = new MMeterPK();
                mMeterPK.setMeterMngId(meterInfo.getMeterMngId());
                mMeterPK.setDevId(devId);
                mMeter.setId(mMeterPK);
                mMeter = mMeterDao.find(mMeter);

                // メータID(計器ID)でのDB取得結果が想定外の場合
                if (mMeter == null) {
                    logAndNotifyError("メータID(計器ID)を基にDB取得しましたが、該当レコードがございませんでした。 メータID:%s", meterId);
                    return null;
                }

                // DBから対象レコードを取得
                MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
                MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
                mMeterLoadlimitPK.setDevId(devId);
                mMeterLoadlimitPK.setMeterMngId(meterInfo.getMeterMngId());
                mMeterLoadlimit.setId(mMeterLoadlimitPK);
                mMeterLoadlimit = mMeterLoadlimitDao.find(mMeterLoadlimit);

                // 状態取得実行時刻をTimestamp型へ変換
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH);
                LocalDateTime ldt = LocalDateTime.parse(currentValueDispDate, fmt);
                // タイムゾーンが無い文字列は、どのゾーンで解釈するかを明示（例: Asia/Tokyo）
                Instant instant = ldt.atZone(MeterManagementConstants.ZONE_JST).toInstant();
                Timestamp currentValueDispTimestamp = Timestamp.from(instant);

                // DBが同期APIにより想定通り更新されているか判定
                boolean isValidMMeter = mMeter.getUpdateUserId() == 9998 &&
                    mMeter.getUpdateDate().compareTo(currentValueDispTimestamp) > 0;

                boolean isValidMMeterLoadlimit = mMeterLoadlimit.getUpdateUserId() == 9998 &&
                        mMeterLoadlimit.getUpdateDate().compareTo(currentValueDispTimestamp) > 0;

                GetLteMNowValResult res = new GetLteMNowValResult();

                // 共通項目設定
                res.setMeterMngId(meterInfo.getMeterMngId());
                res.setTenantId(meterInfo.getTenantId());
                res.setBuildingNo(meterInfo.getBuildingNo());
                res.setBuildingName(meterInfo.getBuildingName());
                res.setMeterId(meterId);

                if (switchCode.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.NONE.getCode())) == 0) {
                    // DBテーブル「m_meter_info」カラム「switch_code(開閉コード)」=0の場合
                    // DB取得結果を設定
                    dbSuccessCount++;
                    if (!dbSuccessCsv.isEmpty()) {
                        dbSuccessCsv = dbSuccessCsv.concat(", ");
                    }
                    dbSuccessCsv = dbSuccessCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

                    // 開閉区分(表示用)
                    res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.NO_SWITCH_DEVICE.getValue());
                    // 開閉区分
                    res.setOpenMode(mMeter.getOpenMode());
                    // 負荷制限
                    res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.NONE.getValue());
                    // 負荷電流
                    res.setLoadCurrent("-");
                    // 自動投入時間
                    res.setAutoInjection("-");
                    // 自動投入回数
                    res.setBreakerActCount("-");
                    // 自動投入回数クリア時間
                    res.setCountClear("-");

                } else if ((switchCode.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                        || switchCode.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                    // DBテーブル「m_meter_info」カラム「switch_code(開閉コード)」=1または3の場合
                    // 同期APIによりDBテーブル「m_meter」が想定通り更新されている場合
                    if (isValidMMeter) {
                        // DB取得結果を設定
                        dbSuccessCount++;
                        if (!dbSuccessCsv.isEmpty()) {
                            dbSuccessCsv = dbSuccessCsv.concat(", ");
                        }
                        dbSuccessCsv = dbSuccessCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

                        // 開閉区分(表示用)
                        if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.OFF.getValue())) {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.OFF.getValue());
                        } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.ON.getValue())) {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.ON.getValue());
                        } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.DEVICE_CHANGE_OFF.getValue())) {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.DEVICE_CHANGE_OFF.getValue());
                        } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.DEVICE_CHANGE_ON.getValue())) {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.DEVICE_CHANGE_ON.getValue());
                        } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.UNKNOWN.getValue())) {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.UNKNOWN.getValue());
                        } else {
                            res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.NO_SWITCH_DEVICE.getValue());
                        }

                    } else {
                        // DB取得結果を設定
                        dbFailedCount++;
                        if (!dbFailedCsv.isEmpty()) {
                          dbFailedCsv = dbFailedCsv.concat(", ");
                        }
                        dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

                        // 開閉区分(表示用)
                        res.setOpenModeDisp(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                    }

                    // 開閉区分
                    res.setOpenMode(mMeter.getOpenMode());
                    // 負荷制限
                    res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.NONE.getValue());
                    // 負荷電流
                    res.setLoadCurrent("-");
                    // 自動投入時間
                    res.setAutoInjection("-");
                    // 自動投入回数
                    res.setBreakerActCount("-");
                    // 自動投入回数クリア時間
                    res.setCountClear("-");

                } else {
                    // 同期APIによりDBテーブル「m_meter」または「m_meter_loadlimit」が想定通り更新されている場合
                    if (isValidMMeter || isValidMMeterLoadlimit) {

                        // 「m_meter」と「m_meter_loadlimit」の両方が想定通り更新されている場合、成功と判定
                        if (isValidMMeter && isValidMMeterLoadlimit) {
                            // DB取得結果を設定
                            dbSuccessCount++;
                            if (!dbSuccessCsv.isEmpty()) {
                                dbSuccessCsv = dbSuccessCsv.concat(", ");
                            }
                            dbSuccessCsv = dbSuccessCsv.concat(String.valueOf(meterInfo.getMeterMngId()));
                        } else {
                            // DB取得結果を設定
                            dbFailedCount++;
                            if (!dbFailedCsv.isEmpty()) {
                              dbFailedCsv = dbFailedCsv.concat(", ");
                            }
                            dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));
                        }

                        // 開閉区分(表示用)
                        if (isValidMMeter) {
                            if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.OFF.getValue())) {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.OFF.getValue());
                            } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.ON.getValue())) {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.ON.getValue());
                            } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.DEVICE_CHANGE_OFF.getValue())) {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.DEVICE_CHANGE_OFF.getValue());
                            } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.DEVICE_CHANGE_ON.getValue())) {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.DEVICE_CHANGE_ON.getValue());
                            } else if (Objects.equals(mMeter.getOpenMode(), MeterManagementConstants.OPEN_MODE.UNKNOWN.getValue())) {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.UNKNOWN.getValue());
                            } else {
                                res.setOpenModeDisp(MeterManagementConstants.OPEN_MODE_DISP.NO_SWITCH_DEVICE.getValue());
                            }
                            // 開閉区分
                            res.setOpenMode(mMeter.getOpenMode());
                        } else {
                            res.setOpenMode(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setOpenModeDisp(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        }

                        if (isValidMMeterLoadlimit) {
                            // 負荷制限
                            res.setLoadlimitMode(mMeterLoadlimit.getLoadlimitMode());

                            if (Objects.equals(mMeterLoadlimit.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.TEMP.getValue())) {
                                // 負荷制限
                                res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.TEMP.getValue());
                                // 負荷電流
                                res.setLoadCurrent(mMeterLoadlimit.getTempLoadCurrent());
                                // 自動投入時間
                                res.setAutoInjection(Objects.equals(mMeterLoadlimit.getTempAutoInjection(), MeterManagementConstants.AUTO_INJECTION.DISABLED.getCode())
                                        ? MeterManagementConstants.AUTO_INJECTION.DISABLED.getValue()
                                        : mMeterLoadlimit.getTempAutoInjection());
                                // 自動投入回数
                                res.setBreakerActCount(mMeterLoadlimit.getTempBreakerActCount());
                                // 自動投入回数クリア時間
                                res.setCountClear(mMeterLoadlimit.getTempCountClear());

                            } else if (Objects.equals(mMeterLoadlimit.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.BASIC.getValue())) {
                                // 負荷制限
                                res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.BASIC.getValue());
                                // 負荷電流
                                res.setLoadCurrent(mMeterLoadlimit.getLoadCurrent());
                                // 自動投入時間
                                res.setAutoInjection(Objects.equals(mMeterLoadlimit.getAutoInjection(), MeterManagementConstants.AUTO_INJECTION.DISABLED.getCode())
                                        ? MeterManagementConstants.AUTO_INJECTION.DISABLED.getValue()
                                        : mMeterLoadlimit.getAutoInjection());
                                // 自動投入回数
                                res.setBreakerActCount(mMeterLoadlimit.getBreakerActCount());
                                // 自動投入回数クリア時間
                                res.setCountClear(mMeterLoadlimit.getCountClear());

                            } else if (Objects.equals(mMeterLoadlimit.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.DISABLED.getValue())
                                    & (Objects.equals(mMeterLoadlimit.getLoadCurrent(), " ") || Objects.equals(mMeterLoadlimit.getLoadCurrent(), "　"))) {
                                // loadlimit_mode=0の場合かつload_currentが[ ]（スペース）の場合 ⇒ 「負荷制限」は「無効」と表示し、以降の列は「ー」とする。
                                // 負荷制限
                                res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.DISABLED.getValue());
                                // 負荷電流
                                res.setLoadCurrent("-");
                                // 自動投入時間
                                res.setAutoInjection("-");
                                // 自動投入回数
                                res.setBreakerActCount("-");
                                // 自動投入回数クリア時間
                                res.setCountClear("-");

                            } else if (Objects.equals(mMeterLoadlimit.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.DISABLED.getValue())) {
                                // 負荷制限
                                res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.DISABLED.getValue());
                                // 負荷電流
                                res.setLoadCurrent(mMeterLoadlimit.getLoadCurrent());
                                // 自動投入時間
                                res.setAutoInjection(Objects.equals(mMeterLoadlimit.getAutoInjection(), MeterManagementConstants.AUTO_INJECTION.DISABLED.getCode())
                                        ? MeterManagementConstants.AUTO_INJECTION.DISABLED.getValue()
                                        : mMeterLoadlimit.getAutoInjection());
                                // 自動投入回数
                                res.setBreakerActCount(mMeterLoadlimit.getBreakerActCount());
                                // 自動投入回数クリア時間
                                res.setCountClear(mMeterLoadlimit.getCountClear());

                            } else {
                                // 負荷制限
                                res.setLoadlimitModeDisp(MeterManagementConstants.LOADLIMIT_MODE_DISP.DISABLED.getValue());
                                // 負荷電流
                                res.setLoadCurrent("-");
                                // 自動投入時間
                                res.setAutoInjection("-");
                                // 自動投入回数
                                res.setBreakerActCount("-");
                                // 自動投入回数クリア時間
                                res.setCountClear("-");
                            }
                        } else {
                            res.setLoadlimitMode(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setLoadlimitModeDisp(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setLoadCurrent(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setAutoInjection(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setBreakerActCount(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                            res.setCountClear(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        }
                    } else {
                        // DB取得結果を設定
                        dbFailedCount++;
                        if (!dbFailedCsv.isEmpty()) {
                          dbFailedCsv = dbFailedCsv.concat(", ");
                        }
                        dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));

                        // 取得中を設定(現在値取得APIのDBへの同期がまだ完了していない もしくは API以外の別の更新が入った可能性あり)
                        res.setOpenMode(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setOpenModeDisp(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setLoadlimitMode(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setLoadlimitModeDisp(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setLoadCurrent(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setAutoInjection(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setBreakerActCount(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                        res.setCountClear(MeterManagementConstants.GET_CURRENT_VALUE_IN_PROGRESS);
                    }
                }
                GetLteMNowValResponse getLteMNowValResponse = new GetLteMNowValResponse();
                getLteMNowValResponse.setResult(res);
                lteMNowValResponseList.add(getLteMNowValResponse);

            } else {
                logAndNotifyError(String.format("メータID(計器ID)を基にDBのテーブル「m_meter_info」を検索しましたが、想定外の取得結果となりました。 メーター管理番号:%s メータID:%s", meterInfo.getMeterMngId(), meterInfo.getMeterId()));
                // DB取得結果を設定
                dbFailedCount++;
                if (!dbFailedCsv.isEmpty()) {
                    dbFailedCsv = dbFailedCsv.concat(", ");
                }
                dbFailedCsv = dbFailedCsv.concat(String.valueOf(meterInfo.getMeterMngId()));
            }
        }
        return lteMNowValResponseList;
    }

    /**
     * DB取得結果をメーター一覧表示用リストへ設定
     *
     * @param meterInfoList メーター一覧表示用リスト
     * @param mMeter DB取得結果
     * @param mMeterLoadlimit DB取得結果
     */
    private void settingToDispList(List<MeterInfo> meterInfoList, MMeter mMeter, MMeterLoadlimit mMeterLoadlimit) {

        if (meterInfoList == null) {
            return;
        }

        for (MeterInfo meter : meterInfoList) {
            String meterId = meter.getMeterId();
            if (meterId == null) {
                continue;
            }

            // 開閉区分
            meter.setOpenMode(mMeter.getOpenMode());
            meter.setOpenModeDisp(convertOpenModeDisp(mMeter.getOpenMode()));

            // 基本設定
            meter.setLoadCurrent(mMeterLoadlimit.getLoadCurrent() == null ? "-" : mMeterLoadlimit.getLoadCurrent().toString());
            meter.setAutoInjection(mMeterLoadlimit.getAutoInjection() == null ? "-" : mMeterLoadlimit.getAutoInjection().toString());
            meter.setBreakerActCount(mMeterLoadlimit.getBreakerActCount() == null ? "-" : mMeterLoadlimit.getBreakerActCount().toString());
            meter.setCountClear(mMeterLoadlimit.getCountClear() == null ? "-" : mMeterLoadlimit.getCountClear().toString());

            // 臨時設定
            meter.setTempLoadCurrent(mMeterLoadlimit.getTempLoadCurrent() == null ? "-" : mMeterLoadlimit.getTempLoadCurrent().toString());
            meter.setTempAutoInjection(mMeterLoadlimit.getTempAutoInjection() == null ? "-" : mMeterLoadlimit.getTempAutoInjection().toString());
            meter.setTempBreakerActCount(mMeterLoadlimit.getTempBreakerActCount() == null ? "-" : mMeterLoadlimit.getTempBreakerActCount().toString());
            meter.setTempCountClear(mMeterLoadlimit.getTempCountClear() == null ? "-" : mMeterLoadlimit.getTempCountClear().toString());

            // 負荷制限モード
            meter.setLoadlimitMode(mMeterLoadlimit.getLoadlimitMode());
        }
        meterList = meterInfoList;
    }

    /**
     * LTE-M用 API情報とDB情報 初期化
     */
    private void initLteMApiInfo() {
        apiSuccessCount = 0;
        dbSuccessCount = 0;
        apiFailedCount = 0;
        dbFailedCount = 0;
        apiSuccessCsv = "";
        dbSuccessCsv = "";
        apiFailedCsv = "";
        dbFailedCsv = "";
        dispUpdateButtonEnabled = false;
        ratedCurrentMessageSet = new LinkedHashSet<>();
        ratedCurrentMissingFlg = false;
    }

    /**
     * LTE-M用 DB情報 初期化
     */
    private void initLteMDbInfo() {
        dbSuccessCount = 0;
        dbFailedCount = 0;
        dbSuccessCsv = "";
        dbFailedCsv = "";
    }

    /**
     * LTE-M再試行管理用
     */
    private Map<String, Boolean> lastSuccessStatus = new HashMap<>();

    public synchronized void updateLastCall(String apiName, boolean success) {
        lastSuccessStatus.put(apiName, success);
    }

    public synchronized String[] getFailedApis() {
        return lastSuccessStatus.entrySet().stream()
                .filter(e -> !e.getValue())
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    public synchronized void resetApi(String apiName) {
        lastSuccessStatus.put(apiName, true); // 再試行後は成功扱いにするか、状態をクリア
    }

   /**
    * 開閉区分の判定
    * @param code 開閉区分の値
    * @return 判定結果
    */
    private String convertOpenModeDisp(String code) {
        if (code == null)
            return "－";
        switch (code) {
        case "0":
        case "2":
        case "6":
        case "8":
            return "開";
        case "1":
        case "3":
        case "7":
        case "9":
            return "閉";
        case "255":
            return "－";
        default:
            return "不明";
        }
    }

    /**
     * 現在値取得画面の現在値表示用変数をリセット
     */
    public void resetNowVal() {
        nowValProperty = new NowValueProperty();
    }

    /**
     * 現在値取得画面の現在値表示ボタン押下時の処理
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public String execDispCurrentValue(Long nowValMeterMngId) throws IllegalAccessException, InvocationTargetException {
        System.out.println("------------------------- execDispCurrentValue Call -------------------------");
        nowValProperty = new NowValueProperty();
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDispCurrentValue():START"));

        nowValProperty = getNowValue(nowValMeterMngId);
        nowValProperty.setDemandingMsg(beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.info.nowValDemanding"));

        if (Objects.isNull(nowValProperty) || Objects.isNull(nowValProperty.getDevId())) {
            nowValProperty.setExistenceFlag(false);
            // 現在値取得できなかった場合の画面表示用メッセージを詰める
            String msgParam = "";
            if (SmsConstants.METER_KIND.SMART.getVal().equals(meterKind)) {
                msgParam = MeterManagementConstants.METER_TYPE_SMART_DISP;
            } else if (SmsConstants.METER_KIND.PULSE.getVal().equals(meterKind)) {
                msgParam = MeterManagementConstants.METER_TYPE_PULSE_DISP;
            }
            nowValProperty.setExistenceMsg1(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.info.nowValNoData1", msgParam));
            nowValProperty.setExistenceMsg2(beanMessages.getMessageFormat("smsCollectSettingMeterMeterManagementBean.info.nowValNoData2", msgParam));
        } else {
            // 表示用に変換が必要なものを変換して詰めなおす
            // 計器種別
            String ifTypeDisp = MeterManagementConstants.IF_TYPE_DISP_MAP
                    .get(nowValProperty.getIfType() != null ? nowValProperty.getIfType().toString() : null);
            nowValProperty.setIfTypeDisp(ifTypeDisp == null ? "" : ifTypeDisp);

            // 開閉区分
            String circuitBreakerDisp = MeterManagementConstants.CIRCUIT_BREAKER_DISP_MAP
                    .get(nowValProperty.getCircuitBreaker());
            nowValProperty.setCircuitBreakerDisp(
                    circuitBreakerDisp == null ? MeterManagementConstants.CIRCUIT_BREAKER_OTHER
                            : circuitBreakerDisp);

            // 現在時刻
            if (nowValProperty.getMeasureDate() != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                nowValProperty.setMeasureDateDisp(df.format(nowValProperty.getMeasureDate()));
            }

            nowValProperty.setExistenceFlag(true);

            // パルス種別
            nowValProperty.setPulseTypeDisp(getKey(pulseTypeMap, nowValProperty.getPulseType()));
        }

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDispCurrentValue():END"));

        return "";
    }

    /**
     * 現在値取得画面の状態表示更新ボタン押下時の処理(LTE-M用)
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public String execDispUpdate(Long nowValMeterMngId) throws IllegalAccessException, InvocationTargetException {
        System.out.println("------------------------- execDispUpdate Call -------------------------");
        nowValProperty = new NowValueProperty();
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDispUpdate():START"));

        // LTE-Mの場合
        if (lteMDeviceFlg) {
            // DB取得結果の初期化
            initLteMDbInfo();

            // API実行結果取得  直近リクエストの結果Listを取り出して渡す
            List<LteMMeterExecResult> results = currentExecResults.getOrDefault(lastRequestId, Collections.emptyList());
            getExecResult(results);

            // 現在値取得にてAPI実行したメーター管理番号 (=画面にて選択欄をチェックした行のメーター管理番号)のCSV取得
            Set<Long> set = new LinkedHashSet<>(csvtoLongSet(apiFailedCsv));
            set.addAll(csvtoLongSet(apiSuccessCsv));
            String checkedCsv = set.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            // meterListにてmeterMngIdをキーにフィルタリング checkedCsvは画面にて選択欄をチェックした行のメーター管理番号のCSV
            List<MeterInfo> checkedMeterList = filterFailedMeters(meterList, checkedCsv);
            // DBより情報取得してLTE-M現在値表示用リストへ格納
            List<GetLteMNowValResponse> dispUpdateList = loadLteMCurrentValueListFromDb(checkedMeterList, mMeterInfoDao);
            if (dispUpdateList != null) {
                // 今回取得したリストをLTE-M現在値表示用リストに追加
                lteMNowValResponseList.addAll(dispUpdateList);

                // meterMngIdで重複排除（最後を残す）
                Map<Long, GetLteMNowValResponse> meterMngIdMap =
                        lteMNowValResponseList.stream()
                            .filter(Objects::nonNull)
                            .filter(r -> r.getResult() != null && r.getResult().getMeterMngId() != null)
                            .collect(Collectors.toMap(
                                r -> r.getResult().getMeterMngId(),
                                Function.identity(),
                                (oldV, newV) -> newV, // 重複時は後勝ち
                                LinkedHashMap::new // 表示順維持
                            ));
                lteMNowValResponseList.clear();

                // メーター管理番号をもとに「DB取得した管理番号」が「APIで失敗した管理番号」を満たすか判定 ※満たさない場合、API以外の別の更新が入った可能性あり
                boolean checkFlg = checkDbCoveringFailedMeters(apiFailedCsv, dbFailedCsv, meterMngIdMap);
                if (checkFlg && dbFailedCount == 0) {
                    dispUpdateButtonEnabled = false;
                } else {
                    dispUpdateButtonEnabled = true;
                }

                // 全てDB取得成功の場合、API成功フラグをON
                if (dbFailedCount == 0) {
                    ltemApiExecSuccessFlg = true;
                }

                lteMNowValResponseList.addAll(meterMngIdMap.values());
            }
            // ログ出力
            logAndNotifyInfo("状態表示更新  取得結果  成功: %d件%s", dbSuccessCount,
                dbSuccessCount == 0 ? "" : " [管理番号：" + dbSuccessCsv + "]"
            );
            logAndNotifyError("状態表示更新  取得結果  失敗: %d件%s", dbFailedCount,
                dbFailedCount == 0 ? "" : " [管理番号：" + dbFailedCsv + "] 現在値取得APIのDBへの同期がまだ完了していない もしくは API以外の別の更新が入った可能性があります。"
            );
        }
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:execDispUpdate():END"));

        return "";
    }

    /**
     * DB取得した管理番号が現在値取得のAPIで失敗した管理番号を満たしているか判定
     *
     * @param apiFailedCsv 現在値取得のAPIで失敗した管理番号のCSV
     * @param dbFailedCsv 現在値取得のDB取得で失敗した管理番号のCSV
     * @param meterMngIdMap DB取得した管理番号
     * @return 判定結果
     */
    private boolean checkDbCoveringFailedMeters(String apiFailedCsv, String dbFailedCsv, Map<Long, GetLteMNowValResponse> meterMngIdMap) {

        // 現在値取得のAPIで失敗した管理番号
        Set<Long> expectedApi = csvtoLongSet(apiFailedCsv);
        // DB取得で失敗した管理番号
        Set<Long> expectedDb = csvtoLongSet(dbFailedCsv);

        // 両方nullまたは空の場合は一致判定
        if ((expectedApi == null || expectedApi.isEmpty()) && (expectedDb == null || expectedDb.isEmpty())) {
            return true;
        }

        // 両Setが完全一致の場合
        if (Objects.equals(expectedApi, expectedDb)) {
            return true;
        }

        // 差分抽出
        Set<Long> apiOnly = new LinkedHashSet<>(expectedApi);
        apiOnly.removeAll(expectedDb);

        Set<Long> dbOnly = new LinkedHashSet<>(expectedDb);
        dbOnly.removeAll(expectedApi);

        // 差分がある場合にログ出力
        if (!apiOnly.isEmpty() || !dbOnly.isEmpty()) {
            String apiOnlyStr = apiOnly.isEmpty() ? "なし" :
                    apiOnly.stream().map(String::valueOf).collect(Collectors.joining(", "));
            String dbOnlyStr = dbOnly.isEmpty() ? "なし" :
                    dbOnly.stream().map(String::valueOf).collect(Collectors.joining(", "));

            logAndNotifyError("API実行失敗とDB取得失敗のメーター管理番号が一致しませんでした。 API実行失敗:%s DB取得失敗:%s",
                apiOnlyStr, dbOnlyStr
            );
        }
        return false;
    }

    /**
     * CSVから数値の集合を取得
     *
     * @param csv 対象CSV
     * @return 数値の集合
     */
    private static Set<Long> csvtoLongSet(String csv) {
        if (csv == null || csv.codePoints().allMatch(Character::isWhitespace)) {
            return Collections.emptySet();
        }
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 対象のメーターリストから実行失敗したメーターリストを取得
     *
     * @param meterList 対象のメーターリスト
     * @param apiFailedCsv 実行失敗したメーター管理番号のCSV
     * @return
     */
    private List<MeterInfo> filterFailedMeters(List<MeterInfo> meterList, String apiFailedCsv) {
        Set<Long> failedIds = parseIdsFromCsv(apiFailedCsv);
        if (failedIds.isEmpty()) {
            return Collections.emptyList();
        }
        return meterList.stream().filter(meterInfo -> meterInfo.getMeterMngId() != null && failedIds.contains(meterInfo.getMeterMngId())).collect(Collectors.toList());
    }


    /**
     * CSVよりIDの集合を取得
     * @param csv 対象のCSV
     * @return IDの集合を取得
     */
    private Set<Long> parseIdsFromCsv(String csv) {
        if (csv == null || csv.codePoints().allMatch(Character::isWhitespace)) {
            return Collections.emptySet();
        }
        Set<Long> ids = new LinkedHashSet<>(); // 重複除去＆順序保持
        for (String token : csv.split(",")) {
            String s = token.trim();
            if (s.isEmpty()) {
                continue;
            }
            try {
                ids.add(Long.valueOf(s));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                logAndNotifyError(MeterManagementBean.class.getPackage().getName().concat(" smsCollectSettingMeterMeterManagementBean:parseIdsFromCsv(): NumberFormatException"));
            }
        }
        return ids;
    }

    /** ダウンロード対象ファイルパス. */
    private String downloadFilePath;

    @Inject
    private SmsFileDownload fileDownloader;

    /** 建物名取得用 */
    @Inject
    private TopBean topBean;

    /** ダウンロードファイル（サーバ）保存名. */
    private String saveFilename;

    /** ダウンロードファイル保存名. */
    // ダウンロードファイル名を「MeterTemplate.csv」から「メーター管理_一括登録_建物名_接続先名_メーター種類_yyyymmddhhmmss.csv」へ変更
//    private static final String DOWNLOAD_FILE_NAME = "MeterTemplate.csv";

      // 「登録データ出力」downloadRegistrationDataCsvFile()に置き換わったためコメントアウト
//    /**
//     * データテンプレート出力(CSV).
//     */
//    public String downloadTemplateCsvFile() {
//        // 出力データ取得
//        List<List<String>> templateList = createCsvTemplate();
//
//        String fileName = DOWNLOAD_FILE_NAME;
//        saveFilename = fileName;
//
//        String outputDir = getCsvOutputDir();
//        downloadFilePath = Paths.get(outputDir, fileName).toString();
//
//        MeterManagementCsvConverter csvConverter = new MeterManagementCsvConverter();
//        csvConverter.csvPrint(outputDir, fileName, templateList);
//
//        return STR_EMPTY;
//    }

    /**
     * 登録データ出力(CSV).
     * @param devName 接続先名
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Logged
    public String downloadRegistrationDataCsvFile(String devName) throws IllegalAccessException, InvocationTargetException {
        // 出力データ取得
        List<List<String>> registrationDataList = createRegistrationDataCsv();

        // 現在日時取得
        String today = DateUtility.changeDateFormat(getCurrentDateTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        // 建物名取得
        String buildingName = topBean.getTopBeanProperty().getListInfo().getBuildingName();
        // メーター種類取得
        String kind = "";
        if (METER_KIND.SMART.getVal().equals(getMeterKind())) {
            kind = "smart";
        } else if (METER_KIND.PULSE.getVal().equals(getMeterKind())) {
            kind = "pulse";
        } else if (METER_KIND.IOTR.getVal().equals(getMeterKind())) {
            kind = "iotr";
        } else if (METER_KIND.HANDY.getVal().equals(getMeterKind())) {
            kind = "handy";
        } else {
            kind = "aielink";
        }
        String fileName = String.format("メーター管理_一括登録_%s_%s_%s_%s.csv", buildingName, devName, kind, today);
        saveFilename = fileName;

        // 接続先とメーター種類でディレクトリを分ける
        String outputDir = getCsvOutputDir().concat(File.separator).concat(devId).concat(File.separator);
        downloadFilePath = Paths.get(outputDir, fileName).toString();

        MeterManagementCsvConverter csvConverter = new MeterManagementCsvConverter();
        csvConverter.csvPrint(outputDir, fileName, registrationDataList);

        return STR_EMPTY;
    }

    /**
     * 現在日時を取得
     * @return 現在日時
     */
    private Timestamp getCurrentDateTime() {
        return new Timestamp((new Date()).getTime());
    }

    /**
     * ファイルダウンロード開始.
     */
    @Logged
    public String downloadFileStart() {
        int result = fileDownloader.fileDownload(downloadFilePath, saveFilename);
        System.out.println(result);
        return STR_EMPTY;
    }

    /**
     * データテンプレートの中身を作る
     * @return データテンプレートとしてCSV出力するファイルの中身
     */
    private List<List<String>> createCsvTemplate() {
        List<List<String>> retList = new ArrayList<>();

        List<String> inList = null;

        // 共通項目
        inList = new ArrayList<>(Arrays.asList("#Point"));
        retList.add(inList);

        inList = new ArrayList<>(Arrays.asList("#################################################################################################################################################################################################################################"));
        retList.add(inList);

        inList = new ArrayList<>(Arrays.asList("# メーターデータは、以下の順番になります。"));
        retList.add(inList);

        if (METER_KIND.SMART.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // スマートメーターの処理
            // -------------------------------------------------
            if (lteMDeviceFlg) {
                // LTE-Mの場合
                inList = new ArrayList<>(Arrays.asList(
                        "# 更新区分"
                        ,"管理番号"
                        ,"計器ID"
                        ,"メーター種別"
                        ,"ユーザーコード"
                        ,""
                        ,"開閉区分"
                        ,"負荷制限"
                        ,"負荷電流(基本)"
                        ,"自動投入(基本)"
                        ,"開閉器動作カウント(基本)"
                        ,"開閉器カウントクリア(基本)"
                        ,"負荷電流(臨時)"
                        ,"自動投入(臨時)"
                        ,"開閉器動作カウント(臨時)"
                        ,"開閉器カウントクリア(臨時)"
                        ,"検満西暦・和暦"
                        ,"検満年月"
                        ,"検満通知"
                        ,"コメント"));
                retList.add(inList);

                inList = new ArrayList<>(Arrays.asList(
                        "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal())
                        ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                        ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                        ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                        ,""
                        ,MeterManagementConstants.CHECK_OPEN_MODE_CLOSE + " or " + MeterManagementConstants.CHECK_OPEN_MODE_OPEN + " or 空欄(新規登録時のみ)"
                        ,LOAD_LIMIT.INVALID.getVal() + " / " + LOAD_LIMIT.VALID.getVal() + " / " + genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) + " / " + genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal()) + " or 空欄(新規登録時のみ)"
                        ,"半角数字" + MeterManagementConstants.CHECK_LOAD_CURRENT + " or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_AUTO_INJECTION_ZERO + " or " + MeterManagementConstants.CHECK_AUTO_INJECTION_MIN + " - " + MeterManagementConstants.CHECK_AUTO_INJECTION_MAX + "[秒]) or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN + " - " + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX + "[回]) or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_COUNT_CLEAR_MIN + " - " + MeterManagementConstants.CHECK_COUNT_CLEAR_MAX + "[分]) or 空欄(新規登録時のみ)"
                        ,"半角数字" + MeterManagementConstants.CHECK_LOAD_CURRENT + " or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_AUTO_INJECTION_ZERO + " or " + MeterManagementConstants.CHECK_AUTO_INJECTION_MIN + " - " + MeterManagementConstants.CHECK_AUTO_INJECTION_MAX + "[秒]) or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN + " - " + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX + "[回]) or 空欄(新規登録時のみ)"
                        ,"半角数字(" + MeterManagementConstants.CHECK_COUNT_CLEAR_MIN + " - " + MeterManagementConstants.CHECK_COUNT_CLEAR_MAX + "[分]) or 空欄(新規登録時のみ)"
                        ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                        ,"半角数字" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM.length() + "桁(" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM + ")"
                        ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                        ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
                retList.add(inList);

                inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","A123456789","12","123456","","切","無効","60","123","1","12","60","0","0","0","西暦","202401","無効","コメント１２３４５６７８９０１２３４５６"));
                retList.add(inList);

                inList = new ArrayList<>(Arrays.asList("# （例）新規登録の場合、項目「開閉区分」 から項目 「開閉器カウントクリア(臨時)」は何も記載しないでください。"));
                retList.add(inList);

            } else {
                // LTE-M以外の場合
                inList = new ArrayList<>(Arrays.asList(
                        "# 更新区分"
                        ,"管理番号"
                        ,"計器ID"
                        ,"メーター種別"
                        ,"ユーザーコード"
                        ,"機器への設定送信"
                        ,"開閉区分"
                        ,"負荷制限"
                        ,"負荷電流(基本)"
                        ,"自動投入(基本)"
                        ,"開閉器動作カウント(基本)"
                        ,"開閉器カウントクリア(基本)"
                        ,"負荷電流(臨時)"
                        ,"自動投入(臨時)"
                        ,"開閉器動作カウント(臨時)"
                        ,"開閉器カウントクリア(臨時)"
                        ,"検満西暦・和暦"
                        ,"検満年月"
                        ,"検満通知"
                        ,"コメント"));
                retList.add(inList);

                inList = new ArrayList<>(Arrays.asList(
                        "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal())
                        ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                        ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                        ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                        ,SEND_DEVICE.NOT_SEND.getVal() + " or " + SEND_DEVICE.SEND.getVal()
                        ,MeterManagementConstants.CHECK_OPEN_MODE_CLOSE + " or " + MeterManagementConstants.CHECK_OPEN_MODE_OPEN
                        ,LOAD_LIMIT.INVALID.getVal() + " / " + LOAD_LIMIT.VALID.getVal() + " / " + genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal()) + " / " + genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal())
                        ,"半角数字" + MeterManagementConstants.CHECK_LOAD_CURRENT
                        ,"半角数字(" + MeterManagementConstants.CHECK_AUTO_INJECTION_ZERO + " or " + MeterManagementConstants.CHECK_AUTO_INJECTION_MIN + " - " + MeterManagementConstants.CHECK_AUTO_INJECTION_MAX + "[秒])"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN + " - " + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX + "[回])"
                        ,"半角数字(" + MeterManagementConstants.CHECK_COUNT_CLEAR_MIN + " - " + MeterManagementConstants.CHECK_COUNT_CLEAR_MAX + "[分])"
                        ,"半角数字" + MeterManagementConstants.CHECK_LOAD_CURRENT
                        ,"半角数字(" + MeterManagementConstants.CHECK_AUTO_INJECTION_ZERO + " or " + MeterManagementConstants.CHECK_AUTO_INJECTION_MIN + " - " + MeterManagementConstants.CHECK_AUTO_INJECTION_MAX + "[秒])"
                        ,"半角数字(" + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MIN + " - " + MeterManagementConstants.CHECK_BREAKER_ACT_COUNT_MAX + "[回])"
                        ,"半角数字(" + MeterManagementConstants.CHECK_COUNT_CLEAR_MIN + " - " + MeterManagementConstants.CHECK_COUNT_CLEAR_MAX + "[分])"
                        ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                        ,"半角数字" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM.length() + "桁(" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM + ")"
                        ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                        ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
                retList.add(inList);

                inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","A123456789","12","123456","送信しない","切","無効","60","123","1","12","60","0","0","0","西暦","202401","無効","コメント１２３４５６７８９０１２３４５６"));
                retList.add(inList);
            }
        } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // パルスメーターの処理
            // -------------------------------------------------
            inList = new ArrayList<>(Arrays.asList(
                    "# 更新区分"
                    ,"管理番号"
                    ,"計器ID"
                    ,"メーター種別"
                    ,"ユーザーコード"
                    ,"機器への設定送信"
                    ,"乗率"
                    ,"パルス種別変更フラグ"
                    ,"パルス種別"
                    ,"パルス重み変更フラグ"
                    ,"パルス重み(×0.01)"
                    ,"指針値変更フラグ"
                    ,"指針値(×0.01)"
                    ,"検満西暦・和暦"
                    ,"検満年月"
                    ,"検満通知"
                    ,"コメント"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList(
                    "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal())
                    ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                    ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                    ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                    ,SEND_DEVICE.NOT_SEND.getVal() + " or " + SEND_DEVICE.SEND.getVal()
                    ,"半角数字(" + MeterManagementConstants.CHECK_MULTI_MIN + " - " + MeterManagementConstants.CHECK_MULTI_MAX + ")"
                    ,CHECK_CHANGE.NOT_CHANGE.getVal() +" / " + CHECK_CHANGE.CHANGE.getVal()
                    ,genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.INVALID.getVal()) + " / " + genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.SHORT_PULSE.getVal()) + " / " + genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.LONG_PULSE.getVal()) + " / " + genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.STATUS.getVal())
                    ,CHECK_CHANGE.NOT_CHANGE.getVal() +" / " + CHECK_CHANGE.CHANGE.getVal()
                    ,"半角数字(" + MeterManagementConstants.CHECK_PULSE_WEIGHT_MIN + " - " + MeterManagementConstants.CHECK_PULSE_WEIGHT_MAX + ")"
                    ,CHECK_CHANGE.NOT_CHANGE.getVal() +" / " + CHECK_CHANGE.CHANGE.getVal()
                    ,"半角数字(" + MeterManagementConstants.CHECK_CURRENT_DATA_MIN + " - " + MeterManagementConstants.CHECK_CURRENT_DATA_MAX + ")"
                    ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                    ,"半角数字" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM.length() + "桁(" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM + ")"
                    ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                    ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","P123456789","12","123456","送信しない","1","変更する","短パルス","変更する","12345","変更する","12345678","西暦","202401","無効","コメント１２３４５６７８９０１２３４５６"));
            retList.add(inList);

        } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // IoT-R連携用メーターの処理
            // -------------------------------------------------
            inList = new ArrayList<>(Arrays.asList(
                    "# 更新区分"
                    ,"管理番号"
                    ,"計器ID"
                    ,"メーター種別"
                    ,"ユーザーコード"
                    ,"検満西暦・和暦"
                    ,"検満年月"
                    ,"検満通知"
                    ,"コメント"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList(
                    "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal())
                    ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                    ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                    ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                    ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                    ,"半角数字" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM.length() + "桁(" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM + ")"
                    ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                    ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","A123456789","12","123456","西暦","202401","無効","コメント１２３４５６７８９０１２３４５６"));
            retList.add(inList);

        } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // ハンディ検針用メーターの処理
            // -------------------------------------------------
            inList = new ArrayList<>(Arrays.asList(
                    "# 更新区分"
                    ,"管理番号"
                    ,"計器ID"
                    ,"メーター種別"
                    ,"ユーザーコード"
                    ,"乗率"
                    ,"無線種別"
                    ,"無線ID"
                    ,"リレー無線ID1"
                    ,"リレー無線ID2"
                    ,"リレー無線ID3"
                    ,"ポーリンググループNo"
                    ,"検満西暦・和暦"
                    ,"検満年月"
                    ,"検満通知"
                    ,"コメント"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList(
                    "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.INSERT.getVal())
                    ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                    ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                    ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_MULTI_MIN + " - " + MeterManagementConstants.CHECK_MULTI_MAX + ")"
                    ,genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.HYBRID.getVal()) + " / " + genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.CURRENT.getVal()) + " / " + genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.ELECTRONIC.getVal())
                    ,"半角英数" + MeterManagementConstants.CHECK_WIRELESS_ID + "桁"
                    ,"半角英数" + MeterManagementConstants.CHECK_WIRELESS_ID + "桁"
                    ,"半角英数" + MeterManagementConstants.CHECK_WIRELESS_ID + "桁"
                    ,"半角英数" + MeterManagementConstants.CHECK_WIRELESS_ID + "桁"
                    ,"半角英数" + MeterManagementConstants.CHECK_WIRELESS_ID + "桁"
                    ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                    ,"半角数字" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM.length() + "桁(" + MeterManagementConstants.CHECK_PATTERN_EXAM_END_YM + ")"
                    ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                    ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","A123456789","12","123456","1234","ハイブリッド水道メーター","ABCDEF123456","ABCDEF123456","ABCDEF123456","ABCDEF123456","ABCDEF123456","西暦","202401","無効","コメント１２３４５６７８９０１２３４５６"));
            retList.add(inList);

        } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
            // -------------------------------------------------
            // AieLink用メーターの処理
            // 「OCR検針」→「AieLink」へ変更
            // -------------------------------------------------
            inList = new ArrayList<>(Arrays.asList(
                    "# 更新区分"
                    ,"管理番号"
                    ,"計器ID"
                    ,"メーター種別"
                    ,"ユーザーコード"
                    ,"乗率"
                    ,"検満西暦・和暦"
                    ,"検満通知"
                    ,"コメント"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList(
                    "# " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()) + " / " + genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.UPDATE.getVal())
                    ,"半角数字(" + MeterManagementConstants.METERM_MNG_ID_INT_MIN + " - " + MeterManagementConstants.METERM_MNG_ID_INT_MAX + ")"
                    ,"半角英数" + MeterManagementConstants.CHECK_METER_ID_CNT + "桁"
                    ,"半角数字(" + MeterManagementConstants.CHECK_METER_TYPE_MIN + " - " + MeterManagementConstants.CHECK_METER_TYPE_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_BUILDING_ID_MIN + " - " + MeterManagementConstants.CHECK_BUILDING_ID_MAX + ")"
                    ,"半角数字(" + MeterManagementConstants.CHECK_MULTI_MIN + " - " + MeterManagementConstants.CHECK_MULTI_MAX + ")"
                    ,genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal()) + " / " + genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal())
                    ,genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()) + " / " + genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                    ,"最大" + MeterManagementConstants.CHECK_MEMO_CNT + "文字"));
            retList.add(inList);

            inList = new ArrayList<>(Arrays.asList("# （例）更新あり","123","A123456789","12","123456","1234","西暦","無効","コメント１２３４５６７８９０１２３４５６"));
            retList.add(inList);

        }

        inList = new ArrayList<>(Arrays.asList("#この行の下にメーターデータを追加してください。##################################################################################################################################################################################"));
        retList.add(inList);

        return retList;
    }

    /**
     * 登録データのCSVの中身を作る
     * @return 登録データのCSVの中身
     */
    private List<List<String>> createRegistrationDataCsv() throws IllegalAccessException, InvocationTargetException {

        List<List<String>> retList = createCsvTemplate();

        // 出力予定データ取得
        List<MeterInfo> meterList = executeSearch(false, devId);

        // 現在のDB登録データをCSVファイル出力用にマッピング nullの場合は空文字を設定
        for (int i = 0; i < meterList.size(); i++) {

            // 共通項目
            List<String> inList = new ArrayList<>(Arrays.asList(
                    genericTypeList.getUpdateKbn(ApiGenericTypeConstants.UPDATE_KBN.NOT_UPDATE.getVal()), // 更新区分 CSVダウンロードの出力は全て「更新なし」に設定

                    Objects.isNull(meterList.get(i).getMeterMngId()) ? "" : String.valueOf(meterList.get(i).getMeterMngId()), // 管理番号

                    Objects.isNull(meterList.get(i).getMeterId()) ? "" : meterList.get(i).getMeterId(), // 計器ID

                    Objects.isNull(meterList.get(i).getMeterType()) ? "" : String.valueOf(meterList.get(i).getMeterType()), // メーター種別

                    Objects.isNull(meterList.get(i).getTenantId()) ? "" : String.valueOf(meterList.get(i).getTenantId()) // ユーザーコード
                ));

            if (METER_KIND.SMART.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // スマートメーターの処理
                // -------------------------------------------------
                if (lteMDeviceFlg) {
                    // LTE-Mの場合
                    inList.addAll(new ArrayList<>(Arrays.asList(
                            "", // LTE-Mの場合、列「機器への設定送信」は空欄とする。（列をずらさないのは、他のCSVとの互換性や、客先でマクロを使用している場合に座標がずれないようにするため）
                            Objects.isNull(meterList.get(i).getOpenMode()) ? "" : meterList.get(i).getOpenMode().equals(ApiGenericTypeConstants.SMS_OPEN_MODE_302.OPEN.getVal())
                                                    ? genericTypeList.getSmsOpenMode(ApiGenericTypeConstants.SMS_OPEN_MODE_302.OPEN.getVal())
                                                    : genericTypeList.getSmsOpenMode(ApiGenericTypeConstants.SMS_OPEN_MODE_302.CLOSE.getVal()), // 開閉区分 切：0／入：1

                            Objects.isNull(meterList.get(i).getLoadlimitMode()) ? "" : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal())
                                    ? LOAD_LIMIT.INVALID.getVal()
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal())
                                    ? LOAD_LIMIT.VALID.getVal()
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal())
                                    ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal())
                                    : LOAD_LIMIT.NONE.getVal(), // 負荷制限 無効:0／有効:1／臨時設定:A／臨時設定解除:R／機能なし:SP

                            Objects.isNull(meterList.get(i).getLoadCurrent()) ? "" : meterList.get(i).getLoadCurrent(), // 負荷電流(基本)

                            Objects.isNull(meterList.get(i).getAutoInjection()) ? "" : meterList.get(i).getAutoInjection(), // 自動投入(基本)

                            Objects.isNull(meterList.get(i).getBreakerActCount()) ? "" : meterList.get(i).getBreakerActCount(), // 開閉器動作カウント(基本)

                            Objects.isNull(meterList.get(i).getCountClear()) ? "" : meterList.get(i).getCountClear(), // 開閉器カウントクリア(基本)

                            Objects.isNull(meterList.get(i).getTempLoadCurrent()) ? "" : meterList.get(i).getTempLoadCurrent(), // 負荷電流(臨時)

                            Objects.isNull(meterList.get(i).getTempAutoInjection()) ? "" : meterList.get(i).getTempAutoInjection(), // 自動投入(臨時)

                            Objects.isNull(meterList.get(i).getTempBreakerActCount()) ? "" : meterList.get(i).getTempBreakerActCount(), // 開閉器動作カウント(臨時)

                            Objects.isNull(meterList.get(i).getTempCountClear()) ? "" : meterList.get(i).getTempCountClear() // 開閉器カウントクリア(臨時)
                        )));
                } else {
                    // LTE-M以外の場合
                    inList.addAll(new ArrayList<>(Arrays.asList(
                            SEND_DEVICE.NOT_SEND.getVal(), // 機器への設定送信 登録データダウンロードで出力するCSVには「送信しない」を設定

                            Objects.isNull(meterList.get(i).getOpenMode()) ? "" : meterList.get(i).getOpenMode().equals(ApiGenericTypeConstants.SMS_OPEN_MODE_302.OPEN.getVal())
                                                    ? genericTypeList.getSmsOpenMode(ApiGenericTypeConstants.SMS_OPEN_MODE_302.OPEN.getVal())
                                                    : genericTypeList.getSmsOpenMode(ApiGenericTypeConstants.SMS_OPEN_MODE_302.CLOSE.getVal()), // 開閉区分 切：0／入：1

                            Objects.isNull(meterList.get(i).getLoadlimitMode()) ? "" : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.INVALID.getVal())
                                    ? LOAD_LIMIT.INVALID.getVal()
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.VALID.getVal())
                                    ? LOAD_LIMIT.VALID.getVal()
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.SETTING.getVal())
                                    : meterList.get(i).getLoadlimitMode().equals(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal())
                                    ? genericTypeList.getSmsLoadLimit(ApiGenericTypeConstants.SMS_LOAD_LIMIT_300.CANCEL.getVal())
                                    : LOAD_LIMIT.NONE.getVal(), // 負荷制限 無効:0／有効:1／臨時設定:A／臨時設定解除:R／機能なし:SP

                            Objects.isNull(meterList.get(i).getLoadCurrent()) ? "" : meterList.get(i).getLoadCurrent(), // 負荷電流(基本)

                            Objects.isNull(meterList.get(i).getAutoInjection()) ? "" : meterList.get(i).getAutoInjection(), // 自動投入(基本)

                            Objects.isNull(meterList.get(i).getBreakerActCount()) ? "" : meterList.get(i).getBreakerActCount(), // 開閉器動作カウント(基本)

                            Objects.isNull(meterList.get(i).getCountClear()) ? "" : meterList.get(i).getCountClear(), // 開閉器カウントクリア(基本)

                            Objects.isNull(meterList.get(i).getTempLoadCurrent()) ? "" : meterList.get(i).getTempLoadCurrent(), // 負荷電流(臨時)

                            Objects.isNull(meterList.get(i).getTempAutoInjection()) ? "" : meterList.get(i).getTempAutoInjection(), // 自動投入(臨時)

                            Objects.isNull(meterList.get(i).getTempBreakerActCount()) ? "" : meterList.get(i).getTempBreakerActCount(), // 開閉器動作カウント(臨時)

                            Objects.isNull(meterList.get(i).getTempCountClear()) ? "" : meterList.get(i).getTempCountClear() // 開閉器カウントクリア(臨時)
                        )));
                }
            } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // パルスメーターの処理
                // -------------------------------------------------
                inList.addAll(new ArrayList<>(Arrays.asList(
                        SEND_DEVICE.NOT_SEND.getVal(), // 機器への設定送信 登録データダウンロードで出力するCSVには「送信しない」を設定

                        Objects.isNull(meterList.get(i).getMulti()) ? "" : String.valueOf(meterList.get(i).getMulti()), // 乗率

                        CHECK_CHANGE.NOT_CHANGE.getVal(), // パルス種別変更フラグ 登録データダウンロードで出力するCSVには「変更しない」を設定

                        Objects.isNull(meterList.get(i).getPulseType()) ? "" : meterList.get(i).getPulseType().equals(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.SHORT_PULSE.getVal())
                                ? genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.SHORT_PULSE.getVal())
                                : meterList.get(i).getPulseType().equals(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.LONG_PULSE.getVal())
                                ? genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.LONG_PULSE.getVal())
                                : meterList.get(i).getPulseType().equals(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.STATUS.getVal())
                                ? genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.STATUS.getVal())
                                : genericTypeList.getSmsPulseType(ApiGenericTypeConstants.SMS_PULSE_TYPE_322.INVALID.getVal()), // パルス種別 無効: 0 / 短パルス: 1 / 長パルス: 2 / 状態: 3

                        CHECK_CHANGE.NOT_CHANGE.getVal(), // パルス重み変更フラグ 登録データダウンロードで出力するCSVには「変更しない」を設定

                        Objects.isNull(meterList.get(i).getPulseWeight()) ? "" : String.valueOf(meterList.get(i).getPulseWeight()), // パルス重み(×0.01) 機器にてパルス重みを(×0.01)した値を保持するが、サーバサイドではDBのパルス重みに登録された値を取得する

                        CHECK_CHANGE.NOT_CHANGE.getVal(), // 指針値変更フラグ 登録データダウンロードで出力するCSVには「変更しない」を設定

                        Objects.isNull(meterList.get(i).getCurrentData()) ? "" : String.valueOf(meterList.get(i).getCurrentData()) // 指針値(×0.01) 機器にて指針値を(×0.01)した値を保持するが、サーバサイドではDBの指針値に登録された値を取得する
                    )));

            } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // IoT-R連携用メーターの処理
                // -------------------------------------------------
                // IoT-R連携用メーター特有の設定項目は無し

            } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // ハンディ検針用メーターの処理
                // -------------------------------------------------
                inList.addAll(new ArrayList<>(Arrays.asList(
                        Objects.isNull(meterList.get(i).getMulti()) ? "" : String.valueOf(meterList.get(i).getMulti()), // 乗率

                        Objects.isNull(meterList.get(i).getWirelessType()) ? "" : meterList.get(i).getWirelessType().equals(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.CURRENT.getVal())
                                ? genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.CURRENT.getVal())
                                : meterList.get(i).getWirelessType().equals(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.ELECTRONIC.getVal())
                                ? genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.ELECTRONIC.getVal())
                                : genericTypeList.getSmsHandyTypeByName(ApiGenericTypeConstants.SMS_HANDY_TYPE_326.HYBRID.getVal()), // 無線種別 ハイブリッド水道メーター: 0 / カレント式電力量計: 1 / 電子式水道メーター: 2

                        Objects.isNull(meterList.get(i).getWirelessId()) ? "" : String.valueOf(meterList.get(i).getWirelessId()), // 無線ID

                        Objects.isNull(meterList.get(i).getHop1Id()) ? "" : String.valueOf(meterList.get(i).getHop1Id()), // リレー無線ID1

                        Objects.isNull(meterList.get(i).getHop2Id()) ? "" : String.valueOf(meterList.get(i).getHop2Id()), // リレー無線ID2

                        Objects.isNull(meterList.get(i).getHop3Id()) ? "" : String.valueOf(meterList.get(i).getHop3Id()), // リレー無線ID3

                        Objects.isNull(meterList.get(i).getPollingId()) ? "" : String.valueOf(meterList.get(i).getPollingId()) // ポーリンググループNo
                    )));
            } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
                // -------------------------------------------------
                // AieLink用メーターの処理
                // 「OCR検針」→「AieLink」へ変更
                // -------------------------------------------------
                inList.addAll(new ArrayList<>(Arrays.asList(
                        Objects.isNull(meterList.get(i).getMulti()) ? "" : String.valueOf(meterList.get(i).getMulti()), // 乗率

                        Objects.isNull(meterList.get(i).getDispYearFlg()) ? "" : meterList.get(i).getDispYearFlg().equals(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal())
                                ? genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal())
                                : genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal()), // 検満西暦・和暦 西暦：0／和暦：1

                        Objects.isNull(meterList.get(i).getExamNotice()) ? "" : meterList.get(i).getExamNotice().equals(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                                ? genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                                : genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()), // 検満通知 無効：0／有効：1

                        Objects.isNull(meterList.get(i).getMemo()) ? "" : meterList.get(i).getMemo() // コメント
                    )));
            }

            // 「OCR検針」→「AieLink」へ変更
            // AieLink用メーター以外の場合
            if (!METER_KIND.OCR.getVal().equals(meterKind)) {
                // 共通項目
                inList.addAll(new ArrayList<>(Arrays.asList(
                        Objects.isNull(meterList.get(i).getDispYearFlg()) ? "" : meterList.get(i).getDispYearFlg().equals(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal())
                                ? genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.GREGORIAN_CALENDER.getVal())
                                : genericTypeList.getSmsDispYear(ApiGenericTypeConstants.SMS_DISP_YEAR_303.JAPANESE_CALENDER.getVal()), // 検満西暦・和暦 西暦：0／和暦：1

                        Objects.isNull(meterList.get(i).getExamEndYm()) ? "" : meterList.get(i).getExamEndYm().replace(".", ""), // 検満年月 yyyy.MMのためyyyyMMへ変換

                        Objects.isNull(meterList.get(i).getExamNotice()) ? "" : meterList.get(i).getExamNotice().equals(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                                ? genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.VALID.getVal())
                                : genericTypeList.getSmsExamNotic(ApiGenericTypeConstants.SMS_EXAM_NOTIC_305.INVALID.getVal()), // 検満通知 無効：0／有効：1

                        Objects.isNull(meterList.get(i).getMemo()) ? "" : meterList.get(i).getMemo() // コメント
                    )));
            }

            retList.add(inList);
        }
        return retList;
    }

    /**
     * yyyyMM(西暦年月)を和暦に変換する
     * @param ym
     * @return [0]:元号(H:平成, R:令和), [1]:年, [2]:月
     */
    private String[] convertToJpn(String ym) {

        // javaのバージョン的に令和に対応していないのでゴリゴリ処理していく
        // 平成:1989年 1月 8日 – 2019年 4月30日
        // 令和:2019年 5月 1日 - 2999年12月31日（ひとまずMAXを定義）

        // 和暦変換テーブル
        String warekiTable[][] = {
                { "19890108", "20190431", MeterManagementConstants.DISP_YEAR_GENGO_H }, //平成
                { "20190501", "99991231", MeterManagementConstants.DISP_YEAR_GENGO_R } }; //令和

        // YYYYMMDDの形式に変換する
        String ymd = ym + "08";
        // 暦変換テーブルをサーチする
        int i = 0;
        for (i = 0; i < warekiTable.length; i++) {
            // 当該西暦が開始年以上で最終年以下ならbreak
            if (ymd.compareTo(warekiTable[i][0]) >= 0 && ymd.compareTo(warekiTable[i][1]) <= 0) {
                break;
            }
        }
        // 暦テーブルに該当レコードがあれば、和暦年を計算する
        if (i < warekiTable.length) {
            int jpYear = Integer.parseInt(ymd.substring(0, 4)) - Integer.parseInt(warekiTable[i][0].substring(0, 4))
                    + 1;
            // 和号+和暦年+月を返す
            String ret[] = { warekiTable[i][2], Integer.toString(jpYear),
                    String.format("%02d", Integer.parseInt(ymd.substring(4, 6))) };
            return ret;
        }
        return null;
    }

    /**
     * yyMM(和暦年月)を西暦に変換する
     * @param gengo 元号
     * @param year 和暦 年
     * @param month 月
     * @return 西暦年月(yyyymm)
     */
    private String convertToAd(String gengo, String year, String month) {
        int baseYear = 0;
        if (MeterManagementConstants.DISP_YEAR_GENGO_CODE_H.equals(gengo)) {
            baseYear = 1989 - 1;
        } else {
            baseYear = 2019 - 1;
        }
        return String.format("%04d", baseYear + Integer.parseInt(year))
                + String.format("%02d", Integer.parseInt(month));
    }

    /**
     * 検満年月をDB登録用に整形する
     * @param dispYearFlg 西暦・和暦表示確認用フラグ
     * @param gengo 元号
     * @param year 検満_年
     * @param yearWareki 検満_年(和暦用)
     * @param month 検満_月
     * @return 整形後の検満年月(yyyyMM)
     */
    private String convertToExamEndYm(String dispYearFlg, String gengo, String year, String yearWareki, String month) {
        String ret = null;
        if (MeterManagementConstants.FLG_OFF.equals(dispYearFlg)) {
            // 西暦が選択されている場合
            if (!CheckUtility.isNullOrEmpty(year) && !CheckUtility.isNullOrEmpty(month)) {
                ret = String.format("%04d", Integer.parseInt(year)) + String.format("%02d", Integer.parseInt(month));
            }
        } else {
            // 和暦が選択されている場合
            if (!MeterManagementConstants.FLG_OFF.equals(gengo) && !CheckUtility.isNullOrEmpty(yearWareki)
                    && !CheckUtility.isNullOrEmpty(month)) {
                ret = convertToAd(gengo, yearWareki, month);
            }
        }
        return ret;
    }

    /**
     * メーター情報のメーター管理IDを全件取得する
     * @param conditionList
     * @return メーター管理IDリスト
     */
    private List<Long> getAllMeterMngId() {
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getAllMeterMngId():START"));
        // 検索
        ListSmsMeterResponse response = new ListSmsMeterResponse();
        ListSmsMeterParameter parameter = new ListSmsMeterParameter();

        // APIBeanの指定
        parameter.setBean("ListSmsMeterSearchBean");
        parameter.setCorpId(buildingInfo.getCorpId());
        parameter.setBuildingId(Long.valueOf(buildingInfo.getBuildingId()));
        parameter.setDevId(smartProperty.getDevId());
        parameter.setMeterKind(meterKind); // メーター種類(選択タブにあわせて変更する)

        // DBから値取得 ページ・件数指定はしない（全件取得）
        response = callApiPost(parameter, ListSmsMeterResponse.class);

        List<Long> idList = new ArrayList<>();
        // 画面表示用にリストの詰め替え
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            // DBから取得できた件数分ループして詰め替えていく
            for (ListSmsMeterResultData getData : response.getResult().getList()) {
                // 詰め
                idList.add(getData.getMeterMngId());
            }
        }
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getAllMeterMngId():END"));
        return idList;
    }

    /**
     * メーター管理IDのリストを表示用に成形してMapに詰める
     * @param list メーター管理IDのリスト
     * @return メーター管理IDのリストを表示用に成形してMapに詰めたもの
     */
    private void createPagerMap(List<Long> list) {
        pagerMap = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0 || (i % SmsConstants.METER_PER_PAGE) == 0) {
                pagerMap.put(list.get(i), i);
            }
        }
    }

    /**
     * メーター管理IDのリストを表示用に成形してMapに詰める
     * @param list メーター管理IDのリスト
     * @return メーター管理IDのリストを表示用に成形してMapに詰めたもの
     */
    private void createPagerMapFromResult(List<ListSmsMeterResultData> orglist) {
        for (int i = 0; i < orglist.size(); i++) {
            if (i == 0 || (i % SmsConstants.METER_PER_PAGE) == 0) {
                pagerMap.put(orglist.get(i).getMeterMngId(), i);
            }
        }
    }

    /**
     * 引数のmapのキーをvalueから逆引き
     * @param map
     * @param value
     * @return 引数valueに紐づくキー
     */
    public String getKey(Map<String, String> map, String value) {
        BidiMap<String, String> bidiMap = new DualHashBidiMap<String, String>(map);
        String key = bidiMap.getKey(value);
        return key;
    }

    /**
     * 「接続先」セレクトボックス変更時の処理
     */
    @Logged
    public void changeDevIdSel(String id) {
        smartProperty.setDevId(id);
        pulseProperty.setDevId(id);
        iotRProperty.setDevId(id);
        handyProperty.setDevId(id);
        ocrProperty.setDevId(id);
        // メーター切替のMap更新
        createPagerMap(getAllMeterMngId());

        // スマートメーターとパルスメーターの場合
        if (METER_KIND.SMART.getVal().equals(meterKind) || METER_KIND.PULSE.getVal().equals(meterKind)) {
            // 接続先の保持
            changeSmartPulseTabKeepDevId = id;
        }
    }

    /**
     * 検索条件の変更によって表示が変わる部分の更新
     */
    private void dispUpdate() {
        // 検満通知月の表示を更新
        updateExamNoticeMonth();

        // 各画面の装置名を更新
        String tmpDevId = smartProperty.getDevId();
        String tmpDevName = getKey(devIdMap, tmpDevId);
        smartProperty.setDevName(tmpDevName);
        pulseProperty.setDevName(tmpDevName);
        iotRProperty.setDevName(tmpDevName);
        handyProperty.setDevName(tmpDevName);
        ocrProperty.setDevName(tmpDevName);
        // 各画面の装置IDを更新
        smartInsertProperty.setDevId(tmpDevId);
        smartUpdateProperty.setDevId(tmpDevId);
        smartSettingUpdateProperty.setDevId(tmpDevId);
        pulseInsertProperty.setDevId(tmpDevId);
        pulseUpdateProperty.setDevId(tmpDevId);
        pulseSettingUpdateProperty.setDevId(tmpDevId);
        iotRInsertProperty.setDevId(tmpDevId);
        iotRUpdateProperty.setDevId(tmpDevId);
        iotRSettingUpdateProperty.setDevId(tmpDevId);
        handyInsertProperty.setDevId(tmpDevId);
        handyUpdateProperty.setDevId(tmpDevId);
        ocrInsertProperty.setDevId(tmpDevId);
        ocrUpdateProperty.setDevId(tmpDevId);

        // メーター管理番号のMap更新（新規登録系画面）
        getNotRegisteredMeterMngId();
    }

    @EJB
    private MDevPrmDao mDevPrmDao;

    /**
     * 装置情報取得
     * @return 装置情報
     */
    private MDevPrm getMDevPrm() {
        MDevPrm param = new MDevPrm();
        param.setDevId(smartProperty.getDevId());
        MDevPrm ret = mDevPrmDao.find(param);
        return ret;
    }

    /**
     * 接続先の現在の選択値を条件に検満通知月の表示を更新する
     */
    private void updateExamNoticeMonth() {
        // 装置情報取得
        MDevPrm getMDevPrmData = getMDevPrm();
        // 取得できなかった場合は--を表示する
        examNoticeMonth = "--";
        if (getMDevPrmData != null && getMDevPrmData.getExamNoticeMonth() != null) {
            examNoticeMonth = getMDevPrmData.getExamNoticeMonth().toString();
        }
    }

    /** 現在登録可能なメーター管理IDの最小値 */
    private Long minMeterMngId;

    /**
     * メーター管理番号に未登録の番号をすべて取得する
     * @return メーター管理番号に未登録の番号map
     */
    private Map<Long, Long> getNotRegisteredMeterMngId() {
        Map<Long, Long> map = new LinkedHashMap<>();
        // 既に登録済となっているメーター管理番号を全件 リストで取得
        List<Long> registeredAllList = getAllMeterMngIdByDevId();
        boolean minFlg = true;
        // メーター管理番号登録可能範囲の下限値から上限値まで繰り返し処理する
        for (Long i = MeterManagementConstants.METERM_MNG_ID_MIN; i <= MeterManagementConstants.METERM_MNG_ID_MAX; i++) {
            // mapに追加しようとしている番号が登録済でないことを確認
            if (!registeredAllList.contains(i)) {
                map.put(i, i);
            }
            // 入力値プロパティを初期化する際に使いたいので最小値をとっておく
            if (minFlg) {
                minMeterMngId = i;
                minFlg = false;
            }
        }
        this.isListMax = map.size() == 0;
        return map;
    }

    /**
     * 装置IDを条件にメーター情報のメーター管理IDを全件取得する（他の条件設定なし）
     * @return メーター管理IDリスト
     */
    private List<Long> getAllMeterMngIdByDevId() {

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getAllMeterMngIdByDevId():START"));
        // 検索
        ListSmsMeterResponse response = new ListSmsMeterResponse();
        ListSmsMeterParameter parameter = new ListSmsMeterParameter();

        // APIBeanの指定
        parameter.setBean("listSmsMeterMngIdByDevIdBean");
        parameter.setDevId(smartProperty.getDevId());

        // DBから値取得
        response = callApiPost(parameter, ListSmsMeterResponse.class);

        List<Long> idList = new ArrayList<>();
        // 画面表示用にリストの詰め替え
        if (response.getResult() != null) {
            // DBから取得できた件数分ループして詰め替えていく
            for (ListSmsMeterResultData getData : response.getResult().getList()) {
                Long id = getData.getMeterMngId();
                // 詰め
                idList.add(id);
            }
        }
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getAllMeterMngIdByDevId():END"));
        return idList;
    }

    private Long firstMeterType;

    /**
     * メーター種別をすべて取得
     * @return メーター種別map
     */
    private Map<String, Long> getMeterTypeAll() {

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getMeterTypeAll():START"));

        Map<String, Long> map = new LinkedHashMap<>();
        // 検索
        ListSmsMeterResponse response = new ListSmsMeterResponse();
        ListSmsMeterParameter parameter = new ListSmsMeterParameter();

        // APIBeanの指定
        parameter.setBean("listSmsMeterMeterTypeBean");
        parameter.setCorpId(buildingInfo.getCorpId());
        parameter.setBuildingId(Long.valueOf(buildingInfo.getBuildingId()));

        // DBから値取得
        response = callApiPost(parameter, ListSmsMeterResponse.class);

        // 画面表示用にリストの詰め替え
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            boolean firstFlg = true;
            // DBから取得できた件数分ループして詰め替えていく
            for (ListSmsMeterResultData getData : response.getResult().getList()) {
                // 詰め
                map.put(getData.getMeterTypeName(), getData.getMeterType());

                // 入力値プロパティを初期化する際に使いたいので最小値をとっておく
                if (firstFlg) {
                    firstMeterType = getData.getMeterType();
                    firstFlg = false;
                }
            }
        }
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:getMeterTypeAll():END"));

        return map;
    }

    /**
     * 「選択」チェックボックスの状態が変化したときに実行される処理
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Logged
    public void checkBoxOperation() throws IllegalAccessException, InvocationTargetException {
        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat("  smsCollectSettingMeterMeterManagementBean:checkBoxOperation():START"));
        //        boolean selectFlg = meterList.stream().anyMatch(x -> x.getCheckBox());
        nowValMeterMngIdMap = new LinkedHashMap<>();
        for (MeterInfo meterInfo : meterList) {
            if (meterInfo.getCheckBox() != null && meterInfo.getCheckBox()) {
                // メーター種類を選択タブにあわせて動的に値を詰めるpropertyを変更する
                if (METER_KIND.SMART.getVal().equals(meterKind) || meterKind == null) {
                    copyUpdatePropertySmart(meterInfo);
                    copyUpdatePropertySmartMeterStatus(meterInfo);
                } else if (METER_KIND.PULSE.getVal().equals(meterKind)) {
                    copyUpdatePropertyPulse(meterInfo);
                    copyUpdatePropertyPulseMeterStatus(meterInfo);
                } else if (METER_KIND.IOTR.getVal().equals(meterKind)) {
                    copyUpdatePropertyIotR(meterInfo);
                    copyUpdatePropertyIotRMeterStatus(meterInfo);
                } else if (METER_KIND.HANDY.getVal().equals(meterKind)) {
                    copyUpdatePropertyHandy(meterInfo);
                } else if (METER_KIND.OCR.getVal().equals(meterKind)) {
                    copyUpdatePropertyOcr(meterInfo);

                } else {
                    new Exception();
                }
            }
        }

        // LTE-Mの場合、設定内容変更画面の初期値を設定
        if (lteMDeviceFlg) {
            smartSettingUpdateProperty.setLoadlimitMode(null);
            smartSettingUpdateProperty.setLoadCurrent(null);
            smartSettingUpdateProperty.setTempLoadCurrent(null);
            smartSettingUpdateProperty.setAutoInjection(null);
            smartSettingUpdateProperty.setTempAutoInjection(null);
            smartSettingUpdateProperty.setBreakerActCount(null);
            smartSettingUpdateProperty.setTempBreakerActCount(null);
            smartSettingUpdateProperty.setCountClear(null);
            smartSettingUpdateProperty.setTempCountClear(null);
            smartSettingUpdateProperty.setOpenMode(null);
        }

        eventLogger.debug(MeterManagementBean.class.getPackage().getName()
                .concat(" smsCollectSettingMeterMeterManagementBean:checkBoxOperation():END"));
    }

    /**
     * 設定内容変更にて非活性項目の初期化
     */
    public void initSmartSettingUpdateProperty() {

        invalidComponent.clear(); // エラー(赤色)表示を解除
        String form = "smsCollectSettingMeterMeterManagementBean:smartSettingEditForm:";
        if (LOAD_LIMIT_MODE.INVALID.getVal().equals(smartSettingUpdateProperty.getLoadlimitMode())
                || LOAD_LIMIT_MODE.VALID.getVal().equals(smartSettingUpdateProperty.getLoadlimitMode())) {
            // 「なし」「あり」の場合、臨時設定を非活性化
            smartSettingUpdateProperty.setTempLoadCurrent(null);
            smartSettingUpdateProperty.setTempAutoInjection(null);
            smartSettingUpdateProperty.setTempBreakerActCount(null);
            smartSettingUpdateProperty.setTempCountClear(null);
            resetInputs(form + "s5TmpCurlimitSelectGroup");
            resetInputs(form + "s5TmpAutorecGroup");
            resetInputs(form + "s5TmpOccountSelectGroup");
            resetInputs(form + "s5TmpCntclrSelectGroup");

        } else if (LOAD_LIMIT_MODE.TEMP_VALID.getVal().equals(smartSettingUpdateProperty.getLoadlimitMode())) {
            // 「臨時設定」の場合、基本設定を非活性化
            smartSettingUpdateProperty.setLoadCurrent(null);
            smartSettingUpdateProperty.setAutoInjection(null);
            smartSettingUpdateProperty.setBreakerActCount(null);
            smartSettingUpdateProperty.setCountClear(null);
            resetInputs(form + "s5BasicCurlimitSelectGroup");
            resetInputs(form + "s5BasicAutorecGroup");
            resetInputs(form + "s5BasicOccountSelectGroup");
            resetInputs(form + "s5BasicCntclrSelectGroup");

        } else if (LOAD_LIMIT_MODE.TEMP_INVALID.getVal().equals(smartSettingUpdateProperty.getLoadlimitMode())) {
            // 「臨時解除」の場合、基本設定と臨時設定を非活性化
            smartSettingUpdateProperty.setLoadCurrent(null);
            smartSettingUpdateProperty.setAutoInjection(null);
            smartSettingUpdateProperty.setBreakerActCount(null);
            smartSettingUpdateProperty.setCountClear(null);
            smartSettingUpdateProperty.setTempLoadCurrent(null);
            smartSettingUpdateProperty.setTempAutoInjection(null);
            smartSettingUpdateProperty.setTempBreakerActCount(null);
            smartSettingUpdateProperty.setTempCountClear(null);
            resetInputs(form + "s5BasicCurlimitSelectGroup");
            resetInputs(form + "s5BasicAutorecGroup");
            resetInputs(form + "s5BasicOccountSelectGroup");
            resetInputs(form + "s5BasicCntclrSelectGroup");
            resetInputs(form + "s5TmpCurlimitSelectGroup");
            resetInputs(form + "s5TmpAutorecGroup");
            resetInputs(form + "s5TmpOccountSelectGroup");
            resetInputs(form + "s5TmpCntclrSelectGroup");

        } else {
            // 「--要選択--」の場合、基本設定と臨時設定を非活性化
            smartSettingUpdateProperty.setLoadlimitMode(null);
            smartSettingUpdateProperty.setLoadCurrent(null);
            smartSettingUpdateProperty.setAutoInjection(null);
            smartSettingUpdateProperty.setBreakerActCount(null);
            smartSettingUpdateProperty.setCountClear(null);
            smartSettingUpdateProperty.setTempLoadCurrent(null);
            smartSettingUpdateProperty.setTempAutoInjection(null);
            smartSettingUpdateProperty.setTempBreakerActCount(null);
            smartSettingUpdateProperty.setTempCountClear(null);
            resetInputs(form + "s5BasicCurlimitSelectGroup");
            resetInputs(form + "s5BasicAutorecGroup");
            resetInputs(form + "s5BasicOccountSelectGroup");
            resetInputs(form + "s5BasicCntclrSelectGroup");
            resetInputs(form + "s5TmpCurlimitSelectGroup");
            resetInputs(form + "s5TmpAutorecGroup");
            resetInputs(form + "s5TmpOccountSelectGroup");
            resetInputs(form + "s5TmpCntclrSelectGroup");
        }
    }

    /**
     * UIInputの値を初期化
     *
     * @param containerClientId 初期化対象クライアントID
     */
    private void resetInputs(String containerClientId) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent root = fc.getViewRoot().findComponent(containerClientId);
        if (root == null) {
            return;
        }
        root.visitTree(VisitContext.createVisitContext(fc), (ctx, comp) -> {
            if (comp instanceof UIInput) {
                ((UIInput) comp).resetValue();
            }
            return VisitResult.ACCEPT;
        });
    }

    /**
     * スマート変更画面用property詰め
     * @param meterInfo
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void copyUpdatePropertySmart(MeterInfo meterInfo) throws IllegalAccessException, InvocationTargetException {
        // ◆初期化
        // スマメ更新用プロパティ
        smartUpdateProperty = new MeterManagementInputProperty();
        // スマメ設定更新用プロパテ
        smartSettingUpdateProperty = new MeterManagementInputProperty();

        // ◆登録内容更新画面用プロパティを詰める
        // 現在値取得画面のメーター管理番号セレクトボックスにセット
        nowValMeterMngIdMap.put(meterInfo.getMeterMngId(), meterInfo.getMeterMngId());
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        smartUpdateProperty = new MeterManagementInputProperty();
        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(smartUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
        // copyPropertiesでコピーできないものを個別でセット
        if (!CheckUtility.isNullOrEmpty(meterInfo.getExamEndYm())) {
            // yyyy.MMの形からyyyyとMMに分解
            String[] tmpYmArray = meterInfo.getExamEndYm().split(Pattern.quote("."));
            // 西暦表示用
            smartUpdateProperty.setExamEndYear(tmpYmArray[0]);
            smartUpdateProperty.setExamEndMonth(tmpYmArray[1]);

            // 和暦表示用 年に変換→それぞれの値をセット
            String[] ymArray = convertToJpn(tmpYmArray[0] + tmpYmArray[1]);
            smartUpdateProperty.setExamEndYmGengo(
                    MeterManagementConstants.DISP_YEAR_GENGO_H.equals(ymArray[0])
                            ? MeterManagementConstants.DISP_YEAR_GENGO_CODE_H
                            : MeterManagementConstants.DISP_YEAR_GENGO_CODE_R);
            smartUpdateProperty.setExamEndYearWareki(ymArray[1]);
        }

        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        // ◆設定内容更新画面用プロパティを詰める
        BeanUtils.copyProperties(smartSettingUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
    }

    /**
     * パルス変更画面用property詰め
     * @param meterInfo
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void copyUpdatePropertyPulse(MeterInfo meterInfo) throws IllegalAccessException, InvocationTargetException {
        // ◆初期化
        // パルス更新用プロパティ
        pulseUpdateProperty = new MeterManagementInputProperty();
        // パルス設定更新用プロパテ
        pulseSettingUpdateProperty = new MeterManagementInputProperty();

        // ◆登録内容更新画面用プロパティを詰める
        // 現在値取得画面のメーター管理番号セレクトボックスにセット
        nowValMeterMngIdMap.put(meterInfo.getMeterMngId(), meterInfo.getMeterMngId());
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        pulseUpdateProperty = new MeterManagementInputProperty();
        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(pulseUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
        // copyPropertiesでコピーできないものを個別でセット
        if (!CheckUtility.isNullOrEmpty(meterInfo.getExamEndYm())) {
            // yyyy.MMの形からyyyyとMMに分解
            String[] tmpYmArray = meterInfo.getExamEndYm().split(Pattern.quote("."));
            // 西暦表示用
            pulseUpdateProperty.setExamEndYear(tmpYmArray[0]);
            pulseUpdateProperty.setExamEndMonth(tmpYmArray[1]);

            // 和暦表示用 年に変換→それぞれの値をセット
            String[] ymArray = convertToJpn(tmpYmArray[0] + tmpYmArray[1]);
            pulseUpdateProperty.setExamEndYmGengo(
                    MeterManagementConstants.DISP_YEAR_GENGO_H.equals(ymArray[0])
                            ? MeterManagementConstants.DISP_YEAR_GENGO_CODE_H
                            : MeterManagementConstants.DISP_YEAR_GENGO_CODE_R);
            pulseUpdateProperty.setExamEndYearWareki(ymArray[1]);
        }

        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        // ◆設定内容更新画面用プロパティを詰める
        BeanUtils.copyProperties(pulseSettingUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();

        // 現在値変更フラグ
        if (MeterManagementConstants.FLG_ON.equals(meterInfo.getCurrentDataChg())) {
            pulseSettingUpdateProperty.setCurrentDataChg(true);
        } else {
            pulseSettingUpdateProperty.setCurrentDataChg(false);
        }
        // パルス種別変更フラグ
        if (MeterManagementConstants.FLG_ON.equals(meterInfo.getPulseTypeChg())) {
            pulseSettingUpdateProperty.setPulseTypeChg(true);
        } else {
            pulseSettingUpdateProperty.setPulseTypeChg(false);
        }
        // パルス重み変更フラグ
        if (MeterManagementConstants.FLG_ON.equals(meterInfo.getPulseWeightChg())) {
            pulseSettingUpdateProperty.setPulseWeightChg(true);
        } else {
            pulseSettingUpdateProperty.setPulseWeightChg(false);
        }
    }

    /**
     * IoT-R変更画面用property詰め
     * @param meterInfo
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void copyUpdatePropertyIotR(MeterInfo meterInfo) throws IllegalAccessException, InvocationTargetException {
        // ◆初期化
        // Iot-R更新用プロパティ
        iotRUpdateProperty = new MeterManagementInputProperty();

        // ◆登録内容更新画面用プロパティを詰める
        // 現在値取得画面のメーター管理番号セレクトボックスにセット
        nowValMeterMngIdMap.put(meterInfo.getMeterMngId(), meterInfo.getMeterMngId());
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        iotRUpdateProperty = new MeterManagementInputProperty();
        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(iotRUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
        // copyPropertiesでコピーできないものを個別でセット
        if (!CheckUtility.isNullOrEmpty(meterInfo.getExamEndYm())) {
            // yyyy.MMの形からyyyyとMMに分解
            String[] tmpYmArray = meterInfo.getExamEndYm().split(Pattern.quote("."));
            // 西暦表示用
            iotRUpdateProperty.setExamEndYear(tmpYmArray[0]);
            iotRUpdateProperty.setExamEndMonth(tmpYmArray[1]);

            // 和暦表示用 年に変換→それぞれの値をセット
            String[] ymArray = convertToJpn(tmpYmArray[0] + tmpYmArray[1]);
            iotRUpdateProperty.setExamEndYmGengo(
                    MeterManagementConstants.DISP_YEAR_GENGO_H.equals(ymArray[0])
                            ? MeterManagementConstants.DISP_YEAR_GENGO_CODE_H
                            : MeterManagementConstants.DISP_YEAR_GENGO_CODE_R);
            iotRUpdateProperty.setExamEndYearWareki(ymArray[1]);
        }
    }

    /**
     * ハンディ変更画面用property詰め
     * @param meterInfo
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void copyUpdatePropertyHandy(MeterInfo meterInfo) throws IllegalAccessException, InvocationTargetException {
        // ◆初期化
        // ハンディ更新用プロパティ
        handyUpdateProperty = new MeterManagementInputProperty();

        // ◆登録内容更新画面用プロパティを詰める
        // 現在値取得画面のメーター管理番号セレクトボックスにセット
        nowValMeterMngIdMap.put(meterInfo.getMeterMngId(), meterInfo.getMeterMngId());
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        handyUpdateProperty = new MeterManagementInputProperty();
        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(handyUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
        // copyPropertiesでコピーできないものを個別でセット
        if (!CheckUtility.isNullOrEmpty(meterInfo.getExamEndYm())) {
            // yyyy.MMの形からyyyyとMMに分解
            String[] tmpYmArray = meterInfo.getExamEndYm().split(Pattern.quote("."));
            // 西暦表示用
            handyUpdateProperty.setExamEndYear(tmpYmArray[0]);
            handyUpdateProperty.setExamEndMonth(tmpYmArray[1]);

            // 和暦表示用 年に変換→それぞれの値をセット
            String[] ymArray = convertToJpn(tmpYmArray[0] + tmpYmArray[1]);
            handyUpdateProperty.setExamEndYmGengo(
                    MeterManagementConstants.DISP_YEAR_GENGO_H.equals(ymArray[0])
                            ? MeterManagementConstants.DISP_YEAR_GENGO_CODE_H
                            : MeterManagementConstants.DISP_YEAR_GENGO_CODE_R);
            handyUpdateProperty.setExamEndYearWareki(ymArray[1]);
        }
    }

    /**
     * スマートメーター状態登録用property詰め
     * @param meterInfo
     */
    private void copyUpdatePropertySmartMeterStatus(MeterInfo meterInfo) {
        // メーター状態更新用プロパティ
        smartMeterStatusProperty = new MeterStatusProperty();
        // ◆登録内容更新画面用プロパティを詰める
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        smartMeterStatusProperty.setMeterPresentSituationDisp(meterInfo.getMeterPresentSituation().toString());
        BigDecimal apf = new BigDecimal("0");
        // アラート停止フラグの型変換
        if (meterInfo.getAlertPauseFlg().equals(apf)) {
            smartMeterStatusProperty.setAlertPauseFlgBool(false);
        } else {
            smartMeterStatusProperty.setAlertPauseFlgBool(true);
            // アラート停止期間
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseStart())) {
                smartMeterStatusProperty.setAlertPauseStart(meterInfo.getAlertPauseStart());
                try {
                    smartMeterStatusProperty.setCalAlertPauseStart(sdf.parse(meterInfo.getAlertPauseStart()));
                } catch (ParseException e) {
                    smartMeterStatusProperty.setCalAlertPauseStart(null);
                }
            }
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseEnd())) {
                smartMeterStatusProperty.setAlertPauseEnd(meterInfo.getAlertPauseEnd());
                try {
                    smartMeterStatusProperty.setCalAlertPauseEnd(sdf.parse(meterInfo.getAlertPauseEnd()));
                } catch (ParseException e) {
                    smartMeterStatusProperty.setCalAlertPauseEnd(null);
                }
            }
        }
        smartMeterStatusProperty.setMeterStatusMemo(meterInfo.getMeterStatusMemo());
        smartMeterStatusProperty.setMeterId(meterInfo.getMeterId());
    }

    /**
     * パルスメーター状態登録用property詰め
     * @param meterInfo
     */
    private void copyUpdatePropertyPulseMeterStatus(MeterInfo meterInfo) {
        // メーター状態更新用プロパティ
        pulseMeterStatusProperty = new MeterStatusProperty();
        // ◆登録内容更新画面用プロパティを詰める
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        if (!Objects.isNull(meterInfo.getMeterPresentSituation())) {
            pulseMeterStatusProperty.setMeterPresentSituationDisp(meterInfo.getMeterPresentSituation().toString());
        } else {
            pulseMeterStatusProperty.setMeterPresentSituationDisp("0");
        }
        BigDecimal apf = new BigDecimal("0");
        // アラート停止フラグの型変換
        if (Objects.isNull(meterInfo.getAlertPauseFlg()) || meterInfo.getAlertPauseFlg().equals(apf)) {
            pulseMeterStatusProperty.setAlertPauseFlgBool(false);
        } else {
            pulseMeterStatusProperty.setAlertPauseFlgBool(true);
            // アラート停止期間
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseStart())) {
                pulseMeterStatusProperty.setAlertPauseStart(meterInfo.getAlertPauseStart());
                try {
                    pulseMeterStatusProperty.setCalAlertPauseStart(sdf.parse(meterInfo.getAlertPauseStart()));
                } catch (ParseException e) {
                    pulseMeterStatusProperty.setCalAlertPauseStart(null);
                }
            }
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseEnd())) {
                pulseMeterStatusProperty.setAlertPauseEnd(meterInfo.getAlertPauseEnd());
                try {
                    pulseMeterStatusProperty.setCalAlertPauseEnd(sdf.parse(meterInfo.getAlertPauseEnd()));
                } catch (ParseException e) {
                    pulseMeterStatusProperty.setCalAlertPauseStart(null);
                }
            }
        }
        pulseMeterStatusProperty.setMeterStatusMemo(meterInfo.getMeterStatusMemo());
        pulseMeterStatusProperty.setMeterId(meterInfo.getMeterId());
    }

    /**
     * IoT-R連携用メーター状態登録用property詰め
     * @param meterInfo
     */
    private void copyUpdatePropertyIotRMeterStatus(MeterInfo meterInfo) {
        // メーター状態更新用プロパティ
        iotRMeterStatusProperty = new MeterStatusProperty();
        // ◆登録内容更新画面用プロパティを詰める
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        if (!Objects.isNull(meterInfo.getMeterPresentSituation())) {
            iotRMeterStatusProperty.setMeterPresentSituationDisp(meterInfo.getMeterPresentSituation().toString());
        } else {
            iotRMeterStatusProperty.setMeterPresentSituationDisp("0");
        }
        BigDecimal apf = new BigDecimal("0");
        // アラート停止フラグの型変換
        if (Objects.isNull(meterInfo.getAlertPauseFlg()) || meterInfo.getAlertPauseFlg().equals(apf)) {
            iotRMeterStatusProperty.setAlertPauseFlgBool(false);
        } else {
            iotRMeterStatusProperty.setAlertPauseFlgBool(true);
            // アラート停止期間
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseStart())) {
                iotRMeterStatusProperty.setAlertPauseStart(meterInfo.getAlertPauseStart());
                try {
                    iotRMeterStatusProperty.setCalAlertPauseStart(sdf.parse(meterInfo.getAlertPauseStart()));
                } catch (ParseException e) {
                    iotRMeterStatusProperty.setCalAlertPauseStart(null);
                }
            }
            if (!CheckUtility.isNullOrEmpty(meterInfo.getAlertPauseEnd())) {
                iotRMeterStatusProperty.setAlertPauseEnd(meterInfo.getAlertPauseEnd());
                try {
                    iotRMeterStatusProperty.setCalAlertPauseEnd(sdf.parse(meterInfo.getAlertPauseEnd()));
                } catch (ParseException e) {
                    iotRMeterStatusProperty.setCalAlertPauseStart(null);
                }
            }
        }
        iotRMeterStatusProperty.setMeterStatusMemo(meterInfo.getMeterStatusMemo());
        iotRMeterStatusProperty.setMeterId(meterInfo.getMeterId());
    }

    /**
     * OCR変更画面用property詰め
     * @param meterInfo
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void copyUpdatePropertyOcr(MeterInfo meterInfo) throws IllegalAccessException, InvocationTargetException {
        // ◆初期化
        // OCR更新用プロパティ
        ocrUpdateProperty = new MeterManagementInputProperty();

        // ◆登録内容更新画面用プロパティを詰める
        // 現在値取得画面のメーター管理番号セレクトボックスにセット
        nowValMeterMngIdMap.put(meterInfo.getMeterMngId(), meterInfo.getMeterMngId());
        // チェックONのレコードのデータを変更画面の各入力項目にセット
        ocrUpdateProperty = new MeterManagementInputProperty();
        // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
        BeanUtils.copyProperties(ocrUpdateProperty, meterInfo);
        // 回避するやつを解除
        ConvertUtils.deregister();
        // copyPropertiesでコピーできないものを個別でセット
        if (!CheckUtility.isNullOrEmpty(meterInfo.getExamEndYm())) {
            // yyyy.MMの形からyyyyとMMに分解
            String[] tmpYmArray = meterInfo.getExamEndYm().split(Pattern.quote("."));
            // 西暦表示用
            ocrUpdateProperty.setExamEndYear(tmpYmArray[0]);
            ocrUpdateProperty.setExamEndMonth(tmpYmArray[1]);

            // 和暦表示用 年に変換→それぞれの値をセット
            String[] ymArray = convertToJpn(tmpYmArray[0] + tmpYmArray[1]);
            ocrUpdateProperty.setExamEndYmGengo(
                    MeterManagementConstants.DISP_YEAR_GENGO_H.equals(ymArray[0])
                            ? MeterManagementConstants.DISP_YEAR_GENGO_CODE_H
                            : MeterManagementConstants.DISP_YEAR_GENGO_CODE_R);
            ocrUpdateProperty.setExamEndYearWareki(ymArray[1]);
        }
    }

    /**
     * チェックボックスの全選択/全未選択の切り替え処理
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Logged
    public void checkBoxOperationAll() throws IllegalAccessException, InvocationTargetException {
        boolean tmpCheck = meterList.get(0).getCheckBox();
        boolean setCheck = true;
        int cnt = 0;
        for (MeterInfo meterInfo : meterList) {
            if (meterInfo.getCheckBox() != tmpCheck) {
                // 前のレコードと一致しない場合はループを途中で抜ける
                break;
            }
            tmpCheck = meterInfo.getCheckBox();
            cnt++;
        }

        // すべてがtrue/falseだった場合
        if (meterList.size() == cnt) {
            // 今の状態を反転したものをセットする
            setCheck = !tmpCheck;
        }

        List<MeterInfo> tmpList = new ArrayList<>();
        for (MeterInfo meterInfo : meterList) {
            MeterInfo tmpMeterInfo = new MeterInfo();

            // BeanUtils.copyPropertiesする際にBigDecimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
            ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
            ConvertUtils.register(new DateConverter(null), Date.class);
            // 内容をコピー
            BeanUtils.copyProperties(tmpMeterInfo, meterInfo);
            // 回避するやつを解除
            ConvertUtils.deregister();
            // ユーザーコードがNULLの場合、0→NULLに変換し直す
            if (meterInfo.getTenantId() == null) {
                tmpMeterInfo.setTenantId(null);
            }

            // ◆◆◆ここがメイン処理◆◆◆ チェックを選択状態/未選択状態に書き換え
            tmpMeterInfo.setCheckBox(setCheck);
            tmpList.add(tmpMeterInfo);
        }

        // 書き換えたもので上書き
        meterList = tmpList;
        checkBoxOperation();
    }

    @Logged
    public void btnHopIdHistoryClick(MeterManagementInputProperty prop) {

        hopIdHistoryProperty.setHopIdList(new ArrayList<>());

        // 検索
        GetSmsWirelessIdHistoryResponse response = new GetSmsWirelessIdHistoryResponse();
        GetSmsWirelessIdHistoryParameter parameter = new GetSmsWirelessIdHistoryParameter();

        // APIBeanの指定
        parameter.setBean("GetSmsWirelessIdHistoryBean");
        parameter.setDevId(smartProperty.getDevId());
        parameter.setAmount(10);
        // DBから値取得
        response = callApiPost(parameter, GetSmsWirelessIdHistoryResponse.class);

        // 画面表示用にリストの詰め替え
        List<String> list = new ArrayList<>();
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {
            List<GetSmsWirelessIdHistoryResultData> dataList = response.getResult().getList();
            if (Objects.nonNull(dataList) && dataList.size() != 0) {
                for (GetSmsWirelessIdHistoryResultData data : dataList) {
                    list.add(data.getWirelessId());
                }
            }
        }

        hopIdHistoryProperty.setHopIdList(list);

        hopIdHistoryProperty.setHop1Id(prop.getHop1Id());
        hopIdHistoryProperty.setHop2Id(prop.getHop2Id());
        hopIdHistoryProperty.setHop3Id(prop.getHop3Id());
        hopIdHistoryProperty.setIsFocusHop1Id("0");
        hopIdHistoryProperty.setIsFocusHop2Id("0");
        hopIdHistoryProperty.setIsFocusHop3Id("0");
    }

    @Logged
    public void btnConfirmClick(HopIdHistoryProperty prop) {

        handyInsertProperty.setHop1Id(prop.getHop1Id());
        handyInsertProperty.setHop2Id(prop.getHop2Id());
        handyInsertProperty.setHop3Id(prop.getHop3Id());
        handyUpdateProperty.setHop1Id(prop.getHop1Id());
        handyUpdateProperty.setHop2Id(prop.getHop2Id());
        handyUpdateProperty.setHop3Id(prop.getHop3Id());
    }

    @Logged
    public void btnClearClick() {

        hopIdHistoryProperty.setHop1Id("");
        hopIdHistoryProperty.setHop2Id("");
        hopIdHistoryProperty.setHop3Id("");
    }

    @Logged
    public void linkHopIdClick(String value, HopIdHistoryProperty prop) {

        if ("1".equals(prop.getIsFocusHop1Id())) {
            hopIdHistoryProperty.setHop1Id(value);
        }
        if ("1".equals(prop.getIsFocusHop2Id())) {
            hopIdHistoryProperty.setHop2Id(value);
        }
        if ("1".equals(prop.getIsFocusHop3Id())) {
            hopIdHistoryProperty.setHop3Id(value);
        }
    }

    public boolean hasError() {
        List<FacesMessage> messageList = getMessageList();
        if (messageList == null || messageList.isEmpty()) {
            return false;
        }
        for (FacesMessage message : messageList) {
            if (message.getSeverity().equals(FacesMessage.SEVERITY_FATAL)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_ERROR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 帳票出力(CSV).
     */
    @Logged
    public String downloadCsvFile() {

        try {
            String outputDir = getCsvOutputDir();
            List<List<String>> csvList = new ArrayList<>();

            String downloadFileName = "";

            String kind = getMeterKind();
            if (METER_KIND.SMART.getVal().equals(kind)) {
                downloadFileName = String.format("メーター管理（スマートメーター）_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), devName,
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            } else if (METER_KIND.PULSE.getVal().equals(kind)) {
                downloadFileName = String.format("メーター管理（パルスメーター）_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), devName,
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            } else if (METER_KIND.IOTR.getVal().equals(kind)) {
                downloadFileName = String.format("メーター管理（IoT-R連携用メーター）_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), devName,
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            } else if (METER_KIND.HANDY.getVal().equals(kind)) {
                downloadFileName = String.format("メーター管理（ハンディ検針用メーター）_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), devName,
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            } else {
                downloadFileName = String.format("メーター管理（AieLink用メーター）_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), devName,
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            }

            List<MeterInfo> meterList = executeSearch(false, devId);

            //タイトル
            List<String> titleList = new ArrayList<>();
            titleList.add("管理番号");
            titleList.add("建物テナント番号");
            titleList.add("建物テナント名");
            titleList.add("計器ID");
            titleList.add("種別");
            if (METER_KIND.SMART.getVal().equals(kind)) {

                titleList.add("指針値");
                titleList.add("開閉状態");
                titleList.add("負荷電流");
                titleList.add("自動投入");
                titleList.add("乗率");
                titleList.add("コンセントレータ名");
                titleList.add("操作状態");
                titleList.add("コメント");
            } else if (METER_KIND.PULSE.getVal().equals(kind)) {

                titleList.add("パルス種別");
                titleList.add("パルス重み");
                titleList.add("乗率");
                titleList.add("コンセントレータ名");
                titleList.add("操作状態");
                titleList.add("コメント");
            } else if (METER_KIND.IOTR.getVal().equals(kind)) {
                titleList.add("コメント");

            } else if (METER_KIND.HANDY.getVal().equals(kind)) {
                titleList.add("無線種別");
                titleList.add("乗率");
                titleList.add("無線ID");
                titleList.add("リレー1無線ID");
                titleList.add("リレー2無線ID");
                titleList.add("リレー3無線ID");
                titleList.add("ポーリンググループNo");
            } else if (METER_KIND.OCR.getVal().equals(kind)) {
                titleList.add("乗率");
            }
            titleList.add("検満");
            csvList.add(titleList);

            for (MeterInfo data : meterList) {
                List<String> dataList = new ArrayList<>();
                dataList.add(Objects.isNull(data.getMeterMngId()) ? null : data.getMeterMngId().toString());
                dataList.add(Objects.isNull(data.getTenantId()) ? "" : data.getTenantId().toString());
                dataList.add(data.getBuildingName());
                dataList.add(data.getMeterId());
                dataList.add(data.getMeterTypeName());
                if (METER_KIND.SMART.getVal().equals(kind)) {
                    dataList.add(Objects.isNull(data.getDmvKwh()) ? null : data.getDmvKwh().toString());
                    dataList.add(data.getOpenModeDisp());
                    if ("A".equals(data.getLoadlimitMode())) {
                        //臨時設定（A）
                        dataList.add(data.getTempLoadCurrent());
                        dataList.add(data.getTempAutoInjection());
                    } else if ("0".equals(data.getLoadlimitMode()) || " ".equals(data.getLoadlimitMode())) {
                        //無効, 設定なし
                        dataList.add("-");
                        dataList.add("-");
                    } else {
                        //有効（1）, 臨時設定解除（R）
                        dataList.add(data.getLoadCurrent());
                        dataList.add(data.getAutoInjection());
                    }
                    dataList.add(Objects.isNull(data.getMulti()) ? null : data.getMulti().toString());
                    dataList.add(data.getConcentratorName());
                    dataList.add(data.getOperationStatus());
                    dataList.add(data.getMemo());

                } else if (METER_KIND.PULSE.getVal().equals(kind)) {
                    dataList.add(data.getPulseTypeName());
                    dataList.add(Objects.isNull(data.getPulseWeight()) ? null : data.getPulseWeight().toString());
                    dataList.add(Objects.isNull(data.getMulti()) ? null : data.getMulti().toString());
                    dataList.add(data.getConcentratorName());
                    dataList.add(data.getOperationStatus());
                    dataList.add(data.getMemo());

                } else if (METER_KIND.IOTR.getVal().equals(kind)) {
                    dataList.add(data.getMemo());

                } else if (METER_KIND.HANDY.getVal().equals(kind)) {
                    dataList.add(data.getHandyType());
                    dataList.add(Objects.isNull(data.getMulti()) ? null : data.getMulti().toString());
                    dataList.add(data.getWirelessId());
                    dataList.add(data.getHop1Id());
                    dataList.add(data.getHop2Id());
                    dataList.add(data.getHop3Id());
                    dataList.add(data.getPollingId());
                } else if (METER_KIND.OCR.getVal().equals(kind)) {

                    dataList.add(Objects.isNull(data.getMulti()) ? null : data.getMulti().toString());
                }

                if ("0".equals(data.getDispYearFlg())) {
                    dataList.add(data.getExamEndYm());
                } else if ("1".equals(data.getDispYearFlg())) {
                    dataList.add(data.getExamEndYmWareki());
                } else {
                    dataList.add("");
                }
                csvList.add(dataList);
            }

            saveFilename = downloadFileName;
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            new MeterReadingCsvConverter().csvPrint(outputDir, saveFilename, csvList);
        } catch (Exception e) {

        }

        return STR_EMPTY;
    }

    /**
     * LTE-M用 帳票出力(CSV).
     */
    @Logged
    public String downloadCsvFileLteM() {

        try {
            String outputDir = getCsvOutputDir();
            List<List<String>> csvList = new ArrayList<>();

            String downloadFileName = "";

            String kind = getMeterKind();
            if (METER_KIND.SMART.getVal().equals(kind)) {
                downloadFileName = String.format("開閉負荷制限状態_%s_%s_%s.csv",
                        this.buildingInfo.getBuildingName(), smartProperty.getDevName(),
                        DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
            }

            //タイトル
            List<String> titleList = new ArrayList<>();
            titleList.add("管理番号");
            titleList.add("建物テナント番号");
            titleList.add("建物テナント名");
            titleList.add("計器ID");
            titleList.add("開閉区分");
            titleList.add("負荷制限");
            titleList.add("負荷電流(A)");
            titleList.add("自動投入(秒)");
            titleList.add("開閉器動作カウントクリア(回)");
            titleList.add("開閉器カウントクリア(分)");
            csvList.add(titleList);

            for (GetLteMNowValResponse res : lteMNowValResponseList) {
                List<String> dataList = new ArrayList<>();
                dataList.add(Objects.isNull(res.getResult().getMeterMngId()) ? null : res.getResult().getMeterMngId().toString());
                dataList.add(Objects.isNull(res.getResult().getTenantId()) ? "" : res.getResult().getTenantId().toString());
                dataList.add(res.getResult().getBuildingName());
                dataList.add(res.getResult().getMeterId());

                // FAILEDのときだけ実施
                if (DISP_UPDATE_STATUS.FAILED.getValue().equals(getDispUpdateStatus())) {
                    // 現在値取得失敗時はCSVファイルの値を「取得失敗」へ置換
                    checkRowIfFailed(dataList, res);
                } else {
                    dataList.add(res.getResult().getOpenModeDisp());
                    dataList.add(res.getResult().getLoadlimitModeDisp());
                    dataList.add(res.getResult().getLoadCurrent());
                    dataList.add(res.getResult().getAutoInjection());
                    dataList.add(res.getResult().getBreakerActCount());
                    dataList.add(res.getResult().getCountClear());
                }
                csvList.add(dataList);
            }

            saveFilename = downloadFileName;
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            new MeterReadingCsvConverter().csvPrint(outputDir, saveFilename, csvList);
        } catch (Exception e) {

        }

        return STR_EMPTY;
    }

    /**
     * CSVファイル内の表示値を「取得失敗」へ置換
     *
     * @param dataList CSVファイル出力リスト
     * @param res 対象行
     */
    private void checkRowIfFailed(List<String> dataList, GetLteMNowValResponse res) {

        if (res == null || res.getResult() == null) {
            return;
        }
        GetLteMNowValResult result = res.getResult();
        // FAILEDのときだけ実施
        if (!DISP_UPDATE_STATUS.FAILED.getValue().equals(getDispUpdateStatus())) {
            return;
        }
        // 置換実施
        result.setOpenModeDisp(replaceToFailed(result.getOpenModeDisp()));
        result.setLoadCurrent(replaceToFailed(result.getLoadCurrent()));
        result.setAutoInjection(replaceToFailed(result.getAutoInjection()));
        result.setBreakerActCount(replaceToFailed(result.getBreakerActCount()));
        result.setCountClear(replaceToFailed(result.getCountClear()));
        result.setTempLoadCurrent(replaceToFailed(result.getTempLoadCurrent()));
        result.setTempAutoInjection(replaceToFailed(result.getTempAutoInjection()));
        result.setTempBreakerActCount(replaceToFailed(result.getTempBreakerActCount()));
        result.setTempCountClear(replaceToFailed(result.getTempCountClear()));
        result.setLoadlimitModeDisp(replaceToFailed(result.getLoadlimitModeDisp()));

        dataList.add(result.getOpenModeDisp());
        dataList.add(result.getLoadlimitModeDisp());
        dataList.add(result.getLoadCurrent());
        dataList.add(result.getAutoInjection());
        dataList.add(result.getBreakerActCount());
        dataList.add(result.getCountClear());
    }

    /**
     * 「取得失敗」へ置換
     *
     * @param value 対象文字列
     * @return 置換後文字列
     */
    private static String replaceToFailed(String value) {
        return (value != null && value.contains(DISP_UPDATE_STATUS.IN_PROGRESS.getValue()))
                ? DISP_UPDATE_STATUS.FAILED.getValue() : value;
    }

    /**
     * 現在値取得にてタイマーのタイムアップ時(xhtmlのタグ「a4j:jsFunction」(=Ajax)から呼び出される)
     */
    public void markTimeoutFailed() {
        if (lteMNowValResponseList == null || lteMNowValResponseList.isEmpty()) {
            return;
        }

        // JavaScriptから受け取ったダウンロードID（なければBeanのcurrentDownloadIdを使う）
        String downloadId = FacesContext.getCurrentInstance()
                         .getExternalContext().getRequestParameterMap().get("downloadId");
        if (downloadId == null) {
            downloadId = this.currentDownloadId;
        }

        // 条件1:取得中
        if (DISP_UPDATE_STATUS.IN_PROGRESS.getValue().equals(getDispUpdateStatus())) {
            // 条件2:今回のダウンロード対象だけを失敗化
            if (matchesCurrentDownload(downloadId)) {
                setDispUpdateStatus(DISP_UPDATE_STATUS.FAILED.getValue());
            }
        }
    }

    /**
     * 今回のダウンロード対象かを判定
     *
     * @param downloadId 今回ダウンロード対象ID(JavaScriptより受け取ったID)
     * @return true:対象 false:対象ではない
     */
    private boolean matchesCurrentDownload(String downloadId) {

        // 競合を避けるため、ダウンロードIDで一致判定
        if (this.currentDownloadId != null && downloadId != null) {
            return downloadId.equals(this.currentDownloadId);
        }
        return true;
    }

    private String confirmMsg;

    public String getConfirmMsg() {
        return confirmMsg;
    }

    public void setConfirmMsg() {
        // LTE-M かつ スマートメーターの場合
        if (lteMDeviceFlg && Objects.equals(meterKind, METER_KIND.SMART.getVal())) {
            // 改行がうまく機能しないため全角空白文字にて代替対応
            this.confirmMsg = "一括登録を行います。よろしいですか？　　　　　　　　　　　　　　　　　　　　※開閉区分、負荷制限の設定は設定反映まで数分かかる場合があります。　　　　　　　　　　　※設定結果は現在値取得からご確認ください。";
        } else {
            // 一括登録を行います。よろしいですか？
            this.confirmMsg = beanMessages.getMessage("smsCollectSettingMeterMeterManagementBean.warn.beforeBulkRegisterMessage");
        }
    }

    /**
     * 確認ポップアップ画面に表示する確認メッセージを返却する
     * @param flg 確認メッセージ種別(1:登録, 2:更新, 3:削除)
     * @return 確認ポップアップ画面に表示する確認メッセージ
     */
    public String getConfirmMsg(Long flg) {
        if (flg == 0) {
            setConfirmMsg();
            return confirmMsg;
        } else if (flg == 1) {
            // この内容で登録します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
        } else if (flg == 2) {
            // 変更を保存します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeUpdateMessage");
        } else if (flg == 3) {
            // 削除します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeDeleteMessage");
        } else {
            // APIより最新の情報を取得します。よろしいですか？
            return beanMessages.getMessage("osol.warn.beforeGetApiMessage");
        }
    }

    /**
     * "yyyy-mm" を "yyyymm" に変換する
     * @param ym 書式:"yyyy-mm"
     * @return 書式:"yyyymm"
     */
    public static String convYyyyMm(String ym) {
        if (ym == null || ym.isEmpty()) {
            return null;
        }
        if (ym.length() == 7 && "-".equals(ym.substring(4, 5))) {
            return ym.substring(0, 4) + ym.substring(5);
        } else if (ym.length() == 6 && "-".equals(ym.substring(4, 5))) {
            return ym.substring(0, 4) + "0" + ym.substring(5);
        } else {
            return ym;
        }
    }

    /**
     * 元号コードを元号名に変換
     * @param gengoCd 元号コード
     * @return 元号名
     */
    public String getGengoNm(String gengoCd) {
        if (gengoMap == null || gengoCd == null) {
            return null;
        }

        Optional<String> mapKey = gengoMap.keySet().stream().filter(key -> gengoCd.equals(gengoMap.get(key))).findFirst();
        if (!mapKey.isPresent()) {
            return null;
        }

        return mapKey.get();
    }

    /** 完了メッセージ */
    private String alertMsg;

    /**
     * 完了メッセージポップアップ画面に表示するメッセージを返却する
     * @return
     */
    public String getAlertMsg() {
        return alertMsg;
    }

    /**
     * @return devType
     */
    public String getDevType() {
        return devType;
    }

    /**
     * @param devType セットする devType
     */
    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @param devIdMap セットする devIdMap
     */
    public void setDevIdMap(Map<String, String> devIdMap) {
        this.devIdMap = devIdMap;
    }

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<Long, Integer> getPagerMap() {
        return pagerMap;
    }

    public void setPagerMap(Map<Long, Integer> pagerMap) {
        this.pagerMap = pagerMap;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public Map<Long, Long> getMeterMngIdMap() {
        return meterMngIdMap;
    }

    public void setMeterMngIdMap(Map<Long, Long> meterMngIdMap) {
        this.meterMngIdMap = meterMngIdMap;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return buildingNo
     */
    public String getBuildingNo() {
        return buildingNo;
    }

    /**
     * @param buildingNo セットする buildingNo
     */
    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public Map<String, String> getLoadlimitMap() {
        return loadlimitMap;
    }

    public void setLoadlimitMap(Map<String, String> loadlimitMap) {
        this.loadlimitMap = loadlimitMap;
    }

    public String getLoadlimit() {
        return loadlimit;
    }

    public void setLoadlimit(String loadlimit) {
        this.loadlimit = loadlimit;
    }

    public Map<String, String> getCurlimitMap() {
        return curlimitMap;
    }

    public void setCurlimitMap(Map<String, String> curlimitMap) {
        this.curlimitMap = curlimitMap;
    }

    public String getBasicCurlimit() {
        return basicCurlimit;
    }

    public void setBasicCurlimit(String basicCurlimit) {
        this.basicCurlimit = basicCurlimit;
    }

    public String getTmpCurlimit() {
        return tmpCurlimit;
    }

    public void setTmpCurlimit(String tmpCurlimit) {
        this.tmpCurlimit = tmpCurlimit;
    }

    public String getBasicAutorec() {
        return basicAutorec;
    }

    public void setBasicAutorec(String basicAutorec) {
        this.basicAutorec = basicAutorec;
    }

    public String getTmpAutorec() {
        return tmpAutorec;
    }

    public void setTmpAutorec(String tmpAutorec) {
        this.tmpAutorec = tmpAutorec;
    }

    public String getBasicOccount() {
        return basicOccount;
    }

    public void setBasicOccount(String basicOccount) {
        this.basicOccount = basicOccount;
    }

    public String getTmpOccount() {
        return tmpOccount;
    }

    public void setTmpOccount(String tmpOccount) {
        this.tmpOccount = tmpOccount;
    }

    public Map<String, String> getOccountMap() {
        return occountMap;
    }

    public void setOccountMap(Map<String, String> occountMap) {
        this.occountMap = occountMap;
    }

    public Map<String, String> getCntclrMap() {
        return cntclrMap;
    }

    public void setCntclrMap(Map<String, String> cntclrMap) {
        this.cntclrMap = cntclrMap;
    }

    public String getBasicCntclr() {
        return basicCntclr;
    }

    public void setBasicCntclr(String basicCntclr) {
        this.basicCntclr = basicCntclr;
    }

    public String getTmpCntclr() {
        return tmpCntclr;
    }

    public void setTmpCntclr(String tmpCntclr) {
        this.tmpCntclr = tmpCntclr;
    }

    public Map<String, String> getOcstateMap() {
        return ocstateMap;
    }

    public void setOcstateMap(Map<String, String> ocstateMap) {
        this.ocstateMap = ocstateMap;
    }

    public String getOcstate() {
        return ocstate;
    }

    public void setOcstate(String ocstate) {
        this.ocstate = ocstate;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public Map<String, Long> getMetertypeMap() {
        return metertypeMap;
    }

    public void setMetertypeMap(Map<String, Long> metertypeMap) {
        this.metertypeMap = metertypeMap;
    }

    public String getMetertype() {
        return metertype;
    }

    public void setMetertype(String metertype) {
        this.metertype = metertype;
    }

    public Map<String, String> getDispYearMap() {
        return dispYearMap;
    }

    public void setDispYearMap(Map<String, String> dispYearMap) {
        this.dispYearMap = dispYearMap;
    }

    public String getDispYear() {
        return dispYear;
    }

    public void setDispYear(String dispYear) {
        this.dispYear = dispYear;
    }

    public Map<String, String> getGengoMap() {
        return gengoMap;
    }

    public void setGengoMap(Map<String, String> gengoMap) {
        this.gengoMap = gengoMap;
    }

    public String getGengo() {
        return gengo;
    }

    public void setGengo(String gengo) {
        this.gengo = gengo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getExamNoticeMonth() {
        return examNoticeMonth;
    }

    public void setExamNoticeMonth(String examNoticeMonth) {
        this.examNoticeMonth = examNoticeMonth;
    }

    public String getExamnotice() {
        return examnotice;
    }

    public void setExamnotice(String examnotice) {
        this.examnotice = examnotice;
    }

    public Map<String, String> getExamnoticeMap() {
        return examnoticeMap;
    }

    public void setExamnoticeMap(Map<String, String> examnoticeMap) {
        this.examnoticeMap = examnoticeMap;
    }

    public Map<String, String> getMeterPresSituMap() {
        return meterPresSituMap;
    }

    public void setMeterPresSituMap(Map<String, String> meterPresSituMap) {
        this.meterPresSituMap = meterPresSituMap;
    }

    public String getAlertPauseStart() {
        return alertPauseStart;
    }

    public void setAlertPauseStart(String alertPauseStart) {
        this.alertPauseStart = alertPauseStart;
    }

    public String getAlertPauseEnd() {
        return alertPauseEnd;
    }

    public void setAlertPauseEnd(String alertPauseEnd) {
        this.alertPauseEnd = alertPauseEnd;
    }

    public String getMeterStatusMemo() {
        return meterStatusMemo;
    }

    public void setMeterStatusMemo(String meterStatusMemo) {
        this.meterStatusMemo = meterStatusMemo;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Part getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(Part uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public List<String> getBulkErrorList() {
        return bulkErrorList;
    }

    public void setBulkErrorList(List<String> bulkErrorList) {
        this.bulkErrorList = bulkErrorList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return meterList
     */
    public List<MeterInfo> getMeterList() {
        return meterList;
    }

    /**
     * @param meterList セットする meterList
     */
    public void setMeterList(List<MeterInfo> meterList) {
        this.meterList = meterList;
    }

    /**
     * @return dispCnt
     */
    public int getDispCnt() {
        return dispCnt;
    }

    /**
     * @param dispCnt セットする dispCnt
     */
    public void setDispCnt(int dispCnt) {
        this.dispCnt = dispCnt;
    }

    /**
     * @return smartProperty
     */
    public MeterManagementCommonProperty getSmartProperty() {
        return smartProperty;
    }

    /**
     * @param smartProperty セットする smartProperty
     */
    public void setSmartProperty(MeterManagementCommonProperty smartProperty) {
        this.smartProperty = smartProperty;
    }

    /**
     * @return smartInsertProperty
     */
    public MeterManagementInputProperty getSmartInsertProperty() {
        return smartInsertProperty;
    }

    /**
     * @param smartInsertProperty セットする smartInsertProperty
     */
    public void setSmartInsertProperty(MeterManagementInputProperty smartInsertProperty) {
        this.smartInsertProperty = smartInsertProperty;
    }

    /**
     * @return pulseProperty
     */
    public MeterManagementCommonProperty getPulseProperty() {
        return pulseProperty;
    }

    /**
     * @param pulseProperty セットする pulseProperty
     */
    public void setPulseProperty(MeterManagementCommonProperty pulseProperty) {
        this.pulseProperty = pulseProperty;
    }

    /**
     * @return iotRProperty
     */
    public MeterManagementCommonProperty getIotRProperty() {
        return iotRProperty;
    }

    /**
     * @param iotRProperty セットする iotRProperty
     */
    public void setiotRProperty(MeterManagementCommonProperty iotRProperty) {
        this.iotRProperty = iotRProperty;
    }

    /**
     * @return handyProperty
     */
    public MeterManagementCommonProperty getHandyProperty() {
        return handyProperty;
    }

    /**
     * @param handyProperty セットする handyProperty
     */
    public void setHandyProperty(MeterManagementCommonProperty handyProperty) {
        this.handyProperty = handyProperty;
    }

    /**
     * @return ocrProperty
     */
    public MeterManagementCommonProperty getOcrProperty() {
        return ocrProperty;
    }

    /**
     * @param ocrProperty セットする ocrProperty
     */
    public void setOcrProperty(MeterManagementCommonProperty ocrProperty) {
        this.ocrProperty = ocrProperty;
    }

    /**
     * @return ocrInsertProperty
     */
    public MeterManagementInputProperty getOcrInsertProperty() {
        return ocrInsertProperty;
    }

    /**
     * @param ocrInsertProperty セットする ocrInsertProperty
     */
    public void setOcrInsertProperty(MeterManagementInputProperty ocrInsertProperty) {
        this.ocrInsertProperty = ocrInsertProperty;
    }

    /**
     * @return ocrUpdateProperty
     */
    public MeterManagementInputProperty getOcrUpdateProperty() {
        return ocrUpdateProperty;
    }

    /**
     * @param ocrUpdateProperty セットする ocrUpdateProperty
     */
    public void setOcrUpdateProperty(MeterManagementInputProperty ocrUpdateProperty) {
        this.ocrUpdateProperty = ocrUpdateProperty;
    }

    /**
     * @return smartUpdateProperty
     */
    public MeterManagementInputProperty getSmartUpdateProperty() {
        return smartUpdateProperty;
    }

    /**
     * @param smartUpdateProperty セットする smartUpdateProperty
     */
    public void setSmartUpdateProperty(MeterManagementInputProperty smartUpdateProperty) {
        this.smartUpdateProperty = smartUpdateProperty;
    }

    /**
     * @return iotRCurrentValueMeterMngId
     */
    public Long getPulseCurrentValueMeterMngId() {
        return pulseCurrentValueMeterMngId;
    }

    /**
     * @return iotRCurrentValueMeterMngId
     */
    public Long getIotRCurrentValueMeterMngId() {
        return iotRCurrentValueMeterMngId;
    }

    /**
     * @return handyCurrentValueMeterMngId
     */
    public Long getHandyCurrentValueMeterMngId() {
        return handyCurrentValueMeterMngId;
    }

    /**
     * @param pulseCurrentValueMeterMngId セットする pulseCurrentValueMeterMngId
     */
    public void setPulseCurrentValueMeterMngId(Long pulseCurrentValueMeterMngId) {
        this.pulseCurrentValueMeterMngId = pulseCurrentValueMeterMngId;
    }

    /**
     * @param iotRCurrentValueMeterMngId セットする iotRCurrentValueMeterMngId
     */
    public void setIotRCurrentValueMeterMngId(Long iotRCurrentValueMeterMngId) {
        this.iotRCurrentValueMeterMngId = iotRCurrentValueMeterMngId;
    }

    /**
     * @param handyCurrentValueMeterMngId セットする handyCurrentValueMeterMngId
     */
    public void setHandyCurrentValueMeterMngId(Long handyCurrentValueMeterMngId) {
        this.handyCurrentValueMeterMngId = handyCurrentValueMeterMngId;
    }

    /**
     * @return smartSettingUpdateProperty
     */
    public MeterManagementInputProperty getSmartSettingUpdateProperty() {
        return smartSettingUpdateProperty;
    }

    /**
     * @param smartSettingUpdateProperty セットする smartSettingUpdateProperty
     */
    public void setSmartSettingUpdateProperty(MeterManagementInputProperty smartSettingUpdateProperty) {
        this.smartSettingUpdateProperty = smartSettingUpdateProperty;
    }

    /**
     * @return nowValProperty
     */
    public NowValueProperty getNowValProperty() {
        return nowValProperty;
    }

    /**
     * @param nowValProperty セットする nowValProperty
     */
    public void setNowValProperty(NowValueProperty nowValProperty) {
        this.nowValProperty = nowValProperty;
    }

    /**
     * @return lteMNowValResponseList
     */
    public List<GetLteMNowValResponse> getLteMNowValResponseList() {
        return lteMNowValResponseList;
    }

    /**
     * @param lteMNowValResponseList セットする lteMNowValResponseList
     */
    public void setLteMNowValResponseList(List<GetLteMNowValResponse> lteMNowValResponseList) {
        this.lteMNowValResponseList = lteMNowValResponseList;
    }

    /**
     * @return currentDownloadId
     */
    public String getCurrentDownloadId() {
        return currentDownloadId;
    }

    /**
     * @param currentDownloadId セットする currentDownloadId
     */
    public void setCurrentDownloadId(String currentDownloadId) {
        this.currentDownloadId = currentDownloadId;
    }

    /**
     * @return dispUpdateStatus
     */
    public String getDispUpdateStatus() {
        return dispUpdateStatus;
    }

    /**
     * @param dispUpdateStatus セットする dispUpdateStatus
     */
    public void setDispUpdateStatus(String dispUpdateStatus) {
        this.dispUpdateStatus = dispUpdateStatus;
    }

    /**
     * @return currentValueDispDate
     */
    public String getCurrentValueDispDate() {
        return currentValueDispDate;
    }

    /**
     * @param currentValueDispDate セットする currentValueDispDate
     */
    public void setCurrentValueDispDate(String currentValueDispDate) {
        this.currentValueDispDate = currentValueDispDate;
    }

    /**
     * @return nowValMeterMngIdMap
     */
    public Map<Long, Long> getNowValMeterMngIdMap() {
        return nowValMeterMngIdMap;
    }

    /**
     * @param nowValMeterMngIdMap セットする nowValMeterMngIdMap
     */
    public void setNowValMeterMngIdMap(Map<Long, Long> nowValMeterMngIdMap) {
        this.nowValMeterMngIdMap = nowValMeterMngIdMap;
    }

    /**
     * @return meterKind
     */
    public String getMeterKind() {
        return meterKind;
    }

    /**
     * @param meterKind セットする meterKind
     */
    public void setMeterKind(String meterKind) {
        this.meterKind = meterKind;
    }

    /**
     * @return pulseTypeMap
     */
    public Map<String, String> getPulseTypeMap() {
        return pulseTypeMap;
    }

    /**
     * @param pulseTypeMap セットする pulseTypeMap
     */
    public void setPulseTypeMap(Map<String, String> pulseTypeMap) {
        this.pulseTypeMap = pulseTypeMap;
    }

    /**
     * @return pulseInsertProperty
     */
    public MeterManagementInputProperty getPulseInsertProperty() {
        return pulseInsertProperty;
    }

    /**
     * @param pulseInsertProperty セットする pulseInsertProperty
     */
    public void setPulseInsertProperty(MeterManagementInputProperty pulseInsertProperty) {
        this.pulseInsertProperty = pulseInsertProperty;
    }

    /**
     * @return pulseUpdateProperty
     */
    public MeterManagementInputProperty getPulseUpdateProperty() {
        return pulseUpdateProperty;
    }

    /**
     * @param pulseUpdateProperty セットする pulseUpdateProperty
     */
    public void setPulseUpdateProperty(MeterManagementInputProperty pulseUpdateProperty) {
        this.pulseUpdateProperty = pulseUpdateProperty;
    }

    /**
     * @return pulseSettingUpdateProperty
     */
    public MeterManagementInputProperty getPulseSettingUpdateProperty() {
        return pulseSettingUpdateProperty;
    }

    /**
     * @param pulseSettingUpdateProperty セットする pulseSettingUpdateProperty
     */
    public void setPulseSettingUpdateProperty(MeterManagementInputProperty pulseSettingUpdateProperty) {
        this.pulseSettingUpdateProperty = pulseSettingUpdateProperty;
    }

    /**
     * @return iotRTypeMap
     */
    public Map<String, String> getIotRTypeMap() {
        return iotRTypeMap;
    }

    /**
     * @param iotRTypeMap セットする iotRTypeMap
     */
    public void setIotRTypeMap(Map<String, String> iotRTypeMap) {
        this.iotRTypeMap = iotRTypeMap;
    }

    /**
     * @return iotRInsertProperty
     */
    public MeterManagementInputProperty getIotRInsertProperty() {
        return iotRInsertProperty;
    }

    /**
     * @param iotRInsertProperty セットする iotRInsertProperty
     */
    public void setIotRInsertProperty(MeterManagementInputProperty iotRInsertProperty) {
        this.iotRInsertProperty = iotRInsertProperty;
    }

    /**
     * @return iotRUpdateProperty
     */
    public MeterManagementInputProperty getIotRUpdateProperty() {
        return iotRUpdateProperty;
    }

    /**
     * @param iotRUpdateProperty セットする iotRUpdateProperty
     */
    public void setIotRUpdateProperty(MeterManagementInputProperty iotRUpdateProperty) {
        this.iotRUpdateProperty = iotRUpdateProperty;
    }

    /**
     * @return iotRSettingUpdateProperty
     */
    public MeterManagementInputProperty getIotRSettingUpdateProperty() {
        return iotRSettingUpdateProperty;
    }

    /**
     * @param iotRSettingUpdateProperty セットする iotRSettingUpdateProperty
     */
    public void setIotRSettingUpdateProperty(MeterManagementInputProperty iotRSettingUpdateProperty) {
        this.iotRSettingUpdateProperty = iotRSettingUpdateProperty;
    }

    /**
     * @return smartMeterStatusProperty
     */
    public MeterStatusProperty getSmartMeterStatusProperty() {
        return smartMeterStatusProperty;
    }

    /**
     * @param smartMeterStatusProperty セットする smartMeterStatusProperty
     */
    public void setSmartMeterStatusProperty(MeterStatusProperty smartMeterStatusProperty) {
        this.smartMeterStatusProperty = smartMeterStatusProperty;
    }

    /**
     * @return pulseMeterStatusProperty
     */
    public MeterStatusProperty getPulseMeterStatusProperty() {
        return pulseMeterStatusProperty;
    }

    /**
     * @param pulseMeterStatusProperty セットする pulseMeterStatusProperty
     */
    public void setPulseMeterStatusProperty(MeterStatusProperty pulseMeterStatusProperty) {
        this.pulseMeterStatusProperty = pulseMeterStatusProperty;
    }

    /**
     * @return iotRMeterStatusProperty
     */
    public MeterStatusProperty getIotRMeterStatusProperty() {
        return iotRMeterStatusProperty;
    }

    /**
     * @param iotRMeterStatusProperty セットする iotRMeterStatusProperty
     */
    public void setIotRMeterStatusProperty(MeterStatusProperty iotRMeterStatusProperty) {
        this.iotRMeterStatusProperty = iotRMeterStatusProperty;
    }

    /**
     * @return nowValMeterMngIdSmart
     */
    public Long getNowValMeterMngIdSmart() {
        return nowValMeterMngIdSmart;
    }

    /**
     * @param nowValMeterMngIdSmart セットする nowValMeterMngIdSmart
     */
    public void setNowValMeterMngIdSmart(Long nowValMeterMngIdSmart) {
        this.nowValMeterMngIdSmart = nowValMeterMngIdSmart;
    }

    /**
     * @return nowValMeterMngIdPulse
     */
    public Long getNowValMeterMngIdPulse() {
        return nowValMeterMngIdPulse;
    }

    /**
     * @param nowValMeterMngIdPulse セットする nowValMeterMngIdPulse
     */
    public void setNowValMeterMngIdPulse(Long nowValMeterMngIdPulse) {
        this.nowValMeterMngIdPulse = nowValMeterMngIdPulse;
    }

    /**
     * @return nowValMeterMngIdIotR
     */
    public Long getNowValMeterMngIdIotR() {
        return nowValMeterMngIdIotR;
    }

    /**
     * @param nowValMeterMngIdIotR セットする nowValMeterMngIdIotR
     */
    public void setNowValMeterMngIdIotR(Long nowValMeterMngIdIotR) {
        this.nowValMeterMngIdIotR = nowValMeterMngIdIotR;
    }

    public MeterManagementInputProperty getHandyInsertProperty() {
        return handyInsertProperty;
    }

    public void setHandyInsertProperty(MeterManagementInputProperty handyInsertProperty) {
        this.handyInsertProperty = handyInsertProperty;
    }

    public MeterManagementInputProperty getHandyUpdateProperty() {
        return handyUpdateProperty;
    }

    public void setHandyUpdateProperty(MeterManagementInputProperty handyUpdateProperty) {
        this.handyUpdateProperty = handyUpdateProperty;
    }

    public Map<String, String> getHandyTypeMap() {
        return handyTypeMap;
    }

    public void setHandyTypeMap(Map<String, String> handyTypeMap) {
        this.handyTypeMap = handyTypeMap;
    }

    public HopIdHistoryProperty getHopIdHistoryProperty() {
        return hopIdHistoryProperty;
    }

    public void setHopIdHistoryProperty(HopIdHistoryProperty hopIdHistoryProperty) {
        this.hopIdHistoryProperty = hopIdHistoryProperty;
    }

    public ListInfo getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo(ListInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public boolean isSmartPulseTabViewFlg() {
        return smartPulseTabViewFlg;
    }

    public void setSmartPulseTabViewFlg(boolean smartPulseTabViewFlg) {
        this.smartPulseTabViewFlg = smartPulseTabViewFlg;
    }

    public boolean isIotRTabViewFlg() {
        return iotRTabViewFlg;
    }

    public void setIotRTabViewFlg(boolean iotRTabViewFlg) {
        this.iotRTabViewFlg = iotRTabViewFlg;
    }

    public boolean isHandyTablViewFlg() {
        return handyTablViewFlg;
    }

    public void setHandyTablViewFlg(boolean handyTablViewFlg) {
        this.handyTablViewFlg = handyTablViewFlg;
    }

    public boolean isOcrTablViewFlg() {
        return ocrTablViewFlg;
    }

    public void setOcrTablViewFlg(boolean ocrTablViewFlg) {
        this.ocrTablViewFlg = ocrTablViewFlg;
    }

    /**
     * 担当者の権限有無で登録系機能を制限
     * @return true:許可、false:許可しない
     */
    public boolean isSmsAuthControl() {
        return super.isSmsAuthControl();
    }

    /**
     * 大崎権限の権限有無で登録系機能を制限
     * @return true:許可、false:許可しない
     */
    public boolean isOsakiAuth() {
        if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(this.getLoginCorpType())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isListMax() {
        return isListMax;
    }

    public void setListMax(boolean isListMax) {
        this.isListMax = isListMax;
    }

    public void initInvalidComponent() {

        if (this.invalidComponent == null) {
            this.invalidComponent = new ArrayList<>();
        } else {
            this.invalidComponent.clear();
        }
    }

    /**
     * @return sendLumpFlg
     */
    public boolean isSendLumpFlg() {
        return sendLumpFlg;
    }

    /**
     * @param sendLumpFlg セットする sendLumpFlg
     */
    public void setSendLumpFlg(boolean sendLumpFlg) {
        this.sendLumpFlg = sendLumpFlg;
    }

    /**
     * @return sendDelFlg
     */
    public boolean isSendDelFlg() {
        return sendDelFlg;
    }

    /**
     * @param sendDelFlg セットする sendDelFlg
     */
    public void setSendDelFlg(boolean sendDelFlg) {
        this.sendDelFlg = sendDelFlg;
    }

}
