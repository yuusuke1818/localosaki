/**
 *
 */
package jp.co.osaki.sms.batch.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.DailyDataCreationDLSCountResultSet;
import jp.co.osaki.sms.batch.resultset.DailyDataCreationMeterListResultSet;

/**
 * 翌日0時00分の日報空データ作成 DAOクラス
 * @author sagi_h
 *
 */
public class CreateDailyDataDao extends SmsBatchDao {

    /**
     * コンストラクタ
     * @param entityManager
     */
    public CreateDailyDataDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 全メーターのリストを取得する。
     * @return メーターのリスト
     */
    public List<DailyDataCreationMeterListResultSet> getAllMeters() {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<DailyDataCreationMeterListResultSet> query = builder
                .createQuery(DailyDataCreationMeterListResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件:削除フラグがONでない
        conditionList.add(builder.notEqual(root.get(MMeter_.delFlg), OsolConstants.FLG_ON));

        query.select(builder.construct(DailyDataCreationMeterListResultSet.class, root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<DailyDataCreationMeterListResultSet> tMMeterList = this.entityManager.createQuery(query).getResultList();

        return tMMeterList;
    }

    /**
     * 指定した条件に合致するロードサーベイ日データの件数を取得する。
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param getDate 収集日時
     * @return 合致するロードサーベイ日データの件数
     */
    public DailyDataCreationDLSCountResultSet countDayLoadSurveyData(String devId, Long meterMngId, String getDate) {
        // ロードサーベイ日データ情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<DailyDataCreationDLSCountResultSet> query = builder
                .createQuery(DailyDataCreationDLSCountResultSet.class);
        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件2: メーター管理番号が引数に一致
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        // 条件3: 収集日時が引数に一致
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), getDate));

        // 論理削除対象でないため、削除フラグ判定はなし

        query.select(builder.construct(DailyDataCreationDLSCountResultSet.class, builder.count(root)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));
        this.entityManager.clear();

        DailyDataCreationDLSCountResultSet result = entityManager.createQuery(query).getSingleResult();

        return result;
    }

    /**
     * 与えられた値で、ロードサーベイ日データを登録する。
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param getDate 収集日時
     * @param recDate 現在時刻(REC_DATE、作成・更新日時用)
     */
    public void createDayLoadSurveyData(String devId, Long meterMngId, String getDate, Timestamp recDate) {
        TDayLoadSurvey tDayLoadSurvey = new TDayLoadSurvey();
        TDayLoadSurveyPK tDayLoadSurveyPK = new TDayLoadSurveyPK();

        tDayLoadSurveyPK.setDevId(devId);
        tDayLoadSurveyPK.setMeterMngId(meterMngId);
        tDayLoadSurveyPK.setGetDate(getDate);
        tDayLoadSurvey.setId(tDayLoadSurveyPK);

        tDayLoadSurvey.setVersion(Integer.valueOf(0));
        tDayLoadSurvey.setRecMan(SmsBatchConstants.REC_MAN.CREATE_DAILY_DATA.getVal());
        tDayLoadSurvey.setRecDate(recDate);
        tDayLoadSurvey.setCreateUserId(Long.valueOf(0));
        tDayLoadSurvey.setCreateDate(recDate);
        tDayLoadSurvey.setUpdateUserId(Long.valueOf(0));
        tDayLoadSurvey.setUpdateDate(recDate);

        entityManager.persist(tDayLoadSurvey);
        entityManager.flush();
        entityManager.clear();
    }
}
