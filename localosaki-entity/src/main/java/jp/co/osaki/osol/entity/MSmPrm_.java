package jp.co.osaki.osol.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-05-07T16:50:19.252+0900")
@StaticMetamodel(MSmPrm.class)
public class MSmPrm_ {
    public static volatile SingularAttribute<MSmPrm, Long> smId;
    public static volatile SingularAttribute<MSmPrm, Integer> aielMasterConnectFlg;
    public static volatile SingularAttribute<MSmPrm, Timestamp> createDate;
    public static volatile SingularAttribute<MSmPrm, Long> createUserId;
    public static volatile SingularAttribute<MSmPrm, Integer> delFlg;
    public static volatile SingularAttribute<MSmPrm, Date> endDate;
    public static volatile SingularAttribute<MSmPrm, String> ipAddress;
    public static volatile SingularAttribute<MSmPrm, String> note;
    public static volatile SingularAttribute<MSmPrm, String> plotAnalogPointNo1;
    public static volatile SingularAttribute<MSmPrm, String> plotAnalogPointNo2;
    public static volatile SingularAttribute<MSmPrm, String> smAddress;
    public static volatile SingularAttribute<MSmPrm, Date> startDate;
    public static volatile SingularAttribute<MSmPrm, Timestamp> updateDate;
    public static volatile SingularAttribute<MSmPrm, Long> updateUserId;
    public static volatile SingularAttribute<MSmPrm, Integer> version;
    public static volatile ListAttribute<MSmPrm, MBuildingSm> MBuildingSms;
    public static volatile ListAttribute<MSmPrm, MSmAirSetting> MSmAirSettings;
    public static volatile ListAttribute<MSmPrm, MSmCollectManage> MSmCollectManages;
    public static volatile ListAttribute<MSmPrm, MSmControlLoad> MSmControlLoads;
    public static volatile ListAttribute<MSmPrm, MSmPoint> MSmPoints;
    public static volatile SingularAttribute<MSmPrm, MProductSpec> MProductSpec;
    public static volatile ListAttribute<MSmPrm, TDemandPowerForecastDay> TDemandPowerForecastDays;
    public static volatile ListAttribute<MSmPrm, TDemandPowerForecastTime> TDemandPowerForecastTimes;
    public static volatile ListAttribute<MSmPrm, TEventControlLog> TEventControlLogs;
    public static volatile ListAttribute<MSmPrm, TLoadControlLog> TLoadControlLogs;
    public static volatile ListAttribute<MSmPrm, TLoadControlResult> TLoadControlResults;
    public static volatile SingularAttribute<MSmPrm, TSmAlarmCall> TSmAlarmCall;
    public static volatile ListAttribute<MSmPrm, TSmControlHolidayLog> TSmControlHolidayLogs;
    public static volatile SingularAttribute<MSmPrm, MSmPrmEx> MSmPrmEx;
    public static volatile ListAttribute<MSmPrm, TTempHumidControlLog> TTempHumidControlLogs;
    public static volatile SingularAttribute<MSmPrm, TSmConnectStatus> TSmConnectStatus;
    public static volatile ListAttribute<MSmPrm, TSmControlScheduleLog> TSmControlScheduleLogs;
}
