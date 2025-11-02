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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.energy.verify.DemandOrgKensyoNameListResult;
import jp.co.osaki.osol.api.result.servicedao.MProductControlLoadResult;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MProductControlLoad;
import jp.co.osaki.osol.entity.MProductControlLoadPK_;
import jp.co.osaki.osol.entity.MProductControlLoad_;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class DemandOrgKensyoCircuitNameListServiceDaoImpl implements BaseServiceDao<DemandOrgKensyoNameListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(List<DemandOrgKensyoNameListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoNameListResult find(DemandOrgKensyoNameListResult t, EntityManager em) {
        DemandOrgKensyoNameListResult resultSet = new DemandOrgKensyoNameListResult();
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MProductControlLoadResult> controlLoadQuery = builder.createQuery(MProductControlLoadResult.class);

        Root<MBuildingSm> rootControlLoad = controlLoadQuery.from(MBuildingSm.class);

        Join<MBuildingSm, MSmPrm> joinMSmPrmName = rootControlLoad.join(MBuildingSm_.MSmPrm, JoinType.INNER);

        Join<MSmPrm, MProductSpec> joinMProductSpecName = joinMSmPrmName.join(MSmPrm_.MProductSpec, JoinType.INNER);

        Join<MProductSpec, MProductControlLoad> joinMProductControlLoadName
                = joinMProductSpecName.join(MProductSpec_.MProductControlLoads, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();
        //企業ID
        whereList.add(builder.equal(rootControlLoad.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), corpId));
        whereList.add(builder.equal(rootControlLoad.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId), buildingId));

        controlLoadQuery = controlLoadQuery.select(builder.construct(MProductControlLoadResult.class,
                joinMProductControlLoadName.get(MProductControlLoad_.id).get(MProductControlLoadPK_.productCd),
                joinMProductControlLoadName.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad),
                joinMProductControlLoadName.get(MProductControlLoad_.controlLoadCircuit),
                joinMProductControlLoadName.get(MProductControlLoad_.delFlg),
                joinMProductControlLoadName.get(MProductControlLoad_.version),
                joinMProductControlLoadName.get(MProductControlLoad_.createUserId),
                joinMProductControlLoadName.get(MProductControlLoad_.createDate),
                joinMProductControlLoadName.get(MProductControlLoad_.updateUserId),
                joinMProductControlLoadName.get(MProductControlLoad_.updateDate)))
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(joinMProductControlLoadName.get(MProductControlLoad_.id).get(MProductControlLoadPK_.controlLoad)));
        em.createQuery(controlLoadQuery).getResultList();

        return resultSet;
    }

    @Override
    public void persist(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoNameListResult merge(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
