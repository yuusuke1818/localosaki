package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-12-13T16:21:51.911+0900")
@StaticMetamodel(MAggregateDmLine.class)
public class MAggregateDmLine_ {
	public static volatile SingularAttribute<MAggregateDmLine, MAggregateDmLinePK> id;
	public static volatile SingularAttribute<MAggregateDmLine, Timestamp> createDate;
	public static volatile SingularAttribute<MAggregateDmLine, Long> createUserId;
	public static volatile SingularAttribute<MAggregateDmLine, Timestamp> updateDate;
	public static volatile SingularAttribute<MAggregateDmLine, Long> updateUserId;
	public static volatile SingularAttribute<MAggregateDmLine, Integer> version;
	public static volatile SingularAttribute<MAggregateDmLine, MBuildingDm> MBuildingDm;
	public static volatile SingularAttribute<MAggregateDmLine, MLine> MLine;
	public static volatile SingularAttribute<MAggregateDmLine, MAggregateDm> MAggregateDm;
}
