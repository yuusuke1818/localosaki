package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.434+0900")
@StaticMetamodel(MSmLinePoint.class)
public class MSmLinePoint_ {
	public static volatile SingularAttribute<MSmLinePoint, MSmLinePointPK> id;
	public static volatile SingularAttribute<MSmLinePoint, String> comment;
	public static volatile SingularAttribute<MSmLinePoint, Timestamp> createDate;
	public static volatile SingularAttribute<MSmLinePoint, Long> createUserId;
	public static volatile SingularAttribute<MSmLinePoint, Integer> delFlg;
	public static volatile SingularAttribute<MSmLinePoint, String> pointCalcType;
	public static volatile SingularAttribute<MSmLinePoint, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmLinePoint, Long> updateUserId;
	public static volatile SingularAttribute<MSmLinePoint, Integer> version;
	public static volatile SingularAttribute<MSmLinePoint, MBuildingSmPoint> MBuildingSmPoint;
	public static volatile SingularAttribute<MSmLinePoint, MLine> MLine;
}
