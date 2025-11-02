package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the m_facility_kind database table.
 *
 */
@Entity
@Table(name = "m_facility_kind")
@NamedQueries({
    @NamedQuery(name = "MFacilityKind.findAll", query = "SELECT m FROM MFacilityKind m"),
    @NamedQuery(name = "MFacilityKind.findBuildingId",
            query = "SELECT m FROM MFacilityKind m"
            + " WHERE (:corpId is null or m.id.corpId =:corpId) "
            + " AND (:buildingId is null or m.id.buildingId =:buildingId) "
            + " AND (:facilityId is null or m.id.facilityId =:facilityId) "
            + " AND m.delFlg <> 1 "
    )
})

public class MFacilityKind implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MFacilityKindPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MCorpFacilityKind
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "facility_big_kind_id", referencedColumnName = "facility_big_kind_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "facility_small_kind_id", referencedColumnName = "facility_small_kind_id", nullable = false, insertable = false, updatable = false)
    })
    private MCorpFacilityKind MCorpFacilityKind;

    //bi-directional many-to-one association to MFacility
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "facility_id", referencedColumnName = "facility_id", nullable = false, insertable = false, updatable = false)
    })
    private MFacility MFacility;

    public MFacilityKind() {
    }

    public MFacilityKindPK getId() {
        return this.id;
    }

    public void setId(MFacilityKindPK id) {
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

    public MCorpFacilityKind getMCorpFacilityKind() {
        return this.MCorpFacilityKind;
    }

    public void setMCorpFacilityKind(MCorpFacilityKind MCorpFacilityKind) {
        this.MCorpFacilityKind = MCorpFacilityKind;
    }

    public MFacility getMFacility() {
        return this.MFacility;
    }

    public void setMFacility(MFacility MFacility) {
        this.MFacility = MFacility;
    }

}
