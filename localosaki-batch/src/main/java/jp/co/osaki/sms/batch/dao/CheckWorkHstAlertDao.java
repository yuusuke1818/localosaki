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
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSms_;
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
import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK_;
import jp.co.osaki.osol.entity.TWorkHst_;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.DevCommandBuildingMailAddressResultSet;
import jp.co.osaki.sms.batch.resultset.DevCommandDeviceListResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;

/**
 * メーター検定満期通知メール実行 Daoクラス
 *
 * @author sagi_h
 *
 */
public class CheckWorkHstAlertDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public CheckWorkHstAlertDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * MUDM2の装置リストを取得する。
     *
     * @return MUDM2の装置リスト
     */
    public List<DevCommandDeviceListResultSet> getMDevPrmOfMUDM2() {
        // 装置情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<DevCommandDeviceListResultSet> query = builder.createQuery(DevCommandDeviceListResultSet.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:装置種別がMUDM2
        conditionList.add(builder.equal(root.get(MDevPrm_.devKind), SmsBatchConstants.DEV_KIND.MUDM2.getVal()));
        // 条件2:削除フラグがONでない
        conditionList.add(builder.notEqual(root.get(MDevPrm_.delFlg), OsolConstants.FLG_ON));

        // 装置ID昇順で並べ替え
        Order order = builder.asc(root.get(MDevPrm_.devId));

        query.select(builder.construct(DevCommandDeviceListResultSet.class, root.get(MDevPrm_.devId),
                root.get(MDevPrm_.devSta), root.get(MDevPrm_.name), root.get(MDevPrm_.homeDirectory),
                root.get(MDevPrm_.revFlg)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).orderBy(order);

        this.entityManager.clear();
        List<DevCommandDeviceListResultSet> tMDevPrmList = this.entityManager.createQuery(query).getResultList();

        return tMDevPrmList;
    }

    /**
     * 装置件数取得
     *
     * @param String devId
     * @return コマンド件数
     */
    public Long getCountMeter(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

        query.select(cb.count(root)).where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();

        return ret;
    }



    /**
     * 処理予約の処置待ち処理フラグ件数を取得
     *
     * @param String devId
     * @return 件数
     */
    public Long getWaitWorkHstWaitCount(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TWorkHst> root = query.from(TWorkHst.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), devId));
        // 条件2:devId
        conditionList.add(cb.equal(root.get(TWorkHst_.srvEnt), SmsConstants.TWORKHST_SRV_ENT.WAIT.getVal()));

        query.select(cb.count(root)).where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;
    }

    /**
     * コマンド送信の送信中処理フラグ件数を取得
     *
     * @param String devId
     * @return 件数
     */
    public Long getGetSendWaitCommandCount(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<TWorkHst> root = query.from(TWorkHst.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TWorkHst_.id).get(TWorkHstPK_.devId), devId));
        // 条件2:devId
        conditionList.add(cb.equal(root.get(TWorkHst_.srvEnt), SmsConstants.TWORKHST_SRV_ENT.GETSEND_WAIT.getVal()));

        query.select(cb.count(root)).where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        Long ret = this.entityManager.createQuery(query).getSingleResult();
        return ret;
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
        // 条件2: 未使用フラグが有効でない
        conditionList.add(builder.or(
                builder.isNull(root.get(MAlertMail_.disabledFlg)),
                builder.notEqual(root.get(MAlertMail_.disabledFlg), OsolConstants.FLG_ON.toString())
            ));

        query.select(builder.construct(MAlertMailListResultSet.class, root.get(MAlertMail_.email)))
                .where(builder.and(conditionList.toArray(new Predicate[] {}))).distinct(true);

        this.entityManager.clear();
        List<MAlertMailListResultSet> tMAlertMailList = this.entityManager.createQuery(query).getResultList();

        return tMAlertMailList;
    }

    /**
     * 指定された装置IDと対応する建物メール情報を取得する。
     *
     * @param devId 装置ID
     * @return 建物情報
     */
    public DevCommandBuildingMailAddressResultSet getBuildingMailAddrssForDev(String devId) {
        // 装置(親子)関係情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<DevCommandBuildingMailAddressResultSet> query = builder.createQuery(DevCommandBuildingMailAddressResultSet.class);

        Root<MDevRelation> root = query.from(MDevRelation.class);

        // 建物情報と結合
        Join<MDevRelation, TBuilding> joinTBuilding = root.join(MDevRelation_.TBuilding, JoinType.INNER);
        // 建物SMSと結合
        Join<TBuilding, MBuildingSms> joinMBuildingSms = joinTBuilding.join(TBuilding_.MBuildingSms, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置関係情報の装置IDが引数の値と一致するもの
        conditionList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        // 条件2:削除フラグがONでない
        conditionList.add(builder.notEqual(joinTBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_ON));

        query.select(builder.construct(DevCommandBuildingMailAddressResultSet.class,
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingName),
                joinMBuildingSms.get(MBuildingSms_.mail1),
                joinMBuildingSms.get(MBuildingSms_.mail2),
                joinMBuildingSms.get(MBuildingSms_.mail3),
                joinMBuildingSms.get(MBuildingSms_.mail4),
                joinMBuildingSms.get(MBuildingSms_.mail5),
                joinMBuildingSms.get(MBuildingSms_.mail6),
                joinMBuildingSms.get(MBuildingSms_.mail7),
                joinMBuildingSms.get(MBuildingSms_.mail8),
                joinMBuildingSms.get(MBuildingSms_.mail9),
                joinMBuildingSms.get(MBuildingSms_.mail10)
                )).where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<DevCommandBuildingMailAddressResultSet> tTBuildingSmsMailAddrList =
                this.entityManager.createQuery(query).getResultList();
        if (tTBuildingSmsMailAddrList != null && tTBuildingSmsMailAddrList.size() > 0) {
            return tTBuildingSmsMailAddrList.get(0);
        } else {
            return null;
        }

        // DevCommandBuildingMailAddressResultSet tTBuildingSmsMailAddr = this.entityManager.createQuery(query).getSingleResult();
        // return tTBuildingSmsMailAddr;
    }

    public MPauseMailResultSet getDevSendMailFlg(String devId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MPauseMailResultSet> query = builder.createQuery(MPauseMailResultSet.class);

        Root<MPauseMail> root = query.from(MPauseMail.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(MPauseMail_.devId), devId));

        query.select(builder.construct(MPauseMailResultSet.class, root.get(MPauseMail_.workHstErr)))
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
