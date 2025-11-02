package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-10-11T11:30:58.102+0900")
@StaticMetamodel(TSmConnectControlProduct.class)
public class TSmConnectControlProduct_ {
	public static volatile SingularAttribute<TSmConnectControlProduct, TSmConnectControlProductPK> id;
	public static volatile SingularAttribute<TSmConnectControlProduct, Timestamp> createDate;
	public static volatile SingularAttribute<TSmConnectControlProduct, Long> createUserId;
	public static volatile SingularAttribute<TSmConnectControlProduct, Timestamp> updateDate;
	public static volatile SingularAttribute<TSmConnectControlProduct, Long> updateUserId;
	public static volatile SingularAttribute<TSmConnectControlProduct, Integer> version;
	public static volatile SingularAttribute<TSmConnectControlProduct, TSmConnectControlSetting> TSmConnectControlSetting;
	public static volatile SingularAttribute<TSmConnectControlProduct, MProductSpec> MProductSpec;
}
