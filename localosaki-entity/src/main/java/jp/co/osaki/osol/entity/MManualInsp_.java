package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.227+0900")
@StaticMetamodel(MManualInsp.class)
public class MManualInsp_ {
	public static volatile SingularAttribute<MManualInsp, MManualInspPK> id;
	public static volatile SingularAttribute<MManualInsp, String> commandFlg;
	public static volatile SingularAttribute<MManualInsp, Timestamp> createDate;
	public static volatile SingularAttribute<MManualInsp, Long> createUserId;
	public static volatile SingularAttribute<MManualInsp, Timestamp> recDate;
	public static volatile SingularAttribute<MManualInsp, String> recMan;
	public static volatile SingularAttribute<MManualInsp, String> srvEnt;
	public static volatile SingularAttribute<MManualInsp, Timestamp> updateDate;
	public static volatile SingularAttribute<MManualInsp, Long> updateUserId;
	public static volatile SingularAttribute<MManualInsp, Integer> version;
	public static volatile SingularAttribute<MManualInsp, MMeter> MMeter;
}
