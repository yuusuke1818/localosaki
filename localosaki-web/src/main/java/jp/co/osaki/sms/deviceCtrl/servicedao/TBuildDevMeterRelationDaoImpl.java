package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TBuildDevMeterRelationDaoImpl implements BaseServiceDao<TBuildDevMeterRelation>   {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(TBuildDevMeterRelation target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildDevMeterRelation> query = builder.createQuery(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId), meterMngId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(List<TBuildDevMeterRelation> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TBuildDevMeterRelation find(TBuildDevMeterRelation target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildDevMeterRelation> query = builder.createQuery(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));
        whereList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId), meterMngId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TBuildDevMeterRelation target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public TBuildDevMeterRelation merge(TBuildDevMeterRelation target, EntityManager em) {

        return em.merge(target);
    }

    @Override
    public void remove(TBuildDevMeterRelation target, EntityManager em) {
        em.remove(target);
        return;

    }

}
