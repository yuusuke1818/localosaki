package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.539+0900")
@StaticMetamodel(TInspectionMeter.class)
public class TInspectionMeter_ {
	public static volatile SingularAttribute<TInspectionMeter, TInspectionMeterPK> id;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> createDate;
	public static volatile SingularAttribute<TInspectionMeter, Long> createUserId;
	public static volatile SingularAttribute<TInspectionMeter, String> inspType;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> latestInspDate;
	public static volatile SingularAttribute<TInspectionMeter, String> latestInspVal;
	public static volatile SingularAttribute<TInspectionMeter, BigDecimal> latestUseVal;
	public static volatile SingularAttribute<TInspectionMeter, BigDecimal> multipleRate;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> prevInspDate;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> prevInspDate2;
	public static volatile SingularAttribute<TInspectionMeter, String> prevInspVal;
	public static volatile SingularAttribute<TInspectionMeter, String> prevInspVal2;
	public static volatile SingularAttribute<TInspectionMeter, BigDecimal> prevUseVal;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> recDate;
	public static volatile SingularAttribute<TInspectionMeter, String> recMan;
	public static volatile SingularAttribute<TInspectionMeter, Timestamp> updateDate;
	public static volatile SingularAttribute<TInspectionMeter, Long> updateUserId;
	public static volatile SingularAttribute<TInspectionMeter, BigDecimal> usePerRate;
	public static volatile SingularAttribute<TInspectionMeter, Integer> version;
}
