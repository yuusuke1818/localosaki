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

import jp.co.osaki.osol.entity.TSmControlHolidayLog;
import jp.co.osaki.osol.entity.TSmControlHolidayLogPK_;
import jp.co.osaki.osol.entity.TSmControlHolidayLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-maruta
 */
public class TSmControlHolidayLogServiceDaoImpl implements BaseServiceDao<TSmControlHolidayLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayLog> getResultList(TSmControlHolidayLog t, EntityManager em) {
        Long smControlHolidayLogId = t.getId().getSmControlHolidayLogId();
        Long smId = t.getId().getSmId();
        Timestamp settingUpdateDatetime = t.getSettingUpdateDatetime();
        Long createUserId = t.getCreateUserId();
        Timestamp createDate = t.getCreateDate();
        Long updateUserId = t.getUpdateUserId();
        Timestamp updateDate = t.getUpdateDate();
        Integer version = t.getVersion();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSmControlHolidayLog> query = cb.createQuery(TSmControlHolidayLog.class);
        Root<TSmControlHolidayLog> root = query.from(TSmControlHolidayLog.class);
        List<Predicate> whereList = new ArrayList<>();
        if(smControlHolidayLogId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smControlHolidayLogId), smControlHolidayLogId));
        }

        if(smId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smId), smId));
        }

        if(settingUpdateDatetime != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.settingUpdateDatetime), settingUpdateDatetime));
        }

        if(createUserId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.createUserId), createUserId));
        }

        if(createDate != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.createDate), createDate));
        }

        if(updateUserId != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.updateUserId), updateUserId));
        }

        if(updateDate != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.updateDate), updateDate));
        }

        if(version != null) {
            whereList.add(cb.equal(root.get(TSmControlHolidayLog_.version), version));
        }

        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSmControlHolidayLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayLog> getResultList(List<TSmControlHolidayLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlHolidayLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlHolidayLog find(TSmControlHolidayLog t, EntityManager em) {
        return em.find(TSmControlHolidayLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlHolidayLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlHolidayLog merge(TSmControlHolidayLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlHolidayLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
