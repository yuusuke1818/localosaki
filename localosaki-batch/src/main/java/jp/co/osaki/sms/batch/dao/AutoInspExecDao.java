package jp.co.osaki.sms.batch.dao;

import java.math.BigDecimal;
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
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK_;
import jp.co.osaki.osol.entity.MAlertMail_;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MGroupPrice;
import jp.co.osaki.osol.entity.MGroupPricePK_;
import jp.co.osaki.osol.entity.MGroupPrice_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK_;
import jp.co.osaki.osol.entity.MMeterGroup_;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.MPauseMail_;
import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.co.osaki.osol.entity.MPriceMenuLightaPK_;
import jp.co.osaki.osol.entity.MPriceMenuLighta_;
import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.co.osaki.osol.entity.MPriceMenuLightbPK_;
import jp.co.osaki.osol.entity.MPriceMenuLightb_;
import jp.co.osaki.osol.entity.MTenantPriceInfo;
import jp.co.osaki.osol.entity.MTenantPriceInfoPK_;
import jp.co.osaki.osol.entity.MTenantPriceInfo_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK_;
import jp.co.osaki.osol.entity.MUnitPrice_;
import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.MVarious_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.co.osaki.osol.entity.TWkInspectionIncomp;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.AutoInspExecResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspLoadSurveyResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterSvrResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspPriceCalcInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspTenantInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspUnitPriceResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;

/**
 * SMS 自動検針 Daoクラス
 *
 * @author tominaga
 *
 */
public class AutoInspExecDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public AutoInspExecDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 自動検針対象データを取得
     *
     * @return 自動検針対象データリスト
     */
    public List<AutoInspExecResultSet> getAutoInsp() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspExecResultSet> query = cb.createQuery(AutoInspExecResultSet.class);

        Root<MAutoInsp> root = query.from(MAutoInsp.class);
        Join<MAutoInsp, MDevPrm> joinAutoInspDevPrm = root
                .join(MAutoInsp_.MDevPrm, JoinType.INNER);
        Join<MDevPrm, MDevRelation> joinDevPrmDevRel = joinAutoInspDevPrm
                .join(MDevPrm_.MDevRelations, JoinType.INNER);
        Join<MDevRelation, TBuilding> joinDevRelBuilding = joinDevPrmDevRel
                .join(MDevRelation_.TBuilding, JoinType.INNER);
        Join<TBuilding, MBuildingSms> joinBuildingSms = joinDevRelBuilding
                .join(TBuilding_.MBuildingSms, JoinType.INNER);
        Join<TBuilding, MMeterType> joinBuildingMeterType = joinDevRelBuilding
                .join(TBuilding_.MMeterTypes, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:DEV_KIND=1：MUDM2
        conditionList.add(cb.equal(joinAutoInspDevPrm.get(MDevPrm_.devKind), SmsBatchConstants.DEV_KIND.MUDM2.getVal()));
        // 条件2:DEV_ID='MH*'以外
        conditionList.add(cb.notLike(joinAutoInspDevPrm.get(MDevPrm_.devId), "MH%"));
        // 条件3:DEV_ID='OC*'以外
        conditionList.add(cb.notLike(joinAutoInspDevPrm.get(MDevPrm_.devId), "OC%"));
        // 条件4:autoinsp.metertype=metertype.metertype
        conditionList.add(cb.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.meterType), joinBuildingMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));
        // 条件5:装置del_flg
        conditionList.add(cb.equal(joinAutoInspDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));
        // 条件6:建物del_flg
        conditionList.add(cb.equal(joinDevRelBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // 条件7:SMS建物del_flg
        conditionList.add(cb.equal(joinBuildingSms.get(MBuildingSms_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(AutoInspExecResultSet.class,
                joinDevRelBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinAutoInspDevPrm.get(MDevPrm_.devId),
                joinBuildingMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                joinBuildingMeterType.get(MMeterType_.meterTypeName),
                root.get(MAutoInsp_.month),
                root.get(MAutoInsp_.day),
                root.get(MAutoInsp_.hour),
                root.get(MAutoInsp_.waitTime),
                cb.coalesce(joinBuildingSms.get(MBuildingSms_.chkInt), BigDecimal.ZERO)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(joinDevRelBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)),
                        cb.asc(joinAutoInspDevPrm.get(MDevPrm_.devId)),
                        cb.asc(joinBuildingMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

        this.entityManager.clear();
        List<AutoInspExecResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;
    }

//    public void test()
//    {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<MMeter> query = cb.createQuery(MMeter.class);
//
//        Root<MMeter> root = query.from(MMeter.class);
//        query.select(root);
//        this.entityManager.clear();
//        List<MMeter> ret = this.entityManager.createQuery(query).getResultList();
//
//    }
    /**
     * 自動検針対象装置IDの登録メータ取得
     *
     * @param devId 装置ID
     * @param meterType メーター種別
     * @return メータ登録リスト
     */
    public List<AutoInspMeterResultSet> getAutoInspMeter(String devId, Long meterType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterResultSet> query = cb.createQuery(AutoInspMeterResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件2:meterType
        conditionList.add(cb.equal(root.get(MMeter_.meterType), meterType));
        // 条件3:srvEnt
        conditionList.add(cb.or(cb.isNull(root.get(MMeter_.srvEnt)), cb.equal(root.get(MMeter_.srvEnt), "")));
        // 条件4:del_flg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(AutoInspMeterResultSet.class,
                root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                root.get(MMeter_.srvEnt),
                root.get(MMeter_.multi),
                root.get(MMeter_.meterId)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspMeterResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;
    }

    /**
     * 自動検針結果リスト取得
     *
     * @param devId 装置ID
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @return 自動検針結果リスト
     */
    public List<AutoInspMeterSvrResultSet> getAutoInspMeterSvr(String devId, String inspYear, String inspMonth) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterSvrResultSet> query = cb.createQuery(AutoInspMeterSvrResultSet.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件3:inspMonth
        inspMonth = Integer.valueOf(inspMonth).toString();
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));

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
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId)),
                        cb.asc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspMeterSvrResultSet> ret = this.entityManager.createQuery(query).getResultList();
        return ret;
    }

    /**
     * 日報データ取得
     *
     * @param devId 装置ID
     * @param targetDate 自動検針実施日
     * @param meterType メーター種別
     * @param srvEntEna Boolean
     * @return 日報データリスト
     */
    public List<AutoInspLoadSurveyResultSet> getAutoInspLoadSurvey(String devId, String targetDate, Long meterType, Boolean srvEntEna) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspLoadSurveyResultSet> query = cb.createQuery(AutoInspLoadSurveyResultSet.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        // サブクエリ
        Subquery<MMeter> subquery = query.subquery(MMeter.class);
        Root<MMeter> root2 = subquery.from(MMeter.class);
        subquery.select(root2);
        List<Predicate> subconditionList = new ArrayList<>();
        // 条件1:devId
        subconditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), root2.get(MMeter_.id).get(MMeterPK_.devId)));
        // 条件2:meterMngId
        subconditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), root2.get(MMeter_.id).get(MMeterPK_.meterMngId)));
        if (meterType != null) {
            // 条件3:meterType
            subconditionList.add(cb.equal(root2.get(MMeter_.meterType), meterType));
        }
        if (srvEntEna) {
            // 条件4:srv_ent
            subconditionList.add(cb.or(cb.isNull(root2.get(MMeter_.srvEnt)), cb.equal(root2.get(MMeter_.srvEnt), "")));
        }
        // 条件5:del_flg
        subconditionList.add(cb.equal(root2.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        if (targetDate != null && targetDate.length() != 0) {
            // 条件2:getDate
            conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), targetDate));
        }
        // 条件3:subquery
        conditionList.add(cb.exists(subquery));

        query.select(cb.construct(AutoInspLoadSurveyResultSet.class,
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate),
                root.get(TDayLoadSurvey_.kwh30),
                root.get(TDayLoadSurvey_.dmvKwh)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId)),
                        cb.asc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspLoadSurveyResultSet> ret = this.entityManager.createQuery(query).getResultList();
        return ret;
    }

    /**
     * 未完了日報データリスト取得１
     *
     * @param devId 装置ID
     * @param meterType メーター種別
     * @param targetDate 自動検針実施日
     * @return 未完了日報データリスト
     */
    public List<Long> listInCompLoadSurvey1(String devId, Long meterType, String targetDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<MMeter> root = query.from(MMeter.class);

        // サブクエリ
        Subquery<TDayLoadSurvey> subquery = query.subquery(TDayLoadSurvey.class);
        Root<TDayLoadSurvey> root2 = subquery.from(TDayLoadSurvey.class);
        subquery.select(root2);
        List<Predicate> subconditionList = new ArrayList<>();
        // 条件 tdayloadsuryvey.getDate=targetDate
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), targetDate));
        // 条件 tdayloadsuryvey.dmv_kwh=null
        subconditionList.add(cb.isNull(root2.get(TDayLoadSurvey_.dmvKwh)));
        // 条件 tdayloadsuryvey.devId=mmeter.devId
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), root.get(MMeter_.id).get(MMeterPK_.devId)));
        // 条件 mmeter.metermngid=tdayloadsuryvey.metermngid
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), root.get(MMeter_.id).get(MMeterPK_.meterMngId)));
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));

        List<Predicate> conditionList = new ArrayList<>();
        // 条件 mmeter.devId=devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件 mmeter.metertype=meterType
        conditionList.add(cb.equal(root.get(MMeter_.meterType), meterType));
        // 条件 mmeter.srvEnt=null or mmeter.srvEnt=""
        conditionList.add(cb.or(cb.isNull(root.get(MMeter_.srvEnt)), cb.equal(root.get(MMeter_.srvEnt), "")));
        // 条件 del_flg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        // 条件 subquery
        conditionList.add(cb.exists(subquery));

        query.select(cb.construct(Long.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<Long> ret = this.entityManager.createQuery(query).getResultList();
        return ret;
    }

    /**
     * 未完了日報データリスト取得２
     *
     * @param devId 装置ID
     * @param meterType メーター種別
     * @param targetDate 自動検針実施日
     * @return 未完了日報データリスト
     */
    public List<Long> listInCompLoadSurvey2(String devId, Long meterType, String targetDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<MMeter> root = query.from(MMeter.class);

        // サブクエリ
        Subquery<TDayLoadSurvey> subquery = query.subquery(TDayLoadSurvey.class);
        Root<TDayLoadSurvey> root2 = subquery.from(TDayLoadSurvey.class);
        subquery.select(root2);
        List<Predicate> subconditionList = new ArrayList<>();
        // 条件 tdayloadsuryvey.devId=devId
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件 getDate=targetDate
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), targetDate));
        // 条件 mmeter.metermngid=tdayloadsuryvey.metermngid
        subconditionList.add(cb.equal(root2.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), root.get(MMeter_.id).get(MMeterPK_.meterMngId)));
        subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));

        List<Predicate> conditionList = new ArrayList<>();
        // 条件 mmeter.devId=devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件 mmeter.metertype=meterType
        conditionList.add(cb.equal(root.get(MMeter_.meterType), meterType));
        // 条件 mmeter.srvEnt=null or mmeter.srvEnt=""
        conditionList.add(cb.or(cb.isNull(root.get(MMeter_.srvEnt)), cb.equal(root.get(MMeter_.srvEnt), "")));
        // 条件 del_flg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        // 条件 subquery
        conditionList.add(cb.not(cb.exists(subquery)));

        query.select(cb.construct(Long.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<Long> ret = this.entityManager.createQuery(query).getResultList();
        return ret;
    }

    /**
     * コマンド件数取得
     *
     * @param devId 装置ID
     * @param targetDate 自動検針実施日
     * @param command コマンド
     * @return コマンド件数
     */
    public Long getCountCommand(String devId, String targetDate, String command) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        if (command != null && command.length() != 0) {
            // 条件3:command
            conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        }
        if (targetDate != null && targetDate.length() != 0) {
            // 条件4:tag
            conditionList.add(cb.equal(root.get(TCommand_.tag), targetDate));
        }
        // 条件5:srvEnt
        conditionList.add(cb.or(cb.equal(root.get(TCommand_.srvEnt), "1"), cb.equal(root.get(TCommand_.srvEnt), "2")));

        query.select(cb.count(root))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;
    }

    /**
     * コマンド削除
     *
     * @param devId 装置ID
     * @param targetDate 自動検針実施日
     * @param command コマンド
     * @return Integer
     */
    public void removeCommand(String devId, String targetDate, String command) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TCommand> delete = cb.createCriteriaDelete(TCommand.class);

        Root<TCommand> root = delete.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        if (command != null && command.length() != 0) {
            // 条件3:command
            conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        }
        if (targetDate != null && targetDate.length() != 0) {
            // 条件4:tag
            conditionList.add(cb.equal(root.get(TCommand_.tag), targetDate));
        }
//        // 条件5:srvEnt
//        conditionList.add(cb.or(cb.equal(root.get(TCommand_.srvEnt), "1"), cb.equal(root.get(TCommand_.srvEnt), "2")));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     *
     * コマンドの追加
     *
     * @param devId 装置ID
     * @param targetDate 自動検針実施日
     * @param command コマンド
     * @param svDate
     * @param inspType 検針種別  SmsBatchConstants.INSP_KIND.AUTO.getVal() または SmsBatchConstants.INSP_KIND.SCHEDULE.getVal()
     */
    public void createCommand(String devId, String targetDate, String command, Timestamp svDate, String inspType) {
        TCommand t = new TCommand();
        TCommandPK pk = new TCommandPK();

        pk.setDevId(devId);
        pk.setCommand(command);
        pk.setRecDate(svDate);

        t.setId(pk);
        t.setSrvEnt("1");
        if (SmsBatchConstants.INSP_KIND.SCHEDULE.getVal().equals(inspType)) {
            t.setRecMan("makeScheduleInspData");
        } else {
            t.setRecMan("makeAutoInspData");
        }
        t.setTag(targetDate);

        t.setCreateUserId(Long.valueOf(0));
        t.setCreateDate(svDate);
        t.setUpdateUserId(Long.valueOf(0));
        t.setUpdateDate(svDate);
        entityManager.persist(t);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 自動検針結果取得
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 検針月連番
     * @return 検索結果 1件
     */
    public TInspectionMeterSvr findAutoInspMeterSvr(String devId, Long meterMngId, String inspYear, String inspMonth, Long inspMonthNo) {

        inspMonth = Integer.valueOf(inspMonth).toString(); // ゼロサプレス

        TInspectionMeterSvrPK targetPK = new TInspectionMeterSvrPK();
        targetPK.setDevId(devId);
        targetPK.setMeterMngId(meterMngId);
        targetPK.setInspYear(inspYear);
        targetPK.setInspMonth(inspMonth);
        targetPK.setInspMonthNo(inspMonthNo);
        return entityManager.find(TInspectionMeterSvr.class, targetPK);
    }

    /**
     * 検針結果取得(検針種別ごと)
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspType 検針種別
     * @return 検索結果 1件
     */
    public List<TInspectionMeterSvr> findInspMeterSvrByInspType(String devId, Long meterMngId, String inspYear, String inspMonth, String inspType) {

        inspMonth = Integer.valueOf(inspMonth).toString(); // ゼロサプレス

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();

        //装置ID
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        //メーター管理番号
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        //表示年
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        //表示月
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        //検針タイプ
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), inspType));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * 自動検針結果削除
     *
     * @param devId 装置ID
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param meterMngId メーター管理番号
     * @param inspMonthNo 検針月連番
     * @return
     */
    public void removeAutoInspMeterSvr(String devId, String inspYear, String inspMonth, Long meterMngId, Long inspMonthNo) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TInspectionMeterSvr> delete = cb.createCriteriaDelete(TInspectionMeterSvr.class);

        Root<TInspectionMeterSvr> root = delete.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:meterMngId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        // 条件3:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件4:inspMonth
        inspMonth = Integer.valueOf(inspMonth).toString();
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件5:inspMonthNo
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), inspMonthNo));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    /**
     *
     * 自動検針結果(未完了)の追加
     *
     * @param t TWkInspectionIncomp
     */
    public void createIncomplateAutoInspMeterSvr(TWkInspectionIncomp t) {

        entityManager.persist(t);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     *
     * 自動検針結果の追加
     *
     * @param t TInspectionMeterSvr
     */
    public void createAutoInspMeterSvr(TInspectionMeterSvr t) {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 自動検針結果(最新)
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @return 最新の自動検針結果
     */
    public AutoInspMeterSvrResultSet getAutoInspLatestResult(String devId, Long meterMngId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspMeterSvrResultSet> query = cb.createQuery(AutoInspMeterSvrResultSet.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:meterMngId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        // 条件3:endFlg
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE));

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
                        cb.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear)),
                        cb.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth).as(Integer.class)),
                        cb.desc(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo)));

        this.entityManager.clear();
        List<AutoInspMeterSvrResultSet> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();

        AutoInspMeterSvrResultSet ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0);
        }
        return ret;
    }

    /**
     * 日報データからDMV値取得
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param targetDate 自動検針実施日
     * @return 指針値
     */
    public BigDecimal getLoadSurveyDMVKWH(String devId, Long meterMngId, String targetDate) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspLoadSurveyResultSet> query = cb.createQuery(AutoInspLoadSurveyResultSet.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件2:meterMngId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        // 条件3:getDate
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), targetDate));

        query.select(cb.construct(AutoInspLoadSurveyResultSet.class,
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId),
                root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate),
                root.get(TDayLoadSurvey_.kwh30),
                root.get(TDayLoadSurvey_.dmvKwh)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AutoInspLoadSurveyResultSet> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        BigDecimal ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0).getDmvkwh();
        }
        return ret;
    }

    /**
     * 自動検針未完了数取得
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param meterType メーター種別
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 検針月連番
     * @return 自動検針未完了件数
     */
    public Long getCountIncomplate(String devId, Long meterMngId, Long meterType, String inspYear, String inspMonth, String inspMonthNo) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:meterMngId
        if (meterMngId != null) {
            conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        }
        // 条件4:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件5:inspMonth
        inspMonth = Integer.valueOf(inspMonth).toString();
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件6:inspMonthNo
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), inspMonthNo));
        // 条件7:endFlg
        conditionList.add(cb.or(cb.isNull(root.get(TInspectionMeterSvr_.endFlg)), cb.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        // 条件8:meterType
        if (meterType != null) {
            // サブクエリ
            Subquery<MMeter> subquery = query.subquery(MMeter.class);
            Root<MMeter> root2 = subquery.from(MMeter.class);
            subquery.select(root2);
            List<Predicate> subconditionList = new ArrayList<>();
            // 条件1:devId
            subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), root2.get(MMeter_.id).get(MMeterPK_.devId)));
            // 条件2:meterMngId
            subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), root2.get(MMeter_.id).get(MMeterPK_.meterMngId)));
            // 条件3:meterType
            subconditionList.add(cb.equal(root2.get(MMeter_.meterType), meterType));
            // 条件4:del_flg
            subconditionList.add(cb.equal(root2.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
            subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));
            conditionList.add(cb.exists(subquery));

        }

        query.select(cb.count(root))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;
    }

    /**
     * 自動検針未完了数取得
     *
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param meterType メーター種別
     * @param inspYear 検針年
     * @param inspMonth 検針月
     * @param inspMonthNo 検針月連番
     * @return 自動検針未完了件数
     */
    public List<Long> getListIncomplate(String devId, Long meterMngId, Long meterType, String inspYear, String inspMonth, String inspMonthNo) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        // 条件2:meterMngId
        if (meterMngId != null) {
            conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        }
        // 条件4:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        // 条件5:inspMonth
        inspMonth = Integer.valueOf(inspMonth).toString();
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        // 条件6:inspMonthNo
        conditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonthNo), inspMonthNo));
        // 条件7:endFlg
        conditionList.add(cb.or(cb.isNull(root.get(TInspectionMeterSvr_.endFlg)), cb.notEqual(root.get(TInspectionMeterSvr_.endFlg), BigDecimal.ONE)));

        // 条件8:meterType
        if (meterType != null) {
            // サブクエリ
            Subquery<MMeter> subquery = query.subquery(MMeter.class);
            Root<MMeter> root2 = subquery.from(MMeter.class);
            subquery.select(root2);
            List<Predicate> subconditionList = new ArrayList<>();
            // 条件1:devId
            subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), root2.get(MMeter_.id).get(MMeterPK_.devId)));
            // 条件2:meterMngId
            subconditionList.add(cb.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), root2.get(MMeter_.id).get(MMeterPK_.meterMngId)));
            // 条件3:meterType
            subconditionList.add(cb.equal(root2.get(MMeter_.meterType), meterType));
            // 条件4:del_flg
            subconditionList.add(cb.equal(root2.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
            subquery.where(cb.and(subconditionList.toArray(new Predicate[] {})));
            conditionList.add(cb.exists(subquery));

        }

        query.select(cb.construct(Long.class,
                root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<Long> ret = this.entityManager.createQuery(query).getResultList();
        return ret;
    }

    /**
     * 指定された装置IDのアラートメール通知先リストを取得する。
     *
     * @param devId 装置ID
     * @return アラートメール通知先リスト
     */
    public List<MAlertMailListResultSet> getMAlertMail(String devId, Boolean loadSurvey) {
        // アラートメール設定を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MAlertMailListResultSet> query = builder.createQuery(MAlertMailListResultSet.class);

        Root<MAlertMail> root = query.from(MAlertMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MAlertMail_.id).get(MAlertMailPK_.devId), devId));
        // 条件2: ロードサーベイ欠損通知が有効
        if (loadSurvey) {
            conditionList.add(builder.equal(root.get(MAlertMail_.alertLoadsurveyErr), OsolConstants.FLG_ON.toString()));
        }
        // 条件3: 未使用フラグが有効でない
        conditionList.add(builder.notEqual(root.get(MAlertMail_.disabledFlg), OsolConstants.FLG_ON.toString()));

        query.select(builder.construct(MAlertMailListResultSet.class, root.get(MAlertMail_.email)))
                .distinct(true)
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).distinct(true);

        this.entityManager.clear();
        List<MAlertMailListResultSet> tMAlertMailList = this.entityManager.createQuery(query).getResultList();

        return tMAlertMailList;
    }

    /**
     * 料金計算対象リスト取得
     *
     * @param
     * @return 料金計算対象リスト
     */
    public List<AutoInspPriceCalcInfoResultSet> listPriceCalc() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspPriceCalcInfoResultSet> query = cb.createQuery(AutoInspPriceCalcInfoResultSet.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MDevRelation> joinBuildDevRel = root
                .join(TBuilding_.MDevRelations, JoinType.INNER);
        Join<MDevRelation, MDevPrm> joinDevPrm = joinBuildDevRel
                .join(MDevRelation_.MDevPrm, JoinType.INNER);
        Join<MDevPrm, MMeter> joinMeter = joinDevPrm
                .join(MDevPrm_.MMeters, JoinType.INNER);
        Join<MMeter, MMeterGroup> joinMeterGroup = joinMeter
                .join(MMeter_.MMeterGroups, JoinType.INNER);
        Join<TBuilding, MMeterType> joinMeterType = root
                .join(TBuilding_.MMeterTypes, JoinType.INNER);
        Join<TBuilding, MVarious> joinVarious = root
                .join(TBuilding_.MVariouses, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:meter.meterType=meterType.meterType
        conditionList.add(cb.equal(joinMeter.get(MMeter_.meterType), joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));
        // 条件3:建物del_flg
        conditionList.add(cb.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // 条件4:メータdel_flg
        conditionList.add(cb.equal(joinMeter.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(AutoInspPriceCalcInfoResultSet.class,
                joinMeterGroup.get(MMeterGroup_.id).get(MMeterGroupPK_.meterGroupId),
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinMeter.get(MMeter_.id).get(MMeterPK_.devId),
                joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                joinMeter.get(MMeter_.id).get(MMeterPK_.meterMngId),
                joinMeterType.get(MMeterType_.autoInspDay),
                joinMeterGroup.get(MMeterGroup_.calcType),
                joinVarious.get(MVarious_.decimalFraction)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(joinMeterGroup.get(MMeterGroup_.id).get(MMeterGroupPK_.meterGroupId)),
                        cb.asc(joinMeter.get(MMeter_.id).get(MMeterPK_.devId)),
                        cb.asc(joinMeter.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        this.entityManager.clear();
        List<AutoInspPriceCalcInfoResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * グループ金額からの最新日時取得
     *
     * @param Long meter_group_id
     * @return 最新日時文字列(YYYYMM)
     */
    public String getMaxDate(Long meter_group_id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);

        Root<MGroupPrice> root = query.from(MGroupPrice.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:meter_group_id
        conditionList.add(cb.equal(root.get(MGroupPrice_.id).get(MGroupPricePK_.meterGroupId), meter_group_id));

        query.select(cb.greatest(root.get(MGroupPrice_.id).get(MGroupPricePK_.latestInspDate)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<String> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        String ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0);
        }
        return ret;
    }

    /**
     * ロードサーベイから日付範囲内のカウントを取得
     *
     * @param String devId
     * @param Long   meter_mng_id
     * @param String start_date_Time
     * @param String end_date_time
     * @return カウント数
     */
    public Long getCountLoadSurvey(String devId, Long meter_mng_id, String start_date_Time, String end_date_time) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meter_mng_id));
        conditionList.add(cb.between(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), start_date_Time, end_date_time));

        query.select(cb.count(root))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;
    }

    /**
     * テナント情報取得
     *
     * @param String devId
     * @param Long   meterMngId
     * @param Long   meterType
     * @return テナント情報
     */
    public AutoInspTenantInfoResultSet getTenantInfo(String devId, Long meterMngId, Long meterType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspTenantInfoResultSet> query = cb.createQuery(AutoInspTenantInfoResultSet.class);

        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, TBuildDevMeterRelation> joinBuildMeterRel = root
                .join(TBuilding_.TBuildDevMeterRelations, JoinType.INNER);
        Join<TBuildDevMeterRelation, MMeter> joinMeter = joinBuildMeterRel
                .join(TBuildDevMeterRelation_.MMeter, JoinType.INNER);
        Join<TBuilding, MTenantSm> joinTenantSms = root
                .join(TBuilding_.MTenantSms, JoinType.INNER);
        Join<TBuilding, MTenantPriceInfo> joinTenantPriceInfo = root
                .join(TBuilding_.MTenantPriceInfos, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(cb.equal(joinBuildMeterRel.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));
        conditionList.add(cb.equal(joinBuildMeterRel.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId), meterMngId));
        conditionList.add(cb.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.meterType), meterType));
        // 条件3:建物del_flg
        conditionList.add(cb.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        // 条件4:メータdel_flg
        conditionList.add(cb.equal(joinMeter.get(MMeter_.delFlg), OsolConstants.FLG_OFF));
        // 条件5:テナントdel_flg
        conditionList.add(cb.equal(joinTenantSms.get(MTenantSm_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(AutoInspTenantInfoResultSet.class,
                joinTenantSms.get(MTenantSm_.contractCapacity),
                joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.powerPlanId),
                joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.pricePlanId),
                joinTenantPriceInfo.get(MTenantPriceInfo_.basicPrice),
                joinTenantPriceInfo.get(MTenantPriceInfo_.discountRate)))
                .distinct(true)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AutoInspTenantInfoResultSet> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        AutoInspTenantInfoResultSet ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0);
        }

        return ret;

    }

    /**
     * Price情報リスト取得
     *
     * @param Long price_plan_id
     * @return Price情報リスト
     */
    public List<AutoInspUnitPriceResultSet> listUnitPrice(Long price_plan_id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspUnitPriceResultSet> query = cb.createQuery(AutoInspUnitPriceResultSet.class);

        Root<MUnitPrice> root = query.from(MUnitPrice.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:meter_group_id
        conditionList.add(cb.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), price_plan_id));

        query.select(cb.construct(AutoInspUnitPriceResultSet.class,
                root.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal),
                root.get(MUnitPrice_.unitPrice)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal)));

        this.entityManager.clear();
        List<AutoInspUnitPriceResultSet> ret = this.entityManager.createQuery(query).getResultList();
        return ret;

    }

    /**
     * PriceMenuLightA情報取得
     *
     * @param Long buildingId
     * @return PriceMenuLightA情報
     */
    public MPriceMenuLighta getPriceMenuLightA(Long buildingId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MPriceMenuLighta> query = cb.createQuery(MPriceMenuLighta.class);

        Root<MPriceMenuLighta> root = query.from(MPriceMenuLighta.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:meter_group_id
        conditionList.add(cb.equal(root.get(MPriceMenuLighta_.id).get(MPriceMenuLightaPK_.buildingId), buildingId));

        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));
        this.entityManager.clear();
        List<MPriceMenuLighta> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        MPriceMenuLighta ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0);
        }
        return ret;
    }

    /**
     * PriceMenuLightB情報取得
     *
     * @param Long buildingId
     * @return PriceMenuLightA情報
     */
    public MPriceMenuLightb getPriceMenuLightB(Long buildingId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MPriceMenuLightb> query = cb.createQuery(MPriceMenuLightb.class);

        Root<MPriceMenuLightb> root = query.from(MPriceMenuLightb.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:meter_group_id
        conditionList.add(cb.equal(root.get(MPriceMenuLightb_.id).get(MPriceMenuLightbPK_.buildingId), buildingId));

        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));
        this.entityManager.clear();
        List<MPriceMenuLightb> retList = this.entityManager.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        MPriceMenuLightb ret = null;
        if (retList.size() != 0) {
            ret = retList.get(0);
        }
        return ret;
    }

    /**
     *
     * 自動検針結果の追加
     *
     * @param MGroupPrice m
     */
    public void insertGroupPrice(MGroupPrice m) {
        entityManager.persist(m);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 完了通知時のメールフラグを取得
     * @param devId
     * @return
     */
    public MPauseMailResultSet getCompSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.autoinspComp)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).distinct(true);

        this.entityManager.clear();
        List<MPauseMailResultSet> sendFlg = this.entityManager.createQuery(query).getResultList();
        if(sendFlg == null || sendFlg.size() == 0) {
            return null;
        }else {
            return sendFlg.get(0);
        }
    }

    /**
     * 欠損通知時のメールフラグを取得
     * @param devId
     * @return
     */
    public MPauseMailResultSet getMissingSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.autoinspMissing)))
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
