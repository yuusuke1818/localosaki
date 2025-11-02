package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-18T13:54:34.068+0900")
@StaticMetamodel(MLineTimeStandard.class)
public class MLineTimeStandard_ {
	public static volatile SingularAttribute<MLineTimeStandard, MLineTimeStandardPK> id;
	public static volatile SingularAttribute<MLineTimeStandard, Timestamp> createDate;
	public static volatile SingularAttribute<MLineTimeStandard, Long> createUserId;
	public static volatile SingularAttribute<MLineTimeStandard, Integer> delFlg;
	public static volatile SingularAttribute<MLineTimeStandard, BigDecimal> lineLimitStandardKw;
	public static volatile SingularAttribute<MLineTimeStandard, BigDecimal> lineLowerStandardKw;
	public static volatile SingularAttribute<MLineTimeStandard, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineTimeStandard, Long> updateUserId;
	public static volatile SingularAttribute<MLineTimeStandard, Integer> version;
	public static volatile SingularAttribute<MLineTimeStandard, MBuildingDm> MBuildingDm;
	public static volatile SingularAttribute<MLineTimeStandard, MLine> MLine;
}
