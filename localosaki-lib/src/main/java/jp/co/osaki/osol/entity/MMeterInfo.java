package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_meter_info database table.
 *
 */
@Entity
@Table(name="m_meter_info")
@NamedQuery(name="MMeterInfo.findAll", query="SELECT m FROM MMeterInfo m")
public class MMeterInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="meter_id")
    private String meterId;

    @Column(name="backup_func")
    private BigDecimal backupFunc;

    private BigDecimal bidirectional;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="create_user_id")
    private Long createUserId;

    @Column(name="multi_method")
    private BigDecimal multiMethod;

    @Column(name="num_digit")
    private BigDecimal numDigit;

    @Column(name="phase_type")
    private BigDecimal phaseType;

    @Column(name="product_name")
    private String productName;

    @Column(name="rated_current")
    private BigDecimal ratedCurrent;

    @Column(name="rated_voltage")
    private BigDecimal ratedVoltage;

    @Column(name="rec_date")
    private Timestamp recDate;

    @Column(name="rec_man")
    private String recMan;

    @Column(name="serial_num")
    private String serialNum;

    @Column(name="switch_code")
    private BigDecimal switchCode;

    @Column(name="trans_ratio_den")
    private BigDecimal transRatioDen;

    @Column(name="trans_ratio_num")
    private BigDecimal transRatioNum;

    @Column(name="update_date")
    private Timestamp updateDate;

    @Column(name="update_user_id")
    private Long updateUserId;

    @Version
    private Integer version;

    public MMeterInfo() {
    }

    public String getMeterId() {
        return this.meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public BigDecimal getBackupFunc() {
        return this.backupFunc;
    }

    public void setBackupFunc(BigDecimal backupFunc) {
        this.backupFunc = backupFunc;
    }

    public BigDecimal getBidirectional() {
        return this.bidirectional;
    }

    public void setBidirectional(BigDecimal bidirectional) {
        this.bidirectional = bidirectional;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public BigDecimal getMultiMethod() {
        return this.multiMethod;
    }

    public void setMultiMethod(BigDecimal multiMethod) {
        this.multiMethod = multiMethod;
    }

    public BigDecimal getNumDigit() {
        return this.numDigit;
    }

    public void setNumDigit(BigDecimal numDigit) {
        this.numDigit = numDigit;
    }

    public BigDecimal getPhaseType() {
        return this.phaseType;
    }

    public void setPhaseType(BigDecimal phaseType) {
        this.phaseType = phaseType;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getRatedCurrent() {
        return this.ratedCurrent;
    }

    public void setRatedCurrent(BigDecimal ratedCurrent) {
        this.ratedCurrent = ratedCurrent;
    }

    public BigDecimal getRatedVoltage() {
        return this.ratedVoltage;
    }

    public void setRatedVoltage(BigDecimal ratedVoltage) {
        this.ratedVoltage = ratedVoltage;
    }

    public Timestamp getRecDate() {
        return this.recDate;
    }

    public void setRecDate(Timestamp recDate) {
        this.recDate = recDate;
    }

    public String getRecMan() {
        return this.recMan;
    }

    public void setRecMan(String recMan) {
        this.recMan = recMan;
    }

    public String getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public BigDecimal getSwitchCode() {
        return this.switchCode;
    }

    public void setSwitchCode(BigDecimal switchCode) {
        this.switchCode = switchCode;
    }

    public BigDecimal getTransRatioDen() {
        return this.transRatioDen;
    }

    public void setTransRatioDen(BigDecimal transRatioDen) {
        this.transRatioDen = transRatioDen;
    }

    public BigDecimal getTransRatioNum() {
        return this.transRatioNum;
    }

    public void setTransRatioNum(BigDecimal transRatioNum) {
        this.transRatioNum = transRatioNum;
    }

    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}