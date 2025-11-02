package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastDayListResult;
import jp.co.osaki.osol.entity.TDemandPowerForecastDay;
import jp.co.osaki.osol.entity.TDemandPowerForecastDayPK_;
import jp.co.osaki.osol.entity.TDemandPowerForecastDay_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 需要電力予測1日値データ取得用 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonDemandPowerForecastDayListServiceDaoImpl implements BaseServiceDao<CommonDemandPowerForecastDayListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastDayListResult> getResultList(CommonDemandPowerForecastDayListResult target,
            EntityManager em) {
        Long smId = target.getSmId();
        Date forecastDateFrom = target.getForecastDateFrom();
        Date forecastDateTo = target.getForecastDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandPowerForecastDayListResult> query = builder.createQuery(CommonDemandPowerForecastDayListResult.class);

        Root<TDemandPowerForecastDay> root = query.from(TDemandPowerForecastDay.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.smId), smId));
        //予測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.forecastDate), forecastDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.forecastDate), forecastDateTo));

        query = query.select(builder.construct(CommonDemandPowerForecastDayListResult.class,
                root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.smId),
                root.get(TDemandPowerForecastDay_.id).get(TDemandPowerForecastDayPK_.forecastDate),
                root.get(TDemandPowerForecastDay_.airDemandForecastValue),
                root.get(TDemandPowerForecastDay_.totalDemandValue),
                root.get(TDemandPowerForecastDay_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandPowerForecastDayListResult> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastDayListResult> getResultList(
            List<CommonDemandPowerForecastDayListResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastDayListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandPowerForecastDayListResult find(CommonDemandPowerForecastDayListResult target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandPowerForecastDayListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandPowerForecastDayListResult merge(CommonDemandPowerForecastDayListResult target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandPowerForecastDayListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
