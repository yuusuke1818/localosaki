package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.639+0900")
@StaticMetamodel(TWkLoadSurveyTest.class)
public class TWkLoadSurveyTest_ {
	public static volatile SingularAttribute<TWkLoadSurveyTest, TWkLoadSurveyTestPK> id;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Timestamp> createDate;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Long> createUserId;
	public static volatile SingularAttribute<TWkLoadSurveyTest, BigDecimal> dmvKwh;
	public static volatile SingularAttribute<TWkLoadSurveyTest, BigDecimal> dmvNone;
	public static volatile SingularAttribute<TWkLoadSurveyTest, BigDecimal> kwh30;
	public static volatile SingularAttribute<TWkLoadSurveyTest, BigDecimal> kwh30None;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Timestamp> recDate;
	public static volatile SingularAttribute<TWkLoadSurveyTest, String> recMan;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Timestamp> updateDate;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Long> updateUserId;
	public static volatile SingularAttribute<TWkLoadSurveyTest, Integer> version;
}
