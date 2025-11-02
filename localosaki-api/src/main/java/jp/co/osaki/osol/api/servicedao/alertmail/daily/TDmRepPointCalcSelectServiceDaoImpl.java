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

import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepPointCalcCheckListResultData;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPoint_;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPoint_;
import jp.co.osaki.osol.entity.TDmDayRepPoint;
import jp.co.osaki.osol.entity.TDmDayRepPointPK_;
import jp.co.osaki.osol.entity.TDmDayRepPoint_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 受電ゼロチェック ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class TDmRepPointCalcSelectServiceDaoImpl implements BaseServiceDao<TDmRepPointCalcCheckListResultData> {

    @Override
    public List<TDmRepPointCalcCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * チェック対象となる受電ゼロチェックデータを取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return 建物・テナントリスト
     */
    @Override
    public List<TDmRepPointCalcCheckListResultData> getResultList(TDmRepPointCalcCheckListResultData target,
            EntityManager em) {

        List<TDmRepPointCalcCheckListResultData> resultList = new ArrayList<TDmRepPointCalcCheckListResultData>();

        if (target == null) {
            return new ArrayList<TDmRepPointCalcCheckListResultData>();
        }

        Date nowDate = target.getNowDate();
        Date targetDate = target.getTargetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmRepPointCalcCheckListResultData> query = builder
                .createQuery(TDmRepPointCalcCheckListResultData.class);

        // 取得元テーブル
        Root<TDmDayRepPoint> rootTDmDayRepPoint = query.from(TDmDayRepPoint.class);

        // 結合テーブル
        Join<TDmDayRepPoint, MBuildingSmPoint> joinMBuildingSmPoint = rootTDmDayRepPoint
                .join(TDmDayRepPoint_.MBuildingSmPoint, JoinType.INNER);
        Join<MBuildingSmPoint, MSmPoint> joinMSmPoint = joinMBuildingSmPoint.join(MBuildingSmPoint_.MSmPoint,
                JoinType.INNER);

        // 条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(builder.equal(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId),
                target.getCorpId()));
        conditionList.add(builder.equal(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId),
                target.getBuildingId()));
        conditionList.add(builder.equal(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId),
                target.getSmId()));
        conditionList.add(builder.equal(joinMSmPoint.get(MSmPoint_.pointType), "P"));
        conditionList
                .add(builder.equal(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo), "SRC"));
        conditionList.add(builder.greaterThanOrEqualTo(
                rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), targetDate));
        conditionList.add(builder.lessThanOrEqualTo(
                rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), nowDate));
        conditionList.add(builder.and(builder.or(builder.isNull(rootTDmDayRepPoint.get(TDmDayRepPoint_.pointVal)),
                builder.equal(rootTDmDayRepPoint.get(TDmDayRepPoint_.pointVal), new BigDecimal("0")))));

        // SQL発行
        query = query
                .select(builder.construct(TDmRepPointCalcCheckListResultData.class,
                        rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                        builder.count(
                                rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate))))
                .where(builder.and(conditionList.toArray(new Predicate[] {})))
                .groupBy(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate))
                .orderBy((builder
                        .desc(rootTDmDayRepPoint.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate))));

        // SQL実行
        List<TDmRepPointCalcCheckListResultData> dataList = em.createQuery(query).getResultList();

        if (dataList == null || dataList.size() == 0) {

        } else {
            // 取得できた場合、過去一週間のいずれかの値が不正
            int addDayCnt = 0;
            int index = 0;

            TDmRepPointCalcCheckListResultData targetData = new TDmRepPointCalcCheckListResultData();
            List<Long> judenZeroNullCntList = new ArrayList<>();

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
                        // 存在しない場合は0をセット
                        judenZeroNullCntList.add(0L);

                        if (addDayCnt == 0) {
                            // 計測年月日の先頭（前日）がエラーでない場合は
                            // 以降でエラーがあった場合でもエラーなしとする
                            targetData.setNonErrFlg(true);
                        }
                    } else {
                        judenZeroNullCntList.add(dataList.get(index).getJudenZeroNullCnt());
                        index++;
                    }
                    addDayCnt--;
                } else {
                    // 存在しない場合は0をセット
                    judenZeroNullCntList.add(0L);
                    addDayCnt--;
                }
            }
            targetData.setJudenZeroNullCntList(judenZeroNullCntList);
            resultList.add(targetData);
        }

        // 取得結果を返却
        return resultList;

    }

    @Override
    public void persist(TDmRepPointCalcCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(TDmRepPointCalcCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepPointCalcCheckListResultData> getResultList(List<TDmRepPointCalcCheckListResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepPointCalcCheckListResultData find(TDmRepPointCalcCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmRepPointCalcCheckListResultData merge(TDmRepPointCalcCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmRepPointCalcCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
