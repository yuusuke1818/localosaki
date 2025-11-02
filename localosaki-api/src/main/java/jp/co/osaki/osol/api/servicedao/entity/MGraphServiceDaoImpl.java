package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MGraph;
import jp.co.osaki.osol.entity.MGraphPK_;
import jp.co.osaki.osol.entity.MGraph_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class MGraphServiceDaoImpl implements BaseServiceDao<MGraph> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraph> getResultList(MGraph t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MGraph> query = builder.createQuery(MGraph.class);

        Root<MGraph> root = query.from(MGraph.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        if (t.getId() != null && !CheckUtility.isNullOrEmpty(t.getId().getCorpId())) {
            whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.corpId), t.getId().getCorpId()));
        }
        //建物ID
        if (t.getId() != null && t.getId().getBuildingId() != null) {
            whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.buildingId), t.getId().getBuildingId()));
        }
        //系統グループID
        if (t.getId() != null && t.getId().getLineGroupId() != null) {
            whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.lineGroupId), t.getId().getLineGroupId()));
        }
        //グラフID
        if (t.getId() != null && t.getId().getGraphId() != null) {
            whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.graphId), t.getId().getGraphId()));
        }
        //削除フラグ
        if (t.getDelFlg() != null) {
            whereList.add(builder.equal(root.get(MGraph_.delFlg), t.getDelFlg()));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MGraph> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraph> getResultList(List<MGraph> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MGraph> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MGraph find(MGraph t, EntityManager em) {
        return em.find(MGraph.class, t.getId());
    }

    @Override
    public void persist(MGraph t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MGraph merge(MGraph t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MGraph t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
