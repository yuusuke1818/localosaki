package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-10-11T11:30:58.118+0900")
@StaticMetamodel(TSmConnectControlSetting.class)
public class TSmConnectControlSetting_ {
	public static volatile SingularAttribute<TSmConnectControlSetting, Long> smConnectControlSettingId;
	public static volatile SingularAttribute<TSmConnectControlSetting, Timestamp> createDate;
	public static volatile SingularAttribute<TSmConnectControlSetting, Long> createUserId;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> parallelConnectControlMaxCount;
	public static volatile SingularAttribute<TSmConnectControlSetting, String> smConnectControlGroupName;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> smConnectRetryCount;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> smConnectWaitTime;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> socketConnectRetryCount;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> socketConnectWaitTime;
	public static volatile SingularAttribute<TSmConnectControlSetting, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmConnectControlSetting, Long> updateUserId;
	public static volatile SingularAttribute<TSmConnectControlSetting, Integer> version;
	public static volatile ListAttribute<TSmConnectControlSetting, TSmConnectControlProduct> TSmConnectControlProducts;
}
