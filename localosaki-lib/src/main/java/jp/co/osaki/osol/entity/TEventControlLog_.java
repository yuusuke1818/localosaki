package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-12T09:57:08.092+0900")
@StaticMetamodel(TEventControlLog.class)
public class TEventControlLog_ {
	public static volatile SingularAttribute<TEventControlLog, TEventControlLogPK> id;
	public static volatile SingularAttribute<TEventControlLog, String> controlEvent1Kind;
	public static volatile SingularAttribute<TEventControlLog, BigDecimal> controlEvent1Val;
	public static volatile SingularAttribute<TEventControlLog, String> controlEvent2Kind;
	public static volatile SingularAttribute<TEventControlLog, BigDecimal> controlEvent2Val;
	public static volatile SingularAttribute<TEventControlLog, String> controlEvent3Kind;
	public static volatile SingularAttribute<TEventControlLog, BigDecimal> controlEvent3Val;
	public static volatile SingularAttribute<TEventControlLog, String> controlStatus;
	public static volatile SingularAttribute<TEventControlLog, Timestamp> createDate;
	public static volatile SingularAttribute<TEventControlLog, Long> createUserId;
	public static volatile SingularAttribute<TEventControlLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TEventControlLog, Long> updateUserId;
	public static volatile SingularAttribute<TEventControlLog, Integer> version;
	public static volatile SingularAttribute<TEventControlLog, MSmPrm> MSmPrm;
}
