package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.009+0900")
@StaticMetamodel(TAvailableEnergyBulkInput.class)
public class TAvailableEnergyBulkInput_ {
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, TAvailableEnergyBulkInputPK> id;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Timestamp> createDate;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Long> createUserId;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Integer> delFlg;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Timestamp> processEndDatetime;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> processResult;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> processResultFileName;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> processResultFilePath;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Long> processResultFileSize;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Timestamp> processStartDatetime;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> processStatus;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Timestamp> updateDate;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Long> updateUserId;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> uploadFileName;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, String> uploadFilePath;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Long> uploadFileSize;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, Integer> version;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, MCorp> MCorp;
	public static volatile SingularAttribute<TAvailableEnergyBulkInput, MPerson> MPerson;
}
