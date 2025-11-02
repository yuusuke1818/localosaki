package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.955+0900")
@StaticMetamodel(MPrefecture.class)
public class MPrefecture_ {
	public static volatile SingularAttribute<MPrefecture, String> prefectureCd;
	public static volatile SingularAttribute<MPrefecture, Timestamp> createDate;
	public static volatile SingularAttribute<MPrefecture, Long> createUserId;
	public static volatile SingularAttribute<MPrefecture, String> prefectureName;
	public static volatile SingularAttribute<MPrefecture, Timestamp> updateDate;
	public static volatile SingularAttribute<MPrefecture, Long> updateUserId;
	public static volatile SingularAttribute<MPrefecture, Integer> version;
	public static volatile ListAttribute<MPrefecture, MCorp> MCorps;
	public static volatile ListAttribute<MPrefecture, MMunicipality> MMunicipalities;
	public static volatile ListAttribute<MPrefecture, TBuilding> TBuildings;
	public static volatile ListAttribute<MPrefecture, MWeatherCity> MWeatherCities;
}
