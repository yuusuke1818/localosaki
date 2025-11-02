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
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyPK_;
import jp.co.osaki.osol.entity.TAvailableEnergy_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー
 *
 * @author t_hirata
 */
public class TAvailableEnergyServiceDaoImpl implements BaseServiceDao<TAvailableEnergy> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergy> getResultList(TAvailableEnergy t, EntityManager em) {
        // パラメータ
        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        String engTypeCd = t.getId().getEngTypeCd();
        Long engId = t.getId().getEngId();
        Long contractId = t.getId().getContractId();

        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TAvailableEnergy> query = builder.createQuery(TAvailableEnergy.class);

        Root<TAvailableEnergy> root = query.from(TAvailableEnergy.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.corpId), corpId));
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.buildingId), buildingId));
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engTypeCd), engTypeCd));
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engId), engId));
        if (contractId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.contractId), contractId));
        }
        whereList.add(builder.equal(root.get(TAvailableEnergy_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(TAvailableEnergy_.energyStartYm)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TAvailableEnergy> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergy> getResultList(List<TAvailableEnergy> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAvailableEnergy> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TAvailableEnergy find(TAvailableEnergy t, EntityManager em) {
        return em.find(TAvailableEnergy.class, t.getId());
    }

    @Override
    public void persist(TAvailableEnergy t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TAvailableEnergy merge(TAvailableEnergy t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TAvailableEnergy t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
