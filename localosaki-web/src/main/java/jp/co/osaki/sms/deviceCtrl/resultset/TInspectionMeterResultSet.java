package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TInspectionMeterResultSet {

    private String devId;
    private Long meterMngId;
    private String inspYear;
    private String inspMonth;
    private String inspMonthNo;
    private Timestamp createDate;
    private Long createUserId;
    private String inspType;
    private Timestamp latestInspDate;
    private String latestInspVal;
    private BigDecimal latestUseVal;
    private BigDecimal multipleRate;
    private Timestamp prevInspDate;
    private Timestamp prevInspDate2;
    private String prevInspVal;
    private String prevInspVal2;
    private BigDecimal prevUseVal;
    private Timestamp recDate;
    private String recMan;
    private Timestamp updateDate;
    private Long updateUserId;
    private BigDecimal usePerRate;

    public TInspectionMeterResultSet(String devId, Long meterMngId, String inspYear, String inspMonth,
            String inspMonthNo, String inspType, Timestamp latestInspDate, String latestInspVal,
            BigDecimal latestUseVal, BigDecimal multipleRate, Timestamp prevInspDate, Timestamp prevInspDate2,
            String prevInspVal, String prevInspVal2, BigDecimal prevUseVal, BigDecimal usePerRate) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.inspYear = inspYear;
        this.inspMonth = inspMonth;
        this.inspMonthNo = inspMonthNo;
        this.inspType = inspType;
        this.latestInspDate = latestInspDate;
        this.latestInspVal = latestInspVal;
        this.latestUseVal = latestUseVal;
        this.multipleRate = multipleRate;
        this.prevInspDate = prevInspDate;
        this.prevInspDate2 = prevInspDate2;
        this.prevInspVal = prevInspVal;
        this.prevInspVal2 = prevInspVal2;
        this.prevUseVal = prevUseVal;
        this.usePerRate = usePerRate;
    }



    public TInspectionMeterResultSet() {

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
    public String getInspMonthNo() {
        return inspMonthNo;
    }
    public void setInspMonthNo(String inspMonthNo) {
        this.inspMonthNo = inspMonthNo;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    public Long getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public String getInspType() {
        return inspType;
    }
    public void setInspType(String inspType) {
        this.inspType = inspType;
    }
    public Timestamp getLatestInspDate() {
        return latestInspDate;
    }
    public void setLatestInspDate(Timestamp latestInspDate) {
        this.latestInspDate = latestInspDate;
    }
    public String getLatestInspVal() {
        return latestInspVal;
    }
    public void setLatestInspVal(String latestInspVal) {
        this.latestInspVal = latestInspVal;
    }
    public BigDecimal getLatestUseVal() {
        return latestUseVal;
    }
    public void setLatestUseVal(BigDecimal latestUseVal) {
        this.latestUseVal = latestUseVal;
    }
    public BigDecimal getMultipleRate() {
        return multipleRate;
    }
    public void setMultipleRate(BigDecimal multipleRate) {
        this.multipleRate = multipleRate;
    }
    public Timestamp getPrevInspDate() {
        return prevInspDate;
    }
    public void setPrevInspDate(Timestamp prevInspDate) {
        this.prevInspDate = prevInspDate;
    }
    public Timestamp getPrevInspDate2() {
        return prevInspDate2;
    }
    public void setPrevInspDate2(Timestamp prevInspDate2) {
        this.prevInspDate2 = prevInspDate2;
    }
    public String getPrevInspVal() {
        return prevInspVal;
    }
    public void setPrevInspVal(String prevInspVal) {
        this.prevInspVal = prevInspVal;
    }
    public String getPrevInspVal2() {
        return prevInspVal2;
    }
    public void setPrevInspVal2(String prevInspVal2) {
        this.prevInspVal2 = prevInspVal2;
    }
    public BigDecimal getPrevUseVal() {
        return prevUseVal;
    }
    public void setPrevUseVal(BigDecimal prevUseVal) {
        this.prevUseVal = prevUseVal;
    }
    public Timestamp getRecDate() {
        return recDate;
    }
    public void setRecDate(Timestamp recDate) {
        this.recDate = recDate;
    }
    public String getRecMan() {
        return recMan;
    }
    public void setRecMan(String recMan) {
        this.recMan = recMan;
    }
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    public Long getUpdateUserId() {
        return updateUserId;
    }
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
    public BigDecimal getUsePerRate() {
        return usePerRate;
    }
    public void setUsePerRate(BigDecimal usePerRate) {
        this.usePerRate = usePerRate;
    }









}
