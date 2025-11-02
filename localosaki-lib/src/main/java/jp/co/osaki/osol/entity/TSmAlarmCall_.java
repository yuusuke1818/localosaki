package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-05-07T16:50:19.564+0900")
@StaticMetamodel(TSmAlarmCall.class)
public class TSmAlarmCall_ {
	public static volatile SingularAttribute<TSmAlarmCall, Long> smId;
	public static volatile SingularAttribute<TSmAlarmCall, Timestamp> createDate;
	public static volatile SingularAttribute<TSmAlarmCall, Long> createUserId;
	public static volatile SingularAttribute<TSmAlarmCall, Timestamp> smAlarmCallDate;
	public static volatile SingularAttribute<TSmAlarmCall, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmAlarmCall, Long> updateUserId;
	public static volatile SingularAttribute<TSmAlarmCall, Integer> version;
	public static volatile SingularAttribute<TSmAlarmCall, MSmPrm> MSmPrm;
}
