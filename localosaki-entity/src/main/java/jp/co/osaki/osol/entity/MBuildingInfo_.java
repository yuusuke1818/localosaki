package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.146+0900")
@StaticMetamodel(MBuildingInfo.class)
public class MBuildingInfo_ {
	public static volatile SingularAttribute<MBuildingInfo, MBuildingInfoPK> id;
	public static volatile SingularAttribute<MBuildingInfo, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingInfo, Long> createUserId;
	public static volatile SingularAttribute<MBuildingInfo, Timestamp> recDate;
	public static volatile SingularAttribute<MBuildingInfo, String> recMan;
	public static volatile SingularAttribute<MBuildingInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingInfo, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingInfo, Integer> version;
	public static volatile SingularAttribute<MBuildingInfo, TBuilding> TBuilding;
}
