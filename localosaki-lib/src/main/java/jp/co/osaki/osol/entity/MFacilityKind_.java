package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.879+0900")
@StaticMetamodel(MFacilityKind.class)
public class MFacilityKind_ {
	public static volatile SingularAttribute<MFacilityKind, MFacilityKindPK> id;
	public static volatile SingularAttribute<MFacilityKind, Timestamp> createDate;
	public static volatile SingularAttribute<MFacilityKind, Long> createUserId;
	public static volatile SingularAttribute<MFacilityKind, Integer> delFlg;
	public static volatile SingularAttribute<MFacilityKind, Timestamp> updateDate;
	public static volatile SingularAttribute<MFacilityKind, Long> updateUserId;
	public static volatile SingularAttribute<MFacilityKind, Integer> version;
	public static volatile SingularAttribute<MFacilityKind, MCorpFacilityKind> MCorpFacilityKind;
	public static volatile SingularAttribute<MFacilityKind, MFacility> MFacility;
}
