/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器情報取得処理 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class SmSelectResultServiceDaoImpl implements BaseServiceDao<SmSelectResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmSelectResult> getResultList(SmSelectResult t, EntityManager em) {
        Long smId = t.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmSelectResult> query = builder.createQuery(SmSelectResult.class);

        Root<MSmPrm> root = query.from(MSmPrm.class);
        Join<MSmPrm, MProductSpec> joinProduct = root.join(MSmPrm_.MProductSpec);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(MSmPrm_.smId), smId));
        //削除フラグ
        whereList.add(builder.equal(root.get(MSmPrm_.delFlg), OsolConstants.FLG_OFF));
        whereList.add(builder.equal(joinProduct.get(MProductSpec_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmSelectResult.class,
                root.get(MSmPrm_.smId),
                joinProduct.get(MProductSpec_.productCd),
                root.get(MSmPrm_.smAddress),
                root.get(MSmPrm_.ipAddress),
                root.get(MSmPrm_.startDate),
                root.get(MSmPrm_.endDate),
                root.get(MSmPrm_.plotAnalogPointNo1),
                root.get(MSmPrm_.plotAnalogPointNo2),
                root.get(MSmPrm_.note),
                root.get(MSmPrm_.aielMasterConnectFlg),
                root.get(MSmPrm_.delFlg),
                root.get(MSmPrm_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmSelectResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmSelectResult> getResultList(List<SmSelectResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmSelectResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmSelectResult find(SmSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmSelectResult merge(SmSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
