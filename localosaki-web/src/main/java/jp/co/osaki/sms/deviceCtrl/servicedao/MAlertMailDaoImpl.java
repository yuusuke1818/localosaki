package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK_;
import jp.co.osaki.osol.entity.MAlertMail_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MAlertMailDaoImpl implements BaseServiceDao<MAlertMail> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MAlertMail> getResultList(MAlertMail target, EntityManager em) {
        String devId = target.getId().getDevId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAlertMail> query = builder.createQuery(MAlertMail.class);

        Root<MAlertMail> root = query.from(MAlertMail.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MAlertMail_.id).get(MAlertMailPK_.devId), devId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MAlertMail> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MAlertMail> getResultList(List<MAlertMail> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MAlertMail> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MAlertMail find(MAlertMail target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(MAlertMail target, EntityManager em) {


    }

    @Override
    public MAlertMail merge(MAlertMail target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MAlertMail target, EntityManager em) {


    }

}
