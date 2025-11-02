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
 * The persistent class for the t_facility_bulk_input database table.
 * 
 */
@Entity
@Table(name="t_facility_bulk_input")
@NamedQuery(name="TFacilityBulkInput.findAll", query="SELECT t FROM TFacilityBulkInput t")
public class TFacilityBulkInput implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TFacilityBulkInputPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="process_end_datetime")
	private Timestamp processEndDatetime;

	@Column(name="process_result", nullable=false, length=6)
	private String processResult;

	@Column(name="process_result_file_name", length=510)
	private String processResultFileName;

	@Column(name="process_result_file_path", length=510)
	private String processResultFilePath;

	@Column(name="process_result_file_size", nullable=false)
	private Long processResultFileSize;

	@Column(name="process_start_datetime")
	private Timestamp processStartDatetime;

	@Column(name="process_status", nullable=false, length=6)
	private String processStatus;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(name="upload_file_name", length=510)
	private String uploadFileName;

	@Column(name="upload_file_path", length=510)
	private String uploadFilePath;

	@Column(name="upload_file_size", nullable=false)
	private Long uploadFileSize;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to MPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="input_person_corp_id", referencedColumnName="corp_id", nullable=false),
		@JoinColumn(name="input_person_id", referencedColumnName="person_id", nullable=false)
		})
	private MPerson MPerson;

	public TFacilityBulkInput() {
	}

	public TFacilityBulkInputPK getId() {
		return this.id;
	}

	public void setId(TFacilityBulkInputPK id) {
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

	public Timestamp getProcessEndDatetime() {
		return this.processEndDatetime;
	}

	public void setProcessEndDatetime(Timestamp processEndDatetime) {
		this.processEndDatetime = processEndDatetime;
	}

	public String getProcessResult() {
		return this.processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public String getProcessResultFileName() {
		return this.processResultFileName;
	}

	public void setProcessResultFileName(String processResultFileName) {
		this.processResultFileName = processResultFileName;
	}

	public String getProcessResultFilePath() {
		return this.processResultFilePath;
	}

	public void setProcessResultFilePath(String processResultFilePath) {
		this.processResultFilePath = processResultFilePath;
	}

	public Long getProcessResultFileSize() {
		return this.processResultFileSize;
	}

	public void setProcessResultFileSize(Long processResultFileSize) {
		this.processResultFileSize = processResultFileSize;
	}

	public Timestamp getProcessStartDatetime() {
		return this.processStartDatetime;
	}

	public void setProcessStartDatetime(Timestamp processStartDatetime) {
		this.processStartDatetime = processStartDatetime;
	}

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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

	public String getUploadFileName() {
		return this.uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadFilePath() {
		return this.uploadFilePath;
	}

	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

	public Long getUploadFileSize() {
		return this.uploadFileSize;
	}

	public void setUploadFileSize(Long uploadFileSize) {
		this.uploadFileSize = uploadFileSize;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

	public MPerson getMPerson() {
		return this.MPerson;
	}

	public void setMPerson(MPerson MPerson) {
		this.MPerson = MPerson;
	}

}