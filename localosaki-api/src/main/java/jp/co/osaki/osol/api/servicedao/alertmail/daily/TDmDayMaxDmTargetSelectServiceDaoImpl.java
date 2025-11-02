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
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmDayMaxDmTargetCheckListResultData;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK_;
import jp.co.osaki.osol.entity.MBuildingDm_;
import jp.co.osaki.osol.entity.TDmMonthRep;
import jp.co.osaki.osol.entity.TDmMonthRepPK_;
import jp.co.osaki.osol.entity.TDmMonthRep_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド目標超過チェック ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class TDmDayMaxDmTargetSelectServiceDaoImpl implements BaseServiceDao<TDmDayMaxDmTargetCheckListResultData> {

    @Override
    public List<TDmDayMaxDmTargetCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * チェック対象となるデマンド目標超過チェックデータを取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return 建物・テナントリスト
     */
    @Override
    public List<TDmDayMaxDmTargetCheckListResultData> getResultList(TDmDayMaxDmTargetCheckListResultData target,
            EntityManager em) {

        List<TDmDayMaxDmTargetCheckListResultData> resultList = new ArrayList<TDmDayMaxDmTargetCheckListResultData>();

        if (target == null) {
            return new ArrayList<TDmDayMaxDmTargetCheckListResultData>();
        }

        Date nowDate = target.getNowDate();
        Date targetDate = target.getTargetDate();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDmDayMaxDmTargetCheckListResultData> query = builder
                .createQuery(TDmDayMaxDmTargetCheckListResultData.class);

        // 取得元テーブル
        Root<TDmMonthRep> rootTDmMonthRep = query.from(TDmMonthRep.class);

        // 結合テーブル
        Join<TDmMonthRep, MBuildingDm> joinMBuildingDm = rootTDmMonthRep.join(TDmMonthRep_.MBuildingDm,
                JoinType.INNER);

        // 結合条件（ON句）
        joinMBuildingDm.on(
                builder.equal(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.buildingId), joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.buildingId)),
                builder.equal(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.corpId), joinMBuildingDm.get(MBuildingDm_.id).get(MBuildingDmPK_.corpId)),
                builder.equal(joinMBuildingDm.get(MBuildingDm_.delFlg), OsolConstants.FLG_OFF));

        // 条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(
                builder.equal(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.corpId), target.getCorpId()));
        conditionList.add(builder.equal(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.buildingId),
                target.getBuildingId()));
        conditionList.add(builder.greaterThanOrEqualTo(
                rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate), targetDate));
        conditionList.add(builder
                .lessThanOrEqualTo(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate), nowDate));
        conditionList.add(builder.and(builder.isNotNull(joinMBuildingDm.get(MBuildingDm_.targetKw)), builder
                .greaterThan(rootTDmMonthRep.get(TDmMonthRep_.maxKw), joinMBuildingDm.get(MBuildingDm_.targetKw))));

        // SQL発行
        query = query
                .select(builder.construct(TDmDayMaxDmTargetCheckListResultData.class,
                        rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate),
                        rootTDmMonthRep.get(TDmMonthRep_.maxKw),
                        joinMBuildingDm.get(MBuildingDm_.targetKw)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(builder.desc(rootTDmMonthRep.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate)));

        List<TDmDayMaxDmTargetCheckListResultData> dataList = em.createQuery(query).getResultList();

        // 取得できなかった場合、チェックエラーなし
        if (dataList == null || dataList.size() == 0) {

        } else {

            int addDayCnt = 0;
            int index = 0;

            TDmDayMaxDmTargetCheckListResultData targetData = new TDmDayMaxDmTargetCheckListResultData();
            List<BigDecimal> maxDmTargetList = new ArrayList<>();

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
                    targetData.setTargetKw(dataList.get(index).getTargetKw());
                }

                if (index < dataList.size()) {

                    // 取得したデータの計測年月日を判定
                    if (!DateUtility.changeDateFormat(dataList.get(index).getMeasurementDate(),
                            DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .equals(DateUtility.changeDateFormat(DateUtility.plusDay(nowDate, addDayCnt),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                        maxDmTargetList.add(null);

                        if (addDayCnt == 0) {
                            // 計測年月日の先頭（前日）がエラーでない場合は
                            // 以降でエラーがあった場合でもエラーなしとする
                            targetData.setNonErrFlg(true);
                        }
                    } else {
                        maxDmTargetList.add(dataList.get(index).getMaxKw());
                        index++;
                    }
                    addDayCnt--;
                } else {
                    // 取得したデータリストサイズを超える場合
                    maxDmTargetList.add(null);
                    addDayCnt--;
                }
            }
            targetData.setMaxDmTargetList(maxDmTargetList);
            resultList.add(targetData);
        }

        // 取得結果を返却
        return resultList;

    }

    @Override
    public void persist(TDmDayMaxDmTargetCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(TDmDayMaxDmTargetCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmDayMaxDmTargetCheckListResultData> getResultList(
            List<TDmDayMaxDmTargetCheckListResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmDayMaxDmTargetCheckListResultData find(TDmDayMaxDmTargetCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDmDayMaxDmTargetCheckListResultData merge(TDmDayMaxDmTargetCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDmDayMaxDmTargetCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
