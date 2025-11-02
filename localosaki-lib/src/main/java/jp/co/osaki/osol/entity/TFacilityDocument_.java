package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.087+0900")
@StaticMetamodel(TFacilityDocument.class)
public class TFacilityDocument_ {
	public static volatile SingularAttribute<TFacilityDocument, TFacilityDocumentPK> id;
	public static volatile SingularAttribute<TFacilityDocument, Timestamp> createDate;
	public static volatile SingularAttribute<TFacilityDocument, Long> createUserId;
	public static volatile SingularAttribute<TFacilityDocument, Integer> delFlg;
	public static volatile SingularAttribute<TFacilityDocument, Timestamp> updateDate;
	public static volatile SingularAttribute<TFacilityDocument, Long> updateUserId;
	public static volatile SingularAttribute<TFacilityDocument, Integer> version;
	public static volatile SingularAttribute<TFacilityDocument, MFacility> MFacility;
	public static volatile SingularAttribute<TFacilityDocument, TRelatedDocument> TRelatedDocument;
}
