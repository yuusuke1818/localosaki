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
 * The persistent class for the m_corp_facility_kind database table.
 *
 */
@Entity
@Table(name = "m_corp_facility_kind")
@NamedQueries({
    @NamedQuery(name = "MCorpFacilityKind.findAll", query = "SELECT m FROM MCorpFacilityKind m"),
    @NamedQuery(name = "MCorpFacilityKind.findBigKindId",
            query = "SELECT m FROM MCorpFacilityKind m "
                    + " LEFT JOIN FETCH m.MFacilitySmallKind mfs "
                    + " LEFT JOIN FETCH mfs.MFacilityBigKind mfb "
                    + " WHERE m.id.corpId =:corpId "
                    + " AND m.delFlg = 0 "
                    + " ORDER BY m.bigDisplayOrder "),
    @NamedQuery(name = "MCorpFacilityKind.findSmallKindId",
            query = "SELECT m FROM MCorpFacilityKind m "
                    + " LEFT JOIN FETCH m.MFacilitySmallKind mfs "
                    + " WHERE m.id.corpId =:corpId "
                    + " AND m.id.facilityBigKindId =:facilityBigKindId "
                    + " AND m.delFlg = 0 "
                    + " ORDER BY m.smallDisplayOrder"),
    @NamedQuery(name = "MCorpFacilityKind.findFreonInputStateList", 
            query = "SELECT m FROM MCorpFacilityKind m "
                    + "LEFT JOIN M.MFacilitySmallKind mfs "
                    + "LEFT JOIN mfs.MFacilityBigKind mfb "
                    + "WHERE M.id.corpId = :corpId "
                    + "AND M.id.facilityBigKindId = :facilityBigKindId "
                    + "AND M.id.facilitySmallKindId = :facilitySmallKindId")
})
public class MCorpFacilityKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MCorpFacilityKindPK id;

    @Column(name = "big_display_order", nullable = false)
    private Integer bigDisplayOrder;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "facility_small_kind_edit_name", nullable = false, length = 100)
    private String facilitySmallKindEditName;

    @Column(name = "small_display_order", nullable = false)
    private Integer smallDisplayOrder;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MCorp
    @ManyToOne
    @JoinColumn(name = "corp_id", nullable = false, insertable = false, updatable = false)
    private MCorp MCorp;

    //bi-directional many-to-one association to MFacilitySmallKind
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "facility_big_kind_id", referencedColumnName = "facility_big_kind_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "facility_small_kind_id", referencedColumnName = "facility_small_kind_id", nullable = false, insertable = false, updatable = false)
    })
    private MFacilitySmallKind MFacilitySmallKind;

    //bi-directional many-to-one association to MFacilityKind
    @OneToMany(mappedBy = "MCorpFacilityKind")
    private List<MFacilityKind> MFacilityKinds;

    public MCorpFacilityKind() {
    }

    public MCorpFacilityKindPK getId() {
        return this.id;
    }

    public void setId(MCorpFacilityKindPK id) {
        this.id = id;
    }

    public Integer getBigDisplayOrder() {
        return this.bigDisplayOrder;
    }

    public void setBigDisplayOrder(Integer bigDisplayOrder) {
        this.bigDisplayOrder = bigDisplayOrder;
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

    public String getFacilitySmallKindEditName() {
        return this.facilitySmallKindEditName;
    }

    public void setFacilitySmallKindEditName(String facilitySmallKindEditName) {
        this.facilitySmallKindEditName = facilitySmallKindEditName;
    }

    public Integer getSmallDisplayOrder() {
        return this.smallDisplayOrder;
    }

    public void setSmallDisplayOrder(Integer smallDisplayOrder) {
        this.smallDisplayOrder = smallDisplayOrder;
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

    public MCorp getMCorp() {
        return this.MCorp;
    }

    public void setMCorp(MCorp MCorp) {
        this.MCorp = MCorp;
    }

    public MFacilitySmallKind getMFacilitySmallKind() {
        return this.MFacilitySmallKind;
    }

    public void setMFacilitySmallKind(MFacilitySmallKind MFacilitySmallKind) {
        this.MFacilitySmallKind = MFacilitySmallKind;
    }

    public List<MFacilityKind> getMFacilityKinds() {
        return this.MFacilityKinds;
    }

    public void setMFacilityKinds(List<MFacilityKind> MFacilityKinds) {
        this.MFacilityKinds = MFacilityKinds;
    }

    public MFacilityKind addMFacilityKind(MFacilityKind MFacilityKind) {
        getMFacilityKinds().add(MFacilityKind);
        MFacilityKind.setMCorpFacilityKind(this);

        return MFacilityKind;
    }

    public MFacilityKind removeMFacilityKind(MFacilityKind MFacilityKind) {
        getMFacilityKinds().remove(MFacilityKind);
        MFacilityKind.setMCorpFacilityKind(null);

        return MFacilityKind;
    }

}
