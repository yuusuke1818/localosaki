package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-07-20T15:59:50.929+0900")
@StaticMetamodel(TDmYearRepPoint.class)
public class TDmYearRepPoint_ {
	public static volatile SingularAttribute<TDmYearRepPoint, TDmYearRepPointPK> id;
	public static volatile SingularAttribute<TDmYearRepPoint, Timestamp> createDate;
	public static volatile SingularAttribute<TDmYearRepPoint, Long> createUserId;
	public static volatile SingularAttribute<TDmYearRepPoint, BigDecimal> pointAvg;
	public static volatile SingularAttribute<TDmYearRepPoint, BigDecimal> pointMax;
	public static volatile SingularAttribute<TDmYearRepPoint, BigDecimal> pointMin;
	public static volatile SingularAttribute<TDmYearRepPoint, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmYearRepPoint, Long> updateUserId;
	public static volatile SingularAttribute<TDmYearRepPoint, Integer> version;
	public static volatile SingularAttribute<TDmYearRepPoint, MBuildingSmPoint> MBuildingSmPoint;
	public static volatile SingularAttribute<TDmYearRepPoint, TDmYearRep> TDmYearRep;
}
