package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterInfo;
import jp.co.osaki.osol.entity.MMeterInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターの詳細情報 DaoImpl
 *
 * @author y.nakamura
 */
public class MMeterInfoDaoImpl implements BaseServiceDao<MMeterInfo>  {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MMeterInfo> getResultList(MMeterInfo target, EntityManager em) {
        String meterId = target.getMeterId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterInfo> query = builder.createQuery(MMeterInfo.class);

        Root<MMeterInfo> root = query.from(MMeterInfo.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterInfo_.meterId), meterId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{}))).orderBy(builder.asc(root.get(MMeterInfo_.meterId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeterInfo> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterInfo> getResultList(List<MMeterInfo> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MMeterInfo> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MMeterInfo find(MMeterInfo target, EntityManager em) {
        String meterId = target.getMeterId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterInfo> query = builder.createQuery(MMeterInfo.class);

        Root<MMeterInfo> root = query.from(MMeterInfo.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterInfo_.meterId), meterId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getSingleResult();
    }


    @Override
    public void persist(MMeterInfo target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeterInfo merge(MMeterInfo target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeterInfo target, EntityManager em) {
        em.remove(target);
    }

}
