package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-07-30T19:41:30.190+0900")
@StaticMetamodel(MLineType.class)
public class MLineType_ {
	public static volatile SingularAttribute<MLineType, String> lineType;
	public static volatile SingularAttribute<MLineType, Integer> airValidFlg;
	public static volatile SingularAttribute<MLineType, Timestamp> createDate;
	public static volatile SingularAttribute<MLineType, Long> createUserId;
	public static volatile SingularAttribute<MLineType, Integer> delFlg;
	public static volatile SingularAttribute<MLineType, Integer> displayOrder;
	public static volatile SingularAttribute<MLineType, Integer> dmValidFlg;
	public static volatile SingularAttribute<MLineType, Integer> eventValidFlg;
	public static volatile SingularAttribute<MLineType, String> lineTypeName;
	public static volatile SingularAttribute<MLineType, Timestamp> updateDate;
	public static volatile SingularAttribute<MLineType, Long> updateUserId;
	public static volatile SingularAttribute<MLineType, Integer> version;
	public static volatile ListAttribute<MLineType, MLine> MLines;
}
