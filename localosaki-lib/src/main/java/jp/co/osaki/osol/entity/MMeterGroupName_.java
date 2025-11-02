package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-10-20T10:33:44.224+0900")
@StaticMetamodel(MMeterGroupName.class)
public class MMeterGroupName_ {
	public static volatile SingularAttribute<MMeterGroupName, MMeterGroupNamePK> id;
	public static volatile SingularAttribute<MMeterGroupName, Timestamp> createDate;
	public static volatile SingularAttribute<MMeterGroupName, Long> createUserId;
	public static volatile SingularAttribute<MMeterGroupName, Timestamp> updateDate;
	public static volatile SingularAttribute<MMeterGroupName, Long> updateUserId;
	public static volatile SingularAttribute<MMeterGroupName, Integer> version;
	public static volatile ListAttribute<MMeterGroupName, MGroupPrice> MGroupPrices;
	public static volatile ListAttribute<MMeterGroupName, MMeterGroup> MMeterGroups;
	public static volatile SingularAttribute<MMeterGroupName, TBuilding> TBuilding;
	public static volatile SingularAttribute<MMeterGroupName, String> meterGroupName;
}
