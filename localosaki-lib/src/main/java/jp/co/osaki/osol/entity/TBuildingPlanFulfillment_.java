package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-22T14:07:33.254+0900")
@StaticMetamodel(TBuildingPlanFulfillment.class)
public class TBuildingPlanFulfillment_ {
	public static volatile SingularAttribute<TBuildingPlanFulfillment, TBuildingPlanFulfillmentPK> id;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> createUserId;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> prevMonthAchieveCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> prevMonthInputCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> prevMonthNeedCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> prevMonthTargetInputCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> thisMonthAchieveCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> thisMonthInputCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> thisMonthNeedCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> thisMonthTargetInputCount;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, Integer> version;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, TBuilding> TBuilding;
	public static volatile SingularAttribute<TBuildingPlanFulfillment, TPlanFulfillmentInfo> TPlanFulfillmentInfo;
	public static volatile ListAttribute<TBuildingPlanFulfillment, TPlanFulfillmentResult> TPlanFulfillmentResults;
}
