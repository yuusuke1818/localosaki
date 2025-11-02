package jp.co.osaki.sms.servicedao;

import java.text.SimpleDateFormat;
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
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemand;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandRevPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandRev_;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemand_;
import jp.co.osaki.osol.utility.CriteriaUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 過去最大デマンド取得
 * @author hayashi_tak
 *
 */
public class ListSmsMaxDemandPastAllDevDaoImpl implements BaseServiceDao<ListSmsDataViewResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(ListSmsDataViewResultData target, EntityManager em) {
        List<ListSmsDataViewResultData> resultData = new ArrayList<>();
        // 装置ID
        Collection<String> devIdList = new ArrayList<>(target.getDevIdMeterMngIdListMap().keySet());
        for(String devId: devIdList) {
            // メーター管理番号
            Collection<Long> meterMngIdList = target.getDevIdMeterMngIdListMap().get(devId);


            //最大デマンド取得
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);

            // 収集日時Collection
            List<String> getDateList = new ArrayList<>();
            getDateList.addAll(target.getTargetDateTimeList());
            String today = createToday();

            List<String> devIdClct = target.getDevIdList();

            if (target.isForwardDiract()) {
                // 正方向の場合
                query = createForwardDiractQuery(builder, devId, meterMngIdList, today);
            } else {
                // 逆方向の場合
                query = createReverseDiractQuery(builder, devId, meterMngIdList, today);
            }

            resultData.addAll(em.createQuery(query).getResultList());
        }
        return resultData;
    }

    /**
     * 正方向使用量・指針値データ取得CriteriaQueryを生成.
     *
     * @param builder CriteriaBuilder
     * @param devId 装置ID
     * @param meterMngIdClct 対象メーター管理番号Collection
     * @param getDateClct 収集日時Collection
     * @return 正方向の最大デマンド取得CriteriaQuery
     */
    private CriteriaQuery<ListSmsDataViewResultData> createForwardDiractQuery(CriteriaBuilder builder, String devId,
            Collection<Long> meterMngIdList, String today) {

        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);
        Root<TDayLoadSurveyMaxDemand> root = query.from(TDayLoadSurveyMaxDemand.class);

        List<Predicate> whereList = new ArrayList<>();
        Path<String> devIdPath = root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.devId);
        Path<Long> meterMngIdPath = root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.meterMngId);
        whereList.add(builder.equal(devIdPath, devId));
        if (CollectionUtils.isNotEmpty(meterMngIdList)) {
            whereList.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdList));
        }

        // 取得日時
        whereList.add(builder.lessThanOrEqualTo(root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.getSurveyDate), today));

        query.select(builder.construct(ListSmsDataViewResultData.class,
                root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.devId),
                root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.meterMngId),
                builder.max(root.get(TDayLoadSurveyMaxDemand_.maxDemand)),
                root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.getSurveyDate)))
        .where(whereList.toArray(new Predicate[]{}))
        .groupBy(root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.devId), root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.meterMngId), root.get(TDayLoadSurveyMaxDemand_.id).get(TDayLoadSurveyMaxDemandPK_.getSurveyDate));

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
            Collection<Long> meterMngIdList, String today) {

        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);
        Root<TDayLoadSurveyMaxDemandRev> root = query.from(TDayLoadSurveyMaxDemandRev.class);

        List<Predicate> whereList = new ArrayList<>();
        Path<String> devIdPath = root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.devId);
        Path<Long> meterMngIdPath = root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.meterMngId);
        whereList.add(builder.equal(devIdPath, devId));
        if (CollectionUtils.isNotEmpty(meterMngIdList)) {
            whereList.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdList));
        }

        // 取得日時
        whereList.add(builder.lessThanOrEqualTo(root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.getSurveyDate), today));

        query.select(builder.construct(ListSmsDataViewResultData.class,
                root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.devId),
                root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.meterMngId),
                builder.max(root.get(TDayLoadSurveyMaxDemandRev_.maxDemand)),
                root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.getSurveyDate)))
        .where(whereList.toArray(new Predicate[]{}))
        .groupBy(root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.devId), root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.meterMngId), root.get(TDayLoadSurveyMaxDemandRev_.id).get(TDayLoadSurveyMaxDemandRevPK_.getSurveyDate));

        return query;
    }

    private String createToday() {
        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strDate = dateFormat.format(date);
        return strDate;

    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(List<ListSmsDataViewResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsDataViewResultData find(ListSmsDataViewResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsDataViewResultData target, EntityManager em) {


    }

    @Override
    public ListSmsDataViewResultData merge(ListSmsDataViewResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsDataViewResultData target, EntityManager em) {


    }

}
