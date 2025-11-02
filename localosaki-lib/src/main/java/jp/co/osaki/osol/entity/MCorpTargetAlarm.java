package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_corp_target_alarm database table.
 * 
 */
@Entity
@Table(name="m_corp_target_alarm")
@NamedQuery(name="MCorpTargetAlarm.findAll", query="SELECT m FROM MCorpTargetAlarm m")
public class MCorpTargetAlarm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="corp_id", unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="alert_mail_address_1", length=100)
	private String alertMailAddress1;

	@Column(name="alert_mail_address_10", length=100)
	private String alertMailAddress10;

	@Column(name="alert_mail_address_2", length=100)
	private String alertMailAddress2;

	@Column(name="alert_mail_address_3", length=100)
	private String alertMailAddress3;

	@Column(name="alert_mail_address_4", length=100)
	private String alertMailAddress4;

	@Column(name="alert_mail_address_5", length=100)
	private String alertMailAddress5;

	@Column(name="alert_mail_address_6", length=100)
	private String alertMailAddress6;

	@Column(name="alert_mail_address_7", length=100)
	private String alertMailAddress7;

	@Column(name="alert_mail_address_8", length=100)
	private String alertMailAddress8;

	@Column(name="alert_mail_address_9", length=100)
	private String alertMailAddress9;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="detect_time")
	private Time detectTime;

	@Column(name="mail_last_send_date")
	private Timestamp mailLastSendDate;

	@Column(name="mail_will_send_time")
	private Time mailWillSendTime;

	@Column(name="monthly_alarm_flg", nullable=false)
	private Integer monthlyAlarmFlg;

	@Column(name="monthly_alarm_lock_date", nullable=false, precision=2)
	private BigDecimal monthlyAlarmLockDate;

	@Column(name="period_alarm_flg", nullable=false)
	private Integer periodAlarmFlg;

	@Column(name="period_alarm_lock_date", nullable=false, precision=3)
	private BigDecimal periodAlarmLockDate;

	@Column(name="share_period", nullable=false, precision=1)
	private BigDecimal sharePeriod;

	@Column(name="target_kwh_month_1", precision=10, scale=1)
	private BigDecimal targetKwhMonth1;

	@Column(name="target_kwh_month_10", precision=10, scale=1)
	private BigDecimal targetKwhMonth10;

	@Column(name="target_kwh_month_11", precision=10, scale=1)
	private BigDecimal targetKwhMonth11;

	@Column(name="target_kwh_month_12", precision=10, scale=1)
	private BigDecimal targetKwhMonth12;

	@Column(name="target_kwh_month_2", precision=10, scale=1)
	private BigDecimal targetKwhMonth2;

	@Column(name="target_kwh_month_3", precision=10, scale=1)
	private BigDecimal targetKwhMonth3;

	@Column(name="target_kwh_month_4", precision=10, scale=1)
	private BigDecimal targetKwhMonth4;

	@Column(name="target_kwh_month_5", precision=10, scale=1)
	private BigDecimal targetKwhMonth5;

	@Column(name="target_kwh_month_6", precision=10, scale=1)
	private BigDecimal targetKwhMonth6;

	@Column(name="target_kwh_month_7", precision=10, scale=1)
	private BigDecimal targetKwhMonth7;

	@Column(name="target_kwh_month_8", precision=10, scale=1)
	private BigDecimal targetKwhMonth8;

	@Column(name="target_kwh_month_9", precision=10, scale=1)
	private BigDecimal targetKwhMonth9;

	@Column(name="target_kwh_monthly_over_rate", nullable=false, precision=3)
	private BigDecimal targetKwhMonthlyOverRate;

	@Column(name="target_kwh_period_over_rate", nullable=false, precision=3)
	private BigDecimal targetKwhPeriodOverRate;

	@Column(name="target_kwh_year_over_rate", nullable=false, precision=3)
	private BigDecimal targetKwhYearOverRate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="year_alarm_flg", nullable=false)
	private Integer yearAlarmFlg;

	@Column(name="year_alarm_lock_date", nullable=false, precision=3)
	private BigDecimal yearAlarmLockDate;

	//bi-directional one-to-one association to MCorpDm
	@OneToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorpDm MCorpDm;

	public MCorpTargetAlarm() {
	}

	public String getCorpId() {
		return this.corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getAlertMailAddress1() {
		return this.alertMailAddress1;
	}

	public void setAlertMailAddress1(String alertMailAddress1) {
		this.alertMailAddress1 = alertMailAddress1;
	}

	public String getAlertMailAddress10() {
		return this.alertMailAddress10;
	}

	public void setAlertMailAddress10(String alertMailAddress10) {
		this.alertMailAddress10 = alertMailAddress10;
	}

	public String getAlertMailAddress2() {
		return this.alertMailAddress2;
	}

	public void setAlertMailAddress2(String alertMailAddress2) {
		this.alertMailAddress2 = alertMailAddress2;
	}

	public String getAlertMailAddress3() {
		return this.alertMailAddress3;
	}

	public void setAlertMailAddress3(String alertMailAddress3) {
		this.alertMailAddress3 = alertMailAddress3;
	}

	public String getAlertMailAddress4() {
		return this.alertMailAddress4;
	}

	public void setAlertMailAddress4(String alertMailAddress4) {
		this.alertMailAddress4 = alertMailAddress4;
	}

	public String getAlertMailAddress5() {
		return this.alertMailAddress5;
	}

	public void setAlertMailAddress5(String alertMailAddress5) {
		this.alertMailAddress5 = alertMailAddress5;
	}

	public String getAlertMailAddress6() {
		return this.alertMailAddress6;
	}

	public void setAlertMailAddress6(String alertMailAddress6) {
		this.alertMailAddress6 = alertMailAddress6;
	}

	public String getAlertMailAddress7() {
		return this.alertMailAddress7;
	}

	public void setAlertMailAddress7(String alertMailAddress7) {
		this.alertMailAddress7 = alertMailAddress7;
	}

	public String getAlertMailAddress8() {
		return this.alertMailAddress8;
	}

	public void setAlertMailAddress8(String alertMailAddress8) {
		this.alertMailAddress8 = alertMailAddress8;
	}

	public String getAlertMailAddress9() {
		return this.alertMailAddress9;
	}

	public void setAlertMailAddress9(String alertMailAddress9) {
		this.alertMailAddress9 = alertMailAddress9;
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

	public Time getDetectTime() {
		return this.detectTime;
	}

	public void setDetectTime(Time detectTime) {
		this.detectTime = detectTime;
	}

	public Timestamp getMailLastSendDate() {
		return this.mailLastSendDate;
	}

	public void setMailLastSendDate(Timestamp mailLastSendDate) {
		this.mailLastSendDate = mailLastSendDate;
	}

	public Time getMailWillSendTime() {
		return this.mailWillSendTime;
	}

	public void setMailWillSendTime(Time mailWillSendTime) {
		this.mailWillSendTime = mailWillSendTime;
	}

	public Integer getMonthlyAlarmFlg() {
		return this.monthlyAlarmFlg;
	}

	public void setMonthlyAlarmFlg(Integer monthlyAlarmFlg) {
		this.monthlyAlarmFlg = monthlyAlarmFlg;
	}

	public BigDecimal getMonthlyAlarmLockDate() {
		return this.monthlyAlarmLockDate;
	}

	public void setMonthlyAlarmLockDate(BigDecimal monthlyAlarmLockDate) {
		this.monthlyAlarmLockDate = monthlyAlarmLockDate;
	}

	public Integer getPeriodAlarmFlg() {
		return this.periodAlarmFlg;
	}

	public void setPeriodAlarmFlg(Integer periodAlarmFlg) {
		this.periodAlarmFlg = periodAlarmFlg;
	}

	public BigDecimal getPeriodAlarmLockDate() {
		return this.periodAlarmLockDate;
	}

	public void setPeriodAlarmLockDate(BigDecimal periodAlarmLockDate) {
		this.periodAlarmLockDate = periodAlarmLockDate;
	}

	public BigDecimal getSharePeriod() {
		return this.sharePeriod;
	}

	public void setSharePeriod(BigDecimal sharePeriod) {
		this.sharePeriod = sharePeriod;
	}

	public BigDecimal getTargetKwhMonth1() {
		return this.targetKwhMonth1;
	}

	public void setTargetKwhMonth1(BigDecimal targetKwhMonth1) {
		this.targetKwhMonth1 = targetKwhMonth1;
	}

	public BigDecimal getTargetKwhMonth10() {
		return this.targetKwhMonth10;
	}

	public void setTargetKwhMonth10(BigDecimal targetKwhMonth10) {
		this.targetKwhMonth10 = targetKwhMonth10;
	}

	public BigDecimal getTargetKwhMonth11() {
		return this.targetKwhMonth11;
	}

	public void setTargetKwhMonth11(BigDecimal targetKwhMonth11) {
		this.targetKwhMonth11 = targetKwhMonth11;
	}

	public BigDecimal getTargetKwhMonth12() {
		return this.targetKwhMonth12;
	}

	public void setTargetKwhMonth12(BigDecimal targetKwhMonth12) {
		this.targetKwhMonth12 = targetKwhMonth12;
	}

	public BigDecimal getTargetKwhMonth2() {
		return this.targetKwhMonth2;
	}

	public void setTargetKwhMonth2(BigDecimal targetKwhMonth2) {
		this.targetKwhMonth2 = targetKwhMonth2;
	}

	public BigDecimal getTargetKwhMonth3() {
		return this.targetKwhMonth3;
	}

	public void setTargetKwhMonth3(BigDecimal targetKwhMonth3) {
		this.targetKwhMonth3 = targetKwhMonth3;
	}

	public BigDecimal getTargetKwhMonth4() {
		return this.targetKwhMonth4;
	}

	public void setTargetKwhMonth4(BigDecimal targetKwhMonth4) {
		this.targetKwhMonth4 = targetKwhMonth4;
	}

	public BigDecimal getTargetKwhMonth5() {
		return this.targetKwhMonth5;
	}

	public void setTargetKwhMonth5(BigDecimal targetKwhMonth5) {
		this.targetKwhMonth5 = targetKwhMonth5;
	}

	public BigDecimal getTargetKwhMonth6() {
		return this.targetKwhMonth6;
	}

	public void setTargetKwhMonth6(BigDecimal targetKwhMonth6) {
		this.targetKwhMonth6 = targetKwhMonth6;
	}

	public BigDecimal getTargetKwhMonth7() {
		return this.targetKwhMonth7;
	}

	public void setTargetKwhMonth7(BigDecimal targetKwhMonth7) {
		this.targetKwhMonth7 = targetKwhMonth7;
	}

	public BigDecimal getTargetKwhMonth8() {
		return this.targetKwhMonth8;
	}

	public void setTargetKwhMonth8(BigDecimal targetKwhMonth8) {
		this.targetKwhMonth8 = targetKwhMonth8;
	}

	public BigDecimal getTargetKwhMonth9() {
		return this.targetKwhMonth9;
	}

	public void setTargetKwhMonth9(BigDecimal targetKwhMonth9) {
		this.targetKwhMonth9 = targetKwhMonth9;
	}

	public BigDecimal getTargetKwhMonthlyOverRate() {
		return this.targetKwhMonthlyOverRate;
	}

	public void setTargetKwhMonthlyOverRate(BigDecimal targetKwhMonthlyOverRate) {
		this.targetKwhMonthlyOverRate = targetKwhMonthlyOverRate;
	}

	public BigDecimal getTargetKwhPeriodOverRate() {
		return this.targetKwhPeriodOverRate;
	}

	public void setTargetKwhPeriodOverRate(BigDecimal targetKwhPeriodOverRate) {
		this.targetKwhPeriodOverRate = targetKwhPeriodOverRate;
	}

	public BigDecimal getTargetKwhYearOverRate() {
		return this.targetKwhYearOverRate;
	}

	public void setTargetKwhYearOverRate(BigDecimal targetKwhYearOverRate) {
		this.targetKwhYearOverRate = targetKwhYearOverRate;
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

	public Integer getYearAlarmFlg() {
		return this.yearAlarmFlg;
	}

	public void setYearAlarmFlg(Integer yearAlarmFlg) {
		this.yearAlarmFlg = yearAlarmFlg;
	}

	public BigDecimal getYearAlarmLockDate() {
		return this.yearAlarmLockDate;
	}

	public void setYearAlarmLockDate(BigDecimal yearAlarmLockDate) {
		this.yearAlarmLockDate = yearAlarmLockDate;
	}

	public MCorpDm getMCorpDm() {
		return this.MCorpDm;
	}

	public void setMCorpDm(MCorpDm MCorpDm) {
		this.MCorpDm = MCorpDm;
	}

}