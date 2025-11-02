package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.571+0900")
@StaticMetamodel(TDmWeekRepPointPK.class)
public class TDmWeekRepPointPK_ {
	public static volatile SingularAttribute<TDmWeekRepPointPK, String> corpId;
	public static volatile SingularAttribute<TDmWeekRepPointPK, Long> buildingId;
	public static volatile SingularAttribute<TDmWeekRepPointPK, Long> smId;
	public static volatile SingularAttribute<TDmWeekRepPointPK, String> fiscalYear;
	public static volatile SingularAttribute<TDmWeekRepPointPK, BigDecimal> weekNo;
	public static volatile SingularAttribute<TDmWeekRepPointPK, String> summaryUnit;
	public static volatile SingularAttribute<TDmWeekRepPointPK, String> pointNo;
}
