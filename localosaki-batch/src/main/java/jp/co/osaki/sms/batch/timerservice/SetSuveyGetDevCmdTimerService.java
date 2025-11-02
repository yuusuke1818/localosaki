package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jboss.ejb3.annotation.TransactionTimeout;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.SetSurveyGetDevCmdDao;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialDeviceListResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * SMS 日報収集データセット（定期処理） TimerServiceクラス
 *
 * @author tominaga
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SetSuveyGetDevCmdTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = -2994690374587519739L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.SET_SUVEY_GETDEVCMD.getVal();

    // 何年分のデータを残すか（年）
    private static final int DATA_DEL_PRIOD = -6;
    // データ取得時刻(分)
    private static final int DATA_REQUEST_MINUTE = -30;
    /**
     * Dao
     */
    private SetSurveyGetDevCmdDao dao;

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new SetSurveyGetDevCmdDao(this.entityManager);

        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(), SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);

    }

    /*
     * (非 Javadoc)
     *
     * @see jp.skygroup.enl.webap.base.batch.BaseBatchScheduleTimer#execute()
     */
    @TransactionTimeout(unit = TimeUnit.HOURS, value = 2)
    @Timeout
    @Override
    protected void execute() {

        try {

            batchLogger.info(this.batchName.concat(" Start"));

            // 処理日時を取得する
            serverDateTime = dao.getServerDateTime();

            //現在時刻を生成
            Calendar calNow = Calendar.getInstance();
            calNow.setTimeInMillis(serverDateTime.getTime());
            Date now_date = new Date(calNow.getTimeInMillis());

            //日報要求の対象日時を生成
            //処理対象のデータは1時限前（30分前）なので30分マイナスした日時にする
            Calendar calCommandRequest = Calendar.getInstance();
            calCommandRequest.setTimeInMillis(serverDateTime.getTime());
            calCommandRequest.add(Calendar.MINUTE, DATA_REQUEST_MINUTE);
            Date req_date = new Date(calCommandRequest.getTimeInMillis());
            String req_date_str = DateUtility.changeDateFormat(req_date, DateUtility.DATE_FORMAT_YYYYMMDD);

            //過去日報削除の対象日時を生成
            //現在時刻から6年マイナスする
            Calendar calDeleteOldData = Calendar.getInstance();
            calDeleteOldData.setTimeInMillis(serverDateTime.getTime());
        	calDeleteOldData.add(Calendar.YEAR, DATA_DEL_PRIOD);
            Date del_date = new Date(calDeleteOldData.getTimeInMillis());
            String del_date_str = DateUtility.changeDateFormat(del_date, DateUtility.DATE_FORMAT_YYYYMMDD);

            // 起動時刻が0:00～0:30？
            if (calNow.get(Calendar.HOUR_OF_DAY) == 0 && calNow.get(Calendar.MINUTE) < 30) {
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (entityManager.getTransaction().isActive()) {
                    // コマンドテーブル削除
                    dao.delCommand(serverDateTime);
                    batchLogger.info(this.batchName.concat(" delete T_COMMAND"));

                    // ロードサーベイ メーター指定用削除
                    dao.delCommandMeter(serverDateTime);
                    batchLogger.info(this.batchName.concat(" delete T_COMMAND_LOAD_SURVEY_METER"));

                    // ロードサーベイ 時間指定用削除
                    dao.delCommandTime(serverDateTime);
                    batchLogger.info(this.batchName.concat(" delete T_COMMAND_LOAD_SURVEY_TIME"));

                    // 処理予約テーブル削除
                    dao.delWrkHst(serverDateTime);
                    batchLogger.info(this.batchName.concat(" delete T_WORK_HST"));

                    if (entityManager.getTransaction().getRollbackOnly()) {
                        entityManager.getTransaction().rollback();
                        batchLogger.info(this.batchName.concat(" delete rollback"));
                    } else {
                        entityManager.getTransaction().commit();
                        batchLogger.info(this.batchName.concat(" delete commit"));
                    }
                }
                entityManager.clear();
            }
    
            // 装置単位で日報データの取得コマンドを再設定
            List<PrevSurveyRetrialDeviceListResultSet> devIdList = dao.listDevPrm();
            for (PrevSurveyRetrialDeviceListResultSet devInfo : devIdList) {
                String devId = devInfo.getDevId();
                String revFlg = devInfo.getRevFlg();

                if (devId.startsWith(SmsConstants.DEVICE_KIND.OCR.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.HANDY.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.IOTR.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.LTEM.getVal())) {
                    continue;
                } else {

                    batchLogger.info(this.batchName.concat("loadsurvey command set dev_id=" + devId));
                    
                    // 要求コマンドをDBへインサート
                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    if (entityManager.getTransaction().isActive()) {
                        Timestamp recdate = new Timestamp(now_date.getTime());
                        // ロードサーベイ日データ
                        dao.setCommand(devId, SmsConstants.CMD_KIND.GET_DLSDATA.getVal(), req_date_str, recdate);
                        // ロードサーベイ日指針値データ
                        dao.setCommand(devId, SmsConstants.CMD_KIND.GET_DMVDATA.getVal(), req_date_str, recdate);

                        // 装置の逆潮対応機能未使用なら要求しない
                        if ((revFlg != null) && (revFlg.equals(OsolConstants.FLG_ON.toString()))) {
                            // ロードサーベイ日データ（逆方向）
                            dao.setCommand(devId, SmsConstants.CMD_KIND.GET_RDLSDATA.getVal(), req_date_str, recdate);
                            // ロードサーベイ日指針値データ（逆方向）
                            dao.setCommand(devId, SmsConstants.CMD_KIND.GET_RDMVDATA.getVal(), req_date_str, recdate);
                        }
                        if (entityManager.getTransaction().getRollbackOnly()) {
                            entityManager.getTransaction().rollback();
                        } else {
                            entityManager.getTransaction().commit();
                        }
                    }
                    entityManager.clear();
                }
            }

            // 起動時刻が0:00～0:30？
            if (calNow.get(Calendar.HOUR_OF_DAY) == 0 && calNow.get(Calendar.MINUTE) < 30) {
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (entityManager.getTransaction().isActive()) {
                    // 過去データの削除
                    batchLogger.info(this.batchName.concat(" del_date >>" + del_date + ",del_date_str >>" + del_date_str));

                    // ロードサーベイ日データ削除
                    batchLogger.info(this.batchName.concat(" del_date T_DAY_LOAD_SURVEY>> start"));
                    dao.delDayLoadSurvey(del_date_str);
                    batchLogger.info(this.batchName.concat(" del_date T_DAY_LOAD_SURVEY>> end"));

                    // ロードサーベイ日データ（逆方向）削除
                    batchLogger.info(this.batchName.concat(" del_date T_DAY_LOAD_SURVEY_REV>> start"));
                    dao.delRDayLoadSurvey(del_date_str);
                    batchLogger.info(this.batchName.concat(" del_date T_DAY_LOAD_SURVEY_REV>> end"));

                    // 検針結果データ削除
                    batchLogger.info(this.batchName.concat(" del_date T_INSPECTION_METER>> start"));
                    dao.delInspMeter(del_date_str);
                    batchLogger.info(this.batchName.concat(" del_date T_INSPECTION_METER>> end"));

                    // メータ現在値データ削除
                    batchLogger.info(this.batchName.concat(" del_date T_METER_DATA>> start"));
                    dao.delCurrentMeterData(new Timestamp(del_date.getTime()));
                    batchLogger.info(this.batchName.concat(" del_date T_METER_DATA>> end"));

                    if (entityManager.getTransaction().getRollbackOnly()) {
                        batchLogger.info(this.batchName.concat(" rollback>> start"));
                        entityManager.getTransaction().rollback();
                        batchLogger.info(this.batchName.concat(" rollback>> end"));
                    } else {
                        batchLogger.info(this.batchName.concat(" commit>> start"));
                        entityManager.getTransaction().commit();
                        batchLogger.info(this.batchName.concat(" commit>> end"));
                    }
                }
                entityManager.clear();
            }

            batchLogger.info(this.batchName.concat(" End"));

        } catch (Exception ex) {

            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));

        }
    }

}
