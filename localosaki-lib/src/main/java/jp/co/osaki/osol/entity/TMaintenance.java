package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the t_maintenance database table.
 * 
 */
@Entity
@Table(name="t_maintenance")
@NamedQuery(name="TMaintenance.findAll", query="SELECT t FROM TMaintenance t")
public class TMaintenance implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TMaintenancePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="repeat_interval", nullable=false, length=6)
	private String repeatInterval;

	@Column(name="repeat_interval_edit_flg", nullable=false)
	private Integer repeatIntervalEditFlg;

	@Temporal(TemporalType.DATE)
	@Column(name="request_plan_end_date", nullable=false)
	private Date requestPlanEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name="request_plan_start_date", nullable=false)
	private Date requestPlanStartDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	//bi-directional many-to-one association to TMaintenanceRequest
	@OneToMany(mappedBy="TMaintenance")
	private List<TMaintenanceRequest> TMaintenanceRequests;

	public TMaintenance() {
	}

	public TMaintenancePK getId() {
		return this.id;
	}

	public void setId(TMaintenancePK id) {
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

	public String getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(String repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Integer getRepeatIntervalEditFlg() {
		return this.repeatIntervalEditFlg;
	}

	public void setRepeatIntervalEditFlg(Integer repeatIntervalEditFlg) {
		this.repeatIntervalEditFlg = repeatIntervalEditFlg;
	}

	public Date getRequestPlanEndDate() {
		return this.requestPlanEndDate;
	}

	public void setRequestPlanEndDate(Date requestPlanEndDate) {
		this.requestPlanEndDate = requestPlanEndDate;
	}

	public Date getRequestPlanStartDate() {
		return this.requestPlanStartDate;
	}

	public void setRequestPlanStartDate(Date requestPlanStartDate) {
		this.requestPlanStartDate = requestPlanStartDate;
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

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

	public List<TMaintenanceRequest> getTMaintenanceRequests() {
		return this.TMaintenanceRequests;
	}

	public void setTMaintenanceRequests(List<TMaintenanceRequest> TMaintenanceRequests) {
		this.TMaintenanceRequests = TMaintenanceRequests;
	}

	public TMaintenanceRequest addTMaintenanceRequest(TMaintenanceRequest TMaintenanceRequest) {
		getTMaintenanceRequests().add(TMaintenanceRequest);
		TMaintenanceRequest.setTMaintenance(this);

		return TMaintenanceRequest;
	}

	public TMaintenanceRequest removeTMaintenanceRequest(TMaintenanceRequest TMaintenanceRequest) {
		getTMaintenanceRequests().remove(TMaintenanceRequest);
		TMaintenanceRequest.setTMaintenance(null);

		return TMaintenanceRequest;
	}

}