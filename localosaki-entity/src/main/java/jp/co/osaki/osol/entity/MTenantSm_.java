package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-10-07T10:18:23.573+0900")
@StaticMetamodel(MTenantSm.class)
public class MTenantSm_ {
	public static volatile SingularAttribute<MTenantSm, MTenantSmPK> id;
	public static volatile SingularAttribute<MTenantSm, String> address2;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> contractCapacity;
	public static volatile SingularAttribute<MTenantSm, Timestamp> createDate;
	public static volatile SingularAttribute<MTenantSm, Long> createUserId;
	public static volatile SingularAttribute<MTenantSm, Integer> delFlg;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate1;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate10;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate2;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate3;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate4;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate5;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate6;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate7;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate8;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> divRate9;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> elecHomeDisc;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> enerEquipDiscCapacity;
	public static volatile SingularAttribute<MTenantSm, String> fixed1Name;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> fixed1Price;
	public static volatile SingularAttribute<MTenantSm, String> fixed2Name;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> fixed2Price;
	public static volatile SingularAttribute<MTenantSm, String> fixed3Name;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> fixed3Price;
	public static volatile SingularAttribute<MTenantSm, String> fixed4Name;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> fixed4Price;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> micomDiscCapacity;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> priceMenuNo;
	public static volatile SingularAttribute<MTenantSm, Timestamp> recDate;
	public static volatile SingularAttribute<MTenantSm, String> recMan;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type1ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type2ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type3ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type4ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type5ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type6ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type7ComDivrate;
	public static volatile SingularAttribute<MTenantSm, BigDecimal> type8ComDivrate;
	public static volatile SingularAttribute<MTenantSm, Timestamp> updateDate;
	public static volatile SingularAttribute<MTenantSm, Long> updateUserId;
	public static volatile SingularAttribute<MTenantSm, Integer> version;
	public static volatile SingularAttribute<MTenantSm, TBuilding> TBuilding;
	public static volatile SingularAttribute<MTenantSm, Long> tenantId;
}
