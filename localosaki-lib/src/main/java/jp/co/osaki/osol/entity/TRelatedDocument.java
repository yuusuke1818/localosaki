package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_related_document database table.
 * 
 */
@Entity
@Table(name="t_related_document")
@NamedQuery(name="TRelatedDocument.findAll", query="SELECT t FROM TRelatedDocument t")
public class TRelatedDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TRelatedDocumentPK id;

	@Column(name="building_flg", nullable=false)
	private Integer buildingFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="facility_flg", nullable=false)
	private Integer facilityFlg;

	@Column(name="file_name", nullable=false, length=510)
	private String fileName;

	@Column(name="file_path", nullable=false, length=510)
	private String filePath;

	@Column(name="file_size", nullable=false)
	private Long fileSize;

	@Column(name="file_type", nullable=false, length=6)
	private String fileType;

	@Column(name="maintenance_flg", nullable=false)
	private Integer maintenanceFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TBuildingDocument
	@OneToMany(mappedBy="TRelatedDocument")
	private List<TBuildingDocument> TBuildingDocuments;

	//bi-directional many-to-one association to TFacilityDocument
	@OneToMany(mappedBy="TRelatedDocument")
	private List<TFacilityDocument> TFacilityDocuments;

	//bi-directional many-to-one association to TMaintenanceDocument
	@OneToMany(mappedBy="TRelatedDocument")
	private List<TMaintenanceDocument> TMaintenanceDocuments;

	public TRelatedDocument() {
	}

	public TRelatedDocumentPK getId() {
		return this.id;
	}

	public void setId(TRelatedDocumentPK id) {
		this.id = id;
	}

	public Integer getBuildingFlg() {
		return this.buildingFlg;
	}

	public void setBuildingFlg(Integer buildingFlg) {
		this.buildingFlg = buildingFlg;
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

	public Integer getFacilityFlg() {
		return this.facilityFlg;
	}

	public void setFacilityFlg(Integer facilityFlg) {
		this.facilityFlg = facilityFlg;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getMaintenanceFlg() {
		return this.maintenanceFlg;
	}

	public void setMaintenanceFlg(Integer maintenanceFlg) {
		this.maintenanceFlg = maintenanceFlg;
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

	public List<TBuildingDocument> getTBuildingDocuments() {
		return this.TBuildingDocuments;
	}

	public void setTBuildingDocuments(List<TBuildingDocument> TBuildingDocuments) {
		this.TBuildingDocuments = TBuildingDocuments;
	}

	public TBuildingDocument addTBuildingDocument(TBuildingDocument TBuildingDocument) {
		getTBuildingDocuments().add(TBuildingDocument);
		TBuildingDocument.setTRelatedDocument(this);

		return TBuildingDocument;
	}

	public TBuildingDocument removeTBuildingDocument(TBuildingDocument TBuildingDocument) {
		getTBuildingDocuments().remove(TBuildingDocument);
		TBuildingDocument.setTRelatedDocument(null);

		return TBuildingDocument;
	}

	public List<TFacilityDocument> getTFacilityDocuments() {
		return this.TFacilityDocuments;
	}

	public void setTFacilityDocuments(List<TFacilityDocument> TFacilityDocuments) {
		this.TFacilityDocuments = TFacilityDocuments;
	}

	public TFacilityDocument addTFacilityDocument(TFacilityDocument TFacilityDocument) {
		getTFacilityDocuments().add(TFacilityDocument);
		TFacilityDocument.setTRelatedDocument(this);

		return TFacilityDocument;
	}

	public TFacilityDocument removeTFacilityDocument(TFacilityDocument TFacilityDocument) {
		getTFacilityDocuments().remove(TFacilityDocument);
		TFacilityDocument.setTRelatedDocument(null);

		return TFacilityDocument;
	}

	public List<TMaintenanceDocument> getTMaintenanceDocuments() {
		return this.TMaintenanceDocuments;
	}

	public void setTMaintenanceDocuments(List<TMaintenanceDocument> TMaintenanceDocuments) {
		this.TMaintenanceDocuments = TMaintenanceDocuments;
	}

	public TMaintenanceDocument addTMaintenanceDocument(TMaintenanceDocument TMaintenanceDocument) {
		getTMaintenanceDocuments().add(TMaintenanceDocument);
		TMaintenanceDocument.setTRelatedDocument(this);

		return TMaintenanceDocument;
	}

	public TMaintenanceDocument removeTMaintenanceDocument(TMaintenanceDocument TMaintenanceDocument) {
		getTMaintenanceDocuments().remove(TMaintenanceDocument);
		TMaintenanceDocument.setTRelatedDocument(null);

		return TMaintenanceDocument;
	}

}