package jp.co.osaki.osol.entity;

import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-05T14:32:20.832+0900")
@StaticMetamodel(TSignageContent.class)
public class TSignageContent_ {
	public static volatile SingularAttribute<TSignageContent, TSignageContentPK> id;
	public static volatile SingularAttribute<TSignageContent, Timestamp> createDate;
	public static volatile SingularAttribute<TSignageContent, Long> createUserId;
	public static volatile SingularAttribute<TSignageContent, Integer> delFlg;
	public static volatile SingularAttribute<TSignageContent, Time> displayEndTime;
	public static volatile SingularAttribute<TSignageContent, Integer> displayOrder;
	public static volatile SingularAttribute<TSignageContent, Time> displayStartTime;
	public static volatile SingularAttribute<TSignageContent, String> fontColorCode;
	public static volatile SingularAttribute<TSignageContent, Integer> fontSize;
	public static volatile SingularAttribute<TSignageContent, String> imageFileName;
	public static volatile SingularAttribute<TSignageContent, String> imageFilePath;
	public static volatile SingularAttribute<TSignageContent, String> message;
	public static volatile SingularAttribute<TSignageContent, String> signageContentsType;
	public static volatile SingularAttribute<TSignageContent, Integer> specifyFriday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifyMonday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifySaturday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifySunday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifyThursday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifyTuesday;
	public static volatile SingularAttribute<TSignageContent, Integer> specifyWednesday;
	public static volatile SingularAttribute<TSignageContent, String> title;
	public static volatile SingularAttribute<TSignageContent, Timestamp> updateDate;
	public static volatile SingularAttribute<TSignageContent, Long> updateUserId;
	public static volatile SingularAttribute<TSignageContent, Integer> version;
	public static volatile SingularAttribute<TSignageContent, TBuilding> TBuilding;
}
