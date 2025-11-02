package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.402+0900")
@StaticMetamodel(MLineTargetAlarmTimePK.class)
public class MLineTargetAlarmTimePK_ {
	public static volatile SingularAttribute<MLineTargetAlarmTimePK, String> corpId;
	public static volatile SingularAttribute<MLineTargetAlarmTimePK, Long> buildingId;
	public static volatile SingularAttribute<MLineTargetAlarmTimePK, Long> lineGroupId;
	public static volatile SingularAttribute<MLineTargetAlarmTimePK, String> lineNo;
	public static volatile SingularAttribute<MLineTargetAlarmTimePK, BigDecimal> jigenNo;
}
