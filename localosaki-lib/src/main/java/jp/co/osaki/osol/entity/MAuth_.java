package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.350+0900")
@StaticMetamodel(MAuth.class)
public class MAuth_ {
	public static volatile SingularAttribute<MAuth, String> authorityCd;
	public static volatile SingularAttribute<MAuth, String> authorityName;
	public static volatile SingularAttribute<MAuth, Timestamp> createDate;
	public static volatile SingularAttribute<MAuth, Long> createUserId;
	public static volatile SingularAttribute<MAuth, Timestamp> updateDate;
	public static volatile SingularAttribute<MAuth, Long> updateUserId;
	public static volatile SingularAttribute<MAuth, Integer> version;
	public static volatile ListAttribute<MAuth, MCorpPersonAuth> MCorpPersonAuths;
}
