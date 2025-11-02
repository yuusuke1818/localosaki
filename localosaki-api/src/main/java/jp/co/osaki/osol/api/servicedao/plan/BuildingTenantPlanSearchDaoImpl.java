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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.plan.BuildingTenantPlanResult;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class BuildingTenantPlanSearchDaoImpl implements BaseServiceDao<BuildingTenantPlanResult> {

    @Override
    public List<BuildingTenantPlanResult> getResultList(BuildingTenantPlanResult t, EntityManager em) {
        String corpId = t.getCorpId();
        long planFulfillmentId = t.getPlanFulfillmentId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildingTenantPlanResult> query = builder.createQuery(BuildingTenantPlanResult.class);
        Root<TBuildingPlanFulfillment> fromTBuildingPlanFulfillment = query.from(TBuildingPlanFulfillment.class);
        Join<TBuildingPlanFulfillment, TBuilding> joinTBuilding = fromTBuildingPlanFulfillment
                .join(TBuildingPlanFulfillment_.TBuilding);
        Join<TBuilding, MPrefecture> joinMPrefecture = joinTBuilding.join(TBuilding_.MPrefecture);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(
                TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId), corpId));
        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(
                TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId), planFulfillmentId));
        whereList.add(builder.equal(fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg), 0));

        query = query.select(builder.construct(BuildingTenantPlanResult.class,
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id)
                        .get(TBuildingPlanFulfillmentPK_.buildingId),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id)
                        .get(TBuildingPlanFulfillmentPK_.planFulfillmentId),
                joinTBuilding.get(TBuilding_.buildingNo),
                joinTBuilding.get(TBuilding_.buildingName),
                joinMPrefecture.get(MPrefecture_.prefectureCd),
                joinTBuilding.get(TBuilding_.address),
                joinTBuilding.get(TBuilding_.addressBuilding),
                joinTBuilding.get(TBuilding_.buildingDelDate),
                joinTBuilding.get(TBuilding_.totalStartYm),
                joinTBuilding.get(TBuilding_.totalEndYm),
                joinTBuilding.get(TBuilding_.nyukyoTypeCd),
                fromTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.version),
                joinTBuilding.get(TBuilding_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingTenantPlanResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingTenantPlanResult> getResultList(List<BuildingTenantPlanResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingTenantPlanResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingTenantPlanResult find(BuildingTenantPlanResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingTenantPlanResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingTenantPlanResult merge(BuildingTenantPlanResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(BuildingTenantPlanResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
