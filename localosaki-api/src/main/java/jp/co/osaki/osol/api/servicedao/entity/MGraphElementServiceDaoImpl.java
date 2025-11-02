package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK_;
import jp.co.osaki.osol.entity.MGraphElement_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * グラフ要素設定　EntityServiceDaoクラス
 * @author t_hirata
 */
public class MGraphElementServiceDaoImpl implements BaseServiceDao<MGraphElement> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraphElement> getResultList(MGraphElement t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        Long lineGroupId = t.getId().getLineGroupId();
        Long graphId = t.getId().getGraphId();
        Long graphElementId = t.getId().getGraphElementId();
        String graphElementType = t.getGraphElementType();
        Long graphLineGroupId = t.getGraphLineGroupId();
        String graphLineNo = t.getGraphLineNo();
        Long smId = t.getGraphSmId();
        String pointNo = t.getGraphPointNo();
        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MGraphElement> query = builder.createQuery(MGraphElement.class);
        Root<MGraphElement> root = query.from(MGraphElement.class);
        // 条件
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MGraphElement_.id).get(MGraphElementPK_.corpId), corpId));
        if (buildingId != null) {
            whereList.add(builder.equal(
                    root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId), buildingId));
        }
        if (lineGroupId != null) {
            whereList.add(builder.equal(
                    root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId), lineGroupId));
        }
        if (graphId != null) {
            whereList.add(builder.equal(
                    root.get(MGraphElement_.id).get(MGraphElementPK_.graphId), graphId));
        }
        if (graphElementId != null) {
            whereList.add(builder.equal(
                    root.get(MGraphElement_.id).get(MGraphElementPK_.graphElementId), graphElementId));
        }
        if (!CheckUtility.isNullOrEmpty(graphElementType)) {
            whereList.add(builder.equal(root.get(MGraphElement_.graphElementType), graphElementType));
        }
        if (graphLineGroupId != null) {
            whereList.add(builder.equal(root.get(MGraphElement_.graphLineGroupId), graphLineGroupId));
        }
        if (!CheckUtility.isNullOrEmpty(graphLineNo)) {
            whereList.add(builder.equal(root.get(MGraphElement_.graphLineNo), graphLineNo));
        }
        if (smId != null) {
            whereList.add(builder.equal(root.get(MGraphElement_.graphSmId), smId));
        }
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(root.get(MGraphElement_.graphPointNo), pointNo));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MGraphElement_.delFlg), OsolConstants.FLG_OFF));
        // 表示順
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.asc(root.get(MGraphElement_.displayOrder)));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MGraphElement> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraphElement> getResultList(List<MGraphElement> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraphElement> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MGraphElement find(MGraphElement t, EntityManager em) {
        return em.find(MGraphElement.class, t.getId());
    }

    @Override
    public void persist(MGraphElement t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MGraphElement merge(MGraphElement t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MGraphElement t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
