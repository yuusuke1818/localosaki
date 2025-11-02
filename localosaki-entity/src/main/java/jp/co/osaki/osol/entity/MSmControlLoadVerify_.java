package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.703+0900")
@StaticMetamodel(MSmControlLoadVerify.class)
public class MSmControlLoadVerify_ {
	public static volatile SingularAttribute<MSmControlLoadVerify, MSmControlLoadVerifyPK> id;
	public static volatile SingularAttribute<MSmControlLoadVerify, BigDecimal> controlLoadRunningHours;
	public static volatile SingularAttribute<MSmControlLoadVerify, Timestamp> createDate;
	public static volatile SingularAttribute<MSmControlLoadVerify, Long> createUserId;
	public static volatile SingularAttribute<MSmControlLoadVerify, Integer> delFlg;
	public static volatile SingularAttribute<MSmControlLoadVerify, Timestamp> updateDate;
	public static volatile SingularAttribute<MSmControlLoadVerify, Long> updateUserId;
	public static volatile SingularAttribute<MSmControlLoadVerify, Integer> version;
	public static volatile SingularAttribute<MSmControlLoadVerify, MSmControlLoad> MSmControlLoad;
}
