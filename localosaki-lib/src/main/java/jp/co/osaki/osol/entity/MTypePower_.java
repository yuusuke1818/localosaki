package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.378+0900")
@StaticMetamodel(MTypePower.class)
public class MTypePower_ {
	public static volatile SingularAttribute<MTypePower, MTypePowerPK> id;
	public static volatile SingularAttribute<MTypePower, Timestamp> createDate;
	public static volatile SingularAttribute<MTypePower, Long> createUserId;
	public static volatile SingularAttribute<MTypePower, BigDecimal> dayOtherPrice;
	public static volatile SingularAttribute<MTypePower, BigDecimal> daySummerPrice;
	public static volatile SingularAttribute<MTypePower, Timestamp> recDate;
	public static volatile SingularAttribute<MTypePower, String> recMan;
	public static volatile SingularAttribute<MTypePower, Timestamp> updateDate;
	public static volatile SingularAttribute<MTypePower, Long> updateUserId;
	public static volatile SingularAttribute<MTypePower, Integer> version;
	public static volatile SingularAttribute<MTypePower, MTypeStage> MTypeStage;
}
