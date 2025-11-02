package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.109+0900")
@StaticMetamodel(TMaintenanceDocument.class)
public class TMaintenanceDocument_ {
	public static volatile SingularAttribute<TMaintenanceDocument, TMaintenanceDocumentPK> id;
	public static volatile SingularAttribute<TMaintenanceDocument, Timestamp> createDate;
	public static volatile SingularAttribute<TMaintenanceDocument, Long> createUserId;
	public static volatile SingularAttribute<TMaintenanceDocument, Integer> delFlg;
	public static volatile SingularAttribute<TMaintenanceDocument, Timestamp> updateDate;
	public static volatile SingularAttribute<TMaintenanceDocument, Long> updateUserId;
	public static volatile SingularAttribute<TMaintenanceDocument, Integer> version;
	public static volatile SingularAttribute<TMaintenanceDocument, TMaintenanceRequestHistory> TMaintenanceRequestHistory;
	public static volatile SingularAttribute<TMaintenanceDocument, TRelatedDocument> TRelatedDocument;
}
