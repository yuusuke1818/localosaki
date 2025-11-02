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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_building_sm database table.
 *
 */
@Entity
@Table(name="m_building_sm")
@NamedQuery(name="MBuildingSm.findAll", query="SELECT m FROM MBuildingSm m")
public class MBuildingSm implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MBuildingSmPK id;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    //bi-directional many-to-one association to MBuildingDm
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
        })
    private MBuildingDm MBuildingDm;

    //bi-directional many-to-one association to MSmPrm
    @ManyToOne
    @JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
    private MSmPrm MSmPrm;

        //bi-directional many-to-one association to MBuildingSmPoint
    @OneToMany(mappedBy="MBuildingSm")
    private List<MBuildingSmPoint> MBuildingSmPoints;

    //bi-directional many-to-one association to TDmDayMax
    @OneToMany(mappedBy="MBuildingSm")
    private List<TDmDayMax> TDmDayMaxs;

    //bi-directional many-to-one association to MSmLineVerify
    @OneToMany(mappedBy="MBuildingSm")
    private List<MSmLineVerify> MSmLineVerifies;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
    })
    private TBuilding TBuilding;

        public MBuildingSm() {
    }

    public MBuildingSmPK getId() {
        return this.id;
    }

    public void setId(MBuildingSmPK id) {
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

    public MBuildingDm getMBuildingDm() {
        return this.MBuildingDm;
    }

    public void setMBuildingDm(MBuildingDm MBuildingDm) {
        this.MBuildingDm = MBuildingDm;
    }

    public MSmPrm getMSmPrm() {
        return this.MSmPrm;
    }

    public void setMSmPrm(MSmPrm MSmPrm) {
        this.MSmPrm = MSmPrm;
    }

        public List<MBuildingSmPoint> getMBuildingSmPoints() {
        return this.MBuildingSmPoints;
    }

    public void setMBuildingSmPoints(List<MBuildingSmPoint> MBuildingSmPoints) {
        this.MBuildingSmPoints = MBuildingSmPoints;
    }

    public MBuildingSmPoint addMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
        getMBuildingSmPoints().add(MBuildingSmPoint);
        MBuildingSmPoint.setMBuildingSm(this);

        return MBuildingSmPoint;
    }

    public MBuildingSmPoint removeMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
        getMBuildingSmPoints().remove(MBuildingSmPoint);
        MBuildingSmPoint.setMBuildingSm(null);

        return MBuildingSmPoint;
    }

    public List<TDmDayMax> getTDmDayMaxs() {
        return this.TDmDayMaxs;
    }

    public void setTDmDayMaxs(List<TDmDayMax> TDmDayMaxs) {
        this.TDmDayMaxs = TDmDayMaxs;
    }

    public TDmDayMax addTDmDayMax(TDmDayMax TDmDayMax) {
        getTDmDayMaxs().add(TDmDayMax);
        TDmDayMax.setMBuildingSm(this);

        return TDmDayMax;
    }

    public TDmDayMax removeTDmDayMax(TDmDayMax TDmDayMax) {
        getTDmDayMaxs().remove(TDmDayMax);
        TDmDayMax.setMBuildingSm(null);

        return TDmDayMax;
    }

    public List<MSmLineVerify> getMSmLineVerifies() {
        return this.MSmLineVerifies;
    }

    public void setMSmLineVerifies(List<MSmLineVerify> MSmLineVerifies) {
        this.MSmLineVerifies = MSmLineVerifies;
    }

    public MSmLineVerify addMSmLineVerify(MSmLineVerify MSmLineVerify) {
        getMSmLineVerifies().add(MSmLineVerify);
        MSmLineVerify.setMBuildingSm(this);

        return MSmLineVerify;
    }

    public MSmLineVerify removeMSmLineVerify(MSmLineVerify MSmLineVerify) {
        getMSmLineVerifies().remove(MSmLineVerify);
        MSmLineVerify.setMBuildingSm(null);

        return MSmLineVerify;
    }

   public TBuilding getTBuilding() {
        return this.TBuilding;
    }

    public void setTBuilding(TBuilding TBuilding) {
        this.TBuilding = TBuilding;
    }
}