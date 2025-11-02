package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_building_document database table.
 * 
 */
@Entity
@Table(name = "t_building_document")
@NamedQueries({
    @NamedQuery(name = "TBuildingDocument.findAll", query = "SELECT t FROM TBuildingDocument t"),
    @NamedQuery(name = "TBuildingDocument.findAllByCondition",
            query = "SELECT t FROM TBuildingDocument t "
            + " WHERE t.id.corpId =:corpId AND t.id.buildingId =:buildingId"
            + " AND (:fileType is null or t.TRelatedDocument.fileType =:fileType) "
            + " AND (:fileName is null or t.TRelatedDocument.fileName like :fileName) "
            + " AND t.delFlg <> '1' AND t.TRelatedDocument.delFlg <> '1' "
            + " ORDER BY t.TRelatedDocument.createDate desc "),})

public class TBuildingDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildingDocumentPK id;

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

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	//bi-directional many-to-one association to TRelatedDocument
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="document_id", referencedColumnName="document_id", nullable=false, insertable=false, updatable=false)
		})
	private TRelatedDocument TRelatedDocument;

	public TBuildingDocument() {
	}

	public TBuildingDocumentPK getId() {
		return this.id;
	}

	public void setId(TBuildingDocumentPK id) {
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

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

	public TRelatedDocument getTRelatedDocument() {
		return this.TRelatedDocument;
	}

	public void setTRelatedDocument(TRelatedDocument TRelatedDocument) {
		this.TRelatedDocument = TRelatedDocument;
	}

}