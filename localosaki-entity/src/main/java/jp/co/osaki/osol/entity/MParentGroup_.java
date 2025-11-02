package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.938+0900")
@StaticMetamodel(MParentGroup.class)
public class MParentGroup_ {
	public static volatile SingularAttribute<MParentGroup, MParentGroupPK> id;
	public static volatile SingularAttribute<MParentGroup, Timestamp> createDate;
	public static volatile SingularAttribute<MParentGroup, Long> createUserId;
	public static volatile SingularAttribute<MParentGroup, Integer> delFlg;
	public static volatile SingularAttribute<MParentGroup, Integer> displayOrder;
	public static volatile SingularAttribute<MParentGroup, Integer> overlapFlg;
	public static volatile SingularAttribute<MParentGroup, String> parentGroupName;
	public static volatile SingularAttribute<MParentGroup, Timestamp> updateDate;
	public static volatile SingularAttribute<MParentGroup, Long> updateUserId;
	public static volatile SingularAttribute<MParentGroup, Integer> version;
	public static volatile ListAttribute<MParentGroup, MChildGroup> MChildGroups;
}
