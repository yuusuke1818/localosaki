/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.building.setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyDetailDataListResultData;
import jp.co.osaki.osol.entity.MEnergy;
import jp.co.osaki.osol.entity.MEnergyType;
import jp.co.osaki.osol.entity.MEnergyType_;
import jp.co.osaki.osol.entity.MEnergy_;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyPK_;
import jp.co.osaki.osol.entity.TAvailableEnergy_;
import jp.co.osaki.osol.entity.TBuildingEnergyType;
import jp.co.osaki.osol.entity.TBuildingEnergyType_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー詳細情報取得 ServiceDaoクラス
 *
 * @author y-maruta
 */
public class AvailableEnergyDetailDataListServiceDaoImpl implements BaseServiceDao<AvailableEnergyDetailDataListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyDetailDataListResultData> getResultList(AvailableEnergyDetailDataListResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String engTypeCd = t.getEngTypeCd();
        Long engId = t.getEngId();
        Long contractId = t.getContractId();
        Date energyYmFrom = t.getEnergyYmFrom();
        Date energyYmTo = t.getEnergyYmTo();
        Date energyYmPoint = t.getEnergyYmPoint();
        String dayAndNightType = t.getDayAndNightType();
        String engSupplyType = t.getEngSupplyType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AvailableEnergyDetailDataListResultData> query = builder.createQuery(AvailableEnergyDetailDataListResultData.class);

        Root<TAvailableEnergy> root = query.from(TAvailableEnergy.class);
        Join<TAvailableEnergy, MEnergy> joinMEnergy = root.join(TAvailableEnergy_.MEnergy);
        Join<MEnergy , MEnergyType> joinMEnergyType = joinMEnergy.join(MEnergy_.MEnergyType);
        Join<TAvailableEnergy,TBuildingEnergyType> joinTBuildingEnergyType = root.join(TAvailableEnergy_.TBuildingEnergyType);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.buildingId), buildingId));
        //エネルギー種類コード
        if (!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engTypeCd), engTypeCd));
        }
        //エネルギーID
        if (engId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engId), engId));
        }
        //契約ID
        if (contractId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.contractId), contractId));
        }
        //昼夜区分
        if (!CheckUtility.isNullOrEmpty(dayAndNightType)) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.dayAndNightType), dayAndNightType));
        }
        //供給区分
        if (!CheckUtility.isNullOrEmpty(engSupplyType)) {
            whereList.add(builder.equal(root.get(TAvailableEnergy_.engSupplyType), engSupplyType));
        }

        //エネルギー利用年月From/エネルギー利用年月To
        if (energyYmFrom != null && energyYmTo != null) {
            whereList.add(builder.lessThanOrEqualTo(root.get(TAvailableEnergy_.energyStartYm), energyYmFrom));
            whereList.add(builder.or(builder.isNull(root.get(TAvailableEnergy_.energyEndYm)), builder.greaterThanOrEqualTo(root.get(TAvailableEnergy_.energyEndYm), energyYmTo)));
        } else if (energyYmFrom != null && energyYmTo == null) {
            whereList.add(builder.lessThanOrEqualTo(root.get(TAvailableEnergy_.energyStartYm), energyYmFrom));
        }

        //エネルギー利用基準年月
        if (energyYmPoint != null) {
            whereList.add(builder.lessThanOrEqualTo(root.get(TAvailableEnergy_.energyStartYm), energyYmPoint));
            whereList.add(builder.or(builder.isNull(root.get(TAvailableEnergy_.energyEndYm)), builder.greaterThanOrEqualTo(root.get(TAvailableEnergy_.energyEndYm), energyYmPoint)));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(TAvailableEnergy_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AvailableEnergyDetailDataListResultData.class,
                root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.corpId),
                root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.buildingId),
                root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engTypeCd),
                root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.engId),
                root.get(TAvailableEnergy_.id).get(TAvailableEnergyPK_.contractId),
                root.get(TAvailableEnergy_.energyStartYm),
                root.get(TAvailableEnergy_.energyEndYm),
                root.get(TAvailableEnergy_.customerNo),
                root.get(TAvailableEnergy_.supplyPointSpecificNo),
                root.get(TAvailableEnergy_.dayAndNightType),
                root.get(TAvailableEnergy_.engSupplyType),
                root.get(TAvailableEnergy_.usePlace),
                root.get(TAvailableEnergy_.contractType),
                root.get(TAvailableEnergy_.contractPower),
                root.get(TAvailableEnergy_.contractPowerUnit),
                root.get(TAvailableEnergy_.contractBiko),
                root.get(TAvailableEnergy_.inputFlg),
                root.get(TAvailableEnergy_.displayOrder),
                root.get(TAvailableEnergy_.delFlg),
                root.get(TAvailableEnergy_.energyUseLineValueFlg),
                root.get(TAvailableEnergy_.version),
                joinMEnergy.get(MEnergy_.engName),
                joinMEnergy.get(MEnergy_.supplyCompany),
                joinMEnergy.get(MEnergy_.supplyArea),
                joinMEnergy.get(MEnergy_.unit),
                joinMEnergy.get(MEnergy_.supplyCompanyTelNo),
                joinMEnergy.get(MEnergy_.supplyCompanyUrl),
                joinMEnergy.get(MEnergy_.supplyCompanyBiko),
                joinMEnergy.get(MEnergy_.displayOrder),
                joinMEnergyType.get(MEnergyType_.engTypeName),
                joinMEnergyType.get(MEnergyType_.distributionSuikeiOutFlg),
                joinMEnergyType.get(MEnergyType_.displayOrder),
                joinTBuildingEnergyType.get(TBuildingEnergyType_.displayOrder),
                joinTBuildingEnergyType.get(TBuildingEnergyType_.version)
                )).

                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AvailableEnergyDetailDataListResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyDetailDataListResultData> getResultList(List<AvailableEnergyDetailDataListResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyDetailDataListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AvailableEnergyDetailDataListResultData find(AvailableEnergyDetailDataListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AvailableEnergyDetailDataListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AvailableEnergyDetailDataListResultData merge(AvailableEnergyDetailDataListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AvailableEnergyDetailDataListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
