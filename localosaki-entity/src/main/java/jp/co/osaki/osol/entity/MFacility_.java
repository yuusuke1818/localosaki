package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.870+0900")
@StaticMetamodel(MFacility.class)
public class MFacility_ {
	public static volatile SingularAttribute<MFacility, MFacilityPK> id;
	public static volatile SingularAttribute<MFacility, Date> capitalizationDate;
	public static volatile SingularAttribute<MFacility, Timestamp> createDate;
	public static volatile SingularAttribute<MFacility, Long> createUserId;
	public static volatile SingularAttribute<MFacility, Integer> delFlg;
	public static volatile SingularAttribute<MFacility, Integer> disposal;
	public static volatile SingularAttribute<MFacility, Date> disposalYmd;
	public static volatile SingularAttribute<MFacility, BigDecimal> facilityCapacity;
	public static volatile SingularAttribute<MFacility, String> facilityLedgerNo;
	public static volatile SingularAttribute<MFacility, String> facilityName;
	public static volatile SingularAttribute<MFacility, Integer> facilityQuantity;
	public static volatile SingularAttribute<MFacility, Date> facilityUpdatePlanYmd;
	public static volatile SingularAttribute<MFacility, BigDecimal> freonPossession;
	public static volatile SingularAttribute<MFacility, String> insAtionLocation;
	public static volatile SingularAttribute<MFacility, Date> introductionDate;
	public static volatile SingularAttribute<MFacility, Date> madeDate;
	public static volatile SingularAttribute<MFacility, String> makerModelNo;
	public static volatile SingularAttribute<MFacility, String> makerName;
	public static volatile SingularAttribute<MFacility, String> ownerShipCode;
	public static volatile SingularAttribute<MFacility, BigDecimal> ratedOutput;
	public static volatile SingularAttribute<MFacility, String> refrigerantMemo;
	public static volatile SingularAttribute<MFacility, Integer> serviceLife;
	public static volatile SingularAttribute<MFacility, Timestamp> updateDate;
	public static volatile SingularAttribute<MFacility, Long> updateUserId;
	public static volatile SingularAttribute<MFacility, String> use;
	public static volatile SingularAttribute<MFacility, Integer> version;
	public static volatile SingularAttribute<MFacility, MEnergyType> MEnergyType;
	public static volatile SingularAttribute<MFacility, MFacilityUnit> MFacilityUnit1;
	public static volatile SingularAttribute<MFacility, MFacilityUnit> MFacilityUnit2;
	public static volatile SingularAttribute<MFacility, MRefrigerant> MRefrigerant;
	public static volatile SingularAttribute<MFacility, TBuilding> TBuilding;
	public static volatile ListAttribute<MFacility, MFacilityKind> MFacilityKinds;
	public static volatile ListAttribute<MFacility, TFacilityDocument> TFacilityDocuments;
	public static volatile ListAttribute<MFacility, TFreonFillingReport> TFreonFillingReports;
	public static volatile ListAttribute<MFacility, TMaintenanceFacility> TMaintenanceFacilities;
}
