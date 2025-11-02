package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-11T09:40:48.652+0900")
@StaticMetamodel(TApiKey.class)
public class TApiKey_ {
	public static volatile SingularAttribute<TApiKey, TApiKeyPK> id;
	public static volatile SingularAttribute<TApiKey, String> apiKey;
	public static volatile SingularAttribute<TApiKey, Timestamp> createDate;
	public static volatile SingularAttribute<TApiKey, Long> createUserId;
	public static volatile SingularAttribute<TApiKey, Integer> delFlg;
	public static volatile SingularAttribute<TApiKey, Timestamp> expirationDate;
	public static volatile SingularAttribute<TApiKey, Timestamp> issuedDate;
	public static volatile SingularAttribute<TApiKey, String> refreshKey;
	public static volatile SingularAttribute<TApiKey, Timestamp> refreshKeyExpirationDate;
	public static volatile SingularAttribute<TApiKey, Timestamp> updateDate;
	public static volatile SingularAttribute<TApiKey, Long> updateUserId;
	public static volatile SingularAttribute<TApiKey, Integer> validityPeriodMin;
	public static volatile SingularAttribute<TApiKey, Integer> version;
	public static volatile SingularAttribute<TApiKey, MPerson> MPerson;
}
