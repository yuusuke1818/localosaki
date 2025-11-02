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
import javax.persistence.Version;


/**
 * The persistent class for the t_dm_day_load database table.
 * 
 */
@Entity
@Table(name="t_dm_day_load")
@NamedQuery(name="TDmDayLoad.findAll", query="SELECT t FROM TDmDayLoad t")
public class TDmDayLoad implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmDayLoadPK id;

	@Column(name="contact_out_status", nullable=false, length=1)
	private String contactOutStatus;

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

	//bi-directional many-to-one association to TDmDayMax
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="crnt_min", referencedColumnName="crnt_min", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="measurement_date", referencedColumnName="measurement_date", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private TDmDayMax TDmDayMax;

	public TDmDayLoad() {
	}

	public TDmDayLoadPK getId() {
		return this.id;
	}

	public void setId(TDmDayLoadPK id) {
		this.id = id;
	}

	public String getContactOutStatus() {
		return this.contactOutStatus;
	}

	public void setContactOutStatus(String contactOutStatus) {
		this.contactOutStatus = contactOutStatus;
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

	public TDmDayMax getTDmDayMax() {
		return this.TDmDayMax;
	}

	public void setTDmDayMax(TDmDayMax TDmDayMax) {
		this.TDmDayMax = TDmDayMax;
	}

}