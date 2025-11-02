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
 * The persistent class for the m_refrigerant database table.
 *
 */
@Entity
@Table(name = "m_refrigerant")
@NamedQueries({
    @NamedQuery(name = "MRefrigerant.findAll", query = "SELECT m FROM MRefrigerant m"),
    @NamedQuery(name = "MRefrigerant.findAllOrderByDisplayOrder", query = "SELECT m FROM MRefrigerant m ORDER BY m.displayOrder"),})
public class MRefrigerant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "refrigerant_id", unique = true, nullable = false)
    private Long refrigerantId;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "freon_name", length = 50)
    private String freonName;

    @Column(name = "refrigerant_no", nullable = false, length = 50)
    private String refrigerantNo;

    @Column(name = "refrigerant_type", nullable = false, length = 6)
    private String refrigerantType;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MFacility
    @OneToMany(mappedBy = "MRefrigerant")
    private List<MFacility> MFacilities;

    //bi-directional many-to-one association to MRefrigerantYearGwp
    @OneToMany(mappedBy = "MRefrigerant")
    private List<MRefrigerantYearGwp> MRefrigerantYearGwps;

    public MRefrigerant() {
    }

    public Long getRefrigerantId() {
        return this.refrigerantId;
    }

    public void setRefrigerantId(Long refrigerantId) {
        this.refrigerantId = refrigerantId;
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

    public String getFreonName() {
        return this.freonName;
    }

    public void setFreonName(String freonName) {
        this.freonName = freonName;
    }

    public String getRefrigerantNo() {
        return this.refrigerantNo;
    }

    public void setRefrigerantNo(String refrigerantNo) {
        this.refrigerantNo = refrigerantNo;
    }

    public String getRefrigerantType() {
        return this.refrigerantType;
    }

    public void setRefrigerantType(String refrigerantType) {
        this.refrigerantType = refrigerantType;
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

    public List<MFacility> getMFacilities() {
        return this.MFacilities;
    }

    public void setMFacilities(List<MFacility> MFacilities) {
        this.MFacilities = MFacilities;
    }

    public MFacility addMFacility(MFacility MFacility) {
        getMFacilities().add(MFacility);
        MFacility.setMRefrigerant(this);

        return MFacility;
    }

    public MFacility removeMFacility(MFacility MFacility) {
        getMFacilities().remove(MFacility);
        MFacility.setMRefrigerant(null);

        return MFacility;
    }

    public List<MRefrigerantYearGwp> getMRefrigerantYearGwps() {
        return this.MRefrigerantYearGwps;
    }

    public void setMRefrigerantYearGwps(List<MRefrigerantYearGwp> MRefrigerantYearGwps) {
        this.MRefrigerantYearGwps = MRefrigerantYearGwps;
    }

    public MRefrigerantYearGwp addMRefrigerantYearGwp(MRefrigerantYearGwp MRefrigerantYearGwp) {
        getMRefrigerantYearGwps().add(MRefrigerantYearGwp);
        MRefrigerantYearGwp.setMRefrigerant(this);

        return MRefrigerantYearGwp;
    }

    public MRefrigerantYearGwp removeMRefrigerantYearGwp(MRefrigerantYearGwp MRefrigerantYearGwp) {
        getMRefrigerantYearGwps().remove(MRefrigerantYearGwp);
        MRefrigerantYearGwp.setMRefrigerant(null);

        return MRefrigerantYearGwp;
    }

}
