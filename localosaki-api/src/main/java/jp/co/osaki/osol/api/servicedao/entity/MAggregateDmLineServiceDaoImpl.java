package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MAggregateDmLine;
import jp.co.osaki.osol.entity.MAggregateDmLinePK_;
import jp.co.osaki.osol.entity.MAggregateDmLine_;
import jp.co.osaki.osol.entity.MAggregateDmPK_;
import jp.co.osaki.osol.entity.MAggregateDm_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 集計デマンド系統 EntityServiceDaoクラス
 * @author ya-ishida
 *
 */
public class MAggregateDmLineServiceDaoImpl implements BaseServiceDao<MAggregateDmLine> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDmLine> getResultList(MAggregateDmLine target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAggregateDmLine> query = builder.createQuery(MAggregateDmLine.class);

        Root<MAggregateDmLine> root = query.from(MAggregateDmLine.class);

        List<Predicate> whereList = new ArrayList<>();

        if (target.getId() != null) {
            //企業ID
            if (!CheckUtility.isNullOrEmpty(target.getId().getCorpId())) {
                whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.corpId),
                        target.getId().getCorpId()));
            }
            //建物ID
            if (target.getId().getBuildingId() != null) {
                whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.buildingId),
                        target.getId().getBuildingId()));
            }
            //系統グループID
            if (target.getId().getLineGroupId() != null) {
                whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineGroupId),
                        target.getId().getLineGroupId()));
            }
            //系統番号
            if (target.getId().getLineNo() != null) {
                whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineNo),
                        target.getId().getLineNo()));
            }
        }

        if (target.getMAggregateDm() != null && target.getMAggregateDm().getId() != null) {
            //集計デマンド企業ID
            if (!CheckUtility.isNullOrEmpty(target.getMAggregateDm().getId().getCorpId())) {
                whereList.add(builder.equal(
                        root.get(MAggregateDmLine_.MAggregateDm).get(MAggregateDm_.id).get(MAggregateDmPK_.corpId),
                        target.getMAggregateDm().getId().getCorpId()));
            }

            //集計デマンド建物ID
            if (target.getMAggregateDm().getId().getBuildingId() != null) {
                whereList.add(builder.equal(
                        root.get(MAggregateDmLine_.MAggregateDm).get(MAggregateDm_.id).get(MAggregateDmPK_.buildingId),
                        target.getMAggregateDm().getId().getBuildingId()));
            }

            //集計デマンドID
            if (target.getMAggregateDm().getId().getAggregateDmId() != null) {
                whereList.add(builder.equal(
                        root.get(MAggregateDmLine_.MAggregateDm).get(MAggregateDm_.id)
                                .get(MAggregateDmPK_.aggregateDmId),
                        target.getMAggregateDm().getId().getAggregateDmId()));
            }
        }

        if (whereList.isEmpty()) {
            query = query.select(root);
        } else {
            query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MAggregateDmLine> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDmLine> getResultList(List<MAggregateDmLine> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAggregateDmLine> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MAggregateDmLine find(MAggregateDmLine target, EntityManager em) {
        return em.find(MAggregateDmLine.class, target.getId());
    }

    @Override
    public void persist(MAggregateDmLine target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MAggregateDmLine merge(MAggregateDmLine target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MAggregateDmLine target, EntityManager em) {
        em.remove(target);
    }

}
