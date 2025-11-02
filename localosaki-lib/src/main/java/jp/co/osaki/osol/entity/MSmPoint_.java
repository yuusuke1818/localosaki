package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.440+0900")
@StaticMetamodel(MSmPoint.class)
public class MSmPoint_ {
    public static volatile SingularAttribute<MSmPoint, MSmPointPK> id;
    public static volatile SingularAttribute<MSmPoint, BigDecimal> analogConversionFactor;
    public static volatile SingularAttribute<MSmPoint, BigDecimal> analogOffSetValue;
    public static volatile SingularAttribute<MSmPoint, Timestamp> createDate;
    public static volatile SingularAttribute<MSmPoint, Long> createUserId;
    public static volatile SingularAttribute<MSmPoint, BigDecimal> dmCorrectionFactor;
    public static volatile SingularAttribute<MSmPoint, String> pointType;
    public static volatile SingularAttribute<MSmPoint, Timestamp> updateDate;
    public static volatile SingularAttribute<MSmPoint, Long> updateUserId;
    public static volatile SingularAttribute<MSmPoint, Integer> version;
    public static volatile ListAttribute<MSmPoint, MBuildingSmPoint> MBuildingSmPoints;
    public static volatile SingularAttribute<MSmPoint, MSmPrm> MSmPrm;
    public static volatile ListAttribute<MSmPoint, MSmPointEx> MSmPointExs;
    public static volatile ListAttribute<MSmPoint, TDmDayRepPoint> TDmDayRepPoints;
}
