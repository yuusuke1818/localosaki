package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-07-20T15:59:50.922+0900")
@StaticMetamodel(TDmYearRepLinePK.class)
public class TDmYearRepLinePK_ {
	public static volatile SingularAttribute<TDmYearRepLinePK, String> corpId;
	public static volatile SingularAttribute<TDmYearRepLinePK, Long> buildingId;
	public static volatile SingularAttribute<TDmYearRepLinePK, String> yearNo;
	public static volatile SingularAttribute<TDmYearRepLinePK, BigDecimal> monthNo;
	public static volatile SingularAttribute<TDmYearRepLinePK, String> summaryUnit;
	public static volatile SingularAttribute<TDmYearRepLinePK, Long> lineGroupId;
	public static volatile SingularAttribute<TDmYearRepLinePK, String> lineNo;
}
