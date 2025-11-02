package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.043+0900")
@StaticMetamodel(TBuildingEstimateKind.class)
public class TBuildingEstimateKind_ {
	public static volatile SingularAttribute<TBuildingEstimateKind, TBuildingEstimateKindPK> id;
	public static volatile SingularAttribute<TBuildingEstimateKind, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingEstimateKind, Long> createUserId;
	public static volatile SingularAttribute<TBuildingEstimateKind, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingEstimateKind, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingEstimateKind, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingEstimateKind, Integer> version;
	public static volatile SingularAttribute<TBuildingEstimateKind, MEstimateKind> MEstimateKind;
	public static volatile SingularAttribute<TBuildingEstimateKind, TBuilding> TBuilding;
}
