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
 * The persistent class for the t_building_subtype database table.
 *
 */
@Entity
@Table(name = "t_building_subtype")
@NamedQueries({
    @NamedQuery(name = "TBuildingSubtype.findAll", query = "SELECT t FROM TBuildingSubtype t"),
    @NamedQuery(name = "TBuildingSubtype.findBuildingId",
            query = "SELECT t FROM TBuildingSubtype t"
            + " WHERE (:corpId is null or t.id.corpId =:corpId) "
            + " AND (:buildingId is null or t.id.buildingId =:buildingId) "
            + " AND t.delFlg =:delFlg "
    )
})
public class TBuildingSubtype implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TBuildingSubtypePK id;

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

    //bi-directional many-to-one association to MSubtype
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "subtype_id", referencedColumnName = "subtype_id", nullable = false, insertable = false, updatable = false)
    })
    private MSubtype MSubtype;

    //bi-directional many-to-one association to TBuilding
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false)
    })
    private TBuilding TBuilding;

    public TBuildingSubtype() {
    }

    public TBuildingSubtypePK getId() {
        return this.id;
    }

    public void setId(TBuildingSubtypePK id) {
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

    public MSubtype getMSubtype() {
        return this.MSubtype;
    }

    public void setMSubtype(MSubtype MSubtype) {
        this.MSubtype = MSubtype;
    }

    public TBuilding getTBuilding() {
        return this.TBuilding;
    }

    public void setTBuilding(TBuilding TBuilding) {
        this.TBuilding = TBuilding;
    }

}
