package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-05T15:34:36.769+0900")
@StaticMetamodel(TEventControlLogPK.class)
public class TEventControlLogPK_ {
	public static volatile SingularAttribute<TEventControlLogPK, Long> smId;
	public static volatile SingularAttribute<TEventControlLogPK, String> recordYmdhms;
	public static volatile SingularAttribute<TEventControlLogPK, BigDecimal> controlLoad;
}
