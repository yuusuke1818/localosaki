package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.387+0900")
@StaticMetamodel(MTypeStage.class)
public class MTypeStage_ {
	public static volatile SingularAttribute<MTypeStage, MTypeStagePK> id;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> aPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> aRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> bPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> bRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> cPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> cRange;
	public static volatile SingularAttribute<MTypeStage, Timestamp> createDate;
	public static volatile SingularAttribute<MTypeStage, Long> createUserId;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> dPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> dRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> ePrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> eRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> fPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> fRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> gPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> gRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> hPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> hRange;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> iPrice;
	public static volatile SingularAttribute<MTypeStage, BigDecimal> iRange;
	public static volatile SingularAttribute<MTypeStage, Timestamp> recDate;
	public static volatile SingularAttribute<MTypeStage, String> recMan;
	public static volatile SingularAttribute<MTypeStage, Timestamp> updateDate;
	public static volatile SingularAttribute<MTypeStage, Long> updateUserId;
	public static volatile SingularAttribute<MTypeStage, Integer> version;
	public static volatile ListAttribute<MTypeStage, MTypePower> MTypePowers;
	public static volatile SingularAttribute<MTypeStage, TBuilding> TBuilding;
}
