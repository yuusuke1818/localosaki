package jp.co.osaki.osol.api.servicedao.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.weather.AmedasWeatherListDetailResultData;
import jp.co.osaki.osol.entity.TAmedasWeather;
import jp.co.osaki.osol.entity.TAmedasWeatherPK_;
import jp.co.osaki.osol.entity.TAmedasWeather_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * アメダス気象情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class AmedasWeatherListServiceDaoImpl implements BaseServiceDao<AmedasWeatherListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AmedasWeatherListDetailResultData> getResultList(AmedasWeatherListDetailResultData target,
            EntityManager em) {
        Date observationDateFrom = target.getObservationDateFrom();
        Date observationDateTo = target.getObservationDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AmedasWeatherListDetailResultData> query = builder.createQuery(AmedasWeatherListDetailResultData.class);

        Root<TAmedasWeather> root = query.from(TAmedasWeather.class);

        List<Predicate> whereList = new ArrayList<>();

        // 観測日時
        whereList.add(builder.greaterThanOrEqualTo(root.get(TAmedasWeather_.id).get(TAmedasWeatherPK_.observationDate), observationDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TAmedasWeather_.id).get(TAmedasWeatherPK_.observationDate), observationDateTo));

        query = query.select(builder.construct(AmedasWeatherListDetailResultData.class,
                root.get(TAmedasWeather_.id).get(TAmedasWeatherPK_.amedasObservatoryNo),
                root.get(TAmedasWeather_.id).get(TAmedasWeatherPK_.observationDate),
                root.get(TAmedasWeather_.outAirTemp),
                root.get(TAmedasWeather_.version))).
                where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AmedasWeatherListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AmedasWeatherListDetailResultData> getResultList(List<AmedasWeatherListDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AmedasWeatherListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AmedasWeatherListDetailResultData find(AmedasWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(AmedasWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AmedasWeatherListDetailResultData merge(AmedasWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(AmedasWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
