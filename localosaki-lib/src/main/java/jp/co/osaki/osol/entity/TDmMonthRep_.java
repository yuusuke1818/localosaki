package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.531+0900")
@StaticMetamodel(TDmMonthRep.class)
public class TDmMonthRep_ {
    public static volatile SingularAttribute<TDmMonthRep, TDmMonthRepPK> id;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> closePreparationKwh;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> closeTimeKwh;
    public static volatile SingularAttribute<TDmMonthRep, Timestamp> createDate;
    public static volatile SingularAttribute<TDmMonthRep, Long> createUserId;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> maxKw;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> minKw;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> openPreparationKwh;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> openTimeKwh;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> outAirTempAvg;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> outAirTempMax;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> outAirTempMin;
    public static volatile SingularAttribute<TDmMonthRep, Time> shopCloseTime;
    public static volatile SingularAttribute<TDmMonthRep, Time> shopOpenTime;
    public static volatile SingularAttribute<TDmMonthRep, String> sumDate;
    public static volatile SingularAttribute<TDmMonthRep, BigDecimal> targetKw;
    public static volatile SingularAttribute<TDmMonthRep, Timestamp> updateDate;
    public static volatile SingularAttribute<TDmMonthRep, Long> updateUserId;
    public static volatile SingularAttribute<TDmMonthRep, Integer> version;
    public static volatile SingularAttribute<TDmMonthRep, Time> workEndTime;
    public static volatile SingularAttribute<TDmMonthRep, Time> workStartTime;
    public static volatile SingularAttribute<TDmMonthRep, MBuildingDm> MBuildingDm;
    public static volatile ListAttribute<TDmMonthRep, TDmMonthRepLine> TDmMonthRepLines;
    public static volatile ListAttribute<TDmMonthRep, TDmMonthRepPoint> TDmMonthRepPoints;
}
