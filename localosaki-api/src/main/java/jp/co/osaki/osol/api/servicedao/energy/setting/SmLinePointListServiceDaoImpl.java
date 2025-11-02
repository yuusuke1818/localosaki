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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmLinePointListResultData;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK_;
import jp.co.osaki.osol.entity.MSmLinePoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器系統ポイント情報取得 ServiceDaoクラス
 *
 * @author y-maruta
 */
public class SmLinePointListServiceDaoImpl implements BaseServiceDao<SmLinePointListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointListResultData> getResultList(SmLinePointListResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        String pointNo = t.getPointNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmLinePointListResultData> query = builder.createQuery(SmLinePointListResultData.class);

        Root<MSmLinePoint> root = query.from(MSmLinePoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        if (!CheckUtility.isNullOrEmpty(corpId)) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), corpId));
        }

        //系統グループID
        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId), lineGroupId));
        }

        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineNo), lineNo));
        }

        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), buildingId));
        }

        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), smId));
        }

        //ポイント番号
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo), pointNo));
        }

        whereList.add(builder.equal(root.get(MSmLinePoint_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmLinePointListResultData.class,
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineNo),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo),
                root.get(MSmLinePoint_.pointCalcType),
                root.get(MSmLinePoint_.comment),
                root.get(MSmLinePoint_.delFlg),
                root.get(MSmLinePoint_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmLinePointListResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointListResultData> getResultList(List<SmLinePointListResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointListResultData find(SmLinePointListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmLinePointListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointListResultData merge(SmLinePointListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmLinePointListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
