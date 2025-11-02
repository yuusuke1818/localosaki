package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-07-20T15:59:50.918+0900")
@StaticMetamodel(TDmYearRepLine.class)
public class TDmYearRepLine_ {
	public static volatile SingularAttribute<TDmYearRepLine, TDmYearRepLinePK> id;
	public static volatile SingularAttribute<TDmYearRepLine, Timestamp> createDate;
	public static volatile SingularAttribute<TDmYearRepLine, Long> createUserId;
	public static volatile SingularAttribute<TDmYearRepLine, BigDecimal> lineMaxKw;
	public static volatile SingularAttribute<TDmYearRepLine, BigDecimal> lineValueKwh;
	public static volatile SingularAttribute<TDmYearRepLine, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmYearRepLine, Long> updateUserId;
	public static volatile SingularAttribute<TDmYearRepLine, Integer> version;
	public static volatile SingularAttribute<TDmYearRepLine, MLine> MLine;
	public static volatile SingularAttribute<TDmYearRepLine, TDmYearRep> TDmYearRep;
}
