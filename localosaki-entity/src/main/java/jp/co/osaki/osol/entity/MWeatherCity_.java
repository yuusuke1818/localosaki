package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.634+0900")
@StaticMetamodel(MWeatherCity.class)
public class MWeatherCity_ {
	public static volatile SingularAttribute<MWeatherCity, String> cityCd;
	public static volatile SingularAttribute<MWeatherCity, String> cityName;
	public static volatile SingularAttribute<MWeatherCity, Timestamp> createDate;
	public static volatile SingularAttribute<MWeatherCity, Long> createUserId;
	public static volatile SingularAttribute<MWeatherCity, Timestamp> updateDate;
	public static volatile SingularAttribute<MWeatherCity, Long> updateUserId;
	public static volatile SingularAttribute<MWeatherCity, Integer> version;
	public static volatile SingularAttribute<MWeatherCity, MPrefecture> MPrefecture;
	public static volatile ListAttribute<MWeatherCity, TBuilding> TBuildings;
	public static volatile ListAttribute<MWeatherCity, TTimeSlotWeather> TTimeSlotWeathers;
	public static volatile ListAttribute<MWeatherCity, TTimelyWeather> TTimelyWeathers;
	public static volatile ListAttribute<MWeatherCity, TWeekWeather> TWeekWeathers;
}
