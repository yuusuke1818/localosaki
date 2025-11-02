package jp.co.osaki.osol.entity;

import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-11-15T14:21:06.034+0900")
@StaticMetamodel(MDemandCollectProduct.class)
public class MDemandCollectProduct_ {
	public static volatile SingularAttribute<MDemandCollectProduct, MDemandCollectProductPK> id;
	public static volatile SingularAttribute<MDemandCollectProduct, Timestamp> createDate;
	public static volatile SingularAttribute<MDemandCollectProduct, Long> createUserId;
	public static volatile SingularAttribute<MDemandCollectProduct, Timestamp> updateDate;
	public static volatile SingularAttribute<MDemandCollectProduct, Long> updateUserId;
	public static volatile SingularAttribute<MDemandCollectProduct, Integer> version;
	public static volatile SingularAttribute<MDemandCollectProduct, MDemandCollect> MDemandCollect;
	public static volatile SingularAttribute<MDemandCollectProduct, MProductSpec> MProductSpec;
}
