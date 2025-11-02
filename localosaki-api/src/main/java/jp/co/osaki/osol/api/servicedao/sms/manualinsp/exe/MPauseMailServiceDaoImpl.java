package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

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

public class MPauseMailServiceDaoImpl implements BaseServiceDao<MPauseMail>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MPauseMail> getResultList(MPauseMail target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MPauseMail> query = builder.createQuery(MPauseMail.class);

        // ■from句の整理
        // メール停止
        Root<MPauseMail> root = query.from(MPauseMail.class);


        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MPauseMail_.devId), target.getDevId()));

        // ■SELECT句の整理
        query = query.select(root).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).setMaxResults(1).getResultList();
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

        return null;
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
