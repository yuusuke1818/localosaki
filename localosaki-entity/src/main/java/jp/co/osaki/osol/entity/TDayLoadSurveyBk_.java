package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.467+0900")
@StaticMetamodel(TDayLoadSurveyBk.class)
public class TDayLoadSurveyBk_ {
	public static volatile SingularAttribute<TDayLoadSurveyBk, TDayLoadSurveyBkPK> id;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Timestamp> createDate;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Long> createUserId;
	public static volatile SingularAttribute<TDayLoadSurveyBk, BigDecimal> dmvKwh;
	public static volatile SingularAttribute<TDayLoadSurveyBk, BigDecimal> kwh30;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Timestamp> recDate;
	public static volatile SingularAttribute<TDayLoadSurveyBk, String> recMan;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Timestamp> updateDate;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Long> updateUserId;
	public static volatile SingularAttribute<TDayLoadSurveyBk, Integer> version;
}
