package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.142+0900")
@StaticMetamodel(TMaintenanceRequest.class)
public class TMaintenanceRequest_ {
	public static volatile SingularAttribute<TMaintenanceRequest, TMaintenanceRequestPK> id;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> accrualDate;
	public static volatile SingularAttribute<TMaintenanceRequest, String> cause;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> completionDate;
	public static volatile SingularAttribute<TMaintenanceRequest, String> contactUs;
	public static volatile SingularAttribute<TMaintenanceRequest, Timestamp> createDate;
	public static volatile SingularAttribute<TMaintenanceRequest, Long> createUserId;
	public static volatile SingularAttribute<TMaintenanceRequest, Integer> delFlg;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> executePlanEndDate;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> executePlanStartDate;
	public static volatile SingularAttribute<TMaintenanceRequest, String> maintenanceFaxNo;
	public static volatile SingularAttribute<TMaintenanceRequest, String> maintenanceMailAddress;
	public static volatile SingularAttribute<TMaintenanceRequest, Long> maintenanceManageNo;
	public static volatile SingularAttribute<TMaintenanceRequest, String> maintenanceTelNo;
	public static volatile SingularAttribute<TMaintenanceRequest, String> maintenanceTraderName;
	public static volatile SingularAttribute<TMaintenanceRequest, String> maintenanceTraderPersonName;
	public static volatile SingularAttribute<TMaintenanceRequest, String> otherContactInfo;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> planningDate;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> requestDate;
	public static volatile SingularAttribute<TMaintenanceRequest, String> requestOverview;
	public static volatile SingularAttribute<TMaintenanceRequest, Date> requestRegistDate;
	public static volatile SingularAttribute<TMaintenanceRequest, String> requestStatus;
	public static volatile SingularAttribute<TMaintenanceRequest, String> requestType;
	public static volatile SingularAttribute<TMaintenanceRequest, String> situation;
	public static volatile SingularAttribute<TMaintenanceRequest, Timestamp> updateDate;
	public static volatile SingularAttribute<TMaintenanceRequest, Long> updateUserId;
	public static volatile SingularAttribute<TMaintenanceRequest, Integer> version;
	public static volatile ListAttribute<TMaintenanceRequest, TFreonFillingReport> TFreonFillingReports;
	public static volatile SingularAttribute<TMaintenanceRequest, MPerson> MPerson1;
	public static volatile SingularAttribute<TMaintenanceRequest, MPerson> MPerson2;
	public static volatile SingularAttribute<TMaintenanceRequest, TMaintenance> TMaintenance;
	public static volatile ListAttribute<TMaintenanceRequest, TMaintenanceRequestHistory> TMaintenanceRequestHistories;
}
