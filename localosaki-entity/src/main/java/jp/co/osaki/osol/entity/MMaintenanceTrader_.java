package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.922+0900")
@StaticMetamodel(MMaintenanceTrader.class)
public class MMaintenanceTrader_ {
	public static volatile SingularAttribute<MMaintenanceTrader, MMaintenanceTraderPK> id;
	public static volatile SingularAttribute<MMaintenanceTrader, Timestamp> createDate;
	public static volatile SingularAttribute<MMaintenanceTrader, Long> createUserId;
	public static volatile SingularAttribute<MMaintenanceTrader, Integer> delFlg;
	public static volatile SingularAttribute<MMaintenanceTrader, String> maintenanceFaxNo;
	public static volatile SingularAttribute<MMaintenanceTrader, String> maintenanceMailAddress;
	public static volatile SingularAttribute<MMaintenanceTrader, String> maintenanceTelNo;
	public static volatile SingularAttribute<MMaintenanceTrader, String> maintenanceTraderName;
	public static volatile SingularAttribute<MMaintenanceTrader, String> maintenanceTraderPersonName;
	public static volatile SingularAttribute<MMaintenanceTrader, Timestamp> updateDate;
	public static volatile SingularAttribute<MMaintenanceTrader, Long> updateUserId;
	public static volatile SingularAttribute<MMaintenanceTrader, Integer> version;
	public static volatile SingularAttribute<MMaintenanceTrader, MCorp> MCorp;
}
