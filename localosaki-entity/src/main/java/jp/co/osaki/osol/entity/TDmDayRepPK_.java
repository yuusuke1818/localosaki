package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.520+0900")
@StaticMetamodel(TDmDayRepPK.class)
public class TDmDayRepPK_ {
	public static volatile SingularAttribute<TDmDayRepPK, String> corpId;
	public static volatile SingularAttribute<TDmDayRepPK, Long> buildingId;
	public static volatile SingularAttribute<TDmDayRepPK, Date> measurementDate;
	public static volatile SingularAttribute<TDmDayRepPK, BigDecimal> jigenNo;
}
