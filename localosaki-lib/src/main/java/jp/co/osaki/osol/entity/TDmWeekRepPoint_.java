package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.567+0900")
@StaticMetamodel(TDmWeekRepPoint.class)
public class TDmWeekRepPoint_ {
	public static volatile SingularAttribute<TDmWeekRepPoint, TDmWeekRepPointPK> id;
	public static volatile SingularAttribute<TDmWeekRepPoint, Timestamp> createDate;
	public static volatile SingularAttribute<TDmWeekRepPoint, Long> createUserId;
	public static volatile SingularAttribute<TDmWeekRepPoint, BigDecimal> pointAvg;
	public static volatile SingularAttribute<TDmWeekRepPoint, BigDecimal> pointMax;
	public static volatile SingularAttribute<TDmWeekRepPoint, BigDecimal> pointMin;
	public static volatile SingularAttribute<TDmWeekRepPoint, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmWeekRepPoint, Long> updateUserId;
	public static volatile SingularAttribute<TDmWeekRepPoint, Integer> version;
	public static volatile SingularAttribute<TDmWeekRepPoint, MBuildingSmPoint> MBuildingSmPoint;
	public static volatile SingularAttribute<TDmWeekRepPoint, TDmWeekRep> TDmWeekRep;
}
