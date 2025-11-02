package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-26T17:25:22.660+0900")
@StaticMetamodel(TDayLoadSurveyMaxDemandRev.class)
public class TDayLoadSurveyMaxDemandRev_ {
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, TDayLoadSurveyMaxDemandRevPK> id;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Timestamp> createDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Long> createUserId;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, BigDecimal> maxDemand;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Timestamp> recDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, String> recMan;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Timestamp> updateDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Long> updateUserId;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemandRev, Integer> version;
}
