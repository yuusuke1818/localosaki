package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-05-07T16:50:18.940+0900")
@StaticMetamodel(MCorp.class)
public class MCorp_ {
	public static volatile SingularAttribute<MCorp, String> corpId;
	public static volatile SingularAttribute<MCorp, String> address1;
	public static volatile SingularAttribute<MCorp, String> address2;
	public static volatile SingularAttribute<MCorp, String> corpKana;
	public static volatile SingularAttribute<MCorp, String> corpLogoImageFileName;
	public static volatile SingularAttribute<MCorp, String> corpLogoImageFilePath;
	public static volatile SingularAttribute<MCorp, String> corpName;
	public static volatile SingularAttribute<MCorp, String> corpType;
	public static volatile SingularAttribute<MCorp, Timestamp> createDate;
	public static volatile SingularAttribute<MCorp, Long> createUserId;
	public static volatile SingularAttribute<MCorp, String> executiveName;
	public static volatile SingularAttribute<MCorp, String> executivePostName;
	public static volatile SingularAttribute<MCorp, String> faxNo;
	public static volatile SingularAttribute<MCorp, String> maintenanceUseSetting;
	public static volatile SingularAttribute<MCorp, String> specificBusinessNo;
	public static volatile SingularAttribute<MCorp, String> specificEmissionCd;
	public static volatile SingularAttribute<MCorp, String> telNo;
	public static volatile SingularAttribute<MCorp, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorp, Long> updateUserId;
	public static volatile SingularAttribute<MCorp, Date> useStopEndDate;
	public static volatile SingularAttribute<MCorp, Date> useStopStartDate;
	public static volatile SingularAttribute<MCorp, Integer> version;
	public static volatile SingularAttribute<MCorp, String> zipCd;
	public static volatile SingularAttribute<MCorp, MMunicipality> MMunicipality;
	public static volatile SingularAttribute<MCorp, MPerson> MPerson;
	public static volatile SingularAttribute<MCorp, MPrefecture> MPrefecture;
	public static volatile ListAttribute<MCorp, MCorpFacilityKind> MCorpFacilityKinds;
	public static volatile ListAttribute<MCorp, MCorpPerson> MCorpPersons;
	public static volatile ListAttribute<MCorp, MEstimateKind> MEstimateKinds;
	public static volatile ListAttribute<MCorp, MLoginIpAddr> MLoginIpAddrs;
	public static volatile ListAttribute<MCorp, MMaintenanceMailSetting> MMaintenanceMailSettings;
	public static volatile ListAttribute<MCorp, MMaintenanceTrader> MMaintenanceTraders;
	public static volatile ListAttribute<MCorp, MPerson> MPersons;
	public static volatile ListAttribute<MCorp, MSubtype> MSubtypes;
	public static volatile ListAttribute<MCorp, MUnitDivide> MUnitDivides;
	public static volatile ListAttribute<MCorp, TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs;
	public static volatile ListAttribute<MCorp, TBuilding> TBuildings;
	public static volatile ListAttribute<MCorp, TOshirase> TOshirases;
	public static volatile ListAttribute<MCorp, TFacilityBulkInput> TFacilityBulkInputs;
	public static volatile ListAttribute<MCorp, TPlanFulfillmentInfo> TPlanFulfillmentInfos;
	public static volatile ListAttribute<MCorp, MCorpFunctionUse> MCorpFunctionUses;
	public static volatile SingularAttribute<MCorp, MCorpDm> MCorpDm;
	public static volatile ListAttribute<MCorp, TAggregateReservationInfo> TAggregateReservationInfos;
}
