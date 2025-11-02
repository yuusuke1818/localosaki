package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.MGenericType;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author take_suzuki
 */
public class MGenericTypeServiceDaoImpl implements BaseServiceDao<MGenericType> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGenericType> getResultList(MGenericType target, EntityManager em) {
        TypedQuery<MGenericType> query = em.createNamedQuery("MGenericType.findGroupCode", MGenericType.class);
        query.setParameter("groupCode", target.getId().getGroupCode());
        return query.getResultList();
    }

    @Override
    public List<MGenericType> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGenericType> getResultList(List<MGenericType> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGenericType> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MGenericType find(MGenericType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MGenericType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MGenericType merge(MGenericType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MGenericType target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
