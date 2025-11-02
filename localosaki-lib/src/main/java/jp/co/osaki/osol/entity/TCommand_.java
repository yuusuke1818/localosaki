package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.437+0900")
@StaticMetamodel(TCommand.class)
public class TCommand_ {
	public static volatile SingularAttribute<TCommand, TCommandPK> id;
	public static volatile SingularAttribute<TCommand, Timestamp> createDate;
	public static volatile SingularAttribute<TCommand, Long> createUserId;
	public static volatile SingularAttribute<TCommand, String> linking;
	public static volatile SingularAttribute<TCommand, String> recMan;
	public static volatile SingularAttribute<TCommand, BigDecimal> retryCount;
	public static volatile SingularAttribute<TCommand, String> srvEnt;
	public static volatile SingularAttribute<TCommand, String> tag;
	public static volatile SingularAttribute<TCommand, Timestamp> updateDate;
	public static volatile SingularAttribute<TCommand, Long> updateUserId;
	public static volatile SingularAttribute<TCommand, Integer> version;
}
