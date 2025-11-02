package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:57.041+0900")
@StaticMetamodel(MAutoInsp.class)
public class MAutoInsp_ {
	public static volatile SingularAttribute<MAutoInsp, MAutoInspPK> id;
	public static volatile SingularAttribute<MAutoInsp, String> commandFlg;
	public static volatile SingularAttribute<MAutoInsp, Timestamp> createDate;
	public static volatile SingularAttribute<MAutoInsp, Long> createUserId;
	public static volatile SingularAttribute<MAutoInsp, String> day;
	public static volatile SingularAttribute<MAutoInsp, BigDecimal> hour;
	public static volatile SingularAttribute<MAutoInsp, String> month;
	public static volatile SingularAttribute<MAutoInsp, Timestamp> recDate;
	public static volatile SingularAttribute<MAutoInsp, String> recMan;
	public static volatile SingularAttribute<MAutoInsp, String> srvEnt;
	public static volatile SingularAttribute<MAutoInsp, Timestamp> updateDate;
	public static volatile SingularAttribute<MAutoInsp, Long> updateUserId;
	public static volatile SingularAttribute<MAutoInsp, Integer> version;
	public static volatile SingularAttribute<MAutoInsp, BigDecimal> waitTime;
	public static volatile SingularAttribute<MAutoInsp, MDevPrm> MDevPrm;
}
