package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:58.066+0900")
@StaticMetamodel(TCommandLoadSurveyTime.class)
public class TCommandLoadSurveyTime_ {
	public static volatile SingularAttribute<TCommandLoadSurveyTime, TCommandLoadSurveyTimePK> id;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Timestamp> createDate;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Long> createUserId;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Timestamp> recDate;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, String> recMan;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, String> srvEnt;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Timestamp> updateDate;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Long> updateUserId;
	public static volatile SingularAttribute<TCommandLoadSurveyTime, Integer> version;
}
