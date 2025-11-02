package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK_;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class MBuildingSmPointServiceDaoImpl implements BaseServiceDao<MBuildingSmPoint> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSmPoint> getResultList(MBuildingSmPoint t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        Long smId = t.getId().getSmId();
        String pointNo = t.getId().getPointNo();
        String pointType = null;
        if (t.getMSmPoint() != null) {
            pointType = t.getMSmPoint().getPointType();
        }
        Integer pointSumFlg = t.getPointSumFlg();
        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MBuildingSmPoint> query = builder.createQuery(MBuildingSmPoint.class);
        Root<MBuildingSmPoint> root = query.from(MBuildingSmPoint.class);
        // 条件
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.corpId), corpId));
        if (buildingId != null) {
            whereList.add(builder.equal(
                    root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.buildingId), buildingId));
        }
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.smId), smId));
        }
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(
                    root.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.pointNo), pointNo));
        }
        if (!CheckUtility.isNullOrEmpty(pointType)) {
            whereList.add(builder.equal(
                    root.get(MBuildingSmPoint_.MSmPoint).get(MSmPoint_.pointType), pointType));
        }
        if (pointSumFlg != null) {
            whereList.add(builder.equal(
                    root.get(MBuildingSmPoint_.pointSumFlg), pointSumFlg));
        }
        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MBuildingSmPoint> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSmPoint> getResultList(List<MBuildingSmPoint> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSmPoint> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MBuildingSmPoint find(MBuildingSmPoint t, EntityManager em) {
        return em.find(MBuildingSmPoint.class, t.getId());
    }

    @Override
    public void persist(MBuildingSmPoint t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MBuildingSmPoint merge(MBuildingSmPoint t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MBuildingSmPoint t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
