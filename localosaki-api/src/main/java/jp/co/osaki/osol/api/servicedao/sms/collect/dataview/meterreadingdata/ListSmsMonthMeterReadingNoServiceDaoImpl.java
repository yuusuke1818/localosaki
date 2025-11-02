package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoResultData;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsMonthMeterReadingNoServiceDaoImpl implements BaseServiceDao<ListSmsMeterReadingDataMonthNoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterReadingDataMonthNoResultData> getResultList(ListSmsMeterReadingDataMonthNoResultData target, EntityManager em) {

        String devId = target.getDevId();
        List<String> devIdList = target.getDevIdList();
        String inspYear = target.getYear();
        String inspMonth = target.getMonth();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterReadingDataMonthNoResultData> query = builder.createQuery(ListSmsMeterReadingDataMonthNoResultData.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();

        //装置ID
        if(null != devId && !devId.equals("0")){
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        }else if(null != devIdList){
            whereList.add(builder.and(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId).in(devIdList)));
        }
        //表示年
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        //表示月
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));

        query = query.select(builder.construct(ListSmsMeterReadingDataMonthNoResultData.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                root.get(TInspectionMeterSvr_.inspType)))
                .distinct(true)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)),
                        builder.asc(root.get(TInspectionMeterSvr_.inspType))
                );

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterReadingDataMonthNoResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataMonthNoResultData> getResultList(List<ListSmsMeterReadingDataMonthNoResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataMonthNoResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterReadingDataMonthNoResultData find(ListSmsMeterReadingDataMonthNoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterReadingDataMonthNoResultData target, EntityManager em) {

    }

    @Override
    public ListSmsMeterReadingDataMonthNoResultData merge(ListSmsMeterReadingDataMonthNoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterReadingDataMonthNoResultData target, EntityManager em) {


    }
}
