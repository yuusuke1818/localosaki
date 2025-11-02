package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.319+0900")
@StaticMetamodel(MBuildingSmPoint.class)
public class MBuildingSmPoint_ {
	public static volatile SingularAttribute<MBuildingSmPoint, MBuildingSmPointPK> id;
	public static volatile SingularAttribute<MBuildingSmPoint, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingSmPoint, Long> createUserId;
	public static volatile SingularAttribute<MBuildingSmPoint, Integer> delFlg;
	public static volatile SingularAttribute<MBuildingSmPoint, String> pointName;
	public static volatile SingularAttribute<MBuildingSmPoint, Integer> pointSumFlg;
	public static volatile SingularAttribute<MBuildingSmPoint, String> pointUnit;
	public static volatile SingularAttribute<MBuildingSmPoint, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingSmPoint, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingSmPoint, Integer> version;
	public static volatile SingularAttribute<MBuildingSmPoint, MBuildingSm> MBuildingSm;
	public static volatile SingularAttribute<MBuildingSmPoint, MSmPoint> MSmPoint;
	public static volatile ListAttribute<MBuildingSmPoint, MSmLinePoint> MSmLinePoints;
	public static volatile ListAttribute<MBuildingSmPoint, TDmDayRepPoint> TDmDayRepPoints;
	public static volatile ListAttribute<MBuildingSmPoint, TDmMonthRepPoint> TDmMonthRepPoints;
	public static volatile ListAttribute<MBuildingSmPoint, TDmWeekRepPoint> TDmWeekRepPoints;
	public static volatile ListAttribute<MBuildingSmPoint, TDmYearRepPoint> TDmYearRepPoints;
    public static volatile ListAttribute<MBuildingSmPoint, TDmDayRepPointInput> TDmDayRepPointInputs;
}
