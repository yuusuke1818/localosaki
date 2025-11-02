package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.727+0900")
@StaticMetamodel(TDemandPowerForecastDay.class)
public class TDemandPowerForecastDay_ {
	public static volatile SingularAttribute<TDemandPowerForecastDay, TDemandPowerForecastDayPK> id;
	public static volatile SingularAttribute<TDemandPowerForecastDay, BigDecimal> airDemandForecastValue;
	public static volatile SingularAttribute<TDemandPowerForecastDay, Timestamp> createDate;
	public static volatile SingularAttribute<TDemandPowerForecastDay, Long> createUserId;
	public static volatile SingularAttribute<TDemandPowerForecastDay, BigDecimal> totalDemandValue;
	public static volatile SingularAttribute<TDemandPowerForecastDay, Timestamp> updateDate;
	public static volatile SingularAttribute<TDemandPowerForecastDay, Long> updateUserId;
	public static volatile SingularAttribute<TDemandPowerForecastDay, Integer> version;
	public static volatile SingularAttribute<TDemandPowerForecastDay, MSmPrm> MSmPrm;
}
