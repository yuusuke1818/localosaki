package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-18T13:54:34.039+0900")
@StaticMetamodel(MLineGroupEx.class)
public class MLineGroupEx_ {
	public static volatile SingularAttribute<MLineGroupEx, MLineGroupExPK> id;
	public static volatile SingularAttribute<MLineGroupEx, Timestamp> createDate;
	public static volatile SingularAttribute<MLineGroupEx, Long> createUserId;
	public static volatile SingularAttribute<MLineGroupEx, Integer> delFlg;
	public static volatile SingularAttribute<MLineGroupEx, Integer> etcLineErrorFlg;
	public static volatile SingularAttribute<MLineGroupEx, BigDecimal> etcLineErrorThreshold;
	public static volatile SingularAttribute<MLineGroupEx, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineGroupEx, Long> updateUserId;
	public static volatile SingularAttribute<MLineGroupEx, Integer> version;
	public static volatile SingularAttribute<MLineGroupEx, MLineGroup> MLineGroup;
}
