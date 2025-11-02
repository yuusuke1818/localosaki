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

import jp.co.osaki.osol.api.resultdata.plan.PlanBuildingTenantSearchPersonSearchResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MCorpPersonAuthPK_;
import jp.co.osaki.osol.entity.MCorpPersonAuth_;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPersonPK_;
import jp.co.osaki.osol.entity.TBuildingPerson_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物担当者を検索する。
 *
 * @author n-takada
 */
public class BuildingPersonSearchServiceDaoImpl implements BaseServiceDao<PlanBuildingTenantSearchPersonSearchResultData> {

    /**
     * ・corpIdとbuildingIDが一致する建物（テナント）に対してauthorityCdListに含まれている<br>
     * authorityCdが一致する担当者情報を返す。<br>
     *
     * @param map
     * @param em
     * @return
     */
    @Override
    public List<PlanBuildingTenantSearchPersonSearchResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {

        Object corpId = Utility.getListFirstItem(map.get("corpId"));
        Object buildingId = Utility.getListFirstItem(map.get("buildingId"));
        List<Object> authorityCdList = map.get("authorityCdList");
        if (corpId == null || buildingId == null) {
            return new ArrayList<>();
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlanBuildingTenantSearchPersonSearchResultData> query
                = builder.createQuery(PlanBuildingTenantSearchPersonSearchResultData.class);

        Root<MPerson> fromMPerson = query.from(MPerson.class);
        Join<MPerson, TBuildingPerson> joinTBuildingPerson = fromMPerson.join(MPerson_.TBuildingPersons);
        Join<TBuildingPerson, TBuilding> joinTBuilding = joinTBuildingPerson.join(TBuildingPerson_.TBuilding);
        Join<MPerson, MCorpPerson> joinMCorpPerson = fromMPerson.join(MPerson_.MCorpPersons);
        Join<MCorpPerson, MCorpPersonAuth> joinMCorpPersonAuth = joinMCorpPerson.join(MCorpPerson_.MCorpPersonAuths);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(fromMPerson.get(MPerson_.id).get(MPersonPK_.corpId), corpId.toString()));
        whereList.add(builder.equal(joinTBuildingPerson.get(TBuildingPerson_.id).get(TBuildingPersonPK_.buildingId), buildingId.toString()));
        if (authorityCdList != null && !authorityCdList.isEmpty()) {
            whereList.add(joinMCorpPersonAuth.get(MCorpPersonAuth_.id).get(MCorpPersonAuthPK_.authorityCd).in(authorityCdList));
        }
        whereList.add(builder.equal(joinMCorpPersonAuth.get(MCorpPersonAuth_.authorityFlg), 1));

        whereList.add(builder.equal(joinMCorpPersonAuth.get(MCorpPersonAuth_.delFlg), 0));
        whereList.add(builder.equal(joinTBuildingPerson.get(TBuildingPerson_.delFlg), 0));
        whereList.add(builder.equal(joinTBuilding.get(TBuilding_.delFlg), 0));
        whereList.add(builder.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));

        query = query.select(builder.construct(PlanBuildingTenantSearchPersonSearchResultData.class,
                fromMPerson.get(MPerson_.id).get(MPersonPK_.personId),
                fromMPerson.get(MPerson_.personName),
                fromMPerson.get(MPerson_.telNo),
                fromMPerson.get(MPerson_.mailAddress)))
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .groupBy(
                        fromMPerson.get(MPerson_.id).get(MPersonPK_.personId),
                        fromMPerson.get(MPerson_.personName),
                        fromMPerson.get(MPerson_.telNo),
                        fromMPerson.get(MPerson_.mailAddress))
                .orderBy(builder.asc(fromMPerson.get(MPerson_.id).get(MPersonPK_.personId)));

        return em.createQuery(query).getResultList();

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchPersonSearchResultData> getResultList(PlanBuildingTenantSearchPersonSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchPersonSearchResultData> getResultList(List<PlanBuildingTenantSearchPersonSearchResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchPersonSearchResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanBuildingTenantSearchPersonSearchResultData find(PlanBuildingTenantSearchPersonSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(PlanBuildingTenantSearchPersonSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanBuildingTenantSearchPersonSearchResultData merge(PlanBuildingTenantSearchPersonSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(PlanBuildingTenantSearchPersonSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
