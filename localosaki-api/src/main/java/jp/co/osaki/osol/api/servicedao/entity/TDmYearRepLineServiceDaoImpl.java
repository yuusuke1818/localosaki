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

import jp.co.osaki.osol.entity.TDmYearRepLine;
import jp.co.osaki.osol.entity.TDmYearRepLinePK_;
import jp.co.osaki.osol.entity.TDmYearRepLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報系統
 *
 * @author t_hirata
 */
public class TDmYearRepLineServiceDaoImpl implements BaseServiceDao<TDmYearRepLine> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepLine> getResultList(TDmYearRepLine t, EntityManager em) {

        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        String yearNo = t.getId().getYearNo();
        BigDecimal monthNo = t.getId().getMonthNo();
        String summaryUnit = t.getId().getSummaryUnit();
        Long lineGroupId = t.getId().getLineGroupId();
        String lineNo = t.getId().getLineNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmYearRepLine> query = builder.createQuery(TDmYearRepLine.class);

        Root<TDmYearRepLine> root = query.from(TDmYearRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.buildingId), buildingId));
        }
        //年度
        if (!CheckUtility.isNullOrEmpty(yearNo)) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNo));
        }
        //月No
        if (monthNo != null) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNo));
        }
        //集計単位
        if (!CheckUtility.isNullOrEmpty(summaryUnit)) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.summaryUnit), summaryUnit));
        }
        //系統グループID
        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineGroupId), lineGroupId));
        }
        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineNo), lineNo));
        }

        query = query.select(root)
                     .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TDmYearRepLine> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepLine> getResultList(List<TDmYearRepLine> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmYearRepLine> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmYearRepLine find(TDmYearRepLine t, EntityManager em) {
        return em.find(TDmYearRepLine.class, t.getId());
    }

    @Override
    public void persist(TDmYearRepLine t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TDmYearRepLine merge(TDmYearRepLine t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TDmYearRepLine t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
