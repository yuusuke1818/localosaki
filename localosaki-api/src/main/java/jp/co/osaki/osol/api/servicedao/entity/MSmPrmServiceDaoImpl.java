package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MSmPrm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器テーブル EntityResultSetクラス
 * @author ya-ishida
 *
 */
public class MSmPrmServiceDaoImpl implements BaseServiceDao<MSmPrm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MSmPrm> getResultList(MSmPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MSmPrm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MSmPrm> getResultList(List<MSmPrm> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MSmPrm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MSmPrm find(MSmPrm target, EntityManager em) {
        return em.find(MSmPrm.class, target.getSmId());
    }

    @Override
    public void persist(MSmPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MSmPrm merge(MSmPrm target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MSmPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
