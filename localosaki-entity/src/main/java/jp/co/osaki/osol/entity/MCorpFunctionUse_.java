package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-08T20:04:03.695+0900")
@StaticMetamodel(MCorpFunctionUse.class)
public class MCorpFunctionUse_ {
	public static volatile SingularAttribute<MCorpFunctionUse, MCorpFunctionUsePK> id;
	public static volatile SingularAttribute<MCorpFunctionUse, Timestamp> createDate;
	public static volatile SingularAttribute<MCorpFunctionUse, Long> createUserId;
	public static volatile SingularAttribute<MCorpFunctionUse, Timestamp> updateDate;
	public static volatile SingularAttribute<MCorpFunctionUse, Long> updateUserId;
	public static volatile SingularAttribute<MCorpFunctionUse, Integer> useFlg;
	public static volatile SingularAttribute<MCorpFunctionUse, Integer> version;
	public static volatile SingularAttribute<MCorpFunctionUse, MCorp> MCorp;
	public static volatile SingularAttribute<MCorpFunctionUse, MFunction> MFunction;
}
