/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.entity.MProductControlLoad;
import jp.co.osaki.osol.entity.MProductControlLoadPK_;
import jp.co.osaki.osol.entity.MProductControlLoad_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 製品制御負荷情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class ProductControlLoadListServiceDaoImpl implements BaseServiceDao<ProductControlLoadListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductControlLoadListDetailResultData> getResultList(ProductControlLoadListDetailResultData t,
            EntityManager em) {
        String productCd = t.getProductCd();
        BigDecimal controlLoadFrom = t.getControlLoadFrom();
        BigDecimal controlLoadTo = t.getControlLoadTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ProductControlLoadListDetailResultData> query = builder
                .createQuery(ProductControlLoadListDetailResultData.class);

        Root<MProductControlLoad> root = query.from(MProductControlLoad.class);

        List<Predicate> whereList = new ArrayList<>();

        //製品コード
        whereList
                .add(builder.equal(root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.productCd), productCd));

        //制御負荷
        if (controlLoadFrom != null && controlLoadTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad), controlLoadFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad), controlLoadTo));
        } else if (controlLoadFrom != null && controlLoadTo == null) {
            whereList.add(builder.equal(root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad),
                    controlLoadFrom));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MProductControlLoad_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(ProductControlLoadListDetailResultData.class,
                root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.productCd),
                root.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad),
                root.get(MProductControlLoad_.controlLoadCircuit),
                root.get(MProductControlLoad_.manualLoadControlFlg),
                root.get(MProductControlLoad_.demandControlFlg),
                root.get(MProductControlLoad_.eventControlFlg),
                root.get(MProductControlLoad_.scheduleControlFlg),
                root.get(MProductControlLoad_.delFlg),
                root.get(MProductControlLoad_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ProductControlLoadListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductControlLoadListDetailResultData> getResultList(List<ProductControlLoadListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProductControlLoadListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductControlLoadListDetailResultData find(ProductControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ProductControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductControlLoadListDetailResultData merge(ProductControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ProductControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
