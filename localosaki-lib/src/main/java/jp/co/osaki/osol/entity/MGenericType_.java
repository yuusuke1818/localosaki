package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.904+0900")
@StaticMetamodel(MGenericType.class)
public class MGenericType_ {
	public static volatile SingularAttribute<MGenericType, MGenericTypePK> id;
	public static volatile SingularAttribute<MGenericType, Timestamp> createDate;
	public static volatile SingularAttribute<MGenericType, Long> createUserId;
	public static volatile SingularAttribute<MGenericType, Integer> displayOrder;
	public static volatile SingularAttribute<MGenericType, String> groupName;
	public static volatile SingularAttribute<MGenericType, String> kbnName;
	public static volatile SingularAttribute<MGenericType, Timestamp> updateDate;
	public static volatile SingularAttribute<MGenericType, Long> updateUserId;
	public static volatile SingularAttribute<MGenericType, Integer> version;
}
