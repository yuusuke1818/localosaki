package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:56.001+0900")
@StaticMetamodel(MUrlLink.class)
public class MUrlLink_ {
	public static volatile SingularAttribute<MUrlLink, String> urlLink;
	public static volatile SingularAttribute<MUrlLink, Timestamp> createDate;
	public static volatile SingularAttribute<MUrlLink, Long> createUserId;
	public static volatile SingularAttribute<MUrlLink, Integer> displayOrder;
	public static volatile SingularAttribute<MUrlLink, String> title;
	public static volatile SingularAttribute<MUrlLink, Timestamp> updateDate;
	public static volatile SingularAttribute<MUrlLink, Long> updateUserId;
	public static volatile SingularAttribute<MUrlLink, String> url;
	public static volatile SingularAttribute<MUrlLink, Integer> version;
}
