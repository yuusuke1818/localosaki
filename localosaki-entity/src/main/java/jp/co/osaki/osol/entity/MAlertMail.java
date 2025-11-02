package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_alert_mail database table.
 * 
 */
@Entity
@Table(name="m_alert_mail")
@NamedQuery(name="MAlertMail.findAll", query="SELECT m FROM MAlertMail m")
public class MAlertMail implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MAlertMailPK id;

	@Column(name="alert_command_err", length=1)
	private String alertCommandErr;

	@Column(name="alert_concent_err", length=1)
	private String alertConcentErr;

	@Column(name="alert_dmv_err", length=1)
	private String alertDmvErr;

	@Column(name="alert_exam", length=1)
	private String alertExam;

	@Column(name="alert_loadsurvey_err", length=1)
	private String alertLoadsurveyErr;

	@Column(name="alert_manual_insp", length=1)
	private String alertManualInsp;

	@Column(name="alert_meter_err", length=1)
	private String alertMeterErr;

	@Column(name="alert_term_err", length=1)
	private String alertTermErr;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="disabled_flg", length=1)
	private String disabledFlg;

	@Column(length=80)
	private String email;

	@Column(length=120)
	private String memo;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MDevPrm
	@ManyToOne
	@JoinColumn(name="dev_id", nullable=false, insertable=false, updatable=false)
	private MDevPrm MDevPrm;

	public MAlertMail() {
	}

	public MAlertMailPK getId() {
		return this.id;
	}

	public void setId(MAlertMailPK id) {
		this.id = id;
	}

	public String getAlertCommandErr() {
		return this.alertCommandErr;
	}

	public void setAlertCommandErr(String alertCommandErr) {
		this.alertCommandErr = alertCommandErr;
	}

	public String getAlertConcentErr() {
		return this.alertConcentErr;
	}

	public void setAlertConcentErr(String alertConcentErr) {
		this.alertConcentErr = alertConcentErr;
	}

	public String getAlertDmvErr() {
		return this.alertDmvErr;
	}

	public void setAlertDmvErr(String alertDmvErr) {
		this.alertDmvErr = alertDmvErr;
	}

	public String getAlertExam() {
		return this.alertExam;
	}

	public void setAlertExam(String alertExam) {
		this.alertExam = alertExam;
	}

	public String getAlertLoadsurveyErr() {
		return this.alertLoadsurveyErr;
	}

	public void setAlertLoadsurveyErr(String alertLoadsurveyErr) {
		this.alertLoadsurveyErr = alertLoadsurveyErr;
	}

	public String getAlertManualInsp() {
		return this.alertManualInsp;
	}

	public void setAlertManualInsp(String alertManualInsp) {
		this.alertManualInsp = alertManualInsp;
	}

	public String getAlertMeterErr() {
		return this.alertMeterErr;
	}

	public void setAlertMeterErr(String alertMeterErr) {
		this.alertMeterErr = alertMeterErr;
	}

	public String getAlertTermErr() {
		return this.alertTermErr;
	}

	public void setAlertTermErr(String alertTermErr) {
		this.alertTermErr = alertTermErr;
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

	public String getDisabledFlg() {
		return this.disabledFlg;
	}

	public void setDisabledFlg(String disabledFlg) {
		this.disabledFlg = disabledFlg;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
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

	public MDevPrm getMDevPrm() {
		return this.MDevPrm;
	}

	public void setMDevPrm(MDevPrm MDevPrm) {
		this.MDevPrm = MDevPrm;
	}

}