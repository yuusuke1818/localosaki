package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.552+0900")
@StaticMetamodel(TDmWeekRep.class)
public class TDmWeekRep_ {
	public static volatile SingularAttribute<TDmWeekRep, TDmWeekRepPK> id;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> outAirTempAvg;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> closePreparationKwh;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> closeTimeKwh;
	public static volatile SingularAttribute<TDmWeekRep, Timestamp> createDate;
	public static volatile SingularAttribute<TDmWeekRep, Long> createUserId;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> maxKw;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> outAirTempMax;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> minKw;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> outAirTempMin;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> openPreparationKwh;
	public static volatile SingularAttribute<TDmWeekRep, BigDecimal> openTimeKwh;
	public static volatile SingularAttribute<TDmWeekRep, Time> shopCloseTime;
	public static volatile SingularAttribute<TDmWeekRep, Time> shopOpenTime;
	public static volatile SingularAttribute<TDmWeekRep, String> sumDate;
	public static volatile SingularAttribute<TDmWeekRep, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmWeekRep, Long> updateUserId;
	public static volatile SingularAttribute<TDmWeekRep, Integer> version;
	public static volatile SingularAttribute<TDmWeekRep, Time> workEndTime;
	public static volatile SingularAttribute<TDmWeekRep, Time> workStartTime;
	public static volatile SingularAttribute<TDmWeekRep, MBuildingDm> MBuildingDm;
	public static volatile ListAttribute<TDmWeekRep, TDmWeekRepLine> TDmWeekRepLines;
	public static volatile ListAttribute<TDmWeekRep, TDmWeekRepPoint> TDmWeekRepPoints;
}
