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
 * The persistent class for the m_building_dm database table.
 *
 */
@Entity
@Table(name="m_building_dm")
@NamedQuery(name="MBuildingDm.findAll", query="SELECT m FROM MBuildingDm m")
public class MBuildingDm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MBuildingDmPK id;

	@Column(name="co2_day_and_night_type", length=6)
	private String co2DayAndNightType;

	@Column(name="co2_eng_id")
	private Long co2EngId;

	@Column(name="co2_eng_type_cd", length=3)
	private String co2EngTypeCd;

	@Column(name="contract_kw", precision=10, scale=1)
	private BigDecimal contractKw;

	@Column(name="coop_target_alarm_flg", nullable=false)
	private Integer coopTargetAlarmFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="day_commodity_charge_unit_price", precision=7, scale=2)
	private BigDecimal dayCommodityChargeUnitPrice;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="excellent_target_contrast_rate", precision=3)
	private BigDecimal excellentTargetContrastRate;

	@Column(name="facilities_mail_address_1", length=100)
	private String facilitiesMailAddress1;

	@Column(name="facilities_mail_address_2", length=100)
	private String facilitiesMailAddress2;

	@Column(name="facilities_mail_address_3", length=100)
	private String facilitiesMailAddress3;

	@Column(name="facilities_mail_address_4", length=100)
	private String facilitiesMailAddress4;

	@Column(name="facilities_mail_address_5", length=100)
	private String facilitiesMailAddress5;

	@Column(name="night_commodity_charge_unit_price", precision=7, scale=2)
	private BigDecimal nightCommodityChargeUnitPrice;

	@Column(name="out_air_temp_disp_flg", nullable=false)
	private Integer outAirTempDispFlg;

	@Column(name="shop_close_time")
	private Time shopCloseTime;

	@Column(name="shop_open_time")
	private Time shopOpenTime;

	@Column(name="standard_target_contrast_rate", precision=3)
	private BigDecimal standardTargetContrastRate;

	@Column(name="sum_date", nullable=false, length=6)
	private String sumDate;

	@Column(name="target_kw", precision=10, scale=1)
	private BigDecimal targetKw;

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

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="week_closing_day_of_week", nullable=false)
	private Integer weekClosingDayOfWeek;

	@Column(name="week_start_day", nullable=false, length=4)
	private String weekStartDay;

	@Column(name="work_end_time")
	private Time workEndTime;

	@Column(name="work_start_time")
	private Time workStartTime;

	//bi-directional many-to-one association to MAmedasObservatory
	@ManyToOne
	@JoinColumn(name="amedas_observatory_no")
	private MAmedasObservatory MAmedasObservatory;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	//bi-directional many-to-one association to MCorpDm
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorpDm MCorpDm;

        //bi-directional many-to-one association to MBuildingSm
	@OneToMany(mappedBy="MBuildingDm")
	private List<MBuildingSm> MBuildingSms;

	//bi-directional many-to-one association to MBuildingTargetAlarm
	@OneToMany(mappedBy="MBuildingDm")
	private List<MBuildingTargetAlarm> MBuildingTargetAlarms;

	//bi-directional many-to-one association to MGraph
	@OneToMany(mappedBy="MBuildingDm")
	private List<MGraph> MGraphs;

	//bi-directional many-to-one association to MLineTargetAlarm
	@OneToMany(mappedBy="MBuildingDm")
	private List<MLineTargetAlarm> MLineTargetAlarms;

	//bi-directional many-to-one association to MLineTimeStandard
	@OneToMany(mappedBy="MBuildingDm")
	private List<MLineTimeStandard> MLineTimeStandards;

	//bi-directional many-to-one association to TDmDayRep
	@OneToMany(mappedBy="MBuildingDm")
	private List<TDmDayRep> TDmDayReps;

	//bi-directional many-to-one association to TDmMonthRep
	@OneToMany(mappedBy="MBuildingDm")
	private List<TDmMonthRep> TDmMonthReps;

	//bi-directional many-to-one association to TDmWeekRep
	@OneToMany(mappedBy="MBuildingDm")
	private List<TDmWeekRep> TDmWeekReps;

	//bi-directional many-to-one association to TDmYearRep
	@OneToMany(mappedBy="MBuildingDm")
	private List<TDmYearRep> TDmYearReps;

    //bi-directional many-to-one association to MAggregateDm
    @OneToMany(mappedBy="MBuildingDm")
    private List<MAggregateDm> MAggregateDms;

    //bi-directional many-to-one association to MAggregateDmLine
    @OneToMany(mappedBy="MBuildingDm")
    private List<MAggregateDmLine> MAggregateDmLines;

    public MBuildingDm() {
	}

	public MBuildingDmPK getId() {
		return this.id;
	}

	public void setId(MBuildingDmPK id) {
		this.id = id;
	}

	public String getCo2DayAndNightType() {
		return this.co2DayAndNightType;
	}

	public void setCo2DayAndNightType(String co2DayAndNightType) {
		this.co2DayAndNightType = co2DayAndNightType;
	}

	public Long getCo2EngId() {
		return this.co2EngId;
	}

	public void setCo2EngId(Long co2EngId) {
		this.co2EngId = co2EngId;
	}

	public String getCo2EngTypeCd() {
		return this.co2EngTypeCd;
	}

	public void setCo2EngTypeCd(String co2EngTypeCd) {
		this.co2EngTypeCd = co2EngTypeCd;
	}

	public BigDecimal getContractKw() {
		return this.contractKw;
	}

	public void setContractKw(BigDecimal contractKw) {
		this.contractKw = contractKw;
	}

	public Integer getCoopTargetAlarmFlg() {
		return this.coopTargetAlarmFlg;
	}

	public void setCoopTargetAlarmFlg(Integer coopTargetAlarmFlg) {
		this.coopTargetAlarmFlg = coopTargetAlarmFlg;
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

	public BigDecimal getDayCommodityChargeUnitPrice() {
		return this.dayCommodityChargeUnitPrice;
	}

	public void setDayCommodityChargeUnitPrice(BigDecimal dayCommodityChargeUnitPrice) {
		this.dayCommodityChargeUnitPrice = dayCommodityChargeUnitPrice;
	}

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public BigDecimal getExcellentTargetContrastRate() {
		return this.excellentTargetContrastRate;
	}

	public void setExcellentTargetContrastRate(BigDecimal excellentTargetContrastRate) {
		this.excellentTargetContrastRate = excellentTargetContrastRate;
	}

	public String getFacilitiesMailAddress1() {
		return this.facilitiesMailAddress1;
	}

	public void setFacilitiesMailAddress1(String facilitiesMailAddress1) {
		this.facilitiesMailAddress1 = facilitiesMailAddress1;
	}

	public String getFacilitiesMailAddress2() {
		return this.facilitiesMailAddress2;
	}

	public void setFacilitiesMailAddress2(String facilitiesMailAddress2) {
		this.facilitiesMailAddress2 = facilitiesMailAddress2;
	}

	public String getFacilitiesMailAddress3() {
		return this.facilitiesMailAddress3;
	}

	public void setFacilitiesMailAddress3(String facilitiesMailAddress3) {
		this.facilitiesMailAddress3 = facilitiesMailAddress3;
	}

	public String getFacilitiesMailAddress4() {
		return this.facilitiesMailAddress4;
	}

	public void setFacilitiesMailAddress4(String facilitiesMailAddress4) {
		this.facilitiesMailAddress4 = facilitiesMailAddress4;
	}

	public String getFacilitiesMailAddress5() {
		return this.facilitiesMailAddress5;
	}

	public void setFacilitiesMailAddress5(String facilitiesMailAddress5) {
		this.facilitiesMailAddress5 = facilitiesMailAddress5;
	}

	public BigDecimal getNightCommodityChargeUnitPrice() {
		return this.nightCommodityChargeUnitPrice;
	}

	public void setNightCommodityChargeUnitPrice(BigDecimal nightCommodityChargeUnitPrice) {
		this.nightCommodityChargeUnitPrice = nightCommodityChargeUnitPrice;
	}

	public Integer getOutAirTempDispFlg() {
		return this.outAirTempDispFlg;
	}

	public void setOutAirTempDispFlg(Integer outAirTempDispFlg) {
		this.outAirTempDispFlg = outAirTempDispFlg;
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

	public BigDecimal getStandardTargetContrastRate() {
		return this.standardTargetContrastRate;
	}

	public void setStandardTargetContrastRate(BigDecimal standardTargetContrastRate) {
		this.standardTargetContrastRate = standardTargetContrastRate;
	}

	public String getSumDate() {
		return this.sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
	}

	public BigDecimal getTargetKw() {
		return this.targetKw;
	}

	public void setTargetKw(BigDecimal targetKw) {
		this.targetKw = targetKw;
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

	public Integer getWeekClosingDayOfWeek() {
		return this.weekClosingDayOfWeek;
	}

	public void setWeekClosingDayOfWeek(Integer weekClosingDayOfWeek) {
		this.weekClosingDayOfWeek = weekClosingDayOfWeek;
	}

	public String getWeekStartDay() {
		return this.weekStartDay;
	}

	public void setWeekStartDay(String weekStartDay) {
		this.weekStartDay = weekStartDay;
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

	public MAmedasObservatory getMAmedasObservatory() {
		return this.MAmedasObservatory;
	}

	public void setMAmedasObservatory(MAmedasObservatory MAmedasObservatory) {
		this.MAmedasObservatory = MAmedasObservatory;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

	public MCorpDm getMCorpDm() {
		return this.MCorpDm;
	}

	public void setMCorpDm(MCorpDm MCorpDm) {
		this.MCorpDm = MCorpDm;
	}

        public List<MBuildingSm> getMBuildingSms() {
		return this.MBuildingSms;
	}

	public void setMBuildingSms(List<MBuildingSm> MBuildingSms) {
		this.MBuildingSms = MBuildingSms;
	}

	public MBuildingSm addMBuildingSm(MBuildingSm MBuildingSm) {
		getMBuildingSms().add(MBuildingSm);
		MBuildingSm.setMBuildingDm(this);

		return MBuildingSm;
	}

	public MBuildingSm removeMBuildingSm(MBuildingSm MBuildingSm) {
		getMBuildingSms().remove(MBuildingSm);
		MBuildingSm.setMBuildingDm(null);

		return MBuildingSm;
	}

	public List<MBuildingTargetAlarm> getMBuildingTargetAlarms() {
		return this.MBuildingTargetAlarms;
	}

	public void setMBuildingTargetAlarms(List<MBuildingTargetAlarm> MBuildingTargetAlarms) {
		this.MBuildingTargetAlarms = MBuildingTargetAlarms;
	}

	public MBuildingTargetAlarm addMBuildingTargetAlarm(MBuildingTargetAlarm MBuildingTargetAlarm) {
		getMBuildingTargetAlarms().add(MBuildingTargetAlarm);
		MBuildingTargetAlarm.setMBuildingDm(this);

		return MBuildingTargetAlarm;
	}

	public MBuildingTargetAlarm removeMBuildingTargetAlarm(MBuildingTargetAlarm MBuildingTargetAlarm) {
		getMBuildingTargetAlarms().remove(MBuildingTargetAlarm);
		MBuildingTargetAlarm.setMBuildingDm(null);

		return MBuildingTargetAlarm;
	}

	public List<MGraph> getMGraphs() {
		return this.MGraphs;
	}

	public void setMGraphs(List<MGraph> MGraphs) {
		this.MGraphs = MGraphs;
	}

	public MGraph addMGraph(MGraph MGraph) {
		getMGraphs().add(MGraph);
		MGraph.setMBuildingDm(this);

		return MGraph;
	}

	public MGraph removeMGraph(MGraph MGraph) {
		getMGraphs().remove(MGraph);
		MGraph.setMBuildingDm(null);

		return MGraph;
	}

	public List<MLineTargetAlarm> getMLineTargetAlarms() {
		return this.MLineTargetAlarms;
	}

	public void setMLineTargetAlarms(List<MLineTargetAlarm> MLineTargetAlarms) {
		this.MLineTargetAlarms = MLineTargetAlarms;
	}

	public MLineTargetAlarm addMLineTargetAlarm(MLineTargetAlarm MLineTargetAlarm) {
		getMLineTargetAlarms().add(MLineTargetAlarm);
		MLineTargetAlarm.setMBuildingDm(this);

		return MLineTargetAlarm;
	}

	public MLineTargetAlarm removeMLineTargetAlarm(MLineTargetAlarm MLineTargetAlarm) {
		getMLineTargetAlarms().remove(MLineTargetAlarm);
		MLineTargetAlarm.setMBuildingDm(null);

		return MLineTargetAlarm;
	}

	public List<MLineTimeStandard> getMLineTimeStandards() {
		return this.MLineTimeStandards;
	}

	public void setMLineTimeStandards(List<MLineTimeStandard> MLineTimeStandards) {
		this.MLineTimeStandards = MLineTimeStandards;
	}

	public MLineTimeStandard addMLineTimeStandard(MLineTimeStandard MLineTimeStandard) {
		getMLineTimeStandards().add(MLineTimeStandard);
		MLineTimeStandard.setMBuildingDm(this);

		return MLineTimeStandard;
	}

	public MLineTimeStandard removeMLineTimeStandard(MLineTimeStandard MLineTimeStandard) {
		getMLineTimeStandards().remove(MLineTimeStandard);
		MLineTimeStandard.setMBuildingDm(null);

		return MLineTimeStandard;
	}

	public List<TDmDayRep> getTDmDayReps() {
		return this.TDmDayReps;
	}

	public void setTDmDayReps(List<TDmDayRep> TDmDayReps) {
		this.TDmDayReps = TDmDayReps;
	}

	public TDmDayRep addTDmDayRep(TDmDayRep TDmDayRep) {
		getTDmDayReps().add(TDmDayRep);
		TDmDayRep.setMBuildingDm(this);

		return TDmDayRep;
	}

	public TDmDayRep removeTDmDayRep(TDmDayRep TDmDayRep) {
		getTDmDayReps().remove(TDmDayRep);
		TDmDayRep.setMBuildingDm(null);

		return TDmDayRep;
	}

	public List<TDmMonthRep> getTDmMonthReps() {
		return this.TDmMonthReps;
	}

	public void setTDmMonthReps(List<TDmMonthRep> TDmMonthReps) {
		this.TDmMonthReps = TDmMonthReps;
	}

	public TDmMonthRep addTDmMonthRep(TDmMonthRep TDmMonthRep) {
		getTDmMonthReps().add(TDmMonthRep);
		TDmMonthRep.setMBuildingDm(this);

		return TDmMonthRep;
	}

	public TDmMonthRep removeTDmMonthRep(TDmMonthRep TDmMonthRep) {
		getTDmMonthReps().remove(TDmMonthRep);
		TDmMonthRep.setMBuildingDm(null);

		return TDmMonthRep;
	}

	public List<TDmWeekRep> getTDmWeekReps() {
		return this.TDmWeekReps;
	}

	public void setTDmWeekReps(List<TDmWeekRep> TDmWeekReps) {
		this.TDmWeekReps = TDmWeekReps;
	}

	public TDmWeekRep addTDmWeekRep(TDmWeekRep TDmWeekRep) {
		getTDmWeekReps().add(TDmWeekRep);
		TDmWeekRep.setMBuildingDm(this);

		return TDmWeekRep;
	}

	public TDmWeekRep removeTDmWeekRep(TDmWeekRep TDmWeekRep) {
		getTDmWeekReps().remove(TDmWeekRep);
		TDmWeekRep.setMBuildingDm(null);

		return TDmWeekRep;
	}

	public List<TDmYearRep> getTDmYearReps() {
		return this.TDmYearReps;
	}

	public void setTDmYearReps(List<TDmYearRep> TDmYearReps) {
		this.TDmYearReps = TDmYearReps;
	}

	public TDmYearRep addTDmYearRep(TDmYearRep TDmYearRep) {
		getTDmYearReps().add(TDmYearRep);
		TDmYearRep.setMBuildingDm(this);

		return TDmYearRep;
	}

	public TDmYearRep removeTDmYearRep(TDmYearRep TDmYearRep) {
		getTDmYearReps().remove(TDmYearRep);
		TDmYearRep.setMBuildingDm(null);

		return TDmYearRep;
	}

    public List<MAggregateDm> getMAggregateDms() {
        return this.MAggregateDms;
    }

    public void setMAggregateDms(List<MAggregateDm> MAggregateDms) {
        this.MAggregateDms = MAggregateDms;
    }

    public MAggregateDm addMAggregateDm(MAggregateDm MAggregateDm) {
        getMAggregateDms().add(MAggregateDm);
        MAggregateDm.setMBuildingDm(this);

        return MAggregateDm;
    }

    public MAggregateDm removeMAggregateDm(MAggregateDm MAggregateDm) {
        getMAggregateDms().remove(MAggregateDm);
        MAggregateDm.setMBuildingDm(null);

        return MAggregateDm;
    }

    public List<MAggregateDmLine> getMAggregateDmLines() {
        return this.MAggregateDmLines;
    }

    public void setMAggregateDmLines(List<MAggregateDmLine> MAggregateDmLines) {
        this.MAggregateDmLines = MAggregateDmLines;
    }

    public MAggregateDmLine addMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
        getMAggregateDmLines().add(MAggregateDmLine);
        MAggregateDmLine.setMBuildingDm(this);

        return MAggregateDmLine;
    }

    public MAggregateDmLine removeMAggregateDmLine(MAggregateDmLine MAggregateDmLine) {
        getMAggregateDmLines().remove(MAggregateDmLine);
        MAggregateDmLine.setMBuildingDm(null);

        return MAggregateDmLine;
    }

}