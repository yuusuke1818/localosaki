package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-18T14:31:06.883+0900")
@StaticMetamodel(TAvailableEnergy.class)
public class TAvailableEnergy_ {
	public static volatile SingularAttribute<TAvailableEnergy, TAvailableEnergyPK> id;
	public static volatile SingularAttribute<TAvailableEnergy, String> contractBiko;
	public static volatile SingularAttribute<TAvailableEnergy, BigDecimal> contractPower;
	public static volatile SingularAttribute<TAvailableEnergy, String> contractPowerUnit;
	public static volatile SingularAttribute<TAvailableEnergy, String> contractType;
	public static volatile SingularAttribute<TAvailableEnergy, Timestamp> createDate;
	public static volatile SingularAttribute<TAvailableEnergy, Long> createUserId;
	public static volatile SingularAttribute<TAvailableEnergy, String> customerNo;
	public static volatile SingularAttribute<TAvailableEnergy, String> dayAndNightType;
	public static volatile SingularAttribute<TAvailableEnergy, Integer> delFlg;
	public static volatile SingularAttribute<TAvailableEnergy, Integer> displayOrder;
	public static volatile SingularAttribute<TAvailableEnergy, Date> energyEndYm;
	public static volatile SingularAttribute<TAvailableEnergy, Date> energyStartYm;
    public static volatile SingularAttribute<TAvailableEnergy, Integer> energyUseLineValueFlg;
	public static volatile SingularAttribute<TAvailableEnergy, String> engSupplyType;
	public static volatile SingularAttribute<TAvailableEnergy, Integer> inputFlg;
	public static volatile SingularAttribute<TAvailableEnergy, String> supplyPointSpecificNo;
	public static volatile SingularAttribute<TAvailableEnergy, Timestamp> updateDate;
	public static volatile SingularAttribute<TAvailableEnergy, Long> updateUserId;
	public static volatile SingularAttribute<TAvailableEnergy, String> usePlace;
	public static volatile SingularAttribute<TAvailableEnergy, Integer> version;
	public static volatile SingularAttribute<TAvailableEnergy, MEnergy> MEnergy;
	public static volatile SingularAttribute<TAvailableEnergy, TBuilding> TBuilding;
	public static volatile SingularAttribute<TAvailableEnergy, TBuildingEnergyType> TBuildingEnergyType;
	public static volatile ListAttribute<TAvailableEnergy, TEnergyUse> TEnergyUses;
    public static volatile ListAttribute<TAvailableEnergy, TAvailableEnergyLine> TAvailableEnergyLines;
}
