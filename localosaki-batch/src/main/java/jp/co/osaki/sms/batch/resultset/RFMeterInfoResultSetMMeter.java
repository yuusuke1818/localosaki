package jp.co.osaki.sms.batch.resultset;

/**
 * SMS CSV取込 メーター登録用リストResultSetクラス
 *
 * @author yonezawa.a
 *
 */
public class RFMeterInfoResultSetMMeter {

    private String devId;

    private Long meterMngId;

    private Long meterType;

    private String wirelessType;

    public RFMeterInfoResultSetMMeter(String devId, Long meterMngId, Long meterType, String wirelessType) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.meterType = meterType;
        this.wirelessType = wirelessType;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Long getMeterMngId() {
        return this.meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getWirelessType() {
        return wirelessType;
    }

    public void setWirelessType(String wirelessType) {
        this.wirelessType = wirelessType;
    }

}
