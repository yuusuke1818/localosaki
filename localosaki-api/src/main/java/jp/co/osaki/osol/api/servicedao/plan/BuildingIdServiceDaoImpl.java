/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class BuildingIdServiceDaoImpl implements BaseServiceDao<Long> {

    @Override
    public List<Long> getResultList(Map<String, List<Object>> map, EntityManager em) {
        Object corpId = Utility.getListFirstItem(map.get("corpId"));
        Object tenantFlg = map.get("tenantFlg");
        Object buildingFlg = map.get("buildingFlg");
        Object osakiFlg = Utility.getListFirstItem(map.get("osakiFlg"));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<TBuilding> tBuildingRoot = query.from(TBuilding.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.delFlg), 0));

        if (osakiFlg == null || !"1".equals(osakiFlg)) {
            whereList.add(cb.isNull(tBuildingRoot.get(TBuilding_.buildingDelDate)));
        }
        if (corpId != null) {
            whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId.toString()));
        }
        if (tenantFlg == null) {
            whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.buildingType),"0"));
        } else if (buildingFlg == null) {
            whereList.add(cb.equal(tBuildingRoot.get(TBuilding_.buildingType),"1"));
        }
        query = query.select(cb.construct(Long.class,
                tBuildingRoot.get(TBuilding_.id).get(TBuildingPK_.buildingId)))
                .where(cb.and(whereList.toArray(new Predicate[]{})))
                .orderBy(cb.asc(tBuildingRoot.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

        return em.createQuery(query).getResultList();

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(Long t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(List<Long> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long find(Long t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(Long t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long merge(Long t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(Long t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
