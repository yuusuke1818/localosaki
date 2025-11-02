package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.933+0900")
@StaticMetamodel(MMunicipality.class)
public class MMunicipality_ {
	public static volatile SingularAttribute<MMunicipality, String> municipalityCd;
	public static volatile SingularAttribute<MMunicipality, Timestamp> createDate;
	public static volatile SingularAttribute<MMunicipality, Long> createUserId;
	public static volatile SingularAttribute<MMunicipality, String> municipalityName;
	public static volatile SingularAttribute<MMunicipality, Timestamp> updateDate;
	public static volatile SingularAttribute<MMunicipality, Long> updateUserId;
	public static volatile SingularAttribute<MMunicipality, Integer> version;
	public static volatile ListAttribute<MMunicipality, MCorp> MCorps;
	public static volatile SingularAttribute<MMunicipality, MMunicipality> MMunicipality;
	public static volatile ListAttribute<MMunicipality, MMunicipality> MMunicipalities;
	public static volatile SingularAttribute<MMunicipality, MPrefecture> MPrefecture;
	public static volatile SingularAttribute<MMunicipality, MRegionType> MRegionType;
	public static volatile ListAttribute<MMunicipality, TBuilding> TBuildings;
}
