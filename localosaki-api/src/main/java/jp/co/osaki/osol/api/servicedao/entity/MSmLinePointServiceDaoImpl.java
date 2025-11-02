package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK_;
import jp.co.osaki.osol.entity.MSmLinePoint_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class MSmLinePointServiceDaoImpl implements BaseServiceDao<MSmLinePoint> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLinePoint> getResultList(MSmLinePoint t, EntityManager em) {

        // パラメータ
        String corpId = t.getId().getCorpId();
        Long lineGroupId = t.getId().getLineGroupId();
        Long buildingId = t.getId().getBuildingId();
        Long smId = t.getId().getSmId();

        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmLinePoint> query = builder.createQuery(MSmLinePoint.class);

        Root<MSmLinePoint> root = query.from(MSmLinePoint.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), corpId));
        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId), lineGroupId));
        }
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), buildingId));
        }
        if (smId != null) {
            whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), smId));
        }
        whereList.add(builder.equal(root.get(MSmLinePoint_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo)),
                         builder.asc(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MSmLinePoint> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLinePoint> getResultList(List<MSmLinePoint> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmLinePoint> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmLinePoint find(MSmLinePoint t, EntityManager em) {
        return em.find(MSmLinePoint.class, t.getId());
    }

    @Override
    public void persist(MSmLinePoint t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MSmLinePoint merge(MSmLinePoint t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MSmLinePoint t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
