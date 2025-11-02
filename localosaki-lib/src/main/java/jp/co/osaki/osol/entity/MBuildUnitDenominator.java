package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_build_unit_denominator database table.
 *
 */
@Entity
@Table(name = "m_build_unit_denominator")
@NamedQueries({
    @NamedQuery(name = "MBuildUnitDenominator.findAll", query = "SELECT m FROM MBuildUnitDenominator m"),
    @NamedQuery(name = "MBuildUnitDenominator.energyInputGetUseUnitDenominator",
            query = "SELECT DISTINCT m FROM MBuildUnitDenominator m "
            + "LEFT JOIN FETCH m.MUnitDivide mud "
            + "LEFT JOIN FETCH mud.MUnitFactor muf "
            + "LEFT JOIN FETCH m.TMonthlyUnitDenominators tmud "
            + "LEFT JOIN FETCH m.TBuilding tb "
            + "WHERE m.id.corpId =:corpId "
            + "AND m.id.buildingId =:buildingId "
            + "AND (:gentani IS NULL OR mud.unitDivideFormula IS NOT NULL) "
            + "AND (:gentani IS NOT NULL OR mud.unitDivideFormula IS NULL) "
            + "AND (mud.delFlg IS NULL OR mud.delFlg = 0) "
            + "AND (tb.delFlg IS NULL OR tb.delFlg = 0) "
            + "ORDER BY m.id.unitDivideId, tmud.id.ym"),
    @NamedQuery(name = "MBuildUnitDenominator.findByBuildingId",
            query = "SELECT DISTINCT m FROM MBuildUnitDenominator m "
            + "LEFT JOIN FETCH m.MUnitDivide mud "
            + "LEFT JOIN FETCH mud.MUnitFactor muf "
            + "WHERE m.id.corpId =:corpId "
            + "AND m.id.buildingId =:buildingId "
            + "AND mud.delFlg <>1 "
            + "ORDER BY m.id.buildingId, m.id.unitDivideId "),
    @NamedQuery(name = "MBuildUnitDenominator.energyInputYearUnitDenominator",
            query = "SELECT DISTINCT m FROM MBuildUnitDenominator m "
            + "LEFT JOIN FETCH m.MUnitDivide mud "
            + "LEFT JOIN FETCH mud.MUnitFactor "
            + "LEFT JOIN FETCH m.TMonthlyUnitDenominators tmud "
            + "LEFT JOIN FETCH m.TBuilding tb "
            + "WHERE m.id.corpId =:corpId "
            + "AND (m.id.buildingId IN :buildingId) "
            + "AND (tmud.id.ym >=:nendoStartYm OR tmud.id.ym IS NULL) "
            + "AND (tmud.id.ym <=:nendoEndYm  OR tmud.id.ym IS NULL) "
            + "AND m.delFlg =:delFlg "
            + "AND mud.delFlg =:delFlg "
            + "ORDER BY m.id.buildingId, m.id.unitDivideId ")})

public class MBuildUnitDenominator implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MBuildUnitDenominatorPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "default_unit_denominator", precision = 23, scale = 10)
    private BigDecimal defaultUnitDenominator;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "unit_divide_not_flg", nullable = false)
    private Integer unitDivideNotFlg;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MUnitDivide
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "unit_divide_id", referencedColumnName = "unit_divide_id", nullable = false, insertable = false, updatable = false)
    })
    private MUnitDivide MUnitDivide;

    //bi-directional many-to-one association to TBuilding
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false)
    })
    private TBuilding TBuilding;

    //bi-directional many-to-one association to TMonthlyUnitDenominator
    @OneToMany(mappedBy = "MBuildUnitDenominator")
    private List<TMonthlyUnitDenominator> TMonthlyUnitDenominators;

    public MBuildUnitDenominator() {
    }

    public MBuildUnitDenominatorPK getId() {
        return this.id;
    }

    public void setId(MBuildUnitDenominatorPK id) {
        this.id = id;
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

    public BigDecimal getDefaultUnitDenominator() {
        return this.defaultUnitDenominator;
    }

    public void setDefaultUnitDenominator(BigDecimal defaultUnitDenominator) {
        this.defaultUnitDenominator = defaultUnitDenominator;
    }

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getUnitDivideNotFlg() {
        return this.unitDivideNotFlg;
    }

    public void setUnitDivideNotFlg(Integer unitDivideNotFlg) {
        this.unitDivideNotFlg = unitDivideNotFlg;
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

    public MUnitDivide getMUnitDivide() {
        return this.MUnitDivide;
    }

    public void setMUnitDivide(MUnitDivide MUnitDivide) {
        this.MUnitDivide = MUnitDivide;
    }

    public TBuilding getTBuilding() {
        return this.TBuilding;
    }

    public void setTBuilding(TBuilding TBuilding) {
        this.TBuilding = TBuilding;
    }

    public List<TMonthlyUnitDenominator> getTMonthlyUnitDenominators() {
        return this.TMonthlyUnitDenominators;
    }

    public void setTMonthlyUnitDenominators(List<TMonthlyUnitDenominator> TMonthlyUnitDenominators) {
        this.TMonthlyUnitDenominators = TMonthlyUnitDenominators;
    }

    public TMonthlyUnitDenominator addTMonthlyUnitDenominator(TMonthlyUnitDenominator TMonthlyUnitDenominator) {
        getTMonthlyUnitDenominators().add(TMonthlyUnitDenominator);
        TMonthlyUnitDenominator.setMBuildUnitDenominator(this);

        return TMonthlyUnitDenominator;
    }

    public TMonthlyUnitDenominator removeTMonthlyUnitDenominator(TMonthlyUnitDenominator TMonthlyUnitDenominator) {
        getTMonthlyUnitDenominators().remove(TMonthlyUnitDenominator);
        TMonthlyUnitDenominator.setMBuildUnitDenominator(null);

        return TMonthlyUnitDenominator;
    }

}
