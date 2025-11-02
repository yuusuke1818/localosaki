package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MAlertMailSetting;
import jp.co.osaki.osol.entity.MAlertMailSetting_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MAlertMailSettingDaoImpl implements BaseServiceDao<MAlertMailSetting>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MAlertMailSetting> getResultList(MAlertMailSetting target, EntityManager em) {

        return null;
    }

    @Override
    public List<MAlertMailSetting> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {


        return null;
    }

    @Override
    public List<MAlertMailSetting> getResultList(List<MAlertMailSetting> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MAlertMailSetting> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MAlertMailSetting find(MAlertMailSetting target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAlertMailSetting> query = builder.createQuery(MAlertMailSetting.class);

        Root<MAlertMailSetting> root = query.from(MAlertMailSetting.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MAlertMailSetting_.alertCd), target.getAlertCd()));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void persist(MAlertMailSetting target, EntityManager em) {


    }

    @Override
    public MAlertMailSetting merge(MAlertMailSetting target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(MAlertMailSetting target, EntityManager em) {


    }

}
