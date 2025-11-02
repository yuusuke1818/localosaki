package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-10-11T11:30:58.118+0900")
@StaticMetamodel(TSmConnectStatus.class)
public class TSmConnectStatus_ {
	public static volatile SingularAttribute<TSmConnectStatus, Long> smId;
	public static volatile SingularAttribute<TSmConnectStatus, Integer> connectActiveFlg;
	public static volatile SingularAttribute<TSmConnectStatus, Timestamp> createDate;
	public static volatile SingularAttribute<TSmConnectStatus, Long> createUserId;
	public static volatile SingularAttribute<TSmConnectStatus, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmConnectStatus, Long> updateUserId;
	public static volatile SingularAttribute<TSmConnectStatus, Integer> version;
	public static volatile SingularAttribute<TSmConnectStatus, MSmPrm> MSmPrm;
}
