package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:48:52.174+0900")
@StaticMetamodel(MPriceMenu.class)
public class MPriceMenu_ {
	public static volatile SingularAttribute<MPriceMenu, MPriceMenuPK> id;
	public static volatile SingularAttribute<MPriceMenu, Timestamp> createDate;
	public static volatile SingularAttribute<MPriceMenu, Long> createUserId;
	public static volatile SingularAttribute<MPriceMenu, String> menuName;
	public static volatile SingularAttribute<MPriceMenu, Timestamp> recDate;
	public static volatile SingularAttribute<MPriceMenu, String> recMan;
	public static volatile SingularAttribute<MPriceMenu, Timestamp> updateDate;
	public static volatile SingularAttribute<MPriceMenu, Long> updateUserId;
	public static volatile SingularAttribute<MPriceMenu, Integer> version;
	public static volatile SingularAttribute<MPriceMenu, TBuilding> TBuilding;
	public static volatile ListAttribute<MPriceMenu, MPriceMenuPattern> MPriceMenuPatterns;
}
