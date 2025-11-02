package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-07-27T18:06:21.512+0900")
@StaticMetamodel(MSystem.class)
public class MSystem_ {
	public static volatile SingularAttribute<MSystem, Integer> systemKey;
	public static volatile SingularAttribute<MSystem, String> coefficientNote;
	public static volatile SingularAttribute<MSystem, Date> coefficientUpdate;
	public static volatile SingularAttribute<MSystem, Timestamp> createDate;
	public static volatile SingularAttribute<MSystem, Long> createUserId;
	public static volatile SingularAttribute<MSystem, Timestamp> updateDate;
	public static volatile SingularAttribute<MSystem, Long> updateUserId;
	public static volatile SingularAttribute<MSystem, Integer> version;
}
