package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.312+0900")
@StaticMetamodel(MPriceMenuLighta.class)
public class MPriceMenuLighta_ {
	public static volatile SingularAttribute<MPriceMenuLighta, MPriceMenuLightaPK> id;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> adjustPriceOver15;
	public static volatile SingularAttribute<MPriceMenuLighta, Timestamp> createDate;
	public static volatile SingularAttribute<MPriceMenuLighta, Long> createUserId;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> fuelAdjustPrice;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> lowestPrice;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> powerPriceOver300;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> powerPriceTo120;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> powerPriceTo300;
	public static volatile SingularAttribute<MPriceMenuLighta, Timestamp> recDate;
	public static volatile SingularAttribute<MPriceMenuLighta, String> recMan;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> renewEnerPrice;
	public static volatile SingularAttribute<MPriceMenuLighta, BigDecimal> renewPriceOver15;
	public static volatile SingularAttribute<MPriceMenuLighta, Timestamp> updateDate;
	public static volatile SingularAttribute<MPriceMenuLighta, Long> updateUserId;
	public static volatile SingularAttribute<MPriceMenuLighta, Integer> version;
	public static volatile SingularAttribute<MPriceMenuLighta, TBuilding> TBuilding;
}
