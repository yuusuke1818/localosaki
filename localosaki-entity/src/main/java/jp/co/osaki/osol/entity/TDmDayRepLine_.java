package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.513+0900")
@StaticMetamodel(TDmDayRepLine.class)
public class TDmDayRepLine_ {
	public static volatile SingularAttribute<TDmDayRepLine, TDmDayRepLinePK> id;
	public static volatile SingularAttribute<TDmDayRepLine, Timestamp> createDate;
	public static volatile SingularAttribute<TDmDayRepLine, Long> createUserId;
	public static volatile SingularAttribute<TDmDayRepLine, BigDecimal> lineLimitStandardKw;
	public static volatile SingularAttribute<TDmDayRepLine, BigDecimal> lineLowerStandardKw;
	public static volatile SingularAttribute<TDmDayRepLine, BigDecimal> lineValueKw;
	public static volatile SingularAttribute<TDmDayRepLine, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmDayRepLine, Long> updateUserId;
	public static volatile SingularAttribute<TDmDayRepLine, Integer> version;
	public static volatile SingularAttribute<TDmDayRepLine, MLine> MLine;
	public static volatile SingularAttribute<TDmDayRepLine, TDmDayRep> TDmDayRep;
}
