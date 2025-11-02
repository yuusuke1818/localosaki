package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.479+0900")
@StaticMetamodel(TDayLoadSurveyRev.class)
public class TDayLoadSurveyRev_ {
	public static volatile SingularAttribute<TDayLoadSurveyRev, TDayLoadSurveyRevPK> id;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Timestamp> createDate;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Long> createUserId;
	public static volatile SingularAttribute<TDayLoadSurveyRev, BigDecimal> dmvKwh;
	public static volatile SingularAttribute<TDayLoadSurveyRev, BigDecimal> kwh30;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Timestamp> recDate;
	public static volatile SingularAttribute<TDayLoadSurveyRev, String> recMan;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Timestamp> updateDate;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Long> updateUserId;
	public static volatile SingularAttribute<TDayLoadSurveyRev, Integer> version;
}
