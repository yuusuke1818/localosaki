package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.556+0900")
@StaticMetamodel(MSmCollectManage.class)
public class MSmCollectManage_ {
	public static volatile SingularAttribute<MSmCollectManage, MSmCollectManagePK> id;
	public static volatile SingularAttribute<MSmCollectManage, Integer> collectFlg;
	public static volatile SingularAttribute<MSmCollectManage, Timestamp> createDate;
	public static volatile SingularAttribute<MSmCollectManage, Long> createUserId;
	public static volatile SingularAttribute<MSmCollectManage, Integer> delFlg;
	public static volatile SingularAttribute<MSmCollectManage, String> note;
	public static volatile SingularAttribute<MSmCollectManage, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmCollectManage, Long> updateUserId;
	public static volatile SingularAttribute<MSmCollectManage, Integer> version;
	public static volatile SingularAttribute<MSmCollectManage, MSmPrm> MSmPrm;
	public static volatile SingularAttribute<MSmCollectManage, TBatchStartupSetting> TBatchStartupSetting;
}
