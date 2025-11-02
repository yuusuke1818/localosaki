package jp.co.osaki.sms.batch.resultset;

public class CheckSurveyDevPrmResultSet {

    String devId;
    String devName;

    public CheckSurveyDevPrmResultSet(String devId, String devName) {
        this.devId = devId;
        this.devName = devName;
    }

    public CheckSurveyDevPrmResultSet() {

    }

    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
    }
    public String getDevName() {
        return devName;
    }
    public void setDevName(String devName) {
        this.devName = devName;
    }
}
