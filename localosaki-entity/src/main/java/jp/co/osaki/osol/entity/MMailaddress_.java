package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.223+0900")
@StaticMetamodel(MMailaddress.class)
public class MMailaddress_ {
	public static volatile SingularAttribute<MMailaddress, Long> no;
	public static volatile SingularAttribute<MMailaddress, Timestamp> createDate;
	public static volatile SingularAttribute<MMailaddress, Long> createUserId;
	public static volatile SingularAttribute<MMailaddress, String> disabledFlg;
	public static volatile SingularAttribute<MMailaddress, String> email;
	public static volatile SingularAttribute<MMailaddress, String> memo;
	public static volatile SingularAttribute<MMailaddress, Timestamp> recDate;
	public static volatile SingularAttribute<MMailaddress, String> recMan;
	public static volatile SingularAttribute<MMailaddress, Timestamp> updateDate;
	public static volatile SingularAttribute<MMailaddress, Long> updateUserId;
	public static volatile SingularAttribute<MMailaddress, Integer> version;
}
