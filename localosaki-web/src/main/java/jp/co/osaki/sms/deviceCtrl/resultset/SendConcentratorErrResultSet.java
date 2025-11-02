package jp.co.osaki.sms.deviceCtrl.resultset;

public class SendConcentratorErrResultSet {
    private String concentId;
    private String concentState;

    public SendConcentratorErrResultSet() {

    }

    public String getConcentId() {
        return concentId;
    }
    public void setConcentId(String concentId) {
        this.concentId = concentId;
    }
    public String getConcentState() {
        return concentState;
    }
    public void setConcentState(String concentState) {
        this.concentState = concentState;
    }
}
