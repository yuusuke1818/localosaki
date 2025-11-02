package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-13T16:21:51.880+0900")
@StaticMetamodel(MAggregateDm.class)
public class MAggregateDm_ {
	public static volatile SingularAttribute<MAggregateDm, MAggregateDmPK> id;
	public static volatile SingularAttribute<MAggregateDm, String> aggregateDmName;
	public static volatile SingularAttribute<MAggregateDm, Timestamp> createDate;
	public static volatile SingularAttribute<MAggregateDm, Long> createUserId;
	public static volatile SingularAttribute<MAggregateDm, Integer> delFlg;
	public static volatile SingularAttribute<MAggregateDm, String> sumDate;
	public static volatile SingularAttribute<MAggregateDm, Timestamp> updateDate;
	public static volatile SingularAttribute<MAggregateDm, Long> updateUserId;
	public static volatile SingularAttribute<MAggregateDm, Integer> version;
	public static volatile SingularAttribute<MAggregateDm, MBuildingDm> MBuildingDm;
	public static volatile ListAttribute<MAggregateDm, MAggregateDmLine> MAggregateDmLines;
}
