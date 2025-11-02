package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-20T15:31:11.075+0900")
@StaticMetamodel(MMeterGroup.class)
public class MMeterGroup_ {
	public static volatile SingularAttribute<MMeterGroup, MMeterGroupPK> id;
	public static volatile SingularAttribute<MMeterGroup, BigDecimal> calcType;
	public static volatile SingularAttribute<MMeterGroup, Timestamp> createDate;
	public static volatile SingularAttribute<MMeterGroup, Long> createUserId;
	public static volatile SingularAttribute<MMeterGroup, Timestamp> updateDate;
	public static volatile SingularAttribute<MMeterGroup, Long> updateUserId;
	public static volatile SingularAttribute<MMeterGroup, Integer> version;
	public static volatile SingularAttribute<MMeterGroup, MMeter> MMeter;
	public static volatile SingularAttribute<MMeterGroup, MMeterGroupName> MMeterGroupName;
}
