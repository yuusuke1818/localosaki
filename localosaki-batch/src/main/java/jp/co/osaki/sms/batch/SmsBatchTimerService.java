package jp.co.osaki.sms.batch;

import javax.annotation.PreDestroy;

import com.cronutils.model.CronType;

import jp.skygroup.enl.webap.base.batch.BaseBatchEntityManager;

/**
 *
 * OSOLスケジュールタイマーサービス共通クラス
 *
 * @author take_suzuki
 *
 */
public abstract class SmsBatchTimerService extends BaseBatchEntityManager {

    /**
     * バッチ起動スケジュール Cron書式
     */
    protected static final CronType SCHEDULE_CRON_TYPE = CronType.SPRING;

    /**
     * バッチ処理名称
     */
    protected String batchName = this.getClass().getSimpleName();

    /**
     * 停止処理
     */
    @PreDestroy
    protected void stop() {

        //停止処理
        super.stop(this.batchName);
    }


}
