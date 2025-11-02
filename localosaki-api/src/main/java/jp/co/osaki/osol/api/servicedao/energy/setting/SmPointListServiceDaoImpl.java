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

import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPointPK_;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器ポイント情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class SmPointListServiceDaoImpl implements BaseServiceDao<SmPointListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmPointListDetailResultData> getResultList(SmPointListDetailResultData t, EntityManager em) {
        Long smId = t.getSmId();
        String pointNo = t.getPointNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmPointListDetailResultData> query = builder.createQuery(SmPointListDetailResultData.class);

        Root<MSmPoint> root = query.from(MSmPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(MSmPoint_.id).get(MSmPointPK_.smId), smId));
        //ポイント番号
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(root.get(MSmPoint_.id).get(MSmPointPK_.pointNo), pointNo));
        }

        query = query.select(builder.construct(SmPointListDetailResultData.class,
                root.get(MSmPoint_.id).get(MSmPointPK_.smId),
                root.get(MSmPoint_.id).get(MSmPointPK_.pointNo),
                root.get(MSmPoint_.pointType),
                root.get(MSmPoint_.dmCorrectionFactor),
                root.get(MSmPoint_.analogOffSetValue),
                root.get(MSmPoint_.analogConversionFactor),
                root.get(MSmPoint_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmPointListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmPointListDetailResultData> getResultList(List<SmPointListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmPointListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmPointListDetailResultData find(SmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmPointListDetailResultData merge(SmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmPointListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
