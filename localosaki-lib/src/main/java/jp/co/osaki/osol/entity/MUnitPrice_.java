package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.399+0900")
@StaticMetamodel(MUnitPrice.class)
public class MUnitPrice_ {
	public static volatile SingularAttribute<MUnitPrice, MUnitPricePK> id;
	public static volatile SingularAttribute<MUnitPrice, Timestamp> createDate;
	public static volatile SingularAttribute<MUnitPrice, Long> createUserId;
	public static volatile SingularAttribute<MUnitPrice, Timestamp> recDate;
	public static volatile SingularAttribute<MUnitPrice, String> recMan;
	public static volatile SingularAttribute<MUnitPrice, BigDecimal> unitPrice;
	public static volatile SingularAttribute<MUnitPrice, Timestamp> updateDate;
	public static volatile SingularAttribute<MUnitPrice, Long> updateUserId;
	public static volatile SingularAttribute<MUnitPrice, Integer> version;
}
