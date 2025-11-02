package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.586+0900")
@StaticMetamodel(TDmYearRepPK.class)
public class TDmYearRepPK_ {
	public static volatile SingularAttribute<TDmYearRepPK, String> corpId;
	public static volatile SingularAttribute<TDmYearRepPK, Long> buildingId;
	public static volatile SingularAttribute<TDmYearRepPK, String> yearNo;
	public static volatile SingularAttribute<TDmYearRepPK, BigDecimal> monthNo;
	public static volatile SingularAttribute<TDmYearRepPK, String> summaryUnit;
}
