package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.122+0900")
@StaticMetamodel(MAllNightDay.class)
public class MAllNightDay_ {
	public static volatile SingularAttribute<MAllNightDay, MAllNightDayPK> id;
	public static volatile SingularAttribute<MAllNightDay, Timestamp> createDate;
	public static volatile SingularAttribute<MAllNightDay, Long> createUserId;
	public static volatile SingularAttribute<MAllNightDay, Timestamp> recDate;
	public static volatile SingularAttribute<MAllNightDay, String> recMan;
	public static volatile SingularAttribute<MAllNightDay, Timestamp> updateDate;
	public static volatile SingularAttribute<MAllNightDay, Long> updateUserId;
	public static volatile SingularAttribute<MAllNightDay, Integer> version;
	public static volatile SingularAttribute<MAllNightDay, TBuilding> TBuilding;
}
