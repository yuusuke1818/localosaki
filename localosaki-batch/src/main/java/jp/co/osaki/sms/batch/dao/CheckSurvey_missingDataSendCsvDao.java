package jp.co.osaki.sms.batch.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MAlertMailSetting;
import jp.co.osaki.osol.entity.MAlertMailSetting_;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMailaddress;
import jp.co.osaki.osol.entity.MMailaddress_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.MPauseMail_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.BatchSendEmailResultSet;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.LoadSurveyResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.co.osaki.sms.batch.resultset.MeterTypeResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;
import jp.co.osaki.sms.batch.resultset.MissngDataCsvResultSet;
/**
 * SMS 日報欠損メール送信 csv添付 Daoクラス
 *
 * @author nishida.t
 *
 */
public class CheckSurvey_missingDataSendCsvDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public CheckSurvey_missingDataSendCsvDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 日報欠損メール送信対象建物リスト取得
     *
     * @param
     * @return 日報欠損メール送信対象建物リスト
     */
    public List<MissingLoadSurveyListResultSet> listMissingBuild() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MissingLoadSurveyListResultSet> query = cb.createQuery(MissingLoadSurveyListResultSet.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MBuildingSms> joinBuilingsms = root.join(TBuilding_.MBuildingSms, JoinType.INNER);
        List<Predicate> conditionList = new ArrayList<>();
        // 建物delflg
        conditionList.add(cb.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // SMS建物delflg
        conditionList.add(cb.equal(joinBuilingsms.get(MBuildingSms_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(MissingLoadSurveyListResultSet.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinBuilingsms.get(MBuildingSms_.mail1),
                joinBuilingsms.get(MBuildingSms_.mail2),
                joinBuilingsms.get(MBuildingSms_.mail3),
                joinBuilingsms.get(MBuildingSms_.mail4),
                joinBuilingsms.get(MBuildingSms_.mail5),
                joinBuilingsms.get(MBuildingSms_.mail6),
                joinBuilingsms.get(MBuildingSms_.mail7),
                joinBuilingsms.get(MBuildingSms_.mail8),
                joinBuilingsms.get(MBuildingSms_.mail9),
                joinBuilingsms.get(MBuildingSms_.mail10)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

        this.entityManager.clear();
        List<MissingLoadSurveyListResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;
    }

    /**
     * 日報欠損メール送信対象装置リスト取得
     *
     * @param String corpId
     * @param Long   builingId
     * @return 日報欠損メール送信対象装置リスト
     */
    public List<CheckSurveyDevPrmResultSet> listMissingDev(String corpId, Long builingId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CheckSurveyDevPrmResultSet> query = cb.createQuery(CheckSurveyDevPrmResultSet.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);
        Join<MDevPrm, MDevRelation> joinDevRel = root
                .join(MDevPrm_.MDevRelations, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:DEV_ID='MH*'以外
        conditionList.add(cb.notLike(root.get(MDevPrm_.devId), "MH%"));
        // 条件2:DEV_ID='OC*'以外
        conditionList.add(cb.notLike(root.get(MDevPrm_.devId), "OC%"));
        // 条件3:alertDisableFlg （警報メール対象）
        conditionList.add(cb.equal(root.get(MDevPrm_.alertDisableFlg), "0"));
        // 条件4:corpId
        conditionList.add(cb.equal(joinDevRel.get(MDevRelation_.id).get(MDevRelationPK_.corpId), corpId));
        // 条件5:buildingId
        conditionList.add(cb.equal(joinDevRel.get(MDevRelation_.id).get(MDevRelationPK_.buildingId), builingId));
        // 装置delflg
        conditionList.add(cb.equal(root.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(CheckSurveyDevPrmResultSet.class,
                root.get(MDevPrm_.devId),
                root.get(MDevPrm_.name)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MDevPrm_.devId)));

        this.entityManager.clear();
        List<CheckSurveyDevPrmResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * 日報リスト取得
     *
     * @param devId 装置ID
     * @param befDate 収集日時From
     * @param aftDate 収集日時To
     * @param meterMngId メーター管理番号
     * @return 日報リスト
     */
    public List<LoadSurveyResultSet> getLoadSurveyList(String devId, String befDate, String aftDate, Long meterMngId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LoadSurveyResultSet> query = cb.createQuery(LoadSurveyResultSet.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getdate
        conditionList.add(cb.between(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), befDate, aftDate));
        // 条件2:devId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件3:meterMngId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        // 条件4:kwh30
        conditionList.add(cb.isNotNull(root.get(TDayLoadSurvey_.kwh30)));
        // 条件5:dmv_kwh
        conditionList.add(cb.isNotNull(root.get(TDayLoadSurvey_.dmvKwh)));

        query.select(cb.construct(LoadSurveyResultSet.class,
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate),
                root.get(TDayLoadSurvey_.kwh30),
                root.get(TDayLoadSurvey_.dmvKwh)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate)));

        List<LoadSurveyResultSet> retList = this.entityManager.createQuery(query).getResultList();
        if (retList == null) {
            retList = new ArrayList<>();
        }
        return retList;

    }

    /**
     * メーター取得（アラート停止状況を確認して、停止中でない件数を取得）
     *
     * @param String devId
     * @param Date currentDate
     * @return 対象メーター
     */
    public List<MMeter> getCountTargetList(String devId, Date currentDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = cb.createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件2:メーターdelflg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        // 条件3: アラートチェック（アラート停止をしていない条件）
        String strToday = DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD);

        conditionList.add(
                cb.or(
                        // アラート停止フラグ：0（停止しない）
                        cb.and(cb.equal(root.get(MMeter_.alertPauseFlg), OsolConstants.FLG_OFF)
                                ),

                        // アラート停止フラグ：１（停止する）、(アラート停止期間開始 > 現在日時) && (アラート停止期間終了 == null)
                        cb.and(
                                cb.equal(root.get(MMeter_.alertPauseFlg), OsolConstants.FLG_ON),
                                cb.greaterThan(root.get(MMeter_.alertPauseStart), strToday),
                                cb.isNull(root.get(MMeter_.alertPauseEnd))
                                ),

                        // アラート停止フラグ：１（停止する）、(アラート停止期間開始 > 現在日時) || (アラート停止期間終了 < 現在日時)
                        cb.and(
                                cb.equal(root.get(MMeter_.alertPauseFlg), OsolConstants.FLG_ON),
                                cb.or(
                                        cb.greaterThan(root.get(MMeter_.alertPauseStart), strToday),
                                        cb.lessThan(root.get(MMeter_.alertPauseEnd), strToday))
                                )
                        )
                );


        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<MMeter> ret = this.entityManager.createQuery(query).getResultList();
        return ret;

    }

    /**
     * Email宛先リスト取得
     *
     * @param
     * @return Email宛先リスト
     */
    public List<String> listEmailList() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);

        Root<MMailaddress> root = query.from(MMailaddress.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:disabledFlg
        conditionList.add(cb.equal(root.get(MMailaddress_.disabledFlg), "0"));
        conditionList.add(cb.isNotNull(root.get(MMailaddress_.email)));
        conditionList.add(cb.notEqual(root.get(MMailaddress_.email), ""));

        query.select(cb.construct(String.class,
                root.get(MMailaddress_.email)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<String> ret = this.entityManager.createQuery(query).getResultList();
        return ret;

    }

    /**
     * メーターから建物関係の情報を取得
     * @param corpId
     * @param buildingId
     * @param devId
     * @param meterMngIdList
     * @return
     */
    public List<MissngDataCsvResultSet> getMeterBuildingList(String corpId, Long buildingId, String devId, List<Long> meterMngIdList) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MissngDataCsvResultSet> query = cb.createQuery(MissngDataCsvResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);
        Join<MMeter, MDevPrm> joinMDevPrm = root.join(MMeter_.MDevPrm, JoinType.INNER);
        Join<MDevPrm, MDevRelation> joinMDevRelation = joinMDevPrm.join(MDevPrm_.MDevRelations, JoinType.INNER);
        Join<MDevRelation, TBuilding> joinTBuilding = joinMDevRelation.join(MDevRelation_.TBuilding, JoinType.INNER);
        Join<TBuilding, MCorp> joinMCorp = joinTBuilding.join(TBuilding_.MCorp, JoinType.INNER);


        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:corpId
        conditionList.add(cb.equal(joinMCorp.get(MCorp_.corpId), corpId));
        // 条件2:buildingId
        conditionList.add(cb.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
        // 条件3:devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件4:meterMngId
        conditionList.add(root.get(MMeter_.id).get(MMeterPK_.meterMngId).in(meterMngIdList));

        query.select(cb.construct(MissngDataCsvResultSet.class,
                joinMCorp.get(MCorp_.corpId),
                joinMCorp.get(MCorp_.corpName),
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingName),
                joinMDevPrm.get(MDevPrm_.devId),
                joinMDevPrm.get(MDevPrm_.name),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                root.get(MMeter_.meterType),
                root.get(MMeter_.meterId),
                root.get(MMeter_.meterSta),
                root.get(MMeter_.meterPresSitu),
                root.get(MMeter_.alertPauseStart),
                root.get(MMeter_.alertPauseEnd),
                root.get(MMeter_.meterStaMemo)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<MissngDataCsvResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

/*
    select
        tb.corp_id, mc.corp_name, tb.building_id, tb.building_name, mdp.dev_id, mdp.name, m.meter_mng_id, m.meter_type

    from
        m_meter m

    inner join
        m_dev_prm mdp
    on
        m.dev_id = mdp.dev_id

    inner join
        m_dev_relation mdr
    on
        mdp.dev_id = mdr.dev_id

    inner join
        t_building tb
    on
        mdr.corp_id = tb.corp_id
    and
        mdr.building_id = tb.building_id

    inner join
        m_corp mc
    on
        tb.corp_id = mc.corp_id

    where
        tb.corp_id = '?'
    and
        tb.building_id = '?
    and
        m.dev_id = '?'
    and
        m.meter_mng_id in ('?'...)

    order by
        m.dev_id asc, m.meter_mng_id asc
*/
    }

    /**
     * テナント情報関係を取得
     * @param corpId
     * @param buildingId
     * @param devId
     * @return
     */
    public List<MissngDataCsvResultSet> getMeterTenantList(String corpId, Long buildingId, String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MissngDataCsvResultSet> query = cb.createQuery(MissngDataCsvResultSet.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, TBuildDevMeterRelation> joinTBuildDevMeterRelation = root.join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:corpId
        conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 条件2:divisionCorpId
        conditionList.add(cb.equal(root.get(TBuilding_.divisionCorpId), corpId));
        // 条件3:divisionBuildingId
        conditionList.add(cb.equal(root.get(TBuilding_.divisionBuildingId), buildingId));
        // 条件4:devId
        conditionList.add(cb.equal(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));

        query.select(cb.construct(MissngDataCsvResultSet.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                root.get(TBuilding_.buildingName),
                joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId),
                joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.corpId)),
                        cb.asc(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId)),
                        cb.asc(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId)),
                        cb.asc(joinTBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId)));

        this.entityManager.clear();
        List<MissngDataCsvResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

/*
    select
        t.corp_id, t.building_id, t.building_name, tr.dev_id, tr.meter_mng_id

     from
         t_building t

    inner join
        t_build_dev_meter_relation tr
    on
        t.corp_id = tr.corp_id
    and
        t.building_id = tr.building_id

    where
        t.corp_id = '?'
    and
        t.division_corp_id = '?'
    and
        t.division_building_id= '?'
    and
        tr.dev_id = '?'

    order by
        tr.corp_id asc, tr.building_id asc, tr.meter_mng_id asc, tr.dev_id asc

*/
    }

    /**
     * メーター種別リスト
     * @param corpId
     * @param buildingId
     * @return
     */
    public List<MeterTypeResultSet> getMeterTypeList(String corpId, Long buildingId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MeterTypeResultSet> query = cb.createQuery(MeterTypeResultSet.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MMeterType> joinMMeterType = root.join(TBuilding_.MMeterTypes, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:corpId
        conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 条件2:buildingId
        conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query.select(cb.construct(MeterTypeResultSet.class,
                joinMMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                joinMMeterType.get(MMeterType_.meterTypeName)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .groupBy(joinMMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                        joinMMeterType.get(MMeterType_.meterTypeName))
                .orderBy(
                        cb.asc(joinMMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

        this.entityManager.clear();
        List<MeterTypeResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

/*
    select
        mmt.meter_type, mmt.meter_type_name

    from
        t_building t

    inner join
        m_meter_type mmt
    on
        t.corp_id = mmt.corp_id
    and
        t.building_id = mmt.building_id

    where
        t.corp_id = '?'
    and
        t.building_id = '?'

    group by
        mmt.meter_type, mmt.meter_type_name

    order by mmt.meter_type asc

*/
    }

    /**
     * Email一括送信宛先リスト取得
     *
     * @param   batchProcessCd
     * @return Email一括送信宛先リスト
     */
    public BatchSendEmailResultSet getBatchSendEmailList(String alertCd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BatchSendEmailResultSet> query = cb.createQuery(BatchSendEmailResultSet.class);

        Root<MAlertMailSetting> root = query.from(MAlertMailSetting.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:batchProcessCd
        conditionList.add(cb.equal(root.get(MAlertMailSetting_.alertCd), alertCd));

        query.select(cb.construct(BatchSendEmailResultSet.class,
                root.get(MAlertMailSetting_.mail1),
                root.get(MAlertMailSetting_.mail10),
                root.get(MAlertMailSetting_.mail2),
                root.get(MAlertMailSetting_.mail3),
                root.get(MAlertMailSetting_.mail4),
                root.get(MAlertMailSetting_.mail5),
                root.get(MAlertMailSetting_.mail6),
                root.get(MAlertMailSetting_.mail7),
                root.get(MAlertMailSetting_.mail8),
                root.get(MAlertMailSetting_.mail9)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();

        BatchSendEmailResultSet ret = this.entityManager.createQuery(query).getSingleResult();

        return ret;

    }

    /**
     * 当日欠損メールの送信フラグ取得
     * @param devId
     * @return
     */
    public MPauseMailResultSet getMissingDataSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.loadsurveyCsvErr)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).distinct(true);

        this.entityManager.clear();
        List<MPauseMailResultSet> sendFlg = this.entityManager.createQuery(query).getResultList();
        if(sendFlg == null || sendFlg.size() == 0) {
            return null;
        }else {
            return sendFlg.get(0);
        }
    }


}
