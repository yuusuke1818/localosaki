/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.master;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.master.CommonWeatherCityDetailResultData;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.MWeatherCity;
import jp.co.osaki.osol.entity.MWeatherCity_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class CommonWeatherCityServiceDaoImpl implements BaseServiceDao<CommonWeatherCityDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeatherCityDetailResultData> getResultList(CommonWeatherCityDetailResultData t,
            EntityManager em) {
        String cityName = t.getCityName();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonWeatherCityDetailResultData> query = builder
                .createQuery(CommonWeatherCityDetailResultData.class);

        Root<MWeatherCity> fromMWeatherCity = query.from(MWeatherCity.class);

        List<Predicate> whereList = new ArrayList<>();

        if (!CheckUtility.isNullOrEmpty(cityName)) {
            StringBuilder sb = new StringBuilder("%");
            sb.append(cityName);
            sb.append("%");
            whereList.add(builder.or(
                    builder.like(fromMWeatherCity.get(MWeatherCity_.cityName), sb.toString()),
                    builder.like(fromMWeatherCity.get(MWeatherCity_.cityCd), sb.toString())));
        }

        // 都道府県
        if(t.getPrefectureCdList() != null && !t.getPrefectureCdList().isEmpty()) {
            whereList.add(
                    fromMWeatherCity.get(MWeatherCity_.MPrefecture).get(MPrefecture_.prefectureCd).in(t.getPrefectureCdList()));
        }

        query = query.select(builder.construct(CommonWeatherCityDetailResultData.class,
                fromMWeatherCity.get(MWeatherCity_.cityCd),
                fromMWeatherCity.get(MWeatherCity_.cityName),
                fromMWeatherCity.get(MWeatherCity_.version),
                fromMWeatherCity.get(MWeatherCity_.createUserId),
                fromMWeatherCity.get(MWeatherCity_.createDate),
                fromMWeatherCity.get(MWeatherCity_.updateUserId),
                fromMWeatherCity.get(MWeatherCity_.updateDate),
                fromMWeatherCity.get(MWeatherCity_.MPrefecture).get(MPrefecture_.prefectureCd)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(fromMWeatherCity.get(MWeatherCity_.cityCd)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonWeatherCityDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeatherCityDetailResultData> getResultList(List<CommonWeatherCityDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonWeatherCityDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonWeatherCityDetailResultData find(CommonWeatherCityDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonWeatherCityDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonWeatherCityDetailResultData merge(CommonWeatherCityDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonWeatherCityDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
