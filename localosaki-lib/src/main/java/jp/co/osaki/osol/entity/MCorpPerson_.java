package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.811+0900")
@StaticMetamodel(MCorpPerson.class)
public class MCorpPerson_ {
	public static volatile SingularAttribute<MCorpPerson, MCorpPersonPK> id;
	public static volatile SingularAttribute<MCorpPerson, String> authorityType;
	public static volatile SingularAttribute<MCorpPerson, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpPerson, Long> createUserId;
	public static volatile SingularAttribute<MCorpPerson, Integer> delFlg;
	public static volatile SingularAttribute<MCorpPerson, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpPerson, Long> updateUserId;
	public static volatile SingularAttribute<MCorpPerson, Integer> version;
	public static volatile SingularAttribute<MCorpPerson, MCorp> MCorp;
	public static volatile SingularAttribute<MCorpPerson, MPerson> MPerson;
	public static volatile ListAttribute<MCorpPerson, MCorpPersonAuth> MCorpPersonAuths;
}
