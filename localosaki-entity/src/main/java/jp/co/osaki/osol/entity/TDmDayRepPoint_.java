package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.524+0900")
@StaticMetamodel(TDmDayRepPoint.class)
public class TDmDayRepPoint_ {
    public static volatile SingularAttribute<TDmDayRepPoint, TDmDayRepPointPK> id;
    public static volatile SingularAttribute<TDmDayRepPoint, Timestamp> createDate;
    public static volatile SingularAttribute<TDmDayRepPoint, Long> createUserId;
    public static volatile SingularAttribute<TDmDayRepPoint, BigDecimal> pointVal;
    public static volatile SingularAttribute<TDmDayRepPoint, Timestamp> updateDate;
    public static volatile SingularAttribute<TDmDayRepPoint, Long> updateUserId;
    public static volatile SingularAttribute<TDmDayRepPoint, Integer> version;
    public static volatile SingularAttribute<TDmDayRepPoint, MBuildingSmPoint> MBuildingSmPoint;
    public static volatile SingularAttribute<TDmDayRepPoint, TDmDayRep> TDmDayRep;
}
