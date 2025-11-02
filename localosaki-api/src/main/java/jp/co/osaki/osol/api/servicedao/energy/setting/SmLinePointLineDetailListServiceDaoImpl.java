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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmLinePointLineDetailListResultData;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
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
public class SmLinePointLineDetailListServiceDaoImpl implements BaseServiceDao<SmLinePointLineDetailListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointLineDetailListResultData> getResultList(SmLinePointLineDetailListResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        String pointNo = t.getPointNo();
        String pointCalcType = t.getPointCalcType();
        Integer lineEnableFlg = t.getLineEnableFlg();
        Integer inputEnableFlg = t.getInputEnableFlg();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmLinePointLineDetailListResultData> query = builder.createQuery(SmLinePointLineDetailListResultData.class);

        Root<MSmLinePoint> root = query.from(MSmLinePoint.class);
        Join<MSmLinePoint,MLine>joinMLine = root.join(MSmLinePoint_.MLine);

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

      //ポイント番号
        if (!CheckUtility.isNullOrEmpty(pointCalcType)) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.pointCalcType), pointCalcType));
        }

        //系統有効フラグ
        if (lineEnableFlg != null) {
            whereList.add(builder.equal(joinMLine.get(MLine_.lineEnableFlg), lineEnableFlg));
        }

      //系統有効フラグ
        if (inputEnableFlg != null) {
            whereList.add(builder.equal(joinMLine.get(MLine_.inputEnableFlg), inputEnableFlg));
        }

        whereList.add(builder.equal(root.get(MSmLinePoint_.delFlg), OsolConstants.FLG_OFF));

        whereList.add(builder.equal(joinMLine.get(MLine_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmLinePointLineDetailListResultData.class,
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineNo),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo),
                root.get(MSmLinePoint_.pointCalcType),
                root.get(MSmLinePoint_.comment),
                joinMLine.get(MLine_.lineName),
                joinMLine.get(MLine_.lineUnit),
                joinMLine.get(MLine_.lineTarget),
                joinMLine.get(MLine_.MLineType).get(MLineType_.lineType),
                joinMLine.get(MLine_.lineEnableFlg),
                joinMLine.get(MLine_.inputEnableFlg))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmLinePointLineDetailListResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointLineDetailListResultData> getResultList(List<SmLinePointLineDetailListResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointLineDetailListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointLineDetailListResultData find(SmLinePointLineDetailListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmLinePointLineDetailListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointLineDetailListResultData merge(SmLinePointLineDetailListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmLinePointLineDetailListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
