package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:48:52.146+0900")
@StaticMetamodel(MMeterType.class)
public class MMeterType_ {
	public static volatile SingularAttribute<MMeterType, MMeterTypePK> id;
	public static volatile SingularAttribute<MMeterType, String> allComDiv;
	public static volatile SingularAttribute<MMeterType, String> autoInspDay;
	public static volatile SingularAttribute<MMeterType, String> autoInspHour;
	public static volatile SingularAttribute<MMeterType, BigDecimal> calcType;
	public static volatile SingularAttribute<MMeterType, BigDecimal> co2Coefficient;
	public static volatile SingularAttribute<MMeterType, Timestamp> createDate;
	public static volatile SingularAttribute<MMeterType, Long> createUserId;
	public static volatile SingularAttribute<MMeterType, String> meterTypeName;
	public static volatile SingularAttribute<MMeterType, Timestamp> recDate;
	public static volatile SingularAttribute<MMeterType, String> recMan;
	public static volatile SingularAttribute<MMeterType, String> unitCo2Coefficient;
	public static volatile SingularAttribute<MMeterType, BigDecimal> unitPrice;
	public static volatile SingularAttribute<MMeterType, String> unitUsageBased;
	public static volatile SingularAttribute<MMeterType, Timestamp> updateDate;
	public static volatile SingularAttribute<MMeterType, Long> updateUserId;
	public static volatile SingularAttribute<MMeterType, Integer> version;
	public static volatile ListAttribute<MMeterType, MMeterRange> MMeterRanges;
	public static volatile SingularAttribute<MMeterType, TBuilding> TBuilding;
}
