package jp.co.osaki.osol.api.servicedao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDmYearRepPoint;
import jp.co.osaki.osol.entity.TDmYearRepPointPK_;
import jp.co.osaki.osol.entity.TDmYearRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報ポイント
 *
 * @author t_hirata
 */
public class TDmYearRepPointServiceDaoImpl implements BaseServiceDao<TDmYearRepPoint> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepPoint> getResultList(TDmYearRepPoint t, EntityManager em) {

        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        Long smId = t.getId().getSmId();
        String yearNo = t.getId().getYearNo();
        BigDecimal monthNo = t.getId().getMonthNo();
        String summaryUnit = t.getId().getSummaryUnit();
        String pointNo = t.getId().getPointNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmYearRepPoint> query = builder.createQuery(TDmYearRepPoint.class);

        Root<TDmYearRepPoint> root = query.from(TDmYearRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId), buildingId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId), smId));
        }
        //年度
        if (!CheckUtility.isNullOrEmpty(yearNo)) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNo));
        }
        //月No
        if (monthNo != null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNo));
        }
        //集計単位
        if (!CheckUtility.isNullOrEmpty(summaryUnit)) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.summaryUnit), summaryUnit));
        }
        //ポイント番号
        if (!CheckUtility.isNullOrEmpty(pointNo)) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), pointNo));
        }

        query = query.select(root)
                     .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TDmYearRepPoint> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepPoint> getResultList(List<TDmYearRepPoint> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepPoint> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmYearRepPoint find(TDmYearRepPoint t, EntityManager em) {
        return em.find(TDmYearRepPoint.class, t.getId());
    }

    @Override
    public void persist(TDmYearRepPoint t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TDmYearRepPoint merge(TDmYearRepPoint t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TDmYearRepPoint t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
