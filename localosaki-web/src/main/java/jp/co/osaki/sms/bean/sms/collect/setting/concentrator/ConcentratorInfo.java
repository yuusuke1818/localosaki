package jp.co.osaki.sms.bean.sms.collect.setting.concentrator;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;

public class ConcentratorInfo {
    private Boolean checkBox;
    private String status;
    private ListSmsConcentratorsResultData concentrator;

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
    public ListSmsConcentratorsResultData getConcentrator() {
        return concentrator;
    }
    public void setConcentrator(ListSmsConcentratorsResultData concentrator) {
        this.concentrator = concentrator;
    }
}
