package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-09T19:25:44.659+0900")
@StaticMetamodel(MLineTargetAlarm.class)
public class MLineTargetAlarm_ {
	public static volatile SingularAttribute<MLineTargetAlarm, MLineTargetAlarmPK> id;
	public static volatile SingularAttribute<MLineTargetAlarm, BigDecimal> continueDays;
	public static volatile SingularAttribute<MLineTargetAlarm, Timestamp> createDate;
	public static volatile SingularAttribute<MLineTargetAlarm, Long> createUserId;
	public static volatile SingularAttribute<MLineTargetAlarm, Integer> delFlg;
	public static volatile SingularAttribute<MLineTargetAlarm, BigDecimal> lineOverRate;
	public static volatile SingularAttribute<MLineTargetAlarm, Integer> overRateDetectFlg;
	public static volatile SingularAttribute<MLineTargetAlarm, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineTargetAlarm, Long> updateUserId;
	public static volatile SingularAttribute<MLineTargetAlarm, Integer> version;
	public static volatile SingularAttribute<MLineTargetAlarm, MBuildingDm> MBuildingDm;
	public static volatile SingularAttribute<MLineTargetAlarm, MLine> MLine;
	public static volatile ListAttribute<MLineTargetAlarm, MLineTargetAlarmTime> MLineTargetAlarmTimes;
}
