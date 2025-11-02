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
 * The persistent class for the t_inspection_meter_svr database table.
 * 
 */
@Entity
@Table(name="t_inspection_meter_svr")
@NamedQuery(name="TInspectionMeterSvr.findAll", query="SELECT t FROM TInspectionMeterSvr t")
public class TInspectionMeterSvr implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TInspectionMeterSvrPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="end_flg", precision=1)
	private BigDecimal endFlg;

	@Column(name="insp_type", nullable=false, length=1)
	private String inspType;

	@Column(name="latest_insp_date")
	private Timestamp latestInspDate;

	@Column(name="latest_insp_val", precision=6, scale=1)
	private BigDecimal latestInspVal;

	@Column(name="latest_use_val", precision=8, scale=1)
	private BigDecimal latestUseVal;

	@Column(name="multiple_rate", precision=4)
	private BigDecimal multipleRate;

	@Column(name="prev_insp_date")
	private Timestamp prevInspDate;

	@Column(name="prev_insp_date2")
	private Timestamp prevInspDate2;

	@Column(name="prev_insp_val", precision=6, scale=1)
	private BigDecimal prevInspVal;

	@Column(name="prev_insp_val2", precision=6, scale=1)
	private BigDecimal prevInspVal2;

	@Column(name="prev_use_val", precision=8, scale=1)
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

	@Column(name="wk_day_other_use", precision=6, scale=1)
	private BigDecimal wkDayOtherUse;

	@Column(name="wk_day_summer_use", precision=6, scale=1)
	private BigDecimal wkDaySummerUse;

	@Column(name="wk_family_use", precision=6, scale=1)
	private BigDecimal wkFamilyUse;

	@Column(name="wk_night_use", precision=6, scale=1)
	private BigDecimal wkNightUse;

	public TInspectionMeterSvr() {
	}

	public TInspectionMeterSvrPK getId() {
		return this.id;
	}

	public void setId(TInspectionMeterSvrPK id) {
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

	public BigDecimal getEndFlg() {
		return this.endFlg;
	}

	public void setEndFlg(BigDecimal endFlg) {
		this.endFlg = endFlg;
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

	public BigDecimal getLatestInspVal() {
		return this.latestInspVal;
	}

	public void setLatestInspVal(BigDecimal latestInspVal) {
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

	public BigDecimal getPrevInspVal() {
		return this.prevInspVal;
	}

	public void setPrevInspVal(BigDecimal prevInspVal) {
		this.prevInspVal = prevInspVal;
	}

	public BigDecimal getPrevInspVal2() {
		return this.prevInspVal2;
	}

	public void setPrevInspVal2(BigDecimal prevInspVal2) {
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

	public BigDecimal getWkDayOtherUse() {
		return this.wkDayOtherUse;
	}

	public void setWkDayOtherUse(BigDecimal wkDayOtherUse) {
		this.wkDayOtherUse = wkDayOtherUse;
	}

	public BigDecimal getWkDaySummerUse() {
		return this.wkDaySummerUse;
	}

	public void setWkDaySummerUse(BigDecimal wkDaySummerUse) {
		this.wkDaySummerUse = wkDaySummerUse;
	}

	public BigDecimal getWkFamilyUse() {
		return this.wkFamilyUse;
	}

	public void setWkFamilyUse(BigDecimal wkFamilyUse) {
		this.wkFamilyUse = wkFamilyUse;
	}

	public BigDecimal getWkNightUse() {
		return this.wkNightUse;
	}

	public void setWkNightUse(BigDecimal wkNightUse) {
		this.wkNightUse = wkNightUse;
	}

}