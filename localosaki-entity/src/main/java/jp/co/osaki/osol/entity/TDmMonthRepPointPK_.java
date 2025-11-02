package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.549+0900")
@StaticMetamodel(TDmMonthRepPointPK.class)
public class TDmMonthRepPointPK_ {
	public static volatile SingularAttribute<TDmMonthRepPointPK, String> corpId;
	public static volatile SingularAttribute<TDmMonthRepPointPK, Long> buildingId;
	public static volatile SingularAttribute<TDmMonthRepPointPK, Long> smId;
	public static volatile SingularAttribute<TDmMonthRepPointPK, Date> measurementDate;
	public static volatile SingularAttribute<TDmMonthRepPointPK, String> pointNo;
}
