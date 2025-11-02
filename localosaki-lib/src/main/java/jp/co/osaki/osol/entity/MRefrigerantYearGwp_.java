package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.964+0900")
@StaticMetamodel(MRefrigerantYearGwp.class)
public class MRefrigerantYearGwp_ {
	public static volatile SingularAttribute<MRefrigerantYearGwp, MRefrigerantYearGwpPK> id;
	public static volatile SingularAttribute<MRefrigerantYearGwp, Timestamp> createDate;
	public static volatile SingularAttribute<MRefrigerantYearGwp, Long> createUserId;
	public static volatile SingularAttribute<MRefrigerantYearGwp, BigDecimal> gwp;
	public static volatile SingularAttribute<MRefrigerantYearGwp, Timestamp> updateDate;
	public static volatile SingularAttribute<MRefrigerantYearGwp, Long> updateUserId;
	public static volatile SingularAttribute<MRefrigerantYearGwp, Integer> version;
	public static volatile SingularAttribute<MRefrigerantYearGwp, MRefrigerant> MRefrigerant;
}
