package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.204+0900")
@StaticMetamodel(MAmedasObservatory.class)
public class MAmedasObservatory_ {
	public static volatile SingularAttribute<MAmedasObservatory, String> amedasObservatoryNo;
	public static volatile SingularAttribute<MAmedasObservatory, String> amedasObservatoryName;
	public static volatile SingularAttribute<MAmedasObservatory, Timestamp> createDate;
	public static volatile SingularAttribute<MAmedasObservatory, Long> createUserId;
	public static volatile SingularAttribute<MAmedasObservatory, Integer> delFlg;
	public static volatile SingularAttribute<MAmedasObservatory, Timestamp> updateDate;
	public static volatile SingularAttribute<MAmedasObservatory, Long> updateUserId;
	public static volatile SingularAttribute<MAmedasObservatory, Integer> version;
	public static volatile ListAttribute<MAmedasObservatory, MBuildingDm> MBuildingDms;
	public static volatile ListAttribute<MAmedasObservatory, TAmedasWeather> TAmedasWeathers;
}
