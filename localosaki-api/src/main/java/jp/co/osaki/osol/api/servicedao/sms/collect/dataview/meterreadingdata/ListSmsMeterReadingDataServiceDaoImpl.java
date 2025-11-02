package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataSearchResultData;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsMeterReadingDataServiceDaoImpl implements BaseServiceDao<ListSmsMeterReadingDataSearchResultData> {
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterReadingDataSearchResultData> getResultList(ListSmsMeterReadingDataSearchResultData target, EntityManager em) {

        String devId = target.getDevId();
        Long meterMngId = target.getMeterMngId();
        String inspYear = target.getYear();
        String inspMonth = target.getMonth();
        Long monthMeterReadingNo = target.getMonthMeterReadingNo();
        String meterReadingType = target.getMeterReadingType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterReadingDataSearchResultData> query = builder.createQuery(ListSmsMeterReadingDataSearchResultData.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();

        //装置ID
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        //メーター管理番号
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        //表示年
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        //表示月
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        //月検針連番
        //対象の年月は検針連番指定する
        if(monthMeterReadingNo != null) {
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), monthMeterReadingNo));
        }
        //検針タイプ
        //対象の年月は検針タイプ指定する
        if(meterReadingType != null) {
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), meterReadingType));
        }

        query = query.select(builder.construct(ListSmsMeterReadingDataSearchResultData.class,
                root.get(TInspectionMeterSvr_.latestInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal),
                root.get(TInspectionMeterSvr_.multipleRate),
                root.get(TInspectionMeterSvr_.latestUseVal),
                root.get(TInspectionMeterSvr_.prevUseVal),
                root.get(TInspectionMeterSvr_.usePerRate),
                root.get(TInspectionMeterSvr_.latestInspDate),
                root.get(TInspectionMeterSvr_.prevInspDate),
                root.get(TInspectionMeterSvr_.endFlg),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.inspType))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        query.orderBy(builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterReadingDataSearchResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataSearchResultData> getResultList(List<ListSmsMeterReadingDataSearchResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataSearchResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterReadingDataSearchResultData find(ListSmsMeterReadingDataSearchResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterReadingDataSearchResultData target, EntityManager em) {

    }

    @Override
    public ListSmsMeterReadingDataSearchResultData merge(ListSmsMeterReadingDataSearchResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterReadingDataSearchResultData target, EntityManager em) {


    }
}
