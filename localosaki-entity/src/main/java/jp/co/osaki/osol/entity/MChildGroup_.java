package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.776+0900")
@StaticMetamodel(MChildGroup.class)
public class MChildGroup_ {
	public static volatile SingularAttribute<MChildGroup, MChildGroupPK> id;
	public static volatile SingularAttribute<MChildGroup, String> childGroupName;
	public static volatile SingularAttribute<MChildGroup, Timestamp> createDate;
	public static volatile SingularAttribute<MChildGroup, Long> createUserId;
	public static volatile SingularAttribute<MChildGroup, Integer> delFlg;
	public static volatile SingularAttribute<MChildGroup, Integer> displayOrder;
	public static volatile SingularAttribute<MChildGroup, Timestamp> updateDate;
	public static volatile SingularAttribute<MChildGroup, Long> updateUserId;
	public static volatile SingularAttribute<MChildGroup, Integer> version;
	public static volatile SingularAttribute<MChildGroup, MParentGroup> MParentGroup;
	public static volatile ListAttribute<MChildGroup, TBuildingGroup> TBuildingGroups;
}
