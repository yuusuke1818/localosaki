package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MManualInsp;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針設定 ServiceDAOクラス
 * @author sagi_h
 *
 */
public class MManualInspServiceDaoImpl implements BaseServiceDao<MManualInsp> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MManualInsp> getResultList(MManualInsp target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MManualInsp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MManualInsp> getResultList(List<MManualInsp> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MManualInsp> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MManualInsp find(MManualInsp target, EntityManager em) {
        return em.find(MManualInsp.class, target.getId());
    }

    @Override
    public void persist(MManualInsp target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MManualInsp merge(MManualInsp target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(MManualInsp target, EntityManager em) {
        em.remove(target);
    }

}
