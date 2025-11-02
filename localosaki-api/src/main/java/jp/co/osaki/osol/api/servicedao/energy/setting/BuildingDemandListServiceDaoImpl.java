/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.entity.MAmedasObservatory;
import jp.co.osaki.osol.entity.MAmedasObservatory_;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK_;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物デマンド情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class BuildingDemandListServiceDaoImpl implements BaseServiceDao<BuildingDemandListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDemandListDetailResultData> getResultList(BuildingDemandListDetailResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildingDemandListDetailResultData> query = builder.createQuery(BuildingDemandListDetailResultData.class);

        Root<MBuildingDm> root = query.from(MBuildingDm.class);
        Join<MBuildingDm, MAmedasObservatory> joinAmedas = root.join(MBuildingDm_.MAmedasObservatory, JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MBuildingDm_.id).get(MBuildingDmPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId), buildingId));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));
        whereList.add(builder.or(builder.equal(joinAmedas.get(MAmedasObservatory_.delFlg), OsolConstants.FLG_OFF), builder.isNull(joinAmedas.get(MAmedasObservatory_.delFlg))));

        query = query.select(builder.construct(BuildingDemandListDetailResultData.class,
                root.get(MBuildingDm_.id).get(MBuildingDmPK_.corpId),
                root.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId),
                root.get(MBuildingDm_.sumDate),
                root.get(MBuildingDm_.weekClosingDayOfWeek),
                root.get(MBuildingDm_.weekStartDay),
                root.get(MBuildingDm_.targetKw),
                root.get(MBuildingDm_.workStartTime),
                root.get(MBuildingDm_.shopOpenTime),
                root.get(MBuildingDm_.shopCloseTime),
                root.get(MBuildingDm_.workEndTime),
                root.get(MBuildingDm_.targetKwhMonth1),
                root.get(MBuildingDm_.targetKwhMonth2),
                root.get(MBuildingDm_.targetKwhMonth3),
                root.get(MBuildingDm_.targetKwhMonth4),
                root.get(MBuildingDm_.targetKwhMonth5),
                root.get(MBuildingDm_.targetKwhMonth6),
                root.get(MBuildingDm_.targetKwhMonth7),
                root.get(MBuildingDm_.targetKwhMonth8),
                root.get(MBuildingDm_.targetKwhMonth9),
                root.get(MBuildingDm_.targetKwhMonth10),
                root.get(MBuildingDm_.targetKwhMonth11),
                root.get(MBuildingDm_.targetKwhMonth12),
                root.get(MBuildingDm_.contractKw),
                root.get(MBuildingDm_.co2EngTypeCd),
                root.get(MBuildingDm_.co2EngId),
                root.get(MBuildingDm_.co2DayAndNightType),
                root.get(MBuildingDm_.facilitiesMailAddress1),
                root.get(MBuildingDm_.facilitiesMailAddress2),
                root.get(MBuildingDm_.facilitiesMailAddress3),
                root.get(MBuildingDm_.facilitiesMailAddress4),
                root.get(MBuildingDm_.facilitiesMailAddress5),
                joinAmedas.get(MAmedasObservatory_.amedasObservatoryNo),
                joinAmedas.get(MAmedasObservatory_.amedasObservatoryName),
                root.get(MBuildingDm_.outAirTempDispFlg),
                root.get(MBuildingDm_.coopTargetAlarmFlg),
                root.get(MBuildingDm_.standardTargetContrastRate),
                root.get(MBuildingDm_.excellentTargetContrastRate),
                root.get(MBuildingDm_.dayCommodityChargeUnitPrice),
                root.get(MBuildingDm_.nightCommodityChargeUnitPrice),
                root.get(MBuildingDm_.delFlg),
                root.get(MBuildingDm_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<BuildingDemandListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDemandListDetailResultData> getResultList(List<BuildingDemandListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingDemandListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingDemandListDetailResultData find(BuildingDemandListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingDemandListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingDemandListDetailResultData merge(BuildingDemandListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(BuildingDemandListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
