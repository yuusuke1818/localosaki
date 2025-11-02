package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.802+0900")
@StaticMetamodel(MCorpFacilityKind.class)
public class MCorpFacilityKind_ {
	public static volatile SingularAttribute<MCorpFacilityKind, MCorpFacilityKindPK> id;
	public static volatile SingularAttribute<MCorpFacilityKind, Integer> bigDisplayOrder;
	public static volatile SingularAttribute<MCorpFacilityKind, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpFacilityKind, Long> createUserId;
	public static volatile SingularAttribute<MCorpFacilityKind, Integer> delFlg;
	public static volatile SingularAttribute<MCorpFacilityKind, String> facilitySmallKindEditName;
	public static volatile SingularAttribute<MCorpFacilityKind, Integer> smallDisplayOrder;
	public static volatile SingularAttribute<MCorpFacilityKind, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpFacilityKind, Long> updateUserId;
	public static volatile SingularAttribute<MCorpFacilityKind, Integer> version;
	public static volatile SingularAttribute<MCorpFacilityKind, MCorp> MCorp;
	public static volatile SingularAttribute<MCorpFacilityKind, MFacilitySmallKind> MFacilitySmallKind;
	public static volatile ListAttribute<MCorpFacilityKind, MFacilityKind> MFacilityKinds;
}
