package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MAggregateDm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 集計デマンド EntityServiceDaoクラス
 * @author ya-ishida
 *
 */
public class MAggregateDmServiceDaoImpl implements BaseServiceDao<MAggregateDm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDm> getResultList(MAggregateDm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDm> getResultList(List<MAggregateDm> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MAggregateDm find(MAggregateDm target, EntityManager em) {
        return em.find(MAggregateDm.class, target.getId());
    }

    @Override
    public void persist(MAggregateDm target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MAggregateDm merge(MAggregateDm target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MAggregateDm target, EntityManager em) {
        em.remove(target);
    }

}
