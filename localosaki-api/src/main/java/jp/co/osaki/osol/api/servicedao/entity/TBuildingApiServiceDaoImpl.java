/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物テーブル EntityServiceDaoクラス（webと重複するため一時的に"API"をクラス名に付与）
 * @author n-takada
 */
public class TBuildingApiServiceDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(TBuilding t, EntityManager em) {
        String corpId = t.getId().getCorpId();
        String buildingNo = t.getBuildingNo();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> query = cb.createQuery(TBuilding.class);
        Root<TBuilding> tBuildingRoot = query.from(TBuilding.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.delFlg), 0));
        whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.buildingNo), buildingNo));
        query = query.select(tBuildingRoot)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuilding find(TBuilding t, EntityManager em) {
        TBuildingPK id = t.getId();
        if (id == null) {
            return null;
        }
        String corpId = id.getCorpId();
        Long buildingId = id.getBuildingId();
        if (corpId != null && buildingId != null) {
            return em.find(TBuilding.class, id);
        }

        String buildingNo = t.getBuildingNo();
        if (buildingNo != null) {
            List<TBuilding> list = getResultList(t, em);
            if (list != null && list.size() == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public void persist(TBuilding t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TBuilding merge(TBuilding t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TBuilding t, EntityManager em) {
        em.remove(t);
    }

}
