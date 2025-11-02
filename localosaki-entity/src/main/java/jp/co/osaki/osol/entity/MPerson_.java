package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-08-22T10:54:11.811+0900")
@StaticMetamodel(MPerson.class)
public class MPerson_ {
	public static volatile SingularAttribute<MPerson, MPersonPK> id;
	public static volatile SingularAttribute<MPerson, Timestamp> accountStopDate;
	public static volatile SingularAttribute<MPerson, Integer> accountStopFlg;
	public static volatile SingularAttribute<MPerson, Timestamp> authLastUpdateDate;
	public static volatile SingularAttribute<MPerson, Timestamp> createDate;
	public static volatile SingularAttribute<MPerson, Long> createUserId;
	public static volatile SingularAttribute<MPerson, Integer> delFlg;
	public static volatile SingularAttribute<MPerson, String> deptName;
	public static volatile SingularAttribute<MPerson, String> faxNo;
	public static volatile SingularAttribute<MPerson, Timestamp> lastLoginDate;
	public static volatile SingularAttribute<MPerson, Timestamp> lastOshiraseCheckTime;
	public static volatile SingularAttribute<MPerson, String> mailAddress;
	public static volatile SingularAttribute<MPerson, Integer> passMissCount;
	public static volatile SingularAttribute<MPerson, String> password;
	public static volatile SingularAttribute<MPerson, String> personKana;
	public static volatile SingularAttribute<MPerson, String> personName;
	public static volatile SingularAttribute<MPerson, String> personType;
	public static volatile SingularAttribute<MPerson, String> positionName;
	public static volatile SingularAttribute<MPerson, String> telNo;
	public static volatile SingularAttribute<MPerson, Date> tempPassExpirationDate;
	public static volatile SingularAttribute<MPerson, String> tempPassword;
	public static volatile SingularAttribute<MPerson, Timestamp> updateDate;
	public static volatile SingularAttribute<MPerson, Timestamp> updatePassDate;
	public static volatile SingularAttribute<MPerson, Long> updateUserId;
	public static volatile SingularAttribute<MPerson, Long> userId;
	public static volatile SingularAttribute<MPerson, Integer> version;
	public static volatile ListAttribute<MPerson, MCorp> MCorps;
	public static volatile ListAttribute<MPerson, MCorpPerson> MCorpPersons;
	public static volatile SingularAttribute<MPerson, MCorp> MCorp;
	public static volatile ListAttribute<MPerson, TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs;
	public static volatile ListAttribute<MPerson, TBuildingPerson> TBuildingPersons;
	public static volatile ListAttribute<MPerson, TMaintenanceRequest> TMaintenanceRequests1;
	public static volatile ListAttribute<MPerson, TMaintenanceRequest> TMaintenanceRequests2;
	public static volatile ListAttribute<MPerson, TFacilityBulkInput> TFacilityBulkInputs;
	public static volatile SingularAttribute<MPerson, MUiScreen> MUiScreen;
	public static volatile ListAttribute<MPerson, TApiKey> TApiKeys;
	public static volatile ListAttribute<MPerson, MClaimantInfo> MClaimantInfos;
	public static volatile ListAttribute<MPerson, TAggregateReservationInfo> TAggregateReservationInfos;
	public static volatile ListAttribute<MPerson, TMeterReadingDownloadReservationInfo> TMeterReadingDownloadReservationInfos;
}
