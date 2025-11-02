package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.MEstimateKind;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author h-shiba
 */
public class MEstimateKindServiceDaoImpl implements BaseServiceDao<MEstimateKind> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MEstimateKind> getResultList(MEstimateKind target, EntityManager em) {
        TypedQuery<MEstimateKind> query = em.createNamedQuery("MEstimateKind.findValidAll", MEstimateKind.class);
        query.setParameter("corpId", target.getId().getCorpId());
        List<MEstimateKind> mEstimateKindList = query.getResultList();
        return mEstimateKindList;
    }

    @Override
    public List<MEstimateKind> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MEstimateKind> getResultList(List<MEstimateKind> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MEstimateKind> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MEstimateKind find(MEstimateKind target, EntityManager em) {
        if ("-1".equals(target.getId().getEstimateId().toString())) {
            return null;
        }

        TypedQuery<MEstimateKind> query = em.createNamedQuery("MEstimateKind.findByEstimateId", MEstimateKind.class);
        query.setParameter("estimateId", target.getId().getEstimateId());
        MEstimateKind result = query.getSingleResult();
        return result;
    }

    @Override
    public void persist(MEstimateKind target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MEstimateKind merge(MEstimateKind target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MEstimateKind target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
