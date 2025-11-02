package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-08-26T14:45:17.162+0900")
@StaticMetamodel(MOshiraseDelivery.class)
public class MOshiraseDelivery_ {
	public static volatile SingularAttribute<MOshiraseDelivery, MOshiraseDeliveryPK> id;
	public static volatile SingularAttribute<MOshiraseDelivery, Timestamp> createDate;
	public static volatile SingularAttribute<MOshiraseDelivery, Long> createUserId;
	public static volatile SingularAttribute<MOshiraseDelivery, Integer> deliveryUseFlg;
	public static volatile SingularAttribute<MOshiraseDelivery, Timestamp> updateDate;
	public static volatile SingularAttribute<MOshiraseDelivery, Long> updateUserId;
	public static volatile SingularAttribute<MOshiraseDelivery, Integer> version;
	public static volatile SingularAttribute<MOshiraseDelivery, TOshirase> TOshirase;
}
