package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev_;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TDayLoadSurveyRevDaoImpl implements BaseServiceDao<TDayLoadSurveyRev>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TDayLoadSurveyRev> getResultList(TDayLoadSurveyRev target, EntityManager em) {
        String devId = target.getId().getDevId();
        String date = target.getId().getGetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurveyRev> query = builder.createQuery(TDayLoadSurveyRev.class);

        Root<TDayLoadSurveyRev> root = query.from(TDayLoadSurveyRev.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.devId), devId));
        whereList.add(builder.like(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate), date + DeviceCtrlConstants.percent));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId), meterMngId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(
                builder.asc(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId)),
                builder.asc(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate)));
        return  em.createQuery(query).getResultList();
    }

    @Override
    public List<TDayLoadSurveyRev> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TDayLoadSurveyRev> getResultList(List<TDayLoadSurveyRev> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TDayLoadSurveyRev> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TDayLoadSurveyRev find(TDayLoadSurveyRev target, EntityManager em) {
        String devId = target.getId().getDevId();
        String date = target.getId().getGetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurveyRev> query = builder.createQuery(TDayLoadSurveyRev.class);

        Root<TDayLoadSurveyRev> root = query.from(TDayLoadSurveyRev.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.devId), devId));
        whereList.add(builder.equal(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate), date));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId), meterMngId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(
                builder.asc(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId)),
                builder.asc(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate)));
        return  em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TDayLoadSurveyRev target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public TDayLoadSurveyRev merge(TDayLoadSurveyRev target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TDayLoadSurveyRev target, EntityManager em) {
        em.remove(target);
        return ;
    }

}
