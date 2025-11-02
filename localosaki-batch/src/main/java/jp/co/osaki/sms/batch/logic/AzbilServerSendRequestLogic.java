package jp.co.osaki.sms.batch.logic;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.dto.AzbilServerSendDayLoadServeyPostResDto;
import jp.co.osaki.sms.batch.dto.AzbilServerTokenEndpointPostResDto;
import jp.co.osaki.sms.batch.utility.common.HttpRequester;
import jp.skygroup.enl.webap.base.batch.BaseBatchLogic;

/**
 * アズビルサーバ リクエスト ロジッククラス
 *
 * @author akr_iwamoto
 *
 */
@Stateless
public final class AzbilServerSendRequestLogic extends BaseBatchLogic {


    /**
     * 結果変換GSON
     */
    private final Gson gson = new GsonBuilder().setDateFormat(SmsBatchConstants.DATEFORMATSTRING).create();

    /**
     * リトライ回数
     */
    private static final int RETRY_CNT = 3;
    /**
     * リトライインターバル(ms)
     */
    private static final int RETRY_INTERVAL = 10000;

    /**
     * データ送付要ContentType
     */
    private static final String SENDDATA_CONTENT_TYPE = "text/plain; charset=Shift_JIS";

    /**
     * 設定ファイル
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /**
     * コンストラクター
     */
    AzbilServerSendRequestLogic(){}

    /**
     * コンストラクター
     *
     * @param batchName
     */
    public AzbilServerSendRequestLogic(String batchName){
        this.batchName = batchName;
    }

    /**
     * アズビルサーバからアクセストークンを取得する
     *
     * @param InfoId
     * @param name
     * @param pass
     * @return トークン取得結果
     */
    public AzbilServerTokenEndpointPostResDto getTokenEndpoint(String InfoId, String name, String pass) {

        // パラメータチェック
        if (InfoId == null || name == null || pass == null) {
            // パラメータ不正
            errorLogger.error(this.batchName.concat("getTokenEndpoint() fail invalid parameter:buildingInfoId=[" + InfoId + "]" +
                    "user=[" + name + "]" + "pass=[" + pass + "]"));
            return null;
        }

        AzbilServerTokenEndpointPostResDto resDto = new AzbilServerTokenEndpointPostResDto();

        // URLセット
        String valAzbilServername = smsBatchConfigs.getConfig(SmsBatchConstants.VAL_AZBIL_SERVERNAME);
        String requestURL = valAzbilServername + InfoId +
                SmsBatchConstants.VAL_AZBIL_SERVERNAME_AUTH;
        batchLogger.info(this.batchName.concat(" requestURL:["+ requestURL + "]"));

        HttpRequester http = new HttpRequester(this.batchName);
        http.setServerAddress(requestURL);
        // サブアドレスセット
        //http.addURLDirectory(PaletteCloudServerConstants.SERVERNAME_AUTH_SUBDIR_TOKENS);

        Map<String,String> paramMap = new HashMap<String,String>();

        // postMapへ
        paramMap.put(SmsBatchConstants.KEY_REQPARAM_GRANT_TYPE, SmsBatchConstants.KEY_REQPARAM_PASSWORD);
        paramMap.put(SmsBatchConstants.KEY_REQPARAM_USERNAME, name);
        paramMap.put(SmsBatchConstants.KEY_REQPARAM_PASSWORD, pass);

        // リトライ含め最大3回試行する
        // ただし、リトライの条件はコード500の場合に限る
        for (int i=0; i < RETRY_CNT; i++) {
            if (!http.requestPost(paramMap)) {
                // 接続不可時
                errorLogger.error(this.batchName.concat("getTokenEndpoint() fail requestGet"));
                resDto.setErrorMessage("<auth server> error connecting to URI");
                break;
            } else {
                int responseCode = http.getResponseCode();
                if (responseCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                    // 500エラーは一定時間スリープ後にリトライ
                    if (i < RETRY_CNT - 1) {
                        errorLogger.error(this.batchName.concat("getTokenEndpoint() 500error:retrycnt=[" + i + "]"));
                        try {
                            Thread.sleep(RETRY_INTERVAL);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        errorLogger.error(this.batchName.concat("getTokenEndpoint() 500error:retrycnt=MAX"));
                    }
                } else if (responseCode != HttpServletResponse.SC_OK) {
                    // パラメータ不正によるエラー
                    errorLogger.error(this.batchName.concat(String.format("getTokenEndpoint() responseCode=%d", responseCode)));
                    resDto.setHttpResponseCode(responseCode);
                    resDto.setErrorMessage("<auth server> requestParameter error by token");
                    break;
                } else {
                    // 正常応答
                    String body = http.getResponseBody();
                    try {
                        // サーバーからのエラーメッセージを設定したいため一旦Json変換する
                        resDto = gson.fromJson(body, AzbilServerTokenEndpointPostResDto.class);
                        batchLogger.info(this.batchName.concat(String.format(" getTokenEndpoint() Get tokens successfully. / send data : url=%s , %s=%s , %s=%s / response : token=%s", requestURL, SmsBatchConstants.KEY_REQPARAM_GRANT_TYPE, SmsBatchConstants.KEY_REQPARAM_PASSWORD, SmsBatchConstants.KEY_REQPARAM_USERNAME, name, resDto.getAccess_token())));
                        resDto.setHttpResponseCode(responseCode);
                    } catch (Exception e) {
                        errorLogger.error(this.batchName.concat("getTokenEndpoint() Json syntax error"));
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
     * アズビルサーバへ計測値送信リクエストを送信する<br>
     * ※HWから呼び出す用
     *
     * @param buildngInfoId 外部建物ID
     * @param ymd 年月日
     * @param token アクセストークン
     * @param contents コンテンツ
     * @return
     */
    public AzbilServerSendDayLoadServeyPostResDto sendDayLoadServey(String buildngInfoId, String ymd, String token, String contents) {

        // パラメータチェック
        if (buildngInfoId == null || ymd == null || token == null || contents == null) {
            // パラメータ不正
            errorLogger.error(this.batchName.concat("sendDayLoadServey() fail invalid parameter:buildingInfoId=[" + buildngInfoId + "]" +
                    "ymd=[" + ymd + "]" + "token=[" + token + "]" + "contents=[" + contents + "]"));
            return null;
        }

        AzbilServerSendDayLoadServeyPostResDto resDto = new AzbilServerSendDayLoadServeyPostResDto();

        // URLセット
        String valAzbilServername = smsBatchConfigs.getConfig(SmsBatchConstants.VAL_AZBIL_SERVERNAME);
        String requestURL = valAzbilServername + buildngInfoId +
                SmsBatchConstants.VAL_AZBIL_SERVERNAME_MEASUREMENTVALUE + ymd;
        batchLogger.info(this.batchName.concat(" requestURL:["+ requestURL + "]"));

        HttpRequester http = new HttpRequester(this.batchName);
        http.setServerAddress(requestURL);

        // リトライ含め最大3回試行する
        // ただし、リトライの条件はコード500の場合に限る
        for (int i=0; i < RETRY_CNT; i++) {
            if (!http.requestPost(contents, SENDDATA_CONTENT_TYPE, token)) {
                // 接続不可時
                errorLogger.error(this.batchName.concat("sendDayLoadServey() fail requestGet"));
                resDto.setErrorMessage("<auth server> error connecting to URI");
                break;
            } else {
                int responseCode = http.getResponseCode();
                if (responseCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                    // 500エラーは一定時間スリープ後にリトライ
                    if (i < RETRY_CNT - 1) {
                        errorLogger.error(this.batchName.concat("sendDayLoadServey() 500error:retrycnt=[" + i + "]"));
                        try {
                            Thread.sleep(RETRY_INTERVAL);
                        } catch (InterruptedException e) {

                        }
                    } else {
                        errorLogger.error(this.batchName.concat("sendDayLoadServey() 500error:retrycnt=MAX"));
                    }
                } else if (responseCode != HttpServletResponse.SC_OK) {
                    // パラメータ不正によるエラー
                    errorLogger.error(this.batchName.concat(String.format("sendDayLoadServey() responseCode=%d", responseCode)));
                    resDto.setHttpResponseCode(responseCode);
                    resDto.setErrorMessage("<auth server> requestParameter error by token");
                    break;
                } else {
                    // 正常応答
                    String body = http.getResponseBody();
                    try {
                        // サーバーからのエラーメッセージを設定したいため一旦Json変換する
                        batchLogger.info(this.batchName.concat(String.format(" sendDayLoadServey() Send to AzbilServer successfully. / send data : url=%s , token=%s , buildngInfoId=%s , ymd=%s , contents=\r\n%s", requestURL, token, buildngInfoId, ymd, contents)));
                        resDto = gson.fromJson(body, AzbilServerSendDayLoadServeyPostResDto.class);
                        resDto.setHttpResponseCode(responseCode);
                    } catch (Exception e) {
                        errorLogger.error(this.batchName.concat("sendDayLoadServey() Json syntax error"));
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
