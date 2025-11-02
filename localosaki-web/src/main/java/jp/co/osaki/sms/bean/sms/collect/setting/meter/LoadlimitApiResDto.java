package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 負荷制限 DTOクラス
 *
 * @author y.nakamura
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadlimitApiResDto {

    /** 処理結果コード（0:正常／それ以外:異常） アプリ内の管理用でありJSONとは無関係 */
    private int processResult = 0;

    /** HTTPレスポンスコード */
    private int httpResponseCode = 0;

    /** エラー情報（null:正常） */
    private String errorMessage = null;

    /** リクエストID */
    @JsonProperty("request_id")
    private String requestId;

    /** メッセージ内容 */
    @JsonProperty("message")
    private String message;

    /** 実行結果オブジェクト */
    @JsonProperty("result")
    private ResultBody resultBody;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultBody {

        /** 負荷制限対象(reglr等) */
        @JsonProperty("loadlimit_target")
        private String loadlimitTarget;

        /** 負荷制限モード(文字列"1"等) */
        @JsonProperty("loadlimit_mode")
        private String loadlimitMode;

        /** 負荷電流 */
        @JsonProperty("load_current")
        private Integer loadCurrent;

        /** 自動投入時間（秒） */
        @JsonProperty("auto_injection")
        private Integer autoInjection;

        /** 自動投入回数（回） */
        @JsonProperty("breaker_act_count")
        private Integer breakerActCount;

        /** 自動投入クリア時間（分） */
        @JsonProperty("count_clear")
        private Integer countClear;

        /** 最終更新ユーザ */
        @JsonProperty("last_update_user")
        private Integer lastUpdateUser;

        /** 最終更新日時(書式: yyyy/M/d HH:mm:ss) */
        @JsonProperty("last_update_at")
        private String lastUpdateAt;

        public String getLoadlimitTarget() {
            return loadlimitTarget;
        }

        public void setLoadlimitTarget(String v) {
            this.loadlimitTarget = v;
        }

        public String getLoadlimitMode() {
            return loadlimitMode;
        }

        public void setLoadlimitMode(String v) {
            this.loadlimitMode = v;
        }

        public Integer getLoadCurrent() {
            return loadCurrent;
        }

        public void setLoadCurrent(Integer v) {
            this.loadCurrent = v;
        }

        public Integer getAutoInjection() {
            return autoInjection;
        }

        public void setAutoInjection(Integer v) {
            this.autoInjection = v;
        }

        public Integer getBreakerActCount() {
            return breakerActCount;
        }

        public void setBreakerActCount(Integer v) {
            this.breakerActCount = v;
        }

        public Integer getCountClear() {
            return countClear;
        }

        public void setCountClear(Integer v) {
            this.countClear = v;
        }

        public Integer getLastUpdateUser() {
            return lastUpdateUser;
        }

        public void setLastUpdateUser(Integer v) {
            this.lastUpdateUser = v;
        }

        public String getLastUpdateAt() {
            return lastUpdateAt;
        }

        public void setLastUpdateAt(String v) {
            this.lastUpdateAt = v;
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

    /** 正常応答かを返す（processResult==0 かつ errorMessage==null） */
    public boolean isSuccess() {
        return this.processResult == 0 && this.errorMessage == null;
    }

    @Override
    public String toString() {
        return "LoadlimitApiResDto{" + "processResult=" + processResult + ", httpResponseCode=" + httpResponseCode + ", errorMessage='" + errorMessage + '\'' + ", requestId='" + requestId + '\'' + ", message='" + message + '\'' + ", resultBody=" + (resultBody != null ? resultBody.getClass().getSimpleName() : "null") + '}';
    }

}
