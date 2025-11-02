package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-09T19:25:44.683+0900")
@StaticMetamodel(TDmDayMax.class)
public class TDmDayMax_ {
	public static volatile SingularAttribute<TDmDayMax, TDmDayMaxPK> id;
	public static volatile SingularAttribute<TDmDayMax, Timestamp> createDate;
	public static volatile SingularAttribute<TDmDayMax, Long> createUserId;
	public static volatile SingularAttribute<TDmDayMax, BigDecimal> kwVal;
	public static volatile SingularAttribute<TDmDayMax, Timestamp> kwValUpdateTime;
	public static volatile SingularAttribute<TDmDayMax, BigDecimal> plotAnalogPointNo1Val;
	public static volatile SingularAttribute<TDmDayMax, BigDecimal> plotAnalogPointNo2Val;
	public static volatile SingularAttribute<TDmDayMax, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmDayMax, Long> updateUserId;
	public static volatile SingularAttribute<TDmDayMax, Integer> version;
	public static volatile ListAttribute<TDmDayMax, TDmDayLoad> TDmDayLoads;
	public static volatile SingularAttribute<TDmDayMax, MBuildingSm> MBuildingSm;
}
