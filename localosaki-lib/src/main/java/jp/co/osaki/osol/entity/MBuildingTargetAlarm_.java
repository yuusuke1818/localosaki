package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-08-22T10:54:11.639+0900")
@StaticMetamodel(MBuildingTargetAlarm.class)
public class MBuildingTargetAlarm_ {
	public static volatile SingularAttribute<MBuildingTargetAlarm, MBuildingTargetAlarmPK> id;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertIntermalMailAddress1;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertIntermalMailAddress2;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress1;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress10;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress2;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress3;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress4;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress5;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress6;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress7;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress8;
	public static volatile SingularAttribute<MBuildingTargetAlarm, String> alertMailAddress9;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Timestamp> createDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Long> createUserId;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> delFlg;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> detectDayOfWeek;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Time> detectTime;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Timestamp> mailLastSendTime;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Time> mailWillSendTime;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> monthlyAlarmFlg;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> monthlyAlarmLockDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> periodAlarmFlg;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> periodAlarmLockDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> sharePeriod;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> targetKwhMonthlyOverDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> targetKwhPeriodOverDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> targetKwhYearOverDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Timestamp> updateDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Long> updateUserId;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> version;
	public static volatile SingularAttribute<MBuildingTargetAlarm, Integer> yearAlarmFlg;
	public static volatile SingularAttribute<MBuildingTargetAlarm, BigDecimal> yearAlarmLockDate;
	public static volatile SingularAttribute<MBuildingTargetAlarm, MBuildingDm> MBuildingDm;
}
