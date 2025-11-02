package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the t_build_dev_meter_relation database table.
 * 
 */
@Entity
@Table(name="t_build_dev_meter_relation")
@NamedQuery(name="TBuildDevMeterRelation.findAll", query="SELECT t FROM TBuildDevMeterRelation t")
public class TBuildDevMeterRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildDevMeterRelationPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MMeter
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dev_id", referencedColumnName="dev_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="meter_mng_id", referencedColumnName="meter_mng_id", nullable=false, insertable=false, updatable=false)
		})
	private MMeter MMeter;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public TBuildDevMeterRelation() {
	}

	public TBuildDevMeterRelationPK getId() {
		return this.id;
	}

	public void setId(TBuildDevMeterRelationPK id) {
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

	public MMeter getMMeter() {
		return this.MMeter;
	}

	public void setMMeter(MMeter MMeter) {
		this.MMeter = MMeter;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}