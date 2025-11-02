/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター種別設定 serviceクラス
 *
 * @author yonezawa.a
 */
public class MMeterTypeServiceDaoImpl implements BaseServiceDao<MMeterType> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterType> getResultList(MMeterType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterType> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MMeterType> criteria = cb.createQuery(MMeterType.class);
        Root<MMeterType> mMeterType = criteria.from(MMeterType.class);
        List<Predicate> conditionList = new ArrayList<Predicate>();

        conditionList
                .add(cb.equal(mMeterType.get(MMeterType_.id).get(MMeterTypePK_.corpId), parameterMap.get("corpId").get(0)));
        conditionList.add(
                cb.equal(mMeterType.get(MMeterType_.id).get(MMeterTypePK_.buildingId), parameterMap.get("buildingId").get(0)));
        conditionList.add(cb.between(mMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType), 1L, 20L));

        criteria = criteria.select(mMeterType).where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(cb.asc(mMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

        List<MMeterType> resultMMeterType = em.createQuery(criteria).getResultList();
        return resultMMeterType;
    }

    @Override
    public List<MMeterType> getResultList(List<MMeterType> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeterType find(MMeterType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MMeterType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeterType merge(MMeterType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MMeterType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterType> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
