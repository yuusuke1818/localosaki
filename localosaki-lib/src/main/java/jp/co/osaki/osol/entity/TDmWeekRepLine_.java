package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.556+0900")
@StaticMetamodel(TDmWeekRepLine.class)
public class TDmWeekRepLine_ {
	public static volatile SingularAttribute<TDmWeekRepLine, TDmWeekRepLinePK> id;
	public static volatile SingularAttribute<TDmWeekRepLine, Timestamp> createDate;
	public static volatile SingularAttribute<TDmWeekRepLine, Long> createUserId;
	public static volatile SingularAttribute<TDmWeekRepLine, BigDecimal> lineMaxKw;
	public static volatile SingularAttribute<TDmWeekRepLine, BigDecimal> lineValueKwh;
	public static volatile SingularAttribute<TDmWeekRepLine, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmWeekRepLine, Long> updateUserId;
	public static volatile SingularAttribute<TDmWeekRepLine, Integer> version;
	public static volatile SingularAttribute<TDmWeekRepLine, MLine> MLine;
	public static volatile SingularAttribute<TDmWeekRepLine, TDmWeekRep> TDmWeekRep;
}
