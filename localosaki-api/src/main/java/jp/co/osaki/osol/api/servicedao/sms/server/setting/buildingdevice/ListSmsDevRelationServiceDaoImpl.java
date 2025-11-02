/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsDevRelationDetailResultData;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物、装置関連テーブル ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class ListSmsDevRelationServiceDaoImpl implements BaseServiceDao<ListSmsDevRelationDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDevRelationDetailResultData> getResultList(ListSmsDevRelationDetailResultData t,
            EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsDevRelationDetailResultData> query = builder
                .createQuery(ListSmsDevRelationDetailResultData.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        List<Predicate> whereList = new ArrayList<>();

        if (Objects.nonNull(t.getCorpId())) {
            whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.corpId), t.getCorpId()));
        }

        if (Objects.nonNull(t.getBuildingId())) {
            whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId),
                    t.getBuildingId()));
        }

        if (Objects.nonNull(t.getDevId())) {
            whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), t.getDevId()));
        }

        query = query.select(builder.construct(ListSmsDevRelationDetailResultData.class,
                root.get(MDevRelation_.id).get(MDevRelationPK_.corpId),
                root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId),
                root.get(MDevRelation_.id).get(MDevRelationPK_.devId)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsDevRelationDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDevRelationDetailResultData> getResultList(List<ListSmsDevRelationDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDevRelationDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDevRelationDetailResultData find(ListSmsDevRelationDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsDevRelationDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDevRelationDetailResultData merge(ListSmsDevRelationDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsDevRelationDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
