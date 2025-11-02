package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.556+0900")
@StaticMetamodel(TInspectionMeterRev.class)
public class TInspectionMeterRev_ {
	public static volatile SingularAttribute<TInspectionMeterRev, TInspectionMeterRevPK> id;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> createDate;
	public static volatile SingularAttribute<TInspectionMeterRev, Long> createUserId;
	public static volatile SingularAttribute<TInspectionMeterRev, String> inspType;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> latestInspDate;
	public static volatile SingularAttribute<TInspectionMeterRev, String> latestInspVal;
	public static volatile SingularAttribute<TInspectionMeterRev, BigDecimal> latestUseVal;
	public static volatile SingularAttribute<TInspectionMeterRev, BigDecimal> multipleRate;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> prevInspDate;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> prevInspDate2;
	public static volatile SingularAttribute<TInspectionMeterRev, String> prevInspVal;
	public static volatile SingularAttribute<TInspectionMeterRev, String> prevInspVal2;
	public static volatile SingularAttribute<TInspectionMeterRev, BigDecimal> prevUseVal;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> recDate;
	public static volatile SingularAttribute<TInspectionMeterRev, String> recMan;
	public static volatile SingularAttribute<TInspectionMeterRev, Timestamp> updateDate;
	public static volatile SingularAttribute<TInspectionMeterRev, Long> updateUserId;
	public static volatile SingularAttribute<TInspectionMeterRev, BigDecimal> usePerRate;
	public static volatile SingularAttribute<TInspectionMeterRev, Integer> version;
}
