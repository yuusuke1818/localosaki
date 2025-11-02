package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:57.734+0900")
@StaticMetamodel(MRepeater.class)
public class MRepeater_ {
	public static volatile SingularAttribute<MRepeater, MRepeaterPK> id;
	public static volatile SingularAttribute<MRepeater, String> commandFlg;
	public static volatile SingularAttribute<MRepeater, Timestamp> createDate;
	public static volatile SingularAttribute<MRepeater, Long> createUserId;
	public static volatile SingularAttribute<MRepeater, String> memo;
	public static volatile SingularAttribute<MRepeater, Timestamp> recDate;
	public static volatile SingularAttribute<MRepeater, String> recMan;
	public static volatile SingularAttribute<MRepeater, String> repeaterId;
	public static volatile SingularAttribute<MRepeater, String> srvEnt;
	public static volatile SingularAttribute<MRepeater, Timestamp> updateDate;
	public static volatile SingularAttribute<MRepeater, Long> updateUserId;
	public static volatile SingularAttribute<MRepeater, Integer> version;
	public static volatile SingularAttribute<MRepeater, MDevPrm> MDevPrm;
}
