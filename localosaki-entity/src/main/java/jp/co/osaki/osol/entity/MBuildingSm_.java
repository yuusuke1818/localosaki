package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.311+0900")
@StaticMetamodel(MBuildingSm.class)
public class MBuildingSm_ {
	public static volatile SingularAttribute<MBuildingSm, MBuildingSmPK> id;
	public static volatile SingularAttribute<MBuildingSm, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingSm, Long> createUserId;
	public static volatile SingularAttribute<MBuildingSm, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingSm, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingSm, Integer> version;
	public static volatile SingularAttribute<MBuildingSm, MBuildingDm> MBuildingDm;
	public static volatile SingularAttribute<MBuildingSm, MSmPrm> MSmPrm;
	public static volatile ListAttribute<MBuildingSm, MBuildingSmPoint> MBuildingSmPoints;
	public static volatile ListAttribute<MBuildingSm, TDmDayMax> TDmDayMaxs;
	public static volatile ListAttribute<MBuildingSm, MSmLineVerify> MSmLineVerifies;
}
