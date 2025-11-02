package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.096+0900")
@StaticMetamodel(TFreonFillingReport.class)
public class TFreonFillingReport_ {
	public static volatile SingularAttribute<TFreonFillingReport, TFreonFillingReportPK> id;
	public static volatile SingularAttribute<TFreonFillingReport, Timestamp> createDate;
	public static volatile SingularAttribute<TFreonFillingReport, Long> createUserId;
	public static volatile SingularAttribute<TFreonFillingReport, Integer> delFlg;
	public static volatile SingularAttribute<TFreonFillingReport, BigDecimal> fillingAmount;
	public static volatile SingularAttribute<TFreonFillingReport, Date> fillingDate;
	public static volatile SingularAttribute<TFreonFillingReport, BigDecimal> leakageAmount;
	public static volatile SingularAttribute<TFreonFillingReport, BigDecimal> recoveredAmount;
	public static volatile SingularAttribute<TFreonFillingReport, Timestamp> updateDate;
	public static volatile SingularAttribute<TFreonFillingReport, Long> updateUserId;
	public static volatile SingularAttribute<TFreonFillingReport, Integer> version;
	public static volatile SingularAttribute<TFreonFillingReport, MFacility> MFacility;
	public static volatile SingularAttribute<TFreonFillingReport, TMaintenanceRequest> TMaintenanceRequest;
}
