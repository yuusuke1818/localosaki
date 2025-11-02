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

import jp.co.osaki.osol.entity.TDmYearRep;
import jp.co.osaki.osol.entity.TDmYearRepPK_;
import jp.co.osaki.osol.entity.TDmYearRep_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報
 *
 * @author t_hirata
 */
public class TDmYearRepServiceDaoImpl implements BaseServiceDao<TDmYearRep> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRep> getResultList(TDmYearRep t, EntityManager em) {
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        String yearNo = t.getId().getYearNo();
        BigDecimal monthNo = t.getId().getMonthNo();
        String summaryUnit = t.getId().getSummaryUnit();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmYearRep> query = builder.createQuery(TDmYearRep.class);

        Root<TDmYearRep> root = query.from(TDmYearRep.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.buildingId), buildingId));
        }
        //年度
        if (!CheckUtility.isNullOrEmpty(yearNo)) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNo));
        }
        //月No
        if (monthNo != null) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNo));
        }
        //集計単位
        if (!CheckUtility.isNullOrEmpty(summaryUnit)) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.summaryUnit), summaryUnit));
        }

        query = query.select(root)
                     .where(builder.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TDmYearRep> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRep> getResultList(List<TDmYearRep> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRep> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmYearRep find(TDmYearRep t, EntityManager em) {
        return em.find(TDmYearRep.class, t.getId());
    }

    @Override
    public void persist(TDmYearRep t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TDmYearRep merge(TDmYearRep t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TDmYearRep t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
