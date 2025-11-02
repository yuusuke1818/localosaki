package jp.co.osaki.osol.entity;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-12T12:59:39.411+0900")
@StaticMetamodel(MLineTimeStandardPK.class)
public class MLineTimeStandardPK_ {
	public static volatile SingularAttribute<MLineTimeStandardPK, String> corpId;
	public static volatile SingularAttribute<MLineTimeStandardPK, Long> buildingId;
	public static volatile SingularAttribute<MLineTimeStandardPK, Long> lineGroupId;
	public static volatile SingularAttribute<MLineTimeStandardPK, String> lineNo;
	public static volatile SingularAttribute<MLineTimeStandardPK, BigDecimal> jigenNo;
}
