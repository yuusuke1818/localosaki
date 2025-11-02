package jp.co.osaki.osol.api.servicedao.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TSmControlHolidayCalLog;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLogPK_;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class TSmControlHolidayCalLogServiceDaoImpl implements BaseServiceDao<TSmControlHolidayCalLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayCalLog> getResultList(TSmControlHolidayCalLog t, EntityManager em) {
        Long smControlHolidayLogId = t.getId().getSmControlHolidayLogId();
        Long smId = t.getId().getSmId();
        String holidayMmdd = t.getId().getHolidayMmdd();
        Long createUserId = t.getCreateUserId();
        Timestamp createDate = t.getCreateDate();
        Long updateUserId = t.getUpdateUserId();
        Timestamp updateDate = t.getUpdateDate();
        Integer version = t.getVersion();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSmControlHolidayCalLog> query = cb.createQuery(TSmControlHolidayCalLog.class);
        Root<TSmControlHolidayCalLog> root = query.from(TSmControlHolidayCalLog.class);
        List<Predicate> whereList = new ArrayList<>();
        if(smControlHolidayLogId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smControlHolidayLogId), smControlHolidayLogId));
        }

        if(smId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smId), smId));
        }

        if(holidayMmdd != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.holidayMmdd), holidayMmdd));
        }

        if(createUserId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.createUserId), createUserId));
        }

        if(createDate != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.createDate), createDate));
        }

        if(updateUserId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.updateUserId), updateUserId));
        }

        if(updateDate != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.updateDate), updateDate));
        }

        if(version != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayCalLog_.version), version));
        }

        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSmControlHolidayCalLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayCalLog> getResultList(List<TSmControlHolidayCalLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayCalLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlHolidayCalLog find(TSmControlHolidayCalLog t, EntityManager em) {
        return em.find(TSmControlHolidayCalLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlHolidayCalLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlHolidayCalLog merge(TSmControlHolidayCalLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlHolidayCalLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
