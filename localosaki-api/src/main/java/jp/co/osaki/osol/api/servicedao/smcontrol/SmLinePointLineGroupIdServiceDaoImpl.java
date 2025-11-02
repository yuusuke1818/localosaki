package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.smcontrol.SmLinePointResult;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK_;
import jp.co.osaki.osol.entity.MSmLinePoint_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器系統ポイントから特定条件時の系統グループID取得
 * ユニット設定のDB更新に使用
 *
 */
public class SmLinePointLineGroupIdServiceDaoImpl implements BaseServiceDao<SmLinePointResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    // 機器ID、企業ID、建物ID、ポイント番号が一致するレコードをGROUP BYで取得（不要なレコードは取得しない）
    @Override
    public List<SmLinePointResult> getResultList(SmLinePointResult target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmLinePointResult> query = builder.createQuery(SmLinePointResult.class);
        Root<MSmLinePoint> root = query.from(MSmLinePoint.class);

        List<Predicate> whereList = new ArrayList<>();

        // where 機器ID,企業ID,建物ID,ポイントNo
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), target.getSmId()));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), target.getCorpId()));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), target.getBuildingId()));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo), target.getPointNo()));

        query.select(builder.construct(SmLinePointResult.class,
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId)))
        .where(
                builder.and(whereList.toArray(new Predicate[] {})))
        .groupBy(
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId))
        .orderBy(
                builder.asc(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmLinePointResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<SmLinePointResult> getResultList(List<SmLinePointResult> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<SmLinePointResult> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public SmLinePointResult find(SmLinePointResult target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(SmLinePointResult target, EntityManager em) {

    }

    @Override
    public SmLinePointResult merge(SmLinePointResult target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(SmLinePointResult target, EntityManager em) {
    }

}
