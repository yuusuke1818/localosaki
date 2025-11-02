package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.MRepeaterPK_;
import jp.co.osaki.osol.entity.MRepeater_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MRepeaterDaoImpl implements BaseServiceDao<MRepeater> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MRepeater> getResultList(MRepeater target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MRepeater> query = builder.createQuery(MRepeater.class);

        Root<MRepeater> root = query.from(MRepeater.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.devId), devId));

        if(target.getId().getRepeaterMngId() != null) {
            Long repeaterMngId = target.getId().getRepeaterMngId();
            whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.repeaterMngId), repeaterMngId));
        }

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MRepeater_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MRepeater_.srvEnt), srvEnt));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MRepeater> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MRepeater> getResultList(List<MRepeater> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MRepeater> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MRepeater find(MRepeater target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long repeaterMngId = target.getId().getRepeaterMngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MRepeater> query = builder.createQuery(MRepeater.class);

        Root<MRepeater> root = query.from(MRepeater.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.devId), devId));
        whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.repeaterMngId), repeaterMngId));

        if(target.getCommandFlg() != null) {
            String commandFlg = target.getCommandFlg();
            whereList.add(builder.equal(root.get(MRepeater_.commandFlg), commandFlg));
        }

        if(target.getSrvEnt() != null) {
            String srvEnt = target.getSrvEnt();
            whereList.add(builder.equal(root.get(MRepeater_.srvEnt), srvEnt));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MRepeater target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public MRepeater merge(MRepeater target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MRepeater target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return;
    }

}
