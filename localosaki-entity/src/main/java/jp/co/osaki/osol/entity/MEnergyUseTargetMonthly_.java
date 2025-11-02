package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.842+0900")
@StaticMetamodel(MEnergyUseTargetMonthly.class)
public class MEnergyUseTargetMonthly_ {
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, MEnergyUseTargetMonthlyPK> id;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Timestamp> createDate;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Long> createUserId;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Integer> delFlg;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, BigDecimal> targetBilledAmount;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, String> targetUseUnit1;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, String> targetUseUnit2;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, BigDecimal> targetUseValue1;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, BigDecimal> targetUseValue2;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Timestamp> updateDate;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Long> updateUserId;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, Integer> version;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, MEnergyType> MEnergyType;
	public static volatile SingularAttribute<MEnergyUseTargetMonthly, TBuilding> TBuilding;
}
