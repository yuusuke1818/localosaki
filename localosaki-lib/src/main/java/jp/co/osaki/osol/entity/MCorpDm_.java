package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-09T19:25:44.656+0900")
@StaticMetamodel(MCorpDm.class)
public class MCorpDm_ {
	public static volatile SingularAttribute<MCorpDm, String> corpId;
	public static volatile SingularAttribute<MCorpDm, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpDm, Long> createUserId;
	public static volatile SingularAttribute<MCorpDm, String> sumDate;
	public static volatile SingularAttribute<MCorpDm, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpDm, Long> updateUserId;
	public static volatile SingularAttribute<MCorpDm, Integer> version;
	public static volatile SingularAttribute<MCorpDm, Integer> weekClosingDayOfWeek;
	public static volatile SingularAttribute<MCorpDm, String> weekStartDay;
	public static volatile SingularAttribute<MCorpDm, MCorp> MCorp;
	public static volatile SingularAttribute<MCorpDm, MCorpTargetAlarm> MCorpTargetAlarm;
	public static volatile ListAttribute<MCorpDm, MLineGroup> MLineGroups;
}
