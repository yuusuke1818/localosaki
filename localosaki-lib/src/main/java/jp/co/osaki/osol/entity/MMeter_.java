package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-10-20T12:05:12.361+0900")
@StaticMetamodel(MMeter.class)
public class MMeter_ {
    public static volatile SingularAttribute<MMeter, MMeterPK> id;
    public static volatile SingularAttribute<MMeter, String> alarm;
    public static volatile SingularAttribute<MMeter, String> alertPauseEnd;
    public static volatile SingularAttribute<MMeter, BigDecimal> alertPauseFlg;
    public static volatile SingularAttribute<MMeter, String> alertPauseStart;
    public static volatile SingularAttribute<MMeter, BigDecimal> basicPrice;
    public static volatile SingularAttribute<MMeter, String> comMeter;
    public static volatile SingularAttribute<MMeter, String> commandFlg;
    public static volatile SingularAttribute<MMeter, BigDecimal> concentId;
    public static volatile SingularAttribute<MMeter, Timestamp> createDate;
    public static volatile SingularAttribute<MMeter, Long> createUserId;
    public static volatile SingularAttribute<MMeter, BigDecimal> currentData;
    public static volatile SingularAttribute<MMeter, String> currentDataChg;
    public static volatile SingularAttribute<MMeter, Integer> delFlg;
    public static volatile SingularAttribute<MMeter, String> dispYearFlg;
    public static volatile SingularAttribute<MMeter, String> examEndYm;
    public static volatile SingularAttribute<MMeter, String> examNotice;
    public static volatile SingularAttribute<MMeter, String> hop1Id;
    public static volatile SingularAttribute<MMeter, String> hop2Id;
    public static volatile SingularAttribute<MMeter, String> hop3Id;
    public static volatile SingularAttribute<MMeter, BigDecimal> ifType;
    public static volatile SingularAttribute<MMeter, String> memo;
    public static volatile SingularAttribute<MMeter, String> meterId;
    public static volatile SingularAttribute<MMeter, String> meterIdOld;
    public static volatile SingularAttribute<MMeter, BigDecimal> meterPresSitu;
    public static volatile SingularAttribute<MMeter, BigDecimal> meterSta;
    public static volatile SingularAttribute<MMeter, String> meterStaMemo;
    public static volatile SingularAttribute<MMeter, Long> meterType;
    public static volatile SingularAttribute<MMeter, BigDecimal> multi;
    public static volatile SingularAttribute<MMeter, String> name;
    public static volatile SingularAttribute<MMeter, String> openMode;
    public static volatile SingularAttribute<MMeter, String> pollingId;
    public static volatile SingularAttribute<MMeter, String> pulseType;
    public static volatile SingularAttribute<MMeter, String> pulseTypeChg;
    public static volatile SingularAttribute<MMeter, BigDecimal> pulseWeight;
    public static volatile SingularAttribute<MMeter, String> pulseWeightChg;
    public static volatile SingularAttribute<MMeter, Timestamp> recDate;
    public static volatile SingularAttribute<MMeter, String> recMan;
    public static volatile SingularAttribute<MMeter, Timestamp> reserveInspDate;
    public static volatile SingularAttribute<MMeter, String> srvEnt;
    public static volatile SingularAttribute<MMeter, String> termAddr;
    public static volatile SingularAttribute<MMeter, BigDecimal> termSta;
    public static volatile SingularAttribute<MMeter, Timestamp> updateDate;
    public static volatile SingularAttribute<MMeter, Long> updateUserId;
    public static volatile SingularAttribute<MMeter, Integer> version;
    public static volatile SingularAttribute<MMeter, String> wirelessId;
    public static volatile SingularAttribute<MMeter, String> wirelessType;
    public static volatile ListAttribute<MMeter, MManualInsp> MManualInsps;
    public static volatile SingularAttribute<MMeter, MDevPrm> MDevPrm;
    public static volatile ListAttribute<MMeter, MMeterGroup> MMeterGroups;
    public static volatile ListAttribute<MMeter, MMeterLoadlimit> MMeterLoadlimits;
    public static volatile ListAttribute<MMeter, TBuildDevMeterRelation> TBuildDevMeterRelations;
}
