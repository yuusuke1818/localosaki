package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-05T10:57:32.060+0900")
@StaticMetamodel(TSmControlHolidayLog.class)
public class TSmControlHolidayLog_ {
	public static volatile SingularAttribute<TSmControlHolidayLog, TSmControlHolidayLogPK> id;
	public static volatile SingularAttribute<TSmControlHolidayLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlHolidayLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlHolidayLog, Timestamp> settingUpdateDatetime;
	public static volatile SingularAttribute<TSmControlHolidayLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlHolidayLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlHolidayLog, Integer> version;
	public static volatile ListAttribute<TSmControlHolidayLog, TSmControlHolidayCalLog> TSmControlHolidayCalLogs;
	public static volatile SingularAttribute<TSmControlHolidayLog, MSmPrm> MSmPrm;
}
