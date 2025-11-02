package jp.co.osaki.sms.batch.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK_;
import jp.co.osaki.osol.entity.MAlertMail_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.MPauseMail_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeBuildingResultSet;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeDeviceListResultSet;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeMeterListResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;

/**
 * メーター検定満期通知メール実行 Daoクラス
 *
 * @author sagi_h
 *
 */
public class CheckMeterExpiryDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public CheckMeterExpiryDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * MUDM2の装置リストを取得する。
     *
     * @return MUDM2の装置リスト
     */
    public List<ExpiryNoticeDeviceListResultSet> getMDevPrmOfMUDM2() {
        // 装置情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ExpiryNoticeDeviceListResultSet> query = builder
                .createQuery(ExpiryNoticeDeviceListResultSet.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:装置種別がMUDM2
        conditionList.add(builder.equal(root.get(MDevPrm_.devKind), SmsBatchConstants.DEV_KIND.MUDM2.getVal()));
        // 条件2:削除フラグがONでない
        conditionList.add(builder.or(builder.isNull(root.get(MDevPrm_.delFlg)),
                builder.notEqual(root.get(MDevPrm_.delFlg), OsolConstants.FLG_ON)));

        // 装置ID昇順で並べ替え
        Order order = builder.asc(root.get(MDevPrm_.devId));

        query.select(builder.construct(ExpiryNoticeDeviceListResultSet.class, root.get(MDevPrm_.devId),
                root.get(MDevPrm_.examNoticeMonth), root.get(MDevPrm_.name)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).orderBy(order);

        this.entityManager.clear();
        List<ExpiryNoticeDeviceListResultSet> tMDevPrmList = this.entityManager.createQuery(query).getResultList();

        return tMDevPrmList;
    }

    /**
     * 指定された装置IDと対応する建物情報を取得する。
     *
     * @param devId 装置ID
     * @return 建物情報
     */
    public ExpiryNoticeBuildingResultSet getBuildingForExpiryNotice(String devId) {
        // 装置(親子)関係情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ExpiryNoticeBuildingResultSet> query = builder.createQuery(ExpiryNoticeBuildingResultSet.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        // 建物情報と結合
        Join<MDevRelation, TBuilding> joinTBuilding = root.join(MDevRelation_.TBuilding, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置関係情報の装置IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        // 条件2:削除フラグがONでない
        conditionList.add(builder.or(builder.isNull(joinTBuilding.get(TBuilding_.delFlg)),
                builder.notEqual(joinTBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_ON)));

        query.select(builder.construct(ExpiryNoticeBuildingResultSet.class,
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingName)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<ExpiryNoticeBuildingResultSet> tTBuildingList = this.entityManager.createQuery(query).getResultList();

        ExpiryNoticeBuildingResultSet tTBuilding = null;
        if (tTBuildingList.size() > 0) {
            tTBuilding = tTBuildingList.get(0);
        }

        return tTBuilding;
    }

    /**
     * 指定された装置IDのアラートメール通知先リストを取得する。
     *
     * @param devId
     * @return アラートメール通知先リスト
     */
    public List<MAlertMailListResultSet> getTargetAddressesForExpiryNotice(String devId) {
        // アラートメール設定を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MAlertMailListResultSet> query = builder.createQuery(MAlertMailListResultSet.class);

        Root<MAlertMail> root = query.from(MAlertMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MAlertMail_.id).get(MAlertMailPK_.devId), devId));
        // 条件2: 検満通知が有効
        conditionList.add(builder.equal(root.get(MAlertMail_.alertExam), OsolConstants.FLG_ON.toString()));
        // 条件3: 未使用フラグが有効でない
        conditionList.add(builder.or(builder.isNull(root.get(MAlertMail_.disabledFlg)),
                builder.notEqual(root.get(MAlertMail_.disabledFlg), OsolConstants.FLG_ON.toString())));

        query.select(builder.construct(MAlertMailListResultSet.class, root.get(MAlertMail_.email)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).distinct(true);

        this.entityManager.clear();
        List<MAlertMailListResultSet> tMAlertMailList = this.entityManager.createQuery(query).getResultList();

        return tMAlertMailList;
    }

    /**
     * 指定された装置IDに紐づくメーターのリストを取得する。
     *
     * @param devId
     * @return メーターリスト
     */
    public List<ExpiryNoticeMeterListResultSet> getMetersForExpiryNotice(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<ExpiryNoticeMeterListResultSet> query = builder.createQuery(ExpiryNoticeMeterListResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 条件2: 検満通知年月がNULLでない
        conditionList.add(builder.isNotNull(root.get(MMeter_.examEndYm)));
        // 条件3: 検満通知フラグが有効
        conditionList.add(builder.equal(root.get(MMeter_.examNotice), OsolConstants.FLG_ON.toString()));
        // 条件4:削除フラグがONでない
        conditionList.add(builder.or(builder.isNull(root.get(MMeter_.delFlg)),
                builder.notEqual(root.get(MMeter_.delFlg), OsolConstants.FLG_ON)));

        query.select(builder.construct(ExpiryNoticeMeterListResultSet.class, root.get(MMeter_.meterId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(MMeter_.examEndYm)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<ExpiryNoticeMeterListResultSet> tMMeterList = this.entityManager.createQuery(query).getResultList();

        return tMMeterList;
    }

    public MPauseMailResultSet getDevSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.exam)))
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
