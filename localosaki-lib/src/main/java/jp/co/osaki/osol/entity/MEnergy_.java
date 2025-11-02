package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.829+0900")
@StaticMetamodel(MEnergy.class)
public class MEnergy_ {
	public static volatile SingularAttribute<MEnergy, MEnergyPK> id;
	public static volatile SingularAttribute<MEnergy, Timestamp> createDate;
	public static volatile SingularAttribute<MEnergy, Long> createUserId;
	public static volatile SingularAttribute<MEnergy, Integer> displayOrder;
	public static volatile SingularAttribute<MEnergy, String> engName;
	public static volatile SingularAttribute<MEnergy, String> supplyArea;
	public static volatile SingularAttribute<MEnergy, String> supplyCompany;
	public static volatile SingularAttribute<MEnergy, String> supplyCompanyBiko;
	public static volatile SingularAttribute<MEnergy, String> supplyCompanyTelNo;
	public static volatile SingularAttribute<MEnergy, String> supplyCompanyUrl;
	public static volatile SingularAttribute<MEnergy, String> unit;
	public static volatile SingularAttribute<MEnergy, Timestamp> updateDate;
	public static volatile SingularAttribute<MEnergy, Long> updateUserId;
	public static volatile SingularAttribute<MEnergy, Integer> version;
	public static volatile ListAttribute<MEnergy, MCoefficientHistoryManage> MCoefficientHistoryManages;
	public static volatile SingularAttribute<MEnergy, MEnergyType> MEnergyType;
	public static volatile ListAttribute<MEnergy, TAvailableEnergy> TAvailableEnergies;
}
