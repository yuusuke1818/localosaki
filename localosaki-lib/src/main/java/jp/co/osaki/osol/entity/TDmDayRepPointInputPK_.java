package jp.co.osaki.osol.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2018-11-15T14:34:43.165+0900")
@StaticMetamodel(TDmDayRepPointInputPK.class)
public class TDmDayRepPointInputPK_ {
    public static volatile SingularAttribute<TDmDayRepPointInputPK, String> corpId;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, Long> buildingId;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, Long> smId;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, Date> measurementDate;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, BigDecimal> jigenNo;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, String> pointNo;
    public static volatile SingularAttribute<TDmDayRepPointInputPK, Long> dmDayRepPointInputId;
}
