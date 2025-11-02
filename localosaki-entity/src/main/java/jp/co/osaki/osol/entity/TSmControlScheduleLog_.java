package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-13T16:21:52.011+0900")
@StaticMetamodel(TSmControlScheduleLog.class)
public class TSmControlScheduleLog_ {
	public static volatile SingularAttribute<TSmControlScheduleLog, TSmControlScheduleLogPK> id;
	public static volatile SingularAttribute<TSmControlScheduleLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlScheduleLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlScheduleLog, Integer> scheduleManageDesignationFlg;
	public static volatile SingularAttribute<TSmControlScheduleLog, Timestamp> settingUpdateDatetime;
	public static volatile SingularAttribute<TSmControlScheduleLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlScheduleLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlScheduleLog, Integer> version;
	public static volatile SingularAttribute<TSmControlScheduleLog, MSmPrm> MSmPrm;
    public static volatile ListAttribute<TSmControlScheduleLog, TSmControlScheduleDutyLog> TSmControlScheduleDutyLogs;
	public static volatile ListAttribute<TSmControlScheduleLog, TSmControlScheduleSetLog> TSmControlScheduleSetLogs;
    public static volatile ListAttribute<TSmControlScheduleLog, TSmControlScheduleTimeLog> TSmControlScheduleTimeLogs;
}
