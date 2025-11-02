package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.bean.common.HttpRequester;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterManagementBean.LOAD_LIMIT_MODE;
import jp.co.osaki.sms.dao.TApiKeyDao;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;

public class LteMApiService {

    /**
     * イベントログ
     */
    protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    /**
     * APIキーDao.
     */
    @EJB
    private TApiKeyDao tApiKeyDao;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    /**
     * 開閉制御API(sync)実行
     *
     * @param http HTTPリクエストクラス
     * @param meterInfo メーター取得結果
     * @return 実行結果
     */
    public boolean execSwitchApiSync(HttpRequester http, MeterInfo meterInfo) {
        // 負荷制限API(sync)にて使用するJSONのMAPを取得
        Map<String, Object> switchApiSyncJsonMap = switchApiSyncToJson(meterInfo.getMeterId());
        // JSON文字列へ変換
        Gson gson = new Gson();
        String switchApiSyncJson = gson.toJson(switchApiSyncJsonMap);

        // ログ用JSON文字列取得（api_keyはマスキング）
        String switchApiSyncLogJson = maskApiKey(switchApiSyncJsonMap);

        // API実行
        if (!http.requestPost(meterInfo.getMeterMngId(),
                MeterManagementConstants.SWITCH_API_NAME,
                smsConfigs.getConfig(MeterManagementConstants.LTEM_SWITCH_API_EXEC_URL),
                switchApiSyncJson,
                "application/json",
                "Basic",
                smsConfigs.getConfig(MeterManagementConstants.LTEM_AUTHORIZATION),
                switchApiSyncLogJson)) {
            // API実行失敗の場合
            logAndNotifyError("【メーター管理番号:%d】 [" + MeterManagementConstants.SWITCH_API_NAME + "] 失敗", meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat("500エラー"), meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() != HttpServletResponse.SC_OK) {
            // パラメータ不正によるエラー
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat(String.format("responseCode=%d", http.getResponseCode())), meterInfo.getMeterMngId());
            return false;
        }
        return true;
    }

    /**
     * 負荷制限API(sync)実行
     *
     * @param http HTTPリクエストクラス
     * @param meterInfo メーター取得結果
     * @return 実行結果
     */
    public boolean execLoadlimitApiSync(HttpRequester http, MeterInfo meterInfo) {
        // 負荷制限API(sync)にて使用するJSONのMAPを取得
        Map<String, Object> loadlimitApiSyncJsonMap = loadlimitApiSyncToJson(meterInfo.getMeterId(), meterInfo);
        // JSON文字列へ変換
        Gson gson = new Gson();
        String loadlimitApiSyncJson = gson.toJson(loadlimitApiSyncJsonMap);

        // ログ用JSON文字列取得（api_keyはマスキング）
        String loadlimitApiSyncLogJson = maskApiKey(loadlimitApiSyncJsonMap);

        // API実行
        if (!http.requestPost(meterInfo.getMeterMngId(),
                MeterManagementConstants.LOADLIMIT_API_NAME,
                smsConfigs.getConfig(MeterManagementConstants.LTEM_LOADLIMIT_API_EXEC_URL),
                loadlimitApiSyncJson,
                "application/json",
                "Basic",
                smsConfigs.getConfig(MeterManagementConstants.LTEM_AUTHORIZATION),
                loadlimitApiSyncLogJson)) {
            // API実行失敗の場合
            logAndNotifyError("【メーター管理番号:%d】 [" + MeterManagementConstants.LOADLIMIT_API_NAME + "] 失敗", meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat("500エラー"), meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() != HttpServletResponse.SC_OK) {
            // パラメータ不正によるエラー
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat(String.format("responseCode=%d", http.getResponseCode())), meterInfo.getMeterMngId());
            return false;
        }
        return true;
    }

    /**
     * 開閉制御API(set)実行
     *
     * @param http HTTPリクエストクラス
     * @param meterInfo メーター取得結果
     * @param input 画面からの入力情報
     * @return 実行結果
     */
    public boolean execSwitchApiSet(HttpRequester http, MeterInfo meterInfo, MeterManagementInputProperty input) {
        // 開閉制御API(set)にて使用するJSONのMAPを取得
        Map<String, Object> switchApiSetJsonMap = switchApiSetToJson(meterInfo.getMeterId(), input);
        // JSON文字列へ変換
        Gson gson = new Gson();
        String switchApiSetJson = gson.toJson(switchApiSetJsonMap);

        // ログ用JSON文字列取得（api_keyはマスキング）
        String switchApiSetLogJson = maskApiKey(switchApiSetJsonMap);

        // API実行
        if (!http.requestPost(meterInfo.getMeterMngId(),
                MeterManagementConstants.SWITCH_API_NAME,
                smsConfigs.getConfig(MeterManagementConstants.LTEM_SWITCH_API_EXEC_URL),
                switchApiSetJson,
                "application/json",
                "Basic",
                smsConfigs.getConfig(MeterManagementConstants.LTEM_AUTHORIZATION),
                switchApiSetLogJson)) {
            // API実行失敗の場合
            logAndNotifyError("【メーター管理番号:%d】 [" + MeterManagementConstants.SWITCH_API_NAME + "] 失敗", meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat("500エラー"), meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() != HttpServletResponse.SC_OK) {
            // パラメータ不正によるエラー
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat(String.format("responseCode=%d", http.getResponseCode())), meterInfo.getMeterMngId());
            return false;
        }
        return true;
    }

    /**
     * 負荷制限API(set)実行
     *
     * @param http HTTPリクエストクラス
     * @param meterInfo メーター取得結果
     * @param input 画面からの入力情報
     * @return 実行結果
     */
    public boolean execLoadlimitApiSet(HttpRequester http, MeterInfo meterInfo, MeterManagementInputProperty input) {
        // 負荷制限API(set)にて使用するJSONのMAPを取得
        Map<String, Object> loadlimitApiSetJsonMap = loadlimitApiSetToJson(meterInfo.getMeterId(), input);
        // JSON文字列へ変換
        Gson gson = new Gson();
        String loadlimitApiSetJson = gson.toJson(loadlimitApiSetJsonMap);

        // ログ用JSON文字列取得（api_keyはマスキング）
        String loadlimitApiSetLogJson = maskApiKey(loadlimitApiSetJsonMap);

        // API実行
        if (!http.requestPost(meterInfo.getMeterMngId(),
                MeterManagementConstants.LOADLIMIT_API_NAME,
                smsConfigs.getConfig(MeterManagementConstants.LTEM_LOADLIMIT_API_EXEC_URL),
                loadlimitApiSetJson,
                "application/json",
                "Basic",
                smsConfigs.getConfig(MeterManagementConstants.LTEM_AUTHORIZATION),
                loadlimitApiSetLogJson)) {
            // API実行失敗の場合
            logAndNotifyError("【メーター管理番号:%d】 [" + MeterManagementConstants.LOADLIMIT_API_NAME + "] 失敗", meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat("500エラー"), meterInfo.getMeterMngId());
            return false;

        } else if (http.getResponseCode() != HttpServletResponse.SC_OK) {
            // パラメータ不正によるエラー
            logAndNotifyError("【メーター管理番号:%d】 " + MeterManagementBean.class.getPackage().getName().concat(String.format("responseCode=%d", http.getResponseCode())), meterInfo.getMeterMngId());
            return false;
        }
        return true;
    }

    /**
     * 開閉制御API(sync)にて使用するJSONのMAPを取得
     *
     * @param meterId 計器ID
     * @return 負荷制限APIにて使用するJSONのMAP
     */
    private Map<String, Object> switchApiSyncToJson(String meterId) {

        // request情報
        Map<String, Object> requestSwitch = new HashMap<>();
        requestSwitch.put("type", MeterManagementConstants.SWITCH_API);
        requestSwitch.put("action", MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode());
        // TODO 中村 meterIdを変数に置き換える、動的に実行できるように。
        requestSwitch.put("request_id", //"A18G001005-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));
        meterId + "-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));

        // 全体のJSON構造
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("auth", getAuth(meterId));
        jsonMap.put("request", requestSwitch);

        return jsonMap;
    }

    /**
     * 負荷制限API(sync)にて使用するJSONのMAPを取得
     *
     * @param meterId 計器ID
     * @param meterInfo メーター取得結果
     * @return 負荷制限APIにて使用するJSONのMAP
     */
    private Map<String, Object> loadlimitApiSyncToJson(String meterId, MeterInfo meterInfo) {

        // request情報
        Map<String, Object> requestLoadlimit = new HashMap<>();
        requestLoadlimit.put("type", MeterManagementConstants.LOADLIMIT_API);
        requestLoadlimit.put("action", MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode());
        // TODO 中村 meterIdを変数に置き換える、動的に実行できるように。
        requestLoadlimit.put("request_id", //"A18G001005-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));
        meterId + "-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));

        // param情報
        Map<String, Object> paramLoadlimit = new HashMap<>();
        paramLoadlimit.put("loadlimit_target", "setting"); // 現在値取得の場合はsettingを設定

        // 全体のJSON構造
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("auth", getAuth(meterId));
        jsonMap.put("request", requestLoadlimit);
        jsonMap.put("param", paramLoadlimit);

        return jsonMap;
    }

    /**
     * 開閉制御API(set)にて使用するJSONのMAPを取得
     *
     * @param meterId 計器ID
     * @param input 画面からの入力情報
     * @return 開閉制御APIにて使用するJSONのMAP
     */
    private Map<String, Object> switchApiSetToJson(String meterId, MeterManagementInputProperty input) {

        // request情報
        Map<String, Object> requestSwitch = new HashMap<>();
        requestSwitch.put("type", MeterManagementConstants.SWITCH_API);
        requestSwitch.put("action", MeterManagementConstants.LTEM_API_TYPE.SET.getCode());
        // TODO 中村 meterIdを変数に置き換える、動的に実行できるように。
        requestSwitch.put("request_id", //"A18G001005-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));
        meterId + "-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));

        // param情報
        Map<String, Object> paramSwitch = new HashMap<>();
        paramSwitch.put("switch", input.getOpenMode());

        // 全体のJSON構造
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("auth", getAuth(meterId));
        jsonMap.put("request", requestSwitch);
        jsonMap.put("param", paramSwitch);

        return jsonMap;
    }

    /**
     * 負荷制限API(set)にて使用するJSONのMAPを取得
     *
     * @param meterId 計器ID
     * @param input 画面からの入力情報
     * @return 負荷制限APIにて使用するJSONのMAP
     */
    private Map<String, Object> loadlimitApiSetToJson(String meterId, MeterManagementInputProperty input) {

        // request情報
        Map<String, Object> requestLoadlimit = new HashMap<>();
        requestLoadlimit.put("type", MeterManagementConstants.LOADLIMIT_API);
        requestLoadlimit.put("action", MeterManagementConstants.LTEM_API_TYPE.SET.getCode());
        // TODO 中村 meterIdを変数に置き換える、動的に実行できるように。
        requestLoadlimit.put("request_id", //"A18G001005-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));
        meterId + "-" + new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).format(getCurrentDateTime()));

        // param情報
        Map<String, Object> paramLoadlimit = new HashMap<>();
        if (CheckUtility.isNullOrEmpty(input.getLoadlimitTarget())) {
            if (Objects.equals(LOAD_LIMIT_MODE.TEMP_VALID.getVal(), input.getLoadlimitMode())
                    || Objects.equals(LOAD_LIMIT_MODE.TEMP_INVALID.getVal(), input.getLoadlimitMode())) {
                // 臨時設定（A）, 臨時設定解除（R）
                paramLoadlimit.put("loadlimit_target", "temp");
            } else {
                // 有効（1）, 無効, 設定なし
                paramLoadlimit.put("loadlimit_target", "reglr");
            }
        } else {
            paramLoadlimit.put("loadlimit_target", input.getLoadlimitTarget());
        }
        paramLoadlimit.put("loadlimit_mode", input.getLoadlimitMode());

        // 負荷制限によって「基本」と「臨時」を判別  画面にて「未選択」の場合、固定値を設定 固定値＝(load_current=1, auto_injection=10, breaker_act_count=5, count_clear=30)
        if (Objects.equals(input.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.TEMP.getValue())) {
            // 臨時の場合
            paramLoadlimit.put("load_current", NumberUtils.toInt(input.getTempLoadCurrent(), MeterManagementConstants.FIXED_LOAD_CURRENT));
            paramLoadlimit.put("auto_injection", NumberUtils.toInt(input.getTempAutoInjection(), MeterManagementConstants.FIXED_AUTO_INJECTION));
            paramLoadlimit.put("breaker_act_count", NumberUtils.toInt(input.getTempBreakerActCount(), MeterManagementConstants.FIXED_BREAKER_ACT_COUNT));
            paramLoadlimit.put("count_clear", NumberUtils.toInt(input.getTempCountClear(), MeterManagementConstants.FIXED_COUNT_CLEAR));

        } else if (Objects.equals(input.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.BASIC.getValue())
                || Objects.equals(input.getLoadlimitMode(), MeterManagementConstants.LOADLIMIT_MODE.DISABLED.getValue())
                || CheckUtility.isNullOrEmpty(input.getLoadlimitMode())) {
            // 基本または無効または空欄の場合
            paramLoadlimit.put("load_current", NumberUtils.toInt(input.getLoadCurrent(), MeterManagementConstants.FIXED_LOAD_CURRENT));
            paramLoadlimit.put("auto_injection", NumberUtils.toInt(input.getAutoInjection(), MeterManagementConstants.FIXED_AUTO_INJECTION));
            paramLoadlimit.put("breaker_act_count", NumberUtils.toInt(input.getBreakerActCount(), MeterManagementConstants.FIXED_BREAKER_ACT_COUNT));
            paramLoadlimit.put("count_clear", NumberUtils.toInt(input.getCountClear(), MeterManagementConstants.FIXED_COUNT_CLEAR));

        } else {
            // それ以外の場合(臨時解除)、固定値を設定
            paramLoadlimit.put("load_current", MeterManagementConstants.FIXED_LOAD_CURRENT);
            paramLoadlimit.put("auto_injection", MeterManagementConstants.FIXED_AUTO_INJECTION);
            paramLoadlimit.put("breaker_act_count", MeterManagementConstants.FIXED_BREAKER_ACT_COUNT);
            paramLoadlimit.put("count_clear", MeterManagementConstants.FIXED_COUNT_CLEAR);
        }

        // 全体のJSON構造
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("auth", getAuth(meterId));
        jsonMap.put("request", requestLoadlimit);
        jsonMap.put("param", paramLoadlimit);

        return jsonMap;
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
     * 認証情報のMAPを取得
     *
     * @param meterId 計器ID
     * @return
     */
    private Map<String, Object> getAuth(String meterId) {

        // プロパティファイルから企業IDと担当者IDを取得
        String corpId = smsConfigs.getConfig(MeterManagementConstants.LTEM_CORP_ID);
        String personId = smsConfigs.getConfig(MeterManagementConstants.LTEM_PERSON_ID);
        // APIキーを取得
        TApiKey tApiKey = tApiKeyDao.find(corpId, personId);
        String apiKey = null;
        if (tApiKey != null) {
            apiKey = tApiKey.getApiKey();
        }

        Map<String, Object> auth = new HashMap<>();
        // TODO 中村 meterIdを変数に置き換える、動的に実行できるように。
        auth.put("api_key", apiKey);
        auth.put("corp_id", corpId);
        auth.put("user_id", personId);
        auth.put("meter_id", meterId);//"A18G001005");
        return auth;
    }

    /**
     * 現在日時を取得
     * @return 現在日時
     */
    private Timestamp getCurrentDateTime() {
        return new Timestamp((new Date()).getTime());
    }

    /**
     * マスク処理
     */
    private static String mask(String value) {
        if (value == null) return "";
        int len = value.length();
        if (len <= 8) return "****";  // 短いものは全部マスク
        return value.replaceAll("(?<=.{4}).(?=.{4})", "*");
    }

    /**
     * JSON文字列にて項目「api_key」をマスキング
     *
     * @param jsonMap JSONのMap
     */
    private static String maskApiKey(Map<String, Object> jsonMap) {
        @SuppressWarnings("unchecked")
        Map<String, Object> authMap = (Map<String, Object>) ((Map<String, Object>) jsonMap).get("auth");

        // 新しいMapをコピー（送信用jsonMapを壊さないため）
        Map<String, Object> logJsonMap = new HashMap<>(jsonMap);
        Map<String, Object> maskedAuth = new HashMap<>(authMap);

        if (maskedAuth.containsKey("api_key")) {
            String apiKey = String.valueOf(maskedAuth.get("api_key"));
            maskedAuth.put("api_key", mask(apiKey)); // マスク処理を適用
        }

        // authを差し替える
        logJsonMap.put("auth", maskedAuth);

        // ログ用JSON文字列
        Gson gson = new Gson();
        return gson.toJson(logJsonMap);
    }

    /**
     * 画面表示のためのエラーメッセージを追加
     *
     * @param message
     */
    protected void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    /**
     * 画面表示のための情報メッセージを追加
     *
     * @param message
     */
    protected void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

}
