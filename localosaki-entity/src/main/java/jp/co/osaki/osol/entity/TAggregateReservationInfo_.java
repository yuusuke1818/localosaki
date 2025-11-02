package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-11-26T18:35:24.263+0900")
@StaticMetamodel(TAggregateReservationInfo.class)
public class TAggregateReservationInfo_ {
	public static volatile SingularAttribute<TAggregateReservationInfo, TAggregateReservationInfoPK> id;
	public static volatile SingularAttribute<TAggregateReservationInfo, String> aggregateCondition;
	public static volatile SingularAttribute<TAggregateReservationInfo, String> aggregateProcessResult;
	public static volatile SingularAttribute<TAggregateReservationInfo, String> aggregateProcessStatus;
	public static volatile SingularAttribute<TAggregateReservationInfo, Timestamp> createDate;
	public static volatile SingularAttribute<TAggregateReservationInfo, Long> createUserId;
	public static volatile SingularAttribute<TAggregateReservationInfo, Integer> delFlg;
	public static volatile SingularAttribute<TAggregateReservationInfo, Timestamp> endDate;
	public static volatile SingularAttribute<TAggregateReservationInfo, String> outputFileName;
	public static volatile SingularAttribute<TAggregateReservationInfo, String> outputFilePath;
	public static volatile SingularAttribute<TAggregateReservationInfo, Timestamp> reservationDate;
	public static volatile SingularAttribute<TAggregateReservationInfo, Timestamp> startDate;
	public static volatile SingularAttribute<TAggregateReservationInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<TAggregateReservationInfo, Long> updateUserId;
	public static volatile SingularAttribute<TAggregateReservationInfo, Integer> version;
	public static volatile SingularAttribute<TAggregateReservationInfo, MCorp> MCorp;
	public static volatile SingularAttribute<TAggregateReservationInfo, MPerson> MPerson;
}
