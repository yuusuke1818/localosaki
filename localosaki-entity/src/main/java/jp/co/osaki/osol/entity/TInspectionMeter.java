package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_inspection_meter database table.
 * 
 */
@Entity
@Table(name="t_inspection_meter")
@NamedQuery(name="TInspectionMeter.findAll", query="SELECT t FROM TInspectionMeter t")
public class TInspectionMeter implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TInspectionMeterPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="insp_type", nullable=false, length=1)
	private String inspType;

	@Column(name="latest_insp_date")
	private Timestamp latestInspDate;

	@Column(name="latest_insp_val", length=7)
	private String latestInspVal;

	@Column(name="latest_use_val", precision=7)
	private BigDecimal latestUseVal;

	@Column(name="multiple_rate", precision=4)
	private BigDecimal multipleRate;

	@Column(name="prev_insp_date")
	private Timestamp prevInspDate;

	@Column(name="prev_insp_date2")
	private Timestamp prevInspDate2;

	@Column(name="prev_insp_val", length=7)
	private String prevInspVal;

	@Column(name="prev_insp_val2", length=7)
	private String prevInspVal2;

	@Column(name="prev_use_val", precision=7)
	private BigDecimal prevUseVal;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(name="use_per_rate", precision=5, scale=2)
	private BigDecimal usePerRate;

	@Version
	@Column(nullable=false)
	private Integer version;

	public TInspectionMeter() {
	}

	public TInspectionMeterPK getId() {
		return this.id;
	}

	public void setId(TInspectionMeterPK id) {
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

	public String getInspType() {
		return this.inspType;
	}

	public void setInspType(String inspType) {
		this.inspType = inspType;
	}

	public Timestamp getLatestInspDate() {
		return this.latestInspDate;
	}

	public void setLatestInspDate(Timestamp latestInspDate) {
		this.latestInspDate = latestInspDate;
	}

	public String getLatestInspVal() {
		return this.latestInspVal;
	}

	public void setLatestInspVal(String latestInspVal) {
		this.latestInspVal = latestInspVal;
	}

	public BigDecimal getLatestUseVal() {
		return this.latestUseVal;
	}

	public void setLatestUseVal(BigDecimal latestUseVal) {
		this.latestUseVal = latestUseVal;
	}

	public BigDecimal getMultipleRate() {
		return this.multipleRate;
	}

	public void setMultipleRate(BigDecimal multipleRate) {
		this.multipleRate = multipleRate;
	}

	public Timestamp getPrevInspDate() {
		return this.prevInspDate;
	}

	public void setPrevInspDate(Timestamp prevInspDate) {
		this.prevInspDate = prevInspDate;
	}

	public Timestamp getPrevInspDate2() {
		return this.prevInspDate2;
	}

	public void setPrevInspDate2(Timestamp prevInspDate2) {
		this.prevInspDate2 = prevInspDate2;
	}

	public String getPrevInspVal() {
		return this.prevInspVal;
	}

	public void setPrevInspVal(String prevInspVal) {
		this.prevInspVal = prevInspVal;
	}

	public String getPrevInspVal2() {
		return this.prevInspVal2;
	}

	public void setPrevInspVal2(String prevInspVal2) {
		this.prevInspVal2 = prevInspVal2;
	}

	public BigDecimal getPrevUseVal() {
		return this.prevUseVal;
	}

	public void setPrevUseVal(BigDecimal prevUseVal) {
		this.prevUseVal = prevUseVal;
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

	public BigDecimal getUsePerRate() {
		return this.usePerRate;
	}

	public void setUsePerRate(BigDecimal usePerRate) {
		this.usePerRate = usePerRate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}