package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.727+0900")
@StaticMetamodel(TDemandPowerForecastTime.class)
public class TDemandPowerForecastTime_ {
	public static volatile SingularAttribute<TDemandPowerForecastTime, TDemandPowerForecastTimePK> id;
	public static volatile SingularAttribute<TDemandPowerForecastTime, BigDecimal> aiForecastValue;
	public static volatile SingularAttribute<TDemandPowerForecastTime, BigDecimal> comfortableForecastValue;
	public static volatile SingularAttribute<TDemandPowerForecastTime, Timestamp> createDate;
	public static volatile SingularAttribute<TDemandPowerForecastTime, Long> createUserId;
	public static volatile SingularAttribute<TDemandPowerForecastTime, BigDecimal> ecoForecastValue;
	public static volatile SingularAttribute<TDemandPowerForecastTime, BigDecimal> normalForecastValue;
	public static volatile SingularAttribute<TDemandPowerForecastTime, Timestamp> updateDate;
	public static volatile SingularAttribute<TDemandPowerForecastTime, Long> updateUserId;
	public static volatile SingularAttribute<TDemandPowerForecastTime, Integer> version;
	public static volatile SingularAttribute<TDemandPowerForecastTime, MSmPrm> MSmPrm;
}
