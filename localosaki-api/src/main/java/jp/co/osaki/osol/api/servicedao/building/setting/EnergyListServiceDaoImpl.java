/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.building.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.building.setting.EnergyListResultData;
import jp.co.osaki.osol.entity.MEnergy;
import jp.co.osaki.osol.entity.MEnergyPK_;
import jp.co.osaki.osol.entity.MEnergy_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * エネルギー情報取得 ServiceDaoクラス
 *
 * @author y-maruta
 */
public class EnergyListServiceDaoImpl implements BaseServiceDao<EnergyListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyListResultData> getResultList(EnergyListResultData t, EntityManager em) {
        String engTypeCd = t.getEngTypeCd();
        Long engId = t.getEngId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<EnergyListResultData> query = builder.createQuery(EnergyListResultData.class);

        Root<MEnergy> root = query.from(MEnergy.class);

        List<Predicate> whereList = new ArrayList<>();

        //エネルギー種類コード
        if (!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(MEnergy_.id).get(MEnergyPK_.engTypeCd), engTypeCd));
        }
        //エネルギーID
        if (engId != null) {
            whereList.add(builder.equal(root.get(MEnergy_.id).get(MEnergyPK_.engId), engId));
        }

        query = query.select(builder.construct(EnergyListResultData.class,
                root.get(MEnergy_.id).get(MEnergyPK_.engTypeCd),
                root.get(MEnergy_.id).get(MEnergyPK_.engId),
                root.get(MEnergy_.engName),
                root.get(MEnergy_.supplyCompany),
                root.get(MEnergy_.supplyArea),
                root.get(MEnergy_.unit),
                root.get(MEnergy_.supplyCompanyTelNo),
                root.get(MEnergy_.supplyCompanyUrl),
                root.get(MEnergy_.supplyCompanyBiko),
                root.get(MEnergy_.displayOrder),
                root.get(MEnergy_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<EnergyListResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyListResultData> getResultList(List<EnergyListResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyListResultData find(EnergyListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(EnergyListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyListResultData merge(EnergyListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(EnergyListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
