package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-07-27T18:43:53.633+0900")
@StaticMetamodel(TFacilityBulkInput.class)
public class TFacilityBulkInput_ {
	public static volatile SingularAttribute<TFacilityBulkInput, TFacilityBulkInputPK> id;
	public static volatile SingularAttribute<TFacilityBulkInput, Timestamp> createDate;
	public static volatile SingularAttribute<TFacilityBulkInput, Long> createUserId;
	public static volatile SingularAttribute<TFacilityBulkInput, Integer> delFlg;
	public static volatile SingularAttribute<TFacilityBulkInput, Timestamp> processEndDatetime;
	public static volatile SingularAttribute<TFacilityBulkInput, String> processResult;
	public static volatile SingularAttribute<TFacilityBulkInput, String> processResultFileName;
	public static volatile SingularAttribute<TFacilityBulkInput, String> processResultFilePath;
	public static volatile SingularAttribute<TFacilityBulkInput, Long> processResultFileSize;
	public static volatile SingularAttribute<TFacilityBulkInput, Timestamp> processStartDatetime;
	public static volatile SingularAttribute<TFacilityBulkInput, String> processStatus;
	public static volatile SingularAttribute<TFacilityBulkInput, Timestamp> updateDate;
	public static volatile SingularAttribute<TFacilityBulkInput, Long> updateUserId;
	public static volatile SingularAttribute<TFacilityBulkInput, String> uploadFileName;
	public static volatile SingularAttribute<TFacilityBulkInput, String> uploadFilePath;
	public static volatile SingularAttribute<TFacilityBulkInput, Long> uploadFileSize;
	public static volatile SingularAttribute<TFacilityBulkInput, Integer> version;
	public static volatile SingularAttribute<TFacilityBulkInput, MCorp> MCorp;
	public static volatile SingularAttribute<TFacilityBulkInput, MPerson> MPerson;
}
