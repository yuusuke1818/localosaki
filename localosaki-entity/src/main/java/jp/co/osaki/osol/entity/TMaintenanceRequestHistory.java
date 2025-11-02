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
 * The persistent class for the t_maintenance_request_history database table.
 * 
 */
@Entity
@Table(name="t_maintenance_request_history")
@NamedQuery(name="TMaintenanceRequestHistory.findAll", query="SELECT t FROM TMaintenanceRequestHistory t")
public class TMaintenanceRequestHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TMaintenanceRequestHistoryPK id;

	@Column(name="after_request_status", nullable=false, length=6)
	private String afterRequestStatus;

	@Column(name="before_request_status", nullable=false, length=6)
	private String beforeRequestStatus;

	@Column(nullable=false, length=2000)
	private String comment;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="regist_corp_id", nullable=false, length=50)
	private String registCorpId;

	@Column(name="regist_person_id", nullable=false, length=50)
	private String registPersonId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TMaintenanceDocument
	@OneToMany(mappedBy="TMaintenanceRequestHistory")
	private List<TMaintenanceDocument> TMaintenanceDocuments;

	//bi-directional many-to-one association to TMaintenanceFacility
	@OneToMany(mappedBy="TMaintenanceRequestHistory")
	private List<TMaintenanceFacility> TMaintenanceFacilities;

	//bi-directional many-to-one association to TMaintenanceRequest
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="maintenance_id", referencedColumnName="maintenance_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="maintenance_request_id", referencedColumnName="maintenance_request_id", nullable=false, insertable=false, updatable=false)
		})
	private TMaintenanceRequest TMaintenanceRequest;

	public TMaintenanceRequestHistory() {
	}

	public TMaintenanceRequestHistoryPK getId() {
		return this.id;
	}

	public void setId(TMaintenanceRequestHistoryPK id) {
		this.id = id;
	}

	public String getAfterRequestStatus() {
		return this.afterRequestStatus;
	}

	public void setAfterRequestStatus(String afterRequestStatus) {
		this.afterRequestStatus = afterRequestStatus;
	}

	public String getBeforeRequestStatus() {
		return this.beforeRequestStatus;
	}

	public void setBeforeRequestStatus(String beforeRequestStatus) {
		this.beforeRequestStatus = beforeRequestStatus;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getRegistCorpId() {
		return this.registCorpId;
	}

	public void setRegistCorpId(String registCorpId) {
		this.registCorpId = registCorpId;
	}

	public String getRegistPersonId() {
		return this.registPersonId;
	}

	public void setRegistPersonId(String registPersonId) {
		this.registPersonId = registPersonId;
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

	public List<TMaintenanceDocument> getTMaintenanceDocuments() {
		return this.TMaintenanceDocuments;
	}

	public void setTMaintenanceDocuments(List<TMaintenanceDocument> TMaintenanceDocuments) {
		this.TMaintenanceDocuments = TMaintenanceDocuments;
	}

	public TMaintenanceDocument addTMaintenanceDocument(TMaintenanceDocument TMaintenanceDocument) {
		getTMaintenanceDocuments().add(TMaintenanceDocument);
		TMaintenanceDocument.setTMaintenanceRequestHistory(this);

		return TMaintenanceDocument;
	}

	public TMaintenanceDocument removeTMaintenanceDocument(TMaintenanceDocument TMaintenanceDocument) {
		getTMaintenanceDocuments().remove(TMaintenanceDocument);
		TMaintenanceDocument.setTMaintenanceRequestHistory(null);

		return TMaintenanceDocument;
	}

	public List<TMaintenanceFacility> getTMaintenanceFacilities() {
		return this.TMaintenanceFacilities;
	}

	public void setTMaintenanceFacilities(List<TMaintenanceFacility> TMaintenanceFacilities) {
		this.TMaintenanceFacilities = TMaintenanceFacilities;
	}

	public TMaintenanceFacility addTMaintenanceFacility(TMaintenanceFacility TMaintenanceFacility) {
		getTMaintenanceFacilities().add(TMaintenanceFacility);
		TMaintenanceFacility.setTMaintenanceRequestHistory(this);

		return TMaintenanceFacility;
	}

	public TMaintenanceFacility removeTMaintenanceFacility(TMaintenanceFacility TMaintenanceFacility) {
		getTMaintenanceFacilities().remove(TMaintenanceFacility);
		TMaintenanceFacility.setTMaintenanceRequestHistory(null);

		return TMaintenanceFacility;
	}

	public TMaintenanceRequest getTMaintenanceRequest() {
		return this.TMaintenanceRequest;
	}

	public void setTMaintenanceRequest(TMaintenanceRequest TMaintenanceRequest) {
		this.TMaintenanceRequest = TMaintenanceRequest;
	}

}