package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * [SMS]ロードサーベイ日データ ResultSetクラス
 * @author nishida.t
 *
 */
public class LoadSurveyResultSet {

    /* 装置ID */
    private String devId;

    /* メーター管理番号 */
    private Long meterMngId;

    /* 取得日時 */
    private String getDate;

    /* 30分毎の使用量 */
    private BigDecimal kwh;

    /* 指針値データ */
    private BigDecimal dmvkwh;

    public LoadSurveyResultSet(String devId, Long meterMngId, String getDate, BigDecimal kwh, BigDecimal dmvkwh) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.getDate = getDate;
        this.kwh = kwh;
        this.dmvkwh = dmvkwh;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public BigDecimal getKwh() {
        return kwh;
    }

    public void setKwh(BigDecimal kwh) {
        this.kwh = kwh;
    }

    public BigDecimal getDmvkwh() {
        return dmvkwh;
    }

    public void setDmvkwh(BigDecimal dmvkwh) {
        this.dmvkwh = dmvkwh;
    }

}
