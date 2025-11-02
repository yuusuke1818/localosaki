package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-18T13:54:34.017+0900")
@StaticMetamodel(MGraph.class)
public class MGraph_ {
	public static volatile SingularAttribute<MGraph, MGraphPK> id;
	public static volatile SingularAttribute<MGraph, Timestamp> createDate;
	public static volatile SingularAttribute<MGraph, Long> createUserId;
	public static volatile SingularAttribute<MGraph, Integer> delFlg;
	public static volatile SingularAttribute<MGraph, String> graphName;
	public static volatile SingularAttribute<MGraph, Integer> initialViewFlg;
	public static volatile SingularAttribute<MGraph, Timestamp> updateDate;
	public static volatile SingularAttribute<MGraph, Long> updateUserId;
	public static volatile SingularAttribute<MGraph, Integer> version;
	public static volatile SingularAttribute<MGraph, MBuildingDm> MBuildingDm;
	public static volatile SingularAttribute<MGraph, MLineGroup> MLineGroup;
	public static volatile ListAttribute<MGraph, MGraphElement> MGraphElements;
}
