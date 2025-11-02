package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TSmControlScheduleLog;
import jp.co.osaki.osol.entity.TSmControlScheduleLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-maruta
 */
public class TSmControlScheduleLogServiceDaoImpl implements BaseServiceDao<TSmControlScheduleLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleLog> getResultList(TSmControlScheduleLog t, EntityManager em) {
        Long smControlScheduleLogId = t.getId().getSmControlScheduleLogId();
        Long smId = t.getId().getSmId();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSmControlScheduleLog> query = cb.createQuery(TSmControlScheduleLog.class);
        Root<TSmControlScheduleLog> root = query.from(TSmControlScheduleLog.class);
        List<Predicate> whereList = new ArrayList<>();
        if(smControlScheduleLogId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smControlScheduleLogId), smControlScheduleLogId));
        }

        if(smId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smId), smId));
        }

        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSmControlScheduleLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleLog> getResultList(List<TSmControlScheduleLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlScheduleLog find(TSmControlScheduleLog t, EntityManager em) {
        return em.find(TSmControlScheduleLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlScheduleLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlScheduleLog merge(TSmControlScheduleLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlScheduleLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
