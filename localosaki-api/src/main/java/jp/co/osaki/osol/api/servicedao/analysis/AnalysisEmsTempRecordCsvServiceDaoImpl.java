package jp.co.osaki.osol.api.servicedao.analysis;

import java.math.BigDecimal;
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
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsRecordCsvResultData;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDmDayRep;
import jp.co.osaki.osol.entity.TDmDayRepPK_;
import jp.co.osaki.osol.entity.TDmDayRep_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
*
* 集計・分析 EMS実績 系統別計測値CSV出力情報取得  外気温用 ServiceDaoクラス
*
*/
public class AnalysisEmsTempRecordCsvServiceDaoImpl implements BaseServiceDao<AnalysisEmsRecordCsvResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsRecordCsvResultData> getResultList(AnalysisEmsRecordCsvResultData target, EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        Date measurementDateFrom = target.getMeasurementDateFrom();
        Date measurementDateTo = target.getMeasurementDateTo();
        String selectedLineId = target.getSelectedLineId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsRecordCsvResultData> query = builder.createQuery(AnalysisEmsRecordCsvResultData.class);

        Root<MCorp> root = query.from(MCorp.class);
        Join<MCorp,TBuilding> joinBuilding = root.join(MCorp_.TBuildings);
        Join<TBuilding,MBuildingDm> joinBuildingDm = joinBuilding.join(TBuilding_.MBuildingDms);
        Join<MBuildingDm,TDmDayRep> joinDmDayRep = joinBuildingDm.join(MBuildingDm_.TDmDayReps);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MCorp_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        if (target.isExtendsPlusMinus12hEnabled()) {
            // 前日の12:30～0:00、当日、翌日の0:30～11:30
            whereList
                    .add(builder.or(
                            builder.and(
                                    builder.equal(
                                            joinDmDayRep.get(TDmDayRep_.id)
                                                    .get(TDmDayRepPK_.measurementDate),
                                            DateUtility.plusDay(measurementDateFrom, -1)),
                                    builder.between(
                                            joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                                            new BigDecimal(25), new BigDecimal(48))),
                            builder.equal(
                                    joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate),
                                    measurementDateFrom),
                            builder.and(
                                    builder.equal(
                                            joinDmDayRep.get(TDmDayRep_.id)
                                                    .get(TDmDayRepPK_.measurementDate),
                                            DateUtility.plusDay(measurementDateFrom, 1)),
                                    builder.between(
                                            joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                                            new BigDecimal(1), new BigDecimal(24)))));
        } else {
            //計測年月日From
            whereList.add(builder.greaterThanOrEqualTo(
                    joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate),
                    measurementDateFrom));

            //計測年月日To
            whereList.add(builder.lessThanOrEqualTo(
                    joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateTo));
        }

        //建物削除フラグ
        whereList.add(builder.equal(joinBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        //建物デマンド削除フラグ
        whereList.add(builder.equal(joinBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));

        if (target.isExtendsPlusMinus12hEnabled()) {
            if (selectedLineId.equals("0")) {
                // 使用量の場合
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                        joinDmDayRep.get(TDmDayRep_.outAirTemp),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .orderBy(builder.asc(joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate)),
                                builder.asc(joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo)));
            } else {
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                        builder.sum(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.max(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.min(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.count(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .groupBy(joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                                joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                                joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                                joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate))
                        .orderBy(
                                builder.asc(
                                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate)),
                                builder.asc(joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo)));
            }
        } else {
            if (selectedLineId.equals("0")) {
                // 使用量の場合
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                        joinDmDayRep.get(TDmDayRep_.outAirTemp)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})));
            } else {
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                        joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                        builder.sum(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.max(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.min(joinDmDayRep.get(TDmDayRep_.outAirTemp)),
                        builder.count(joinDmDayRep.get(TDmDayRep_.outAirTemp))))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .groupBy(joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                                joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                                joinDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo));
            }
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsRecordCsvResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsRecordCsvResultData> getResultList(List<AnalysisEmsRecordCsvResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsRecordCsvResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsRecordCsvResultData find(AnalysisEmsRecordCsvResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisEmsRecordCsvResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsRecordCsvResultData merge(AnalysisEmsRecordCsvResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisEmsRecordCsvResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
