package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.724+0900")
@StaticMetamodel(MSmLineVerify.class)
public class MSmLineVerify_ {
	public static volatile SingularAttribute<MSmLineVerify, MSmLineVerifyPK> id;
	public static volatile SingularAttribute<MSmLineVerify, String> airVerifyType;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> basicRateUnitPrice;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> commodityChargeUnitPrice;
	public static volatile SingularAttribute<MSmLineVerify, Timestamp> createDate;
	public static volatile SingularAttribute<MSmLineVerify, Long> createUserId;
	public static volatile SingularAttribute<MSmLineVerify, Integer> delFlg;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth1;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth10;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth11;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth12;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth2;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth3;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth4;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth5;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth6;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth7;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth8;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> proposalAmountUsedMonth9;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionCorrectionRate;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth1;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth10;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth11;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth12;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth2;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth3;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth4;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth5;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth6;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth7;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth8;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerAmountMonth9;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth1;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth10;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth11;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth12;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth2;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth3;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth4;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth5;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth6;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth7;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth8;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionLowerRateMonth9;
	public static volatile SingularAttribute<MSmLineVerify, BigDecimal> reductionRateThreshold;
	public static volatile SingularAttribute<MSmLineVerify, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmLineVerify, Long> updateUserId;
	public static volatile SingularAttribute<MSmLineVerify, Integer> version;
	public static volatile ListAttribute<MSmLineVerify, MSmLineControlLoadVerify> MSmLineControlLoadVerifies;
	public static volatile SingularAttribute<MSmLineVerify, MBuildingSm> MBuildingSm;
	public static volatile SingularAttribute<MSmLineVerify, MLine> MLine;
}
