package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_facility_small_kind database table.
 *
 */
@Entity
@Table(name = "m_facility_small_kind")
@NamedQueries({
    @NamedQuery(name = "MFacilitySmallKind.findAll", query = "SELECT m FROM MFacilitySmallKind m"),
    @NamedQuery(name = "MFacilitySmallKind.findJoin", 
            query = "SELECT m FROM MFacilitySmallKind m INNER JOIN FETCH m.MFacilityBigKind b ORDER BY b.displayOrder, m.displayOrder"),
    @NamedQuery(name = "MFacilitySmallKind.findNotUse", query = "SELECT m FROM MFacilitySmallKind m WHERE NOT EXISTS (SELECT c FROM m.MCorpFacilityKinds c WHERE c.id.corpId =:corpId AND c.id.facilitySmallKindId = m.id.facilitySmallKindId AND c.id.facilityBigKindId = m.id.facilityBigKindId ) AND m.id.facilityBigKindId =:facilityBigKindId"),
    @NamedQuery(name = "MFacilitySmallKind.findByBigKindIdSortDisplayOrder", query = "SELECT m FROM MFacilitySmallKind m WHERE m.id.facilityBigKindId =:facilityBigKindId ORDER BY m.displayOrder ASC"),
    @NamedQuery(name = "MFacilitySmallKind.findFreonInputStateList", query = "SELECT m FROM MFacilitySmallKind m INNER JOIN FETCH M.MFacilityBigKind WHERE m.id.facilityBigKindId = :facilityBigKindId AND m.id.facilitySmallKindId = :facilitySmallKindId")
})
public class MFacilitySmallKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MFacilitySmallKindPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "facility_small_kind_name", nullable = false, length = 100)
    private String facilitySmallKindName;

    @Column(name = "refrigerant_flg", nullable = false)
    private Integer refrigerantFlg;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MCorpFacilityKind
    @OneToMany(mappedBy = "MFacilitySmallKind")
    private List<MCorpFacilityKind> MCorpFacilityKinds;

    //bi-directional many-to-one association to MFacilityBigKind
    @ManyToOne
    @JoinColumn(name = "facility_big_kind_id", nullable = false, insertable = false, updatable = false)
    private MFacilityBigKind MFacilityBigKind;

    public MFacilitySmallKind() {
    }

    public MFacilitySmallKindPK getId() {
        return this.id;
    }

    public void setId(MFacilitySmallKindPK id) {
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

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getFacilitySmallKindName() {
        return this.facilitySmallKindName;
    }

    public void setFacilitySmallKindName(String facilitySmallKindName) {
        this.facilitySmallKindName = facilitySmallKindName;
    }

    public Integer getRefrigerantFlg() {
        return this.refrigerantFlg;
    }

    public void setRefrigerantFlg(Integer refrigerantFlg) {
        this.refrigerantFlg = refrigerantFlg;
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

    public List<MCorpFacilityKind> getMCorpFacilityKinds() {
        return this.MCorpFacilityKinds;
    }

    public void setMCorpFacilityKinds(List<MCorpFacilityKind> MCorpFacilityKinds) {
        this.MCorpFacilityKinds = MCorpFacilityKinds;
    }

    public MCorpFacilityKind addMCorpFacilityKind(MCorpFacilityKind MCorpFacilityKind) {
        getMCorpFacilityKinds().add(MCorpFacilityKind);
        MCorpFacilityKind.setMFacilitySmallKind(this);

        return MCorpFacilityKind;
    }

    public MCorpFacilityKind removeMCorpFacilityKind(MCorpFacilityKind MCorpFacilityKind) {
        getMCorpFacilityKinds().remove(MCorpFacilityKind);
        MCorpFacilityKind.setMFacilitySmallKind(null);

        return MCorpFacilityKind;
    }

    public MFacilityBigKind getMFacilityBigKind() {
        return this.MFacilityBigKind;
    }

    public void setMFacilityBigKind(MFacilityBigKind MFacilityBigKind) {
        this.MFacilityBigKind = MFacilityBigKind;
    }

}
