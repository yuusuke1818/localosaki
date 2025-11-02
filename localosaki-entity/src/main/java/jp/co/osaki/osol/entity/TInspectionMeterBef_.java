package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-08-20T17:56:20.544+0900")
@StaticMetamodel(TInspectionMeterBef.class)
public class TInspectionMeterBef_ {
	public static volatile SingularAttribute<TInspectionMeterBef, TInspectionMeterBefPK> id;
	public static volatile SingularAttribute<TInspectionMeterBef, Long> buildingId;
	public static volatile SingularAttribute<TInspectionMeterBef, Timestamp> createDate;
	public static volatile SingularAttribute<TInspectionMeterBef, Long> createUserId;
	public static volatile SingularAttribute<TInspectionMeterBef, BigDecimal> latestInspVal;
	public static volatile SingularAttribute<TInspectionMeterBef, Long> meterType;
	public static volatile SingularAttribute<TInspectionMeterBef, BigDecimal> multi;
	public static volatile SingularAttribute<TInspectionMeterBef, Timestamp> recDate;
	public static volatile SingularAttribute<TInspectionMeterBef, String> recMan;
	public static volatile SingularAttribute<TInspectionMeterBef, Timestamp> updateDate;
	public static volatile SingularAttribute<TInspectionMeterBef, Long> updateUserId;
	public static volatile SingularAttribute<TInspectionMeterBef, Integer> version;
}
