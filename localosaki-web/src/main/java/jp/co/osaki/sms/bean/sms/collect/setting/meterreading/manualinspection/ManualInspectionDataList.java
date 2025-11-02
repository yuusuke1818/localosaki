package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

/**
 * 任意検針 一覧表示データリストクラス.
 *
 * @author yonezawa.a
 */
public final class ManualInspectionDataList {

    /**
     * メーター管理番号
     */
    private Long meterMngId;

    /**
     * 計器ID
     */
    private String meterId;

    /**
     * 種別名（M_METER.METER_TYPE_NAME）
     */
    private String meterTypeName;

    /**
     * ユーザーコード（M_TENANT_SMS.TENANT_ID）
     */
    private String userCd;

    /**
     * ユーザー名（T_BUILDING.BUILDING_NAME）
     */
    private String buildingName;

    /**
     * メータータイプ
     */
    private Long meterType;

    /**
     * チェックボックス
     */
    private Boolean checkBox;

    /**
     * 装置ID
     */
    private String devId;

    /**
     * 装置名
     */
    private String devName;

    /**
     * 予約検針日時.
     */
    private String reserveInspDate;

    /** 排他制御用カラム. */
    private Integer version;

    public ManualInspectionDataList() {
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getUserCd() {
        return userCd;
    }

    public void setUserCd(String userCd) {
        this.userCd = userCd;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
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

    public String getReserveInspDate() {
        return reserveInspDate;
    }

    public void setReserveInspDate(String reserveInspDate) {
        this.reserveInspDate = reserveInspDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
