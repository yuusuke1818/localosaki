package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:58.050+0900")
@StaticMetamodel(TCommandLoadSurveyMeter.class)
public class TCommandLoadSurveyMeter_ {
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, TCommandLoadSurveyMeterPK> id;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Timestamp> createDate;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Long> createUserId;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Timestamp> recDate;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, String> recMan;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, String> srvEnt;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Timestamp> updateDate;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Long> updateUserId;
	public static volatile SingularAttribute<TCommandLoadSurveyMeter, Integer> version;
}
