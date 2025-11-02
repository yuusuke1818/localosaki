package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.051+0900")
@StaticMetamodel(TBuildingGroup.class)
public class TBuildingGroup_ {
	public static volatile SingularAttribute<TBuildingGroup, TBuildingGroupPK> id;
	public static volatile SingularAttribute<TBuildingGroup, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingGroup, Long> createUserId;
	public static volatile SingularAttribute<TBuildingGroup, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingGroup, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingGroup, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingGroup, Integer> version;
	public static volatile SingularAttribute<TBuildingGroup, MChildGroup> MChildGroup;
	public static volatile SingularAttribute<TBuildingGroup, TBuilding> TBuilding;
}
