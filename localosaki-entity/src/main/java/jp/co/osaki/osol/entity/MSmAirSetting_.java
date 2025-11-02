package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2019-01-23T10:14:09.503+0900")
@StaticMetamodel(MSmAirSetting.class)
public class MSmAirSetting_ {
    public static volatile SingularAttribute<MSmAirSetting, MSmAirSettingPK> id;
    public static volatile SingularAttribute<MSmAirSetting, BigDecimal> coolingDifferential;
    public static volatile SingularAttribute<MSmAirSetting, Timestamp> createDate;
    public static volatile SingularAttribute<MSmAirSetting, Long> createUserId;
    public static volatile SingularAttribute<MSmAirSetting, BigDecimal> heatingDifferential;
    public static volatile SingularAttribute<MSmAirSetting, BigDecimal> linkOutputPortNo;
    public static volatile SingularAttribute<MSmAirSetting, Timestamp> updateDate;
    public static volatile SingularAttribute<MSmAirSetting, Long> updateUserId;
    public static volatile SingularAttribute<MSmAirSetting, Integer> version;
    public static volatile SingularAttribute<MSmAirSetting, MSmPrm> MSmPrm;
}
