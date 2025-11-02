package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-11-13T15:12:57.324+0900")
@StaticMetamodel(MLineGroup.class)
public class MLineGroup_ {
	public static volatile SingularAttribute<MLineGroup, MLineGroupPK> id;
	public static volatile SingularAttribute<MLineGroup, Long> buildingId;
	public static volatile SingularAttribute<MLineGroup, Timestamp> createDate;
	public static volatile SingularAttribute<MLineGroup, Long> createUserId;
	public static volatile SingularAttribute<MLineGroup, Integer> delFlg;
	public static volatile SingularAttribute<MLineGroup, Integer> initialViewFlg;
	public static volatile SingularAttribute<MLineGroup, String> lineGroupName;
	public static volatile SingularAttribute<MLineGroup, String> lineGroupType;
	public static volatile SingularAttribute<MLineGroup, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineGroup, Long> updateUserId;
	public static volatile SingularAttribute<MLineGroup, Integer> version;
	public static volatile ListAttribute<MLineGroup, MGraph> MGraphs;
	public static volatile ListAttribute<MLineGroup, MLine> MLines;
	public static volatile SingularAttribute<MLineGroup, MCorpDm> MCorpDm;
	public static volatile SingularAttribute<MLineGroup, MLineGroupEx> MLineGroupEx;
}
