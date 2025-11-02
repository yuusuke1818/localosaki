package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-10-11T11:30:58.102+0900")
@StaticMetamodel(MProductSpec.class)
public class MProductSpec_ {
	public static volatile SingularAttribute<MProductSpec, String> productCd;
	public static volatile SingularAttribute<MProductSpec, Timestamp> createDate;
	public static volatile SingularAttribute<MProductSpec, Long> createUserId;
	public static volatile SingularAttribute<MProductSpec, Integer> delFlg;
	public static volatile SingularAttribute<MProductSpec, Integer> loadControlOutput;
	public static volatile SingularAttribute<MProductSpec, Integer> measurementPoint;
	public static volatile SingularAttribute<MProductSpec, String> productName;
	public static volatile SingularAttribute<MProductSpec, String> productType;
	public static volatile SingularAttribute<MProductSpec, Timestamp> updateDate;
	public static volatile SingularAttribute<MProductSpec, Long> updateUserId;
	public static volatile SingularAttribute<MProductSpec, Integer> version;
	public static volatile ListAttribute<MProductSpec, MProductControlLoad> MProductControlLoads;
	public static volatile ListAttribute<MProductSpec, MSmPrm> MSmPrms;
	public static volatile ListAttribute<MProductSpec, TSmConnectControlProduct> TSmConnectControlProducts;
    public static volatile ListAttribute<MProductSpec, MDemandCollectProduct> MDemandCollectProducts;
}
