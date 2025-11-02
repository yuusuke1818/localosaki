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

import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、装置関連テーブル ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class MDevRelationServiceDaoImpl implements BaseServiceDao<MDevRelation> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<MDevRelation> delete = builder.createCriteriaDelete(MDevRelation.class);

        Root<MDevRelation> root = delete.from(MDevRelation.class);
        List<Predicate> whereList = new ArrayList<>();

        whereList.add(root.get(MDevRelation_.id).get(MDevRelationPK_.corpId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.CORP_ID.getVal())));

        whereList.add(root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.BUILDING_ID.getVal())));

        whereList.add(root.get(MDevRelation_.id).get(MDevRelationPK_.devId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.DEV_ID.getVal())));

        delete.where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(delete).executeUpdate();
    }

    @Override
    public List<MDevRelation> getResultList(MDevRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevRelation> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevRelation> getResultList(List<MDevRelation> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevRelation> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevRelation find(MDevRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MDevRelation t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MDevRelation merge(MDevRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MDevRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
