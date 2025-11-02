package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.535+0900")
@StaticMetamodel(TDmMonthRepLine.class)
public class TDmMonthRepLine_ {
	public static volatile SingularAttribute<TDmMonthRepLine, TDmMonthRepLinePK> id;
	public static volatile SingularAttribute<TDmMonthRepLine, Timestamp> createDate;
	public static volatile SingularAttribute<TDmMonthRepLine, Long> createUserId;
	public static volatile SingularAttribute<TDmMonthRepLine, BigDecimal> lineMaxKw;
	public static volatile SingularAttribute<TDmMonthRepLine, BigDecimal> lineValueKwh;
	public static volatile SingularAttribute<TDmMonthRepLine, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmMonthRepLine, Long> updateUserId;
	public static volatile SingularAttribute<TDmMonthRepLine, Integer> version;
	public static volatile SingularAttribute<TDmMonthRepLine, MLine> MLine;
	public static volatile SingularAttribute<TDmMonthRepLine, TDmMonthRep> TDmMonthRep;
}
