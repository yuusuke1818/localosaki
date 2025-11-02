package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * SMS 日報当日分のデータ欠損メール送信 CSV添付用データ
 */
public class MissngDataCsvResultSet implements Cloneable {

    /** 企業ID */
    private String corpId;

    /** 企業名 */
    private String corpName;

    /** 建物ID */
    private Long buildingId;

    /** 建物名（親） */
    private String buildingName;

    /** 装置Id */
    private String devId;

    /** 装置名 */
    private String devName;

    /** 種別名称 */
    private String meterTypeName;

    /** メーター管理番号 */
    private Long meterMngId;

    /** テナント名 */
    private String tenantName;

    /** メーター種別 **/
    private Long meterType;

    /** メーターID（計器ID） */
    private String meterId;

    /** 時限（時刻） **/
    private String jigenTime;

    /** メーター状態 **/
    private BigDecimal meterSta;

    /** メーター状況 **/
    private BigDecimal meterPresSitu;

    /** アラート停止期間（開始） **/
    private String alertPauseStart;

    /** アラート停止期間（終了） **/
    private String alertPauseEnd;

    /** メーター備考 **/
    private String meterStaMemo;

    public MissngDataCsvResultSet() {

    }

    // メーターから建物関係の情報を取得
    public MissngDataCsvResultSet(String corpId, String corpName, Long buildingId, String buildingName, String devId,
            String devName, Long meterMngId, Long meterType, String meterId) {
        super();
        this.corpId = corpId;
        this.corpName = corpName;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.devId = devId;
        this.devName = devName;
        this.meterMngId = meterMngId;
        this.meterType = meterType;
        this.meterId = meterId;
    }

    public MissngDataCsvResultSet(String corpId, String corpName, Long buildingId, String buildingName, String devId,
            String devName, Long meterMngId, Long meterType, String meterId, BigDecimal meterSta, BigDecimal meterPresSitu,
            String alertPauseStart, String alertPauseEnd, String meterStaMemo) {
        super();
        this.corpId = corpId;
        this.corpName = corpName;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.devId = devId;
        this.devName = devName;
        this.meterMngId = meterMngId;
        this.meterType = meterType;
        this.meterId = meterId;
        this.meterSta = meterSta;
        this.meterPresSitu = meterPresSitu;
        this.alertPauseStart = alertPauseStart;
        this.alertPauseEnd = alertPauseEnd;
        this.meterStaMemo = meterStaMemo;
    }

    // テナント情報関係を取得
    public MissngDataCsvResultSet(String corpId, Long buildingId, String buildingName, String devId, Long meterMngId) {
        super();
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.devId = devId;
        this.meterMngId = meterMngId;
    }

    // Cloneableインターフェースのclone()メソッドをオーバーライド
    @Override
    public MissngDataCsvResultSet clone() {
        MissngDataCsvResultSet missngDataCsvResultSet = null;
        // CloneNotSupportedExceptionを返す可能性があるので例外処理が必要
        try {
            // Object型で返ってくるのでキャストが必要
            missngDataCsvResultSet = (MissngDataCsvResultSet)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return missngDataCsvResultSet;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getJigenTime() {
        return jigenTime;
    }

    public void setJigenTime(String jigenTime) {
        this.jigenTime = jigenTime;
    }

    public BigDecimal getMeterSta() {
        return meterSta;
    }

    public void setMeterSta(BigDecimal meterSta) {
        this.meterSta = meterSta;
    }

    public BigDecimal getMeterPresSitu() {
        return meterPresSitu;
    }

    public void setMeterPresSitu(BigDecimal meterSta) {
        this.meterPresSitu = meterSta;
    }

    public String getAlertPauseStart() {
        return alertPauseStart;
    }

    public void setAlertPauseStart(String alertPauseStart) {
        this.alertPauseStart = alertPauseStart;
    }

    public String getAlertPauseEnd() {
        return alertPauseEnd;
    }

    public void setAlertPauseEnd(String alertPauseEnd) {
        this.alertPauseEnd = alertPauseEnd;
    }

    public String getMeterStaMemo() {
        return meterStaMemo;
    }

    public void setMeterStaMemo(String meterStaMemo) {
        this.meterStaMemo = meterStaMemo;
    }

}
