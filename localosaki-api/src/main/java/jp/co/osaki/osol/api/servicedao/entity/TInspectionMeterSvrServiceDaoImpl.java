package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TInspectionMeterSvrServiceDaoImpl implements BaseServiceDao<TInspectionMeterSvr> {
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target, EntityManager em) {

        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();
        String inspYear = target.getId().getInspYear();
        String inspMonth = target.getId().getInspMonth();
        Long monthMeterReadingNo = target.getId().getInspMonthNo();
        String meterReadingType = target.getInspType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

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
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), monthMeterReadingNo));
        //検針タイプ
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), meterReadingType));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(List<TInspectionMeterSvr> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TInspectionMeterSvr find(TInspectionMeterSvr target, EntityManager em) {

        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();
        String inspYear = target.getId().getInspYear();
        String inspMonth = target.getId().getInspMonth();
        Long monthMeterReadingNo = target.getId().getInspMonthNo();
        String meterReadingType = target.getInspType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

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
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), monthMeterReadingNo));
        //検針タイプ
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), meterReadingType));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TInspectionMeterSvr target, EntityManager em) {

    }

    @Override
    public TInspectionMeterSvr merge(TInspectionMeterSvr target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(TInspectionMeterSvr target, EntityManager em) {


    }
}
