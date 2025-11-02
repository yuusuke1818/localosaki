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

import jp.co.osaki.osol.api.result.servicedao.CommonWeekWeatherListResult;
import jp.co.osaki.osol.entity.TWeekWeather;
import jp.co.osaki.osol.entity.TWeekWeatherPK_;
import jp.co.osaki.osol.entity.TWeekWeather_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 週間天気データ取得用 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonWeekWeatherListServiceDaoImpl implements BaseServiceDao<CommonWeekWeatherListResult>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeekWeatherListResult> getResultList(CommonWeekWeatherListResult target, EntityManager em) {
        String cityCd = target.getCityCd();
        Date targetDateFrom = target.getTargetDateFrom();
        Date targetDateTo = target.getTargetDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonWeekWeatherListResult> query = builder.createQuery(CommonWeekWeatherListResult.class);

        Root<TWeekWeather> root = query.from(TWeekWeather.class);

        List<Predicate> whereList = new ArrayList<>();

        //市町村コード
        whereList.add(builder.equal(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.cityCd), cityCd));
        //対象日付
        whereList.add(builder.greaterThanOrEqualTo(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.targetDate), targetDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TWeekWeather_.id).get(TWeekWeatherPK_.targetDate), targetDateTo));

        query = query.select(builder.construct(CommonWeekWeatherListResult.class,
                root.get(TWeekWeather_.id).get(TWeekWeatherPK_.cityCd),
                root.get(TWeekWeather_.id).get(TWeekWeatherPK_.targetDate),
                root.get(TWeekWeather_.weatherState),
                root.get(TWeekWeather_.highTemp),
                root.get(TWeekWeather_.lowTemp),
                root.get(TWeekWeather_.rainProbability),
                root.get(TWeekWeather_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonWeekWeatherListResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeekWeatherListResult> getResultList(List<CommonWeekWeatherListResult> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeekWeatherListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonWeekWeatherListResult find(CommonWeekWeatherListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonWeekWeatherListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonWeekWeatherListResult merge(CommonWeekWeatherListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonWeekWeatherListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
