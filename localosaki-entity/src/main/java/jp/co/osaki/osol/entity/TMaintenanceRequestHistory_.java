package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.147+0900")
@StaticMetamodel(TMaintenanceRequestHistory.class)
public class TMaintenanceRequestHistory_ {
	public static volatile SingularAttribute<TMaintenanceRequestHistory, TMaintenanceRequestHistoryPK> id;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, String> afterRequestStatus;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, String> beforeRequestStatus;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, String> comment;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Timestamp> createDate;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Long> createUserId;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Integer> delFlg;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, String> registCorpId;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, String> registPersonId;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Timestamp> updateDate;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Long> updateUserId;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, Integer> version;
	public static volatile ListAttribute<TMaintenanceRequestHistory, TMaintenanceDocument> TMaintenanceDocuments;
	public static volatile ListAttribute<TMaintenanceRequestHistory, TMaintenanceFacility> TMaintenanceFacilities;
	public static volatile SingularAttribute<TMaintenanceRequestHistory, TMaintenanceRequest> TMaintenanceRequest;
}
