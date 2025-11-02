package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2025-09-17T17:24:00.916+0900")
@StaticMetamodel(MDevFwVersion.class)
public class MDevFwVersion_ {
	public static volatile SingularAttribute<MDevFwVersion, String> devId;
	public static volatile SingularAttribute<MDevFwVersion, Timestamp> createDate;
	public static volatile SingularAttribute<MDevFwVersion, Long> createUserId;
	public static volatile SingularAttribute<MDevFwVersion, Integer> currentFwVer;
	public static volatile SingularAttribute<MDevFwVersion, Timestamp> latestUpdateDate;
	public static volatile SingularAttribute<MDevFwVersion, Timestamp> prevUpdateDate;
	public static volatile SingularAttribute<MDevFwVersion, Integer> targetFwVer;
	public static volatile SingularAttribute<MDevFwVersion, Timestamp> updateDate;
	public static volatile SingularAttribute<MDevFwVersion, Boolean> updateFlg;
	public static volatile SingularAttribute<MDevFwVersion, String> updateGroup;
	public static volatile SingularAttribute<MDevFwVersion, Long> updateUserId;
	public static volatile SingularAttribute<MDevFwVersion, Integer> version;
}
