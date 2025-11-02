package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.332+0900")
@StaticMetamodel(MPriceMenuPattern.class)
public class MPriceMenuPattern_ {
	public static volatile SingularAttribute<MPriceMenuPattern, MPriceMenuPatternPK> id;
	public static volatile SingularAttribute<MPriceMenuPattern, Timestamp> createDate;
	public static volatile SingularAttribute<MPriceMenuPattern, Long> createUserId;
	public static volatile SingularAttribute<MPriceMenuPattern, String> endTime;
	public static volatile SingularAttribute<MPriceMenuPattern, BigDecimal> otherFlg;
	public static volatile SingularAttribute<MPriceMenuPattern, Timestamp> recDate;
	public static volatile SingularAttribute<MPriceMenuPattern, String> recMan;
	public static volatile SingularAttribute<MPriceMenuPattern, String> startTime;
	public static volatile SingularAttribute<MPriceMenuPattern, String> timeName;
	public static volatile SingularAttribute<MPriceMenuPattern, BigDecimal> timePrice;
	public static volatile SingularAttribute<MPriceMenuPattern, Timestamp> updateDate;
	public static volatile SingularAttribute<MPriceMenuPattern, Long> updateUserId;
	public static volatile SingularAttribute<MPriceMenuPattern, Integer> version;
	public static volatile SingularAttribute<MPriceMenuPattern, MPriceMenu> MPriceMenu;
}
