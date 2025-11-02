package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.480+0900")
@StaticMetamodel(TAmedasWeather.class)
public class TAmedasWeather_ {
	public static volatile SingularAttribute<TAmedasWeather, TAmedasWeatherPK> id;
	public static volatile SingularAttribute<TAmedasWeather, Timestamp> createDate;
	public static volatile SingularAttribute<TAmedasWeather, Long> createUserId;
	public static volatile SingularAttribute<TAmedasWeather, BigDecimal> outAirTemp;
	public static volatile SingularAttribute<TAmedasWeather, Timestamp> updateDate;
	public static volatile SingularAttribute<TAmedasWeather, Long> updateUserId;
	public static volatile SingularAttribute<TAmedasWeather, Integer> version;
	public static volatile SingularAttribute<TAmedasWeather, MAmedasObservatory> MAmedasObservatory;
}
