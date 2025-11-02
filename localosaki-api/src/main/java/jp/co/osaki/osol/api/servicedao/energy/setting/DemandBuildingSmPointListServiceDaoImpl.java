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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK_;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物機器ポイント情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class DemandBuildingSmPointListServiceDaoImpl
        implements BaseServiceDao<DemandBuildingSmPointListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmPointListDetailResultData> getResultList(DemandBuildingSmPointListDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        String pointNo = t.getPointNo();
        String pointType = t.getPointType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandBuildingSmPointListDetailResultData> query = builder
                .createQuery(DemandBuildingSmPointListDetailResultData.class);

        Root<MBuildingSmPoint> root = query.from(MBuildingSmPoint.class);
        Join<MBuildingSmPoint, MSmPoint> joinMSmPoint = root.join(MBuildingSmPoint_.MSmPoint, JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.smId), smId));
        }
        //ポイント番号
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.pointNo), pointNo));
        }
        //ポイント種別
        if (!CheckUtility.isNullOrEmpty(pointType)) {
            whereList.add(builder.equal(joinMSmPoint.get(MSmPoint_.pointType), pointType));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MBuildingSmPoint_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(DemandBuildingSmPointListDetailResultData.class,
                root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.corpId),
                root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.buildingId),
                root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.smId),
                root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.pointNo),
                root.get(MBuildingSmPoint_.pointName),
                root.get(MBuildingSmPoint_.pointUnit),
                root.get(MBuildingSmPoint_.pointSumFlg),
                joinMSmPoint.get(MSmPoint_.pointType),
                root.get(MBuildingSmPoint_.delFlg),
                root.get(MBuildingSmPoint_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DemandBuildingSmPointListDetailResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmPointListDetailResultData> getResultList(
            List<DemandBuildingSmPointListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandBuildingSmPointListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandBuildingSmPointListDetailResultData find(DemandBuildingSmPointListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandBuildingSmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandBuildingSmPointListDetailResultData merge(DemandBuildingSmPointListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandBuildingSmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
