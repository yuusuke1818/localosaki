package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-04T14:01:32.357+0900")
@StaticMetamodel(TSmControlScheduleSetLogPK.class)
public class TSmControlScheduleSetLogPK_ {
	public static volatile SingularAttribute<TSmControlScheduleSetLogPK, Long> smControlScheduleLogId;
	public static volatile SingularAttribute<TSmControlScheduleSetLogPK, Long> smId;
	public static volatile SingularAttribute<TSmControlScheduleSetLogPK, BigDecimal> controlLoad;
	public static volatile SingularAttribute<TSmControlScheduleSetLogPK, BigDecimal> targetMonth;
}
