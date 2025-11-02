package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-10T14:38:53.499+0900")
@StaticMetamodel(TApiUseResult.class)
public class TApiUseResult_ {
	public static volatile SingularAttribute<TApiUseResult, TApiUseResultPK> id;
	public static volatile SingularAttribute<TApiUseResult, Integer> apiCount;
	public static volatile SingularAttribute<TApiUseResult, Timestamp> createDate;
	public static volatile SingularAttribute<TApiUseResult, Long> createUserId;
	public static volatile SingularAttribute<TApiUseResult, Timestamp> updateDate;
	public static volatile SingularAttribute<TApiUseResult, Long> updateUserId;
	public static volatile SingularAttribute<TApiUseResult, Integer> version;
	public static volatile SingularAttribute<TApiUseResult, MCorp> MCorp;
}
