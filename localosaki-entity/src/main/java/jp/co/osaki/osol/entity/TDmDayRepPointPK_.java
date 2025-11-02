package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.527+0900")
@StaticMetamodel(TDmDayRepPointPK.class)
public class TDmDayRepPointPK_ {
	public static volatile SingularAttribute<TDmDayRepPointPK, String> corpId;
	public static volatile SingularAttribute<TDmDayRepPointPK, Long> buildingId;
	public static volatile SingularAttribute<TDmDayRepPointPK, Long> smId;
	public static volatile SingularAttribute<TDmDayRepPointPK, Date> measurementDate;
	public static volatile SingularAttribute<TDmDayRepPointPK, BigDecimal> jigenNo;
	public static volatile SingularAttribute<TDmDayRepPointPK, String> pointNo;
}
