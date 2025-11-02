package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-11-15T14:34:43.165+0900")
@StaticMetamodel(TDmDayRepPointInput.class)
public class TDmDayRepPointInput_ {
	public static volatile SingularAttribute<TDmDayRepPointInput, TDmDayRepPointInputPK> id;
	public static volatile SingularAttribute<TDmDayRepPointInput, Timestamp> createDate;
	public static volatile SingularAttribute<TDmDayRepPointInput, Long> createUserId;
	public static volatile SingularAttribute<TDmDayRepPointInput, BigDecimal> pointVal;
	public static volatile SingularAttribute<TDmDayRepPointInput, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmDayRepPointInput, Long> updateUserId;
	public static volatile SingularAttribute<TDmDayRepPointInput, Integer> version;
	public static volatile SingularAttribute<TDmDayRepPointInput, MBuildingSmPoint> MBuildingSmPoint;
	public static volatile SingularAttribute<TDmDayRepPointInput, TDmDayRep> TDmDayRep;
}
