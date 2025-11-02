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

import jp.co.osaki.osol.entity.TWeekWeather;
import jp.co.osaki.osol.entity.TWeekWeatherPK_;
import jp.co.osaki.osol.entity.TWeekWeather_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TWeekWeatherServiceDaoImpl implements BaseServiceDao<TWeekWeather> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWeekWeather> getResultList(TWeekWeather t, EntityManager em) {
        String cityCd = t.getId().getCityCd();
        Timestamp timestamp = (Timestamp) t.getId().getTargetDate();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TWeekWeather> query = cb.createQuery(TWeekWeather.class);
        Root<TWeekWeather> root = query.from(TWeekWeather.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.cityCd), cityCd));
        whereList.add(cb.greaterThanOrEqualTo(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.targetDate), timestamp));
        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[]{})))
                .orderBy(cb.asc(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.targetDate)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TWeekWeather> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWeekWeather> getResultList(List<TWeekWeather> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWeekWeather> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TWeekWeather find(TWeekWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TWeekWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TWeekWeather merge(TWeekWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TWeekWeather t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
