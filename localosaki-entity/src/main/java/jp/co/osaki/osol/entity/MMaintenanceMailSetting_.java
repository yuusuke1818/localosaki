package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-05-07T16:50:19.111+0900")
@StaticMetamodel(MMaintenanceMailSetting.class)
public class MMaintenanceMailSetting_ {
	public static volatile SingularAttribute<MMaintenanceMailSetting, MMaintenanceMailSettingPK> id;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Timestamp> createDate;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Long> createUserId;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Integer> defaultDestinationFlg;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Integer> delFlg;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Integer> deliveryStopFlg;
	public static volatile SingularAttribute<MMaintenanceMailSetting, String> destinationType;
	public static volatile SingularAttribute<MMaintenanceMailSetting, String> maintenanceMailAddress;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Timestamp> updateDate;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Long> updateUserId;
	public static volatile SingularAttribute<MMaintenanceMailSetting, Integer> version;
	public static volatile SingularAttribute<MMaintenanceMailSetting, MCorp> MCorp;
}
