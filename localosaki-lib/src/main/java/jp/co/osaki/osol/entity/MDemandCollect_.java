package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-11-15T14:21:06.019+0900")
@StaticMetamodel(MDemandCollect.class)
public class MDemandCollect_ {
	public static volatile SingularAttribute<MDemandCollect, String> demandCollectCd;
	public static volatile SingularAttribute<MDemandCollect, Timestamp> createDate;
	public static volatile SingularAttribute<MDemandCollect, Long> createUserId;
	public static volatile SingularAttribute<MDemandCollect, String> demandCollectName;
	public static volatile SingularAttribute<MDemandCollect, Timestamp> updateDate;
	public static volatile SingularAttribute<MDemandCollect, Long> updateUserId;
	public static volatile SingularAttribute<MDemandCollect, Integer> version;
	public static volatile ListAttribute<MDemandCollect, MDemandCollectProduct> MDemandCollectProducts;
	public static volatile ListAttribute<MDemandCollect, MDemandCollectSchedule> MDemandCollectSchedules;
}
