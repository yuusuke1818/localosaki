/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.generic.GenericTypeListDetailResultData;
import jp.co.osaki.osol.entity.MGenericType;
import jp.co.osaki.osol.entity.MGenericTypePK_;
import jp.co.osaki.osol.entity.MGenericType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class GenericTypeListServiceDaoImpl implements BaseServiceDao<GenericTypeListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GenericTypeListDetailResultData> getResultList(GenericTypeListDetailResultData t, EntityManager em) {
        String groupCode = t.getGroupCode();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GenericTypeListDetailResultData> query = cb.createQuery(GenericTypeListDetailResultData.class);
        Root<MGenericType> mGenericType = query.from(MGenericType.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(mGenericType.get(MGenericType_.id).get(MGenericTypePK_.groupCode), groupCode));

        query = query.select(cb.construct(GenericTypeListDetailResultData.class,
                mGenericType.get(MGenericType_.id).get(MGenericTypePK_.groupCode),
                mGenericType.get(MGenericType_.id).get(MGenericTypePK_.kbnCode),
                mGenericType.get(MGenericType_.groupName),
                mGenericType.get(MGenericType_.kbnName),
                mGenericType.get(MGenericType_.displayOrder),
                mGenericType.get(MGenericType_.version),
                mGenericType.get(MGenericType_.createUserId),
                mGenericType.get(MGenericType_.createDate),
                mGenericType.get(MGenericType_.updateUserId),
                mGenericType.get(MGenericType_.updateDate)))
                .where(cb.and(whereList.toArray(new Predicate[] {})))
                .orderBy(cb.asc(mGenericType.get(MGenericType_.displayOrder)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<GenericTypeListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GenericTypeListDetailResultData> getResultList(List<GenericTypeListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GenericTypeListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GenericTypeListDetailResultData find(GenericTypeListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(GenericTypeListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GenericTypeListDetailResultData merge(GenericTypeListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(GenericTypeListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
