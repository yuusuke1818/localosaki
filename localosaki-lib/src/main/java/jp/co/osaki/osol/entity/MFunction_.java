package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-08T20:04:03.704+0900")
@StaticMetamodel(MFunction.class)
public class MFunction_ {
	public static volatile SingularAttribute<MFunction, String> functionCd;
	public static volatile SingularAttribute<MFunction, Timestamp> createDate;
	public static volatile SingularAttribute<MFunction, Long> createUserId;
	public static volatile SingularAttribute<MFunction, String> functionName;
	public static volatile SingularAttribute<MFunction, Timestamp> updateDate;
	public static volatile SingularAttribute<MFunction, Long> updateUserId;
	public static volatile SingularAttribute<MFunction, Integer> version;
	public static volatile ListAttribute<MFunction, MCorpFunctionUse> MCorpFunctionUses;
}
