package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.463+0900")
@StaticMetamodel(TDayLoadSurvey.class)
public class TDayLoadSurvey_ {
	public static volatile SingularAttribute<TDayLoadSurvey, TDayLoadSurveyPK> id;
	public static volatile SingularAttribute<TDayLoadSurvey, Timestamp> createDate;
	public static volatile SingularAttribute<TDayLoadSurvey, Long> createUserId;
	public static volatile SingularAttribute<TDayLoadSurvey, BigDecimal> dmvKwh;
	public static volatile SingularAttribute<TDayLoadSurvey, BigDecimal> kwh30;
	public static volatile SingularAttribute<TDayLoadSurvey, Timestamp> recDate;
	public static volatile SingularAttribute<TDayLoadSurvey, String> recMan;
	public static volatile SingularAttribute<TDayLoadSurvey, Timestamp> updateDate;
	public static volatile SingularAttribute<TDayLoadSurvey, Long> updateUserId;
	public static volatile SingularAttribute<TDayLoadSurvey, Integer> version;
}
