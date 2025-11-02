package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the t_plan_fulfillment_info database table.
 * 
 */
@Entity
@Table(name="t_plan_fulfillment_info")
@NamedQuery(name="TPlanFulfillmentInfo.findAll", query="SELECT t FROM TPlanFulfillmentInfo t")
public class TPlanFulfillmentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TPlanFulfillmentInfoPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="plan_fulfillment_contents", nullable=false, length=2147483647)
	private String planFulfillmentContents;

	@Column(name="plan_fulfillment_date_type", nullable=false, length=6)
	private String planFulfillmentDateType;

	@Temporal(TemporalType.DATE)
	@Column(name="plan_fulfillment_end_date", nullable=false)
	private Date planFulfillmentEndDate;

	@Column(name="plan_fulfillment_name", nullable=false, length=50)
	private String planFulfillmentName;

	@Temporal(TemporalType.DATE)
	@Column(name="plan_fulfillment_start_date", nullable=false)
	private Date planFulfillmentStartDate;

	@Column(name="plan_fulfillment_target", nullable=false, length=6)
	private String planFulfillmentTarget;

	@Column(name="specify_friday", nullable=false)
	private Integer specifyFriday;

	@Column(name="specify_monday", nullable=false)
	private Integer specifyMonday;

	@Column(name="specify_month_1", nullable=false)
	private Integer specifyMonth1;

	@Column(name="specify_month_10", nullable=false)
	private Integer specifyMonth10;

	@Column(name="specify_month_11", nullable=false)
	private Integer specifyMonth11;

	@Column(name="specify_month_12", nullable=false)
	private Integer specifyMonth12;

	@Column(name="specify_month_2", nullable=false)
	private Integer specifyMonth2;

	@Column(name="specify_month_3", nullable=false)
	private Integer specifyMonth3;

	@Column(name="specify_month_4", nullable=false)
	private Integer specifyMonth4;

	@Column(name="specify_month_5", nullable=false)
	private Integer specifyMonth5;

	@Column(name="specify_month_6", nullable=false)
	private Integer specifyMonth6;

	@Column(name="specify_month_7", nullable=false)
	private Integer specifyMonth7;

	@Column(name="specify_month_8", nullable=false)
	private Integer specifyMonth8;

	@Column(name="specify_month_9", nullable=false)
	private Integer specifyMonth9;

	@Column(name="specify_saturday", nullable=false)
	private Integer specifySaturday;

	@Column(name="specify_sunday", nullable=false)
	private Integer specifySunday;

	@Column(name="specify_thursday", nullable=false)
	private Integer specifyThursday;

	@Column(name="specify_tuesday", nullable=false)
	private Integer specifyTuesday;

	@Column(name="specify_wednesday", nullable=false)
	private Integer specifyWednesday;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TBuildingPlanFulfillment
	@OneToMany(mappedBy="TPlanFulfillmentInfo")
	private List<TBuildingPlanFulfillment> TBuildingPlanFulfillments;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	public TPlanFulfillmentInfo() {
	}

	public TPlanFulfillmentInfoPK getId() {
		return this.id;
	}

	public void setId(TPlanFulfillmentInfoPK id) {
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

	public String getPlanFulfillmentContents() {
		return this.planFulfillmentContents;
	}

	public void setPlanFulfillmentContents(String planFulfillmentContents) {
		this.planFulfillmentContents = planFulfillmentContents;
	}

	public String getPlanFulfillmentDateType() {
		return this.planFulfillmentDateType;
	}

	public void setPlanFulfillmentDateType(String planFulfillmentDateType) {
		this.planFulfillmentDateType = planFulfillmentDateType;
	}

	public Date getPlanFulfillmentEndDate() {
		return this.planFulfillmentEndDate;
	}

	public void setPlanFulfillmentEndDate(Date planFulfillmentEndDate) {
		this.planFulfillmentEndDate = planFulfillmentEndDate;
	}

	public String getPlanFulfillmentName() {
		return this.planFulfillmentName;
	}

	public void setPlanFulfillmentName(String planFulfillmentName) {
		this.planFulfillmentName = planFulfillmentName;
	}

	public Date getPlanFulfillmentStartDate() {
		return this.planFulfillmentStartDate;
	}

	public void setPlanFulfillmentStartDate(Date planFulfillmentStartDate) {
		this.planFulfillmentStartDate = planFulfillmentStartDate;
	}

	public String getPlanFulfillmentTarget() {
		return this.planFulfillmentTarget;
	}

	public void setPlanFulfillmentTarget(String planFulfillmentTarget) {
		this.planFulfillmentTarget = planFulfillmentTarget;
	}

	public Integer getSpecifyFriday() {
		return this.specifyFriday;
	}

	public void setSpecifyFriday(Integer specifyFriday) {
		this.specifyFriday = specifyFriday;
	}

	public Integer getSpecifyMonday() {
		return this.specifyMonday;
	}

	public void setSpecifyMonday(Integer specifyMonday) {
		this.specifyMonday = specifyMonday;
	}

	public Integer getSpecifyMonth1() {
		return this.specifyMonth1;
	}

	public void setSpecifyMonth1(Integer specifyMonth1) {
		this.specifyMonth1 = specifyMonth1;
	}

	public Integer getSpecifyMonth10() {
		return this.specifyMonth10;
	}

	public void setSpecifyMonth10(Integer specifyMonth10) {
		this.specifyMonth10 = specifyMonth10;
	}

	public Integer getSpecifyMonth11() {
		return this.specifyMonth11;
	}

	public void setSpecifyMonth11(Integer specifyMonth11) {
		this.specifyMonth11 = specifyMonth11;
	}

	public Integer getSpecifyMonth12() {
		return this.specifyMonth12;
	}

	public void setSpecifyMonth12(Integer specifyMonth12) {
		this.specifyMonth12 = specifyMonth12;
	}

	public Integer getSpecifyMonth2() {
		return this.specifyMonth2;
	}

	public void setSpecifyMonth2(Integer specifyMonth2) {
		this.specifyMonth2 = specifyMonth2;
	}

	public Integer getSpecifyMonth3() {
		return this.specifyMonth3;
	}

	public void setSpecifyMonth3(Integer specifyMonth3) {
		this.specifyMonth3 = specifyMonth3;
	}

	public Integer getSpecifyMonth4() {
		return this.specifyMonth4;
	}

	public void setSpecifyMonth4(Integer specifyMonth4) {
		this.specifyMonth4 = specifyMonth4;
	}

	public Integer getSpecifyMonth5() {
		return this.specifyMonth5;
	}

	public void setSpecifyMonth5(Integer specifyMonth5) {
		this.specifyMonth5 = specifyMonth5;
	}

	public Integer getSpecifyMonth6() {
		return this.specifyMonth6;
	}

	public void setSpecifyMonth6(Integer specifyMonth6) {
		this.specifyMonth6 = specifyMonth6;
	}

	public Integer getSpecifyMonth7() {
		return this.specifyMonth7;
	}

	public void setSpecifyMonth7(Integer specifyMonth7) {
		this.specifyMonth7 = specifyMonth7;
	}

	public Integer getSpecifyMonth8() {
		return this.specifyMonth8;
	}

	public void setSpecifyMonth8(Integer specifyMonth8) {
		this.specifyMonth8 = specifyMonth8;
	}

	public Integer getSpecifyMonth9() {
		return this.specifyMonth9;
	}

	public void setSpecifyMonth9(Integer specifyMonth9) {
		this.specifyMonth9 = specifyMonth9;
	}

	public Integer getSpecifySaturday() {
		return this.specifySaturday;
	}

	public void setSpecifySaturday(Integer specifySaturday) {
		this.specifySaturday = specifySaturday;
	}

	public Integer getSpecifySunday() {
		return this.specifySunday;
	}

	public void setSpecifySunday(Integer specifySunday) {
		this.specifySunday = specifySunday;
	}

	public Integer getSpecifyThursday() {
		return this.specifyThursday;
	}

	public void setSpecifyThursday(Integer specifyThursday) {
		this.specifyThursday = specifyThursday;
	}

	public Integer getSpecifyTuesday() {
		return this.specifyTuesday;
	}

	public void setSpecifyTuesday(Integer specifyTuesday) {
		this.specifyTuesday = specifyTuesday;
	}

	public Integer getSpecifyWednesday() {
		return this.specifyWednesday;
	}

	public void setSpecifyWednesday(Integer specifyWednesday) {
		this.specifyWednesday = specifyWednesday;
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

	public List<TBuildingPlanFulfillment> getTBuildingPlanFulfillments() {
		return this.TBuildingPlanFulfillments;
	}

	public void setTBuildingPlanFulfillments(List<TBuildingPlanFulfillment> TBuildingPlanFulfillments) {
		this.TBuildingPlanFulfillments = TBuildingPlanFulfillments;
	}

	public TBuildingPlanFulfillment addTBuildingPlanFulfillment(TBuildingPlanFulfillment TBuildingPlanFulfillment) {
		getTBuildingPlanFulfillments().add(TBuildingPlanFulfillment);
		TBuildingPlanFulfillment.setTPlanFulfillmentInfo(this);

		return TBuildingPlanFulfillment;
	}

	public TBuildingPlanFulfillment removeTBuildingPlanFulfillment(TBuildingPlanFulfillment TBuildingPlanFulfillment) {
		getTBuildingPlanFulfillments().remove(TBuildingPlanFulfillment);
		TBuildingPlanFulfillment.setTPlanFulfillmentInfo(null);

		return TBuildingPlanFulfillment;
	}

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

}