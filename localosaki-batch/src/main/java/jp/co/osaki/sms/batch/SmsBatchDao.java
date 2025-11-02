package jp.co.osaki.sms.batch;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.entity.TBatchStartupSetting_;
import jp.skygroup.enl.webap.base.batch.BaseBatchDao;

/**
 *
 * バッチ用のOSOL共通Daoクラス
 *
 * @author take_suzuki
 *
 */
public abstract class SmsBatchDao extends BaseBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public SmsBatchDao(EntityManager entityManager) {

        super(entityManager);
    }

    /**
    *
    * DBサーバ時刻取得メソッド
    *
    * @return DBサーバ時刻
    */
    @Override
    public Timestamp getServerDateTime() {

        return super.getServerDateTime();
    }

    /**
     * バッチ起動設定を検索
     *
     * @param batchProcessCd    バッチ処理コード
     *
     * @return バッチ起動設定
     */
    public TBatchStartupSetting getTBatchStartupSetting(String batchProcessCd) {

        // バッチ起動設定を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TBatchStartupSetting> query = builder.createQuery(TBatchStartupSetting.class);

        //バッチ起動設定
        Root<TBatchStartupSetting> root = query.from(TBatchStartupSetting.class);

        List<Predicate> conditionList = new ArrayList<>();
        //バッチ起動設定.バッチ処理コード
        conditionList.add(builder.equal(root.get(TBatchStartupSetting_.batchProcessCd), batchProcessCd));

        query.select(root).where(builder.and(conditionList.toArray(new Predicate[] {})));

        List<TBatchStartupSetting> tBatchStartupSettingList = this.entityManager.createQuery(query).getResultList();
        if (tBatchStartupSettingList != null && tBatchStartupSettingList.size() > 0) {
            return tBatchStartupSettingList.get(0);
        } else {
            return null;
        }
    }

}