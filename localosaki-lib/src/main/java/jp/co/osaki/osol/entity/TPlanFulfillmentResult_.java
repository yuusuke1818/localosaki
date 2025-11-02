package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-08-22T14:07:33.271+0900")
@StaticMetamodel(TPlanFulfillmentResult.class)
public class TPlanFulfillmentResult_ {
	public static volatile SingularAttribute<TPlanFulfillmentResult, TPlanFulfillmentResultPK> id;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Timestamp> createDate;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Long> createUserId;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Integer> delFlg;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Timestamp> planFulfillmentDate;
	public static volatile SingularAttribute<TPlanFulfillmentResult, String> planFulfillmentResult;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Timestamp> updateDate;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Long> updateUserId;
	public static volatile SingularAttribute<TPlanFulfillmentResult, Integer> version;
	public static volatile SingularAttribute<TPlanFulfillmentResult, TBuildingPlanFulfillment> TBuildingPlanFulfillment;
}
