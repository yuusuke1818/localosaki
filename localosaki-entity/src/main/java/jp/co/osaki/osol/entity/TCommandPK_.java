package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-07-09T14:20:58.082+0900")
@StaticMetamodel(TCommandPK.class)
public class TCommandPK_ {
	public static volatile SingularAttribute<TCommandPK, String> devId;
	public static volatile SingularAttribute<TCommandPK, String> command;
	public static volatile SingularAttribute<TCommandPK, Date> recDate;
}
