package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.539+0900")
@StaticMetamodel(TDmMonthRepLinePK.class)
public class TDmMonthRepLinePK_ {
	public static volatile SingularAttribute<TDmMonthRepLinePK, String> corpId;
	public static volatile SingularAttribute<TDmMonthRepLinePK, Long> buildingId;
	public static volatile SingularAttribute<TDmMonthRepLinePK, Date> measurementDate;
	public static volatile SingularAttribute<TDmMonthRepLinePK, Long> lineGroupId;
	public static volatile SingularAttribute<TDmMonthRepLinePK, String> lineNo;
}
