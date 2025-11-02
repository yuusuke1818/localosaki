package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 開閉制御 DTOクラス
 *
 * @author y.nakamura
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwitchApiResDto {

    /** 処理結果コード（0:正常／それ以外:異常） アプリ内の管理用でありJSONとは無関係 */
    private int processResult = 0;

    /** HTTPレスポンスコード */
    private int httpResponseCode = 0;

    /** エラー情報（null:正常） */
    private String errorMessage = null;


    /** リクエストID */
    @JsonProperty("request_id")
    private String requestId;

    /** メッセージ */
    @JsonProperty("message")
    private String message;

    /** 実行結果オブジェクト */
    @JsonProperty("result")
    private ResultBody resultBody;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultBody {

        /** 開閉区分 */
        @JsonProperty("open_mode")
        private String openMode;

        /** 最終更新ユーザ */
        @JsonProperty("last_update_user")
        private Integer lastUpdateUser;

        /** 最終更新日時(書式: yyyy/M/d HH:mm:ss) */
        @JsonProperty("last_update_at")
        private String lastUpdateAt;

        public String getOpenMode() {
            return openMode;
        }

        public void setOpenMode(String openMode) {
            this.openMode = openMode;
        }

        public Integer getLastUpdateUser() {
            return lastUpdateUser;
        }

        public void setLastUpdateUser(Integer lastUpdateUser) {
            this.lastUpdateUser = lastUpdateUser;
        }

        public String getLastUpdateAt() {
            return lastUpdateAt;
        }

        public void setLastUpdateAt(String lastUpdateAt) {
            this.lastUpdateAt = lastUpdateAt;
        }
    }

    public int getProcessResult() {
        return processResult;
    }

    public void setProcessResult(int processResult) {
        this.processResult = processResult;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBody getResultBody() {
        return resultBody;
    }

    public void setResultBody(ResultBody resultBody) {
        this.resultBody = resultBody;
    }

    /** 正常応答判定（processResult==0 かつ errorMessage==null） */
    public boolean isSuccess() {
        return this.processResult == 0 && this.errorMessage == null;
    }

    @Override
    public String toString() {
        return "SwitchReadoutApiResDto{" + "processResult=" + processResult + ", httpResponseCode=" + httpResponseCode + ", errorMessage='" + errorMessage + '\'' + ", requestId='" + requestId + '\'' + ", message='" + message + '\'' + ", resultBody=" + (resultBody != null ? resultBody.getClass().getSimpleName() : "null") + '}';
    }

}
