package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.059+0900")
@StaticMetamodel(TBuildingPerson.class)
public class TBuildingPerson_ {
	public static volatile SingularAttribute<TBuildingPerson, TBuildingPersonPK> id;
	public static volatile SingularAttribute<TBuildingPerson, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingPerson, Long> createUserId;
	public static volatile SingularAttribute<TBuildingPerson, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingPerson, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingPerson, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingPerson, Integer> version;
	public static volatile SingularAttribute<TBuildingPerson, MPerson> MPerson;
	public static volatile SingularAttribute<TBuildingPerson, TBuilding> TBuilding;
}
