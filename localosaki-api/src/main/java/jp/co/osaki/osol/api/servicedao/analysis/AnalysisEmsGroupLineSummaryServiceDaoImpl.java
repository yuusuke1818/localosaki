package jp.co.osaki.osol.api.servicedao.analysis;

import java.util.ArrayList;
import java.util.Date;
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
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsGroupLineSummaryResultData;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroup_;
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
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDmDayRep;
import jp.co.osaki.osol.entity.TDmDayRepLine;
import jp.co.osaki.osol.entity.TDmDayRepLinePK_;
import jp.co.osaki.osol.entity.TDmDayRepLine_;
import jp.co.osaki.osol.entity.TDmDayRep_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 *集計・分析 EMS実績 系統別サマリー取得(グループ指定) ServiceDaoクラス
 *
 * @author y-maruta
 *
 */

public class AnalysisEmsGroupLineSummaryServiceDaoImpl implements BaseServiceDao<AnalysisEmsGroupLineSummaryResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsGroupLineSummaryResultData> getResultList(AnalysisEmsGroupLineSummaryResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long parentGroupId = target.getParentGroupId();
        Long childGroupId = target.getChildGroupId();
        Long lineGroupId = target.getLineGroupId();
        Date measurementDateFrom = target.getMeasurementDateFrom();
        Date measurementDateTo = target.getMeasurementDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsGroupLineSummaryResultData> query = builder.createQuery(AnalysisEmsGroupLineSummaryResultData.class);

        Root<MCorp> root = query.from(MCorp.class);
        Join<MCorp,TBuilding> joinBuilding = root.join(MCorp_.TBuildings);
        Join<MCorp,MCorpDm> joinCorpDm = root.join(MCorp_.MCorpDm);
        Join<MCorpDm,MLineGroup> joinLineGroup = joinCorpDm.join(MCorpDm_.MLineGroups);
        Join<MLineGroup,MLine> joinLine = joinLineGroup.join(MLineGroup_.MLines);
        Join<TBuilding,TBuildingGroup> joinBuildingGroup = joinBuilding.join(TBuilding_.TBuildingGroups,JoinType.LEFT);
        Join<TBuildingGroup,MChildGroup> joinChildGroup = joinBuildingGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
        Join<MChildGroup,MParentGroup> joinParentGroup = joinChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);
        Join<TBuilding,MBuildingDm> joinBuildingDm = joinBuilding.join(TBuilding_.MBuildingDms);
        Join<MBuildingDm,TDmDayRep> joinDmDayRep = joinBuildingDm.join(MBuildingDm_.TDmDayReps);
        Join<TDmDayRep,TDmDayRepLine> joinDmDayRepLine = joinDmDayRep.join(TDmDayRep_.TDmDayRepLines);

        //建物グループ削除フラグ
        joinBuildingGroup.on(builder.equal(joinBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        //子グループ削除フラグ
        joinChildGroup.on(builder.equal(joinChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        //親グループ削除フラグ
        joinParentGroup.on(builder.equal(joinParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> whereList = new ArrayList<>();

        //系統.系統グループID = デマンド日報系統.系統グループID
        whereList.add(builder.equal(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId), joinLine.get(MLine_.id).get(MLinePK_.lineGroupId)));

        //系統.系統番号 = デマンド日報系統.系統番号
        whereList.add(builder.equal(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo), joinLine.get(MLine_.id).get(MLinePK_.lineNo)));

        //系統.系統有効フラグ = ON
        whereList.add(builder.equal(joinLine.get(MLine_.lineEnableFlg), OsolConstants.FLG_ON));

        //企業ID
        whereList.add(builder.equal(root.get(MCorp_.corpId), corpId));

        //親グループ
        whereList.add(builder.equal(joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId), parentGroupId));

      //子グループ指定が有る場合
        if(childGroupId != null) {
            whereList.add(builder.equal(joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId), childGroupId));
        }

        //系統グループID
        whereList.add(builder.equal(joinLineGroup.get(MLineGroup_.id).get(MLineGroupPK_.lineGroupId), lineGroupId));

        //計測年月日From
        whereList.add(builder.greaterThanOrEqualTo(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateFrom));

        //計測年月日To
        whereList.add(builder.lessThanOrEqualTo(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateTo));

        //建物削除フラグ
        whereList.add(builder.equal(joinBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        //建物デマンド削除フラグ
        whereList.add(builder.equal(joinBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));

        //系統グループ削除フラグ
        whereList.add(builder.equal(joinLineGroup.get(MLineGroup_.delFlg), OsolConstants.FLG_OFF));

        //系統削除フラグ
        whereList.add(builder.equal(joinLine.get(MLine_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AnalysisEmsGroupLineSummaryResultData.class,
                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId),
                joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                builder.sum(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                builder.max(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                builder.min(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw)),
                builder.count(joinDmDayRepLine.get(TDmDayRepLine_.lineValueKw))
                )).
                where(builder.and(whereList.toArray(new Predicate[]{})))
                .groupBy(joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId),
                        joinBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsGroupLineSummaryResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsGroupLineSummaryResultData> getResultList(List<AnalysisEmsGroupLineSummaryResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsGroupLineSummaryResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsGroupLineSummaryResultData find(AnalysisEmsGroupLineSummaryResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisEmsGroupLineSummaryResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public AnalysisEmsGroupLineSummaryResultData merge(AnalysisEmsGroupLineSummaryResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisEmsGroupLineSummaryResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }



}
