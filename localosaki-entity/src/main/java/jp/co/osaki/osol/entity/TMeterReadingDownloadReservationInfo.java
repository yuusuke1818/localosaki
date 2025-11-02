package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_meter_reading_download_reservation_info database table.
 *
 */
@Entity
@Table(name="t_meter_reading_download_reservation_info")
@NamedQuery(name="TMeterReadingDownloadReservationInfo.findAll", query="SELECT t FROM TMeterReadingDownloadReservationInfo t")
public class TMeterReadingDownloadReservationInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="reservation_id", unique=true, nullable=false)
	private Long reservationId;

	@Column(name="corp_id", unique=true, nullable=false)
	private String corpId;

	@Column(name="person_id", unique=true, nullable=false)
	private String personId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="output_file_name", length=200)
	private String outputFileName;

	@Column(name="output_file_path", length=300)
	private String outputFilePath;

	@Column(name="process_result", nullable=false, length=6)
	private String processResult;

	@Column(name="process_status", nullable=false, length=6)
	private String processStatus;

	@Column(name="reservation_date", nullable=false)
	private Timestamp reservationDate;

	@Column(name="search_condition", nullable=false, length=2147483647)
	private String searchCondition;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", insertable=false, updatable=false, nullable=false),
		@JoinColumn(name="person_id", referencedColumnName="person_id", insertable=false, updatable=false, nullable=false)
		})
	private MPerson MPerson;

	public TMeterReadingDownloadReservationInfo() {
	}

	public Long getReservationId() {
		return this.reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getOutputFileName() {
		return this.outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public String getOutputFilePath() {
		return this.outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public String getProcessResult() {
		return this.processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public Timestamp getReservationDate() {
		return this.reservationDate;
	}

	public void setReservationDate(Timestamp reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getSearchCondition() {
		return this.searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
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

	public MPerson getMPerson() {
		return this.MPerson;
	}

	public void setMPerson(MPerson MPerson) {
		this.MPerson = MPerson;
	}

}