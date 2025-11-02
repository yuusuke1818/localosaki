package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-08-22T10:54:11.780+0900")
@StaticMetamodel(MLoginIpAddr.class)
public class MLoginIpAddr_ {
    public static volatile SingularAttribute<MLoginIpAddr, MLoginIpAddrPK> id;
    public static volatile SingularAttribute<MLoginIpAddr, Timestamp> createDate;
    public static volatile SingularAttribute<MLoginIpAddr, Long> createUserId;
    public static volatile SingularAttribute<MLoginIpAddr, Integer> delFlg;
    public static volatile SingularAttribute<MLoginIpAddr, String> ipAddress;
    public static volatile SingularAttribute<MLoginIpAddr, String> loginPermitStatus;
    public static volatile SingularAttribute<MLoginIpAddr, String> loginPermitTarget;
    public static volatile SingularAttribute<MLoginIpAddr, String> memo;
    public static volatile SingularAttribute<MLoginIpAddr, Timestamp> updateDate;
    public static volatile SingularAttribute<MLoginIpAddr, Long> updateUserId;
    public static volatile SingularAttribute<MLoginIpAddr, Integer> version;
    public static volatile SingularAttribute<MLoginIpAddr, String> ipAddress1;
    public static volatile SingularAttribute<MLoginIpAddr, String> ipAddress2;
    public static volatile SingularAttribute<MLoginIpAddr, String> ipAddress3;
    public static volatile SingularAttribute<MLoginIpAddr, String> ipAddress4;
    public static volatile SingularAttribute<MLoginIpAddr, String> sortIpAddress;
    public static volatile SingularAttribute<MLoginIpAddr, MCorp> MCorp;
}
