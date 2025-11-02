package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-10T14:38:53.486+0900")
@StaticMetamodel(TApiAlertMailSetting.class)
public class TApiAlertMailSetting_ {
	public static volatile SingularAttribute<TApiAlertMailSetting, TApiAlertMailSettingPK> id;
	public static volatile SingularAttribute<TApiAlertMailSetting, Timestamp> createDate;
	public static volatile SingularAttribute<TApiAlertMailSetting, Long> createUserId;
	public static volatile SingularAttribute<TApiAlertMailSetting, Integer> defaultDestinationFlg;
	public static volatile SingularAttribute<TApiAlertMailSetting, Integer> deliveryStopFlg;
	public static volatile SingularAttribute<TApiAlertMailSetting, String> destinationType;
	public static volatile SingularAttribute<TApiAlertMailSetting, String> mailAddress;
	public static volatile SingularAttribute<TApiAlertMailSetting, Timestamp> updateDate;
	public static volatile SingularAttribute<TApiAlertMailSetting, Long> updateUserId;
	public static volatile SingularAttribute<TApiAlertMailSetting, Integer> version;
	public static volatile SingularAttribute<TApiAlertMailSetting, MCorp> MCorp;
}
