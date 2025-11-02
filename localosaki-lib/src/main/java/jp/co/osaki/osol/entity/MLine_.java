package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.361+0900")
@StaticMetamodel(MLine.class)
public class MLine_ {
	public static volatile SingularAttribute<MLine, MLinePK> id;
	public static volatile SingularAttribute<MLine, Timestamp> createDate;
	public static volatile SingularAttribute<MLine, Long> createUserId;
	public static volatile SingularAttribute<MLine, Integer> delFlg;
    public static volatile SingularAttribute<MLine, Integer> inputEnableFlg;
	public static volatile SingularAttribute<MLine, Integer> lineEnableFlg;
	public static volatile SingularAttribute<MLine, String> lineName;
	public static volatile SingularAttribute<MLine, String> lineTarget;
	public static volatile SingularAttribute<MLine, String> lineUnit;
	public static volatile SingularAttribute<MLine, Timestamp> updateDate;
	public static volatile SingularAttribute<MLine, Long> updateUserId;
	public static volatile SingularAttribute<MLine, Integer> version;
	public static volatile SingularAttribute<MLine, MLineGroup> MLineGroup;
	public static volatile SingularAttribute<MLine, MLineType> MLineType;
	public static volatile ListAttribute<MLine, MLineTargetAlarm> MLineTargetAlarms;
	public static volatile ListAttribute<MLine, MLineTimeStandard> MLineTimeStandards;
	public static volatile ListAttribute<MLine, MSmLinePoint> MSmLinePoints;
	public static volatile ListAttribute<MLine, MSmLineVerify> MSmLineVerifies;
	public static volatile ListAttribute<MLine, TDmDayRepLine> TDmDayRepLines;
	public static volatile ListAttribute<MLine, TDmMonthRepLine> TDmMonthRepLines;
	public static volatile ListAttribute<MLine, TDmWeekRepLine> TDmWeekRepLines;
	public static volatile ListAttribute<MLine, TDmYearRepLine> TDmYearRepLines;
    public static volatile ListAttribute<MLine, TAvailableEnergyLine> TAvailableEnergyLines;
    public static volatile ListAttribute<MLine, MAggregateDmLine> MAggregateDmLines;
}
