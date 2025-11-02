package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-29T14:00:21.438+0900")
@StaticMetamodel(MBuildingDm.class)
public class MBuildingDm_ {
	public static volatile SingularAttribute<MBuildingDm, MBuildingDmPK> id;
	public static volatile SingularAttribute<MBuildingDm, String> co2DayAndNightType;
	public static volatile SingularAttribute<MBuildingDm, Long> co2EngId;
	public static volatile SingularAttribute<MBuildingDm, String> co2EngTypeCd;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> contractKw;
	public static volatile SingularAttribute<MBuildingDm, Integer> coopTargetAlarmFlg;
	public static volatile SingularAttribute<MBuildingDm, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingDm, Long> createUserId;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> dayCommodityChargeUnitPrice;
	public static volatile SingularAttribute<MBuildingDm, Integer> delFlg;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> excellentTargetContrastRate;
	public static volatile SingularAttribute<MBuildingDm, String> facilitiesMailAddress1;
	public static volatile SingularAttribute<MBuildingDm, String> facilitiesMailAddress2;
	public static volatile SingularAttribute<MBuildingDm, String> facilitiesMailAddress3;
	public static volatile SingularAttribute<MBuildingDm, String> facilitiesMailAddress4;
	public static volatile SingularAttribute<MBuildingDm, String> facilitiesMailAddress5;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> nightCommodityChargeUnitPrice;
	public static volatile SingularAttribute<MBuildingDm, Integer> outAirTempDispFlg;
	public static volatile SingularAttribute<MBuildingDm, Time> shopCloseTime;
	public static volatile SingularAttribute<MBuildingDm, Time> shopOpenTime;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> standardTargetContrastRate;
	public static volatile SingularAttribute<MBuildingDm, String> sumDate;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKw;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth1;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth10;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth11;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth12;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth2;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth3;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth4;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth5;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth6;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth7;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth8;
	public static volatile SingularAttribute<MBuildingDm, BigDecimal> targetKwhMonth9;
	public static volatile SingularAttribute<MBuildingDm, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingDm, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingDm, Integer> version;
	public static volatile SingularAttribute<MBuildingDm, Integer> weekClosingDayOfWeek;
	public static volatile SingularAttribute<MBuildingDm, String> weekStartDay;
	public static volatile SingularAttribute<MBuildingDm, Time> workEndTime;
	public static volatile SingularAttribute<MBuildingDm, Time> workStartTime;
	public static volatile SingularAttribute<MBuildingDm, MAmedasObservatory> MAmedasObservatory;
	public static volatile SingularAttribute<MBuildingDm, TBuilding> TBuilding;
	public static volatile SingularAttribute<MBuildingDm, MCorpDm> MCorpDm;
	public static volatile ListAttribute<MBuildingDm, MBuildingSm> MBuildingSms;
	public static volatile ListAttribute<MBuildingDm, MBuildingTargetAlarm> MBuildingTargetAlarms;
	public static volatile ListAttribute<MBuildingDm, MGraph> MGraphs;
	public static volatile ListAttribute<MBuildingDm, MLineTargetAlarm> MLineTargetAlarms;
	public static volatile ListAttribute<MBuildingDm, MLineTimeStandard> MLineTimeStandards;
	public static volatile ListAttribute<MBuildingDm, TDmDayRep> TDmDayReps;
	public static volatile ListAttribute<MBuildingDm, TDmMonthRep> TDmMonthReps;
	public static volatile ListAttribute<MBuildingDm, TDmWeekRep> TDmWeekReps;
	public static volatile ListAttribute<MBuildingDm, TDmYearRep> TDmYearReps;
    public static volatile ListAttribute<MBuildingDm, MAggregateDm> MAggregateDms;
    public static volatile ListAttribute<MBuildingDm, MAggregateDmLine> MAggregateDmLines;
}
