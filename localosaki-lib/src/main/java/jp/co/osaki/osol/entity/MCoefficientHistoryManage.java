package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_coefficient_history_manage database table.
 *
 */
@Entity
@Table(name = "m_coefficient_history_manage")
@NamedQueries({
    @NamedQuery(name = "MCoefficientHistoryManage.findAll",
            query = "SELECT m FROM MCoefficientHistoryManage m"),
    @NamedQuery(name = "MCoefficientHistoryManage.findEnergyCdAndIdYoungest",
            query = "SELECT m "
            + "FROM MCoefficientHistoryManage m "
            + "WHERE m.id.engTypeCd =:engTypeCd "
            + "AND m.id.engId =:engId "
            + "AND m.id.dayAndNightType =:dayAndNightType "
            + "AND m.startYm <=:currentYm "
            + "AND (m.endYm IS NULL OR m.endYm >=:currentYm)")})
public class MCoefficientHistoryManage implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MCoefficientHistoryManagePK id;

    @Column(name = "adjust_co2_coefficient", nullable = false, precision = 15, scale = 10)
    private BigDecimal adjustCo2Coefficient;

    @Column(name = "adjust_co2_unit", nullable = false, length = 100)
    private String adjustCo2Unit;

    @Column(name = "city_gas_standard", length = 50)
    private String cityGasStandard;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "end_ym", length = 6)
    private String endYm;

    @Column(name = "gas_coefficient", precision = 15, scale = 10)
    private BigDecimal gasCoefficient;

    @Column(name = "start_ym", nullable = false, length = 6)
    private String startYm;

    @Column(name = "std_co2_coefficient", nullable = false, precision = 15, scale = 10)
    private BigDecimal stdCo2Coefficient;

    @Column(name = "std_co2_unit", nullable = false, length = 100)
    private String stdCo2Unit;

    @Column(name = "std_first_eng_coefficient", nullable = false, precision = 15, scale = 10)
    private BigDecimal stdFirstEngCoefficient;

    @Column(name = "std_first_eng_unit", nullable = false, length = 100)
    private String stdFirstEngUnit;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MEnergy
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "eng_id", referencedColumnName = "eng_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "eng_type_cd", referencedColumnName = "eng_type_cd", nullable = false, insertable = false, updatable = false)
    })
    private MEnergy MEnergy;

    public MCoefficientHistoryManage() {
    }

    public MCoefficientHistoryManagePK getId() {
        return this.id;
    }

    public void setId(MCoefficientHistoryManagePK id) {
        this.id = id;
    }

    public BigDecimal getAdjustCo2Coefficient() {
        return this.adjustCo2Coefficient;
    }

    public void setAdjustCo2Coefficient(BigDecimal adjustCo2Coefficient) {
        this.adjustCo2Coefficient = adjustCo2Coefficient;
    }

    public String getAdjustCo2Unit() {
        return this.adjustCo2Unit;
    }

    public void setAdjustCo2Unit(String adjustCo2Unit) {
        this.adjustCo2Unit = adjustCo2Unit;
    }

    public String getCityGasStandard() {
        return this.cityGasStandard;
    }

    public void setCityGasStandard(String cityGasStandard) {
        this.cityGasStandard = cityGasStandard;
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

    public String getEndYm() {
        return this.endYm;
    }

    public void setEndYm(String endYm) {
        this.endYm = endYm;
    }

    public BigDecimal getGasCoefficient() {
        return this.gasCoefficient;
    }

    public void setGasCoefficient(BigDecimal gasCoefficient) {
        this.gasCoefficient = gasCoefficient;
    }

    public String getStartYm() {
        return this.startYm;
    }

    public void setStartYm(String startYm) {
        this.startYm = startYm;
    }

    public BigDecimal getStdCo2Coefficient() {
        return this.stdCo2Coefficient;
    }

    public void setStdCo2Coefficient(BigDecimal stdCo2Coefficient) {
        this.stdCo2Coefficient = stdCo2Coefficient;
    }

    public String getStdCo2Unit() {
        return this.stdCo2Unit;
    }

    public void setStdCo2Unit(String stdCo2Unit) {
        this.stdCo2Unit = stdCo2Unit;
    }

    public BigDecimal getStdFirstEngCoefficient() {
        return this.stdFirstEngCoefficient;
    }

    public void setStdFirstEngCoefficient(BigDecimal stdFirstEngCoefficient) {
        this.stdFirstEngCoefficient = stdFirstEngCoefficient;
    }

    public String getStdFirstEngUnit() {
        return this.stdFirstEngUnit;
    }

    public void setStdFirstEngUnit(String stdFirstEngUnit) {
        this.stdFirstEngUnit = stdFirstEngUnit;
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

    public MEnergy getMEnergy() {
        return this.MEnergy;
    }

    public void setMEnergy(MEnergy MEnergy) {
        this.MEnergy = MEnergy;
    }

}
