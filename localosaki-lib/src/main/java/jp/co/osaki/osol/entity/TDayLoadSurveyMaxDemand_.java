package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-26T14:22:03.817+0900")
@StaticMetamodel(TDayLoadSurveyMaxDemand.class)
public class TDayLoadSurveyMaxDemand_ {
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, TDayLoadSurveyMaxDemandPK> id;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Timestamp> createDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Long> createUserId;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, BigDecimal> maxDemand;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Timestamp> recDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, String> recMan;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Timestamp> updateDate;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Long> updateUserId;
    public static volatile SingularAttribute<TDayLoadSurveyMaxDemand, Integer> version;
}
