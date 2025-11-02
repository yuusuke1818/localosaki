package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.169+0900")
@StaticMetamodel(MComDivrate.class)
public class MComDivrate_ {
	public static volatile SingularAttribute<MComDivrate, MComDivratePK> id;
	public static volatile SingularAttribute<MComDivrate, Timestamp> createDate;
	public static volatile SingularAttribute<MComDivrate, Long> createUserId;
	public static volatile SingularAttribute<MComDivrate, BigDecimal> divRate;
	public static volatile SingularAttribute<MComDivrate, Timestamp> recDate;
	public static volatile SingularAttribute<MComDivrate, String> recMan;
	public static volatile SingularAttribute<MComDivrate, Timestamp> updateDate;
	public static volatile SingularAttribute<MComDivrate, Long> updateUserId;
	public static volatile SingularAttribute<MComDivrate, Integer> version;
	public static volatile SingularAttribute<MComDivrate, TBuilding> TBuilding;
}
