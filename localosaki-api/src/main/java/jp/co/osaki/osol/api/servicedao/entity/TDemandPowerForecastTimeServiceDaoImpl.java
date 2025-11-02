package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDemandPowerForecastTime;
import jp.co.osaki.osol.entity.TDemandPowerForecastTimePK_;
import jp.co.osaki.osol.entity.TDemandPowerForecastTime_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 需要電力予測30分値データ EntityServiceDaoクラス
 * @author ya-ishida
 *
 */
public class TDemandPowerForecastTimeServiceDaoImpl implements BaseServiceDao<TDemandPowerForecastTime> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastTime> getResultList(TDemandPowerForecastTime target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDemandPowerForecastTime> query = builder.createQuery(TDemandPowerForecastTime.class);

        Root<TDemandPowerForecastTime> root = query.from(TDemandPowerForecastTime.class);

        List<Predicate> whereList = new ArrayList<>();

        if(target.getId() != null) {
            // 機器ID
            if(target.getId().getSmId() != null) {
                whereList.add(builder.equal(root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.smId), target.getId().getSmId()));
            }

            // 予測年月日
            if(target.getId().getForecastDate() != null) {
                whereList.add(builder.equal(root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.forecastDate), target.getId().getForecastDate()));
            }

            // 時限No
            if(target.getId().getJigenNo() != null) {
                whereList.add(builder.equal(root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.jigenNo), target.getId().getJigenNo()));
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
    public List<TDemandPowerForecastTime> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastTime> getResultList(List<TDemandPowerForecastTime> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDemandPowerForecastTime> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDemandPowerForecastTime find(TDemandPowerForecastTime target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TDemandPowerForecastTime target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TDemandPowerForecastTime merge(TDemandPowerForecastTime target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TDemandPowerForecastTime target, EntityManager em) {
        em.remove(target);
    }

}
