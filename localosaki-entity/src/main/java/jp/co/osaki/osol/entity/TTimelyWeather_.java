package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:25.009+0900")
@StaticMetamodel(TTimelyWeather.class)
public class TTimelyWeather_ {
	public static volatile SingularAttribute<TTimelyWeather, TTimelyWeatherPK> id;
	public static volatile SingularAttribute<TTimelyWeather, BigDecimal> barometricPressure;
	public static volatile SingularAttribute<TTimelyWeather, String> cloudAmount;
	public static volatile SingularAttribute<TTimelyWeather, Timestamp> createDate;
	public static volatile SingularAttribute<TTimelyWeather, Long> createUserId;
	public static volatile SingularAttribute<TTimelyWeather, BigDecimal> humidity;
	public static volatile SingularAttribute<TTimelyWeather, BigDecimal> rainAmount;
	public static volatile SingularAttribute<TTimelyWeather, BigDecimal> temperature;
	public static volatile SingularAttribute<TTimelyWeather, Timestamp> updateDate;
	public static volatile SingularAttribute<TTimelyWeather, Long> updateUserId;
	public static volatile SingularAttribute<TTimelyWeather, Integer> version;
	public static volatile SingularAttribute<TTimelyWeather, String> weather;
	public static volatile SingularAttribute<TTimelyWeather, String> windDirection;
	public static volatile SingularAttribute<TTimelyWeather, BigDecimal> windSpeed;
	public static volatile SingularAttribute<TTimelyWeather, MWeatherCity> MWeatherCity;
}
