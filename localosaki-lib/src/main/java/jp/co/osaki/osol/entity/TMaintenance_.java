package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.104+0900")
@StaticMetamodel(TMaintenance.class)
public class TMaintenance_ {
	public static volatile SingularAttribute<TMaintenance, TMaintenancePK> id;
	public static volatile SingularAttribute<TMaintenance, Timestamp> createDate;
	public static volatile SingularAttribute<TMaintenance, Long> createUserId;
	public static volatile SingularAttribute<TMaintenance, Integer> delFlg;
	public static volatile SingularAttribute<TMaintenance, String> repeatInterval;
	public static volatile SingularAttribute<TMaintenance, Integer> repeatIntervalEditFlg;
	public static volatile SingularAttribute<TMaintenance, Date> requestPlanEndDate;
	public static volatile SingularAttribute<TMaintenance, Date> requestPlanStartDate;
	public static volatile SingularAttribute<TMaintenance, Timestamp> updateDate;
	public static volatile SingularAttribute<TMaintenance, Long> updateUserId;
	public static volatile SingularAttribute<TMaintenance, Integer> version;
	public static volatile SingularAttribute<TMaintenance, TBuilding> TBuilding;
	public static volatile ListAttribute<TMaintenance, TMaintenanceRequest> TMaintenanceRequests;
}
