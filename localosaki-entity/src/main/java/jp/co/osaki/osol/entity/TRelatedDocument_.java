package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.175+0900")
@StaticMetamodel(TRelatedDocument.class)
public class TRelatedDocument_ {
	public static volatile SingularAttribute<TRelatedDocument, TRelatedDocumentPK> id;
	public static volatile SingularAttribute<TRelatedDocument, Integer> buildingFlg;
	public static volatile SingularAttribute<TRelatedDocument, Timestamp> createDate;
	public static volatile SingularAttribute<TRelatedDocument, Long> createUserId;
	public static volatile SingularAttribute<TRelatedDocument, Integer> delFlg;
	public static volatile SingularAttribute<TRelatedDocument, Integer> facilityFlg;
	public static volatile SingularAttribute<TRelatedDocument, String> fileName;
	public static volatile SingularAttribute<TRelatedDocument, String> filePath;
	public static volatile SingularAttribute<TRelatedDocument, Long> fileSize;
	public static volatile SingularAttribute<TRelatedDocument, String> fileType;
	public static volatile SingularAttribute<TRelatedDocument, Integer> maintenanceFlg;
	public static volatile SingularAttribute<TRelatedDocument, Timestamp> updateDate;
	public static volatile SingularAttribute<TRelatedDocument, Long> updateUserId;
	public static volatile SingularAttribute<TRelatedDocument, Integer> version;
	public static volatile ListAttribute<TRelatedDocument, TBuildingDocument> TBuildingDocuments;
	public static volatile ListAttribute<TRelatedDocument, TFacilityDocument> TFacilityDocuments;
	public static volatile ListAttribute<TRelatedDocument, TMaintenanceDocument> TMaintenanceDocuments;
}
