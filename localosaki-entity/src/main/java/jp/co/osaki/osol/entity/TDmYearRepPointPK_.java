package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-07-20T15:59:50.933+0900")
@StaticMetamodel(TDmYearRepPointPK.class)
public class TDmYearRepPointPK_ {
	public static volatile SingularAttribute<TDmYearRepPointPK, String> corpId;
	public static volatile SingularAttribute<TDmYearRepPointPK, Long> buildingId;
	public static volatile SingularAttribute<TDmYearRepPointPK, Long> smId;
	public static volatile SingularAttribute<TDmYearRepPointPK, String> yearNo;
	public static volatile SingularAttribute<TDmYearRepPointPK, BigDecimal> monthNo;
	public static volatile SingularAttribute<TDmYearRepPointPK, String> summaryUnit;
	public static volatile SingularAttribute<TDmYearRepPointPK, String> pointNo;
}
