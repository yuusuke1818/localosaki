package jp.co.osaki.sms.bean.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants;

/**
 * HTTPリクエストクラス
 * @author akr_iwamoto
 */
public class HttpRequester extends BaseConstants {
    /**
     * リクエストURLサーバアドレス
     */
    private String serverAddress = "";

    /**
     * リクエストURLディレクトリ
     */
    private String directory = "";

    /**
     * リクエストURLパラメータ
     */
    private String urlParameter = "";

    /**
     * リクエストヘッダパラメータ
     */
    private Map<String, String> paramMap;

    /**
     * レスポンスコード
     */
    private int responseCode = 0;

    /**
     * レスポンスヘッダ
     */
    private Map<String, String> responseHeader = new HashMap<>();

    /**
     * レスポンスボディ
     */
    private String responseBody = "";

    /**
     * バッチ処理名称
     */
    protected String batchName = this.getClass().getSimpleName();

    /**
     * バッチログ
     */
    protected static Logger batchLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.BATCH.getVal());

    /**
     * イベントログ
     */
    protected static Logger eventLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.EVENT.getVal());

    /**
     * エラーログ
     */
    protected static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    /**
     * Content-Type: application/x-www-form-urlencoded
     */
    private static final String CONTENT_TYPE_SEND_PARAM = "application/x-www-form-urlencoded";

    /**
     * コンストラクタ
     */
    public HttpRequester(String batchName) {
        this.batchName = batchName;
        init(null, null, null);
    }

    public void init(String addr, String dir, Map<String, String> paramMap) {
        setServerAddress(addr);
        setURLDirectory(dir);
        setURLParameter(paramMap);
    }
    /**
     * HTTPリクエストURLのアドレスを指定する
     * @param addr
     */
    public void setServerAddress(String addr) {
        this.serverAddress = addr;
    }
    /**
     * HTTPリクエストURLのディレクトリを指定する
     * @param dir
     */
    public void setURLDirectory(String dir) {
        this.directory = "";
        addURLDirectory(dir);
    }
    /**
     * HTTPリクエストURLのディレクトリを追加する
     * @param dir
     */
    public void addURLDirectory(String dir) {
        if (!CheckUtility.isNullOrEmpty(dir)) {
            this.directory += "/" + dir;
        }
    }
    /**
     * HTTPリクエストURLのパラメータを設定する
     * @param paramMap
     */
    public void setURLParameter(Map<String, String> paramMap) {
        this.urlParameter = "";
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (!CheckUtility.isNullOrEmpty(key)) {
                    if (this.urlParameter.isEmpty()) {
                        this.urlParameter += "?";
                    }
                    else {
                        this.urlParameter += "&";
                    }
                    this.urlParameter += key + "=" + value;
                }
            }
        }
    }
    /**
     * HTTPリクエストヘッダを設定する
     * @param paramMap
     */
    public void setHeaderParameter(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
    /**
     * HTTPリクエスト(GET)を送信する
     *
     * @param token "Authorization"の値  (null: "Authorization"を設定しない)
     * @return 成功したらtrue
     */
    public boolean requestGet(String token) {
        boolean bRet = false;

        String url = this.serverAddress + this.directory + this.urlParameter;
        if (CheckUtility.isNullOrEmpty(url)) {
            errorLogger.error(this.batchName.concat("requestGet: URL is null!"));
        }
        else {
            HttpClientBuilder httpBuilder = HttpClientBuilder.create();
            try (CloseableHttpClient client = httpBuilder.build()) {
                //送信データを設定
                HttpGet get = new HttpGet(url);
                if (this.paramMap != null) {
                    for (String key : this.paramMap.keySet()) {
                        String value = this.paramMap.get(key);
                        if (!CheckUtility.isNullOrEmpty(key)) {
                            get.setHeader(key, value);
                        }
                    }
                }
                if (token != null) {
                    // トークン設定
                    get.setHeader("Authorization", "Bearer " + token);
                }

                //データ送信し結果(XML形式)を取得
                HttpResponse response = client.execute(get);
                //レスポンスコードとレスポンスヘッダとボディを取得
                CheckResponse(response);
                //コネクションを閉じる
                bRet = true;
            }
            catch (IOException ioe) {
                errorLogger.error(this.batchName.concat(String.format("requestGet: http server connect error (%s)", url)));
            }
        }
        return bRet;
    }

    /**
     * HTTPリクエスト(POST)を送信する
     *
     * @return 成功したらtrue
     */
    public boolean requestPost() {
        boolean bRet = false;

        String url = this.serverAddress + this.directory;
        if (CheckUtility.isNullOrEmpty(url)) {
            errorLogger.error(this.batchName.concat("requestPost: URL ihs null!"));
        }
        else {
            try {
                URL requrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) requrl.openConnection();

                //POSTに指定
                connection.setRequestMethod("POST");
                //アウトプット可能
                connection.setDoOutput(true);
                //入力可能
                connection.setDoInput(true);
                //cache無し
                connection.setUseCaches(false);

                // データタイプをjsonに指定する
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                //コネクション、通信開始
                connection.connect();

                this.responseCode = connection.getResponseCode();
                if(this.responseCode == HttpURLConnection.HTTP_OK){
                    // 通信に成功した
                    bRet = true;

                    // テキストを取得する
                    InputStream in= connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    if( null == encoding ) {
                        encoding = "UTF-8";
                    }
                    StringBuffer result = new StringBuffer();
                    final InputStreamReader inReader = new InputStreamReader(in, encoding);
                    final BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;
                    // 1行ずつテキストを読み込む
                    while( (line = bufReader.readLine()) != null) {
                        result.append(line);
                    }
                    // 応答内容設定
                    responseBody = result.toString();

                    //  クローズ
                    bufReader.close();
                    inReader.close();
                    in.close();
                }
                // 切断
                connection.disconnect();
            } catch (Exception e) {
                errorLogger.error(this.batchName.concat(String.format("requestPost: http server connect error (%s)", url)));
            }

        }
        return bRet;
    }

    /**
     * HTTPリクエスト(POST)を送信する(パラメータ送付)(トークン無し)
     * @param paramMap パラメータ
     * @return 成功したらtrue
     */
    public boolean requestPost(Map<String,String> paramMap) {
        String content = paramMap.keySet().stream()
                .map(key -> urlEncoder(key) + "=" + urlEncoder(paramMap.get(key)))
                .collect(Collectors.joining("&"));

        return requestPost(content, CONTENT_TYPE_SEND_PARAM, null);
    }

    /**
     * HTTPリクエスト(POST)を送信する(トークン付きデータ送付)
     *
     * @param content コンテンツ
     * @param contentHeader "Content-Type"の値
     * @param token "Authorization"の値  (null: "Authorization"を設定しない)
     * @return 成功したらtrue
     */
    public boolean requestPost(String content, String contentHeader, String token) {
        boolean bRet = false;

        String url = this.serverAddress + this.directory;
        if (CheckUtility.isNullOrEmpty(url)) {
            errorLogger.error(this.batchName.concat("requestPost: URL ihs null!"));
        }
        else {
            try {
                URL requrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) requrl.openConnection();

                //POSTに指定
                connection.setRequestMethod("POST");
                //アウトプット可能
                connection.setDoOutput(true);
                //入力可能
                connection.setDoInput(true);
                //cache無し
                connection.setUseCaches(false);

                // データタイプをjsonに指定する
                connection.setRequestProperty("Content-Type", contentHeader);

                if (CONTENT_TYPE_SEND_PARAM.equals(contentHeader)) {
                    // データサイズを設定する
                    connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
                }

                if (token != null) {
                    // トークン設定
                    connection.setRequestProperty("Authorization","Bearer " + token);
                }

                //コネクション、通信開始
                connection.connect();

                if (content != null) {
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(content);
                    writer.close();
                }

                this.responseCode = connection.getResponseCode();
                if(this.responseCode == HttpURLConnection.HTTP_OK){
                    // 通信に成功した
                    bRet = true;

                    // テキストを取得する
                    InputStream in= connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    if( null == encoding ) {
                        encoding = "UTF-8";
                    }
                    StringBuffer result = new StringBuffer();
                    final InputStreamReader inReader = new InputStreamReader(in, encoding);
                    final BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;
                    // 1行ずつテキストを読み込む
                    while( (line = bufReader.readLine()) != null) {
                        result.append(line);
                    }
                    // 応答内容設定
                    responseBody = result.toString();

                    //  クローズ
                    bufReader.close();
                    inReader.close();
                    in.close();
                }
                // 切断
                connection.disconnect();
            } catch (Exception e) {
                errorLogger.error(this.batchName.concat(String.format("requestPost: http server connect error (%s)", url)));
            }

        }
        return bRet;
    }

    /**
     * HTTPリクエスト(POST)を送信する(トークン付きデータ送付)
     *
     * @param meterMngId メーター管理番号
     * @param apiName API名
     * @param url 対象URL
     * @param content コンテンツ
     * @param contentHeader "Content-Type"の値
     * @param authType "Authorization"のキー
     * @param token "Authorization"の値
     * @param logJson ログ用JSON文字列
     * @return 実行結果
     */
    public boolean requestPost(Long meterMngId, String apiName, String url, String content,
            String contentHeader, String authType, String token, String logJson) {

        boolean bRet = false;

        if (CheckUtility.isNullOrEmpty(url)) {
            errorLogger.error(this.batchName.concat("requestPost: URL is null!"));
        } else {
            try {
                URL requrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) requrl.openConnection();

                // POST指定
                connection.setRequestMethod("POST");
                // アウトプット可能
                connection.setDoOutput(true);
                // 入力可能
                connection.setDoInput(true);
                // cache無し
                connection.setUseCaches(false);

                // データタイプをjsonに指定する
                connection.setRequestProperty("Content-Type", contentHeader);

                if (CONTENT_TYPE_SEND_PARAM.equals(contentHeader)) {
                    // データサイズを設定する
                    connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
                }

                if (token != null && authType != null) {
                    connection.setRequestProperty("Authorization", authType + " " + token);
                }

                // 出力
                System.out.println(String.format(
                        "【メーター管理番号:%d】 [%sリクエスト] url=%s, request=%s",
                        meterMngId,
                        apiName,
                        url,
                        logJson
                    ));
                eventLogger.info(String.format(
                        "【メーター管理番号:%d】 [%sリクエスト] url=%s, request=%s",
                        meterMngId,
                        apiName,
                        url,
                        logJson
                    ));

                // コネクション、通信開始
                bRet = true;
                connection.connect();
                if (content != null) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(content);
                    writer.close();
                }

                this.responseCode = connection.getResponseCode();

                // レスポンスヘッダー取得
                responseHeader.clear();
                Map<String, List<String>> headers = connection.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    String headerName = entry.getKey();
                    if (headerName == null) {
                        continue; // ステータスライン 例)"HTTP/1.1 200 OK"
                    }
                    List<String> headerValues = entry.getValue();
                    for (String value : headerValues) {
                        // テストにてデバッグ用に記載のためコメントアウト
//                        System.out.println(String.format("[%sレスポンスヘッダー] %s: %s", apiName, headerName, value));
//                        eventLogger.info(String.format("[%sレスポンスヘッダー] %s: %s", apiName, headerName, value));
                        String joined = "set-cookie".equalsIgnoreCase(headerName)
                                ? String.join("; ", entry.getValue())
                                : String.join(", ", entry.getValue());
                        responseHeader.put(headerName, joined);
                    }
                }

                if (this.responseCode == HttpURLConnection.HTTP_OK) {
                    // 通信に成功
                    bRet = true;

                    // テキストを取得する
                    InputStream in = connection.getInputStream();
                    String encoding = connection.getContentEncoding();
                    if (null == encoding) {
                        encoding = "UTF-8";
                    }
                    StringBuffer result = new StringBuffer();
                    final InputStreamReader inReader = new InputStreamReader(in, encoding);
                    final BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;
                    // 1行ずつテキストを読み込む
                    while ((line = bufReader.readLine()) != null) {
                        result.append(line);
                        System.out.println(String.format(
                                "【メーター管理番号:%d】 [%sレスポンス] レスポンスコード:%s, body=%s",
                                meterMngId,
                                apiName,
                                this.responseCode,
                                result.toString()
                            ));
                        eventLogger.info(String.format(
                                "【メーター管理番号:%d】 [%sレスポンス] レスポンスコード:%s, body=%s",
                                meterMngId,
                                apiName,
                                this.responseCode,
                                result.toString()
                            ));
                    }
                    // 応答内容設定
                    responseBody = result.toString();

                    // クローズ
                    bufReader.close();
                    inReader.close();
                    in.close();
                } else {
                    System.out.println(String.format(
                            "【メーター管理番号:%d】 [%sレスポンス] レスポンスコード:%s",
                            meterMngId,
                            apiName,
                            this.responseCode
                        ));
                }
                // 切断
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                errorLogger.error(String.format("【メーター管理番号:%d】"  + this.batchName.concat(String.format("requestPost: http server connect error (%s)", url)), meterMngId));
            }
        }
        return bRet;
    }

    /**
     * HTTPリクエストのPOSTパラメータを設定する
     *
     * @param paramMap
     * @return
     */
    List<NameValuePair> setPostParameter(Map<String, String> paramMap) {
        this.urlParameter = "";
        if (paramMap != null) {
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (!CheckUtility.isNullOrEmpty(key)) {
                    paramList.add(new BasicNameValuePair(key, value));
                }
            }
            return paramList;
        }
        return null;
    }

    /**
     * HTTPリクエストのPOSTパラメータを設定する
     *
     * @param paramMap
     * @return
     */
    String setPostParameterStr(Map<String, String> paramMap) {
        this.urlParameter = "";
        if (paramMap != null) {
            String param = "";
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (!CheckUtility.isNullOrEmpty(param)) {
                    // 終端に区切りを挿入
                    param = param + "&";
                }
                // key=value
                param = param + key + "=" + value;
            }
            return param;
        }
        return null;
    }

    /**
     * HTTPレスポンスコードを得る
     * @return
     */
    public int getResponseCode() {
        return this.responseCode;
    }
    /**
     * HTTPレスポンスヘッダを得る
     * @return
     */
    public Map<String, String> getResponseHeader() {
        return this.responseHeader;
    }
    /**
     * HTTPレスポンスボディを得る
     * @return
     */
    public String getResponseBody() {
        return this.responseBody;
    }
    /**
     * HTTPレスポンスヘッダから任意のキーの値を得る
     * @param key
     * @return
     */
    public String getResponseHeaderValue(String key) {
        return this.responseHeader.get(key);
    }

    /**
     * 応答からレスポンスコードとヘッダとボディを取得する
     * @param response httpレスポンス
     */
    private void CheckResponse(HttpResponse response) throws IOException {
        //レスポンスコードを取得
        StatusLine status = response.getStatusLine();
        this.responseCode = status.getStatusCode();
        //レスポンスヘッダを取得
        this.responseHeader = new HashMap<>();
        Header[] headers = response.getAllHeaders();
        for (Header hdr : headers) {
            this.responseHeader.put(hdr.getName(), hdr.getValue());
        }
        //レスポンスボディを取得
        HttpEntity entity = response.getEntity();
        this.responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }

    /**
     * URLエンコード
     * @param value 値
     * @return URLエンコード後の値
     */
    private String urlEncoder(String value) {
        if (value == null) {
            return null;
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

}
