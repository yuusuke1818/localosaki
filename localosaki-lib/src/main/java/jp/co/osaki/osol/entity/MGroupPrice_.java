package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.216+0900")
@StaticMetamodel(MGroupPrice.class)
public class MGroupPrice_ {
	public static volatile SingularAttribute<MGroupPrice, MGroupPricePK> id;
	public static volatile SingularAttribute<MGroupPrice, Timestamp> createDate;
	public static volatile SingularAttribute<MGroupPrice, Long> createUserId;
	public static volatile SingularAttribute<MGroupPrice, BigDecimal> groupPrice;
	public static volatile SingularAttribute<MGroupPrice, Timestamp> recDate;
	public static volatile SingularAttribute<MGroupPrice, String> recMan;
	public static volatile SingularAttribute<MGroupPrice, Timestamp> updateDate;
	public static volatile SingularAttribute<MGroupPrice, Long> updateUserId;
	public static volatile SingularAttribute<MGroupPrice, Integer> version;
	public static volatile SingularAttribute<MGroupPrice, MMeterGroupName> MMeterGroupName;
}
