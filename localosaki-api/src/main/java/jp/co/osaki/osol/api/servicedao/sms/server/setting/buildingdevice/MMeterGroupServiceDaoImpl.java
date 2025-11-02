/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK_;
import jp.co.osaki.osol.entity.MMeterGroup_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ設定テーブル ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class MMeterGroupServiceDaoImpl implements BaseServiceDao<MMeterGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<MMeterGroup> delete = builder.createCriteriaDelete(MMeterGroup.class);

        Root<MMeterGroup> root = delete.from(MMeterGroup.class);
        List<Predicate> whereList = new ArrayList<>();

        whereList.add(root.get(MMeterGroup_.id).get(MMeterGroupPK_.corpId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.CORP_ID.getVal())));

        whereList.add(root.get(MMeterGroup_.id).get(MMeterGroupPK_.buildingId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.BUILDING_ID.getVal())));

        whereList.add(root.get(MMeterGroup_.id).get(MMeterGroupPK_.devId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.DEV_ID.getVal())));

        delete.where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(delete).executeUpdate();
    }

    @Override
    public List<MMeterGroup> getResultList(MMeterGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterGroup> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterGroup> getResultList(List<MMeterGroup> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeterGroup> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeterGroup find(MMeterGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MMeterGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeterGroup merge(MMeterGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MMeterGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
