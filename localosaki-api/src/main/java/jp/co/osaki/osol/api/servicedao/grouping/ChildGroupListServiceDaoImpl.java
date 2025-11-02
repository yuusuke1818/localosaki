/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.grouping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.grouping.ChildGroupListDetailResultData;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroupPK_;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 子グループ一覧取得処理 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class ChildGroupListServiceDaoImpl implements BaseServiceDao<ChildGroupListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ChildGroupListDetailResultData> getResultList(ChildGroupListDetailResultData t, EntityManager em) {

        String corpId = t.getCorpId();
        Long parentGroupId = t.getParentGroupId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ChildGroupListDetailResultData> query = builder.createQuery(ChildGroupListDetailResultData.class);

        Root<MChildGroup> root = query.from(MChildGroup.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MChildGroup_.id).get(MChildGroupPK_.corpId), corpId));
        //親グループID
        if (parentGroupId != null) {
            whereList.add(builder.equal(root.get(MChildGroup_.id).get(MChildGroupPK_.parentGroupId), parentGroupId));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(ChildGroupListDetailResultData.class,
                root.get(MChildGroup_.id).get(MChildGroupPK_.corpId),
                root.get(MChildGroup_.id).get(MChildGroupPK_.parentGroupId),
                root.get(MChildGroup_.id).get(MChildGroupPK_.childGroupId),
                root.get(MChildGroup_.childGroupName),
                root.get(MChildGroup_.displayOrder),
                root.get(MChildGroup_.delFlg),
                root.get(MChildGroup_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MChildGroup_.displayOrder)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ChildGroupListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ChildGroupListDetailResultData> getResultList(List<ChildGroupListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ChildGroupListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChildGroupListDetailResultData find(ChildGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ChildGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChildGroupListDetailResultData merge(ChildGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ChildGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
