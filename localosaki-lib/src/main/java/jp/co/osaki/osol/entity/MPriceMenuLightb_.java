package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.322+0900")
@StaticMetamodel(MPriceMenuLightb.class)
public class MPriceMenuLightb_ {
	public static volatile SingularAttribute<MPriceMenuLightb, MPriceMenuLightbPK> id;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> basicPrice;
	public static volatile SingularAttribute<MPriceMenuLightb, Timestamp> createDate;
	public static volatile SingularAttribute<MPriceMenuLightb, Long> createUserId;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> fuelAdjustPrice;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> powerPriceOver300;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> powerPriceTo120;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> powerPriceTo300;
	public static volatile SingularAttribute<MPriceMenuLightb, Timestamp> recDate;
	public static volatile SingularAttribute<MPriceMenuLightb, String> recMan;
	public static volatile SingularAttribute<MPriceMenuLightb, BigDecimal> renewEnerPrice;
	public static volatile SingularAttribute<MPriceMenuLightb, Timestamp> updateDate;
	public static volatile SingularAttribute<MPriceMenuLightb, Long> updateUserId;
	public static volatile SingularAttribute<MPriceMenuLightb, Integer> version;
	public static volatile SingularAttribute<MPriceMenuLightb, TBuilding> TBuilding;
}
