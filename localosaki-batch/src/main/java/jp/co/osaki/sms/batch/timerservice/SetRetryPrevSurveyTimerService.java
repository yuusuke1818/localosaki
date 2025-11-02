package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.SetRetryPrevSurveyDao;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialCommandCountResultSet;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialDeviceListResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 日報データ前日分 再収集予約処理実行 TimerServiceクラス
 * @author sagi_h
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SetRetryPrevSurveyTimerService extends SmsBatchTimerService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4631352783766039328L;

    /**
     * 日報データ前日分 再収集予約処理実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.SET_RETRY_PREVSURVEY.getVal();

    /**
     * DAO
     */
    private SetRetryPrevSurveyDao dao;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new SetRetryPrevSurveyDao(this.entityManager);
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
        Calendar prevDateCal = null;
        String prevDateStr = null;
        List<PrevSurveyRetrialDeviceListResultSet> deviceList = null;
        try {
            // 現在時刻取得
            svTime = dao.getServerDateTime();

            // 前日日付に設定
            prevDateCal = Calendar.getInstance();
            prevDateCal.setTimeInMillis(svTime.getTime());
//            prevDateCal.add(Calendar.HOUR_OF_DAY, -24);
			if (prevDateCal.get(Calendar.HOUR_OF_DAY) == 12) {
				prevDateCal.add(Calendar.HOUR_OF_DAY, -72);		// 12時の過去データ取得対象は3日前（-72時間）
			}
			else if (prevDateCal.get(Calendar.HOUR_OF_DAY) == 13) {
				prevDateCal.add(Calendar.HOUR_OF_DAY, -168);	// 13時の過去データ取得対象は7日前（-168時間）
			}
			else {
				prevDateCal.add(Calendar.HOUR_OF_DAY, -24);
			}

            // 前日日付文字列生成
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDD);
            prevDateStr = sdf.format(prevDateCal.getTime());

            // 装置リスト取得
            deviceList = dao.getDeviceListForSurveyRetrial();
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        for (PrevSurveyRetrialDeviceListResultSet device : deviceList) {
            try {
                String devId = device.getDevId();
                String revFlg = device.getRevFlg();

                if (devId.startsWith(SmsConstants.DEVICE_KIND.OCR.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.HANDY.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.IOTR.getVal()) ||
                    devId.startsWith(SmsConstants.DEVICE_KIND.LTEM.getVal())) {
                        continue;
                } else {

                    // 装置処理開始ログ出力
                    batchLogger
                            .info(this.batchName.concat(String.format(" dev_id = %s prevDate = %s", devId, prevDateStr)));

                    // 予約対象コマンドリスト作成
                    List<String> commands = new ArrayList<String>();
                    commands.add(SmsConstants.CMD_KIND.GET_DLSDATA.getVal());
                    commands.add(SmsConstants.CMD_KIND.GET_DMVDATA.getVal());

                    // 逆潮フラグONの場合は逆潮のロードサーベイと現在値も取得予約
                    if ((revFlg != null) && (revFlg.equals(OsolConstants.FLG_ON.toString()))) {
                        commands.add(SmsConstants.CMD_KIND.GET_RDLSDATA.getVal());
                        commands.add(SmsConstants.CMD_KIND.GET_RDMVDATA.getVal());
                    }
                    for (String command : commands) {
                        // 当該装置に対する前日のデータ取得コマンドで、処理待ち・電文送信中の件数を取得
                        PrevSurveyRetrialCommandCountResultSet prevCommandsCount = dao.countCommandsForPrevSurvey(devId,
                                command, prevDateStr);

                        if (prevCommandsCount.getCount().longValue() == 0) {
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            if (entityManager.getTransaction().isActive()) {
                                // 再登録処理を実行
                                boolean isRewriteSuccess = rewriteCommandForPrevSurveyRetrial(devId, command, prevDateStr,
                                        svTime);
                                // 再登録に失敗した場合はロールバック
                                if (!isRewriteSuccess || (entityManager.getTransaction().getRollbackOnly())) {
                                    entityManager.getTransaction().rollback();
                                } else {
                                    entityManager.getTransaction().commit();
                                }
                            }
                            entityManager.clear();
                        }
                    }
                }
            } catch (Exception ex) {
                // ログ出力
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                return;
            }
        }
        batchLogger.info(this.batchName.concat(" End"));
    }

    /**
     * 前日データ取得コマンドを再登録する。
     * @param devId 装置ID
     * @param command コマンド文字列
     * @param prevDateStr 前日日付
     * @param svTime 現在時刻
     * @return true:成功 / false:失敗
     */
    private boolean rewriteCommandForPrevSurveyRetrial(String devId, String command, String prevDateStr,
            Timestamp svTime) {
        try {
            // 当該装置に対する前日のデータ取得コマンドで、処理待ち・電文送信中以外を削除
            dao.deleteCommandForPrevSurveyRetrial(devId, command, prevDateStr);

            // 当該装置に対する前日のデータ取得コマンドを再予約
            dao.createCommandForPrevSurveyRetrial(devId, command, prevDateStr, svTime);
        } catch (Exception ex) {
            // ログ出力
            batchLogger.error(this.batchName.concat("DB更新エラー"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        }
        return true;
    }

}
