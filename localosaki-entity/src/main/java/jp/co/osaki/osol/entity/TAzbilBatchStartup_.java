package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-09-04T17:17:38.848+0900")
@StaticMetamodel(TAzbilBatchStartup.class)
public class TAzbilBatchStartup_ {
	public static volatile SingularAttribute<TAzbilBatchStartup, Long> batchId;
	public static volatile SingularAttribute<TAzbilBatchStartup, Timestamp> createDate;
	public static volatile SingularAttribute<TAzbilBatchStartup, Long> createUserId;
	public static volatile SingularAttribute<TAzbilBatchStartup, String> exeDate;
	public static volatile SingularAttribute<TAzbilBatchStartup, Timestamp> updateDate;
	public static volatile SingularAttribute<TAzbilBatchStartup, Long> updateUserId;
	public static volatile SingularAttribute<TAzbilBatchStartup, Integer> version;
}
