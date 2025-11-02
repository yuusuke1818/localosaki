package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-01-23T10:14:09.893+0900")
@StaticMetamodel(TSmControlScheduleDutyLog.class)
public class TSmControlScheduleDutyLog_ {
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, TSmControlScheduleDutyLogPK> id;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, String> dutyControlTimeSlot;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Integer> dutyOffTimeMinute;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Integer> dutyOnTimeMinute;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, Integer> version;
	public static volatile SingularAttribute<TSmControlScheduleDutyLog, TSmControlScheduleLog> TSmControlScheduleLog;
}
