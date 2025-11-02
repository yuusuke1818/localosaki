package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.TAvailableEnergyLine;
import jp.co.osaki.osol.entity.TAvailableEnergyLinePK_;
import jp.co.osaki.osol.entity.TAvailableEnergyLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー系統
 *
 * @author t_hirata
 */
public class TAvailableEnergyLineServiceDaoImpl implements BaseServiceDao<TAvailableEnergyLine> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergyLine> getResultList(TAvailableEnergyLine t, EntityManager em) {
        // パラメータ
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        String engTypeCd = t.getId().getEngTypeCd();
        Long engId = t.getId().getEngId();
        Long contractId = t.getId().getContractId();
        String lineCorpId = t.getMLine().getId().getCorpId();
        Long lineGroupId = t.getMLine().getId().getLineGroupId();
        String lineNo = t.getMLine().getId().getLineNo();

        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TAvailableEnergyLine> query = builder.createQuery(TAvailableEnergyLine.class);

        Root<TAvailableEnergyLine> root = query.from(TAvailableEnergyLine.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        if(!CheckUtility.isNullOrEmpty(corpId)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.corpId), corpId));
        }

        if(buildingId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.buildingId), buildingId));
        }

        if(!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engTypeCd), engTypeCd));
        }

        if(engId != null ) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engId), engId));
        }

        if (contractId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.contractId), contractId));
        }

        if (!CheckUtility.isNullOrEmpty(lineCorpId)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.corpId), lineCorpId));
        }

        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.lineGroupId), lineGroupId));
        }

        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.lineNo), lineNo));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TAvailableEnergyLine> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergyLine> getResultList(List<TAvailableEnergyLine> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergyLine> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TAvailableEnergyLine find(TAvailableEnergyLine t, EntityManager em) {
        return em.find(TAvailableEnergyLine.class, t.getId());
    }

    @Override
    public void persist(TAvailableEnergyLine t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TAvailableEnergyLine merge(TAvailableEnergyLine t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TAvailableEnergyLine t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
