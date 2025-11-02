package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.573+0900")
@StaticMetamodel(TMeterData.class)
public class TMeterData_ {
	public static volatile SingularAttribute<TMeterData, TMeterDataPK> id;
	public static volatile SingularAttribute<TMeterData, BigDecimal> ampere1;
	public static volatile SingularAttribute<TMeterData, BigDecimal> ampere2;
	public static volatile SingularAttribute<TMeterData, BigDecimal> ampere3;
	public static volatile SingularAttribute<TMeterData, String> circuitBreaker;
	public static volatile SingularAttribute<TMeterData, Timestamp> createDate;
	public static volatile SingularAttribute<TMeterData, Long> createUserId;
	public static volatile SingularAttribute<TMeterData, String> currentKwh1;
	public static volatile SingularAttribute<TMeterData, String> currentKwh2;
	public static volatile SingularAttribute<TMeterData, Timestamp> measureDate;
	public static volatile SingularAttribute<TMeterData, String> meterId;
	public static volatile SingularAttribute<TMeterData, BigDecimal> momentaryPwr;
	public static volatile SingularAttribute<TMeterData, BigDecimal> powerFactor;
	public static volatile SingularAttribute<TMeterData, Timestamp> recDate;
	public static volatile SingularAttribute<TMeterData, String> recMan;
	public static volatile SingularAttribute<TMeterData, String> srvEnt;
	public static volatile SingularAttribute<TMeterData, Timestamp> updateDate;
	public static volatile SingularAttribute<TMeterData, Long> updateUserId;
	public static volatile SingularAttribute<TMeterData, Integer> version;
	public static volatile SingularAttribute<TMeterData, BigDecimal> voltage12;
	public static volatile SingularAttribute<TMeterData, BigDecimal> voltage13;
	public static volatile SingularAttribute<TMeterData, BigDecimal> voltage23;
}
