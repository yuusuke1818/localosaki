package jp.co.osaki.sms.batch.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

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
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterSvrResultSet;
import jp.co.osaki.sms.batch.resultset.BatchSendEmailResultSet;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.co.osaki.sms.batch.resultset.MeterTypeResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;
import jp.co.osaki.sms.batch.resultset.UnsettledMeterReadingDataCsvResultSet;

/**
 * SMS 未確定検針アラートメール送信 CSV添付 Daoクラス
 *
 * @author nishida.t
 *
 */
public class UnsettledMeterReadingDao extends SmsBatchDao {

    public UnsettledMeterReadingDao(EntityManager entityManager) {
        super(entityManager);
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
     * 未確定検針メール送信対象建物リスト取得
     *
     * @param
     * @return 未確定検針メール送信対象建物リスト
     */
    public List<MissingLoadSurveyListResultSet> getBuildingList() {
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
                .orderBy(
                        cb.asc(root.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

        this.entityManager.clear();
        List<MissingLoadSurveyListResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;
    }

    /**
     * 未確定検針メール送信対象装置リスト取得
     *
     * @param String corpId
     * @param Long   builingId
     * @return 未確定検針メール送信対象装置リスト
     */
    public List<CheckSurveyDevPrmResultSet> getDevPrmList(String corpId, Long builingId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CheckSurveyDevPrmResultSet> query = cb.createQuery(CheckSurveyDevPrmResultSet.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);
        Join<MDevPrm, MDevRelation> joinDevRel = root.join(MDevPrm_.MDevRelations, JoinType.INNER);

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
        // 条件6:装置delflg
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

    }

    /**
     * 月検針連番の最新値取得
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public Long getLatestMonthNo(String devId, String inspYear, String inspMonth, Long meterType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件4:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));

        // サブクエリ
        Subquery<MMeter> subquery = query.subquery(MMeter.class);
        Root<MMeter> root2 = subquery.from(MMeter.class);

        List<Predicate> subconditionList = new ArrayList<>();
        // 条件1:devId
        subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), root2.get(MMeter_.id).get(MMeterPK_.devId)));
        // 条件2:meterMngId
        subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), root2.get(MMeter_.id).get(MMeterPK_.meterMngId)));
        // 条件3:meterType
        subconditionList.add(cb.equal(root2.get(MMeter_.meterType), meterType));
        // 条件4:del_flg
        subconditionList.add(cb.equal(root2.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        subquery.select(root2);
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));
        conditionList.add(cb.exists(subquery));

        query.select(
                cb.max(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();

        return ret;
    }

    /**
     * 最新検針実行日時の取得
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public Timestamp getLatestInspDate(String devId, Long meterType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Timestamp> query = cb.createQuery(Timestamp.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));

        // サブクエリ
        Subquery<MMeter> subquery = query.subquery(MMeter.class);
        Root<MMeter> root2 = subquery.from(MMeter.class);

        List<Predicate> subconditionList = new ArrayList<>();
        // 条件1:devId
        subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), root2.get(MMeter_.id).get(MMeterPK_.devId)));
        // 条件2:meterMngId
        subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), root2.get(MMeter_.id).get(MMeterPK_.meterMngId)));
        // 条件3:meterType
        subconditionList.add(cb.equal(root2.get(MMeter_.meterType), meterType));
        // 条件4:del_flg
        subconditionList.add(cb.equal(root2.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        subquery.select(root2);
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));
        conditionList.add(cb.exists(subquery));

        query.select(
                cb.greatest(root.get(TInspectionMeterSvr_.latestInspDate)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Timestamp ret = this.entityManager.createQuery(query).getSingleResult();

        return ret;
    }

    /**
     * 未完了の検針結果リスト取得（使用していない）
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public List<AutoInspMeterSvrResultSet> getUnsettledMeterReadingList(String devId, String inspYear, String inspMonth) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterSvrResultSet> query = cb.createQuery(AutoInspMeterSvrResultSet.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();

        // サブクエリ
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<TInspectionMeterSvr> root2 = subquery.from(TInspectionMeterSvr.class);

        List<Predicate> subconditionList = new ArrayList<>();
        // 条件1:devId
        subconditionList.add(cb.equal(root2.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        subconditionList.add(cb.equal(root2.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        subconditionList.add(cb.equal(root2.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件4:inspType（a:自動検針）
        subconditionList.add(cb.equal(root2.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));

        subquery.select(cb.max(root2.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));

        // メインクエリ where
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件4:inspMonthNo
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), subquery));
        // 条件5:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));
        // 条件6:endFlg（0:未完了）
        conditionList.add(cb.or(cb.isNull(root.get(TInspectionMeterSvr_.endFlg)), cb.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        query.select(cb.construct(AutoInspMeterSvrResultSet.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                root.get(TInspectionMeterSvr_.inspType),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                root.get(TInspectionMeterSvr_.endFlg),
                root.get(TInspectionMeterSvr_.latestInspDate),
                root.get(TInspectionMeterSvr_.prevInspDate),
                root.get(TInspectionMeterSvr_.latestInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal2)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)),
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspMeterSvrResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * 月検針連番の最新値取得（使用していない）
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public Long getLatestMonthNo(String devId, String inspYear, String inspMonth) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件4:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));

        query.select(
                cb.max(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();

        return ret;
    }

    /**
     * 未完了の検針結果リスト取得
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public List<AutoInspMeterSvrResultSet> getUnsettledMeterReadingList(String devId, String inspYear, String inspMonth, long inspMonthNo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterSvrResultSet> query = cb.createQuery(AutoInspMeterSvrResultSet.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件4:inspMonthNo
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), inspMonthNo));
        // 条件5:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));
        // 条件6:endFlg（0:未完了）
        conditionList.add(cb.or(cb.isNull(root.get(TInspectionMeterSvr_.endFlg)), cb.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        query.select(cb.construct(AutoInspMeterSvrResultSet.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                root.get(TInspectionMeterSvr_.inspType),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                root.get(TInspectionMeterSvr_.endFlg),
                root.get(TInspectionMeterSvr_.latestInspDate),
                root.get(TInspectionMeterSvr_.prevInspDate),
                root.get(TInspectionMeterSvr_.latestInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal2)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)),
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspMeterSvrResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * 未完了の検針結果リスト取得
     * @param devId
     * @param inspYear
     * @param inspMonth
     * @return
     */
    public List<AutoInspMeterSvrResultSet> getUnsettledMeterReadingList(String devId, Timestamp latestInspDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterSvrResultSet> query = cb.createQuery(AutoInspMeterSvrResultSet.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:latestInspDate
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.latestInspDate), latestInspDate));
        // 条件3:inspType（a:自動検針）
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));
        // 条件4:endFlg（0:未完了）
        conditionList.add(cb.or(cb.isNull(root.get(TInspectionMeterSvr_.endFlg)), cb.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        query.select(cb.construct(AutoInspMeterSvrResultSet.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId),
                root.get(TInspectionMeterSvr_.inspType),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth),
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo),
                root.get(TInspectionMeterSvr_.endFlg),
                root.get(TInspectionMeterSvr_.latestInspDate),
                root.get(TInspectionMeterSvr_.prevInspDate),
                root.get(TInspectionMeterSvr_.latestInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal),
                root.get(TInspectionMeterSvr_.prevInspVal2)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)),
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspMeterSvrResultSet> ret = this.entityManager.createQuery(query).getResultList();

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
    public List<UnsettledMeterReadingDataCsvResultSet> getMeterBuildingList(String corpId, Long buildingId, String devId, List<Long> meterMngIdList) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnsettledMeterReadingDataCsvResultSet> query = cb.createQuery(UnsettledMeterReadingDataCsvResultSet.class);

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

        query.select(cb.construct(UnsettledMeterReadingDataCsvResultSet.class,
                joinMCorp.get(MCorp_.corpId),
                joinMCorp.get(MCorp_.corpName),
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingName),
                joinMDevPrm.get(MDevPrm_.devId),
                joinMDevPrm.get(MDevPrm_.name),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                root.get(MMeter_.meterType),
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
        List<UnsettledMeterReadingDataCsvResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * テナント情報関係を取得
     * @param corpId
     * @param buildingId
     * @param devId
     * @return
     */
    public List<UnsettledMeterReadingDataCsvResultSet> getMeterTenantList(String corpId, Long buildingId, String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnsettledMeterReadingDataCsvResultSet> query = cb.createQuery(UnsettledMeterReadingDataCsvResultSet.class);

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

        query.select(cb.construct(UnsettledMeterReadingDataCsvResultSet.class,
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
        List<UnsettledMeterReadingDataCsvResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

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
     * 通知フラグ取得
     * @param devId
     * @return
     */
    public MPauseMailResultSet getUnsettledSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.unsettledCsvErr)))
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
