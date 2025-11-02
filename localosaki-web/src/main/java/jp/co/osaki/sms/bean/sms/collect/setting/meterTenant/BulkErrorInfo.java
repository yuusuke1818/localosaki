package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.util.Optional;

public class BulkErrorInfo {

    public BulkErrorInfo() {
        super();
    }

    public BulkErrorInfo(Optional<String> error, String value) {
        super();
        this.error = error;
        this.value = value;
    }

    public BulkErrorInfo(String error, String value) {
        super();
        this.error = Optional.of(error) ;
        this.value = value;
    }

    //エラー内容
    private Optional<String> error;

    //CSV項目値
    private String value;

    public Optional<String> getError() {
        return error;
    }

    public void setError(Optional<String> error) {
        this.error = error;
    }

    public void setStringError(String error) {
        this.error = Optional.of(error);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
