package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.727+0900")
@StaticMetamodel(TDemandPowerForecastTimePK.class)
public class TDemandPowerForecastTimePK_ {
    public static volatile SingularAttribute<TDemandPowerForecastTimePK, Long> smId;
    public static volatile SingularAttribute<TDemandPowerForecastTimePK, Date> forecastDate;
    public static volatile SingularAttribute<TDemandPowerForecastTimePK, BigDecimal> jigenNo;
}
