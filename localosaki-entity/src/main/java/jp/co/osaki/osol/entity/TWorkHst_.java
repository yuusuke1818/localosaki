package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.659+0900")
@StaticMetamodel(TWorkHst.class)
public class TWorkHst_ {
	public static volatile SingularAttribute<TWorkHst, TWorkHstPK> id;
	public static volatile SingularAttribute<TWorkHst, Timestamp> createDate;
	public static volatile SingularAttribute<TWorkHst, Long> createUserId;
	public static volatile SingularAttribute<TWorkHst, String> filePath;
	public static volatile SingularAttribute<TWorkHst, String> recMan;
	public static volatile SingularAttribute<TWorkHst, String> srvEnt;
	public static volatile SingularAttribute<TWorkHst, String> tableName;
	public static volatile SingularAttribute<TWorkHst, Timestamp> updateDate;
	public static volatile SingularAttribute<TWorkHst, Long> updateUserId;
	public static volatile SingularAttribute<TWorkHst, Integer> version;
	public static volatile SingularAttribute<TWorkHst, Timestamp> writeDate;
}
