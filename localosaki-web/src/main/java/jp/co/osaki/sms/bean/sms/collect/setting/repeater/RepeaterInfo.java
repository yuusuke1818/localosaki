package jp.co.osaki.sms.bean.sms.collect.setting.repeater;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;

public class RepeaterInfo {
    private Boolean checkBox;
    private String status;
    private ListSmsRepeatersResultData repeater;

    public Boolean getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ListSmsRepeatersResultData getRepeater() {
        return repeater;
    }
    public void setRepeater(ListSmsRepeatersResultData repeater) {
        this.repeater = repeater;
    }
}
