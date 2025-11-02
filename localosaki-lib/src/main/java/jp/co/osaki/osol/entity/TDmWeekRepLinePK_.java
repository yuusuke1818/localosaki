package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.560+0900")
@StaticMetamodel(TDmWeekRepLinePK.class)
public class TDmWeekRepLinePK_ {
	public static volatile SingularAttribute<TDmWeekRepLinePK, String> corpId;
	public static volatile SingularAttribute<TDmWeekRepLinePK, Long> buildingId;
	public static volatile SingularAttribute<TDmWeekRepLinePK, String> fiscalYear;
	public static volatile SingularAttribute<TDmWeekRepLinePK, BigDecimal> weekNo;
	public static volatile SingularAttribute<TDmWeekRepLinePK, String> summaryUnit;
	public static volatile SingularAttribute<TDmWeekRepLinePK, Long> lineGroupId;
	public static volatile SingularAttribute<TDmWeekRepLinePK, String> lineNo;
}
