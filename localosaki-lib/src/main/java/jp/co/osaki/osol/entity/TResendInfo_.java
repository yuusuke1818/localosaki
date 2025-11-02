package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-25T11:39:18.584+0900")
@StaticMetamodel(TResendInfo.class)
public class TResendInfo_ {
	public static volatile SingularAttribute<TResendInfo, TResendInfoPK> id;
	public static volatile SingularAttribute<TResendInfo, Timestamp> createDate;
	public static volatile SingularAttribute<TResendInfo, Long> createUserId;
	public static volatile SingularAttribute<TResendInfo, Timestamp> recDate;
	public static volatile SingularAttribute<TResendInfo, String> recMan;
	public static volatile SingularAttribute<TResendInfo, Timestamp> updateDate;
	public static volatile SingularAttribute<TResendInfo, Long> updateUserId;
	public static volatile SingularAttribute<TResendInfo, Integer> version;
}
