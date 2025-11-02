/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MProductSpec;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 製品仕様EntityServiceDaoクラス（データ選択専用）
 *
 * @author ya-ishida
 */
public class MProductSpecServiceDaoImpl implements BaseServiceDao<MProductSpec> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MProductSpec> getResultList(MProductSpec t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MProductSpec> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MProductSpec> getResultList(List<MProductSpec> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MProductSpec> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MProductSpec find(MProductSpec t, EntityManager em) {
        return em.find(MProductSpec.class, t.getProductCd());
    }

    @Override
    public void persist(MProductSpec t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MProductSpec merge(MProductSpec t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MProductSpec t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
