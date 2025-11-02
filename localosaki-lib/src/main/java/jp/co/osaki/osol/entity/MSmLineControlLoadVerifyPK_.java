package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-08-22T10:54:11.842+0900")
@StaticMetamodel(MSmLineControlLoadVerifyPK.class)
public class MSmLineControlLoadVerifyPK_ {
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, String> corpId;
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, Long> lineGroupId;
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, String> lineNo;
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, Long> buildingId;
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, Long> smId;
	public static volatile SingularAttribute<MSmLineControlLoadVerifyPK, BigDecimal> controlLoad;
}
