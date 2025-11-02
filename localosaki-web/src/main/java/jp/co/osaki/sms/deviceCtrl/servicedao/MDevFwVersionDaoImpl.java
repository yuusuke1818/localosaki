package jp.co.osaki.sms.deviceCtrl.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MDevFwVersion;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MDevFwVersionDaoImpl implements BaseServiceDao<MDevFwVersion>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<MDevFwVersion> getResultList(MDevFwVersion target, EntityManager em) {
        return null;
    }

    @Override
    public List<MDevFwVersion> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevFwVersion> getResultList(List<MDevFwVersion> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MDevFwVersion> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MDevFwVersion find(MDevFwVersion target, EntityManager em) {
        String devId = target.getDevId();
        String sql = "SELECT * FROM m_dev_fw_version WHERE dev_id = :devId";

        @SuppressWarnings("unchecked")
        List<MDevFwVersion> result = em.createNativeQuery(sql, MDevFwVersion.class)
                                       .setParameter("devId", devId)
                                       .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void persist(MDevFwVersion target, EntityManager em) {
        em.persist(target);
        return ;
    }

    @Override
    public MDevFwVersion merge(MDevFwVersion target, EntityManager em) {
        return em.merge(em.contains(target) ? target : em.merge(target));
    }

    @Override
    public void remove(MDevFwVersion target, EntityManager em) {
        em.remove(em.contains(target) ? target : em.merge(target));
        return ;

    }

}
