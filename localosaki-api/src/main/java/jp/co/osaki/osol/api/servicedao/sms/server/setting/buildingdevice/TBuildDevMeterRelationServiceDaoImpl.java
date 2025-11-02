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

import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、メーター関連テーブル ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class TBuildDevMeterRelationServiceDaoImpl implements BaseServiceDao<TBuildDevMeterRelation> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<TBuildDevMeterRelation> delete = builder.createCriteriaDelete(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = delete.from(TBuildDevMeterRelation.class);
        List<Predicate> whereList = new ArrayList<>();

        whereList.add(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId)
                .in(map.get(SmsConstants.RERATION_KEY_NAME.DEV_ID.getVal())));

        delete.where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(delete).executeUpdate();
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(TBuildDevMeterRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(List<TBuildDevMeterRelation> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildDevMeterRelation> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildDevMeterRelation find(TBuildDevMeterRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TBuildDevMeterRelation t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TBuildDevMeterRelation merge(TBuildDevMeterRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TBuildDevMeterRelation t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
