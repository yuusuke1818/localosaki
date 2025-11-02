package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:57.193+0900")
@StaticMetamodel(MConcentrator.class)
public class MConcentrator_ {
	public static volatile SingularAttribute<MConcentrator, MConcentratorPK> id;
	public static volatile SingularAttribute<MConcentrator, String> commandFlg;
	public static volatile SingularAttribute<MConcentrator, BigDecimal> concentSta;
	public static volatile SingularAttribute<MConcentrator, Timestamp> createDate;
	public static volatile SingularAttribute<MConcentrator, Long> createUserId;
	public static volatile SingularAttribute<MConcentrator, BigDecimal> ifType;
	public static volatile SingularAttribute<MConcentrator, String> ipAddr;
	public static volatile SingularAttribute<MConcentrator, String> memo;
	public static volatile SingularAttribute<MConcentrator, String> name;
	public static volatile SingularAttribute<MConcentrator, Timestamp> recDate;
	public static volatile SingularAttribute<MConcentrator, String> recMan;
	public static volatile SingularAttribute<MConcentrator, String> srvEnt;
	public static volatile SingularAttribute<MConcentrator, Timestamp> updateDate;
	public static volatile SingularAttribute<MConcentrator, Long> updateUserId;
	public static volatile SingularAttribute<MConcentrator, Integer> version;
	public static volatile SingularAttribute<MConcentrator, MDevPrm> MDevPrm;
}
