package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.974+0900")
@StaticMetamodel(MRegionType.class)
public class MRegionType_ {
	public static volatile SingularAttribute<MRegionType, Long> regionTypeCode;
	public static volatile SingularAttribute<MRegionType, Timestamp> createDate;
	public static volatile SingularAttribute<MRegionType, Long> createUserId;
	public static volatile SingularAttribute<MRegionType, String> regionTypeName;
	public static volatile SingularAttribute<MRegionType, Timestamp> updateDate;
	public static volatile SingularAttribute<MRegionType, Long> updateUserId;
	public static volatile SingularAttribute<MRegionType, Integer> version;
	public static volatile ListAttribute<MRegionType, MEstimateUnit> MEstimateUnits;
	public static volatile ListAttribute<MRegionType, MMunicipality> MMunicipalities;
}
