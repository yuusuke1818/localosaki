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
import jp.co.osaki.osol.entity.MCorpDm;
import jp.co.osaki.osol.entity.MCorpDm_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineGroup;
import jp.co.osaki.osol.entity.MLineGroupPK_;
import jp.co.osaki.osol.entity.MLineGroup_;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDmDayRep;
import jp.co.osaki.osol.entity.TDmDayRepLine;
import jp.co.osaki.osol.entity.TDmDayRepLinePK_;
import jp.co.osaki.osol.entity.TDmDayRepLine_;
import jp.co.osaki.osol.entity.TDmDayRep_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 *集計・分析 EMS実績 系統別計測値CSV出力情報取得  ServiceDaoクラス
 *
 * @author yonezawa.a
 *
 */

public class AnalysisEmsRecordCsvServiceDaoImpl implements BaseServiceDao<AnalysisEmsRecordCsvResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsRecordCsvResultData> getResultList(AnalysisEmsRecordCsvResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        Long lineGroupId = target.getLineGroupId();
        String lineNo = target.getLineNo();
        Date measurementDateFrom = target.getMeasurementDateFrom();
        Date measurementDateTo = target.getMeasurementDateTo();
        String selectedLineId = target.getSelectedLineId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsRecordCsvResultData> query = builder.createQuery(AnalysisEmsRecordCsvResultData.class);

        Root<MCorp> root = query.from(MCorp.class);
        Join<MCorp,TBuilding> joinBuilding = root.join(MCorp_.TBuildings);
        Join<MCorp,MCorpDm> joinCorpDm = root.join(MCorp_.MCorpDm);
        Join<MCorpDm,MLineGroup> joinLineGroup = joinCorpDm.join(MCorpDm_.MLineGroups);
        Join<MLineGroup,MLine> joinLine = joinLineGroup.join(MLineGroup_.MLines);
        Join<TBuilding,MBuildingDm> joinBuildingDm = joinBuilding.join(TBuilding_.MBuildingDms);
        Join<MBuildingDm,TDmDayRep> joinDmDayRep = joinBuildingDm.join(MBuildingDm_.TDmDayReps);
        Join<TDmDayRep,TDmDayRepLine> joinDmDayRepLine = joinDmDayRep.join(TDmDayRep_.TDmDayRepLines);


        List<Predicate> whereList = new ArrayList<>();

        //系統.系統グループID = デマンド日報系統.系統グループID
        whereList.add(builder.equal(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId), joinLine.get(MLine_.id).get(MLinePK_.lineGroupId)));

        //系統.系統番号 = デマンド日報系統.系統番号
        whereList.add(builder.equal(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo), joinLine.get(MLine_.id).get(MLinePK_.lineNo)));

        //系統.系統有効フラグ = ON
        whereList.add(builder.equal(joinLine.get(MLine_.lineEnableFlg), OsolConstants.FLG_ON));

        //企業ID
        whereList.add(builder.equal(root.get(MCorp_.corpId), corpId));

        //建物ID
        whereList.add(builder.equal(joinBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        //系統グループID
        whereList.add(builder.equal(joinLineGroup.get(MLineGroup_.id).get(MLineGroupPK_.lineGroupId), lineGroupId));

        if (target.isExtendsPlusMinus12hEnabled()) {
            // 前日の12:30～0:00、当日、翌日の0:30～11:30
            whereList
                    .add(builder.or(
                            builder.and(
                                    builder.equal(
                                            joinDmDayRepLine.get(TDmDayRepLine_.id)
                                                    .get(TDmDayRepLinePK_.measurementDate),
                                            DateUtility.plusDay(measurementDateFrom, -1)),
                                    builder.between(
                                            joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                                            new BigDecimal(25), new BigDecimal(48))),
                            builder.equal(
                                    joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                                    measurementDateFrom),
                            builder.and(
                                    builder.equal(
                                            joinDmDayRepLine.get(TDmDayRepLine_.id)
                                                    .get(TDmDayRepLinePK_.measurementDate),
                                            DateUtility.plusDay(measurementDateFrom, 1)),
                                    builder.between(
                                            joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                                            new BigDecimal(1), new BigDecimal(24)))));
        } else {
            //計測年月日From
            whereList.add(builder.greaterThanOrEqualTo(
                    joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                    measurementDateFrom));

            //計測年月日To
            whereList.add(builder.lessThanOrEqualTo(
                    joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateTo));
        }

        // 系統番号
        whereList.add(builder.equal(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo), lineNo));

        //建物削除フラグ
        whereList.add(builder.equal(joinBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        //建物デマンド削除フラグ
        whereList.add(builder.equal(joinBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));

        //系統グループ削除フラグ
        whereList.add(builder.equal(joinLineGroup.get(MLineGroup_.delFlg), OsolConstants.FLG_OFF));

        //系統削除フラグ
        whereList.add(builder.equal(joinLine.get(MLine_.delFlg), OsolConstants.FLG_OFF));

        if (target.isExtendsPlusMinus12hEnabled()) {
            if (selectedLineId.equals("0")) {
                // 使用量の場合
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .orderBy(builder.asc(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)),
                                builder.asc(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo)));;
            } else {
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        //                    joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                        builder.sum(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.max(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.min(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.count(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .groupBy(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                                //                            joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate))
                        .orderBy(
                                builder.asc(
                                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)),
                                builder.asc(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo)));
            }
        } else {
            if (selectedLineId.equals("0")) {
                // 使用量の場合
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)))
                        .where(builder.and(whereList.toArray(new Predicate[] {})));
            } else {
                query = query.select(builder.construct(AnalysisEmsRecordCsvResultData.class,
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                        builder.sum(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.max(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.min(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                        builder.count(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw))))
                        .where(builder.and(whereList.toArray(new Predicate[] {})))
                        .groupBy(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo));
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
