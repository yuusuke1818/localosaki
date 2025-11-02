package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.158+0900")
@StaticMetamodel(TMonthlyUnitDenominator.class)
public class TMonthlyUnitDenominator_ {
	public static volatile SingularAttribute<TMonthlyUnitDenominator, TMonthlyUnitDenominatorPK> id;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Integer> changeAuthFlg;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Timestamp> createDate;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Long> createUserId;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Integer> delFlg;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Integer> inputOutFlg;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, BigDecimal> unitDivideValue;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Timestamp> updateDate;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Long> updateUserId;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, Integer> version;
	public static volatile SingularAttribute<TMonthlyUnitDenominator, MBuildUnitDenominator> MBuildUnitDenominator;
}
