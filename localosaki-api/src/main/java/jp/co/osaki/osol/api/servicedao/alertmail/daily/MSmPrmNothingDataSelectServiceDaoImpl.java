package jp.co.osaki.osol.api.servicedao.alertmail.daily;

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

import jp.co.osaki.osol.api.resultdata.alertmail.daily.MSmPrmNothingDataCheckListResultData;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPointPK_;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.entity.TDmDayRepPoint;
import jp.co.osaki.osol.entity.TDmDayRepPointPK_;
import jp.co.osaki.osol.entity.TDmDayRepPoint_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * データなしチェック ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class MSmPrmNothingDataSelectServiceDaoImpl implements BaseServiceDao<MSmPrmNothingDataCheckListResultData> {

    /**
     * (ポイント（0001～256） + 受電) * 時限数
     */
    private static final Long POINT_ALL_CNT = 257L * 48L;

    @Override
    public List<MSmPrmNothingDataCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * チェック対象となるデータなしチェックデータを取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return データなしチェックリスト
     */
    @Override
    public List<MSmPrmNothingDataCheckListResultData> getResultList(MSmPrmNothingDataCheckListResultData target,
            EntityManager em) {

        List<MSmPrmNothingDataCheckListResultData> resultList = new ArrayList<MSmPrmNothingDataCheckListResultData>();

        if (target == null) {
            return new ArrayList<MSmPrmNothingDataCheckListResultData>();
        }

        Date nowDate = target.getNowDate();
        Date targetDate = target.getTargetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MSmPrmNothingDataCheckListResultData> query = builder
                .createQuery(MSmPrmNothingDataCheckListResultData.class);

        // 取得元テーブル
        Root<MSmPoint> rootMSmPoint = query.from(MSmPoint.class);

        // 結合テーブル
        Join<MSmPoint, TDmDayRepPoint> joinTDmDayRepPoint = rootMSmPoint.join(MSmPoint_.TDmDayRepPoints,
                JoinType.INNER);

        // 結合条件
        joinTDmDayRepPoint.on(
                builder.equal(rootMSmPoint.get(MSmPoint_.id).get(MSmPointPK_.smId), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId)),
                builder.equal(rootMSmPoint.get(MSmPoint_.id).get(MSmPointPK_.pointNo), joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo)),
                builder.notEqual(rootMSmPoint.get(MSmPoint_.pointType), "A"));

        // 条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList
                .add(builder.equal(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId),
                        target.getCorpId()));
        conditionList
                .add(builder.equal(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId),
                        target.getBuildingId()));
        conditionList.add(builder.equal(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId),
                target.getSmId()));
        conditionList.add(builder.lessThanOrEqualTo(
                joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), nowDate));
        conditionList.add(builder.greaterThanOrEqualTo(
                joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), targetDate));
        conditionList.add(builder.isNotNull(joinTDmDayRepPoint.get(TDmDayRepPoint_.pointVal)));

        // SQL発行
        query = query
                .select(builder.construct(MSmPrmNothingDataCheckListResultData.class,
                        joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})))
                .groupBy(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate))
                .having(builder.lessThan(
                        builder.count(
                                joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate)),
                        POINT_ALL_CNT))
                .orderBy(builder
                        .desc(joinTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate)));

        // SQL実行
        List<MSmPrmNothingDataCheckListResultData> dataList = em.createQuery(query).getResultList();

        // 取得できなかった場合、チェックエラーなし
        if (dataList == null || dataList.size() == 0) {

        } else {

            // 取得できた場合、過去一週間のいずれかの値が不正
            int addDayCnt = 0;
            int index = 0;

            MSmPrmNothingDataCheckListResultData targetData = new MSmPrmNothingDataCheckListResultData();
            List<String> nothingDataCheckList = new ArrayList<>();

            // 未収集日チェック
            // 現在日付から逆算していき、過去一週間分を格納
            for (int i = 0; i < 7; i++) {

                if (i == 0) {
                    targetData.setCorpName(target.getCorpName());
                    targetData.setCorpId(target.getCorpId());
                    targetData.setBuildingNo(target.getBuildingNo());
                    targetData.setBuildingName(target.getBuildingName());
                    targetData.setSmId(target.getSmId());
                    targetData.setSmAddress(target.getSmAddress());
                    targetData.setIpAddress(target.getIpAddress());
                }

                if (index < dataList.size()) {
                    // 取得したデータの計測年月日を判定
                    if (!DateUtility.changeDateFormat(dataList.get(index).getMeasurementDate(),
                            DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                        nothingDataCheckList.add("×");

                    } else {
                        nothingDataCheckList.add("○");

                        if (addDayCnt == 0) {
                            // 計測年月日の先頭（前日）がエラーでない場合は
                            // 以降でエラーがあった場合でもエラーなしとする
                            targetData.setNonErrFlg(true);
                        }

                        index++;
                    }

                    addDayCnt--;
                } else {
                    nothingDataCheckList.add("×");
                    addDayCnt--;
                }
            }

            for (String resultStr : nothingDataCheckList) {
                // ×が含まれている場合のみリストに格納
                if ("×".equals(resultStr)) {
                    targetData.setNothingDataCheckList(nothingDataCheckList);
                    resultList.add(targetData);
                    break;
                }
            }
        }

        // 取得結果を返却
        return resultList;

    }

    @Override
    public void persist(MSmPrmNothingDataCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(MSmPrmNothingDataCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmPrmNothingDataCheckListResultData> getResultList(
            List<MSmPrmNothingDataCheckListResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmPrmNothingDataCheckListResultData find(MSmPrmNothingDataCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmPrmNothingDataCheckListResultData merge(MSmPrmNothingDataCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmPrmNothingDataCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
