package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-09T19:25:44.680+0900")
@StaticMetamodel(TBatchStartupSetting.class)
public class TBatchStartupSetting_ {
	public static volatile SingularAttribute<TBatchStartupSetting, String> batchProcessCd;
	public static volatile SingularAttribute<TBatchStartupSetting, String> batchProcessName;
	public static volatile SingularAttribute<TBatchStartupSetting, Timestamp> createDate;
	public static volatile SingularAttribute<TBatchStartupSetting, Long> createUserId;
    public static volatile SingularAttribute<TBatchStartupSetting, String> scheduleCronSpring;
	public static volatile SingularAttribute<TBatchStartupSetting, Timestamp> updateDate;
	public static volatile SingularAttribute<TBatchStartupSetting, Long> updateUserId;
	public static volatile SingularAttribute<TBatchStartupSetting, Integer> version;
    public static volatile ListAttribute<TBatchStartupSetting, MDemandCollectSchedule> MDemandCollectSchedules;
	public static volatile ListAttribute<TBatchStartupSetting, MSmCollectManage> MSmCollectManages;
}
