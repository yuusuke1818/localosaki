package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.079+0900")
@StaticMetamodel(TEnergyUse.class)
public class TEnergyUse_ {
	public static volatile SingularAttribute<TEnergyUse, TEnergyUsePK> id;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> billedAmount;
	public static volatile SingularAttribute<TEnergyUse, Integer> changeAuthFlg;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> co2Coefficient;
	public static volatile SingularAttribute<TEnergyUse, String> co2Unit;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> co2Value;
	public static volatile SingularAttribute<TEnergyUse, String> contractBiko;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> contractPower;
	public static volatile SingularAttribute<TEnergyUse, String> contractPowerUnit;
	public static volatile SingularAttribute<TEnergyUse, String> contractType;
	public static volatile SingularAttribute<TEnergyUse, Timestamp> createDate;
	public static volatile SingularAttribute<TEnergyUse, Long> createUserId;
	public static volatile SingularAttribute<TEnergyUse, String> customerNo;
	public static volatile SingularAttribute<TEnergyUse, String> dayAndNightType;
	public static volatile SingularAttribute<TEnergyUse, Integer> delFlg;
	public static volatile SingularAttribute<TEnergyUse, String> engSupplyType;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> firstEngCoefficient;
	public static volatile SingularAttribute<TEnergyUse, String> firstEngUnit;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> firstEngValue;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> fuelAdjustmentCost;
	public static volatile SingularAttribute<TEnergyUse, String> haneiType;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> maxPower;
	public static volatile SingularAttribute<TEnergyUse, Date> meterCheckDate;
	public static volatile SingularAttribute<TEnergyUse, String> meterImageFileName;
	public static volatile SingularAttribute<TEnergyUse, Long> meterImageFileSize;
	public static volatile SingularAttribute<TEnergyUse, String> meterImagePath;
	public static volatile SingularAttribute<TEnergyUse, Date> paymentDate;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> powerCoefficient;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> rEnergyGpLevy;
	public static volatile SingularAttribute<TEnergyUse, String> supplyPointSpecificNo;
	public static volatile SingularAttribute<TEnergyUse, Timestamp> updateDate;
	public static volatile SingularAttribute<TEnergyUse, Long> updateUserId;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> useDays;
	public static volatile SingularAttribute<TEnergyUse, String> usePlace;
	public static volatile SingularAttribute<TEnergyUse, String> useUnit1;
	public static volatile SingularAttribute<TEnergyUse, String> useUnit2;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> useValue1;
	public static volatile SingularAttribute<TEnergyUse, BigDecimal> useValue2;
	public static volatile SingularAttribute<TEnergyUse, Integer> version;
	public static volatile SingularAttribute<TEnergyUse, TAvailableEnergy> TAvailableEnergy;
}
