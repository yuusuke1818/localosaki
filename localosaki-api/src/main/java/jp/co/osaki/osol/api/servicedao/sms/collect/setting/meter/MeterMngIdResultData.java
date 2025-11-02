package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import jp.co.osaki.osol.api.OsolApiResultData;

/**
 * メーター管理番号取得 ResultData
 * @author kobayashi.sho
 */
public class MeterMngIdResultData extends OsolApiResultData {

    /** 機器ID ※検索条件(必須). */
    private String devId;

    /** メーターID ※検索条件(必須). */
    private String meterId;

    /** メーター管理番号. */
    private Long meterMngId;

    /**
     * 検索条件用
     * @param devId 機器ID
     * @param meterId メーターID
     */
    public MeterMngIdResultData(String devId, String meterId) {
        this.devId = devId;
        this.meterId = meterId;
    }

    /**
     * 検索結果用
     * @param meterMngId メーター管理番号
     */
    public MeterMngIdResultData(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

}
