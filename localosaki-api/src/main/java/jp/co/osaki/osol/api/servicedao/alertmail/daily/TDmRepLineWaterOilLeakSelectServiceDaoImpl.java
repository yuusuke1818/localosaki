package jp.co.osaki.osol.api.servicedao.alertmail.daily;

import java.math.BigDecimal;
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
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepLineWaterOilLeakCheckListResultData;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK_;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.TDmDayRep;
import jp.co.osaki.osol.entity.TDmDayRepLine;
import jp.co.osaki.osol.entity.TDmDayRepLinePK_;
import jp.co.osaki.osol.entity.TDmDayRepLine_;
import jp.co.osaki.osol.entity.TDmDayRepPK_;
import jp.co.osaki.osol.entity.TDmDayRep_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 水・油漏れチェック ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class TDmRepLineWaterOilLeakSelectServiceDaoImpl
        implements BaseServiceDao<TDmRepLineWaterOilLeakCheckListResultData> {

    @Override
    public List<TDmRepLineWaterOilLeakCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * チェック対象となる水・油漏れチェックデータを取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return 建物・テナントリスト
     */
    @Override
    public List<TDmRepLineWaterOilLeakCheckListResultData> getResultList(
            TDmRepLineWaterOilLeakCheckListResultData target, EntityManager em) {

        List<TDmRepLineWaterOilLeakCheckListResultData> resultList = new ArrayList<TDmRepLineWaterOilLeakCheckListResultData>();

        if (target == null) {
            return new ArrayList<TDmRepLineWaterOilLeakCheckListResultData>();
        }

        Date nowDate = target.getNowDate();
        Date targetDate = target.getTargetDate();
        BigDecimal chkLineValueKw = target.getChkLineValueKw();
        BigDecimal chkJigenNoFrom = target.getChkJigenNoFrom();
        BigDecimal chkJigenNoTo = target.getChkJigenNoTo();
        List<String> chkLineTypeList = new ArrayList<>();
        String[] targetLineType = target.getChkLineType().split(",");

        for (String lineType : targetLineType) {
            chkLineTypeList.add(lineType);
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmRepLineWaterOilLeakCheckListResultData> query = builder
                .createQuery(TDmRepLineWaterOilLeakCheckListResultData.class);

        // 取得元テーブル
        Root<MBuildingSm> rootMBuildingSm = query.from(MBuildingSm.class);

        // 結合テーブル
        Join<MBuildingSm, MBuildingDm> joinMBuildingDm = rootMBuildingSm.join(MBuildingSm_.MBuildingDm, JoinType.INNER);
        Join<MBuildingDm, TDmDayRep> joinTDmDayRep = joinMBuildingDm.join(MBuildingDm_.TDmDayReps, JoinType.INNER);
        Join<TDmDayRep, TDmDayRepLine> joinTDmDayRepLine = joinTDmDayRep.join(TDmDayRep_.TDmDayRepLines,
                JoinType.INNER);
        Join<TDmDayRepLine, MLine> joinMLine = joinTDmDayRepLine.join(TDmDayRepLine_.MLine, JoinType.INNER);
        Join<MLine, MLineType> joinMLineType = joinMLine.join(MLine_.MLineType, JoinType.INNER);

        // 結合条件
        joinMBuildingDm.on(
                builder.equal(rootMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.corpId)),
                builder.equal(rootMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId), joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId)));

        joinTDmDayRep.on(
                builder.equal(joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.corpId), joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId)),
                builder.equal(joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId), joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId)));

        joinTDmDayRepLine.on(
                builder.equal(joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId), joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId)),
                builder.equal(joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId), joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId)),
                builder.equal(joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)),
                builder.equal(joinTDmDayRep.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo)));

        joinMLine.on(
                builder.equal(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId), joinMLine.get(MLine_.id).get(MLinePK_.corpId)),
                builder.equal(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId), joinMLine.get(MLine_.id).get(MLinePK_.lineGroupId)),
                builder.equal(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo), joinMLine.get(MLine_.id).get(MLinePK_.lineNo)));

        joinMLineType.on(builder.equal(joinMLine.get(MLine_.MLineType), joinMLineType.get(MLineType_.lineType)));

        // 条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(builder.equal(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                target.getCorpId()));
        conditionList.add(builder.equal(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                target.getBuildingId()));
        conditionList
                .add(builder.equal(rootMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.smId), target.getSmId()));
        conditionList.add(builder.and(builder.greaterThanOrEqualTo(
                joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), targetDate)));
        conditionList.add(builder.and(builder.lessThanOrEqualTo(
                joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), nowDate)));
        conditionList.add(builder.notEqual(joinTDmDayRepLine.get(TDmDayRepLine_.lineValueKw), chkLineValueKw));
        conditionList.add(builder.between(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                chkJigenNoFrom, chkJigenNoTo));
        conditionList.add(joinMLineType.get(MLineType_.lineType).in(chkLineTypeList));
        conditionList.add(builder.equal(joinMBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));
        conditionList.add(builder.equal(joinMLine.get(MLine_.delFlg), OsolConstants.FLG_OFF));
        conditionList.add(builder.equal(joinMLineType.get(MLineType_.delFlg), OsolConstants.FLG_OFF));

        // SQL発行
        query = query
                .select(builder.construct(TDmRepLineWaterOilLeakCheckListResultData.class,
                        joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                        joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinMLineType.get(MLineType_.lineTypeName),
                        joinMLine.get(MLine_.lineName),
                        builder.count(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate))))
                .where(builder.and(conditionList.toArray(new Predicate[] {})))
                .groupBy(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                        joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                        joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                        joinMLine.get(MLine_.lineName),
                        joinMLineType.get(MLineType_.lineTypeName))
                .orderBy(builder.asc(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId)),
                        builder.asc(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo)),
                        builder.desc(joinTDmDayRepLine.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate)));

        // SQL実行
        List<TDmRepLineWaterOilLeakCheckListResultData> dataList = em.createQuery(query).getResultList();

        if (dataList == null || dataList.size() == 0) {

        } else {

            // 取得できた場合、過去一週間のいずれかの値が不正
            // 取得したリスト分ループ
            for (int i = 0; i < dataList.size(); i++) {

                int addDayCnt = 0;

                // 同一系統グループID、系統番号判別フラグ
                boolean groupingFlg = false;

                TDmRepLineWaterOilLeakCheckListResultData targetData = new TDmRepLineWaterOilLeakCheckListResultData();
                List<Long> waterOilLeakCntList = new ArrayList<>();

                // 現在日付から逆算していき、過去一週間分を格納
                for (int j = 0; j < 7; j++) {
                    if ((i == 0 && j == 0) || (j == 0 && resultList.size() > 0
                            && (!dataList.get(i).getLineGroupId()
                                    .equals(resultList.get(resultList.size() - 1).getLineGroupId())
                                    || (dataList.get(i).getLineGroupId()
                                            .equals(resultList.get(resultList.size() - 1).getLineGroupId())
                                            && !dataList.get(i).getLineNo()
                                                    .equals(resultList.get(resultList.size() - 1).getLineNo()))))) {
                        targetData.setCorpName(target.getCorpName());
                        targetData.setCorpId(target.getCorpId());
                        targetData.setBuildingNo(target.getBuildingNo());
                        targetData.setBuildingName(target.getBuildingName());
                        targetData.setSmId(target.getSmId());
                        targetData.setSmAddress(target.getSmAddress());
                        targetData.setIpAddress(target.getIpAddress());
                        targetData.setLineGroupId(dataList.get(i).getLineGroupId());
                        targetData.setLineNo(dataList.get(i).getLineNo());
                        targetData.setLineTypeName(dataList.get(i).getLineTypeName());
                        targetData.setLineName(dataList.get(i).getLineName());
                    }

                    if (j == 0 && resultList.size() > 0
                            && dataList.get(i).getLineGroupId()
                                    .equals(resultList.get(resultList.size() - 1).getLineGroupId())
                            && dataList.get(i).getLineNo().equals(resultList.get(resultList.size() - 1).getLineNo())) {
                        groupingFlg = true;
                    }

                    if (groupingFlg) {
                        // 取得したデータの計測年月日を判定
                        if (!DateUtility.changeDateFormat(dataList.get(i).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                            // 格納済の水・油漏れ件数をセット
                            waterOilLeakCntList.add(resultList.get(resultList.size() - 1).getWaterOilLeakCntList(j));
                        } else {
                            // 前回の計測年月日チェック済で取得結果に同日の計測年月日が存在し、
                            // 水・油漏れ件数が多い場合はリストを詰めなおす
                            if (dataList.get(i).getWaterOilLeakCnt() > resultList.get(resultList.size() - 1)
                                    .getWaterOilLeakCntList(j)) {
                                waterOilLeakCntList.add(dataList.get(i).getWaterOilLeakCnt());
                            } else {
                                waterOilLeakCntList
                                        .add(resultList.get(resultList.size() - 1).getWaterOilLeakCntList(j));
                            }
                        }
                    } else {
                        // 取得したデータの計測年月日を判定
                        if (!DateUtility.changeDateFormat(dataList.get(i).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                            // 存在しない場合は0をセット
                            waterOilLeakCntList.add(0L);

                            if (addDayCnt == 0) {
                                // 計測年月日の先頭（前日）がエラーでない場合は
                                // 以降でエラーがあった場合でもエラーなしとする
                                targetData.setNonErrFlg(true);
                            }
                        } else {
                            waterOilLeakCntList.add(dataList.get(i).getWaterOilLeakCnt());
                        }
                    }

                    addDayCnt--;
                }

                if (groupingFlg) {
                    // 同一の系統を一行で出力するためにリストを上書き
                    resultList.get(resultList.size() - 1).setWaterOilLeakCntList(waterOilLeakCntList);
                } else {
                    targetData.setChkJigenNoFrom(chkJigenNoFrom);
                    targetData.setChkJigenNoTo(chkJigenNoTo);
                    targetData.setWaterOilLeakCntList(waterOilLeakCntList);
                    resultList.add(targetData);
                }
            }
        }

        // 取得結果を返却
        return resultList;

    }

    @Override
    public void persist(TDmRepLineWaterOilLeakCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(TDmRepLineWaterOilLeakCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepLineWaterOilLeakCheckListResultData> getResultList(
            List<TDmRepLineWaterOilLeakCheckListResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepLineWaterOilLeakCheckListResultData find(TDmRepLineWaterOilLeakCheckListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepLineWaterOilLeakCheckListResultData merge(TDmRepLineWaterOilLeakCheckListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepLineWaterOilLeakCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
