/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandBuildingSearchDetailResultData;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class KensyoDemandBuildingSearchServiceDaoImpl
        implements BaseServiceDao<KensyoDemandBuildingSearchDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KensyoDemandBuildingSearchDetailResultData> getResultList(KensyoDemandBuildingSearchDetailResultData t,
            EntityManager em) {

        String operationCorpId = t.getCorpId();
        String buildingNo = null;
        if (!CheckUtility.isNullOrEmpty(t.getBuildingNo())) {
            buildingNo = t.getBuildingNo();
            StringBuilder sb = new StringBuilder();
            sb.append("%");
            sb.append(buildingNo);
            sb.append("%");
            buildingNo = sb.toString();
        }
        // 担当者情報を取得してみる
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<KensyoDemandBuildingSearchDetailResultData> query = cb
                .createQuery(KensyoDemandBuildingSearchDetailResultData.class);
        Root<TBuilding> rootTBuilding = query.from(TBuilding.class);
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId), operationCorpId));
        if (!CheckUtility.isNullOrEmpty(buildingNo)) {
            whereList.add(cb.like(rootTBuilding.get(TBuilding_.buildingNo), buildingNo));
        }

        query = query.distinct(true).select(cb.construct(KensyoDemandBuildingSearchDetailResultData.class,
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                rootTBuilding.get(TBuilding_.buildingNo),
                rootTBuilding.get(TBuilding_.buildingName),
                rootTBuilding.get(TBuilding_.divisionCorpId),
                rootTBuilding.get(TBuilding_.divisionBuildingId),
                rootTBuilding.get(TBuilding_.buildingType)))
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        // 検索実行
        List<KensyoDemandBuildingSearchDetailResultData> list = em.createQuery(query).getResultList();
        // TODO 戻り値
        return list;
    }

    @Override
    public List<KensyoDemandBuildingSearchDetailResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KensyoDemandBuildingSearchDetailResultData> getResultList(
            List<KensyoDemandBuildingSearchDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<KensyoDemandBuildingSearchDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KensyoDemandBuildingSearchDetailResultData find(KensyoDemandBuildingSearchDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(KensyoDemandBuildingSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KensyoDemandBuildingSearchDetailResultData merge(KensyoDemandBuildingSearchDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(KensyoDemandBuildingSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
