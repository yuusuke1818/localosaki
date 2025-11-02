package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-18T13:54:34.012+0900")
@StaticMetamodel(MCorpTargetAlarm.class)
public class MCorpTargetAlarm_ {
	public static volatile SingularAttribute<MCorpTargetAlarm, String> corpId;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress1;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress10;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress2;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress3;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress4;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress5;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress6;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress7;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress8;
	public static volatile SingularAttribute<MCorpTargetAlarm, String> alertMailAddress9;
	public static volatile SingularAttribute<MCorpTargetAlarm, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, Long> createUserId;
	public static volatile SingularAttribute<MCorpTargetAlarm, Integer> delFlg;
	public static volatile SingularAttribute<MCorpTargetAlarm, Time> detectTime;
	public static volatile SingularAttribute<MCorpTargetAlarm, Timestamp> mailLastSendDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, Time> mailWillSendTime;
	public static volatile SingularAttribute<MCorpTargetAlarm, Integer> monthlyAlarmFlg;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> monthlyAlarmLockDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, Integer> periodAlarmFlg;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> periodAlarmLockDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> sharePeriod;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth1;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth10;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth11;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth12;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth2;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth3;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth4;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth5;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth6;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth7;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth8;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonth9;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhMonthlyOverRate;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhPeriodOverRate;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> targetKwhYearOverRate;
	public static volatile SingularAttribute<MCorpTargetAlarm, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, Long> updateUserId;
	public static volatile SingularAttribute<MCorpTargetAlarm, Integer> version;
	public static volatile SingularAttribute<MCorpTargetAlarm, Integer> yearAlarmFlg;
	public static volatile SingularAttribute<MCorpTargetAlarm, BigDecimal> yearAlarmLockDate;
	public static volatile SingularAttribute<MCorpTargetAlarm, MCorpDm> MCorpDm;
}
