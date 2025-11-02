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
 * The persistent class for the t_plan_fulfillment_result database table.
 * 
 */
@Entity
@Table(name="t_plan_fulfillment_result")
@NamedQuery(name="TPlanFulfillmentResult.findAll", query="SELECT t FROM TPlanFulfillmentResult t")
public class TPlanFulfillmentResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TPlanFulfillmentResultPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="plan_fulfillment_date", nullable=false)
	private Timestamp planFulfillmentDate;

	@Column(name="plan_fulfillment_result", nullable=false, length=2147483647)
	private String planFulfillmentResult;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TBuildingPlanFulfillment
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="plan_fulfillment_id", referencedColumnName="plan_fulfillment_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuildingPlanFulfillment TBuildingPlanFulfillment;

	public TPlanFulfillmentResult() {
	}

	public TPlanFulfillmentResultPK getId() {
		return this.id;
	}

	public void setId(TPlanFulfillmentResultPK id) {
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

	public Timestamp getPlanFulfillmentDate() {
		return this.planFulfillmentDate;
	}

	public void setPlanFulfillmentDate(Timestamp planFulfillmentDate) {
		this.planFulfillmentDate = planFulfillmentDate;
	}

	public String getPlanFulfillmentResult() {
		return this.planFulfillmentResult;
	}

	public void setPlanFulfillmentResult(String planFulfillmentResult) {
		this.planFulfillmentResult = planFulfillmentResult;
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

	public TBuildingPlanFulfillment getTBuildingPlanFulfillment() {
		return this.TBuildingPlanFulfillment;
	}

	public void setTBuildingPlanFulfillment(TBuildingPlanFulfillment TBuildingPlanFulfillment) {
		this.TBuildingPlanFulfillment = TBuildingPlanFulfillment;
	}

}