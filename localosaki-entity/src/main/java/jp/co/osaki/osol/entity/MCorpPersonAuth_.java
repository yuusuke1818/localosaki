package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.816+0900")
@StaticMetamodel(MCorpPersonAuth.class)
public class MCorpPersonAuth_ {
	public static volatile SingularAttribute<MCorpPersonAuth, MCorpPersonAuthPK> id;
	public static volatile SingularAttribute<MCorpPersonAuth, Integer> authorityFlg;
	public static volatile SingularAttribute<MCorpPersonAuth, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpPersonAuth, Long> createUserId;
	public static volatile SingularAttribute<MCorpPersonAuth, Integer> delFlg;
	public static volatile SingularAttribute<MCorpPersonAuth, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpPersonAuth, Long> updateUserId;
	public static volatile SingularAttribute<MCorpPersonAuth, Integer> version;
	public static volatile SingularAttribute<MCorpPersonAuth, MAuth> MAuth;
	public static volatile SingularAttribute<MCorpPersonAuth, MCorpPerson> MCorpPerson;
}
