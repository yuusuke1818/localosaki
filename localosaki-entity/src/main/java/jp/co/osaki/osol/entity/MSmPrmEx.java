package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_sm_prm_ex database table.
 *
 */
@Entity
@Table(name="m_sm_prm_ex")
@NamedQuery(name="MSmPrmEx.findAll", query="SELECT m FROM MSmPrmEx m")
public class MSmPrmEx implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="sm_id", unique=true, nullable=false)
    private Long smId;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="del_flg", nullable=false)
    private Integer delFlg;

    @Column(name="dm_target_not_check_flg", nullable=false)
    private Integer dmTargetNotCheckFlg;

    @Column(name="kw_calc_not_check_flg", nullable=false)
    private Integer kwCalcNotCheckFlg;

    @Column(name="max_limit_val_not_check_flg", nullable=false)
    private Integer maxLimitValNotCheckFlg;

    @Column(name="nothing_data_not_check_flg", nullable=false)
    private Integer nothingDataNotCheckFlg;

    @Column(name="temp_humid_sensor_not_check_flg", nullable=false)
    private Integer tempHumidSensorNotCheckFlg;

    @Column(name="line_val_minus_not_check_flg", nullable=false)
    private Integer lineValMinusNotCheckFlg;

    @Column(name="water_oil_leak_not_check_flg", nullable=false)
    private Integer waterOilLeakNotCheckFlg;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    //bi-directional one-to-one association to MSmPrm
    @OneToOne
    @JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
    private MSmPrm MSmPrm;

    public MSmPrmEx() {
    }

    public Long getSmId() {
        return this.smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
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

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getDmTargetNotCheckFlg() {
        return this.dmTargetNotCheckFlg;
    }

    public void setDmTargetNotCheckFlg(Integer dmTargetNotCheckFlg) {
        this.dmTargetNotCheckFlg = dmTargetNotCheckFlg;
    }

    public Integer getKwCalcNotCheckFlg() {
        return this.kwCalcNotCheckFlg;
    }

    public void setKwCalcNotCheckFlg(Integer kwCalcNotCheckFlg) {
        this.kwCalcNotCheckFlg = kwCalcNotCheckFlg;
    }

    public Integer getMaxLimitValNotCheckFlg() {
        return this.maxLimitValNotCheckFlg;
    }

    public void setMaxLimitValNotCheckFlg(Integer maxLimitValNotCheckFlg) {
        this.maxLimitValNotCheckFlg = maxLimitValNotCheckFlg;
    }

    public Integer getNothingDataNotCheckFlg() {
        return this.nothingDataNotCheckFlg;
    }

    public void setNothingDataNotCheckFlg(Integer nothingDataNotCheckFlg) {
        this.nothingDataNotCheckFlg = nothingDataNotCheckFlg;
    }

    public Integer getTempHumidSensorNotCheckFlg() {
        return this.tempHumidSensorNotCheckFlg;
    }

    public void setTempHumidSensorNotCheckFlg(Integer tempHumidSensorNotCheckFlg) {
        this.tempHumidSensorNotCheckFlg = tempHumidSensorNotCheckFlg;
    }

    public Integer getLineValMinusNotCheckFlg() {
        return this.lineValMinusNotCheckFlg;
    }

    public void setLineValMinusNotCheckFlg(Integer lineValMinusNotCheckFlg) {
        this.lineValMinusNotCheckFlg = lineValMinusNotCheckFlg;
    }

    public Integer getWaterOilLeakNotCheckFlg() {
        return this.waterOilLeakNotCheckFlg;
    }

    public void setWaterOilLeakNotCheckFlg(Integer waterOilLeakNotCheckFlg) {
        this.waterOilLeakNotCheckFlg = waterOilLeakNotCheckFlg;
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

    public MSmPrm getMSmPrm() {
        return this.MSmPrm;
    }

    public void setMSmPrm(MSmPrm MSmPrm) {
        this.MSmPrm = MSmPrm;
    }

}