package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-19T09:33:43.704+0900")
@StaticMetamodel(MMeterRange.class)
public class MMeterRange_ {
	public static volatile SingularAttribute<MMeterRange, MMeterRangePK> id;
	public static volatile SingularAttribute<MMeterRange, Timestamp> createDate;
	public static volatile SingularAttribute<MMeterRange, Long> createUserId;
	public static volatile SingularAttribute<MMeterRange, Timestamp> recDate;
	public static volatile SingularAttribute<MMeterRange, String> recMan;
	public static volatile SingularAttribute<MMeterRange, Timestamp> updateDate;
	public static volatile SingularAttribute<MMeterRange, Long> updateUserId;
	public static volatile SingularAttribute<MMeterRange, Integer> version;
	public static volatile SingularAttribute<MMeterRange, MMeterType> MMeterType;
}
