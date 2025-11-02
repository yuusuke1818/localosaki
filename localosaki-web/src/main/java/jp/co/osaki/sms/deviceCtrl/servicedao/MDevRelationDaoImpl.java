package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MDevRelationDaoImpl implements BaseServiceDao<MDevRelation> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MDevRelation> getResultList(MDevRelation target, EntityManager em) {
        String devId = target.getId().getDevId();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelation> query = builder.createQuery(MDevRelation.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        List<MDevRelation> resultList = em.createQuery(query).getResultList();
        return resultList;
    }

    @Override
    public List<MDevRelation> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevRelation> getResultList(List<MDevRelation> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevRelation> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MDevRelation find(MDevRelation target, EntityManager em) {
        String devId = target.getId().getDevId();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevRelation> query = builder.createQuery(MDevRelation.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MDevRelation target, EntityManager em) {
    	em.persist(target);		// 2024-01-19
    }

    @Override
    public MDevRelation merge(MDevRelation target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MDevRelation target, EntityManager em) {


    }



}
