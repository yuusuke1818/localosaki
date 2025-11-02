package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.osol.entity.TMeterDataPK_;
import jp.co.osaki.osol.entity.TMeterData_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TMeterDataDaoImpl implements BaseServiceDao<TMeterData>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<TMeterData> getResultList(TMeterData target, EntityManager em) {

        return null;
    }

    @Override
    public List<TMeterData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<TMeterData> getResultList(List<TMeterData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<TMeterData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public TMeterData find(TMeterData target, EntityManager em) {
        String devId = target.getId().getDevId();
        Long meterMngId = target.getId().getMeterMngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TMeterData> query = builder.createQuery(TMeterData.class);

        Root<TMeterData> root = query.from(TMeterData.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TMeterData_.id).get(TMeterDataPK_.devId), devId));
        whereList.add(builder.equal(root.get(TMeterData_.id).get(TMeterDataPK_.meterMngId), meterMngId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(TMeterData target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TMeterData merge(TMeterData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(TMeterData target, EntityManager em) {


    }

}
