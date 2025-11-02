package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterInspectionDataMonthNoResultData;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsMonthMeterInspectionNoServiceDaoImpl implements BaseServiceDao<ListSmsMeterInspectionDataMonthNoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterInspectionDataMonthNoResultData> getResultList(ListSmsMeterInspectionDataMonthNoResultData target, EntityManager em) {

        String devId = target.getDevId();
        List<String> devIdList = target.getDevIdList();
        String inspYear = target.getYear();
        String inspMonth = target.getMonth();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterInspectionDataMonthNoResultData> query = builder.createQuery(ListSmsMeterInspectionDataMonthNoResultData.class);

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

        query = query.select(builder.construct(ListSmsMeterInspectionDataMonthNoResultData.class,
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
    public List<ListSmsMeterInspectionDataMonthNoResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterInspectionDataMonthNoResultData> getResultList(List<ListSmsMeterInspectionDataMonthNoResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterInspectionDataMonthNoResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterInspectionDataMonthNoResultData find(ListSmsMeterInspectionDataMonthNoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterInspectionDataMonthNoResultData target, EntityManager em) {

    }

    @Override
    public ListSmsMeterInspectionDataMonthNoResultData merge(ListSmsMeterInspectionDataMonthNoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterInspectionDataMonthNoResultData target, EntityManager em) {


    }
}
