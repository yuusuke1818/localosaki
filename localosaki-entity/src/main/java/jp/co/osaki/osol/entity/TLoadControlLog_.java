package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.776+0900")
@StaticMetamodel(TLoadControlLog.class)
public class TLoadControlLog_ {
	public static volatile SingularAttribute<TLoadControlLog, TLoadControlLogPK> id;
	public static volatile SingularAttribute<TLoadControlLog, BigDecimal> adjustKw;
	public static volatile SingularAttribute<TLoadControlLog, Timestamp> createDate;
	public static volatile SingularAttribute<TLoadControlLog, Long> createUserId;
	public static volatile SingularAttribute<TLoadControlLog, BigDecimal> nowKw;
	public static volatile SingularAttribute<TLoadControlLog, BigDecimal> targetKw;
	public static volatile SingularAttribute<TLoadControlLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TLoadControlLog, Long> updateUserId;
	public static volatile SingularAttribute<TLoadControlLog, Integer> version;
	public static volatile SingularAttribute<TLoadControlLog, MSmPrm> MSmPrm;
}
