package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
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
 * The persistent class for the t_dm_year_rep database table.
 * 
 */
@Entity
@Table(name="t_dm_year_rep")
@NamedQuery(name="TDmYearRep.findAll", query="SELECT t FROM TDmYearRep t")
public class TDmYearRep implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDmYearRepPK id;

	@Column(name="close_preparation_kwh", precision=10, scale=1)
	private BigDecimal closePreparationKwh;

	@Column(name="close_time_kwh", precision=10, scale=1)
	private BigDecimal closeTimeKwh;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="max_kw", nullable=false, precision=10, scale=1)
	private BigDecimal maxKw;

	@Column(name="min_kw", nullable=false, precision=10, scale=1)
	private BigDecimal minKw;

	@Column(name="open_preparation_kwh", precision=10, scale=1)
	private BigDecimal openPreparationKwh;

	@Column(name="open_time_kwh", precision=10, scale=1)
	private BigDecimal openTimeKwh;

	@Column(name="out_air_temp_avg", precision=4, scale=1)
	private BigDecimal outAirTempAvg;

	@Column(name="out_air_temp_max", precision=4, scale=1)
	private BigDecimal outAirTempMax;

	@Column(name="out_air_temp_min", precision=4, scale=1)
	private BigDecimal outAirTempMin;

	@Column(name="shop_close_time")
	private Time shopCloseTime;

	@Column(name="shop_open_time")
	private Time shopOpenTime;

	@Column(name="sum_date", nullable=false, length=6)
	private String sumDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="work_end_time")
	private Time workEndTime;

	@Column(name="work_start_time")
	private Time workStartTime;

	//bi-directional many-to-one association to MBuildingDm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingDm MBuildingDm;

	//bi-directional many-to-one association to TDmYearRepLine
	@OneToMany(mappedBy="TDmYearRep")
	private List<TDmYearRepLine> TDmYearRepLines;

	//bi-directional many-to-one association to TDmYearRepPoint
	@OneToMany(mappedBy="TDmYearRep")
	private List<TDmYearRepPoint> TDmYearRepPoints;

	public TDmYearRep() {
	}

	public TDmYearRepPK getId() {
		return this.id;
	}

	public void setId(TDmYearRepPK id) {
		this.id = id;
	}

	public BigDecimal getClosePreparationKwh() {
		return this.closePreparationKwh;
	}

	public void setClosePreparationKwh(BigDecimal closePreparationKwh) {
		this.closePreparationKwh = closePreparationKwh;
	}

	public BigDecimal getCloseTimeKwh() {
		return this.closeTimeKwh;
	}

	public void setCloseTimeKwh(BigDecimal closeTimeKwh) {
		this.closeTimeKwh = closeTimeKwh;
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

	public BigDecimal getMaxKw() {
		return this.maxKw;
	}

	public void setMaxKw(BigDecimal maxKw) {
		this.maxKw = maxKw;
	}

	public BigDecimal getMinKw() {
		return this.minKw;
	}

	public void setMinKw(BigDecimal minKw) {
		this.minKw = minKw;
	}

	public BigDecimal getOpenPreparationKwh() {
		return this.openPreparationKwh;
	}

	public void setOpenPreparationKwh(BigDecimal openPreparationKwh) {
		this.openPreparationKwh = openPreparationKwh;
	}

	public BigDecimal getOpenTimeKwh() {
		return this.openTimeKwh;
	}

	public void setOpenTimeKwh(BigDecimal openTimeKwh) {
		this.openTimeKwh = openTimeKwh;
	}

	public BigDecimal getOutAirTempAvg() {
		return this.outAirTempAvg;
	}

	public void setOutAirTempAvg(BigDecimal outAirTempAvg) {
		this.outAirTempAvg = outAirTempAvg;
	}

	public BigDecimal getOutAirTempMax() {
		return this.outAirTempMax;
	}

	public void setOutAirTempMax(BigDecimal outAirTempMax) {
		this.outAirTempMax = outAirTempMax;
	}

	public BigDecimal getOutAirTempMin() {
		return this.outAirTempMin;
	}

	public void setOutAirTempMin(BigDecimal outAirTempMin) {
		this.outAirTempMin = outAirTempMin;
	}

	public Time getShopCloseTime() {
		return this.shopCloseTime;
	}

	public void setShopCloseTime(Time shopCloseTime) {
		this.shopCloseTime = shopCloseTime;
	}

	public Time getShopOpenTime() {
		return this.shopOpenTime;
	}

	public void setShopOpenTime(Time shopOpenTime) {
		this.shopOpenTime = shopOpenTime;
	}

	public String getSumDate() {
		return this.sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
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

	public Time getWorkEndTime() {
		return this.workEndTime;
	}

	public void setWorkEndTime(Time workEndTime) {
		this.workEndTime = workEndTime;
	}

	public Time getWorkStartTime() {
		return this.workStartTime;
	}

	public void setWorkStartTime(Time workStartTime) {
		this.workStartTime = workStartTime;
	}

	public MBuildingDm getMBuildingDm() {
		return this.MBuildingDm;
	}

	public void setMBuildingDm(MBuildingDm MBuildingDm) {
		this.MBuildingDm = MBuildingDm;
	}

	public List<TDmYearRepLine> getTDmYearRepLines() {
		return this.TDmYearRepLines;
	}

	public void setTDmYearRepLines(List<TDmYearRepLine> TDmYearRepLines) {
		this.TDmYearRepLines = TDmYearRepLines;
	}

	public TDmYearRepLine addTDmYearRepLine(TDmYearRepLine TDmYearRepLine) {
		getTDmYearRepLines().add(TDmYearRepLine);
		TDmYearRepLine.setTDmYearRep(this);

		return TDmYearRepLine;
	}

	public TDmYearRepLine removeTDmYearRepLine(TDmYearRepLine TDmYearRepLine) {
		getTDmYearRepLines().remove(TDmYearRepLine);
		TDmYearRepLine.setTDmYearRep(null);

		return TDmYearRepLine;
	}

	public List<TDmYearRepPoint> getTDmYearRepPoints() {
		return this.TDmYearRepPoints;
	}

	public void setTDmYearRepPoints(List<TDmYearRepPoint> TDmYearRepPoints) {
		this.TDmYearRepPoints = TDmYearRepPoints;
	}

	public TDmYearRepPoint addTDmYearRepPoint(TDmYearRepPoint TDmYearRepPoint) {
		getTDmYearRepPoints().add(TDmYearRepPoint);
		TDmYearRepPoint.setTDmYearRep(this);

		return TDmYearRepPoint;
	}

	public TDmYearRepPoint removeTDmYearRepPoint(TDmYearRepPoint TDmYearRepPoint) {
		getTDmYearRepPoints().remove(TDmYearRepPoint);
		TDmYearRepPoint.setTDmYearRep(null);

		return TDmYearRepPoint;
	}

}