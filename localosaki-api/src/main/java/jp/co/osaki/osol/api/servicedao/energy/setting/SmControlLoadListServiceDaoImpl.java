/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK_;
import jp.co.osaki.osol.entity.MSmControlLoad_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御負荷情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class SmControlLoadListServiceDaoImpl implements BaseServiceDao<SmControlLoadListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadListDetailResultData> getResultList(SmControlLoadListDetailResultData t,
            EntityManager em) {
        Long smId = t.getSmId();
        BigDecimal controlLoadFrom = t.getControlLoadFrom();
        BigDecimal controlLoadTo = t.getControlLoadTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlLoadListDetailResultData> query = builder
                .createQuery(SmControlLoadListDetailResultData.class);

        Root<MSmControlLoad> root = query.from(MSmControlLoad.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.smId), smId));
        //制御負荷
        if (controlLoadFrom != null && controlLoadTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad),
                    controlLoadFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad),
                    controlLoadTo));
        } else if (controlLoadFrom != null && controlLoadTo == null) {
            whereList.add(
                    builder.equal(root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad), controlLoadFrom));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MSmControlLoad_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmControlLoadListDetailResultData.class,
                root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.smId),
                root.get(MSmControlLoad_.id).get(MSmControlLoadPK_.controlLoad),
                root.get(MSmControlLoad_.controlLoadName),
                root.get(MSmControlLoad_.controlLoadMemo),
                root.get(MSmControlLoad_.controlLoadShutOffTime),
                root.get(MSmControlLoad_.controlLoadShutOffRank),
                root.get(MSmControlLoad_.controlLoadShutOffCapacity),
                root.get(MSmControlLoad_.delFlg),
                root.get(MSmControlLoad_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlLoadListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadListDetailResultData> getResultList(List<SmControlLoadListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlLoadListDetailResultData find(SmControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlLoadListDetailResultData merge(SmControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlLoadListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
