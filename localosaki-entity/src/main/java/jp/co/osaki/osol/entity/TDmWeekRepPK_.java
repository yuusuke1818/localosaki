package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.564+0900")
@StaticMetamodel(TDmWeekRepPK.class)
public class TDmWeekRepPK_ {
	public static volatile SingularAttribute<TDmWeekRepPK, String> corpId;
	public static volatile SingularAttribute<TDmWeekRepPK, Long> buildingId;
	public static volatile SingularAttribute<TDmWeekRepPK, String> fiscalYear;
	public static volatile SingularAttribute<TDmWeekRepPK, BigDecimal> weekNo;
	public static volatile SingularAttribute<TDmWeekRepPK, String> summaryUnit;
}
