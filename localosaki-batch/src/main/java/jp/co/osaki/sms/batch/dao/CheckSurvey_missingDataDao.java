package jp.co.osaki.sms.batch.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
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
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMailaddress;
import jp.co.osaki.osol.entity.MMailaddress_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;

/**
 * SMS 日報収集データセット（定期処理） Daoクラス
 *
 * @author tominaga
 *
 */
public class CheckSurvey_missingDataDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public CheckSurvey_missingDataDao(EntityManager entityManager) {
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
        Join<TBuilding, MBuildingSms> joinBuilingsms = root
                .join(TBuilding_.MBuildingSms, JoinType.INNER);
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
        // 条件2:alertDisableFlg
        conditionList.add(cb.equal(root.get(MDevPrm_.alertDisableFlg), "0"));
        // 条件3:corpId
        conditionList.add(cb.equal(joinDevRel.get(MDevRelation_.id).get(MDevRelationPK_.corpId), corpId));
        // 条件4:buildingId
        conditionList.add(cb.equal(joinDevRel.get(MDevRelation_.id).get(MDevRelationPK_.buildingId), builingId));
        // 装置delflg
        conditionList.add(cb.equal(root.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(CheckSurveyDevPrmResultSet.class,
                root.get(MDevPrm_.devId), root.get(MDevPrm_.name)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.asc(root.get(MDevPrm_.devId)));

        this.entityManager.clear();
        List<CheckSurveyDevPrmResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * 日報欠損件数取得
     *
     * @param String devId
     * @param String befDate
     * @param String aftDate
     * @return 件数
     */
    public Long getCountLoadSurvey(String devId, String befDate, String aftDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getdate
        conditionList.add(cb.between(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), befDate, aftDate));
        // 条件2:devId
        conditionList.add(cb.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        // 条件3:kwh30
        conditionList.add(cb.isNotNull(root.get(TDayLoadSurvey_.kwh30)));
        // 条件4:dmv_kwh
        conditionList.add(cb.isNotNull(root.get(TDayLoadSurvey_.dmvKwh)));

        query.select(cb.count(root))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;

    }

    /**
     * 予定日報件数取得
     *
     * @param String devId
     * @return 件数
     */
    public Long getCountTarget(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件2:メーターdelflg
        conditionList.add(cb.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.count(root))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        Long ret = this.entityManager.createQuery(query).getSingleResult();
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
}
