package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.356+0900")
@StaticMetamodel(MTenantPriceInfo.class)
public class MTenantPriceInfo_ {
	public static volatile SingularAttribute<MTenantPriceInfo, MTenantPriceInfoPK> id;
	public static volatile SingularAttribute<MTenantPriceInfo, BigDecimal> basicPrice;
	public static volatile SingularAttribute<MTenantPriceInfo, Timestamp> createDate;
	public static volatile SingularAttribute<MTenantPriceInfo, Long> createUserId;
	public static volatile SingularAttribute<MTenantPriceInfo, BigDecimal> discountRate;
	public static volatile SingularAttribute<MTenantPriceInfo, Timestamp> recDate;
	public static volatile SingularAttribute<MTenantPriceInfo, String> recMan;
	public static volatile SingularAttribute<MTenantPriceInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<MTenantPriceInfo, Long> updateUserId;
	public static volatile SingularAttribute<MTenantPriceInfo, Integer> version;
	public static volatile SingularAttribute<MTenantPriceInfo, TBuilding> TBuilding;
}
