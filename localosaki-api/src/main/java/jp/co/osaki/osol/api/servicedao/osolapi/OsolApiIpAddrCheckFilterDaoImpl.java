package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.MLoginIpAddr;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class OsolApiIpAddrCheckFilterDaoImpl implements BaseServiceDao<MLoginIpAddr> {

    @Override
    public List<MLoginIpAddr> getResultList(MLoginIpAddr target, EntityManager em) {
        TypedQuery<MLoginIpAddr> query;
        if (target.getIpAddress() != null) {
            query = em.createNamedQuery("MLoginIpAddr.findLoginIpAddrCheckFilter", MLoginIpAddr.class);
            query.setParameter("corpId", target.getId().getCorpId());
            query.setParameter("ipAddress", target.getIpAddress());
            query.setParameter("loginPermitStatus", target.getLoginPermitStatus());
            query.setParameter("loginPermitTarget", target.getLoginPermitTarget());
        } else if(target.getIpAddress() == null && target.getLoginPermitStatus() != null){
            // IP指定なしで許可ステータスの指定がある場合
            query = em.createNamedQuery("MLoginIpAddr.findLoginIpAddrCheckFilterStatus", MLoginIpAddr.class);
            query.setParameter("corpId", target.getId().getCorpId());
            query.setParameter("loginPermitStatus", target.getLoginPermitStatus());
            query.setParameter("loginPermitTarget", target.getLoginPermitTarget());
        } else {
            query = em.createNamedQuery("MLoginIpAddr.findLoginIpAddrCheckFilterCorp", MLoginIpAddr.class);
            query.setParameter("corpId", target.getId().getCorpId());
            query.setParameter("loginPermitTarget", target.getLoginPermitTarget());
        }
        // 検索実行
        List<MLoginIpAddr> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLoginIpAddr> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLoginIpAddr> getResultList(List<MLoginIpAddr> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLoginIpAddr> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLoginIpAddr find(MLoginIpAddr target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MLoginIpAddr target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MLoginIpAddr merge(MLoginIpAddr target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MLoginIpAddr target, EntityManager em) {
        em.remove(target);
    }

}
