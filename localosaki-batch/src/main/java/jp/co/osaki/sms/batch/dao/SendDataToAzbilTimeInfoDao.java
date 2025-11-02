package jp.co.osaki.sms.batch.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MBuildingInfo;
import jp.co.osaki.osol.entity.MBuildingInfoPK_;
import jp.co.osaki.osol.entity.MBuildingInfo_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TAzbilBatchStartup;
import jp.co.osaki.osol.entity.TAzbilBatchStartup_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.entity.TResendInfo;
import jp.co.osaki.osol.entity.TResendInfoPK_;
import jp.co.osaki.osol.entity.TResendInfo_;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.AzbilBatchStartupInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilExternalBuildingInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilExternalBuildingMeterInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilMeterDataResultSet;
import jp.co.osaki.sms.batch.resultset.AzbilResendInfoResultSet;

/**
 * アズビル用サーバー 日報データ送信実行 Daoクラス
 *
 * @author sagi_h
 *
 */
public class SendDataToAzbilTimeInfoDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public SendDataToAzbilTimeInfoDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 外部連携用 建物情報リストを取得する。
     *
     * @return 外部連携用 建物情報リスト
     */
    public List<AzbilExternalBuildingInfoResultSet> getExternalBuildingInfo() {
        // 外部連携用 建物情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilExternalBuildingInfoResultSet> query = builder.
                createQuery(AzbilExternalBuildingInfoResultSet.class);

        Root<MBuildingInfo> root = query.from(MBuildingInfo.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件なし

        query.select(builder.construct(AzbilExternalBuildingInfoResultSet.class,
                root.get(MBuildingInfo_.id).get(MBuildingInfoPK_.buildngInfoId),
                root.get(MBuildingInfo_.id).get(MBuildingInfoPK_.username),
                root.get(MBuildingInfo_.id).get(MBuildingInfoPK_.password),
                root.get(MBuildingInfo_.id).get(MBuildingInfoPK_.corpId),
                root.get(MBuildingInfo_.id).get(MBuildingInfoPK_.buildingId)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AzbilExternalBuildingInfoResultSet> tExternalBuildingInfoList =
                this.entityManager.createQuery(query).getResultList();

        return tExternalBuildingInfoList;
    }

    /**
     * 指定された装置IDと対応する建物メール情報を取得する。
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 建物情報
     */
    public List<AzbilExternalBuildingMeterInfoResultSet> getExternalBuildingMeterInfo(String corpId, long buildingId) {
        // 企業ID、建物IDから対象装置、メーター情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilExternalBuildingMeterInfoResultSet> query = builder.
                createQuery(AzbilExternalBuildingMeterInfoResultSet.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);
        // 装置情報と結合
        Join<MDevRelation, MDevPrm> joinMDevPrm = root.join(MDevRelation_.MDevPrm, JoinType.INNER);
        // メーター情報と結合
        Join<MDevPrm, MMeter> joinMMeter = joinMDevPrm.join(MDevPrm_.MMeters, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 建物、メーター関連テーブルの企業IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(MDevRelation_.id).get(
                MDevRelationPK_.corpId), corpId));
        // 条件2: 建物、メーター関連テーブルの企業IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(MDevRelation_.id).get(
                MDevRelationPK_.buildingId), buildingId));
        // 条件3:装置IDがMHから始まらない
        conditionList.add(builder.notLike(joinMDevPrm.get(MDevPrm_.devId), "MH%"));
        // 条件4:装置情報の削除フラグがONでない
        conditionList.add(builder.notEqual(joinMDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_ON));
        // 条件5:メーター情報の削除フラグがONでない
        conditionList.add(builder.notEqual(joinMMeter.get(MMeter_.delFlg), OsolConstants.FLG_ON));

        query.select(builder.construct(AzbilExternalBuildingMeterInfoResultSet.class,
                joinMDevPrm.get(MDevPrm_.devId),
                joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId),
                joinMMeter.get(MMeter_.meterId)
                )).where(builder.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(joinMDevPrm.get(MDevPrm_.devId)),
                        builder.asc(joinMMeter.get(MMeter_.id).get(MMeterPK_.meterMngId)),
                        builder.asc(joinMMeter.get(MMeter_.meterId)));


        this.entityManager.clear();
        List<AzbilExternalBuildingMeterInfoResultSet> tAzbilExternalBuildingIMeternfoList =
                this.entityManager.createQuery(query).getResultList();

        return tAzbilExternalBuildingIMeternfoList;

    }


    /**
     * 指定された装置IDと対応する建物メール情報を取得する。
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 建物情報
     */
    public AzbilMeterDataResultSet getMeterData(String devId, long meterMngId, String date) {

        // 企業ID、建物IDから対象装置、メーター情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilMeterDataResultSet> query = builder.
                createQuery(AzbilMeterDataResultSet.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: ロードサーベイの装置IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(
                TDayLoadSurveyPK_.devId), devId));
        // 条件2: ロードサーベイのメーター管理番号が引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(
                TDayLoadSurveyPK_.meterMngId), meterMngId));
        // 条件3: ロードサーベイのメーター管理番号が引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(
                TDayLoadSurveyPK_.getDate), date));

        query.select(builder.construct(AzbilMeterDataResultSet.class,
                root.get(TDayLoadSurvey_.dmvKwh),
                root.get(TDayLoadSurvey_.kwh30)
                )).where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AzbilMeterDataResultSet> tAzbilMeterDataResultSetList =
                this.entityManager.createQuery(query).getResultList();

        if (tAzbilMeterDataResultSetList != null && tAzbilMeterDataResultSetList.size() > 0) {
            return tAzbilMeterDataResultSetList.get(0);
        } else {
            return null;
        }

    }

    /**
     * 指定された建物、日時のアズビル再送信情報を取得する。
     *
     * @param buildingId 建物ID
     * @param resendDatetime 再送日時
     * @return 再送情報
     */
    public AzbilResendInfoResultSet getResendInfo(long buildingId, String resendDatetime) {

        // 企業ID、建物IDから対象装置、メーター情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilResendInfoResultSet> query = builder.
                createQuery(AzbilResendInfoResultSet.class);

        Root<TResendInfo> root = query.from(TResendInfo.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: ロードサーベイの装置IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TResendInfo_.id).get(
                TResendInfoPK_.buildingId), buildingId));
        // 条件2: ロードサーベイのメーター管理番号が引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TResendInfo_.id).get(
                TResendInfoPK_.resendDatetime), resendDatetime));

        query.select(builder.construct(AzbilResendInfoResultSet.class,
                root.get(TResendInfo_.id).get(TResendInfoPK_.buildingId),
                root.get(TResendInfo_.id).get(TResendInfoPK_.resendDatetime)
                )).where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AzbilResendInfoResultSet> tAzbilResendInfoResultSetList =
                this.entityManager.createQuery(query).getResultList();


        if (tAzbilResendInfoResultSetList != null && tAzbilResendInfoResultSetList.size() > 0) {
            return tAzbilResendInfoResultSetList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 指定された建物のアズビル再送信情報を取得する。
     *
     * @param buildingId 建物ID
     * @return 再送情報
     */
    public List<AzbilResendInfoResultSet> getResendInfo(long buildingId) {

        // 企業ID、建物IDから対象装置、メーター情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilResendInfoResultSet> query = builder.
                createQuery(AzbilResendInfoResultSet.class);

        Root<TResendInfo> root = query.from(TResendInfo.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: ロードサーベイの装置IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(TResendInfo_.id).get(
                TResendInfoPK_.buildingId), buildingId));

        query.select(builder.construct(AzbilResendInfoResultSet.class,
                root.get(TResendInfo_.id).get(TResendInfoPK_.buildingId),
                root.get(TResendInfo_.id).get(TResendInfoPK_.resendDatetime)
                )).where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AzbilResendInfoResultSet> tAzbilResendInfoResultSetList =
                this.entityManager.createQuery(query).getResultList();


        if (tAzbilResendInfoResultSetList != null && tAzbilResendInfoResultSetList.size() > 0) {
            return tAzbilResendInfoResultSetList;
        } else {
            return null;
        }
    }

    /**
     * アズビル再送信情報削除
     *
     * @param String buildingId
     * @param String resendTargetDate
     */
    public void removeResendInfo(Long buildingId, String resendTargetDate) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaDelete<TResendInfo> delete = builder.createCriteriaDelete(TResendInfo.class);

        Root<TResendInfo> root = delete.from(TResendInfo.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:buildingId
        conditionList.add(builder.equal(root.get(TResendInfo_.id).get(TResendInfoPK_.buildingId), buildingId));
        // 条件2:resendTargetDate
        conditionList.add(builder.equal(root.get(TResendInfo_.id).get(TResendInfoPK_.resendDatetime), resendTargetDate));

        delete.where(builder.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    /**
    *
    * アズビル再送信情報の追加
    *
    * @param em
    * @param t
    * @param svDate
    */
    public void createMEnergyUseTargetMonth(TResendInfo t, Timestamp svDate) {

        t.setCreateUserId(Long.valueOf(0));
        t.setCreateDate(svDate);
        t.setUpdateUserId(Long.valueOf(0));
        t.setUpdateDate(svDate);
        entityManager.persist(t);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * アズビル実行用外部連携リストを取得する。
     *
     * @return 外部連携用 アズビル実行リスト
     */
    public List<AzbilBatchStartupInfoResultSet> getAzbilBatchStartupInfoList() {
        // 外部連携用 建物情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AzbilBatchStartupInfoResultSet> query = builder.
                createQuery(AzbilBatchStartupInfoResultSet.class);

        Root<TAzbilBatchStartup> root = query.from(TAzbilBatchStartup.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件なし

        query.select(builder.construct(AzbilBatchStartupInfoResultSet.class,
                root.get(TAzbilBatchStartup_.batchId),
                root.get(TAzbilBatchStartup_.exeDate)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AzbilBatchStartupInfoResultSet> tAzbilBatchStartupInfoList =
                this.entityManager.createQuery(query).getResultList();

        return tAzbilBatchStartupInfoList;
    }

    /**
     * アズビル実行用外部連携データ削除
     *
     * @param String id
     * @param String exeDate
     */
    public void removeAzbilBatchStartupInfo(Long id, String exeDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TAzbilBatchStartup> delete = cb.createCriteriaDelete(TAzbilBatchStartup.class);

        Root<TAzbilBatchStartup> root = delete.from(TAzbilBatchStartup.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:Id
        conditionList.add(cb.equal(root.get(TAzbilBatchStartup_.batchId), id));
        // 条件2:exeDate
        conditionList.add(cb.equal(root.get(TAzbilBatchStartup_.exeDate), exeDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }



}
