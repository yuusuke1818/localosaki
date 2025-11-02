package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeterPK_;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TCommandLoadSurveyMeterDaoImpl implements BaseServiceDao<TCommandLoadSurveyMeter>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TCommandLoadSurveyMeter> getResultList(TCommandLoadSurveyMeter target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommandLoadSurveyMeter> query = builder.createQuery(TCommandLoadSurveyMeter.class);

        Root<TCommandLoadSurveyMeter> root = query.from(TCommandLoadSurveyMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.id).get(TCommandLoadSurveyMeterPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.id).get(TCommandLoadSurveyMeterPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.srvEnt), srvEnt));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommandLoadSurveyMeter_.recDate)));
        return   em.createQuery(query).getResultList();

    }

    @Override
    public List<TCommandLoadSurveyMeter> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommandLoadSurveyMeter> getResultList(List<TCommandLoadSurveyMeter> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommandLoadSurveyMeter> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TCommandLoadSurveyMeter find(TCommandLoadSurveyMeter target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommandLoadSurveyMeter> query = builder.createQuery(TCommandLoadSurveyMeter.class);

        Root<TCommandLoadSurveyMeter> root = query.from(TCommandLoadSurveyMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.id).get(TCommandLoadSurveyMeterPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.id).get(TCommandLoadSurveyMeterPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommandLoadSurveyMeter_.srvEnt), srvEnt));
        }



        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommandLoadSurveyMeter_.recDate)));
        return   em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TCommandLoadSurveyMeter target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TCommandLoadSurveyMeter merge(TCommandLoadSurveyMeter target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TCommandLoadSurveyMeter target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return;
    }

}
