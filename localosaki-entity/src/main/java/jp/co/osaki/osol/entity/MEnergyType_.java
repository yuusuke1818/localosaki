package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.838+0900")
@StaticMetamodel(MEnergyType.class)
public class MEnergyType_ {
	public static volatile SingularAttribute<MEnergyType, String> engTypeCd;
	public static volatile SingularAttribute<MEnergyType, Timestamp> createDate;
	public static volatile SingularAttribute<MEnergyType, Long> createUserId;
	public static volatile SingularAttribute<MEnergyType, Integer> displayOrder;
	public static volatile SingularAttribute<MEnergyType, Integer> distributionSuikeiOutFlg;
	public static volatile SingularAttribute<MEnergyType, String> engTypeName;
	public static volatile SingularAttribute<MEnergyType, Timestamp> updateDate;
	public static volatile SingularAttribute<MEnergyType, Long> updateUserId;
	public static volatile SingularAttribute<MEnergyType, Integer> version;
	public static volatile ListAttribute<MEnergyType, MEnergy> MEnergies;
	public static volatile ListAttribute<MEnergyType, MEnergyUseTargetMonthly> MEnergyUseTargetMonthlies;
	public static volatile ListAttribute<MEnergyType, MFacility> MFacilities;
}
