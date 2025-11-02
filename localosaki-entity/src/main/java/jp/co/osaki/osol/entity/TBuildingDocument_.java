package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.026+0900")
@StaticMetamodel(TBuildingDocument.class)
public class TBuildingDocument_ {
	public static volatile SingularAttribute<TBuildingDocument, TBuildingDocumentPK> id;
	public static volatile SingularAttribute<TBuildingDocument, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingDocument, Long> createUserId;
	public static volatile SingularAttribute<TBuildingDocument, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingDocument, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingDocument, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingDocument, Integer> version;
	public static volatile SingularAttribute<TBuildingDocument, TBuilding> TBuilding;
	public static volatile SingularAttribute<TBuildingDocument, TRelatedDocument> TRelatedDocument;
}
