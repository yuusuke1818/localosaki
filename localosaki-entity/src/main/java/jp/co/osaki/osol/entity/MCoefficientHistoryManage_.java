package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.786+0900")
@StaticMetamodel(MCoefficientHistoryManage.class)
public class MCoefficientHistoryManage_ {
	public static volatile SingularAttribute<MCoefficientHistoryManage, MCoefficientHistoryManagePK> id;
	public static volatile SingularAttribute<MCoefficientHistoryManage, BigDecimal> adjustCo2Coefficient;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> adjustCo2Unit;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> cityGasStandard;
	public static volatile SingularAttribute<MCoefficientHistoryManage, Timestamp> createDate;
	public static volatile SingularAttribute<MCoefficientHistoryManage, Long> createUserId;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> endYm;
	public static volatile SingularAttribute<MCoefficientHistoryManage, BigDecimal> gasCoefficient;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> startYm;
	public static volatile SingularAttribute<MCoefficientHistoryManage, BigDecimal> stdCo2Coefficient;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> stdCo2Unit;
	public static volatile SingularAttribute<MCoefficientHistoryManage, BigDecimal> stdFirstEngCoefficient;
	public static volatile SingularAttribute<MCoefficientHistoryManage, String> stdFirstEngUnit;
	public static volatile SingularAttribute<MCoefficientHistoryManage, Timestamp> updateDate;
	public static volatile SingularAttribute<MCoefficientHistoryManage, Long> updateUserId;
	public static volatile SingularAttribute<MCoefficientHistoryManage, Integer> version;
	public static volatile SingularAttribute<MCoefficientHistoryManage, MEnergy> MEnergy;
}
