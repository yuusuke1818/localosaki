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
 * The persistent class for the m_meter_group database table.
 * 
 */
@Entity
@Table(name="m_meter_group")
@NamedQuery(name="MMeterGroup.findAll", query="SELECT m FROM MMeterGroup m")
public class MMeterGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMeterGroupPK id;

	@Column(name="calc_type", nullable=false, precision=2)
	private BigDecimal calcType;

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

	//bi-directional many-to-one association to MMeter
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="dev_id", referencedColumnName="dev_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="meter_mng_id", referencedColumnName="meter_mng_id", nullable=false, insertable=false, updatable=false)
		})
	private MMeter MMeter;

	//bi-directional many-to-one association to MMeterGroupName
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="meter_group_id", referencedColumnName="meter_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MMeterGroupName MMeterGroupName;

	public MMeterGroup() {
	}

	public MMeterGroupPK getId() {
		return this.id;
	}

	public void setId(MMeterGroupPK id) {
		this.id = id;
	}

	public BigDecimal getCalcType() {
		return this.calcType;
	}

	public void setCalcType(BigDecimal calcType) {
		this.calcType = calcType;
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

	public MMeterGroupName getMMeterGroupName() {
		return this.MMeterGroupName;
	}

	public void setMMeterGroupName(MMeterGroupName MMeterGroupName) {
		this.MMeterGroupName = MMeterGroupName;
	}

}