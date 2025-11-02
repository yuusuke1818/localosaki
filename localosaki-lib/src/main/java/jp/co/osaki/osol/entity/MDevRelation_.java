package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:57.323+0900")
@StaticMetamodel(MDevRelation.class)
public class MDevRelation_ {
	public static volatile SingularAttribute<MDevRelation, MDevRelationPK> id;
	public static volatile SingularAttribute<MDevRelation, Timestamp> createDate;
	public static volatile SingularAttribute<MDevRelation, Long> createUserId;
	public static volatile SingularAttribute<MDevRelation, Timestamp> updateDate;
	public static volatile SingularAttribute<MDevRelation, Long> updateUserId;
	public static volatile SingularAttribute<MDevRelation, Integer> version;
	public static volatile SingularAttribute<MDevRelation, MDevPrm> MDevPrm;
	public static volatile SingularAttribute<MDevRelation, TBuilding> TBuilding;
}
