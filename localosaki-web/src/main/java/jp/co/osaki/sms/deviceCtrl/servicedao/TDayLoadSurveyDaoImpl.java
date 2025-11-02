package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TDayLoadSurveyDaoImpl implements BaseServiceDao<TDayLoadSurvey> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TDayLoadSurvey> getResultList(TDayLoadSurvey target, EntityManager em) {
        String devId = target.getId().getDevId();
        String date = target.getId().getGetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurvey> query = builder.createQuery(TDayLoadSurvey.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        whereList.add(builder.like(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), date + DeviceCtrlConstants.percent));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(
                builder.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId)),
                builder.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate)));
        return  em.createQuery(query).getResultList();

    }

    @Override
    public List<TDayLoadSurvey> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TDayLoadSurvey> getResultList(List<TDayLoadSurvey> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TDayLoadSurvey> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TDayLoadSurvey find(TDayLoadSurvey target, EntityManager em) {
        String devId = target.getId().getDevId();
        String date = target.getId().getGetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurvey> query = builder.createQuery(TDayLoadSurvey.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), date));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(
                builder.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId)),
                builder.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate)));
        return  em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TDayLoadSurvey target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public TDayLoadSurvey merge(TDayLoadSurvey target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TDayLoadSurvey target, EntityManager em) {
        em.remove(target);
    }

}
