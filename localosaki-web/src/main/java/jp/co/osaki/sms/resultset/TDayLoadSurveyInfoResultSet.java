package jp.co.osaki.sms.resultset;

import java.util.List;

/**
 * ロードサーベイ日データ情報ResultSetクラス.
 *
 * @author ozaki.y
 */
public class TDayLoadSurveyInfoResultSet {

    /** 装置ID. */
    private String devId;

    /** メーター管理番号List. */
    private List<Long> meterMngIdList;

    /** 収集日時. */
    private String getDate;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public List<Long> getMeterMngIdList() {
        return meterMngIdList;
    }

    public void setMeterMngIdList(List<Long> meterMngIdList) {
        this.meterMngIdList = meterMngIdList;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }
}
