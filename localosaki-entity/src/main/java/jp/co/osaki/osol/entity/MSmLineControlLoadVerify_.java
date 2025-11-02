package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.713+0900")
@StaticMetamodel(MSmLineControlLoadVerify.class)
public class MSmLineControlLoadVerify_ {
	public static volatile SingularAttribute<MSmLineControlLoadVerify, MSmLineControlLoadVerifyPK> id;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Timestamp> createDate;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Long> createUserId;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Integer> delFlg;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, BigDecimal> dmLoadShutOffCapacity;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, BigDecimal> event1LoadShutOffCapacity;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, BigDecimal> event2LoadShutOffCapacity;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Long> updateUserId;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, Integer> version;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, MSmControlLoad> MSmControlLoad;
	public static volatile SingularAttribute<MSmLineControlLoadVerify, MSmLineVerify> MSmLineVerify;
}
