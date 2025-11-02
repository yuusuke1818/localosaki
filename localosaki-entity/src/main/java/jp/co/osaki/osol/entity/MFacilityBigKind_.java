package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.875+0900")
@StaticMetamodel(MFacilityBigKind.class)
public class MFacilityBigKind_ {
	public static volatile SingularAttribute<MFacilityBigKind, Long> facilityBigKindId;
	public static volatile SingularAttribute<MFacilityBigKind, Timestamp> createDate;
	public static volatile SingularAttribute<MFacilityBigKind, Long> createUserId;
	public static volatile SingularAttribute<MFacilityBigKind, Integer> displayOrder;
	public static volatile SingularAttribute<MFacilityBigKind, String> facilityBigKindName;
	public static volatile SingularAttribute<MFacilityBigKind, Timestamp> updateDate;
	public static volatile SingularAttribute<MFacilityBigKind, Long> updateUserId;
	public static volatile SingularAttribute<MFacilityBigKind, Integer> version;
	public static volatile ListAttribute<MFacilityBigKind, MFacilitySmallKind> MFacilitySmallKinds;
}
