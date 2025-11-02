package jp.co.osaki.sms.batch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.AutoInspLoadSurveyResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterResultSet;
import jp.co.osaki.sms.batch.resultset.ReserveInspExecResultSet;

/**
 * 予約検針Dao.
 * @author kobayashi.sho
 */
public class ReservationInspDao extends SmsBatchDao {

    public ReservationInspDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 予約検針対象データを取得
     *
     * @param reserveInspDate 予約検針日時
     * @return 予約検針対象データリスト
     */
    public List<ReserveInspExecResultSet> getReserveInsp(Timestamp reserveInspDate) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReserveInspExecResultSet> query = cb.createQuery(ReserveInspExecResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);
        Join<MMeter, MDevPrm> joinMeterDevPrm = root.join(MMeter_.MDevPrm, JoinType.INNER);
        Join<MDevPrm, MDevRelation> joinDevPrmDevRel = joinMeterDevPrm.join(MDevPrm_.MDevRelations, JoinType.INNER);
        Join<MDevRelation, TBuilding> joinDevRelBuilding = joinDevPrmDevRel.join(MDevRelation_.TBuilding, JoinType.INNER);
        Join<TBuilding, MBuildingSms> joinBuildingSms = joinDevRelBuilding.join(TBuilding_.MBuildingSms, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件:reserveInspDate
        conditionList.add(cb.equal(root.get(MMeter_.reserveInspDate), reserveInspDate));
        // 条件:del_flg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        // 条件:DEV_ID='OC*'以外
        conditionList.add(cb.notLike(joinMeterDevPrm.get(MDevPrm_.devId), "OC%"));
        // 条件:DEV_KIND=1：MUDM2
        conditionList.add(cb.equal(joinMeterDevPrm.get(MDevPrm_.devKind), SmsBatchConstants.DEV_KIND.MUDM2.getVal()));
        // 条件:装置del_flg
        conditionList.add(cb.equal(joinMeterDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));
        // 条件:建物del_flg
        conditionList.add(cb.equal(joinDevRelBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // 条件:SMS建物del_flg
        conditionList.add(cb.equal(joinBuildingSms.get(MBuildingSms_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(ReserveInspExecResultSet.class,
                root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                root.get(MMeter_.srvEnt),
                root.get(MMeter_.meterType),
                root.get(MMeter_.multi),
                root.get(MMeter_.meterId),
                joinDevRelBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                cb.coalesce(joinBuildingSms.get(MBuildingSms_.chkInt), BigDecimal.ZERO)
                ))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<ReserveInspExecResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;
    }

    /**
     * 日報データ から指定した日時のデータを取得
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param targetDate 予約検針実施日時(予約日時のデータを取得)
     * @return 取得した 日報データ  null:日報データが取得できない
     */
    public AutoInspLoadSurveyResultSet getLoadSurveyForLast(String devId, Long meterMngId, String targetDate) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspLoadSurveyResultSet> query = cb.createQuery(AutoInspLoadSurveyResultSet.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件2:meterMngId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        // 条件3:getDate
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), targetDate));   // 20240906 hoshi 指定した日時のデータのみ取得するように修正

        query.select(cb.construct(AutoInspLoadSurveyResultSet.class,
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate),
                root.get(TDayLoadSurvey_.kwh30),
                root.get(TDayLoadSurvey_.dmvKwh)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));      // 20240906 hoshi 指定した日時のデータのみ取得するように修正


        this.entityManager.clear();
        List<AutoInspLoadSurveyResultSet> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        if (retList.size() == 0) {
            return null;
        }

        return retList.get(0);
    }

    /**
     * 月検診連番取得.
     * @param devId 装置ID
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param targetYmdHm 予約検針日時
     * @return 月検診連番
     */
    public Long getInspMonthNo(String devId, String inspYear, String inspMonth, String targetYmdHm) {
        Long ret = 1L;

        // リトライ処理の場合の値取得（予約検針日時と最新の検針日時が一致する場合）
        Long inspMonthNo = getInspMonthNoIfRetry(devId, inspYear, inspMonth, targetYmdHm);

        if (inspMonthNo != 0) {
            // リトライ処理の場合
            return inspMonthNo;
        }

        // クエリ生成
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        // ■from句の整理
        // 検針結果データ(サーバ用)TBL
        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        inspMonth = Integer.valueOf(inspMonth).toString();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));

        // ■SELECT句の整理
        query = query.select(builder.construct(Long.class,
                builder.coalesce(
                        builder.max(
                                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)
                                ), 0)
                )).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        List<Long> resList = this.entityManager.createQuery(query).getResultList();
        if (resList.size() != 0) {
            // COALESCE(MAX(insp_month_no),0) + 1の+1をここで行う
            ret = resList.get(0) + 1;
        }

        return ret;
    }

    /**
     * 月検診連番取得(リトライ処理の場合).
     * @param devId 装置ID
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param targetYmdHm 予約検針日時
     * @return 月検診連番
     */
    public Long getInspMonthNoIfRetry(String devId, String inspYear, String inspMonth, String targetYmdHm) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        builder.append("    MAX(insp_month_no) ");
        builder.append("FROM ");
        builder.append("    T_INSPECTION_METER_SVR ");
        builder.append("WHERE ");
        builder.append("    dev_id = :devId ");
        builder.append("    AND insp_year = :inspYear ");
        builder.append("    AND insp_month = :inspMonth ");
        builder.append("    AND insp_type = :inspType ");
        builder.append("    AND DATE_PART('year', latest_insp_date) = CAST(:year as int) ");
        builder.append("    AND DATE_PART('month', latest_insp_date) = CAST(:month as int) ");
        builder.append("    AND DATE_PART('day', latest_insp_date) = CAST(:day as int) ");
        builder.append("    AND DATE_PART('hour', latest_insp_date) = CAST(:hour as int) ");
        builder.append("    AND DATE_PART('minute', latest_insp_date) = CAST(:minute as int)");

        Query query = this.entityManager.createNativeQuery(builder.toString());

        // パラメータ設定
        query.setParameter("devId", devId);
        query.setParameter("inspYear", inspYear);
        query.setParameter("inspMonth", String.valueOf(Integer.parseInt(inspMonth)));
        query.setParameter("inspType", SmsBatchConstants.INSP_KIND.SCHEDULE.getVal());
        query.setParameter("year", targetYmdHm.substring(0, 4));

        String month = targetYmdHm.substring(4, 6);
        String day = targetYmdHm.substring(6, 8);
        String hour = targetYmdHm.substring(8, 10);
        String minute = targetYmdHm.substring(10, 12);

        query.setParameter("month", String.valueOf(Integer.parseInt(month)));
        query.setParameter("day", String.valueOf(Integer.parseInt(day)));
        query.setParameter("hour", String.valueOf(Integer.parseInt(hour)));
        query.setParameter("minute", String.valueOf(Integer.parseInt(minute)));

        @SuppressWarnings("unchecked")
        List<BigInteger> resultRecords = query.getResultList();

        if (resultRecords.size() == 1 && resultRecords.get(0) != null) {
            return resultRecords.get(0).longValue();
        } else {
            return 0L;
        }
    }

}
