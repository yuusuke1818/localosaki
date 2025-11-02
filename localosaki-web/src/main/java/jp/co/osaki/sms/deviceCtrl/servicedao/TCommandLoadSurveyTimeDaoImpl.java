package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TCommandLoadSurveyTime;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTimePK_;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTime_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TCommandLoadSurveyTimeDaoImpl implements BaseServiceDao<TCommandLoadSurveyTime>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TCommandLoadSurveyTime> getResultList(TCommandLoadSurveyTime target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommandLoadSurveyTime> query = builder.createQuery(TCommandLoadSurveyTime.class);

        Root<TCommandLoadSurveyTime> root = query.from(TCommandLoadSurveyTime.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.id).get(TCommandLoadSurveyTimePK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.id).get(TCommandLoadSurveyTimePK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommandLoadSurveyTime_.recDate)));
        return   em.createQuery(query).getResultList();

    }

    @Override
    public List<TCommandLoadSurveyTime> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommandLoadSurveyTime> getResultList(List<TCommandLoadSurveyTime> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommandLoadSurveyTime> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TCommandLoadSurveyTime find(TCommandLoadSurveyTime target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommandLoadSurveyTime> query = builder.createQuery(TCommandLoadSurveyTime.class);

        Root<TCommandLoadSurveyTime> root = query.from(TCommandLoadSurveyTime.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.id).get(TCommandLoadSurveyTimePK_.devId), devId));
        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.id).get(TCommandLoadSurveyTimePK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyTime_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommandLoadSurveyTime_.recDate)));
        return   em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TCommandLoadSurveyTime target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public TCommandLoadSurveyTime merge(TCommandLoadSurveyTime target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TCommandLoadSurveyTime target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return;

    }

}
