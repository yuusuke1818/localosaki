package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:57.305+0900")
@StaticMetamodel(MDevPrm.class)
public class MDevPrm_ {
	public static volatile SingularAttribute<MDevPrm, String> alertDisableFlg;
	public static volatile SingularAttribute<MDevPrm, BigDecimal> commInterval;
	public static volatile SingularAttribute<MDevPrm, Timestamp> createDate;
	public static volatile SingularAttribute<MDevPrm, Long> createUserId;
	public static volatile SingularAttribute<MDevPrm, String> demandFlg;
	public static volatile SingularAttribute<MDevPrm, String> devPw;
	public static volatile SingularAttribute<MDevPrm, BigDecimal> devSta;
	public static volatile SingularAttribute<MDevPrm, BigDecimal> examNoticeMonth;
	public static volatile SingularAttribute<MDevPrm, String> homeDirectory;
	public static volatile SingularAttribute<MDevPrm, String> ipAddr;
	public static volatile SingularAttribute<MDevPrm, String> memo;
	public static volatile SingularAttribute<MDevPrm, String> name;
	public static volatile SingularAttribute<MDevPrm, Timestamp> recDate;
	public static volatile SingularAttribute<MDevPrm, String> recMan;
	public static volatile SingularAttribute<MDevPrm, String> revFlg;
	public static volatile SingularAttribute<MDevPrm, BigDecimal> targetPwr;
	public static volatile SingularAttribute<MDevPrm, Timestamp> time;
	public static volatile SingularAttribute<MDevPrm, Timestamp> updateDate;
	public static volatile SingularAttribute<MDevPrm, Long> updateUserId;
	public static volatile SingularAttribute<MDevPrm, Integer> version;
	public static volatile ListAttribute<MDevPrm, MDevRelation> MDevRelations;
	public static volatile SingularAttribute<MDevPrm, String> devId;
	public static volatile SingularAttribute<MDevPrm, Integer> delFlg;
	public static volatile SingularAttribute<MDevPrm, String> devKind;
	public static volatile ListAttribute<MDevPrm, MAlertMail> MAlertMails;
	public static volatile ListAttribute<MDevPrm, MAutoInsp> MAutoInsps;
	public static volatile ListAttribute<MDevPrm, MConcentrator> MConcentrators;
	public static volatile ListAttribute<MDevPrm, MMeter> MMeters;
	public static volatile ListAttribute<MDevPrm, MRepeater> MRepeaters;
}
