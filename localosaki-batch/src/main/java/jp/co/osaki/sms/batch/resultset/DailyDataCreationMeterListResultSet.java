package jp.co.osaki.sms.batch.resultset;

/**
 * 日報データ作成用メーター登録用 ResultSetクラス
 *
 * @author sagi_h
 *
 */
public class DailyDataCreationMeterListResultSet {
    /** 装置ID */
    private String devId;

    /** メーター管理番号 */
    private Long meterMngId;

    /**
     * @param devId
     * @param meterMngId
     */
    public DailyDataCreationMeterListResultSet(String devId, Long meterMngId) {
        this.devId = devId;
        this.meterMngId = meterMngId;
    }

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @return meterMngId
     */
    public Long getMeterMngId() {
        return meterMngId;
    }

    /**
     * @param meterMngId セットする meterMngId
     */
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

}