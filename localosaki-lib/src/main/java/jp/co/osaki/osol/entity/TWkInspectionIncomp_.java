package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.631+0900")
@StaticMetamodel(TWkInspectionIncomp.class)
public class TWkInspectionIncomp_ {
	public static volatile SingularAttribute<TWkInspectionIncomp, TWkInspectionIncompPK> id;
	public static volatile SingularAttribute<TWkInspectionIncomp, Timestamp> createDate;
	public static volatile SingularAttribute<TWkInspectionIncomp, Long> createUserId;
	public static volatile SingularAttribute<TWkInspectionIncomp, BigDecimal> endFlg;
	public static volatile SingularAttribute<TWkInspectionIncomp, Timestamp> recDate;
	public static volatile SingularAttribute<TWkInspectionIncomp, String> recMan;
	public static volatile SingularAttribute<TWkInspectionIncomp, Timestamp> updateDate;
	public static volatile SingularAttribute<TWkInspectionIncomp, Long> updateUserId;
	public static volatile SingularAttribute<TWkInspectionIncomp, Integer> version;
}
