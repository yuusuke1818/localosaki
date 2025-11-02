package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.bean.common.HttpRequester;
import jp.co.osaki.sms.bean.sms.collect.setting.concentrator.ConcentratorManagementBean;

/**
 * AieLinkサーバー通信API.
 * 「OCR検針」→「AieLink」へ変更
 * 参考クラス: AzbilServerSendRequestLogic.
 * @author kobayashi.sho
 */
public class OcrReadingApi {

    /**
     * AieLinkサーバー用 URL パラメータ KEY名.
     * 「OCR検針」→「AieLink」へ変更
     */
    private static final String KEY_REQPARAM_GRANT_TYPE = "grant_type";
    private static final String KEY_REQPARAM_USERNAME = "username";
    private static final String KEY_REQPARAM_PASSWORD = "password";

    /** Jsonデータ内タイムスタンプ文字列フォーマット. */
    private static final String DATEFORMATSTRING = "yyyy-MM-dd'T'HH:mm:ssZ";

    /** 結果変換GSON. */
    private final Gson gson = new GsonBuilder().setDateFormat(DATEFORMATSTRING).create();

    /** リトライ回数. */
    private static final int RETRY_CNT = 3;

    /** リトライインターバル(ms). */
    private static final int RETRY_INTERVAL = 10000;

    /** 通信結果コード ※0:正常. */
    public static final String RESPONS_CODE_SUCCESS = "0";

    /** データ送付要ContentType. */
    private static final String SENDDATA_CONTENT_TYPE = "application/json; charset=UTF-8";

    /** コンフィグ設定値取得クラス. */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    Logger eventLogger;
    Logger errorLogger;

    public OcrReadingApi(Logger eventLogger, Logger errorLogger) {
        this.eventLogger = eventLogger;
        this.errorLogger = errorLogger;
    }

    /**
     * AieLinkサーバーからアクセストークンを取得する
     * 「OCR検針」→「AieLink」へ変更
     * @return トークン取得結果
     */
    public OcrServerGetTokenResDto getToken() {

        // (認証用)メールアドレス・パスワード
        String email = smsConfigs.getConfig(SmsConstants.OCR_SERVER_AUTH_EMAIL);
        String password = smsConfigs.getConfig(SmsConstants.OCR_SERVER_AUTH_PASSWORD);

        // パラメータチェック
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            // パラメータ不正
            errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat(" getToken() fail invalid parameter:email=[" + email + "]" + "password=[" + password + "]"));
            return null;
        }

        OcrServerGetTokenResDto resDto = new OcrServerGetTokenResDto();

        // URLセット
        String requestURL = smsConfigs.getConfig(SmsConstants.OCR_SERVER_URL_TOKEN);
        eventLogger.info(ConcentratorManagementBean.class.getPackage().getName().concat(" requestURL:["+ requestURL + "]"));

        HttpRequester http = new HttpRequester(ConcentratorManagementBean.class.getPackage().getName());
        http.setServerAddress(requestURL);

        String param =
                  "{"
                + " \"email\": \"" + email + "\","
                + " \"password\": \"" + password + "\""
                + "}";

        // リトライ含め最大3回試行する
        // ただし、リトライの条件はコード500の場合に限る
        for (int i=0; i < RETRY_CNT; i++) {
            if (!http.requestPost(param, SENDDATA_CONTENT_TYPE, null)) {
                // 接続不可時
                errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getToken() fail requestGet"));
                resDto.setErrorMessage("<auth server> error connecting to URI");
                break;
            } else {
                int responseCode = http.getResponseCode();
                if (responseCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                    // 500エラーは一定時間スリープ後にリトライ
                    if (i < RETRY_CNT - 1) {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getToken() 500error:retrycnt=[" + i + "]"));
                        try {
                            Thread.sleep(RETRY_INTERVAL);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getToken() 500error:retrycnt=MAX"));
                    }
                } else if (responseCode != HttpServletResponse.SC_OK) {
                    // パラメータ不正によるエラー
                    errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat(String.format("getToken() responseCode=%d", responseCode)));
                    resDto.setHttpResponseCode(responseCode);
                    resDto.setErrorMessage("<auth server> requestParameter error by token");
                    break;
                } else {
                    // 正常応答
                    String body = http.getResponseBody();
                    try {
                        // サーバーからのエラーメッセージを設定したいため一旦Json変換する
                        resDto = gson.fromJson(body, OcrServerGetTokenResDto.class);
                        eventLogger.info(ConcentratorManagementBean.class.getPackage().getName().concat(
                            String.format(" getToken() Get tokens successfully. / send data :  : url=%s , %s=%s , %s=%s / response : code=%s , token=%s , msg=%s",
                                requestURL,
                                KEY_REQPARAM_GRANT_TYPE,
                                KEY_REQPARAM_PASSWORD,
                                KEY_REQPARAM_USERNAME,
                                email,
                                resDto.getCode(),
                                resDto.getData() == null ? "" : resDto.getData().getAccessToken(),
                                resDto.getMsg()
                            )));
                        resDto.setHttpResponseCode(responseCode);
                    } catch (Exception e) {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getToken() Json syntax error"));
                        resDto.setHttpResponseCode(responseCode);
                        resDto.setErrorMessage("<auth server> Json syntax error by token");
                    }
                    break;
                }
            }
        }

        return resDto;
    }

    /**
     * OCRカメラリストの取得する<br>
     * ※HWから呼び出す用
     *
     * @param facility_id 施設・エリアID  ※SMSの 装置ID から先頭の"OC"を除外したコード
     * @param meter_statusoptional OCRメータ状態  ※2:未初期化  3:初期化済  4:稼働中  5:インアクティブ  6:校正中
     * @param comm_standardoptional 通信規格  ※1:NBIoT  2:Cat.M1  3:ZETA  4:WiSUN  5:LoRaWAN
     * @param token アクセストークン
     * @return OCRカメラリスト
     */
    public OcrServerGetMeterListResDto getMeterList(String facility_id, String meter_statusoptional, String comm_standardoptional, String token) {

        // パラメータチェック
        if (token == null) {
            // パラメータ不正
            errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getMeterList() fail invalid parameter:"
                    + "facility_id=[" + facility_id + "]" + "meter_statusoptional=[" + meter_statusoptional + "]"
                    + "comm_standardoptional=[" + comm_standardoptional + "]" + "token=[" + token + "]"));
            return null;
        }

        OcrServerGetMeterListResDto resDto = new OcrServerGetMeterListResDto();

        // URLセット
        // user_id=%s&tenant_id=%s&facility_id=%s&meter_status=%s&comm_standard=%s";
        String param = "";
        if (facility_id != null) {
            param += (param.isEmpty() ? "?" : "&") + "facility_id=" + facility_id;
        }
        if (meter_statusoptional != null) {
            param += (param.isEmpty() ? "?" : "&") + "meter_statusoptional=" + meter_statusoptional;
        }
        if (comm_standardoptional != null) {
            param += (param.isEmpty() ? "?" : "&") + "comm_standardoptional=" + comm_standardoptional;
        }

        String requestURL = smsConfigs.getConfig(SmsConstants.OCR_SERVER_URL_METER_LIST) + param;
        eventLogger.info(ConcentratorManagementBean.class.getPackage().getName().concat(" requestURL:["+ requestURL + "]"));

        HttpRequester http = new HttpRequester(ConcentratorManagementBean.class.getPackage().getName());
        http.setServerAddress(requestURL);

        // リトライ含め最大3回試行する
        // ただし、リトライの条件はコード500の場合に限る
        for (int i=0; i < RETRY_CNT; i++) {
            if (!http.requestGet(token)) {
                // 接続不可時
                errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getMeterList() fail requestGet"));
                resDto.setErrorMessage("<auth server> error connecting to URI");
                break;
            } else {
                int responseCode = http.getResponseCode();
                if (responseCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                    // 500エラーは一定時間スリープ後にリトライ
                    if (i < RETRY_CNT - 1) {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getMeterList() 500error:retrycnt=[" + i + "]"));
                        try {
                            Thread.sleep(RETRY_INTERVAL);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getMeterList() 500error:retrycnt=MAX"));
                    }
                } else if (responseCode != HttpServletResponse.SC_OK) {
                    // パラメータ不正によるエラー
                    errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat(String.format("getMeterList() responseCode=%d", responseCode)));
                    resDto.setHttpResponseCode(responseCode);
                    resDto.setErrorMessage("<auth server> requestParameter error by token");
                    break;
                } else {
                    // 正常応答
                    String body = http.getResponseBody();
                    try {
                        // サーバーからのエラーメッセージを設定したいため一旦Json変換する
                        resDto = gson.fromJson(body, OcrServerGetMeterListResDto.class);
                        eventLogger.info(ConcentratorManagementBean.class.getPackage().getName().concat(
                            String.format(" getMeterList() Send to OCRServer successfully. / send data : url=%s , token=%s / response : code=%s , msg=%s",
                                requestURL,
                                token,
                                resDto.getCode(),
                                resDto.getMsg()
                            )));
                        resDto.setHttpResponseCode(responseCode);
                    } catch (Exception e) {
                        errorLogger.error(ConcentratorManagementBean.class.getPackage().getName().concat("getMeterList() Json syntax error"));
                        resDto.setHttpResponseCode(responseCode);
                        resDto.setErrorMessage("<auth server> Json syntax error by token");
                    }
                    break;
                }
            }
        }

        return resDto;
    }
}
