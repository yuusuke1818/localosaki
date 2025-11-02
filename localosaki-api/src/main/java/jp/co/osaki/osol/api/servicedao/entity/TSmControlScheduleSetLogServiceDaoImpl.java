package jp.co.osaki.osol.api.servicedao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TSmControlScheduleSetLog;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-maruta
 */
public class TSmControlScheduleSetLogServiceDaoImpl implements BaseServiceDao<TSmControlScheduleSetLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleSetLog> getResultList(TSmControlScheduleSetLog t, EntityManager em) {
        Long smControlScheduleLogId = t.getId().getSmControlScheduleLogId();
        Long smId = t.getId().getSmId();
        BigDecimal controlLoad = t.getId().getControlLoad();
        BigDecimal targetMonth = t.getId().getTargetMonth();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSmControlScheduleSetLog> query = cb.createQuery(TSmControlScheduleSetLog.class);
        Root<TSmControlScheduleSetLog> root = query.from(TSmControlScheduleSetLog.class);
        List<Predicate> whereList = new ArrayList<>();
        if(smControlScheduleLogId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smControlScheduleLogId), smControlScheduleLogId));
        }

        if(smId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.smId), smId));
        }

        if(controlLoad != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.controlLoad), controlLoad));
        }

        if(targetMonth != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleSetLog_.id).get(TSmControlScheduleSetLogPK_.targetMonth), targetMonth));
        }

        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSmControlScheduleSetLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleSetLog> getResultList(List<TSmControlScheduleSetLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleSetLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlScheduleSetLog find(TSmControlScheduleSetLog t, EntityManager em) {
        return em.find(TSmControlScheduleSetLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlScheduleSetLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlScheduleSetLog merge(TSmControlScheduleSetLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlScheduleSetLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
