package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.397+0900")
@StaticMetamodel(MLineTargetAlarmTime.class)
public class MLineTargetAlarmTime_ {
	public static volatile SingularAttribute<MLineTargetAlarmTime, MLineTargetAlarmTimePK> id;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Timestamp> createDate;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Long> createUserId;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Integer> delFlg;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Integer> detectFlg;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Long> updateUserId;
	public static volatile SingularAttribute<MLineTargetAlarmTime, Integer> version;
	public static volatile SingularAttribute<MLineTargetAlarmTime, MLineTargetAlarm> MLineTargetAlarm;
}
