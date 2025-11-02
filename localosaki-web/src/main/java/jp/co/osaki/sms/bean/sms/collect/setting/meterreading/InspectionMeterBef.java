package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterBefResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterSvrResultData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 確定前検針データ一覧 検索結果
 * @author kobayashi.sho
 */
public class InspectionMeterBef {

    /** 確定前データフラグ true:確定前データ false:検針漏れデータ. */
    private boolean isInspection;

    /** 選択. */
    private Boolean checkbox;

    /** 装置ID(Key). */
    private String devId;

    /** メーター管理番号(Key). */
    private Long meterMngId;

    /** データ取得日時(Key). */
    private Date latestInspDate;

    /** メーター管理番号 重複フラグ (true:重複  false:重複なし). */
    private Boolean duplicate;

    /** データ取得日時 ※表示用. */
    private String latestInspDateDisp;

    /** REC_DATE. */
    private Date recDate;

    /** REC_MAN. */
    private String recMan;

    /** メーター種別. */
    private Long meterType;

    /** メーター種別名. */
    private String meterTypeName;

    /** 乗率. */
    private BigDecimal multi;

    /** 最新検針値. */
    private BigDecimal latestInspVal;

    /** ユーザーコード. */
    private Long userCode; // tenantId

    /** テナント番号. */
    private String tenantNo; // buildingNo

    /** テナント名. */
    private String tenantName; // buildingName

    /** 排他制御用カラム. */
    private Integer version;

    /**
     * 一覧画面 表示用
     * @param result 確定前検針データ
     */
    public InspectionMeterBef(InspectionMeterBefResultData result) {
        this.isInspection = result.getIsInspection();   // 確定前データフラグ true:確定前データ false:検針漏れデータ
        this.checkbox = false;
        this.devId = result.getDevId();                 // 装置ID(Key)
        this.meterMngId = result.getMeterMngId();       // メーター管理番号(Key)
        this.latestInspDate = result.getLatestInspDate(); // データ取得日時(Key)
        this.latestInspDateDisp = DateUtility.changeDateFormat(result.getLatestInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH); // データ取得日時 ※表示用
        this.recDate = result.getRecDate();             // REC_DATE
        this.recMan = result.getRecMan();               // REC_MAN
        this.meterType = result.getMeterType();         // メーター種別
        this.meterTypeName = result.getMeterTypeName(); // メーター種別名
        this.multi = result.getMulti();                 // 乗率
        this.latestInspVal = result.getLatestInspVal(); // 最新検針値
        this.userCode = result.getUserCode();           // ユーザーコード
        this.tenantNo = result.getTenantNo();           // テナント番号
        this.tenantName = result.getTenantName();       // テナント名
        this.version = result.getVersion();             // 排他制御用カラム
    }

    /**
     * 新規検針連番追加画面・既存検針連番更新画面 の 登録処理の後処理用（入力値のミス度で、登録できなかった分のデータの差し戻し用）
     * @param result 確定用検針データ（入力値のミス度で、登録できなかった分のデータの差し戻し分）
     */
    public InspectionMeterBef(InspectionMeterSvrResultData result) {
        // ※isInspection, checkbox, devId, latestInspDateDisp, recDate, recMan, meterType, meterTypeName, tenantNo, tenantName は、新規検針連番追加画面・既存検針連番更新画面 で使用しないのでセット不要
        this.meterMngId = result.getMeterMngId();       // メーター管理番号
        this.latestInspDate = result.getLatestInspDate(); // データ取得日時
        this.multi = result.getMulti();                 // 乗率
        this.latestInspVal = result.getLatestInspVal(); // 最新検針値
        this.version = result.getVersion();             // 排他制御用カラム
    }

    public boolean getIsInspection() {
        return isInspection;
    }

    public void setIsInspection(boolean isInspection) {
        this.isInspection = isInspection;
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

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public BigDecimal getLatestInspVal() {
        return latestInspVal;
    }

    public void setLatestInspVal(BigDecimal latestInspVal) {
        this.latestInspVal = latestInspVal;
    }

    public BigDecimal getMulti() {
        return multi;
    }

    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

    public Date getLatestInspDate() {
        return latestInspDate;
    }

    public void setLatestInspDate(Date latestInspDate) {
        this.latestInspDate = latestInspDate;
    }

    public String getLatestInspDateDisp() {
        return latestInspDateDisp;
    }

    public void setLatestInspDateDisp(String latestInspDateDisp) {
        this.latestInspDateDisp = latestInspDateDisp;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantNo() {
        return tenantNo;
    }

    public void setTenantNo(String tenantNo) {
        this.tenantNo = tenantNo;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getRecDate() {
        return recDate;
    }

    public void setRecDate(Date recDate) {
        this.recDate = recDate;
    }

    public String getRecMan() {
        return recMan;
    }

    public void setRecMan(String recMan) {
        this.recMan = recMan;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public Boolean getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

}
