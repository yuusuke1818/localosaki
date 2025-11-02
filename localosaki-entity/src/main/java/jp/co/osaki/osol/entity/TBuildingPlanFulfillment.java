package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_building_plan_fulfillment database table.
 * 
 */
@Entity
@Table(name="t_building_plan_fulfillment")
@NamedQuery(name="TBuildingPlanFulfillment.findAll", query="SELECT t FROM TBuildingPlanFulfillment t")
public class TBuildingPlanFulfillment implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildingPlanFulfillmentPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="prev_month_achieve_count")
	private Long prevMonthAchieveCount;

	@Column(name="prev_month_input_count")
	private Long prevMonthInputCount;

	@Column(name="prev_month_need_count")
	private Long prevMonthNeedCount;

	@Column(name="prev_month_target_input_count")
	private Long prevMonthTargetInputCount;

	@Column(name="this_month_achieve_count")
	private Long thisMonthAchieveCount;

	@Column(name="this_month_input_count")
	private Long thisMonthInputCount;

	@Column(name="this_month_need_count")
	private Long thisMonthNeedCount;

	@Column(name="this_month_target_input_count")
	private Long thisMonthTargetInputCount;

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

	//bi-directional many-to-one association to TPlanFulfillmentInfo
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="plan_fulfillment_id", referencedColumnName="plan_fulfillment_id", nullable=false, insertable=false, updatable=false)
		})
	private TPlanFulfillmentInfo TPlanFulfillmentInfo;

	//bi-directional many-to-one association to TPlanFulfillmentResult
	@OneToMany(mappedBy="TBuildingPlanFulfillment")
	private List<TPlanFulfillmentResult> TPlanFulfillmentResults;

	public TBuildingPlanFulfillment() {
	}

	public TBuildingPlanFulfillmentPK getId() {
		return this.id;
	}

	public void setId(TBuildingPlanFulfillmentPK id) {
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

	public Long getPrevMonthAchieveCount() {
		return this.prevMonthAchieveCount;
	}

	public void setPrevMonthAchieveCount(Long prevMonthAchieveCount) {
		this.prevMonthAchieveCount = prevMonthAchieveCount;
	}

	public Long getPrevMonthInputCount() {
		return this.prevMonthInputCount;
	}

	public void setPrevMonthInputCount(Long prevMonthInputCount) {
		this.prevMonthInputCount = prevMonthInputCount;
	}

	public Long getPrevMonthNeedCount() {
		return this.prevMonthNeedCount;
	}

	public void setPrevMonthNeedCount(Long prevMonthNeedCount) {
		this.prevMonthNeedCount = prevMonthNeedCount;
	}

	public Long getPrevMonthTargetInputCount() {
		return this.prevMonthTargetInputCount;
	}

	public void setPrevMonthTargetInputCount(Long prevMonthTargetInputCount) {
		this.prevMonthTargetInputCount = prevMonthTargetInputCount;
	}

	public Long getThisMonthAchieveCount() {
		return this.thisMonthAchieveCount;
	}

	public void setThisMonthAchieveCount(Long thisMonthAchieveCount) {
		this.thisMonthAchieveCount = thisMonthAchieveCount;
	}

	public Long getThisMonthInputCount() {
		return this.thisMonthInputCount;
	}

	public void setThisMonthInputCount(Long thisMonthInputCount) {
		this.thisMonthInputCount = thisMonthInputCount;
	}

	public Long getThisMonthNeedCount() {
		return this.thisMonthNeedCount;
	}

	public void setThisMonthNeedCount(Long thisMonthNeedCount) {
		this.thisMonthNeedCount = thisMonthNeedCount;
	}

	public Long getThisMonthTargetInputCount() {
		return this.thisMonthTargetInputCount;
	}

	public void setThisMonthTargetInputCount(Long thisMonthTargetInputCount) {
		this.thisMonthTargetInputCount = thisMonthTargetInputCount;
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

	public TPlanFulfillmentInfo getTPlanFulfillmentInfo() {
		return this.TPlanFulfillmentInfo;
	}

	public void setTPlanFulfillmentInfo(TPlanFulfillmentInfo TPlanFulfillmentInfo) {
		this.TPlanFulfillmentInfo = TPlanFulfillmentInfo;
	}

	public List<TPlanFulfillmentResult> getTPlanFulfillmentResults() {
		return this.TPlanFulfillmentResults;
	}

	public void setTPlanFulfillmentResults(List<TPlanFulfillmentResult> TPlanFulfillmentResults) {
		this.TPlanFulfillmentResults = TPlanFulfillmentResults;
	}

	public TPlanFulfillmentResult addTPlanFulfillmentResult(TPlanFulfillmentResult TPlanFulfillmentResult) {
		getTPlanFulfillmentResults().add(TPlanFulfillmentResult);
		TPlanFulfillmentResult.setTBuildingPlanFulfillment(this);

		return TPlanFulfillmentResult;
	}

	public TPlanFulfillmentResult removeTPlanFulfillmentResult(TPlanFulfillmentResult TPlanFulfillmentResult) {
		getTPlanFulfillmentResults().remove(TPlanFulfillmentResult);
		TPlanFulfillmentResult.setTBuildingPlanFulfillment(null);

		return TPlanFulfillmentResult;
	}

}