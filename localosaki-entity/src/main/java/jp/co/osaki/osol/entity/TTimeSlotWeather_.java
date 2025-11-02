package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T14:32:20.839+0900")
@StaticMetamodel(TTimeSlotWeather.class)
public class TTimeSlotWeather_ {
	public static volatile SingularAttribute<TTimeSlotWeather, TTimeSlotWeatherPK> id;
	public static volatile SingularAttribute<TTimeSlotWeather, Timestamp> createDate;
	public static volatile SingularAttribute<TTimeSlotWeather, Long> createUserId;
	public static volatile SingularAttribute<TTimeSlotWeather, BigDecimal> rainAmount;
	public static volatile SingularAttribute<TTimeSlotWeather, BigDecimal> temperature;
	public static volatile SingularAttribute<TTimeSlotWeather, Timestamp> updateDate;
	public static volatile SingularAttribute<TTimeSlotWeather, Long> updateUserId;
	public static volatile SingularAttribute<TTimeSlotWeather, Integer> version;
	public static volatile SingularAttribute<TTimeSlotWeather, String> weatherState;
	public static volatile SingularAttribute<TTimeSlotWeather, MWeatherCity> MWeatherCity;
}
