package jp.co.osaki.osol.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2025-09-15T01:41:13.537+0900")
@StaticMetamodel(MMeterInfo.class)
public class MMeterInfo_ {
	public static volatile SingularAttribute<MMeterInfo, String> meterId;
	public static volatile SingularAttribute<MMeterInfo, String> productName;
	public static volatile SingularAttribute<MMeterInfo, String> recMan;
	public static volatile SingularAttribute<MMeterInfo, String> serialNum;
	public static volatile SingularAttribute<MMeterInfo, Integer> version;
	public static volatile SingularAttribute<MMeterInfo, MMeter> MMeter;
}
