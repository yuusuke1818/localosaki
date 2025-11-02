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
 * The persistent class for the t_maintenance_document database table.
 * 
 */
@Entity
@Table(name="t_maintenance_document")
@NamedQuery(name="TMaintenanceDocument.findAll", query="SELECT t FROM TMaintenanceDocument t")
public class TMaintenanceDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TMaintenanceDocumentPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TMaintenanceRequestHistory
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="maintenance_history_id", referencedColumnName="maintenance_history_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="maintenance_id", referencedColumnName="maintenance_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="maintenance_request_id", referencedColumnName="maintenance_request_id", nullable=false, insertable=false, updatable=false)
		})
	private TMaintenanceRequestHistory TMaintenanceRequestHistory;

	//bi-directional many-to-one association to TRelatedDocument
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="document_id", referencedColumnName="document_id", nullable=false, insertable=false, updatable=false)
		})
	private TRelatedDocument TRelatedDocument;

	public TMaintenanceDocument() {
	}

	public TMaintenanceDocumentPK getId() {
		return this.id;
	}

	public void setId(TMaintenanceDocumentPK id) {
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

	public TMaintenanceRequestHistory getTMaintenanceRequestHistory() {
		return this.TMaintenanceRequestHistory;
	}

	public void setTMaintenanceRequestHistory(TMaintenanceRequestHistory TMaintenanceRequestHistory) {
		this.TMaintenanceRequestHistory = TMaintenanceRequestHistory;
	}

	public TRelatedDocument getTRelatedDocument() {
		return this.TRelatedDocument;
	}

	public void setTRelatedDocument(TRelatedDocument TRelatedDocument) {
		this.TRelatedDocument = TRelatedDocument;
	}

}