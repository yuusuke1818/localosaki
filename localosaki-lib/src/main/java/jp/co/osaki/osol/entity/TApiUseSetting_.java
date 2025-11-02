package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-10T14:38:53.506+0900")
@StaticMetamodel(TApiUseSetting.class)
public class TApiUseSetting_ {
	public static volatile SingularAttribute<TApiUseSetting, TApiUseSettingPK> id;
	public static volatile SingularAttribute<TApiUseSetting, String> closingDay;
	public static volatile SingularAttribute<TApiUseSetting, Integer> closingDaySettingFlg;
	public static volatile SingularAttribute<TApiUseSetting, Timestamp> createDate;
	public static volatile SingularAttribute<TApiUseSetting, Long> createUserId;
	public static volatile SingularAttribute<TApiUseSetting, Integer> maxVal;
	public static volatile SingularAttribute<TApiUseSetting, Integer> predictionVal;
	public static volatile SingularAttribute<TApiUseSetting, Timestamp> updateDate;
	public static volatile SingularAttribute<TApiUseSetting, Long> updateUserId;
	public static volatile SingularAttribute<TApiUseSetting, Integer> version;
	public static volatile SingularAttribute<TApiUseSetting, MCorp> MCorp;
}
