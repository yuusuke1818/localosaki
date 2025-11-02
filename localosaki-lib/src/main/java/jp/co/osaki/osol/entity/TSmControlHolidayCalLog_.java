package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-05T10:57:32.028+0900")
@StaticMetamodel(TSmControlHolidayCalLog.class)
public class TSmControlHolidayCalLog_ {
	public static volatile SingularAttribute<TSmControlHolidayCalLog, TSmControlHolidayCalLogPK> id;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, Integer> version;
	public static volatile SingularAttribute<TSmControlHolidayCalLog, TSmControlHolidayLog> TSmControlHolidayLog;
}
