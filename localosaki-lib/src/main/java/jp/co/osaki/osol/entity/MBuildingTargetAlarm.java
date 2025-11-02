package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * The persistent class for the m_building_target_alarm database table.
 * 
 */
@Entity
@Table(name="m_building_target_alarm")
@NamedQuery(name="MBuildingTargetAlarm.findAll", query="SELECT m FROM MBuildingTargetAlarm m")
public class MBuildingTargetAlarm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MBuildingTargetAlarmPK id;

	@Column(name="alert_intermal_mail_address_1", length=100)
	private String alertIntermalMailAddress1;

	@Column(name="alert_intermal_mail_address_2", length=100)
	private String alertIntermalMailAddress2;

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

	@Column(name="detect_day_of_week", nullable=false)
	private Integer detectDayOfWeek;

	@Column(name="detect_time")
	private Time detectTime;

	@Column(name="mail_last_send_time")
	private Timestamp mailLastSendTime;

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

	@Column(name="target_kwh_monthly_over_date", nullable=false, precision=3)
	private BigDecimal targetKwhMonthlyOverDate;

	@Column(name="target_kwh_period_over_date", nullable=false, precision=3)
	private BigDecimal targetKwhPeriodOverDate;

	@Column(name="target_kwh_year_over_date", nullable=false, precision=3)
	private BigDecimal targetKwhYearOverDate;

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

	//bi-directional many-to-one association to MBuildingDm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingDm MBuildingDm;

	public MBuildingTargetAlarm() {
	}

	public MBuildingTargetAlarmPK getId() {
		return this.id;
	}

	public void setId(MBuildingTargetAlarmPK id) {
		this.id = id;
	}

	public String getAlertIntermalMailAddress1() {
		return this.alertIntermalMailAddress1;
	}

	public void setAlertIntermalMailAddress1(String alertIntermalMailAddress1) {
		this.alertIntermalMailAddress1 = alertIntermalMailAddress1;
	}

	public String getAlertIntermalMailAddress2() {
		return this.alertIntermalMailAddress2;
	}

	public void setAlertIntermalMailAddress2(String alertIntermalMailAddress2) {
		this.alertIntermalMailAddress2 = alertIntermalMailAddress2;
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

	public Integer getDetectDayOfWeek() {
		return this.detectDayOfWeek;
	}

	public void setDetectDayOfWeek(Integer detectDayOfWeek) {
		this.detectDayOfWeek = detectDayOfWeek;
	}

	public Time getDetectTime() {
		return this.detectTime;
	}

	public void setDetectTime(Time detectTime) {
		this.detectTime = detectTime;
	}

	public Timestamp getMailLastSendTime() {
		return this.mailLastSendTime;
	}

	public void setMailLastSendTime(Timestamp mailLastSendTime) {
		this.mailLastSendTime = mailLastSendTime;
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

	public BigDecimal getTargetKwhMonthlyOverDate() {
		return this.targetKwhMonthlyOverDate;
	}

	public void setTargetKwhMonthlyOverDate(BigDecimal targetKwhMonthlyOverDate) {
		this.targetKwhMonthlyOverDate = targetKwhMonthlyOverDate;
	}

	public BigDecimal getTargetKwhPeriodOverDate() {
		return this.targetKwhPeriodOverDate;
	}

	public void setTargetKwhPeriodOverDate(BigDecimal targetKwhPeriodOverDate) {
		this.targetKwhPeriodOverDate = targetKwhPeriodOverDate;
	}

	public BigDecimal getTargetKwhYearOverDate() {
		return this.targetKwhYearOverDate;
	}

	public void setTargetKwhYearOverDate(BigDecimal targetKwhYearOverDate) {
		this.targetKwhYearOverDate = targetKwhYearOverDate;
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

	public MBuildingDm getMBuildingDm() {
		return this.MBuildingDm;
	}

	public void setMBuildingDm(MBuildingDm MBuildingDm) {
		this.MBuildingDm = MBuildingDm;
	}

}