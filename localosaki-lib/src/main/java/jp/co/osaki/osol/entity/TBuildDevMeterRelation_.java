package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-10-20T12:05:12.532+0900")
@StaticMetamodel(TBuildDevMeterRelation.class)
public class TBuildDevMeterRelation_ {
	public static volatile SingularAttribute<TBuildDevMeterRelation, TBuildDevMeterRelationPK> id;
	public static volatile SingularAttribute<TBuildDevMeterRelation, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildDevMeterRelation, Long> createUserId;
	public static volatile SingularAttribute<TBuildDevMeterRelation, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildDevMeterRelation, Long> updateUserId;
	public static volatile SingularAttribute<TBuildDevMeterRelation, Integer> version;
	public static volatile SingularAttribute<TBuildDevMeterRelation, MMeter> MMeter;
	public static volatile SingularAttribute<TBuildDevMeterRelation, TBuilding> TBuilding;
}
