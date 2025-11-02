package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-01-18T17:35:24.073+0900")
@StaticMetamodel(MGraphElement.class)
public class MGraphElement_ {
	public static volatile SingularAttribute<MGraphElement, MGraphElementPK> id;
	public static volatile SingularAttribute<MGraphElement, Timestamp> createDate;
	public static volatile SingularAttribute<MGraphElement, Long> createUserId;
	public static volatile SingularAttribute<MGraphElement, Integer> delFlg;
	public static volatile SingularAttribute<MGraphElement, Integer> displayOrder;
	public static volatile SingularAttribute<MGraphElement, String> graphColorCode;
	public static volatile SingularAttribute<MGraphElement, String> graphElementType;
	public static volatile SingularAttribute<MGraphElement, Long> graphLineGroupId;
	public static volatile SingularAttribute<MGraphElement, String> graphLineNo;
	public static volatile SingularAttribute<MGraphElement, String> graphPointNo;
	public static volatile SingularAttribute<MGraphElement, Long> graphSmId;
	public static volatile SingularAttribute<MGraphElement, Timestamp> updateDate;
	public static volatile SingularAttribute<MGraphElement, Long> updateUserId;
	public static volatile SingularAttribute<MGraphElement, Integer> version;
	public static volatile SingularAttribute<MGraphElement, MGraph> MGraph;
}
