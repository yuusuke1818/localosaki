package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDemandPowerForecastDay;
import jp.co.osaki.osol.entity.TDemandPowerForecastDayPK_;
import jp.co.osaki.osol.entity.TDemandPowerForecastDay_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 需要電力予測1日値データ EntityServiceDaoクラス
 * @author ya-ishida
 *
 */
public class TDemandPowerForecastDayServiceDaoImpl implements BaseServiceDao<TDemandPowerForecastDay> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastDay> getResultList(TDemandPowerForecastDay target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDemandPowerForecastDay> query = builder.createQuery(TDemandPowerForecastDay.class);

        Root<TDemandPowerForecastDay> root = query.from(TDemandPowerForecastDay.class);

        List<Predicate> whereList = new ArrayList<>();

        if(target.getId() != null) {
            // 機器ID
            if(target.getId().getSmId() != null) {
                whereList.add(builder.equal(root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.smId), target.getId().getSmId()));
            }

            // 予測年月日
            if(target.getId().getForecastDate() != null) {
                whereList.add(builder.equal(root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.forecastDate), target.getId().getForecastDate()));
            }
        }

        if (whereList.isEmpty()) {
            query = query.select(root);
        } else {
            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<TDemandPowerForecastDay> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastDay> getResultList(List<TDemandPowerForecastDay> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastDay> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDemandPowerForecastDay find(TDemandPowerForecastDay target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TDemandPowerForecastDay target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TDemandPowerForecastDay merge(TDemandPowerForecastDay target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TDemandPowerForecastDay target, EntityManager em) {
        em.remove(target);
    }

}
