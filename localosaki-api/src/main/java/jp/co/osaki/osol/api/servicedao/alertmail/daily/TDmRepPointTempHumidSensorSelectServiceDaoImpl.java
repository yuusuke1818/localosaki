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
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepPointTempHumidSensorCheckListResultData;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK_;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPointPK_;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.entity.TDmDayRepPoint;
import jp.co.osaki.osol.entity.TDmDayRepPointPK_;
import jp.co.osaki.osol.entity.TDmDayRepPoint_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 温湿度センサーチェック ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class TDmRepPointTempHumidSensorSelectServiceDaoImpl
        implements BaseServiceDao<TDmRepPointTempHumidSensorCheckListResultData> {

    @Override
    public List<TDmRepPointTempHumidSensorCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * チェック対象となる温湿度センサーチェックデータを取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return 建物・テナントリスト
     */
    @Override
    public List<TDmRepPointTempHumidSensorCheckListResultData> getResultList(
            TDmRepPointTempHumidSensorCheckListResultData target, EntityManager em) {

        List<TDmRepPointTempHumidSensorCheckListResultData> resultList = new ArrayList<TDmRepPointTempHumidSensorCheckListResultData>();

        if (target == null) {
            return new ArrayList<TDmRepPointTempHumidSensorCheckListResultData>();
        }

        Date nowDate = target.getNowDate();
        Date targetDate = target.getTargetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmRepPointTempHumidSensorCheckListResultData> query = builder
                .createQuery(TDmRepPointTempHumidSensorCheckListResultData.class);

        // 取得元テーブル
        Root<MSmPoint> rootMSmPoint = query.from(MSmPoint.class);

        // 結合テーブル
        Join<MSmPoint, MBuildingSmPoint> joinMBuildingSmPoint = rootMSmPoint.join(MSmPoint_.MBuildingSmPoints,
                JoinType.LEFT);
        Join<MBuildingSmPoint, TDmDayRepPoint> joinTDmDayRepPoint = joinMBuildingSmPoint
                .join(MBuildingSmPoint_.TDmDayRepPoints, JoinType.LEFT);

        // 結合条件（ON句）
        joinMBuildingSmPoint.on(
                builder.equal(rootMSmPoint.get(MSmPoint_.id).get(MSmPointPK_.smId), joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.smId)),
                builder.equal(rootMSmPoint.get(MSmPoint_.id).get(MSmPointPK_.pointNo), joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get(MBuildingSmPointPK_.pointNo)),
                builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.delFlg), OsolConstants.FLG_OFF));

        joinTDmDayRepPoint.on(
                builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get((MBuildingSmPointPK_.corpId)), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId)),
                builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get((MBuildingSmPointPK_.buildingId)), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId)),
                builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get((MBuildingSmPointPK_.smId)), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId)),
                builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.id).get((MBuildingSmPointPK_.pointNo)), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo)));

        // 条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(builder.equal(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId),
                target.getCorpId()));
        conditionList.add(builder.equal(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId),
                target.getBuildingId()));
        conditionList.add(builder.equal(rootMSmPoint.get(MSmPoint_.id).get(MSmPointPK_.smId), target.getSmId()));
        conditionList.add(builder.and(builder.greaterThanOrEqualTo(
                joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), targetDate)));
        conditionList.add(builder.and(builder.lessThanOrEqualTo(
                joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), nowDate)));
        conditionList.add(builder.equal(rootMSmPoint.get(MSmPoint_.pointType), "A"));
        conditionList.add(builder.equal(joinMBuildingSmPoint.get(MBuildingSmPoint_.pointSumFlg), OsolConstants.FLG_ON));
        conditionList.add(
                builder.notEqual(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo), "SRC"));
        conditionList.add(builder.or(builder.isNull(joinTDmDayRepPoint.get(TDmDayRepPoint_.pointVal)),
                builder.greaterThanOrEqualTo(joinTDmDayRepPoint.get(TDmDayRepPoint_.pointVal), new BigDecimal("0"))));

        // SQL発行
        query = query
                .select(builder.construct(TDmRepPointTempHumidSensorCheckListResultData.class,
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo),
                        joinMBuildingSmPoint.get(MBuildingSmPoint_.pointName),
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.pointVal),
                        builder.count(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo))))
                .where(builder.and(conditionList.toArray(new Predicate[] {})))
                .groupBy(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo),
                        joinMBuildingSmPoint.get(MBuildingSmPoint_.pointName),
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.pointVal))
                // 閾値以上のデータが対象
                .having(builder.greaterThanOrEqualTo(
                        builder.count(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo)),
                        target.getThreshold()))
                .orderBy(builder.asc(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo)), builder
                        .desc(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate)));

        // SQL実行
        List<TDmRepPointTempHumidSensorCheckListResultData> dataList = em.createQuery(query).getResultList();

        if (dataList == null || dataList.size() == 0) {

        } else {

            // 取得できた場合、過去一週間のいずれかの値が不正
            // 取得したリスト分ループ
            for (int i = 0; i < dataList.size(); i++) {

                int addDayCnt = 0;

                // 同一ポイント番号判別フラグ
                boolean groupingFlg = false;

                TDmRepPointTempHumidSensorCheckListResultData targetData = new TDmRepPointTempHumidSensorCheckListResultData();
                List<Long> sameTempHumidCntList = new ArrayList<>();

                // 現在日付から逆算していき、過去一週間分を格納
                for (int j = 0; j < 7; j++) {
                    if ((i == 0 && j == 0) || (j == 0 && resultList.size() > 0 && !dataList.get(i).getPointNo()
                            .equals(resultList.get(resultList.size() - 1).getPointNo()))) {
                        targetData.setCorpName(target.getCorpName());
                        targetData.setCorpId(target.getCorpId());
                        targetData.setBuildingNo(target.getBuildingNo());
                        targetData.setBuildingName(target.getBuildingName());
                        targetData.setSmId(target.getSmId());
                        targetData.setSmAddress(target.getSmAddress());
                        targetData.setIpAddress(target.getIpAddress());
                        targetData.setPointNo(dataList.get(i).getPointNo());
                        targetData.setPointName(dataList.get(i).getPointName());
                    }

                    if (j == 0 && resultList.size() > 0
                            && dataList.get(i).getPointNo()
                                    .equals(resultList.get(resultList.size() - 1).getPointNo())) {
                        groupingFlg = true;
                    }

                    if (groupingFlg) {
                        // 取得したデータの計測年月日を判定
                        if (!DateUtility.changeDateFormat(dataList.get(i).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                            // 格納済の同一温湿度件数をセット
                            sameTempHumidCntList.add(resultList.get(resultList.size() - 1).getSameTempHumidCntList(j));
                        } else {
                            // 前回の計測年月日チェック済で取得結果に同日の計測年月日が存在し、
                            // 同一温湿度件数が多い場合はリストを詰めなおす
                            if (dataList.get(i).getSameTempHumidCnt() > resultList.get(resultList.size() - 1)
                                    .getSameTempHumidCntList(j)) {
                                sameTempHumidCntList.add(dataList.get(i).getSameTempHumidCnt());
                            } else {
                                sameTempHumidCntList.add(resultList.get(resultList.size() - 1).getSameTempHumidCntList(j));
                            }
                        }
                    } else {
                        // 取得したデータの計測年月日を判定
                        if (!DateUtility.changeDateFormat(dataList.get(i).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                            // 存在しない場合は0をセット
                            sameTempHumidCntList.add(0L);

                            if (addDayCnt == 0) {
                                // 計測年月日の先頭（前日）がエラーでない場合は
                                // 以降でエラーがあった場合でもエラーなしとする
                                targetData.setNonErrFlg(true);
                            }
                        } else {
                            sameTempHumidCntList.add(dataList.get(i).getSameTempHumidCnt());
                        }
                    }

                    addDayCnt--;
                }

                if (groupingFlg) {
                    // 同一のポイント番号を一行で出力するためにリストを上書き
                    resultList.get(resultList.size() - 1).setSameTempHumidCntList(sameTempHumidCntList);
                } else {
                    targetData.setSameTempHumidCntList(sameTempHumidCntList);
                    resultList.add(targetData);
                }
            }
        }

        // 取得結果を返却
        return resultList;

    }

    @Override
    public void persist(TDmRepPointTempHumidSensorCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(TDmRepPointTempHumidSensorCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepPointTempHumidSensorCheckListResultData> getResultList(
            List<TDmRepPointTempHumidSensorCheckListResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepPointTempHumidSensorCheckListResultData find(TDmRepPointTempHumidSensorCheckListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepPointTempHumidSensorCheckListResultData merge(TDmRepPointTempHumidSensorCheckListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepPointTempHumidSensorCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
