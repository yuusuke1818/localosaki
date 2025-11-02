package jp.co.osaki.sms.batch.dao;

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
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK_;
import jp.co.osaki.osol.entity.TInspectionMeterBef_;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.SqsReceiveMeterResultSet;

/**
 * SQS キュー取得Dao
 * @author hayashi_tak
 *
 */
public class SqsReceiveDao extends SmsBatchDao {

    /**
     * コンストラクタ
     * @param entityManager
     */
    public SqsReceiveDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 指定されたメーターIDに紐づくメーターを取得する。
     *
     * @param devId
     * @return メーターリスト
     */
    public SqsReceiveMeterResultSet getMeterInfo(String meterId) {
        // メーターを検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<SqsReceiveMeterResultSet> query = builder.createQuery(SqsReceiveMeterResultSet.class);

        Root<MMeter> root = query.from(MMeter.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: メーターIDが等価
        conditionList.add(builder.equal(root.get(MMeter_.meterId), meterId));
        // 条件2:削除フラグがONでない
        conditionList.add(builder.or(builder.isNull(root.get(MMeter_.delFlg)),
                builder.notEqual(root.get(MMeter_.delFlg), OsolConstants.FLG_ON)));

        query.select(builder.construct(SqsReceiveMeterResultSet.class, root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(MMeter_.meterType), root.get(MMeter_.multi)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<SqsReceiveMeterResultSet> meterList = new ArrayList<>();
        SqsReceiveMeterResultSet meter = null;
        meterList.addAll(this.entityManager.createQuery(query).getResultList());
        if(meterList.size() != 0) {
        	meter = meterList.get(0);
        }

        return meter;
    }

    /**
     * メーターに紐づく建物IDをListで取得する
     * @param devId
     * @param meterMngId
     * @return 建物IDリスト
     */
    public List<TBuildDevMeterRelation> getBuildMeterRelation(String devId, Long meterMngId) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TBuildDevMeterRelation> query = builder.createQuery(TBuildDevMeterRelation.class);

        Root<TBuildDevMeterRelation> root = query.from(TBuildDevMeterRelation.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが等価
        conditionList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId), devId));
        // 条件2: メーター管理番号が等価
        conditionList.add(builder.equal(root.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId), meterMngId));

        query = query.select(root).where(builder.and(conditionList.toArray(new Predicate[]{})));

        this.entityManager.clear();
        return this.entityManager.createQuery(query).getResultList();
    }

    /**
     *
     * @param corpId
     * @param buildingId
     * @return tenantId
     */
    public Long getTenantId(String corpId, Long buildingId) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MTenantSm> query = builder.createQuery(MTenantSm.class);

        Root<MTenantSm> root = query.from(MTenantSm.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 企業IDが等価
        conditionList.add(builder.equal(root.get(MTenantSm_.id).get(MTenantSmPK_.corpId), corpId));
        // 条件2: 建物IDが等価
        conditionList.add(builder.equal(root.get(MTenantSm_.id).get(MTenantSmPK_.buildingId), buildingId));

        query = query.select(root).where(builder.and(conditionList.toArray(new Predicate[]{})));

        this.entityManager.clear();
        List<MTenantSm> retList = new ArrayList<>();

        retList.addAll(this.entityManager.createQuery(query).getResultList());
        if(retList.size() == 0) {
            return null;
        }

        return retList.get(0).getTenantId();
    }

    /**
     * 主キーで同一のものがあるかどうか
     * 1個以上存在すればtrue
     * 0個であればfalse
     * @param tInspectionMeterBef
     * @return
     */
    public boolean getBoolInspectionMeterBef(TInspectionMeterBef entity) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterBef> query = builder.createQuery(TInspectionMeterBef.class);

        Root<TInspectionMeterBef> root = query.from(TInspectionMeterBef.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.devId), entity.getId().getDevId()));
        // 条件2: meter管理番号が等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.meterMngId), entity.getId().getMeterMngId()));
        // 条件3: 日付が等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.latestInspDate), entity.getId().getLatestInspDate()));

        query = query.select(root).where(builder.and(conditionList.toArray(new Predicate[]{})));

        this.entityManager.clear();
        List<TInspectionMeterBef> retList = new ArrayList<>();

        retList.addAll(this.entityManager.createQuery(query).getResultList());
        if(retList.size() == 0) {
            return false;
        }
    	return true;
    }

    /**
     * 更新掛けるときにどのレコードに更新を掛けるか特定する
     */
    public TInspectionMeterBef find(TInspectionMeterBef entity) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterBef> query = builder.createQuery(TInspectionMeterBef.class);

        Root<TInspectionMeterBef> root = query.from(TInspectionMeterBef.class);
        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.devId), entity.getId().getDevId()));
        // 条件2: meter管理番号が等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.meterMngId), entity.getId().getMeterMngId()));
        // 条件3: 日付が等価
        conditionList.add(builder.equal(root.get(TInspectionMeterBef_.id).get(TInspectionMeterBefPK_.latestInspDate), entity.getId().getLatestInspDate()));

        query = query.select(root).where(builder.and(conditionList.toArray(new Predicate[]{})));

        this.entityManager.clear();
        List<TInspectionMeterBef> retList = new ArrayList<>();

        retList.addAll(this.entityManager.createQuery(query).getResultList());
        return retList.get(0);
    }

    /**
     * 登録処理
     *
     * @param entity
     */
    private void insertDB(Object entity) {

        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 更新処理
     *
     * @param entity
     */
    private void updateDB(Object entity) {
        entityManager.merge(entity);
        entityManager.flush();
        entityManager.clear();
    }

    public void createInspectionBef(TInspectionMeterBef tInspectionMeterBef) {
    	if(getBoolInspectionMeterBef(tInspectionMeterBef)) {
    		updateDB(tInspectionMeterBef);
    	}else {
    		insertDB(tInspectionMeterBef);
    	}

    }



}
