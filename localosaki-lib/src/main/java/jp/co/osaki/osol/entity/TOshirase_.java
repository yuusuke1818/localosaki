package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.167+0900")
@StaticMetamodel(TOshirase.class)
public class TOshirase_ {
	public static volatile SingularAttribute<TOshirase, TOshirasePK> id;
	public static volatile SingularAttribute<TOshirase, Timestamp> createDate;
	public static volatile SingularAttribute<TOshirase, Long> createUserId;
	public static volatile SingularAttribute<TOshirase, Integer> delFlg;
	public static volatile SingularAttribute<TOshirase, Integer> externalSiteFlg;
	public static volatile SingularAttribute<TOshirase, String> fileName;
	public static volatile SingularAttribute<TOshirase, String> markCode;
	public static volatile SingularAttribute<TOshirase, Date> publishedEndDay;
	public static volatile SingularAttribute<TOshirase, Date> publishedStartDay;
	public static volatile SingularAttribute<TOshirase, String> saveFilePath;
	public static volatile SingularAttribute<TOshirase, String> targetCode;
	public static volatile SingularAttribute<TOshirase, String> title;
	public static volatile SingularAttribute<TOshirase, Timestamp> updateDate;
	public static volatile SingularAttribute<TOshirase, Long> updateUserId;
	public static volatile SingularAttribute<TOshirase, String> url;
	public static volatile SingularAttribute<TOshirase, Integer> version;
	public static volatile SingularAttribute<TOshirase, MCorp> MCorp;
	public static volatile ListAttribute<TOshirase, MOshiraseDelivery> MOshiraseDeliveries;
}
