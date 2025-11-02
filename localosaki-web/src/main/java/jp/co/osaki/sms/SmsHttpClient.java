/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * @author n-takada
 */
public class SmsHttpClient extends BaseConstants {

    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    private final String apiServerAddress;

    public SmsHttpClient(String apiServerAddress) {
        this.apiServerAddress = apiServerAddress;
    }

    /**
     * httpclient
     *
     * @param paramMap
     * @return
     * @throws IOException
     */
    public String sendHttpClient(Map<String, String> paramMap) throws IOException {

        HttpClientBuilder httpBuilder = HttpClientBuilder.create();
        String ret;
        try (CloseableHttpClient client = httpBuilder.build()) {
            if (CheckUtility.isNullOrEmpty(apiServerAddress)) {
                errorLogger.error("api server address is null!");
                throw new IOException("api server address is null!");
            }
            HttpPost post = new HttpPost(apiServerAddress);
            //送信文字コードを設定
            Charset charSet = Charset.forName("UTF-8");

            //送信データを設定
            List<NameValuePair> paramList = new ArrayList<>();
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (!CheckUtility.isNullOrEmpty(key)) {
                    paramList.add(new BasicNameValuePair(key, value));
                }
            }   //データセット
            post.setEntity(new UrlEncodedFormEntity(paramList, charSet));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            //データ送信し結果(XML形式)を取得
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            ret = EntityUtils.toString(entity);
            //コネクションを閉じる
        } catch (IOException ioe) {
            errorLogger.error("api server connect error");
            errorLogger.error(BaseUtility.getStackTraceMessage(ioe));
            throw ioe;
        }

        return ret;
    }
}
