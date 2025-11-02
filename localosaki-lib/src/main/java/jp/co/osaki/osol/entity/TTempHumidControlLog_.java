package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.785+0900")
@StaticMetamodel(TTempHumidControlLog.class)
public class TTempHumidControlLog_ {
	public static volatile SingularAttribute<TTempHumidControlLog, TTempHumidControlLogPK> id;
	public static volatile SingularAttribute<TTempHumidControlLog, String> controlAlarmStatus;
	public static volatile SingularAttribute<TTempHumidControlLog, BigDecimal> controlHumid;
	public static volatile SingularAttribute<TTempHumidControlLog, BigDecimal> controlTemp;
	public static volatile SingularAttribute<TTempHumidControlLog, Timestamp> createDate;
	public static volatile SingularAttribute<TTempHumidControlLog, Long> createUserId;
	public static volatile SingularAttribute<TTempHumidControlLog, Timestamp> updateDate;
	public static volatile SingularAttribute<TTempHumidControlLog, Long> updateUserId;
	public static volatile SingularAttribute<TTempHumidControlLog, Integer> version;
	public static volatile SingularAttribute<TTempHumidControlLog, MSmPrm> MSmPrm;
}
