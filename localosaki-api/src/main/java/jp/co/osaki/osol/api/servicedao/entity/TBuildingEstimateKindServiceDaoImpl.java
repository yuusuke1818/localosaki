package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.TBuildingEstimateKind;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author h-shiba
 */
public class TBuildingEstimateKindServiceDaoImpl implements BaseServiceDao<TBuildingEstimateKind> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEstimateKind> getResultList(TBuildingEstimateKind target, EntityManager em) {
        TypedQuery<TBuildingEstimateKind> query = em.createNamedQuery("TBuildingEstimateKind.findBuildingId", TBuildingEstimateKind.class);
        query.setParameter("corpId", target.getId().getCorpId());
        query.setParameter("buildingId", target.getId().getBuildingId());
        List<TBuildingEstimateKind> list = query.getResultList();
        return list;
    }

    @Override
    public List<TBuildingEstimateKind> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEstimateKind> getResultList(List<TBuildingEstimateKind> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEstimateKind> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildingEstimateKind find(TBuildingEstimateKind target, EntityManager em) {
        TBuildingEstimateKind reseltObject = em.find(TBuildingEstimateKind.class, target.getId());
        return reseltObject;
    }

    @Override
    public void persist(TBuildingEstimateKind target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TBuildingEstimateKind merge(TBuildingEstimateKind target, EntityManager em) {
        TBuildingEstimateKind reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(TBuildingEstimateKind target, EntityManager em) {
//        TBuildingEstimateKind entity = em.find(TBuildingEstimateKind.class, target.getId());
////        if (entity.getVersion().equals(target.getVersion())) {
//        em.remove(entity);
////               }
        em.remove(target);
    }

}
