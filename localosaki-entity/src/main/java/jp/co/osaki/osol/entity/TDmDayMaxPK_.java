package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.507+0900")
@StaticMetamodel(TDmDayMaxPK.class)
public class TDmDayMaxPK_ {
	public static volatile SingularAttribute<TDmDayMaxPK, String> corpId;
	public static volatile SingularAttribute<TDmDayMaxPK, Long> buildingId;
	public static volatile SingularAttribute<TDmDayMaxPK, Long> smId;
	public static volatile SingularAttribute<TDmDayMaxPK, Date> measurementDate;
	public static volatile SingularAttribute<TDmDayMaxPK, BigDecimal> crntMin;
}
