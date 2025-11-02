package jp.co.osaki.osol.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-10T14:38:53.502+0900")
@StaticMetamodel(TApiUseResultPK.class)
public class TApiUseResultPK_ {
	public static volatile SingularAttribute<TApiUseResultPK, String> corpId;
	public static volatile SingularAttribute<TApiUseResultPK, Date> useDate;
	public static volatile SingularAttribute<TApiUseResultPK, String> apiKind;
}
