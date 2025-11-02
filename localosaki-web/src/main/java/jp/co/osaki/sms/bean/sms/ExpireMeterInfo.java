package jp.co.osaki.sms.bean.sms;

public class ExpireMeterInfo {
    /** メータ管理番号 */
    private Long meterMngId;

    /** コマンドフラグ */
    private String commandFlg;

    /** 処理フラグ */
    private String srvEnt;

    /** 操作状態 */
    private String operationStatus;

    /** 建物番号 */
    private String buildingNo;

    /** ユーザーコード */
    private Long tenantId;

    /** 建物名 */
    private String buildingName;

    /** メーターID */
    private String meterId;

    /** メーター種別 */
    private Long meterType;

    /** メーター種別名称 */
    private String meterTypeName;

    /** 検満年月(YYYYMM) */
    private String examEndYm;

    /** 検満通知 */
    private String examNotice;

    /** 建物ID */
    private Long buildingId;

    /** 装置ID */
    private String devId;

    /** 企業ID */
    private String corpId;

    /** チェックボックスの状態 */
    private Boolean checkBox;

    /** version */
    private int version;

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getCommandFlg() {
        return commandFlg;
    }

    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
    }

    public String getSrvEnt() {
        return srvEnt;
    }

    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getExamEndYm() {
        return examEndYm;
    }

    public void setExamEndYm(String examEndYm) {
        this.examEndYm = examEndYm;
    }

    public String getExamNotice() {
        return examNotice;
    }

    public void setExamNotice(String examNotice) {
        this.examNotice = examNotice;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
