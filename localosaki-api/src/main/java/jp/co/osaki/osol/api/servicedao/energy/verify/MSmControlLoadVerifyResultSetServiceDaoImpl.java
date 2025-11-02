/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.verify;

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
import jp.co.osaki.osol.api.resultdata.energy.verify.MSmControlLoadVerifyResultData;
import jp.co.osaki.osol.entity.MSmControlLoadVerify;
import jp.co.osaki.osol.entity.MSmControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmControlLoadVerify_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class MSmControlLoadVerifyResultSetServiceDaoImpl implements BaseServiceDao<MSmControlLoadVerifyResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerifyResultData> getResultList(MSmControlLoadVerifyResultData target, EntityManager em) {

        Long smId = target.getSmId();
        BigDecimal controlLoad = target.getControlLoad();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmControlLoadVerifyResultData> query = builder.createQuery(MSmControlLoadVerifyResultData.class);

        Root<MSmControlLoadVerify> root = query.from(MSmControlLoadVerify.class);

        List<Predicate> whereList = new ArrayList<>();

        if (smId != null) {
            whereList.add(builder.equal(root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.smId), smId));
        }
        if (controlLoad != null) {
            whereList.add(builder.equal(root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.controlLoad), controlLoad));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MSmControlLoadVerify_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(MSmControlLoadVerifyResultData.class,
                root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.smId),
                root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.controlLoad),
                root.get(MSmControlLoadVerify_.controlLoadRunningHours),
                root.get(MSmControlLoadVerify_.delFlg),
                root.get(MSmControlLoadVerify_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmControlLoadVerifyResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerifyResultData> getResultList(List<MSmControlLoadVerifyResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmControlLoadVerifyResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmControlLoadVerifyResultData find(MSmControlLoadVerifyResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MSmControlLoadVerifyResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmControlLoadVerifyResultData merge(MSmControlLoadVerifyResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MSmControlLoadVerifyResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
