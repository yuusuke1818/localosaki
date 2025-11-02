package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-08-20T17:56:18.843+0900")
@StaticMetamodel(MBuildingSms.class)
public class MBuildingSms_ {
	public static volatile SingularAttribute<MBuildingSms, MBuildingSmsPK> id;
	public static volatile SingularAttribute<MBuildingSms, BigDecimal> chkInt;
	public static volatile SingularAttribute<MBuildingSms, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingSms, Long> createUserId;
	public static volatile SingularAttribute<MBuildingSms, String> mail1;
	public static volatile SingularAttribute<MBuildingSms, String> mail10;
	public static volatile SingularAttribute<MBuildingSms, String> mail2;
	public static volatile SingularAttribute<MBuildingSms, String> mail3;
	public static volatile SingularAttribute<MBuildingSms, String> mail4;
	public static volatile SingularAttribute<MBuildingSms, String> mail5;
	public static volatile SingularAttribute<MBuildingSms, String> mail6;
	public static volatile SingularAttribute<MBuildingSms, String> mail7;
	public static volatile SingularAttribute<MBuildingSms, String> mail8;
	public static volatile SingularAttribute<MBuildingSms, String> mail9;
	public static volatile SingularAttribute<MBuildingSms, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingSms, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingSms, Integer> version;
	public static volatile SingularAttribute<MBuildingSms, TBuilding> TBuilding;
	public static volatile SingularAttribute<MBuildingSms, Integer> delFlg;
}
