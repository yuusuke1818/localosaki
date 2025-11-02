package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T14:32:20.843+0900")
@StaticMetamodel(TTimeSlotWeatherPK.class)
public class TTimeSlotWeatherPK_ {
	public static volatile SingularAttribute<TTimeSlotWeatherPK, String> cityCd;
	public static volatile SingularAttribute<TTimeSlotWeatherPK, Date> targetDate;
	public static volatile SingularAttribute<TTimeSlotWeatherPK, String> weatherTimeSlot;
}
