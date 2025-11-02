package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TApiAlertMailSetting;
import jp.co.osaki.osol.entity.TApiAlertMailSettingPK_;
import jp.co.osaki.osol.entity.TApiAlertMailSetting_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class OsolApiAlertMailSettingServiceDaoImpl implements BaseServiceDao<TApiAlertMailSetting> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<TApiAlertMailSetting> getResultList(TApiAlertMailSetting target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TApiAlertMailSetting> query = builder.createQuery(TApiAlertMailSetting.class);

        Root<TApiAlertMailSetting> root = query.from(TApiAlertMailSetting.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TApiAlertMailSetting_.id).get(TApiAlertMailSettingPK_.corpId), target.getId().getCorpId()));

        if (target.getId().getApiAlertMailSettingId() != null) {
            whereList.add(builder.equal(root.get(TApiAlertMailSetting_.id).get(TApiAlertMailSettingPK_.apiAlertMailSettingId), target.getId().getApiAlertMailSettingId()));
        }

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TApiAlertMailSetting> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<TApiAlertMailSetting> getResultList(List<TApiAlertMailSetting> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<TApiAlertMailSetting> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public TApiAlertMailSetting find(TApiAlertMailSetting target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(TApiAlertMailSetting target, EntityManager em) {

    }

    @Override
    public TApiAlertMailSetting merge(TApiAlertMailSetting target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(TApiAlertMailSetting target, EntityManager em) {

    }
}
