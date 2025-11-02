package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MDevPrmDaoImpl implements BaseServiceDao<MDevPrm>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MDevPrm> getResultList(MDevPrm target, EntityManager em) {
        String devId = target.getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevPrm> query = builder.createQuery(MDevPrm.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MDevPrm_.devId), devId));
        if(target.getDevPw() != null) {
            String devPw = target.getDevPw();
            whereList.add(builder.equal(root.get(MDevPrm_.devPw), devPw));
        }


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MDevPrm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevPrm> getResultList(List<MDevPrm> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevPrm> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MDevPrm find(MDevPrm target, EntityManager em) {
        String devId = target.getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevPrm> query = builder.createQuery(MDevPrm.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MDevPrm_.devId), devId));


        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();

    }

    @Override
    public void persist(MDevPrm target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MDevPrm merge(MDevPrm target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MDevPrm target, EntityManager em) {
        em.remove(target);

    }

}
