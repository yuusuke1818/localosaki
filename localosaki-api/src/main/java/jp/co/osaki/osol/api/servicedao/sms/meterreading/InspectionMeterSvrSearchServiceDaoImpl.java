/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterSvrSearchResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 検針結果データ(サーバ用) 検索 ServiceDaoクラス
 * @author kobayashi.sho
 */
public class InspectionMeterSvrSearchServiceDaoImpl implements BaseServiceDao<InspectionMeterSvrSearchResultData> {

    /** 検索条件: 装置ID. */
    public static final String DEV_ID = "devId";
    /** 検索条件: メーター管理番号一覧. */
    public static final String METER_MNG_ID = "meterMngId";
    /** 検索条件: 検針年. */
    public static final String INSP_YEAR = "inspYear";
    /** 検索条件: 検針月. */
    public static final String INSP_MONTH = "inspMonth";
    /** 検索条件: 検針種別. */
    public static final String INSP_TYPE = "inspType";
    /** 検索条件: チェック対象外 月検針連番 (null:可). */
    public static final String EXCEPTION_INSP_MONTH_NO = "exceptionInspMonthNo";

    // 検針連番一覧 取得(連番プルダウン用)
    // 複数件数取得DBアクセス処理
    @Override
    public List<InspectionMeterSvrSearchResultData> getResultList(InspectionMeterSvrSearchResultData target, EntityManager em) {
        // 検針連番一覧 取得(連番プルダウン用)

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getInspMonth()));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), target.getInspType()));

        query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)))
                .distinct(true)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));

        return em.createQuery(query).getResultList();
    }

    // 確定登録しようとしている"メーター管理番号"が既に確定登録済みかチェックする(重複チェック)
    // 複数件数取得DBアクセス処理
    @Override
    public List<InspectionMeterSvrSearchResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {

        String devId = Utility.getListFirstItem(map.get(DEV_ID)).toString();               // 装置ID(必須)
        List<Long> meterMngIdList = map.get(METER_MNG_ID).stream().map(meterMngId -> (Long) meterMngId).collect(Collectors.toList()); // メーター管理番号一覧(必須)
        String inspYear = Utility.getListFirstItem(map.get(INSP_YEAR)).toString();         // 検針年(必須)
        String inspMonth = Utility.getListFirstItem(map.get(INSP_MONTH)).toString();       // 検針月(必須)
        String inspType = Utility.getListFirstItem(map.get(INSP_TYPE)).toString();          // 検針種別(必須)
        Long exceptionInspMonthNo = (Long) Utility.getListFirstItem(map.get(EXCEPTION_INSP_MONTH_NO)); // チェック対象外 月検針連番(非必須) ※nullあり

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        whereList.add(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId).in(meterMngIdList));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), inspType));
        if (exceptionInspMonthNo != null) { // チェック対象外の月検針連番 が指定されているときは、除外条件を追加
            whereList.add(builder.notEqual(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), exceptionInspMonthNo));
        }

        query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth)
                ))
                .distinct(true)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    // 月検針連番(最大値)を取得
    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public InspectionMeterSvrSearchResultData find(InspectionMeterSvrSearchResultData target, EntityManager em) {

        if (target.getMode() == InspectionMeterSvrSearchResultData.MODE_MAX_INSP_MONTH_NO) {
            // 月検針連番を取得

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            List<Predicate> whereList = new ArrayList<>();
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getInspMonth()));

            query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                    builder.max(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo))))
                    .where(builder.and(whereList.toArray(new Predicate[]{})));

            List<InspectionMeterSvrSearchResultData> resultList = em.createQuery(query).getResultList();
            if (resultList == null || resultList.isEmpty()) {
                return null;
            }

            return resultList.get(0);
        } else if (target.getMode() == InspectionMeterSvrSearchResultData.MODE_CHECK_FUTURE_INSP_DATA) {
            // 検針結果データ(サーバ用)有無チェック用 ※指定の 検針年,検針月,月検針連番 より未来のデータチェック

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            List<Predicate> whereList = new ArrayList<>();
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getMeterMngId()));
            if (target.getInspMonthNo() != null) {
                // 月検針連番あり
                whereList.add(
                    builder.or(
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getInspMonth()), // =
                            builder.greaterThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),target.getInspMonthNo()) // >
                        ),
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.gt(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class), Integer.valueOf(target.getInspMonth())) // >
                        ),
                        builder.greaterThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()) // >
                        ));
            } else {
                // 月検針連番なし
                whereList.add(
                    builder.or(
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.gt(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class), Integer.valueOf(target.getInspMonth())) // >
                        ),
                        builder.greaterThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()) // >
                        ));
            }

            query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo))) // ← データ存在チェックのため、取得するカラムはなんでもいい
                    .where(builder.and(whereList.toArray(new Predicate[]{})));

            List<InspectionMeterSvrSearchResultData> resultList = em.createQuery(query)
                    .setMaxResults(1) // limit 1
                    .getResultList();
            if (resultList == null || resultList.isEmpty()) {
                return null;
            }

            return resultList.get(0);
        } else if (target.getMode() == InspectionMeterSvrSearchResultData.MODE_CHECK_INSP_DATA) {
            // 指定した検針年月の検針結果データ有無チェック

            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            List<Predicate> whereList = new ArrayList<>();
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getMeterMngId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getInspMonth()));
            if (target.getInspMonthNo() != null) {
                whereList.add(builder.lessThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),target.getInspMonthNo())); // <
            }

            query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                    root.get(TInspectionMeterSvr_.latestInspVal),
                    root.get(TInspectionMeterSvr_.prevInspVal),
                    root.get(TInspectionMeterSvr_.latestInspDate),
                    root.get(TInspectionMeterSvr_.prevInspDate)
                    ))
                    .where(builder.and(whereList.toArray(new Predicate[]{})))
                    .orderBy(builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));

            List<InspectionMeterSvrSearchResultData> resultList = em.createQuery(query)
                    .setMaxResults(1) // limit 1
                    .getResultList();
            if (resultList == null || resultList.isEmpty()) {
                return null;
            }

            return resultList.get(0);
        } else if (target.getMode() == InspectionMeterSvrSearchResultData.MODE_CHECK_EXISTS_INSP_DATA_ALL_TIME) {
            // ※仕様変更 クラウド検針サーバ設計変更_No.7
            //// 指定装置ID、メーター管理番号の検針結果データ存在の真偽値を取得
            //
            //CriteriaBuilder builder = em.getCriteriaBuilder();
            //
            //CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);
            //
            //Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);
            //
            //List<Predicate> whereList = new ArrayList<>();
            //whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
            //whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getMeterMngId()));
            //
            //query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
            //        root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)
            //        ))
            //        .where(builder.and(whereList.toArray(new Predicate[]{})));
            //
            //List<InspectionMeterSvrSearchResultData> resultList = em.createQuery(query)
            //        .setMaxResults(1) // limit 1
            //        .getResultList();
            //if (resultList == null || resultList.isEmpty()) {
            //    return null;
            //}
            //
            //return resultList.get(0);
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        } else { // if (target.getMode() == InspectionMeterSvrResultData.MODE_LATEST_INSP_DATA_ALL_PAST) {

            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<InspectionMeterSvrSearchResultData> query = builder.createQuery(InspectionMeterSvrSearchResultData.class);

            Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

            List<Predicate> whereList = new ArrayList<>();
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), target.getDevId()));
            whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), target.getMeterMngId()));

            if (target.getInspMonthNo() == null) {
                whereList.add(
                    builder.or(
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.le(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class), Integer.valueOf(target.getInspMonth())) // <=
                        ),
                        builder.lessThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()))); // <
            } else {
                whereList.add(
                    builder.or(
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), target.getInspMonth()), // =
                            builder.lessThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), target.getInspMonthNo()) // <
                        ),
                        builder.and(
                            builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()), // =
                            builder.lt(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class), Integer.valueOf(target.getInspMonth())) // <
                        ),
                        builder.lessThan(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), target.getInspYear()))); // <
            }

            query = query.select(builder.construct(InspectionMeterSvrSearchResultData.class,
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                    root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                    root.get(TInspectionMeterSvr_.latestInspVal),
                    root.get(TInspectionMeterSvr_.prevInspVal),
                    root.get(TInspectionMeterSvr_.latestInspDate),
                    root.get(TInspectionMeterSvr_.prevInspDate)
                    ))
                    .where(builder.and(whereList.toArray(new Predicate[]{})))
                    .orderBy(
                            builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear)),
                            builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class)),
                            builder.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo))
                            );

            List<InspectionMeterSvrSearchResultData> resultList = em.createQuery(query)
                    .setMaxResults(1) // limit 1
                    .getResultList();
            if (resultList == null || resultList.isEmpty()) {
                return null;
            }

            return resultList.get(0);
        }
    }

    // 更新処理
    @Override
    public InspectionMeterSvrSearchResultData merge(InspectionMeterSvrSearchResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InspectionMeterSvrSearchResultData> getResultList(List<InspectionMeterSvrSearchResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InspectionMeterSvrSearchResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(InspectionMeterSvrSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(InspectionMeterSvrSearchResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
