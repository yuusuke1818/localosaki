package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_alert_mail_setting database table.
 * 
 */
@Entity
@Table(name="m_alert_mail_setting")
@NamedQuery(name="MAlertMailSetting.findAll", query="SELECT m FROM MAlertMailSetting m")
public class MAlertMailSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="alert_cd", unique=true, nullable=false, length=30)
	private String alertCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(length=255)
	private String mail1;

	@Column(length=255)
	private String mail10;

	@Column(length=255)
	private String mail2;

	@Column(length=255)
	private String mail3;

	@Column(length=255)
	private String mail4;

	@Column(length=255)
	private String mail5;

	@Column(length=255)
	private String mail6;

	@Column(length=255)
	private String mail7;

	@Column(length=255)
	private String mail8;

	@Column(length=255)
	private String mail9;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public MAlertMailSetting() {
	}

	public String getAlertCd() {
		return this.alertCd;
	}

	public void setAlertCd(String alertCd) {
		this.alertCd = alertCd;
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

	public String getMail1() {
		return this.mail1;
	}

	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

	public String getMail10() {
		return this.mail10;
	}

	public void setMail10(String mail10) {
		this.mail10 = mail10;
	}

	public String getMail2() {
		return this.mail2;
	}

	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

	public String getMail3() {
		return this.mail3;
	}

	public void setMail3(String mail3) {
		this.mail3 = mail3;
	}

	public String getMail4() {
		return this.mail4;
	}

	public void setMail4(String mail4) {
		this.mail4 = mail4;
	}

	public String getMail5() {
		return this.mail5;
	}

	public void setMail5(String mail5) {
		this.mail5 = mail5;
	}

	public String getMail6() {
		return this.mail6;
	}

	public void setMail6(String mail6) {
		this.mail6 = mail6;
	}

	public String getMail7() {
		return this.mail7;
	}

	public void setMail7(String mail7) {
		this.mail7 = mail7;
	}

	public String getMail8() {
		return this.mail8;
	}

	public void setMail8(String mail8) {
		this.mail8 = mail8;
	}

	public String getMail9() {
		return this.mail9;
	}

	public void setMail9(String mail9) {
		this.mail9 = mail9;
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

}