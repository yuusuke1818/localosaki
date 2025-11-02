package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MManualInsp;
import jp.co.osaki.osol.entity.MManualInspPK_;
import jp.co.osaki.osol.entity.MManualInsp_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MManualInspDaoImpl implements BaseServiceDao<MManualInsp> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MManualInsp> getResultList(MManualInsp target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MManualInsp> query = builder.createQuery(MManualInsp.class);

        Root<MManualInsp> root = query.from(MManualInsp.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MManualInsp_.id).get(MManualInspPK_.devId), devId));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(MManualInsp_.id).get(MManualInspPK_.meterMngId), meterMngId));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MManualInsp_.commandFlg),commandFlg));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MManualInsp_.id).get(MManualInspPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MManualInsp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MManualInsp> getResultList(List<MManualInsp> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MManualInsp> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MManualInsp find(MManualInsp target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MManualInsp> query = builder.createQuery(MManualInsp.class);

        Root<MManualInsp> root = query.from(MManualInsp.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MManualInsp_.id).get(MManualInspPK_.devId), devId));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(MManualInsp_.id).get(MManualInspPK_.meterMngId), meterMngId));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MManualInsp_.id).get(MManualInspPK_.meterMngId)));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MManualInsp target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MManualInsp merge(MManualInsp target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MManualInsp target, EntityManager em) {
        MManualInsp t = find(target, em);
        em.remove(t);
    }

}
