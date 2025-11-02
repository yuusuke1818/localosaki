/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.input.EnergyUseListDetailResultData;
import jp.co.osaki.osol.entity.TEnergyUse;
import jp.co.osaki.osol.entity.TEnergyUsePK_;
import jp.co.osaki.osol.entity.TEnergyUse_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー実績取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class EnergyUseListServiceDaoImpl implements BaseServiceDao<EnergyUseListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseListDetailResultData> getResultList(EnergyUseListDetailResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String engTypeCd = t.getEngTypeCd();
        Long engId = t.getEngId();
        Long contractId = t.getContractId();
        String calYmFrom = t.getCalYmFrom();
        String calYmTo = t.getCalYmTo();
        String dayAndNightType = t.getDayAndNightType();
        String engSupplyType = t.getEngSupplyType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<EnergyUseListDetailResultData> query = builder.createQuery(EnergyUseListDetailResultData.class);

        Root<TEnergyUse> root = query.from(TEnergyUse.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.buildingId), buildingId));
        //エネルギー種類コード
        if (!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.engTypeCd), engTypeCd));
        }
        //エネルギーID
        if (engId != null) {
            whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.engId), engId));
        }
        //契約ID
        if (contractId != null) {
            whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.contractId), contractId));
        }
        //昼夜区分
        if (!CheckUtility.isNullOrEmpty(dayAndNightType)) {
            whereList.add(builder.equal(root.get(TEnergyUse_.dayAndNightType), dayAndNightType));
        }
        //供給区分
        if (!CheckUtility.isNullOrEmpty(engSupplyType)) {
            whereList.add(builder.equal(root.get(TEnergyUse_.engSupplyType), engSupplyType));
        }

        //カレンダ年月
        if (!CheckUtility.isNullOrEmpty(calYmFrom) && !CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TEnergyUse_.id).get(TEnergyUsePK_.ym), calYmFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TEnergyUse_.id).get(TEnergyUsePK_.ym), calYmTo));
        } else if (!CheckUtility.isNullOrEmpty(calYmFrom) && CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(builder.equal(root.get(TEnergyUse_.id).get(TEnergyUsePK_.ym), calYmFrom));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(TEnergyUse_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(EnergyUseListDetailResultData.class,
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.corpId),
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.buildingId),
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.engTypeCd),
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.engId),
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.contractId),
                root.get(TEnergyUse_.id).get(TEnergyUsePK_.ym),
                root.get(TEnergyUse_.customerNo),
                root.get(TEnergyUse_.supplyPointSpecificNo),
                root.get(TEnergyUse_.dayAndNightType),
                root.get(TEnergyUse_.engSupplyType),
                root.get(TEnergyUse_.usePlace),
                root.get(TEnergyUse_.contractType),
                root.get(TEnergyUse_.contractPower),
                root.get(TEnergyUse_.contractPowerUnit),
                root.get(TEnergyUse_.contractBiko),
                root.get(TEnergyUse_.billedAmount),
                root.get(TEnergyUse_.firstEngValue),
                root.get(TEnergyUse_.firstEngCoefficient),
                root.get(TEnergyUse_.firstEngUnit),
                root.get(TEnergyUse_.co2Value),
                root.get(TEnergyUse_.co2Coefficient),
                root.get(TEnergyUse_.co2Unit),
                root.get(TEnergyUse_.meterImageFileName),
                root.get(TEnergyUse_.meterImagePath),
                root.get(TEnergyUse_.meterImageFileSize),
                root.get(TEnergyUse_.changeAuthFlg),
                root.get(TEnergyUse_.maxPower),
                root.get(TEnergyUse_.powerCoefficient),
                root.get(TEnergyUse_.useDays),
                root.get(TEnergyUse_.haneiType),
                root.get(TEnergyUse_.meterCheckDate),
                root.get(TEnergyUse_.useValue1),
                root.get(TEnergyUse_.useUnit1),
                root.get(TEnergyUse_.useValue2),
                root.get(TEnergyUse_.useUnit2),
                root.get(TEnergyUse_.fuelAdjustmentCost),
                root.get(TEnergyUse_.rEnergyGpLevy),
                root.get(TEnergyUse_.paymentDate),
                root.get(TEnergyUse_.delFlg),
                root.get(TEnergyUse_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<EnergyUseListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseListDetailResultData> getResultList(List<EnergyUseListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<EnergyUseListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyUseListDetailResultData find(EnergyUseListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(EnergyUseListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnergyUseListDetailResultData merge(EnergyUseListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(EnergyUseListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
