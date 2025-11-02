package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.260+0900")
@StaticMetamodel(MMeterLoadlimit.class)
public class MMeterLoadlimit_ {
	public static volatile SingularAttribute<MMeterLoadlimit, MMeterLoadlimitPK> id;
	public static volatile SingularAttribute<MMeterLoadlimit, String> autoInjection;
	public static volatile SingularAttribute<MMeterLoadlimit, String> breakerActCount;
	public static volatile SingularAttribute<MMeterLoadlimit, String> commandFlg;
	public static volatile SingularAttribute<MMeterLoadlimit, String> countClear;
	public static volatile SingularAttribute<MMeterLoadlimit, Timestamp> createDate;
	public static volatile SingularAttribute<MMeterLoadlimit, Long> createUserId;
	public static volatile SingularAttribute<MMeterLoadlimit, String> loadCurrent;
	public static volatile SingularAttribute<MMeterLoadlimit, String> loadlimitMode;
	public static volatile SingularAttribute<MMeterLoadlimit, Timestamp> recDate;
	public static volatile SingularAttribute<MMeterLoadlimit, String> recMan;
	public static volatile SingularAttribute<MMeterLoadlimit, String> srvEnt;
	public static volatile SingularAttribute<MMeterLoadlimit, String> tempAutoInjection;
	public static volatile SingularAttribute<MMeterLoadlimit, String> tempBreakerActCount;
	public static volatile SingularAttribute<MMeterLoadlimit, String> tempCountClear;
	public static volatile SingularAttribute<MMeterLoadlimit, String> tempLoadCurrent;
	public static volatile SingularAttribute<MMeterLoadlimit, Timestamp> updateDate;
	public static volatile SingularAttribute<MMeterLoadlimit, Long> updateUserId;
	public static volatile SingularAttribute<MMeterLoadlimit, Integer> version;
	public static volatile SingularAttribute<MMeterLoadlimit, MMeter> MMeter;
}
