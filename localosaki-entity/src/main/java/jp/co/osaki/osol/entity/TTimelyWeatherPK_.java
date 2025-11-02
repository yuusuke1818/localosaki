package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:25.024+0900")
@StaticMetamodel(TTimelyWeatherPK.class)
public class TTimelyWeatherPK_ {
	public static volatile SingularAttribute<TTimelyWeatherPK, String> cityCd;
	public static volatile SingularAttribute<TTimelyWeatherPK, Date> targetDateTime;
}
