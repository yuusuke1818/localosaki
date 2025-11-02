package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.117+0900")
@StaticMetamodel(TMaintenanceFacility.class)
public class TMaintenanceFacility_ {
	public static volatile SingularAttribute<TMaintenanceFacility, TMaintenanceFacilityPK> id;
	public static volatile SingularAttribute<TMaintenanceFacility, Timestamp> createDate;
	public static volatile SingularAttribute<TMaintenanceFacility, Long> createUserId;
	public static volatile SingularAttribute<TMaintenanceFacility, Integer> delFlg;
	public static volatile SingularAttribute<TMaintenanceFacility, Integer> exclusionFlg;
	public static volatile SingularAttribute<TMaintenanceFacility, Timestamp> updateDate;
	public static volatile SingularAttribute<TMaintenanceFacility, Long> updateUserId;
	public static volatile SingularAttribute<TMaintenanceFacility, Integer> version;
	public static volatile SingularAttribute<TMaintenanceFacility, MFacility> MFacility;
	public static volatile SingularAttribute<TMaintenanceFacility, TMaintenanceRequestHistory> TMaintenanceRequestHistory;
}
