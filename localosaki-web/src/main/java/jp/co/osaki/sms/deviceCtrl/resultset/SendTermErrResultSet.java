package jp.co.osaki.sms.deviceCtrl.resultset;

public class SendTermErrResultSet {

    private String meterMngId;
    private String termState;
    private String tenantName;
    private String inspDate;
    private String meterType;

    public SendTermErrResultSet() {

    }

    public SendTermErrResultSet(String meterMngId, String termState, String tenantName) {
        super();
        this.meterMngId = meterMngId;
        this.termState = termState;
        this.tenantName = tenantName;
    }

    public String getMeterMngId() {
        return meterMngId;
    }
    public void setMeterMngId(String meterMngId) {
        this.meterMngId = meterMngId;
    }
    public String getTermState() {
        return termState;
    }
    public void setTermState(String termState) {
        this.termState = termState;
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
