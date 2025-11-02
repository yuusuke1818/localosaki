package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.GetSmsMeterReadingDataSearchMaxDemandValResultData;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GetSmsMeterReadingDataSearchMaxDemandValDaoImpl
        implements BaseServiceDao<GetSmsMeterReadingDataSearchMaxDemandValResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<GetSmsMeterReadingDataSearchMaxDemandValResultData> getResultList(
            GetSmsMeterReadingDataSearchMaxDemandValResultData target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GetSmsMeterReadingDataSearchMaxDemandValResultData> query = builder.createQuery(GetSmsMeterReadingDataSearchMaxDemandValResultData.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> whereList = new ArrayList<>();

        // 装置ID
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), target.getDevId()));
        // メーター管理番号
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), target.getMeterMngId()));
        // 取得日時
        whereList.add(builder.greaterThan(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), target.getPrevInspDate().replaceAll("[/: ]", "")));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), target.getLatestInspDate().replaceAll("[/: ]", "")));

        query = query.select(builder.construct(GetSmsMeterReadingDataSearchMaxDemandValResultData.class,
                builder.max(root.get(TDayLoadSurvey_.kwh30)))).where(whereList.toArray(new Predicate[]{}));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<GetSmsMeterReadingDataSearchMaxDemandValResultData> getResultList(
            Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<GetSmsMeterReadingDataSearchMaxDemandValResultData> getResultList(
            List<GetSmsMeterReadingDataSearchMaxDemandValResultData> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<GetSmsMeterReadingDataSearchMaxDemandValResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public GetSmsMeterReadingDataSearchMaxDemandValResultData find(
            GetSmsMeterReadingDataSearchMaxDemandValResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(GetSmsMeterReadingDataSearchMaxDemandValResultData target, EntityManager em) {
    }

    @Override
    public GetSmsMeterReadingDataSearchMaxDemandValResultData merge(
            GetSmsMeterReadingDataSearchMaxDemandValResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(GetSmsMeterReadingDataSearchMaxDemandValResultData target, EntityManager em) {
    }
}
