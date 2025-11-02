package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.861+0900")
@StaticMetamodel(MEstimateUnit.class)
public class MEstimateUnit_ {
	public static volatile SingularAttribute<MEstimateUnit, MEstimateUnitPK> id;
	public static volatile SingularAttribute<MEstimateUnit, Timestamp> createDate;
	public static volatile SingularAttribute<MEstimateUnit, Long> createUserId;
	public static volatile SingularAttribute<MEstimateUnit, Integer> delFlg;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month1;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month10;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month11;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month12;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month2;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month3;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month4;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month5;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month6;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month7;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month8;
	public static volatile SingularAttribute<MEstimateUnit, BigDecimal> month9;
	public static volatile SingularAttribute<MEstimateUnit, String> unit;
	public static volatile SingularAttribute<MEstimateUnit, Long> unitDivideId;
	public static volatile SingularAttribute<MEstimateUnit, String> unitFactorCd;
	public static volatile SingularAttribute<MEstimateUnit, Timestamp> updateDate;
	public static volatile SingularAttribute<MEstimateUnit, Long> updateUserId;
	public static volatile SingularAttribute<MEstimateUnit, Integer> version;
	public static volatile SingularAttribute<MEstimateUnit, MEstimateKind> MEstimateKind;
	public static volatile SingularAttribute<MEstimateUnit, MRegionType> MRegionType;
}
