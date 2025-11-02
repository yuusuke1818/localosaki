package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:56.984+0900")
@StaticMetamodel(MAlertMail.class)
public class MAlertMail_ {
	public static volatile SingularAttribute<MAlertMail, MAlertMailPK> id;
	public static volatile SingularAttribute<MAlertMail, String> alertCommandErr;
	public static volatile SingularAttribute<MAlertMail, String> alertConcentErr;
	public static volatile SingularAttribute<MAlertMail, String> alertDmvErr;
	public static volatile SingularAttribute<MAlertMail, String> alertExam;
	public static volatile SingularAttribute<MAlertMail, String> alertLoadsurveyErr;
	public static volatile SingularAttribute<MAlertMail, String> alertManualInsp;
	public static volatile SingularAttribute<MAlertMail, String> alertMeterErr;
	public static volatile SingularAttribute<MAlertMail, String> alertTermErr;
	public static volatile SingularAttribute<MAlertMail, Timestamp> createDate;
	public static volatile SingularAttribute<MAlertMail, Long> createUserId;
	public static volatile SingularAttribute<MAlertMail, String> disabledFlg;
	public static volatile SingularAttribute<MAlertMail, String> email;
	public static volatile SingularAttribute<MAlertMail, String> memo;
	public static volatile SingularAttribute<MAlertMail, Timestamp> recDate;
	public static volatile SingularAttribute<MAlertMail, String> recMan;
	public static volatile SingularAttribute<MAlertMail, Timestamp> updateDate;
	public static volatile SingularAttribute<MAlertMail, Long> updateUserId;
	public static volatile SingularAttribute<MAlertMail, Integer> version;
	public static volatile SingularAttribute<MAlertMail, MDevPrm> MDevPrm;
}
