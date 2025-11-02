package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;
import java.util.Date;

public class AutoInspMeterSvrResultSet {
    /* 装置ID */
    private String devId;
    /* メーター管理番号 */
    private Long meterMngId;
    /* A:自動検針 M:任意検針 */
    private String inspType;
    /* 自動検針年 */
    private String inspYear;
    /* 自動検針月 */
    private String inspMonth;
    /* 月検針連番 1から999 */
    private Long inspMonthNo;
    /* 処理終了フラグ(0:未完了 1:完了) */
    private BigDecimal endFlg;
    /* 最新検針値_日時 */
    private Date latestInspDate;
    /* 前回検針値_日時 */
    private Date prevInspDate;
    /* 最新検針値(整数部最大5桁、小数部最大1桁) */
    private BigDecimal latestInspVal;
    /* 前回検針値(整数部最大5桁、小数部最大1桁) */
    private BigDecimal prevInspVal;
    /* 前前回検針値(整数部最大5桁、小数部最大1桁) */
    private BigDecimal prevInspVal2;
    public AutoInspMeterSvrResultSet(String devId, Long meterMngId, String inspType, String inspYear, String inspMonth, Long inspMonthNo, BigDecimal endFlg, Date latestInspDate, Date prevInspDate, BigDecimal latestInspVal, BigDecimal prevInspVal, BigDecimal prevInspVal2) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.inspType = inspType;
        this.inspYear = inspYear;
        this.inspMonth = inspMonth;
        this.inspMonthNo = inspMonthNo;
        this.endFlg = endFlg;
        this.latestInspDate = latestInspDate;
        this.prevInspDate = prevInspDate;
        this.latestInspVal = latestInspVal;
        this.prevInspVal = prevInspVal;
        this.prevInspVal2 = prevInspVal2;
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
    public String getInspType() {
        return inspType;
    }
    public void setInspType(String inspType) {
        this.inspType = inspType;
    }
    public String getInspYear() {
        return inspYear;
    }
    public void setInspYear(String inspYear) {
        this.inspYear = inspYear;
    }
    public String getInspMonth() {
        return inspMonth;
    }
    public void setInspMonth(String inspMonth) {
        this.inspMonth = inspMonth;
    }
    public Long getInspMonthNo() {
        return inspMonthNo;
    }
    public void setInspMonthNo(Long inspMonthNo) {
        this.inspMonthNo = inspMonthNo;
    }
    public BigDecimal getEndFlg() {
        return endFlg;
    }
    public void setEndFlg(BigDecimal endFlg) {
        this.endFlg = endFlg;
    }
    public Date getLatestInspDate() {
        return latestInspDate;
    }
    public void setLatestInspDate(Date latestInspDate) {
        this.latestInspDate = latestInspDate;
    }
    public Date getPrevInspDate() {
        return prevInspDate;
    }
    public void setPrevInspDate(Date prevInspDate) {
        this.prevInspDate = prevInspDate;
    }
    public BigDecimal getLatestInspVal() {
        return latestInspVal;
    }
    public void setLatestInspVal(BigDecimal latestInspVal) {
        this.latestInspVal = latestInspVal;
    }
    public BigDecimal getPrevInspVal() {
        return prevInspVal;
    }
    public void setPrevInspVal(BigDecimal prevInspVal) {
        this.prevInspVal = prevInspVal;
    }
    public BigDecimal getPrevInspVal2() {
        return prevInspVal2;
    }
    public void setPrevInspVal2(BigDecimal prevInspVal2) {
        this.prevInspVal2 = prevInspVal2;
    }


}
