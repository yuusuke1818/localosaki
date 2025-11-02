package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-20T13:40:24.524+0900")
@StaticMetamodel(MProductControlLoad.class)
public class MProductControlLoad_ {
	public static volatile SingularAttribute<MProductControlLoad, MProductControlLoadPK> id;
	public static volatile SingularAttribute<MProductControlLoad, String> controlLoadCircuit;
	public static volatile SingularAttribute<MProductControlLoad, Timestamp> createDate;
	public static volatile SingularAttribute<MProductControlLoad, Long> createUserId;
	public static volatile SingularAttribute<MProductControlLoad, Integer> delFlg;
	public static volatile SingularAttribute<MProductControlLoad, Integer> demandControlFlg;
	public static volatile SingularAttribute<MProductControlLoad, Integer> eventControlFlg;
	public static volatile SingularAttribute<MProductControlLoad, Integer> manualLoadControlFlg;
	public static volatile SingularAttribute<MProductControlLoad, Integer> scheduleControlFlg;
	public static volatile SingularAttribute<MProductControlLoad, Timestamp> updateDate;
	public static volatile SingularAttribute<MProductControlLoad, Long> updateUserId;
	public static volatile SingularAttribute<MProductControlLoad, Integer> version;
	public static volatile SingularAttribute<MProductControlLoad, MProductSpec> MProductSpec;
}
