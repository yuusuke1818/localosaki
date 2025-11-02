package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the m_subtype database table.
 *
 */
@Entity
@Table(name = "m_subtype")
@NamedQueries({
    @NamedQuery(name = "MSubtype.findAll", query = "SELECT m FROM MSubtype m"),
    @NamedQuery(name = "MSubtype.findReport", query = "SELECT M FROM MSubtype M WHERE M.id.corpId = :corpId AND M.mainBusinessFlg = :mainBusinessFlg"),
    @NamedQuery(name = "MSubtype.findReport2", query = "SELECT M FROM MSubtype M INNER JOIN M.TBuildingSubtypes tb INNER JOIN tb.TBuilding tb WHERE tb.id.corpId = :corpId AND tb.id.buildingId = :buildingId"),})
public class MSubtype implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MSubtypePK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "main_business_flg", nullable = false)
    private Integer mainBusinessFlg;

    @Column(name = "securetary_name", nullable = false, length = 100)
    private String securetaryName;

    @Column(name = "subtype_name", nullable = false, length = 100)
    private String subtypeName;

    @Column(name = "subtype_no", nullable = false, length = 4)
    private String subtypeNo;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Column(name = "unit_divide_id", nullable = false)
    private Long unitDivideId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MCorp
    @ManyToOne
    @JoinColumn(name = "corp_id", nullable = false, insertable = false, updatable = false)
    private MCorp MCorp;

    //bi-directional many-to-one association to MUnitDivide
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "unit_divide_id", referencedColumnName = "unit_divide_id", nullable = false, insertable = false, updatable = false)
    })
    private MUnitDivide MUnitDivide;

    //bi-directional many-to-one association to TBuildingSubtype
    @OneToMany(mappedBy = "MSubtype")
    private List<TBuildingSubtype> TBuildingSubtypes;

    public MSubtype() {
    }

    public MSubtypePK getId() {
        return this.id;
    }

    public void setId(MSubtypePK id) {
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

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getMainBusinessFlg() {
        return this.mainBusinessFlg;
    }

    public void setMainBusinessFlg(Integer mainBusinessFlg) {
        this.mainBusinessFlg = mainBusinessFlg;
    }

    public String getSecuretaryName() {
        return this.securetaryName;
    }

    public void setSecuretaryName(String securetaryName) {
        this.securetaryName = securetaryName;
    }

    public String getSubtypeName() {
        return this.subtypeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    public String getSubtypeNo() {
        return this.subtypeNo;
    }

    public void setSubtypeNo(String subtypeNo) {
        this.subtypeNo = subtypeNo;
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

    public Long getUnitDivideId() {
        return unitDivideId;
    }

    public void setUnitDivideId(Long unitDivideId) {
        this.unitDivideId = unitDivideId;
    }

    public MCorp getMCorp() {
        return this.MCorp;
    }

    public void setMCorp(MCorp MCorp) {
        this.MCorp = MCorp;
    }

    public MUnitDivide getMUnitDivide() {
        return this.MUnitDivide;
    }

    public void setMUnitDivide(MUnitDivide MUnitDivide) {
        this.MUnitDivide = MUnitDivide;
    }

    public List<TBuildingSubtype> getTBuildingSubtypes() {
        return this.TBuildingSubtypes;
    }

    public void setTBuildingSubtypes(List<TBuildingSubtype> TBuildingSubtypes) {
        this.TBuildingSubtypes = TBuildingSubtypes;
    }

    public TBuildingSubtype addTBuildingSubtype(TBuildingSubtype TBuildingSubtype) {
        getTBuildingSubtypes().add(TBuildingSubtype);
        TBuildingSubtype.setMSubtype(this);

        return TBuildingSubtype;
    }

    public TBuildingSubtype removeTBuildingSubtype(TBuildingSubtype TBuildingSubtype) {
        getTBuildingSubtypes().remove(TBuildingSubtype);
        TBuildingSubtype.setMSubtype(null);

        return TBuildingSubtype;
    }

}
