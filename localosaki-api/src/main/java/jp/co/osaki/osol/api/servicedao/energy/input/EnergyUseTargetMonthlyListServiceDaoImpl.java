/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.input.EnergyUseTargetMonthlyListDetailResultData;
import jp.co.osaki.osol.entity.MEnergyUseTargetMonthly;
import jp.co.osaki.osol.entity.MEnergyUseTargetMonthlyPK_;
import jp.co.osaki.osol.entity.MEnergyUseTargetMonthly_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー各月目標取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class EnergyUseTargetMonthlyListServiceDaoImpl
        implements BaseServiceDao<EnergyUseTargetMonthlyListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseTargetMonthlyListDetailResultData> getResultList(EnergyUseTargetMonthlyListDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String engTypeCd = t.getEngTypeCd();
        String calYmFrom = t.getCalYmFrom();
        String calYmTo = t.getCalYmTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<EnergyUseTargetMonthlyListDetailResultData> query = builder
                .createQuery(EnergyUseTargetMonthlyListDetailResultData.class);

        Root<MEnergyUseTargetMonthly> root = query.from(MEnergyUseTargetMonthly.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(
                builder.equal(root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.buildingId),
                buildingId));
        //エネルギー種類コード
        if (!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.engTypeCd),
                    engTypeCd));
        }
        //カレンダ年月
        if (!CheckUtility.isNullOrEmpty(calYmFrom) && !CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.ym), calYmFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.ym), calYmTo));
        } else if (!CheckUtility.isNullOrEmpty(calYmFrom) && CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(
                    builder.equal(root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.ym), calYmFrom));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MEnergyUseTargetMonthly_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(EnergyUseTargetMonthlyListDetailResultData.class,
                root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.corpId),
                root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.buildingId),
                root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.engTypeCd),
                root.get(MEnergyUseTargetMonthly_.id).get(MEnergyUseTargetMonthlyPK_.ym),
                root.get(MEnergyUseTargetMonthly_.targetBilledAmount),
                root.get(MEnergyUseTargetMonthly_.targetUseValue1),
                root.get(MEnergyUseTargetMonthly_.targetUseUnit1),
                root.get(MEnergyUseTargetMonthly_.targetUseValue2),
                root.get(MEnergyUseTargetMonthly_.targetUseUnit2),
                root.get(MEnergyUseTargetMonthly_.delFlg),
                root.get(MEnergyUseTargetMonthly_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<EnergyUseTargetMonthlyListDetailResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseTargetMonthlyListDetailResultData> getResultList(
            List<EnergyUseTargetMonthlyListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseTargetMonthlyListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyUseTargetMonthlyListDetailResultData find(EnergyUseTargetMonthlyListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(EnergyUseTargetMonthlyListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyUseTargetMonthlyListDetailResultData merge(EnergyUseTargetMonthlyListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(EnergyUseTargetMonthlyListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
