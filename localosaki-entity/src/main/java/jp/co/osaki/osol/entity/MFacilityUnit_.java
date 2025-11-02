package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.900+0900")
@StaticMetamodel(MFacilityUnit.class)
public class MFacilityUnit_ {
	public static volatile SingularAttribute<MFacilityUnit, Long> facilityUnitId;
	public static volatile SingularAttribute<MFacilityUnit, Timestamp> createDate;
	public static volatile SingularAttribute<MFacilityUnit, Long> createUserId;
	public static volatile SingularAttribute<MFacilityUnit, Integer> displayOrder;
	public static volatile SingularAttribute<MFacilityUnit, String> facilityUnit;
	public static volatile SingularAttribute<MFacilityUnit, Timestamp> updateDate;
	public static volatile SingularAttribute<MFacilityUnit, Long> updateUserId;
	public static volatile SingularAttribute<MFacilityUnit, Integer> version;
	public static volatile ListAttribute<MFacilityUnit, MFacility> MFacilities1;
	public static volatile ListAttribute<MFacilityUnit, MFacility> MFacilities2;
}
