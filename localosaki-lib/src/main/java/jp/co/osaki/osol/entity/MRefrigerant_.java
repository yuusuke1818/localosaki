package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.960+0900")
@StaticMetamodel(MRefrigerant.class)
public class MRefrigerant_ {
	public static volatile SingularAttribute<MRefrigerant, Long> refrigerantId;
	public static volatile SingularAttribute<MRefrigerant, Timestamp> createDate;
	public static volatile SingularAttribute<MRefrigerant, Long> createUserId;
	public static volatile SingularAttribute<MRefrigerant, Integer> displayOrder;
	public static volatile SingularAttribute<MRefrigerant, String> freonName;
	public static volatile SingularAttribute<MRefrigerant, String> refrigerantNo;
	public static volatile SingularAttribute<MRefrigerant, String> refrigerantType;
	public static volatile SingularAttribute<MRefrigerant, Timestamp> updateDate;
	public static volatile SingularAttribute<MRefrigerant, Long> updateUserId;
	public static volatile SingularAttribute<MRefrigerant, Integer> version;
	public static volatile ListAttribute<MRefrigerant, MFacility> MFacilities;
	public static volatile ListAttribute<MRefrigerant, MRefrigerantYearGwp> MRefrigerantYearGwps;
}
