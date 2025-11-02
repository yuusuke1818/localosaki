package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.545+0900")
@StaticMetamodel(TDmMonthRepPoint.class)
public class TDmMonthRepPoint_ {
	public static volatile SingularAttribute<TDmMonthRepPoint, TDmMonthRepPointPK> id;
	public static volatile SingularAttribute<TDmMonthRepPoint, Timestamp> createDate;
	public static volatile SingularAttribute<TDmMonthRepPoint, Long> createUserId;
	public static volatile SingularAttribute<TDmMonthRepPoint, BigDecimal> pointAvg;
	public static volatile SingularAttribute<TDmMonthRepPoint, BigDecimal> pointMax;
	public static volatile SingularAttribute<TDmMonthRepPoint, BigDecimal> pointMin;
	public static volatile SingularAttribute<TDmMonthRepPoint, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmMonthRepPoint, Long> updateUserId;
	public static volatile SingularAttribute<TDmMonthRepPoint, Integer> version;
	public static volatile SingularAttribute<TDmMonthRepPoint, MBuildingSmPoint> MBuildingSmPoint;
	public static volatile SingularAttribute<TDmMonthRepPoint, TDmMonthRep> TDmMonthRep;
}
