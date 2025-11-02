package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2019-01-24T08:56:29.925+0900")
@StaticMetamodel(TLoadControlResultPK.class)
public class TLoadControlResultPK_ {
    public static volatile SingularAttribute<TLoadControlResultPK, Long> smId;
    public static volatile SingularAttribute<TLoadControlResultPK, Date> loadControlDate;
    public static volatile SingularAttribute<TLoadControlResultPK, String> controlTarget;
    public static volatile SingularAttribute<TLoadControlResultPK, BigDecimal> controlLoad;
}
