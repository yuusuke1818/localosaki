/**
 *
 */
package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.CreateDailyDataDao;
import jp.co.osaki.sms.batch.resultset.DailyDataCreationDLSCountResultSet;
import jp.co.osaki.sms.batch.resultset.DailyDataCreationMeterListResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 翌日0時00分の日報空データ作成 TimerServiceクラス
 * @author sagi_h
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class CreateDailyDataTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 2683024256689263205L;

    /**
     * 翌日0時00分の日報空データ作成
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.CREATE_DAILY_DATA.getVal();

    /**
     * DAO
     */
    private CreateDailyDataDao dao;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new CreateDailyDataDao(this.entityManager);
        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(),
                    SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);
    }

    @Override
    @Timeout
    protected void execute() {
        batchLogger.info(this.batchName.concat(" Start"));
        Timestamp svTime = null;
        Calendar getDateCal = null;
        String getDateStr = null;
        List<DailyDataCreationMeterListResultSet> meterList = null;
        try {
            // 現在時刻取得
            svTime = dao.getServerDateTime();

            // 収集日時を翌日に設定
            getDateCal = Calendar.getInstance();
            getDateCal.setTimeInMillis(svTime.getTime());
            getDateCal.add(Calendar.DATE, 1);
            getDateCal.set(Calendar.HOUR_OF_DAY, 0);
            getDateCal.set(Calendar.MINUTE, 0);
            getDateCal.set(Calendar.SECOND, 0);

            // 収集日時文字列生成
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            getDateStr = sdf.format(getDateCal.getTime());

            // メーターリスト取得
            meterList = dao.getAllMeters();
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        for (DailyDataCreationMeterListResultSet meter : meterList) {
            try {
                String devId = meter.getDevId();
                Long meterMngId = meter.getMeterMngId();

                // 装置IDかメーター管理番号がNULLの場合はログ出力
                if ((devId == null) || (meterMngId == null)) {
                    if (devId == null) {
                        devId = "(空欄)";
                    }
                    String meterMngIdStr = "(空欄)";
                    if (meterMngId != null) {
                        meterMngIdStr = meterMngId.toString();
                    }
                    batchLogger.info(this.batchName.concat(
                            String.format(" エラーメーター情報 （装置ID:%s,メーター管理番号:%s,収集日時:%s）",
                                    devId, meterMngIdStr, getDateStr)));
                    continue;
                }

                // 既存のロードサーベイ日データ件数を取得
                DailyDataCreationDLSCountResultSet dlsCount = dao.countDayLoadSurveyData(devId, meterMngId, getDateStr);

                // 0件であれば空データを追加
                if (dlsCount.getCount().longValue() == 0) {
                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    if (entityManager.getTransaction().isActive()) {
                        dao.createDayLoadSurveyData(devId, meterMngId, getDateStr, svTime);
                        if (entityManager.getTransaction().getRollbackOnly()) {
                            entityManager.getTransaction().rollback();
                        } else {
                            entityManager.getTransaction().commit();
                        }
                    }
                    entityManager.clear();
                }
            } catch (Exception ex) {
                // ログ出力
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

}
