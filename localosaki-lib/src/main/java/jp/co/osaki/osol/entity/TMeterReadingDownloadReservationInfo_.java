package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2024-08-23T16:35:56.697+0900")
@StaticMetamodel(TMeterReadingDownloadReservationInfo.class)
public class TMeterReadingDownloadReservationInfo_ {
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Long> reservationId;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> corpId;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> personId;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Timestamp> createDate;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Long> createUserId;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Integer> delFlg;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Timestamp> endDate;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> outputFileName;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> outputFilePath;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> processResult;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> processStatus;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Timestamp> reservationDate;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, String> searchCondition;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Timestamp> startDate;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Long> updateUserId;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, Integer> version;
	public static volatile SingularAttribute<TMeterReadingDownloadReservationInfo, MPerson> MPerson;
}
