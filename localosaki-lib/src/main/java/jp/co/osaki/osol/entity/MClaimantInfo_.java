package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-18T16:45:55.483+0900")
@StaticMetamodel(MClaimantInfo.class)
public class MClaimantInfo_ {
	public static volatile SingularAttribute<MClaimantInfo, MClaimantInfoPK> id;
	public static volatile SingularAttribute<MClaimantInfo, String> claimantName1;
	public static volatile SingularAttribute<MClaimantInfo, String> claimantName2;
	public static volatile SingularAttribute<MClaimantInfo, Timestamp> createDate;
	public static volatile SingularAttribute<MClaimantInfo, Long> createUserId;
	public static volatile SingularAttribute<MClaimantInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<MClaimantInfo, Long> updateUserId;
	public static volatile SingularAttribute<MClaimantInfo, Integer> version;
	public static volatile SingularAttribute<MClaimantInfo, MPerson> MPerson;
	public static volatile SingularAttribute<MClaimantInfo, TBuilding> TBuilding;
	public static volatile SingularAttribute<MClaimantInfo, Timestamp> recDate;
	public static volatile SingularAttribute<MClaimantInfo, String> recMan;
}
