package jp.co.osaki.sms.bean.sms.collect.dataview;

/**
 * データ収集装置 データ表示機能 表示列データクラス.
 *
 * @author ozaki.y
 */
public class DataViewColumn {

    /** 装置ID */
    private String devId;

    /** 装置名 */
    private String devName;

    /** メーター管理番号(表示用). */
    private String meterMngIdDisp;

    /** 計器ID. */
    private String meterId;

    /** 建物・テナント名. */
    private String tenantName;

    /** メーター種別名称. */
    private String meterTypeName;

    /** 単位. */
    private String unit;

    /** 表示値. */
    private String dispValue;

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

    public String getMeterMngIdDisp() {
        return meterMngIdDisp;
    }

    public void setMeterMngIdDisp(String meterMngIdDisp) {
        this.meterMngIdDisp = meterMngIdDisp;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDispValue() {
        return dispValue;
    }

    public void setDispValue(String dispValue) {
        this.dispValue = dispValue;
    }
}
