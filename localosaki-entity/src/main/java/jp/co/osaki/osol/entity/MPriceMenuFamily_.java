package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.303+0900")
@StaticMetamodel(MPriceMenuFamily.class)
public class MPriceMenuFamily_ {
	public static volatile SingularAttribute<MPriceMenuFamily, MPriceMenuFamilyPK> id;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> basicPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> basicPriceOver;
	public static volatile SingularAttribute<MPriceMenuFamily, Timestamp> createDate;
	public static volatile SingularAttribute<MPriceMenuFamily, Long> createUserId;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> discountRate;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> enerEquipDiscPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> fuelAdjustPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> lowestMonthPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> micomDiscPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> powerPriceDayOther;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> powerPriceDaySummer;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> powerPriceFamily;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> powerPriceNight;
	public static volatile SingularAttribute<MPriceMenuFamily, Timestamp> recDate;
	public static volatile SingularAttribute<MPriceMenuFamily, String> recMan;
	public static volatile SingularAttribute<MPriceMenuFamily, BigDecimal> renewEnerPrice;
	public static volatile SingularAttribute<MPriceMenuFamily, Timestamp> updateDate;
	public static volatile SingularAttribute<MPriceMenuFamily, Long> updateUserId;
	public static volatile SingularAttribute<MPriceMenuFamily, Integer> version;
	public static volatile SingularAttribute<MPriceMenuFamily, TBuilding> TBuilding;
}
