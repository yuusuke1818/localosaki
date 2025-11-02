package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK_;
import jp.co.osaki.osol.entity.TWorkHst_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TWorkHstDaoImpl implements BaseServiceDao<TWorkHst>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TWorkHst> getResultList(TWorkHst target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TWorkHst> query = builder.createQuery(TWorkHst.class);

        Root<TWorkHst> root = query.from(TWorkHst.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TWorkHst_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TWorkHst_.id).get(TWorkHstPK_.recDate)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TWorkHst> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TWorkHst> getResultList(List<TWorkHst> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TWorkHst> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TWorkHst find(TWorkHst target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TWorkHst> query = builder.createQuery(TWorkHst.class);

        Root<TWorkHst> root = query.from(TWorkHst.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TWorkHst_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TWorkHst target, EntityManager em) {
        em.persist(target);
        return ;
    }

    @Override
    public TWorkHst merge(TWorkHst target, EntityManager em) {
        return em.merge(em.contains(target) ? target : em.merge(target));
    }

    @Override
    public void remove(TWorkHst target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return ;
    }

}
