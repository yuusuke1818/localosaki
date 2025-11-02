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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物機器データ取得処理 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class DemandBuildingSmListServiceDaoImpl implements BaseServiceDao<DemandBuildingSmListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmListDetailResultData> getResultList(DemandBuildingSmListDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandBuildingSmListDetailResultData> query = builder
                .createQuery(DemandBuildingSmListDetailResultData.class);

        Root<MBuildingSm> root = query.from(MBuildingSm.class);
        Join<MBuildingSm, MSmPrm> joinSmPrm = root.join(MBuildingSm_.MSmPrm, JoinType.LEFT);
        Join<MSmPrm, MProductSpec> joinProductSpec = joinSmPrm.join(MSmPrm_.MProductSpec, JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId), buildingId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId), smId));
        }

        query = query.select(builder.construct(DemandBuildingSmListDetailResultData.class,
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId),
                joinSmPrm.get(MSmPrm_.smAddress),
                joinProductSpec.get(MProductSpec_.productCd),
                joinProductSpec.get(MProductSpec_.productName),
                joinProductSpec.get(MProductSpec_.loadControlOutput),
                root.get(MBuildingSm_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<DemandBuildingSmListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmListDetailResultData> getResultList(List<DemandBuildingSmListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandBuildingSmListDetailResultData find(DemandBuildingSmListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandBuildingSmListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandBuildingSmListDetailResultData merge(DemandBuildingSmListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandBuildingSmListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
