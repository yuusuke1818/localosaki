package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TCommandDaoImpl implements BaseServiceDao<TCommand>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TCommand> getResultList(TCommand target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = builder.createQuery(TCommand.class);

        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommand_.srvEnt), srvEnt));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommand_.id).get(TCommandPK_.recDate)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TCommand> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommand> getResultList(List<TCommand> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TCommand> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TCommand find(TCommand target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = builder.createQuery(TCommand.class);

        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));

        if(target.getId().getCommand() != null) {
            String command = target.getId().getCommand();
            whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(TCommand_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(TCommand_.id).get(TCommandPK_.recDate)));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TCommand target, EntityManager em) {
        em.persist(target);
        return ;
    }

    @Override
    public TCommand merge(TCommand target, EntityManager em) {
        return em.merge(em.contains(target) ? target : em.merge(target));
    }

    @Override
    public void remove(TCommand target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return ;

    }

}
