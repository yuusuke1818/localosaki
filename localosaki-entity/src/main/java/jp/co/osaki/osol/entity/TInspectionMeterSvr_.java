package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.563+0900")
@StaticMetamodel(TInspectionMeterSvr.class)
public class TInspectionMeterSvr_ {
	public static volatile SingularAttribute<TInspectionMeterSvr, TInspectionMeterSvrPK> id;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> createDate;
	public static volatile SingularAttribute<TInspectionMeterSvr, Long> createUserId;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> endFlg;
	public static volatile SingularAttribute<TInspectionMeterSvr, String> inspType;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> latestInspDate;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> latestInspVal;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> latestUseVal;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> multipleRate;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> prevInspDate;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> prevInspDate2;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> prevInspVal;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> prevInspVal2;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> prevUseVal;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> recDate;
	public static volatile SingularAttribute<TInspectionMeterSvr, String> recMan;
	public static volatile SingularAttribute<TInspectionMeterSvr, Timestamp> updateDate;
	public static volatile SingularAttribute<TInspectionMeterSvr, Long> updateUserId;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> usePerRate;
	public static volatile SingularAttribute<TInspectionMeterSvr, Integer> version;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> wkDayOtherUse;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> wkDaySummerUse;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> wkFamilyUse;
	public static volatile SingularAttribute<TInspectionMeterSvr, BigDecimal> wkNightUse;
}
