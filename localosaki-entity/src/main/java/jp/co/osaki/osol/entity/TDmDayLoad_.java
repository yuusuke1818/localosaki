package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.495+0900")
@StaticMetamodel(TDmDayLoad.class)
public class TDmDayLoad_ {
	public static volatile SingularAttribute<TDmDayLoad, TDmDayLoadPK> id;
	public static volatile SingularAttribute<TDmDayLoad, String> contactOutStatus;
	public static volatile SingularAttribute<TDmDayLoad, Timestamp> createDate;
	public static volatile SingularAttribute<TDmDayLoad, Long> createUserId;
	public static volatile SingularAttribute<TDmDayLoad, Timestamp> updateDate;
	public static volatile SingularAttribute<TDmDayLoad, Long> updateUserId;
	public static volatile SingularAttribute<TDmDayLoad, Integer> version;
	public static volatile SingularAttribute<TDmDayLoad, TDmDayMax> TDmDayMax;
}
