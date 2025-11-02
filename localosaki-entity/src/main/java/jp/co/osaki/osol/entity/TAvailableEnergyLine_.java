package jp.co.osaki.osol.entity;

import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-11-21T16:46:23.250+0900")
@StaticMetamodel(TAvailableEnergyLine.class)
public class TAvailableEnergyLine_ {
	public static volatile SingularAttribute<TAvailableEnergyLine, TAvailableEnergyLinePK> id;
	public static volatile SingularAttribute<TAvailableEnergyLine, Timestamp> createDate;
	public static volatile SingularAttribute<TAvailableEnergyLine, Long> createUserId;
	public static volatile SingularAttribute<TAvailableEnergyLine, Time> endTime;
	public static volatile SingularAttribute<TAvailableEnergyLine, Time> startTime;
	public static volatile SingularAttribute<TAvailableEnergyLine, String> summaryUnit;
	public static volatile SingularAttribute<TAvailableEnergyLine, Timestamp> updateDate;
	public static volatile SingularAttribute<TAvailableEnergyLine, Long> updateUserId;
	public static volatile SingularAttribute<TAvailableEnergyLine, Integer> version;
	public static volatile SingularAttribute<TAvailableEnergyLine, MLine> MLine;
	public static volatile SingularAttribute<TAvailableEnergyLine, TAvailableEnergy> TAvailableEnergy;
}
