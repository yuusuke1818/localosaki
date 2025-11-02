package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-maruta
 */
public class TSmControlScheduleTimeLogServiceDaoImpl implements BaseServiceDao<TSmControlScheduleTimeLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleTimeLog> getResultList(TSmControlScheduleTimeLog t, EntityManager em) {
        Long smControlScheduleLogId = t.getId().getSmControlScheduleLogId();
        Long smId = t.getId().getSmId();
        String patternNo = t.getId().getPatternNo();
        Integer timeSlotNo = t.getId().getTimeSlotNo();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSmControlScheduleTimeLog> query = cb.createQuery(TSmControlScheduleTimeLog.class);
        Root<TSmControlScheduleTimeLog> root = query.from(TSmControlScheduleTimeLog.class);

        List<Predicate> whereList = new ArrayList<>();
        if(smControlScheduleLogId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.smControlScheduleLogId), smControlScheduleLogId));
        }

        if(smId != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.smId), smId));
        }

        if(patternNo != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.patternNo), patternNo));
        }

        if(timeSlotNo != null) {
            whereList.add(cb.equal(root.get(TSmControlScheduleTimeLog_.id).get(TSmControlScheduleTimeLogPK_.timeSlotNo), timeSlotNo));
        }

        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSmControlScheduleTimeLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleTimeLog> getResultList(List<TSmControlScheduleTimeLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleTimeLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlScheduleTimeLog find(TSmControlScheduleTimeLog t, EntityManager em) {
        return em.find(TSmControlScheduleTimeLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlScheduleTimeLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlScheduleTimeLog merge(TSmControlScheduleTimeLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlScheduleTimeLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
