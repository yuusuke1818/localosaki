package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.499+0900")
@StaticMetamodel(TDmDayLoadPK.class)
public class TDmDayLoadPK_ {
	public static volatile SingularAttribute<TDmDayLoadPK, String> corpId;
	public static volatile SingularAttribute<TDmDayLoadPK, Long> buildingId;
	public static volatile SingularAttribute<TDmDayLoadPK, Long> smId;
	public static volatile SingularAttribute<TDmDayLoadPK, Date> measurementDate;
	public static volatile SingularAttribute<TDmDayLoadPK, BigDecimal> crntMin;
	public static volatile SingularAttribute<TDmDayLoadPK, BigDecimal> controlLoad;
}
