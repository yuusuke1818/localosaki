package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.444+0900")
@StaticMetamodel(MSmPointEx.class)
public class MSmPointEx_ {
	public static volatile SingularAttribute<MSmPointEx, MSmPointExPK> id;
	public static volatile SingularAttribute<MSmPointEx, Timestamp> createDate;
	public static volatile SingularAttribute<MSmPointEx, Long> createUserId;
	public static volatile SingularAttribute<MSmPointEx, Integer> delFlg;
	public static volatile SingularAttribute<MSmPointEx, Integer> pointErrorFlg;
	public static volatile SingularAttribute<MSmPointEx, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmPointEx, Long> updateUserId;
	public static volatile SingularAttribute<MSmPointEx, Integer> version;
	public static volatile SingularAttribute<MSmPointEx, MSmPoint> MSmPoint;
}
