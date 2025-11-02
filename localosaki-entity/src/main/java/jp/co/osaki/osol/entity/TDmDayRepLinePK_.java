package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.517+0900")
@StaticMetamodel(TDmDayRepLinePK.class)
public class TDmDayRepLinePK_ {
	public static volatile SingularAttribute<TDmDayRepLinePK, String> corpId;
	public static volatile SingularAttribute<TDmDayRepLinePK, Long> buildingId;
	public static volatile SingularAttribute<TDmDayRepLinePK, Date> measurementDate;
	public static volatile SingularAttribute<TDmDayRepLinePK, BigDecimal> jigenNo;
	public static volatile SingularAttribute<TDmDayRepLinePK, Long> lineGroupId;
	public static volatile SingularAttribute<TDmDayRepLinePK, String> lineNo;
}
