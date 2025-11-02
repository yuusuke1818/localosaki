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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastTimeListResult;
import jp.co.osaki.osol.entity.TDemandPowerForecastTime;
import jp.co.osaki.osol.entity.TDemandPowerForecastTimePK_;
import jp.co.osaki.osol.entity.TDemandPowerForecastTime_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 需要電力予測30分値データ取得用 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonDemandPowerForecastTimeListServiceDaoImpl implements BaseServiceDao<CommonDemandPowerForecastTimeListResult>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastTimeListResult> getResultList(CommonDemandPowerForecastTimeListResult target,
            EntityManager em) {
        Long smId = target.getSmId();
        Date forecastDate = target.getForecastDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandPowerForecastTimeListResult> query = builder.createQuery(CommonDemandPowerForecastTimeListResult.class);

        Root<TDemandPowerForecastTime> root = query.from(TDemandPowerForecastTime.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.smId), smId));
        //予測年月日
        if(forecastDate != null) {
            whereList.add(builder.equal(root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.forecastDate), forecastDate));
        }

        query = query.select(builder.construct(CommonDemandPowerForecastTimeListResult.class,
                root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.smId),
                root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.forecastDate),
                root.get(TDemandPowerForecastTime_.id).get(TDemandPowerForecastTimePK_.jigenNo),
                root.get(TDemandPowerForecastTime_.normalForecastValue),
                root.get(TDemandPowerForecastTime_.comfortableForecastValue),
                root.get(TDemandPowerForecastTime_.ecoForecastValue),
                root.get(TDemandPowerForecastTime_.aiForecastValue),
                root.get(TDemandPowerForecastTime_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandPowerForecastTimeListResult> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastTimeListResult> getResultList(
            List<CommonDemandPowerForecastTimeListResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandPowerForecastTimeListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandPowerForecastTimeListResult find(CommonDemandPowerForecastTimeListResult target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandPowerForecastTimeListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandPowerForecastTimeListResult merge(CommonDemandPowerForecastTimeListResult target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandPowerForecastTimeListResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
