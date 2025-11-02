package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.766+0900")
@StaticMetamodel(MBuildUnitDenominator.class)
public class MBuildUnitDenominator_ {
	public static volatile SingularAttribute<MBuildUnitDenominator, MBuildUnitDenominatorPK> id;
	public static volatile SingularAttribute<MBuildUnitDenominator, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildUnitDenominator, Long> createUserId;
	public static volatile SingularAttribute<MBuildUnitDenominator, BigDecimal> defaultUnitDenominator;
	public static volatile SingularAttribute<MBuildUnitDenominator, Integer> delFlg;
	public static volatile SingularAttribute<MBuildUnitDenominator, Integer> unitDivideNotFlg;
	public static volatile SingularAttribute<MBuildUnitDenominator, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildUnitDenominator, Long> updateUserId;
	public static volatile SingularAttribute<MBuildUnitDenominator, Integer> version;
	public static volatile SingularAttribute<MBuildUnitDenominator, MUnitDivide> MUnitDivide;
	public static volatile SingularAttribute<MBuildUnitDenominator, TBuilding> TBuilding;
	public static volatile ListAttribute<MBuildUnitDenominator, TMonthlyUnitDenominator> TMonthlyUnitDenominators;
}
