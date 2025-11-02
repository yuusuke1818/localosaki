package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductSpecListDetailResultData;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 製品仕様情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class ProductSpecListServiceDaoImpl implements BaseServiceDao<ProductSpecListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductSpecListDetailResultData> getResultList(ProductSpecListDetailResultData target,
            EntityManager em) {
        String productCd = target.getProductCd();
        String productName = target.getProductName();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ProductSpecListDetailResultData> query = builder
                .createQuery(ProductSpecListDetailResultData.class);

        Root<MProductSpec> root = query.from(MProductSpec.class);

        List<Predicate> whereList = new ArrayList<>();

        //製品コード
        if (!CheckUtility.isNullOrEmpty(productCd)) {
            whereList.add(builder.equal(root.get(MProductSpec_.productCd), productCd));
        }
        //製品名（部分一致）
        if (!CheckUtility.isNullOrEmpty(productName)) {
            whereList.add(builder.like(root.get(MProductSpec_.productName), "%".concat(productName).concat("%")));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MProductSpec_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(ProductSpecListDetailResultData.class,
                root.get(MProductSpec_.productCd),
                root.get(MProductSpec_.productType),
                root.get(MProductSpec_.productName),
                root.get(MProductSpec_.measurementPoint),
                root.get(MProductSpec_.loadControlOutput),
                root.get(MProductSpec_.delFlg),
                root.get(MProductSpec_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ProductSpecListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductSpecListDetailResultData> getResultList(List<ProductSpecListDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductSpecListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductSpecListDetailResultData find(ProductSpecListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ProductSpecListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductSpecListDetailResultData merge(ProductSpecListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ProductSpecListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
