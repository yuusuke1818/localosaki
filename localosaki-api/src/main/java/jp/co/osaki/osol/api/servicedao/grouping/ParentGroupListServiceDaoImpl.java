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
import jp.co.osaki.osol.api.resultdata.grouping.ParentGroupListDetailResultData;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroupPK_;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 親グループ一覧取得処理 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class ParentGroupListServiceDaoImpl implements BaseServiceDao<ParentGroupListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ParentGroupListDetailResultData> getResultList(ParentGroupListDetailResultData t, EntityManager em) {

        String corpId = t.getCorpId();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ParentGroupListDetailResultData> query = builder
                .createQuery(ParentGroupListDetailResultData.class);

        Root<MParentGroup> root = query.from(MParentGroup.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MParentGroup_.id).get(MParentGroupPK_.corpId), corpId));
        //削除フラグ
        whereList.add(builder.equal(root.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(ParentGroupListDetailResultData.class,
                root.get(MParentGroup_.id).get(MParentGroupPK_.corpId),
                root.get(MParentGroup_.id).get(MParentGroupPK_.parentGroupId),
                root.get(MParentGroup_.parentGroupName),
                root.get(MParentGroup_.overlapFlg),
                root.get(MParentGroup_.displayOrder),
                root.get(MParentGroup_.delFlg),
                root.get(MParentGroup_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MParentGroup_.displayOrder)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ParentGroupListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ParentGroupListDetailResultData> getResultList(List<ParentGroupListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ParentGroupListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParentGroupListDetailResultData find(ParentGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ParentGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParentGroupListDetailResultData merge(ParentGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ParentGroupListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
