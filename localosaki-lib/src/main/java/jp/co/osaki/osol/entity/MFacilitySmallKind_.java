package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.891+0900")
@StaticMetamodel(MFacilitySmallKind.class)
public class MFacilitySmallKind_ {
	public static volatile SingularAttribute<MFacilitySmallKind, MFacilitySmallKindPK> id;
	public static volatile SingularAttribute<MFacilitySmallKind, Timestamp> createDate;
	public static volatile SingularAttribute<MFacilitySmallKind, Long> createUserId;
	public static volatile SingularAttribute<MFacilitySmallKind, Integer> displayOrder;
	public static volatile SingularAttribute<MFacilitySmallKind, String> facilitySmallKindName;
	public static volatile SingularAttribute<MFacilitySmallKind, Integer> refrigerantFlg;
	public static volatile SingularAttribute<MFacilitySmallKind, Timestamp> updateDate;
	public static volatile SingularAttribute<MFacilitySmallKind, Long> updateUserId;
	public static volatile SingularAttribute<MFacilitySmallKind, Integer> version;
	public static volatile ListAttribute<MFacilitySmallKind, MCorpFacilityKind> MCorpFacilityKinds;
	public static volatile SingularAttribute<MFacilitySmallKind, MFacilityBigKind> MFacilityBigKind;
}
