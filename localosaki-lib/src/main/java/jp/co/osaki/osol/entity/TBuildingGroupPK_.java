package jp.co.osaki.osol.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.055+0900")
@StaticMetamodel(TBuildingGroupPK.class)
public class TBuildingGroupPK_ {
	public static volatile SingularAttribute<TBuildingGroupPK, String> corpId;
	public static volatile SingularAttribute<TBuildingGroupPK, Long> buildingId;
	public static volatile SingularAttribute<TBuildingGroupPK, Long> parentGroupId;
	public static volatile SingularAttribute<TBuildingGroupPK, Long> childGroupId;
}
