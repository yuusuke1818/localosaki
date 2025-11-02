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

import jp.co.osaki.osol.api.resultdata.weather.TimelyWeatherListDetailResultData;
import jp.co.osaki.osol.entity.TTimelyWeather;
import jp.co.osaki.osol.entity.TTimelyWeatherPK_;
import jp.co.osaki.osol.entity.TTimelyWeather_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 時間別天気情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class TimelyWeatherListServiceDaoImpl implements BaseServiceDao<TimelyWeatherListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TimelyWeatherListDetailResultData> getResultList(TimelyWeatherListDetailResultData target,
            EntityManager em) {
        Date targetDateTime = target.getTargetDateTime();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TimelyWeatherListDetailResultData> query = builder.createQuery(TimelyWeatherListDetailResultData.class);

        Root<TTimelyWeather> root = query.from(TTimelyWeather.class);

        List<Predicate> whereList = new ArrayList<>();

        // 対象日時
        whereList.add(builder.greaterThanOrEqualTo(root.get(TTimelyWeather_.id).get(TTimelyWeatherPK_.targetDateTime), targetDateTime));
        if(!CheckUtility.isNullOrEmpty(target.getCityCd())) {
            whereList.add(builder.equal(root.get(TTimelyWeather_.id).get(TTimelyWeatherPK_.cityCd), target.getCityCd()));
        }

        query = query.select(builder.construct(TimelyWeatherListDetailResultData.class,
                root.get(TTimelyWeather_.id).get(TTimelyWeatherPK_.cityCd),
                root.get(TTimelyWeather_.id).get(TTimelyWeatherPK_.targetDateTime),
                root.get(TTimelyWeather_.weather),
                root.get(TTimelyWeather_.barometricPressure),
                root.get(TTimelyWeather_.rainAmount),
                root.get(TTimelyWeather_.windDirection),
                root.get(TTimelyWeather_.windSpeed),
                root.get(TTimelyWeather_.temperature),
                root.get(TTimelyWeather_.humidity),
                root.get(TTimelyWeather_.cloudAmount),
                root.get(TTimelyWeather_.version))).
                where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TimelyWeatherListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TimelyWeatherListDetailResultData> getResultList(List<TimelyWeatherListDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TimelyWeatherListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TimelyWeatherListDetailResultData find(TimelyWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(TimelyWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TimelyWeatherListDetailResultData merge(TimelyWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(TimelyWeatherListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
