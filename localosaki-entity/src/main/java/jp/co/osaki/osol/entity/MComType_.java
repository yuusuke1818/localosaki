package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.177+0900")
@StaticMetamodel(MComType.class)
public class MComType_ {
	public static volatile SingularAttribute<MComType, MComTypePK> id;
	public static volatile SingularAttribute<MComType, String> comName;
	public static volatile SingularAttribute<MComType, Timestamp> createDate;
	public static volatile SingularAttribute<MComType, Long> createUserId;
	public static volatile SingularAttribute<MComType, Timestamp> recDate;
	public static volatile SingularAttribute<MComType, String> recMan;
	public static volatile SingularAttribute<MComType, Timestamp> updateDate;
	public static volatile SingularAttribute<MComType, Long> updateUserId;
	public static volatile SingularAttribute<MComType, Integer> version;
	public static volatile SingularAttribute<MComType, TBuilding> TBuilding;
}
