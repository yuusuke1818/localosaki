package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-05T10:46:55.997+0900")
@StaticMetamodel(MUnitFactor.class)
public class MUnitFactor_ {
	public static volatile SingularAttribute<MUnitFactor, String> unitFactorCd;
	public static volatile SingularAttribute<MUnitFactor, Timestamp> createDate;
	public static volatile SingularAttribute<MUnitFactor, Long> createUserId;
	public static volatile SingularAttribute<MUnitFactor, String> unitFactorName;
	public static volatile SingularAttribute<MUnitFactor, String> unitFactorUnit;
	public static volatile SingularAttribute<MUnitFactor, Timestamp> updateDate;
	public static volatile SingularAttribute<MUnitFactor, Long> updateUserId;
	public static volatile SingularAttribute<MUnitFactor, Integer> version;
	public static volatile ListAttribute<MUnitFactor, MUnitDivide> MUnitDivides;
}
