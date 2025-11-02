package jp.co.osaki.osol.api.auth;

import java.sql.Timestamp;

/**
 * OSOL内部からのアクセスAPIキーの情報を格納するクラス
 *
 * @author ya-ishida
 */
public final class OsolApiPrivateAuthInfo {

    /**
     * APIキー
     */
    private String apiKey;

    /**
     * APIキー発行日時
     */
    private Timestamp apiKeyIssueTime;

    /**
     * APIキー有効期限日時
     */
    private Timestamp apiKeyLimitTime;

    public OsolApiPrivateAuthInfo() {
    }

    public OsolApiPrivateAuthInfo(String apiKey, Timestamp apiKeyIssueTime, Timestamp apiKeyLimitTime) {
        this.apiKey = apiKey;
        this.apiKeyIssueTime = apiKeyIssueTime;
        this.apiKeyLimitTime = apiKeyLimitTime;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Timestamp getApiKeyIssueTime() {
        return apiKeyIssueTime;
    }

    public void setApiKeyIssueTime(Timestamp apiKeyIssueTime) {
        this.apiKeyIssueTime = apiKeyIssueTime;
    }

    public Timestamp getApiKeyLimitTime() {
        return apiKeyLimitTime;
    }

    public void setApiKeyLimitTime(Timestamp apiKeyLimitTime) {
        this.apiKeyLimitTime = apiKeyLimitTime;
    }

}
