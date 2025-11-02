package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MMeterLoadlimitPK_;
import jp.co.osaki.osol.entity.MMeterLoadlimit_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MMeterLoadlimitDaoImpl implements BaseServiceDao<MMeterLoadlimit> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MMeterLoadlimit> getResultList(MMeterLoadlimit target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterLoadlimit> query = builder.createQuery(MMeterLoadlimit.class);

        Root<MMeterLoadlimit> root = query.from(MMeterLoadlimit.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.devId), devId));


        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.meterMngId), meterMngId));
        }


        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeterLoadlimit> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterLoadlimit> getResultList(List<MMeterLoadlimit> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterLoadlimit> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MMeterLoadlimit find(MMeterLoadlimit target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterLoadlimit> query = builder.createQuery(MMeterLoadlimit.class);

        Root<MMeterLoadlimit> root = query.from(MMeterLoadlimit.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.devId), devId));

        if(target.getId().getMeterMngId() != null) {
            Long meterMngId = target.getId().getMeterMngId();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.meterMngId), meterMngId));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MMeterLoadlimit_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeterLoadlimit_.id).get(MMeterLoadlimitPK_.meterMngId)));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MMeterLoadlimit target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeterLoadlimit merge(MMeterLoadlimit target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeterLoadlimit target, EntityManager em) {


    }

}
