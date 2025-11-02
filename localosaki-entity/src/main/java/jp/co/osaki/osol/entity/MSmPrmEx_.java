package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-06-11T09:40:48.595+0900")
@StaticMetamodel(MSmPrmEx.class)
public class MSmPrmEx_ {
    public static volatile SingularAttribute<MSmPrmEx, Long> smId;
    public static volatile SingularAttribute<MSmPrmEx, Timestamp> createDate;
    public static volatile SingularAttribute<MSmPrmEx, Long> createUserId;
    public static volatile SingularAttribute<MSmPrmEx, Integer> delFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> dmTargetNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> kwCalcNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> maxLimitValNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> nothingDataNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> tempHumidSensorNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> lineValMinusNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Integer> waterOilLeakNotCheckFlg;
    public static volatile SingularAttribute<MSmPrmEx, Timestamp> updateDate;
    public static volatile SingularAttribute<MSmPrmEx, Long> updateUserId;
    public static volatile SingularAttribute<MSmPrmEx, Integer> version;
    public static volatile SingularAttribute<MSmPrmEx, MSmPrm> MSmPrm;
}
