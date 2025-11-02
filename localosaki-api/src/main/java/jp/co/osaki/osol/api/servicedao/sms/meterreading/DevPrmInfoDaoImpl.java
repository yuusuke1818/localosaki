package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataInfoSearchResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class DevPrmInfoDaoImpl implements BaseServiceDao<ListSmsMeterReadingDataInfoSearchResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(
            ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {
        List<String> devIdList = target.getDevIdList();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterReadingDataInfoSearchResultData> query = builder.createQuery(ListSmsMeterReadingDataInfoSearchResultData.class);
        List<Predicate> whereList = new ArrayList<>();

        Root<MDevPrm> root = query.from(MDevPrm.class);
        whereList.add(builder.and(root.get(MDevPrm_.devId).in(devIdList)));

        query = query.select(builder.construct(ListSmsMeterReadingDataInfoSearchResultData.class,
                root.get(MDevPrm_.devId),
                root.get(MDevPrm_.name))).distinct(true).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        query.orderBy(
                builder.asc(root.get(MDevPrm_.devId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(
            List<ListSmsMeterReadingDataInfoSearchResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterReadingDataInfoSearchResultData find(ListSmsMeterReadingDataInfoSearchResultData target,
            EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {


    }

    @Override
    public ListSmsMeterReadingDataInfoSearchResultData merge(ListSmsMeterReadingDataInfoSearchResultData target,
            EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {


    }
}
