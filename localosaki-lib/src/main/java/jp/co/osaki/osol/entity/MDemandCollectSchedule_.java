package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-11-15T14:21:06.034+0900")
@StaticMetamodel(MDemandCollectSchedule.class)
public class MDemandCollectSchedule_ {
	public static volatile SingularAttribute<MDemandCollectSchedule, MDemandCollectSchedulePK> id;
	public static volatile SingularAttribute<MDemandCollectSchedule, Timestamp> createDate;
	public static volatile SingularAttribute<MDemandCollectSchedule, Long> createUserId;
	public static volatile SingularAttribute<MDemandCollectSchedule, String> scheduleCrontab;
	public static volatile SingularAttribute<MDemandCollectSchedule, Timestamp> updateDate;
	public static volatile SingularAttribute<MDemandCollectSchedule, Long> updateUserId;
	public static volatile SingularAttribute<MDemandCollectSchedule, Integer> version;
	public static volatile SingularAttribute<MDemandCollectSchedule, MDemandCollect> MDemandCollect;
	public static volatile SingularAttribute<MDemandCollectSchedule, TBatchStartupSetting> TBatchStartupSetting;
}
