package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.411+0900")
@StaticMetamodel(MVarious.class)
public class MVarious_ {
	public static volatile SingularAttribute<MVarious, MVariousPK> id;
	public static volatile SingularAttribute<MVarious, Timestamp> createDate;
	public static volatile SingularAttribute<MVarious, Long> createUserId;
	public static volatile SingularAttribute<MVarious, String> decimalFraction;
	public static volatile SingularAttribute<MVarious, Timestamp> recDate;
	public static volatile SingularAttribute<MVarious, String> recMan;
	public static volatile SingularAttribute<MVarious, String> saleTaxDeal;
	public static volatile SingularAttribute<MVarious, BigDecimal> saleTaxRate;
	public static volatile SingularAttribute<MVarious, Timestamp> updateDate;
	public static volatile SingularAttribute<MVarious, Long> updateUserId;
	public static volatile SingularAttribute<MVarious, Integer> version;
	public static volatile SingularAttribute<MVarious, BigDecimal> yearCloseMonth;
	public static volatile SingularAttribute<MVarious, TBuilding> TBuilding;
}
