package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-13T16:21:52.026+0900")
@StaticMetamodel(TSmControlScheduleTimeLog.class)
public class TSmControlScheduleTimeLog_ {
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, TSmControlScheduleTimeLogPK> id;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Integer> endTimeHour;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Integer> endTimeMinute;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Integer> startTimeHour;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Integer> startTimeMinute;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, Integer> version;
	public static volatile SingularAttribute<TSmControlScheduleTimeLog, TSmControlScheduleLog> TSmControlScheduleLog;
}
