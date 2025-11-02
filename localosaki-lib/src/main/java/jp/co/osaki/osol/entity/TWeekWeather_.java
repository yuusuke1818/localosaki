package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T14:32:20.847+0900")
@StaticMetamodel(TWeekWeather.class)
public class TWeekWeather_ {
	public static volatile SingularAttribute<TWeekWeather, TWeekWeatherPK> id;
	public static volatile SingularAttribute<TWeekWeather, Timestamp> createDate;
	public static volatile SingularAttribute<TWeekWeather, Long> createUserId;
	public static volatile SingularAttribute<TWeekWeather, BigDecimal> highTemp;
	public static volatile SingularAttribute<TWeekWeather, BigDecimal> lowTemp;
	public static volatile SingularAttribute<TWeekWeather, Integer> rainProbability;
	public static volatile SingularAttribute<TWeekWeather, Timestamp> updateDate;
	public static volatile SingularAttribute<TWeekWeather, Long> updateUserId;
	public static volatile SingularAttribute<TWeekWeather, Integer> version;
	public static volatile SingularAttribute<TWeekWeather, String> weatherState;
	public static volatile SingularAttribute<TWeekWeather, MWeatherCity> MWeatherCity;
}
