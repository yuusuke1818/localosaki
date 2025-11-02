package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-01T19:57:50.385+0900")
@StaticMetamodel(MUiScreen.class)
public class MUiScreen_ {
	public static volatile SingularAttribute<MUiScreen, String> uiScreenId;
	public static volatile SingularAttribute<MUiScreen, Timestamp> createDate;
	public static volatile SingularAttribute<MUiScreen, Long> createUserId;
	public static volatile SingularAttribute<MUiScreen, Integer> displayOrder;
	public static volatile SingularAttribute<MUiScreen, String> uiScreenBean;
	public static volatile SingularAttribute<MUiScreen, String> uiScreenName;
	public static volatile SingularAttribute<MUiScreen, Timestamp> updateDate;
	public static volatile SingularAttribute<MUiScreen, Long> updateUserId;
	public static volatile SingularAttribute<MUiScreen, Integer> version;
	public static volatile ListAttribute<MUiScreen, MPerson> MPersons;
}
