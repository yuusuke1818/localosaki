package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-08-29T14:00:21.858+0900")
@StaticMetamodel(TAielMasterAreaSettingName.class)
public class TAielMasterAreaSettingName_ {
	public static volatile SingularAttribute<TAielMasterAreaSettingName, TAielMasterAreaSettingNamePK> id;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, String> areaName;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, Timestamp> createDate;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, Long> createUserId;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, String> sensorName1;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, String> sensorName2;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, String> sensorName3;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, String> sensorName4;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, Timestamp> updateDate;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, Long> updateUserId;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, Integer> version;
	public static volatile SingularAttribute<TAielMasterAreaSettingName, MSmPrm> MSmPrm;
}
