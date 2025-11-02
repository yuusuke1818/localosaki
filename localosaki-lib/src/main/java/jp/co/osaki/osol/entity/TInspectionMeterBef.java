package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_inspection_meter_bef database table.
 * 
 */
@Entity
@Table(name="t_inspection_meter_bef")
@NamedQuery(name="TInspectionMeterBef.findAll", query="SELECT t FROM TInspectionMeterBef t")
public class TInspectionMeterBef implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TInspectionMeterBefPK id;

	@Column(name="building_id")
	private Long buildingId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="latest_insp_val", precision=6, scale=1)
	private BigDecimal latestInspVal;

	@Column(name="meter_type")
	private Long meterType;

	@Column(precision=4)
	private BigDecimal multi;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public TInspectionMeterBef() {
	}

	public TInspectionMeterBefPK getId() {
		return this.id;
	}

	public void setId(TInspectionMeterBefPK id) {
		this.id = id;
	}

	public Long getBuildingId() {
		return this.buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
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

	public BigDecimal getLatestInspVal() {
		return this.latestInspVal;
	}

	public void setLatestInspVal(BigDecimal latestInspVal) {
		this.latestInspVal = latestInspVal;
	}

	public Long getMeterType() {
		return this.meterType;
	}

	public void setMeterType(Long meterType) {
		this.meterType = meterType;
	}

	public BigDecimal getMulti() {
		return this.multi;
	}

	public void setMulti(BigDecimal multi) {
		this.multi = multi;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
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

}