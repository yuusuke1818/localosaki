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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.entity.MCorpDm;
import jp.co.osaki.osol.entity.MCorpDm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業デマンド取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CorpDemandSelectServiceDaoImpl implements BaseServiceDao<CorpDemandSelectResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CorpDemandSelectResult> getResultList(CorpDemandSelectResult t, EntityManager em) {
        String corpId = t.getCorpId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CorpDemandSelectResult> query = builder.createQuery(CorpDemandSelectResult.class);

        Root<MCorpDm> root = query.from(MCorpDm.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MCorpDm_.corpId), corpId));

        query = query.select(builder.construct(CorpDemandSelectResult.class,
                root.get(MCorpDm_.corpId),
                root.get(MCorpDm_.sumDate),
                root.get(MCorpDm_.weekClosingDayOfWeek),
                root.get(MCorpDm_.weekStartDay),
                root.get(MCorpDm_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<CorpDemandSelectResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CorpDemandSelectResult> getResultList(List<CorpDemandSelectResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CorpDemandSelectResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CorpDemandSelectResult find(CorpDemandSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CorpDemandSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CorpDemandSelectResult merge(CorpDemandSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CorpDemandSelectResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
