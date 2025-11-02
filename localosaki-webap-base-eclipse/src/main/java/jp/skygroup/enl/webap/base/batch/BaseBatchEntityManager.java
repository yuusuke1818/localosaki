package jp.skygroup.enl.webap.base.batch;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * バッチ処理用 EntityManager管理クラス
 *
 * @author take_suzuki
 *
 */
public abstract class BaseBatchEntityManager extends BaseBatchScheduleTimer {

    /**
     * EntityManagerFactory
     */
    @PersistenceUnit
    protected EntityManagerFactory entityManagerFactory;

    /**
     * EntityManager
     */
    protected EntityManager entityManager;

    /**
     * 起動処理
     */
    @Override
    protected void start(String batchName) {

        //開始処理
        super.start(batchName);
    }

    /**
     * 停止処理
     */
    @Override
    protected void stop(String batchName) {

        if (Objects.nonNull(this.entityManager)) {
            this.entityManager.clear();
            this.entityManager.close();
        }

        //停止処理
        super.stop(batchName);
    }

}
