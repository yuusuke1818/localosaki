package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-08-22T10:54:11.858+0900")
@StaticMetamodel(MSubtype.class)
public class MSubtype_ {
	public static volatile SingularAttribute<MSubtype, MSubtypePK> id;
	public static volatile SingularAttribute<MSubtype, Timestamp> createDate;
	public static volatile SingularAttribute<MSubtype, Long> createUserId;
	public static volatile SingularAttribute<MSubtype, Integer> delFlg;
	public static volatile SingularAttribute<MSubtype, Integer> mainBusinessFlg;
	public static volatile SingularAttribute<MSubtype, String> securetaryName;
	public static volatile SingularAttribute<MSubtype, String> subtypeName;
	public static volatile SingularAttribute<MSubtype, String> subtypeNo;
	public static volatile SingularAttribute<MSubtype, Timestamp> updateDate;
	public static volatile SingularAttribute<MSubtype, Long> updateUserId;
	public static volatile SingularAttribute<MSubtype, Long> unitDivideId;
	public static volatile SingularAttribute<MSubtype, Integer> version;
	public static volatile SingularAttribute<MSubtype, MCorp> MCorp;
	public static volatile SingularAttribute<MSubtype, MUnitDivide> MUnitDivide;
	public static volatile ListAttribute<MSubtype, TBuildingSubtype> TBuildingSubtypes;
}
