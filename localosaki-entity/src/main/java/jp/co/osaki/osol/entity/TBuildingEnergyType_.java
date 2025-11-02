package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.035+0900")
@StaticMetamodel(TBuildingEnergyType.class)
public class TBuildingEnergyType_ {
	public static volatile SingularAttribute<TBuildingEnergyType, TBuildingEnergyTypePK> id;
	public static volatile SingularAttribute<TBuildingEnergyType, Timestamp> createDate;
	public static volatile SingularAttribute<TBuildingEnergyType, Long> createUserId;
	public static volatile SingularAttribute<TBuildingEnergyType, Integer> delFlg;
	public static volatile SingularAttribute<TBuildingEnergyType, Integer> displayOrder;
	public static volatile SingularAttribute<TBuildingEnergyType, Timestamp> updateDate;
	public static volatile SingularAttribute<TBuildingEnergyType, Long> updateUserId;
	public static volatile SingularAttribute<TBuildingEnergyType, Integer> version;
	public static volatile ListAttribute<TBuildingEnergyType, TAvailableEnergy> TAvailableEnergies;
}
