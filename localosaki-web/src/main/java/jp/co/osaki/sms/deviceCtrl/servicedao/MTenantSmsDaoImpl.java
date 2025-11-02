package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MTenantSmsDaoImpl implements BaseServiceDao<MTenantSm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MTenantSm> getResultList(MTenantSm target, EntityManager em) {
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MTenantSm> query = builder.createQuery(MTenantSm.class);

        Root<MTenantSm> root = query.from(MTenantSm.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MTenantSm_.id).get(MTenantSmPK_.buildingId), buildingId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MTenantSm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MTenantSm> getResultList(List<MTenantSm> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MTenantSm> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MTenantSm find(MTenantSm target, EntityManager em) {
        Long buildingId = target.getId().getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MTenantSm> query = builder.createQuery(MTenantSm.class);

        Root<MTenantSm> root = query.from(MTenantSm.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MTenantSm_.id).get(MTenantSmPK_.buildingId), buildingId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MTenantSm target, EntityManager em) {


    }

    @Override
    public MTenantSm merge(MTenantSm target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MTenantSm target, EntityManager em) {


    }



}
