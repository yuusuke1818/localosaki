package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.523+0900")
@StaticMetamodel(THopidHistory.class)
public class THopidHistory_ {
	public static volatile SingularAttribute<THopidHistory, THopidHistoryPK> id;
	public static volatile SingularAttribute<THopidHistory, Timestamp> createDate;
	public static volatile SingularAttribute<THopidHistory, Long> createUserId;
	public static volatile SingularAttribute<THopidHistory, Timestamp> recDate;
	public static volatile SingularAttribute<THopidHistory, String> recMan;
	public static volatile SingularAttribute<THopidHistory, Timestamp> updateDate;
	public static volatile SingularAttribute<THopidHistory, Long> updateUserId;
	public static volatile SingularAttribute<THopidHistory, Integer> version;
}
