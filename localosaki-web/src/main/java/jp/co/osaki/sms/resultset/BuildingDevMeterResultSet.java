package jp.co.osaki.sms.resultset;

import java.math.BigDecimal;

import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物・装置・メーター検索ResultSet.
 *
 * @author ozaki.y
 */
public class BuildingDevMeterResultSet {

    /** 企業ID. */
    private String corpId;

    /** 建物ID. */
    private Long buildingId;

    /** メーター管理番号. */
    private Long meterMngId;

    /** 装置ID. */
    private String devId;

    /** テナント名. */
    private String tenantName;

    /** メーター種類(A:スマートメーター, P:パルスメーター). */
    private String meterKind;

    /** 乗率. */
    private BigDecimal multi;

    /** テナントフラグ. */
    private boolean isTenant;

    /** メーター管理番号(表示用). */
    private String meterMngIdDisp;

    public BuildingDevMeterResultSet() {
    }

    public BuildingDevMeterResultSet(String corpId, Long buildingId, String devId, String meterId, BigDecimal multi,
            Long meterMngId) {

        // 装置に紐付くメーター情報取得用
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.devId = devId;
        this.multi = multi;
        setMeterMngId(meterMngId);
        setMeterKindByMeterId(meterId);
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
        if (meterMngId != null) {
            this.meterMngIdDisp = String.format("%03d", meterMngId);
        }
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getMeterKind() {
        return meterKind;
    }

    public void setMeterKind(String meterKind) {
        this.meterKind = meterKind;
    }

    public BigDecimal getMulti() {
        return multi;
    }

    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

    public boolean isTenant() {
        return isTenant;
    }

    public void setTenant(boolean isTenant) {
        this.isTenant = isTenant;
    }

    public String getMeterMngIdDisp() {
        return meterMngIdDisp;
    }

    public void setMeterMngIdDisp(String meterMngIdDisp) {
        this.meterMngIdDisp = meterMngIdDisp;
    }

    public void setMeterKindByMeterId(String meterId) {
        if (CheckUtility.isNullOrEmpty(meterId)) {
            return;
        }

        setMeterKind(meterId.substring(0, 1));
    }
}
