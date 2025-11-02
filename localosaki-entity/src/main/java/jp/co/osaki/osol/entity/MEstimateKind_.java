package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.852+0900")
@StaticMetamodel(MEstimateKind.class)
public class MEstimateKind_ {
	public static volatile SingularAttribute<MEstimateKind, MEstimateKindPK> id;
	public static volatile SingularAttribute<MEstimateKind, Timestamp> createDate;
	public static volatile SingularAttribute<MEstimateKind, Long> createUserId;
	public static volatile SingularAttribute<MEstimateKind, Integer> delFlg;
	public static volatile SingularAttribute<MEstimateKind, String> estimateName;
	public static volatile SingularAttribute<MEstimateKind, Timestamp> updateDate;
	public static volatile SingularAttribute<MEstimateKind, Long> updateUserId;
	public static volatile SingularAttribute<MEstimateKind, Integer> version;
	public static volatile SingularAttribute<MEstimateKind, MCorp> MCorp;
	public static volatile ListAttribute<MEstimateKind, MEstimateUnit> MEstimateUnits;
	public static volatile ListAttribute<MEstimateKind, TBuildingEstimateKind> TBuildingEstimateKinds;
}
