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

import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgKensyoSmInfoDetailResultData;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.entity.MProductSpec_;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK_;
import jp.co.osaki.osol.entity.MSmLinePoint_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class DemandOrgKensyoSmInfoServiceDaoImpl implements BaseServiceDao<DemandOrgKensyoSmInfoDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoSmInfoDetailResultData> getResultList(DemandOrgKensyoSmInfoDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandOrgKensyoSmInfoDetailResultData> query = builder
                .createQuery(DemandOrgKensyoSmInfoDetailResultData.class);

        Root<MBuildingSm> root = query.from(MBuildingSm.class);

        Join<MBuildingSm, MSmPrm> joinMSmPrm = root.join(MBuildingSm_.MSmPrm, JoinType.INNER);
        Join<MSmPrm, MProductSpec> joinMProductSpec = joinMSmPrm.join(MSmPrm_.MProductSpec, JoinType.INNER);
        Join<MBuildingSm, MBuildingSmPoint> joinMBuildingSmPoint = root.join(MBuildingSm_.MBuildingSmPoints,
                JoinType.INNER);
        Join<MBuildingSmPoint, MSmLinePoint> joinMSmLinePoint = joinMBuildingSmPoint
                .join(MBuildingSmPoint_.MSmLinePoints, JoinType.INNER);
        Join<MSmLinePoint, MLine> joinMLine = joinMSmLinePoint.join(MSmLinePoint_.MLine, JoinType.INNER);
        Join<MLine, MLineType> joinMLineType = joinMLine.join(MLine_.MLineType, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), corpId));
        whereList.add(builder.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId), buildingId));

        query = query.select(builder.construct(DemandOrgKensyoSmInfoDetailResultData.class,
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId),
                root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId),
                joinMProductSpec.get(MProductSpec_.productCd),
                joinMSmPrm.get(MSmPrm_.smAddress),
                joinMSmPrm.get(MSmPrm_.ipAddress),
                joinMProductSpec.get(MProductSpec_.productType),
                joinMProductSpec.get(MProductSpec_.productName),
                joinMSmLinePoint.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId),
                joinMLineType.get(MLineType_.lineTypeName),
                joinMLineType.get(MLineType_.dmValidFlg),
                joinMLineType.get(MLineType_.eventValidFlg),
                joinMLineType.get(MLineType_.airValidFlg)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DemandOrgKensyoSmInfoDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoSmInfoDetailResultData> getResultList(List<DemandOrgKensyoSmInfoDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoSmInfoDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoSmInfoDetailResultData find(DemandOrgKensyoSmInfoDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandOrgKensyoSmInfoDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoSmInfoDetailResultData merge(DemandOrgKensyoSmInfoDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandOrgKensyoSmInfoDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
