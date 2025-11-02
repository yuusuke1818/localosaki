package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-01-23T16:40:45.165+0900")
@StaticMetamodel(TLoadControlResult.class)
public class TLoadControlResult_ {
	public static volatile SingularAttribute<TLoadControlResult, TLoadControlResultPK> id;
	public static volatile SingularAttribute<TLoadControlResult, Timestamp> createDate;
	public static volatile SingularAttribute<TLoadControlResult, Long> createUserId;
	public static volatile SingularAttribute<TLoadControlResult, BigDecimal> dailyTotalMinute;
	public static volatile SingularAttribute<TLoadControlResult, String> measurementYmdhm;
	public static volatile SingularAttribute<TLoadControlResult, Timestamp> updateDate;
	public static volatile SingularAttribute<TLoadControlResult, Long> updateUserId;
	public static volatile SingularAttribute<TLoadControlResult, Integer> version;
	public static volatile SingularAttribute<TLoadControlResult, MSmPrm> MSmPrm;
}
