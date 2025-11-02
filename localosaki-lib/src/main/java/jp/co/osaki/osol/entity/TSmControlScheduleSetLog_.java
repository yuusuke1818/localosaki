package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-04T14:01:32.325+0900")
@StaticMetamodel(TSmControlScheduleSetLog.class)
public class TSmControlScheduleSetLog_ {
	public static volatile SingularAttribute<TSmControlScheduleSetLog, TSmControlScheduleSetLogPK> id;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Timestamp> createDate;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Long> createUserId;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> fridayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> holidayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> mondayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> saturdayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate1;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate10;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate2;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate3;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate4;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate5;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate6;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate7;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate8;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> specificDate9;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo1;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo10;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo2;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo3;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo4;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo5;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo6;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo7;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo8;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> specificDatePatternNo9;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> sundayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> thursdayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> tuesdayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Long> updateUserId;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, Integer> version;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, String> wednesdayPatternNo;
	public static volatile SingularAttribute<TSmControlScheduleSetLog, TSmControlScheduleLog> TSmControlScheduleLog;
}
