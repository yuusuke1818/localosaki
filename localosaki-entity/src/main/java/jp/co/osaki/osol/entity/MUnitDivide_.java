package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.988+0900")
@StaticMetamodel(MUnitDivide.class)
public class MUnitDivide_ {
	public static volatile SingularAttribute<MUnitDivide, MUnitDividePK> id;
	public static volatile SingularAttribute<MUnitDivide, Timestamp> createDate;
	public static volatile SingularAttribute<MUnitDivide, Long> createUserId;
	public static volatile SingularAttribute<MUnitDivide, Integer> delFlg;
	public static volatile SingularAttribute<MUnitDivide, String> totalAvgType;
	public static volatile SingularAttribute<MUnitDivide, String> unitDivideFormula;
	public static volatile SingularAttribute<MUnitDivide, String> unitDivideName;
	public static volatile SingularAttribute<MUnitDivide, String> unitDividePowerRoot;
	public static volatile SingularAttribute<MUnitDivide, Timestamp> updateDate;
	public static volatile SingularAttribute<MUnitDivide, Long> updateUserId;
	public static volatile SingularAttribute<MUnitDivide, Integer> version;
	public static volatile ListAttribute<MUnitDivide, MBuildUnitDenominator> MBuildUnitDenominators;
	public static volatile ListAttribute<MUnitDivide, MSubtype> MSubtypes;
	public static volatile SingularAttribute<MUnitDivide, MCorp> MCorp;
	public static volatile SingularAttribute<MUnitDivide, MUnitFactor> MUnitFactor;
}
