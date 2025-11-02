/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.master;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.master.UiScreenListDetailResultData;
import jp.co.osaki.osol.entity.MUiScreen;
import jp.co.osaki.osol.entity.MUiScreen_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class UiScreenListServiceDaoImpl implements BaseServiceDao<UiScreenListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UiScreenListDetailResultData> getResultList(UiScreenListDetailResultData t, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UiScreenListDetailResultData> query = cb.createQuery(UiScreenListDetailResultData.class);
        Root<MUiScreen> root = query.from(MUiScreen.class);

        query = query.select(cb.construct(UiScreenListDetailResultData.class,
                root.get(MUiScreen_.uiScreenId),
                root.get(MUiScreen_.uiScreenName),
                root.get(MUiScreen_.uiScreenBean),
                root.get(MUiScreen_.displayOrder),
                root.get(MUiScreen_.version),
                root.get(MUiScreen_.createUserId),
                root.get(MUiScreen_.createDate),
                root.get(MUiScreen_.updateUserId),
                root.get(MUiScreen_.updateDate)))
                .orderBy(cb.asc(root.get(MUiScreen_.displayOrder)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<UiScreenListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UiScreenListDetailResultData> getResultList(List<UiScreenListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UiScreenListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public UiScreenListDetailResultData find(UiScreenListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(UiScreenListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UiScreenListDetailResultData merge(UiScreenListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(UiScreenListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
