package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

public class OcrSendMailResultSet {
    String tenantName;
    String meterId;
    String serverDate;
    String devId;

    public String getTenantName() {
        return tenantName;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    public String getServerDate() {
        return serverDate;
    }
    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }
    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
    }


}
