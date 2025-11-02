package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.427+0900")
@StaticMetamodel(MSmControlLoad.class)
public class MSmControlLoad_ {
	public static volatile SingularAttribute<MSmControlLoad, MSmControlLoadPK> id;
	public static volatile SingularAttribute<MSmControlLoad, String> controlLoadMemo;
	public static volatile SingularAttribute<MSmControlLoad, String> controlLoadName;
	public static volatile SingularAttribute<MSmControlLoad, String> controlLoadShutOffCapacity;
	public static volatile SingularAttribute<MSmControlLoad, String> controlLoadShutOffRank;
	public static volatile SingularAttribute<MSmControlLoad, String> controlLoadShutOffTime;
	public static volatile SingularAttribute<MSmControlLoad, Timestamp> createDate;
	public static volatile SingularAttribute<MSmControlLoad, Long> createUserId;
	public static volatile SingularAttribute<MSmControlLoad, Integer> delFlg;
	public static volatile SingularAttribute<MSmControlLoad, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmControlLoad, Long> updateUserId;
	public static volatile SingularAttribute<MSmControlLoad, Integer> version;
	public static volatile SingularAttribute<MSmControlLoad, MSmPrm> MSmPrm;
	public static volatile ListAttribute<MSmControlLoad, MSmControlLoadVerify> MSmControlLoadVerifies;
	public static volatile ListAttribute<MSmControlLoad, MSmLineControlLoadVerify> MSmLineControlLoadVerifies;
}
