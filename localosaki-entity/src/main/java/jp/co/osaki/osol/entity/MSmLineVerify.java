package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_sm_line_verify database table.
 *
 */
@Entity
@Table(name="m_sm_line_verify")
@NamedQuery(name="MSmLineVerify.findAll", query="SELECT m FROM MSmLineVerify m")
public class MSmLineVerify implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MSmLineVerifyPK id;

	@Column(name="air_verify_type", nullable=false, length=6)
	private String airVerifyType;

	@Column(name="basic_rate_unit_price", precision=7, scale=2)
	private BigDecimal basicRateUnitPrice;

	@Column(name="commodity_charge_unit_price", precision=7, scale=2)
	private BigDecimal commodityChargeUnitPrice;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="proposal_amount_used_month_1", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth1;

	@Column(name="proposal_amount_used_month_10", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth10;

	@Column(name="proposal_amount_used_month_11", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth11;

	@Column(name="proposal_amount_used_month_12", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth12;

	@Column(name="proposal_amount_used_month_2", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth2;

	@Column(name="proposal_amount_used_month_3", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth3;

	@Column(name="proposal_amount_used_month_4", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth4;

	@Column(name="proposal_amount_used_month_5", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth5;

	@Column(name="proposal_amount_used_month_6", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth6;

	@Column(name="proposal_amount_used_month_7", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth7;

	@Column(name="proposal_amount_used_month_8", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth8;

	@Column(name="proposal_amount_used_month_9", precision=10, scale=1)
	private BigDecimal proposalAmountUsedMonth9;

        @Column(name="reduction_correction_rate", precision=3)
	private BigDecimal reductionCorrectionRate;

	@Column(name="reduction_lower_amount_month_1", precision=5)
	private BigDecimal reductionLowerAmountMonth1;

	@Column(name="reduction_lower_amount_month_10", precision=5)
	private BigDecimal reductionLowerAmountMonth10;

	@Column(name="reduction_lower_amount_month_11", precision=5)
	private BigDecimal reductionLowerAmountMonth11;

	@Column(name="reduction_lower_amount_month_12", precision=5)
	private BigDecimal reductionLowerAmountMonth12;

	@Column(name="reduction_lower_amount_month_2", precision=5)
	private BigDecimal reductionLowerAmountMonth2;

	@Column(name="reduction_lower_amount_month_3", precision=5)
	private BigDecimal reductionLowerAmountMonth3;

	@Column(name="reduction_lower_amount_month_4", precision=5)
	private BigDecimal reductionLowerAmountMonth4;

	@Column(name="reduction_lower_amount_month_5", precision=5)
	private BigDecimal reductionLowerAmountMonth5;

	@Column(name="reduction_lower_amount_month_6", precision=5)
	private BigDecimal reductionLowerAmountMonth6;

	@Column(name="reduction_lower_amount_month_7", precision=5)
	private BigDecimal reductionLowerAmountMonth7;

	@Column(name="reduction_lower_amount_month_8", precision=5)
	private BigDecimal reductionLowerAmountMonth8;

	@Column(name="reduction_lower_amount_month_9", precision=5)
	private BigDecimal reductionLowerAmountMonth9;

	@Column(name="reduction_lower_rate_month_1", precision=3)
	private BigDecimal reductionLowerRateMonth1;

	@Column(name="reduction_lower_rate_month_10", precision=3)
	private BigDecimal reductionLowerRateMonth10;

	@Column(name="reduction_lower_rate_month_11", precision=3)
	private BigDecimal reductionLowerRateMonth11;

	@Column(name="reduction_lower_rate_month_12", precision=3)
	private BigDecimal reductionLowerRateMonth12;

	@Column(name="reduction_lower_rate_month_2", precision=3)
	private BigDecimal reductionLowerRateMonth2;

	@Column(name="reduction_lower_rate_month_3", precision=3)
	private BigDecimal reductionLowerRateMonth3;

	@Column(name="reduction_lower_rate_month_4", precision=3)
	private BigDecimal reductionLowerRateMonth4;

	@Column(name="reduction_lower_rate_month_5", precision=3)
	private BigDecimal reductionLowerRateMonth5;

	@Column(name="reduction_lower_rate_month_6", precision=3)
	private BigDecimal reductionLowerRateMonth6;

	@Column(name="reduction_lower_rate_month_7", precision=3)
	private BigDecimal reductionLowerRateMonth7;

	@Column(name="reduction_lower_rate_month_8", precision=3)
	private BigDecimal reductionLowerRateMonth8;

	@Column(name="reduction_lower_rate_month_9", precision=3)
	private BigDecimal reductionLowerRateMonth9;

	@Column(name="reduction_rate_threshold", precision=3)
	private BigDecimal reductionRateThreshold;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmLineControlLoadVerify
	@OneToMany(mappedBy="MSmLineVerify")
	private List<MSmLineControlLoadVerify> MSmLineControlLoadVerifies;

	//bi-directional many-to-one association to MBuildingSm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingSm MBuildingSm;

	//bi-directional many-to-one association to MLine
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false, insertable=false, updatable=false)
		})
	private MLine MLine;

	public MSmLineVerify() {
	}

	public MSmLineVerifyPK getId() {
		return this.id;
	}

	public void setId(MSmLineVerifyPK id) {
		this.id = id;
	}

	public String getAirVerifyType() {
		return this.airVerifyType;
	}

	public void setAirVerifyType(String airVerifyType) {
		this.airVerifyType = airVerifyType;
	}

	public BigDecimal getBasicRateUnitPrice() {
		return this.basicRateUnitPrice;
	}

	public void setBasicRateUnitPrice(BigDecimal basicRateUnitPrice) {
		this.basicRateUnitPrice = basicRateUnitPrice;
	}

	public BigDecimal getCommodityChargeUnitPrice() {
		return this.commodityChargeUnitPrice;
	}

	public void setCommodityChargeUnitPrice(BigDecimal commodityChargeUnitPrice) {
		this.commodityChargeUnitPrice = commodityChargeUnitPrice;
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

	public BigDecimal getProposalAmountUsedMonth1() {
		return this.proposalAmountUsedMonth1;
	}

	public void setProposalAmountUsedMonth1(BigDecimal proposalAmountUsedMonth1) {
		this.proposalAmountUsedMonth1 = proposalAmountUsedMonth1;
	}

	public BigDecimal getProposalAmountUsedMonth10() {
		return this.proposalAmountUsedMonth10;
	}

	public void setProposalAmountUsedMonth10(BigDecimal proposalAmountUsedMonth10) {
		this.proposalAmountUsedMonth10 = proposalAmountUsedMonth10;
	}

	public BigDecimal getProposalAmountUsedMonth11() {
		return this.proposalAmountUsedMonth11;
	}

	public void setProposalAmountUsedMonth11(BigDecimal proposalAmountUsedMonth11) {
		this.proposalAmountUsedMonth11 = proposalAmountUsedMonth11;
	}

	public BigDecimal getProposalAmountUsedMonth12() {
		return this.proposalAmountUsedMonth12;
	}

	public void setProposalAmountUsedMonth12(BigDecimal proposalAmountUsedMonth12) {
		this.proposalAmountUsedMonth12 = proposalAmountUsedMonth12;
	}

	public BigDecimal getProposalAmountUsedMonth2() {
		return this.proposalAmountUsedMonth2;
	}

	public void setProposalAmountUsedMonth2(BigDecimal proposalAmountUsedMonth2) {
		this.proposalAmountUsedMonth2 = proposalAmountUsedMonth2;
	}

	public BigDecimal getProposalAmountUsedMonth3() {
		return this.proposalAmountUsedMonth3;
	}

	public void setProposalAmountUsedMonth3(BigDecimal proposalAmountUsedMonth3) {
		this.proposalAmountUsedMonth3 = proposalAmountUsedMonth3;
	}

	public BigDecimal getProposalAmountUsedMonth4() {
		return this.proposalAmountUsedMonth4;
	}

	public void setProposalAmountUsedMonth4(BigDecimal proposalAmountUsedMonth4) {
		this.proposalAmountUsedMonth4 = proposalAmountUsedMonth4;
	}

	public BigDecimal getProposalAmountUsedMonth5() {
		return this.proposalAmountUsedMonth5;
	}

	public void setProposalAmountUsedMonth5(BigDecimal proposalAmountUsedMonth5) {
		this.proposalAmountUsedMonth5 = proposalAmountUsedMonth5;
	}

	public BigDecimal getProposalAmountUsedMonth6() {
		return this.proposalAmountUsedMonth6;
	}

	public void setProposalAmountUsedMonth6(BigDecimal proposalAmountUsedMonth6) {
		this.proposalAmountUsedMonth6 = proposalAmountUsedMonth6;
	}

	public BigDecimal getProposalAmountUsedMonth7() {
		return this.proposalAmountUsedMonth7;
	}

	public void setProposalAmountUsedMonth7(BigDecimal proposalAmountUsedMonth7) {
		this.proposalAmountUsedMonth7 = proposalAmountUsedMonth7;
	}

	public BigDecimal getProposalAmountUsedMonth8() {
		return this.proposalAmountUsedMonth8;
	}

	public void setProposalAmountUsedMonth8(BigDecimal proposalAmountUsedMonth8) {
		this.proposalAmountUsedMonth8 = proposalAmountUsedMonth8;
	}

	public BigDecimal getProposalAmountUsedMonth9() {
		return this.proposalAmountUsedMonth9;
	}

	public void setProposalAmountUsedMonth9(BigDecimal proposalAmountUsedMonth9) {
		this.proposalAmountUsedMonth9 = proposalAmountUsedMonth9;
	}

        public BigDecimal getReductionCorrectionRate() {
		return this.reductionCorrectionRate;
	}

	public void setReductionCorrectionRate(BigDecimal reductionCorrectionRate) {
		this.reductionCorrectionRate = reductionCorrectionRate;
	}

	public BigDecimal getReductionLowerAmountMonth1() {
		return this.reductionLowerAmountMonth1;
	}

	public void setReductionLowerAmountMonth1(BigDecimal reductionLowerAmountMonth1) {
		this.reductionLowerAmountMonth1 = reductionLowerAmountMonth1;
	}

	public BigDecimal getReductionLowerAmountMonth10() {
		return this.reductionLowerAmountMonth10;
	}

	public void setReductionLowerAmountMonth10(BigDecimal reductionLowerAmountMonth10) {
		this.reductionLowerAmountMonth10 = reductionLowerAmountMonth10;
	}

	public BigDecimal getReductionLowerAmountMonth11() {
		return this.reductionLowerAmountMonth11;
	}

	public void setReductionLowerAmountMonth11(BigDecimal reductionLowerAmountMonth11) {
		this.reductionLowerAmountMonth11 = reductionLowerAmountMonth11;
	}

	public BigDecimal getReductionLowerAmountMonth12() {
		return this.reductionLowerAmountMonth12;
	}

	public void setReductionLowerAmountMonth12(BigDecimal reductionLowerAmountMonth12) {
		this.reductionLowerAmountMonth12 = reductionLowerAmountMonth12;
	}

	public BigDecimal getReductionLowerAmountMonth2() {
		return this.reductionLowerAmountMonth2;
	}

	public void setReductionLowerAmountMonth2(BigDecimal reductionLowerAmountMonth2) {
		this.reductionLowerAmountMonth2 = reductionLowerAmountMonth2;
	}

	public BigDecimal getReductionLowerAmountMonth3() {
		return this.reductionLowerAmountMonth3;
	}

	public void setReductionLowerAmountMonth3(BigDecimal reductionLowerAmountMonth3) {
		this.reductionLowerAmountMonth3 = reductionLowerAmountMonth3;
	}

	public BigDecimal getReductionLowerAmountMonth4() {
		return this.reductionLowerAmountMonth4;
	}

	public void setReductionLowerAmountMonth4(BigDecimal reductionLowerAmountMonth4) {
		this.reductionLowerAmountMonth4 = reductionLowerAmountMonth4;
	}

	public BigDecimal getReductionLowerAmountMonth5() {
		return this.reductionLowerAmountMonth5;
	}

	public void setReductionLowerAmountMonth5(BigDecimal reductionLowerAmountMonth5) {
		this.reductionLowerAmountMonth5 = reductionLowerAmountMonth5;
	}

	public BigDecimal getReductionLowerAmountMonth6() {
		return this.reductionLowerAmountMonth6;
	}

	public void setReductionLowerAmountMonth6(BigDecimal reductionLowerAmountMonth6) {
		this.reductionLowerAmountMonth6 = reductionLowerAmountMonth6;
	}

	public BigDecimal getReductionLowerAmountMonth7() {
		return this.reductionLowerAmountMonth7;
	}

	public void setReductionLowerAmountMonth7(BigDecimal reductionLowerAmountMonth7) {
		this.reductionLowerAmountMonth7 = reductionLowerAmountMonth7;
	}

	public BigDecimal getReductionLowerAmountMonth8() {
		return this.reductionLowerAmountMonth8;
	}

	public void setReductionLowerAmountMonth8(BigDecimal reductionLowerAmountMonth8) {
		this.reductionLowerAmountMonth8 = reductionLowerAmountMonth8;
	}

	public BigDecimal getReductionLowerAmountMonth9() {
		return this.reductionLowerAmountMonth9;
	}

	public void setReductionLowerAmountMonth9(BigDecimal reductionLowerAmountMonth9) {
		this.reductionLowerAmountMonth9 = reductionLowerAmountMonth9;
	}

	public BigDecimal getReductionLowerRateMonth1() {
		return this.reductionLowerRateMonth1;
	}

	public void setReductionLowerRateMonth1(BigDecimal reductionLowerRateMonth1) {
		this.reductionLowerRateMonth1 = reductionLowerRateMonth1;
	}

	public BigDecimal getReductionLowerRateMonth10() {
		return this.reductionLowerRateMonth10;
	}

	public void setReductionLowerRateMonth10(BigDecimal reductionLowerRateMonth10) {
		this.reductionLowerRateMonth10 = reductionLowerRateMonth10;
	}

	public BigDecimal getReductionLowerRateMonth11() {
		return this.reductionLowerRateMonth11;
	}

	public void setReductionLowerRateMonth11(BigDecimal reductionLowerRateMonth11) {
		this.reductionLowerRateMonth11 = reductionLowerRateMonth11;
	}

	public BigDecimal getReductionLowerRateMonth12() {
		return this.reductionLowerRateMonth12;
	}

	public void setReductionLowerRateMonth12(BigDecimal reductionLowerRateMonth12) {
		this.reductionLowerRateMonth12 = reductionLowerRateMonth12;
	}

	public BigDecimal getReductionLowerRateMonth2() {
		return this.reductionLowerRateMonth2;
	}

	public void setReductionLowerRateMonth2(BigDecimal reductionLowerRateMonth2) {
		this.reductionLowerRateMonth2 = reductionLowerRateMonth2;
	}

	public BigDecimal getReductionLowerRateMonth3() {
		return this.reductionLowerRateMonth3;
	}

	public void setReductionLowerRateMonth3(BigDecimal reductionLowerRateMonth3) {
		this.reductionLowerRateMonth3 = reductionLowerRateMonth3;
	}

	public BigDecimal getReductionLowerRateMonth4() {
		return this.reductionLowerRateMonth4;
	}

	public void setReductionLowerRateMonth4(BigDecimal reductionLowerRateMonth4) {
		this.reductionLowerRateMonth4 = reductionLowerRateMonth4;
	}

	public BigDecimal getReductionLowerRateMonth5() {
		return this.reductionLowerRateMonth5;
	}

	public void setReductionLowerRateMonth5(BigDecimal reductionLowerRateMonth5) {
		this.reductionLowerRateMonth5 = reductionLowerRateMonth5;
	}

	public BigDecimal getReductionLowerRateMonth6() {
		return this.reductionLowerRateMonth6;
	}

	public void setReductionLowerRateMonth6(BigDecimal reductionLowerRateMonth6) {
		this.reductionLowerRateMonth6 = reductionLowerRateMonth6;
	}

	public BigDecimal getReductionLowerRateMonth7() {
		return this.reductionLowerRateMonth7;
	}

	public void setReductionLowerRateMonth7(BigDecimal reductionLowerRateMonth7) {
		this.reductionLowerRateMonth7 = reductionLowerRateMonth7;
	}

	public BigDecimal getReductionLowerRateMonth8() {
		return this.reductionLowerRateMonth8;
	}

	public void setReductionLowerRateMonth8(BigDecimal reductionLowerRateMonth8) {
		this.reductionLowerRateMonth8 = reductionLowerRateMonth8;
	}

	public BigDecimal getReductionLowerRateMonth9() {
		return this.reductionLowerRateMonth9;
	}

	public void setReductionLowerRateMonth9(BigDecimal reductionLowerRateMonth9) {
		this.reductionLowerRateMonth9 = reductionLowerRateMonth9;
	}

	public BigDecimal getReductionRateThreshold() {
		return this.reductionRateThreshold;
	}

	public void setReductionRateThreshold(BigDecimal reductionRateThreshold) {
		this.reductionRateThreshold = reductionRateThreshold;
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

	public List<MSmLineControlLoadVerify> getMSmLineControlLoadVerifies() {
		return this.MSmLineControlLoadVerifies;
	}

	public void setMSmLineControlLoadVerifies(List<MSmLineControlLoadVerify> MSmLineControlLoadVerifies) {
		this.MSmLineControlLoadVerifies = MSmLineControlLoadVerifies;
	}

	public MSmLineControlLoadVerify addMSmLineControlLoadVerify(MSmLineControlLoadVerify MSmLineControlLoadVerify) {
		getMSmLineControlLoadVerifies().add(MSmLineControlLoadVerify);
		MSmLineControlLoadVerify.setMSmLineVerify(this);

		return MSmLineControlLoadVerify;
	}

	public MSmLineControlLoadVerify removeMSmLineControlLoadVerify(MSmLineControlLoadVerify MSmLineControlLoadVerify) {
		getMSmLineControlLoadVerifies().remove(MSmLineControlLoadVerify);
		MSmLineControlLoadVerify.setMSmLineVerify(null);

		return MSmLineControlLoadVerify;
	}

	public MBuildingSm getMBuildingSm() {
		return this.MBuildingSm;
	}

	public void setMBuildingSm(MBuildingSm MBuildingSm) {
		this.MBuildingSm = MBuildingSm;
	}

	public MLine getMLine() {
		return this.MLine;
	}

	public void setMLine(MLine MLine) {
		this.MLine = MLine;
	}

}