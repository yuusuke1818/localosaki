/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meter.GetSmsLatestDmvKwhResultData;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター管理 最新の検針値取得 ServiceDaoクラス
 * @author kimura.m
 */
public class GetSmsLatestDmvKwhDaoImple implements BaseServiceDao<GetSmsLatestDmvKwhResultData> {

    /** 取得件数 */
    public final static int LIMIT = 1;

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    // 【生成したいSQL】
//    SELECT
//        DMV_KWH
//      FROM
//        T_DAY_LOAD_SURVEY
//      WHERE
//        DEV_ID = 'M388819Y' /* $1 */
//        AND METER_MNG_ID = 1 /* $2 */
//        AND DMV_KWH IS NOT NULL
//      ORDER BY
//        GET_DATE DESC
//      LIMIT
//        1

    @Override
    public List<GetSmsLatestDmvKwhResultData> getResultList(GetSmsLatestDmvKwhResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GetSmsLatestDmvKwhResultData> query = builder.createQuery(GetSmsLatestDmvKwhResultData.class);

        // ■from句の整理
        // [MuDM2]ロードサーベイ日データTBL
        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);


        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), target.getDevId()));
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), target.getMeterMngId()));
        whereList.add(builder.isNotNull(root.get(TDayLoadSurvey_.dmvKwh)));

        // ■SELECT句の整理
        query = query.select(builder.construct(GetSmsLatestDmvKwhResultData.class,
                root.get(TDayLoadSurvey_.dmvKwh))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.desc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate)));

        return em.createQuery(query).setMaxResults(LIMIT).getResultList();
    }

    @Override
    public List<GetSmsLatestDmvKwhResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<GetSmsLatestDmvKwhResultData> getResultList(List<GetSmsLatestDmvKwhResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<GetSmsLatestDmvKwhResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public GetSmsLatestDmvKwhResultData find(GetSmsLatestDmvKwhResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(GetSmsLatestDmvKwhResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public GetSmsLatestDmvKwhResultData merge(GetSmsLatestDmvKwhResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(GetSmsLatestDmvKwhResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
