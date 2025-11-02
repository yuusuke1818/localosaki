package jp.co.osaki.sms.deviceCtrl.resultset;

public class SendMeterErrResultSet {
    private String meterMngId;
    private String meterId;
    private String meterState;
    private String tenantName;
    private String inspDate;
    private String meterType;



    public SendMeterErrResultSet(String meterMngId, String meterId, String meterState, String tenantName,
            String inspDate, String meterType) {
        super();
        this.meterMngId = meterMngId;
        this.meterId = meterId;
        this.meterState = meterState;
        this.tenantName = tenantName;
        this.inspDate = inspDate;
        this.meterType = meterType;
    }


    public SendMeterErrResultSet() {

    }


    public String getMeterMngId() {
        return meterMngId;
    }
    public void setMeterMngId(String meterMngId) {
        this.meterMngId = meterMngId;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    public String getMeterState() {
        return meterState;
    }
    public void setMeterState(String meterState) {
        this.meterState = meterState;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getInspDate() {
        return inspDate;
    }

    public void setInspDate(String inspDate) {
        this.inspDate = inspDate;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }


}
