package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MAutoInspDaoImpl implements BaseServiceDao<MAutoInsp> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MAutoInsp> getResultList(MAutoInsp target, EntityManager em) {
        String devId = target.getId().getDevId();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAutoInsp> query = builder.createQuery(MAutoInsp.class);

        Root<MAutoInsp> root = query.from(MAutoInsp.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.devId), devId));

        if(target.getId().getMeterType() != null) {
            Long meterType = target.getId().getMeterType();
            whereList.add(builder.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.meterType), meterType));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MAutoInsp_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MAutoInsp_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MAutoInsp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MAutoInsp> getResultList(List<MAutoInsp> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MAutoInsp> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MAutoInsp find(MAutoInsp target, EntityManager em) {
        String devId = target.getId().getDevId();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAutoInsp> query = builder.createQuery(MAutoInsp.class);

        Root<MAutoInsp> root = query.from(MAutoInsp.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.devId), devId));

        if(target.getId().getMeterType() != null) {
            Long meterType = target.getId().getMeterType();
            whereList.add(builder.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.meterType), meterType));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MAutoInsp_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MAutoInsp_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MAutoInsp target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public MAutoInsp merge(MAutoInsp target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MAutoInsp target, EntityManager em) {
        em.remove(target);
        return;
    }

}
