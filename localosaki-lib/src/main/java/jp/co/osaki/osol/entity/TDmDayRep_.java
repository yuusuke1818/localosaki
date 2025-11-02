package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.510+0900")
@StaticMetamodel(TDmDayRep.class)
public class TDmDayRep_ {
	public static volatile SingularAttribute<TDmDayRep, TDmDayRepPK> id;
	public static volatile SingularAttribute<TDmDayRep, Timestamp> createDate;
	public static volatile SingularAttribute<TDmDayRep, Long> createUserId;
	public static volatile SingularAttribute<TDmDayRep, BigDecimal> outAirTemp;
	public static volatile SingularAttribute<TDmDayRep, Date> recordDate;
	public static volatile SingularAttribute<TDmDayRep, String> sumDate;
	public static volatile SingularAttribute<TDmDayRep, BigDecimal> targetKw;
	public static volatile SingularAttribute<TDmDayRep, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmDayRep, Long> updateUserId;
	public static volatile SingularAttribute<TDmDayRep, Integer> version;
	public static volatile SingularAttribute<TDmDayRep, MBuildingDm> MBuildingDm;
	public static volatile ListAttribute<TDmDayRep, TDmDayRepLine> TDmDayRepLines;
	public static volatile ListAttribute<TDmDayRep, TDmDayRepPoint> TDmDayRepPoints;
    public static volatile ListAttribute<TDmDayRep, TDmDayRepPointInput> TDmDayRepPointInputs;
}
