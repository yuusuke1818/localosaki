package jp.co.osaki.osol.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.113+0900")
@StaticMetamodel(TMaintenanceDocumentPK.class)
public class TMaintenanceDocumentPK_ {
	public static volatile SingularAttribute<TMaintenanceDocumentPK, String> corpId;
	public static volatile SingularAttribute<TMaintenanceDocumentPK, Long> buildingId;
	public static volatile SingularAttribute<TMaintenanceDocumentPK, Long> maintenanceId;
	public static volatile SingularAttribute<TMaintenanceDocumentPK, Long> maintenanceRequestId;
	public static volatile SingularAttribute<TMaintenanceDocumentPK, Long> maintenanceHistoryId;
	public static volatile SingularAttribute<TMaintenanceDocumentPK, Long> documentId;
}
