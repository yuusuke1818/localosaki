package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import jp.co.osaki.sms.batch.dao.SendDataToAzbilTimeInfoDao;
import jp.co.osaki.sms.batch.logic.SendDataToAzbilCommonLogic;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * アズビル用サーバー 日報データ送信実行 TimerServiceクラス
 *
 * @author akr_iwamoto
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SendDataToAzbilTimeInfoTimerService extends SmsBatchTimerService implements Serializable {

	/**
	 * implements Serializable.
	 */
	private static final long serialVersionUID = 1361803793889771902L;

	/**
     * アズビル用サーバー 日報データ送信実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.
    		SEND_DATA_TO_AZBIL_TIMEINFO.getVal();

    /**
     * DAO
     */
    private SendDataToAzbilTimeInfoDao dao;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new SendDataToAzbilTimeInfoDao(this.entityManager);
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

        Date svDate = null;
        Calendar svCal = null;
        String strTargetDatetime = null;

        try {
            // 起動日時を取得
            svDate = (Date) dao.getServerDateTime();
            svCal = Calendar.getInstance();
            svCal.setTime(svDate);

            // 対象日時算出
            strTargetDatetime = getTargetDate(svDate);
            List<String> dateTimeList = new ArrayList<>();
            dateTimeList.add(strTargetDatetime);

            // アズビル送信処理呼び出し
            SendDataToAzbilCommonLogic logic = new SendDataToAzbilCommonLogic(entityManager, dao, this.batchName);
            logic.startLogic(dateTimeList, svDate);
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

    /**
     * 対象日時算出
     *
     * @param Date   latestDate
     * @param String endDay(yyyymmddhhmm)
     * @return 開始日(yyyymmddhhmm)
     */
    private String getTargetDate(Date nowDate) {
        String targetDay = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        if (targetDay != null) {
            String dayWork = getMin(targetDay);
            if (Integer.valueOf(dayWork) == 0) {
            	// 0分ジャストの場合は、前時刻の30分丸め
            	Date dateWk = DateUtility.plusHour(nowDate, -1);
            	targetDay = DateUtility.changeDateFormat(dateWk, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            	dayWork = "30";
            } else if (Integer.valueOf(dayWork) > 30) {
            	// 30分以降の場合は、30分丸め
                dayWork = "30";
            } else {
            	// 1～30分の場合は、0分丸め
                dayWork = "00";
            }
            targetDay = getYMDH(targetDay) + dayWork;
        }
        return targetDay;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 分文字列
     */
    private String getMin(String targetDate) {
        String ret = targetDate.substring(10, 12);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 年月日時文字列
     */
    private String getYMDH(String targetDate) {
        String ret = targetDate.substring(0, 10);
        return ret;
    }

}
