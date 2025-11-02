package jp.co.osaki.osol.api.servicedao.sms.collect.dataview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.utility.CriteriaUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * ロードサーベイ 指定期間内の各メーターの使用量・指針値取得 ServiceDaoクラス
 *
 * @author ozaki.y
 */
public class ListSmsDataViewMeterValueServiceDaoImpl implements BaseServiceDao<ListSmsDataViewResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(ListSmsDataViewResultData target, EntityManager em) {
        return selectDevMeterValueDataList(em, target);
    }

    /**
     * 指定期間内の各メーターごとの合計量を取得.
     *
     * @param em EntityManager
     * @param target パラメータ
     * @return 指定期間内の各メーターごとの合計量List
     */
    private List<ListSmsDataViewResultData> selectDevMeterValueDataList(EntityManager em,
            ListSmsDataViewResultData target) {

        // 装置ID
        String devId = target.getDevId();
        // 対象メーター管理番号Collection
        Collection<Long> meterMngIdClct = target.getMeterMngIdList();
        // 対象日時フォーマット
        String targetDateTimeFormat = target.getTargetDateTimeFormat();

        // 収集日時Collection
        List<String> getDateList = new ArrayList<>();
        getDateList.addAll(target.getTargetDateTimeList());

        // 最古の対象収集日時
        Date oldestTargetDateTime = DateUtility.conversionDate(getDateList.get(0),
                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        // 最新の対象収集日時
        Date newestTargetDateTime = DateUtility.conversionDate(getDateList.get(getDateList.size() - 1),
                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

        // 各日各月使用量算出用
        Date getDateForCalcUseValue = null;
        // 合計値取得用
        Date getDateForTotal = null;

        if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)) {
            // 日報
            Date targetDateTimeForDailyTotalFrom = oldestTargetDateTime;

            if (SmsConstants.DISP_TIME_UNIT.M60.getVal().equals(target.getDispTimeUnit())) {
                // 表示時間単位が1時間の場合
                // 最新の対象収集日時 + 1時間を追加
                getDateList.add(DateUtility.changeDateFormat(DateUtility.plusHour(newestTargetDateTime, 1),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM));

            } else if (SmsConstants.DISP_TIME_UNIT.M30.getVal().equals(target.getDispTimeUnit())) {
                // 表示時間単位が30分の場合
                // 合計値(FROM)取得用:最古の対象収集日時 - 30分を追加
                targetDateTimeForDailyTotalFrom = DateUtility.plusMinute(targetDateTimeForDailyTotalFrom, -30);
                getDateList.add(DateUtility.changeDateFormat(targetDateTimeForDailyTotalFrom,
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
            }

            // 合計値(TO)取得用:(最古の対象収集日時 - 30分) + 1日を追加
            getDateForTotal = DateUtility.plusDay(targetDateTimeForDailyTotalFrom, 1);

        } else if (DateUtility.DATE_FORMAT_YYYYMMDD.equals(targetDateTimeFormat)) {
            // 月報
            // 各日使用量算出用:最新の対象収集日時 + 1日を追加
            getDateForCalcUseValue = DateUtility.plusDay(newestTargetDateTime, 1);
            // 合計値取得用:最古の対象収集日時 + 1ヶ月を追加
            getDateForTotal = DateUtility.plusMonth(oldestTargetDateTime, 1);

        } else {
            // 年報
            // 各月使用量算出用:最新の対象収集日時 + 1ヶ月を追加
            getDateForCalcUseValue = DateUtility.plusMonth(newestTargetDateTime, 1);
            // 合計値取得用:最古の対象収集日時 + 1年を追加
            getDateForTotal = DateUtility.plusYear(oldestTargetDateTime, 1);
        }

        if (getDateForCalcUseValue != null) {
            getDateList.add(
                    DateUtility.changeDateFormat(getDateForCalcUseValue, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
        }

        getDateList.add(DateUtility.changeDateFormat(getDateForTotal, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<ListSmsDataViewResultData> query = null;
        if (target.isForwardDiract()) {
            // 正方向の場合
            query = createForwardDiractQuery(builder, devId, meterMngIdClct, getDateList);
        } else {
            // 逆方向の場合
            query = createReverseDiractQuery(builder, devId, meterMngIdClct, getDateList);
        }

        return em.createQuery(query).getResultList();
    }

    /**
     * 正方向使用量・指針値データ取得CriteriaQueryを生成.
     *
     * @param builder CriteriaBuilder
     * @param devId 装置ID
     * @param meterMngIdClct 対象メーター管理番号Collection
     * @param getDateClct 収集日時Collection
     * @return 正方向使用量・指針値データ取得CriteriaQuery
     */
    private CriteriaQuery<ListSmsDataViewResultData> createForwardDiractQuery(CriteriaBuilder builder, String devId,
            Collection<Long> meterMngIdClct, Collection<String> getDateClct) {

        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);

        Root<TDayLoadSurvey> tDayloadSurvey = query.from(TDayLoadSurvey.class);

        Path<String> devIdPath = tDayloadSurvey.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId);
        Path<Long> meterMngIdPath = tDayloadSurvey.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId);
        Path<String> getDatePath = tDayloadSurvey.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate);

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(devIdPath, devId));

        if (CollectionUtils.isNotEmpty(meterMngIdClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdClct));
        }

        if (CollectionUtils.isNotEmpty(getDateClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(getDatePath, getDateClct));
        }

        query.select(builder.construct(ListSmsDataViewResultData.class,
                devIdPath, meterMngIdPath, getDatePath,
                tDayloadSurvey.get(TDayLoadSurvey_.kwh30), tDayloadSurvey.get(TDayLoadSurvey_.dmvKwh)))
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(builder.asc(devIdPath), builder.asc(meterMngIdPath), builder.asc(getDatePath));

        return query;
    }

    /**
     * 逆方向使用量・指針値データ取得CriteriaQueryを生成.
     *
     * @param devId 装置ID
     * @param meterMngIdClct 対象メーター管理番号Collection
     * @param getDateFrom 収集日時(FROM)
     * @param getDateTo 収集日時(TO)
     * @return 逆方向使用量・指針値データ取得CriteriaQuery
     */
    private CriteriaQuery<ListSmsDataViewResultData> createReverseDiractQuery(CriteriaBuilder builder, String devId,
            Collection<Long> meterMngIdClct, Collection<String> getDateClct) {

        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);

        Root<TDayLoadSurveyRev> tDayloadSurvey = query.from(TDayLoadSurveyRev.class);

        Path<String> devIdPath = tDayloadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.devId);
        Path<Long> meterMngIdPath = tDayloadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId);
        Path<String> getDatePath = tDayloadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate);

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(devIdPath, devId));

        if (CollectionUtils.isNotEmpty(meterMngIdClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdClct));
        }

        if (CollectionUtils.isNotEmpty(getDateClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(getDatePath, getDateClct));
        }

        query.select(builder.construct(ListSmsDataViewResultData.class,
                devIdPath, meterMngIdPath, getDatePath,
                tDayloadSurvey.get(TDayLoadSurveyRev_.kwh30), tDayloadSurvey.get(TDayLoadSurveyRev_.dmvKwh)))
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(builder.asc(devIdPath), builder.asc(meterMngIdPath), builder.asc(getDatePath));

        return query;
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(List<ListSmsDataViewResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData find(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData merge(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
