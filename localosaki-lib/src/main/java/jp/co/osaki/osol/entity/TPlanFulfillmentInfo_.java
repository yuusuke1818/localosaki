package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-22T14:07:33.263+0900")
@StaticMetamodel(TPlanFulfillmentInfo.class)
public class TPlanFulfillmentInfo_ {
	public static volatile SingularAttribute<TPlanFulfillmentInfo, TPlanFulfillmentInfoPK> id;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Timestamp> createDate;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Long> createUserId;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> delFlg;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, String> planFulfillmentContents;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, String> planFulfillmentDateType;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Date> planFulfillmentEndDate;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, String> planFulfillmentName;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Date> planFulfillmentStartDate;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, String> planFulfillmentTarget;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyFriday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth1;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth10;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth11;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth12;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth2;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth3;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth4;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth5;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth6;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth7;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth8;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyMonth9;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifySaturday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifySunday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyThursday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyTuesday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> specifyWednesday;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Long> updateUserId;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, Integer> version;
	public static volatile ListAttribute<TPlanFulfillmentInfo, TBuildingPlanFulfillment> TBuildingPlanFulfillments;
	public static volatile SingularAttribute<TPlanFulfillmentInfo, MCorp> MCorp;
}
