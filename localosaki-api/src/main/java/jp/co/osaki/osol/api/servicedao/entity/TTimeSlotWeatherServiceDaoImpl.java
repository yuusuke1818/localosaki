/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import jp.co.osaki.osol.entity.TTimeSlotWeather;
import jp.co.osaki.osol.entity.TTimeSlotWeatherPK_;
import jp.co.osaki.osol.entity.TTimeSlotWeather_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TTimeSlotWeatherServiceDaoImpl implements BaseServiceDao<TTimeSlotWeather> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TTimeSlotWeather> getResultList(TTimeSlotWeather t, EntityManager em) {
        String cityCd = t.getId().getCityCd();
        Timestamp timestamp = (Timestamp) t.getId().getTargetDate();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TTimeSlotWeather> query = cb.createQuery(TTimeSlotWeather.class);
        Root<TTimeSlotWeather> root = query.from(TTimeSlotWeather.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(TTimeSlotWeather_.id).get(TTimeSlotWeatherPK_.cityCd), cityCd));
        whereList.add(cb.equal(root.get(TTimeSlotWeather_.id).get(TTimeSlotWeatherPK_.targetDate), timestamp));
        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[]{})))
                .orderBy(cb.asc(root.get(TTimeSlotWeather_.id).get(TTimeSlotWeatherPK_.weatherTimeSlot)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TTimeSlotWeather> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TTimeSlotWeather> getResultList(List<TTimeSlotWeather> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TTimeSlotWeather> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TTimeSlotWeather find(TTimeSlotWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TTimeSlotWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TTimeSlotWeather merge(TTimeSlotWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TTimeSlotWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
