package jp.co.osaki.osol.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.781+0900")
@StaticMetamodel(MChildGroupPK.class)
public class MChildGroupPK_ {
	public static volatile SingularAttribute<MChildGroupPK, String> corpId;
	public static volatile SingularAttribute<MChildGroupPK, Long> parentGroupId;
	public static volatile SingularAttribute<MChildGroupPK, Long> childGroupId;
}
