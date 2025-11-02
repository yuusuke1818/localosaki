package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-09T19:25:44.690+0900")
@StaticMetamodel(TDmYearRep.class)
public class TDmYearRep_ {
	public static volatile SingularAttribute<TDmYearRep, TDmYearRepPK> id;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> closePreparationKwh;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> closeTimeKwh;
	public static volatile SingularAttribute<TDmYearRep, Timestamp> createDate;
	public static volatile SingularAttribute<TDmYearRep, Long> createUserId;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> maxKw;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> minKw;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> openPreparationKwh;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> openTimeKwh;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> outAirTempAvg;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> outAirTempMax;
	public static volatile SingularAttribute<TDmYearRep, BigDecimal> outAirTempMin;
	public static volatile SingularAttribute<TDmYearRep, Time> shopCloseTime;
	public static volatile SingularAttribute<TDmYearRep, Time> shopOpenTime;
	public static volatile SingularAttribute<TDmYearRep, String> sumDate;
	public static volatile SingularAttribute<TDmYearRep, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmYearRep, Long> updateUserId;
	public static volatile SingularAttribute<TDmYearRep, Integer> version;
	public static volatile SingularAttribute<TDmYearRep, Time> workEndTime;
	public static volatile SingularAttribute<TDmYearRep, Time> workStartTime;
	public static volatile SingularAttribute<TDmYearRep, MBuildingDm> MBuildingDm;
	public static volatile ListAttribute<TDmYearRep, TDmYearRepLine> TDmYearRepLines;
	public static volatile ListAttribute<TDmYearRep, TDmYearRepPoint> TDmYearRepPoints;
}
