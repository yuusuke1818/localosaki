package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.MPauseMail_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MPauseMailDaoImpl implements BaseServiceDao<MPauseMail> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MPauseMail> getResultList(MPauseMail target, EntityManager em) {
        String devId = target.getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MPauseMail> query = builder.createQuery(MPauseMail.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        List<MPauseMail> list = em.createQuery(query).getResultList();
        if(list == null) {
            return new ArrayList<MPauseMail>();
        }
        return list;
    }

    @Override
    public List<MPauseMail> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MPauseMail> getResultList(List<MPauseMail> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MPauseMail> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MPauseMail find(MPauseMail target, EntityManager em) {
        String devId = target.getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MPauseMail> query = builder.createQuery(MPauseMail.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MPauseMail target, EntityManager em) {


    }

    @Override
    public MPauseMail merge(MPauseMail target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MPauseMail target, EntityManager em) {


    }

}
