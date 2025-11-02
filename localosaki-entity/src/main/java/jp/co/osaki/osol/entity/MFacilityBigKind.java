package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_facility_big_kind database table.
 *
 */
@Entity
@Table(name = "m_facility_big_kind")
@NamedQueries({
    @NamedQuery(name = "MFacilityBigKind.findAll", query = "SELECT m FROM MFacilityBigKind m"),
    @NamedQuery(name = "MFacilityBigKind.findNotUse", query = "SELECT m FROM MFacilityBigKind m WHERE NOT EXISTS (SELECT c FROM MCorpFacilityKind c WHERE c.id.corpId = :corpId AND c.id.facilityBigKindId = m.facilityBigKindId)"),
    @NamedQuery(name = "MFacilityBigKind.findUse", query = "SELECT m FROM MFacilityBigKind m WHERE EXISTS (SELECT c FROM MCorpFacilityKind c WHERE c.id.corpId = :corpId AND c.id.facilityBigKindId = m.facilityBigKindId AND c.delFlg='0') ORDER BY m.displayOrder ASC"),
    @NamedQuery(name = "MFacilityBigKind.findAllSortDisplayOrder", query = "SELECT m FROM MFacilityBigKind m ORDER BY m.displayOrder ASC"),
    @NamedQuery(name = "MFacilityBigKind.findFreonInputStateList", query = "SELECT m FROM MFacilityBigKind m WHERE M.facilityBigKindId = :facilityBigKindId")
})
public class MFacilityBigKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "facility_big_kind_id", unique = true, nullable = false)
    private Long facilityBigKindId;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "facility_big_kind_name", nullable = false, length = 100)
    private String facilityBigKindName;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MFacilitySmallKind
    @OneToMany(mappedBy = "MFacilityBigKind")
    private List<MFacilitySmallKind> MFacilitySmallKinds;

    public MFacilityBigKind() {
    }

    public Long getFacilityBigKindId() {
        return this.facilityBigKindId;
    }

    public void setFacilityBigKindId(Long facilityBigKindId) {
        this.facilityBigKindId = facilityBigKindId;
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

    public String getFacilityBigKindName() {
        return this.facilityBigKindName;
    }

    public void setFacilityBigKindName(String facilityBigKindName) {
        this.facilityBigKindName = facilityBigKindName;
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

    public List<MFacilitySmallKind> getMFacilitySmallKinds() {
        return this.MFacilitySmallKinds;
    }

    public void setMFacilitySmallKinds(List<MFacilitySmallKind> MFacilitySmallKinds) {
        this.MFacilitySmallKinds = MFacilitySmallKinds;
    }

    public MFacilitySmallKind addMFacilitySmallKind(MFacilitySmallKind MFacilitySmallKind) {
        getMFacilitySmallKinds().add(MFacilitySmallKind);
        MFacilitySmallKind.setMFacilityBigKind(this);

        return MFacilitySmallKind;
    }

    public MFacilitySmallKind removeMFacilitySmallKind(MFacilitySmallKind MFacilitySmallKind) {
        getMFacilitySmallKinds().remove(MFacilitySmallKind);
        MFacilitySmallKind.setMFacilityBigKind(null);

        return MFacilitySmallKind;
    }

}
