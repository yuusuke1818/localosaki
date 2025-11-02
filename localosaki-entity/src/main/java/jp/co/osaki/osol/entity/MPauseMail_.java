package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-09-06T16:13:19.334+0900")
@StaticMetamodel(MPauseMail.class)
public class MPauseMail_ {
    public static volatile SingularAttribute<MPauseMail, String> devId;
    public static volatile SingularAttribute<MPauseMail, String> autoinspComp;
    public static volatile SingularAttribute<MPauseMail, String> autoinspDateSetting;
    public static volatile SingularAttribute<MPauseMail, String> autoinspMissing;
    public static volatile SingularAttribute<MPauseMail, String> commandErr;
    public static volatile SingularAttribute<MPauseMail, String> concentErr;
    public static volatile SingularAttribute<MPauseMail, Timestamp> createDate;
    public static volatile SingularAttribute<MPauseMail, Long> createUserId;
    public static volatile SingularAttribute<MPauseMail, String> dmvErr;
    public static volatile SingularAttribute<MPauseMail, String> exam;
    public static volatile SingularAttribute<MPauseMail, String> loadsurveyCsvErr;
    public static volatile SingularAttribute<MPauseMail, String> loadsurveyErr;
    public static volatile SingularAttribute<MPauseMail, String> manualInsp;
    public static volatile SingularAttribute<MPauseMail, String> meterErr;
    public static volatile SingularAttribute<MPauseMail, Timestamp> recDate;
    public static volatile SingularAttribute<MPauseMail, String> recMan;
    public static volatile SingularAttribute<MPauseMail, String> termErr;
    public static volatile SingularAttribute<MPauseMail, String> unsettledCsvErr;
    public static volatile SingularAttribute<MPauseMail, Timestamp> updateDate;
    public static volatile SingularAttribute<MPauseMail, Long> updateUserId;
    public static volatile SingularAttribute<MPauseMail, Integer> version;
    public static volatile SingularAttribute<MPauseMail, String> workHstErr;
    public static volatile SingularAttribute<MPauseMail, MDevPrm> MDevPrm;
}
