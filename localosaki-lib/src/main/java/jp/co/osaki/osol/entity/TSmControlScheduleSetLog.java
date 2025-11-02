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
 * The persistent class for the t_sm_control_schedule_set_log database table.
 * 
 */
@Entity
@Table(name="t_sm_control_schedule_set_log")
@NamedQuery(name="TSmControlScheduleSetLog.findAll", query="SELECT t FROM TSmControlScheduleSetLog t")
public class TSmControlScheduleSetLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmControlScheduleSetLogPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="friday_pattern_no", nullable=false, length=2)
	private String fridayPatternNo;

	@Column(name="holiday_pattern_no", nullable=false, length=2)
	private String holidayPatternNo;

	@Column(name="monday_pattern_no", nullable=false, length=2)
	private String mondayPatternNo;

	@Column(name="saturday_pattern_no", nullable=false, length=2)
	private String saturdayPatternNo;

	@Column(name="specific_date_1", nullable=false)
	private Integer specificDate1;

	@Column(name="specific_date_10", nullable=false)
	private Integer specificDate10;

	@Column(name="specific_date_2", nullable=false)
	private Integer specificDate2;

	@Column(name="specific_date_3", nullable=false)
	private Integer specificDate3;

	@Column(name="specific_date_4", nullable=false)
	private Integer specificDate4;

	@Column(name="specific_date_5", nullable=false)
	private Integer specificDate5;

	@Column(name="specific_date_6", nullable=false)
	private Integer specificDate6;

	@Column(name="specific_date_7", nullable=false)
	private Integer specificDate7;

	@Column(name="specific_date_8", nullable=false)
	private Integer specificDate8;

	@Column(name="specific_date_9", nullable=false)
	private Integer specificDate9;

	@Column(name="specific_date_pattern_no_1", nullable=false, length=2)
	private String specificDatePatternNo1;

	@Column(name="specific_date_pattern_no_10", nullable=false, length=2)
	private String specificDatePatternNo10;

	@Column(name="specific_date_pattern_no_2", nullable=false, length=2)
	private String specificDatePatternNo2;

	@Column(name="specific_date_pattern_no_3", nullable=false, length=2)
	private String specificDatePatternNo3;

	@Column(name="specific_date_pattern_no_4", nullable=false, length=2)
	private String specificDatePatternNo4;

	@Column(name="specific_date_pattern_no_5", nullable=false, length=2)
	private String specificDatePatternNo5;

	@Column(name="specific_date_pattern_no_6", nullable=false, length=2)
	private String specificDatePatternNo6;

	@Column(name="specific_date_pattern_no_7", nullable=false, length=2)
	private String specificDatePatternNo7;

	@Column(name="specific_date_pattern_no_8", nullable=false, length=2)
	private String specificDatePatternNo8;

	@Column(name="specific_date_pattern_no_9", nullable=false, length=2)
	private String specificDatePatternNo9;

	@Column(name="sunday_pattern_no", nullable=false, length=2)
	private String sundayPatternNo;

	@Column(name="thursday_pattern_no", nullable=false, length=2)
	private String thursdayPatternNo;

	@Column(name="tuesday_pattern_no", nullable=false, length=2)
	private String tuesdayPatternNo;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="wednesday_pattern_no", nullable=false, length=2)
	private String wednesdayPatternNo;

	//bi-directional many-to-one association to TSmControlScheduleLog
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sm_control_schedule_log_id", referencedColumnName="sm_control_schedule_log_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private TSmControlScheduleLog TSmControlScheduleLog;

	public TSmControlScheduleSetLog() {
	}

	public TSmControlScheduleSetLogPK getId() {
		return this.id;
	}

	public void setId(TSmControlScheduleSetLogPK id) {
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

	public String getFridayPatternNo() {
		return this.fridayPatternNo;
	}

	public void setFridayPatternNo(String fridayPatternNo) {
		this.fridayPatternNo = fridayPatternNo;
	}

	public String getHolidayPatternNo() {
		return this.holidayPatternNo;
	}

	public void setHolidayPatternNo(String holidayPatternNo) {
		this.holidayPatternNo = holidayPatternNo;
	}

	public String getMondayPatternNo() {
		return this.mondayPatternNo;
	}

	public void setMondayPatternNo(String mondayPatternNo) {
		this.mondayPatternNo = mondayPatternNo;
	}

	public String getSaturdayPatternNo() {
		return this.saturdayPatternNo;
	}

	public void setSaturdayPatternNo(String saturdayPatternNo) {
		this.saturdayPatternNo = saturdayPatternNo;
	}

	public Integer getSpecificDate1() {
		return this.specificDate1;
	}

	public void setSpecificDate1(Integer specificDate1) {
		this.specificDate1 = specificDate1;
	}

	public Integer getSpecificDate10() {
		return this.specificDate10;
	}

	public void setSpecificDate10(Integer specificDate10) {
		this.specificDate10 = specificDate10;
	}

	public Integer getSpecificDate2() {
		return this.specificDate2;
	}

	public void setSpecificDate2(Integer specificDate2) {
		this.specificDate2 = specificDate2;
	}

	public Integer getSpecificDate3() {
		return this.specificDate3;
	}

	public void setSpecificDate3(Integer specificDate3) {
		this.specificDate3 = specificDate3;
	}

	public Integer getSpecificDate4() {
		return this.specificDate4;
	}

	public void setSpecificDate4(Integer specificDate4) {
		this.specificDate4 = specificDate4;
	}

	public Integer getSpecificDate5() {
		return this.specificDate5;
	}

	public void setSpecificDate5(Integer specificDate5) {
		this.specificDate5 = specificDate5;
	}

	public Integer getSpecificDate6() {
		return this.specificDate6;
	}

	public void setSpecificDate6(Integer specificDate6) {
		this.specificDate6 = specificDate6;
	}

	public Integer getSpecificDate7() {
		return this.specificDate7;
	}

	public void setSpecificDate7(Integer specificDate7) {
		this.specificDate7 = specificDate7;
	}

	public Integer getSpecificDate8() {
		return this.specificDate8;
	}

	public void setSpecificDate8(Integer specificDate8) {
		this.specificDate8 = specificDate8;
	}

	public Integer getSpecificDate9() {
		return this.specificDate9;
	}

	public void setSpecificDate9(Integer specificDate9) {
		this.specificDate9 = specificDate9;
	}

	public String getSpecificDatePatternNo1() {
		return this.specificDatePatternNo1;
	}

	public void setSpecificDatePatternNo1(String specificDatePatternNo1) {
		this.specificDatePatternNo1 = specificDatePatternNo1;
	}

	public String getSpecificDatePatternNo10() {
		return this.specificDatePatternNo10;
	}

	public void setSpecificDatePatternNo10(String specificDatePatternNo10) {
		this.specificDatePatternNo10 = specificDatePatternNo10;
	}

	public String getSpecificDatePatternNo2() {
		return this.specificDatePatternNo2;
	}

	public void setSpecificDatePatternNo2(String specificDatePatternNo2) {
		this.specificDatePatternNo2 = specificDatePatternNo2;
	}

	public String getSpecificDatePatternNo3() {
		return this.specificDatePatternNo3;
	}

	public void setSpecificDatePatternNo3(String specificDatePatternNo3) {
		this.specificDatePatternNo3 = specificDatePatternNo3;
	}

	public String getSpecificDatePatternNo4() {
		return this.specificDatePatternNo4;
	}

	public void setSpecificDatePatternNo4(String specificDatePatternNo4) {
		this.specificDatePatternNo4 = specificDatePatternNo4;
	}

	public String getSpecificDatePatternNo5() {
		return this.specificDatePatternNo5;
	}

	public void setSpecificDatePatternNo5(String specificDatePatternNo5) {
		this.specificDatePatternNo5 = specificDatePatternNo5;
	}

	public String getSpecificDatePatternNo6() {
		return this.specificDatePatternNo6;
	}

	public void setSpecificDatePatternNo6(String specificDatePatternNo6) {
		this.specificDatePatternNo6 = specificDatePatternNo6;
	}

	public String getSpecificDatePatternNo7() {
		return this.specificDatePatternNo7;
	}

	public void setSpecificDatePatternNo7(String specificDatePatternNo7) {
		this.specificDatePatternNo7 = specificDatePatternNo7;
	}

	public String getSpecificDatePatternNo8() {
		return this.specificDatePatternNo8;
	}

	public void setSpecificDatePatternNo8(String specificDatePatternNo8) {
		this.specificDatePatternNo8 = specificDatePatternNo8;
	}

	public String getSpecificDatePatternNo9() {
		return this.specificDatePatternNo9;
	}

	public void setSpecificDatePatternNo9(String specificDatePatternNo9) {
		this.specificDatePatternNo9 = specificDatePatternNo9;
	}

	public String getSundayPatternNo() {
		return this.sundayPatternNo;
	}

	public void setSundayPatternNo(String sundayPatternNo) {
		this.sundayPatternNo = sundayPatternNo;
	}

	public String getThursdayPatternNo() {
		return this.thursdayPatternNo;
	}

	public void setThursdayPatternNo(String thursdayPatternNo) {
		this.thursdayPatternNo = thursdayPatternNo;
	}

	public String getTuesdayPatternNo() {
		return this.tuesdayPatternNo;
	}

	public void setTuesdayPatternNo(String tuesdayPatternNo) {
		this.tuesdayPatternNo = tuesdayPatternNo;
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

	public String getWednesdayPatternNo() {
		return this.wednesdayPatternNo;
	}

	public void setWednesdayPatternNo(String wednesdayPatternNo) {
		this.wednesdayPatternNo = wednesdayPatternNo;
	}

	public TSmControlScheduleLog getTSmControlScheduleLog() {
		return this.TSmControlScheduleLog;
	}

	public void setTSmControlScheduleLog(TSmControlScheduleLog TSmControlScheduleLog) {
		this.TSmControlScheduleLog = TSmControlScheduleLog;
	}

}