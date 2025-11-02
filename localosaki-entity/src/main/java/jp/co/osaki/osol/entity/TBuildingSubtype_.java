package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.071+0900")
@StaticMetamodel(TBuildingSubtype.class)
public class TBuildingSubtype_ {
	public static volatile SingularAttribute<TBuildingSubtype, TBuildingSubtypePK> id;
	public static volatile SingularAttribute<TBuildingSubtype, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingSubtype, Long> createUserId;
	public static volatile SingularAttribute<TBuildingSubtype, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingSubtype, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingSubtype, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingSubtype, Integer> version;
	public static volatile SingularAttribute<TBuildingSubtype, MSubtype> MSubtype;
	public static volatile SingularAttribute<TBuildingSubtype, TBuilding> TBuilding;
}
