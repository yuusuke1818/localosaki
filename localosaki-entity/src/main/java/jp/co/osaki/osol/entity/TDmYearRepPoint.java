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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_dm_year_rep_point database table.
 * 
 */
@Entity
@Table(name="t_dm_year_rep_point")
@NamedQuery(name="TDmYearRepPoint.findAll", query="SELECT t FROM TDmYearRepPoint t")
public class TDmYearRepPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmYearRepPointPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="point_avg", precision=8, scale=1)
	private BigDecimal pointAvg;

	@Column(name="point_max", precision=8, scale=1)
	private BigDecimal pointMax;

	@Column(name="point_min", precision=8, scale=1)
	private BigDecimal pointMin;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingSmPoint
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="point_no", referencedColumnName="point_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingSmPoint MBuildingSmPoint;

	//bi-directional many-to-one association to TDmYearRep
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="month_no", referencedColumnName="month_no", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="summary_unit", referencedColumnName="summary_unit", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="year_no", referencedColumnName="year_no", nullable=false, insertable=false, updatable=false)
		})
	private TDmYearRep TDmYearRep;

	public TDmYearRepPoint() {
	}

	public TDmYearRepPointPK getId() {
		return this.id;
	}

	public void setId(TDmYearRepPointPK id) {
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

	public BigDecimal getPointAvg() {
		return this.pointAvg;
	}

	public void setPointAvg(BigDecimal pointAvg) {
		this.pointAvg = pointAvg;
	}

	public BigDecimal getPointMax() {
		return this.pointMax;
	}

	public void setPointMax(BigDecimal pointMax) {
		this.pointMax = pointMax;
	}

	public BigDecimal getPointMin() {
		return this.pointMin;
	}

	public void setPointMin(BigDecimal pointMin) {
		this.pointMin = pointMin;
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

	public MBuildingSmPoint getMBuildingSmPoint() {
		return this.MBuildingSmPoint;
	}

	public void setMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
		this.MBuildingSmPoint = MBuildingSmPoint;
	}

	public TDmYearRep getTDmYearRep() {
		return this.TDmYearRep;
	}

	public void setTDmYearRep(TDmYearRep TDmYearRep) {
		this.TDmYearRep = TDmYearRep;
	}

}