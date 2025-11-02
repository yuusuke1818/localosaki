package jp.skygroup.enl.webap.base.batch;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * バッチ処理用 スケジュールタイマー実行 基底クラス
 *
 * @author take_suzuki
 *
 */
public abstract class BaseBatchScheduleTimer extends BaseBatch {

    /**
     * TimerService
     */
    @Resource
    protected TimerService timerService;

    /**
     * Timer
     */
    protected Timer timer;

    /**
     * ScheduleExpression
     */
    protected ScheduleExpression scheduleExpression;

    /**
     * 起動処理
     */
    protected void start(String batchName) {

        if (Objects.nonNull(this.scheduleExpression)) {
            TimerConfig timerConfig = new TimerConfig();
            timerConfig.setPersistent(false);
            timerConfig.setInfo(this.getClass().hashCode());
            this.timer = this.timerService.createCalendarTimer(this.scheduleExpression, timerConfig);
            batchLogger.info(batchName.concat(" Schedule Timer [")
                    .concat(" Second = ").concat(scheduleExpression.getSecond())
                    .concat(" Minute = ").concat(scheduleExpression.getMinute())
                    .concat(" Hour = ").concat(scheduleExpression.getHour())
                    .concat(" DayOfMonth = ").concat(scheduleExpression.getDayOfMonth())
                    .concat(" Month = ").concat(scheduleExpression.getMonth())
                    .concat(" Year = ").concat(scheduleExpression.getYear())
                    .concat(" DayOfWeek = ").concat(scheduleExpression.getDayOfWeek())
                    .concat(" ]"));
        }
    };

    /**
     * 停止処理
     */
    protected void stop(String batchName) {

        if (Objects.nonNull(this.timer)) {
            this.timer.cancel();
            batchLogger.info(batchName.concat(" Schedule Timer Cancel"));
        }
    }

    /**
     * タイマースケジュール処理
     *
     * @Timeout
     */
    protected abstract void execute();

   /**
    *
    * CronTab設定から現在時刻での実行判定
    *
    * @param checkCronExpression      CronTabの設定文字列
    * @param checkCronType            checkCronExpressionの書式
    *
    * CronType.UNIX
    * cronExpression 分(0-59)、時(0-23)、日(1-31)、月(1-12)、曜日(0:日,1:月,2:火,3:水,4:木,5:金,6:土,7:日)
    *
    * CronType.CRON4
    * cronExpression 分(0-59)、時(0-23)、日(1-31)、月(1-12)、曜日(0:日,1:月,2:火,3:水,4:木,5:金,6:土,7:日)
    *
    * CronType.QUARTZ
    * cronExpression 秒(0-59)、分(0-59)、時(0-23)、日(1-31)、月(1-12)、曜日(0:日,1:月,2:火,3:水,4:木,5:金,6:土,7:日)、年
    *
    * CronType.SPRING
    * cronExpression 秒(0-59)、分(0-59)、時(0-23)、日(1-31)、月(1-12)、曜日(0:日,1:月,2:火,3:水,4:木,5:金,6:土,7:日)
    *
    * @return True:実行有 False:実行無
    */
   protected boolean executionJudgmentByCronTab(String checkCronExpression, CronType checkCronType){

       try {
           //create a parser based on provided definition
           CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(checkCronType));
           ZonedDateTime now = ZonedDateTime.now();
           //validate expression
           Cron cron = parser.parse(checkCronExpression).validate();
           ExecutionTime executionTime = ExecutionTime.forCron(cron);
           // Time from last execution
           Optional<Duration> timeFromLastExecution = executionTime.timeFromLastExecution(now);
           //次の実行までの時間を取得します
           Optional<Duration> timeToNextExecution = executionTime.timeToNextExecution(now);
           if (CronType.UNIX.equals(checkCronType) || CronType.CRON4J.equals(checkCronType)){
               //最後の実行から60秒以内 もしくは、次の実行まで0秒
               if ((timeFromLastExecution.get().getSeconds() <= 60) || timeToNextExecution.get().getSeconds() == 0) {
                   return true;
               }
           } else if (CronType.QUARTZ.equals(checkCronType) || CronType.SPRING.equals(checkCronType)){
               //最後の実行から1秒以内 もしくは、次の実行まで0秒
               if (timeFromLastExecution.get().getSeconds() <= 1 || timeToNextExecution.get().getSeconds() == 0) {
                   return true;
               }
           }
       } catch (Exception e) {
           errorLogger.error(BaseUtility.getStackTraceMessage(e));
       }

       return false;
   }

   /**
    *
    * CronTab設定をScheduleExpressionに設定
    *
    * @param settingCronExpression        CronTabの設定文字列
    * @param settingCronType              settingCronExpressionの書式
    *
    * @return ScheduleExpression
    */
   protected ScheduleExpression scheduleExpressionByCronTab(String settingCronExpression, CronType settingCronType) {

       try {
           ScheduleExpression schedule = new ScheduleExpression();
           //create a parser based on provided definition
           CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(settingCronType));
           //validate expression
           Cron cron = parser.parse(settingCronExpression).validate();
           for (Entry<CronFieldName, CronField> entry : cron.retrieveFieldsAsMap().entrySet()) {
               String value = "";
               if ("?".equals(entry.getValue().getExpression().asString().trim())) {
                   value = "*";
               } else {
                   value = entry.getValue().getExpression().asString();
               }
               if (entry.getValue().getField().name().equals(CronFieldName.SECOND.name())) {
                   schedule.second(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.MINUTE.name())) {
                   schedule.minute(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.HOUR.name())) {
                   schedule.hour(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.DAY_OF_MONTH.name())) {
                   schedule.dayOfMonth(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.MONTH.name())) {
                   schedule.month(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.DAY_OF_WEEK.name())) {
                   schedule.dayOfWeek(value);
               }
               if (entry.getValue().getField().name().equals(CronFieldName.YEAR.name())) {
                   schedule.year(value);
               }
           }
           return schedule;
       } catch (IllegalArgumentException  e) {
           batchLogger.error(this.getClass().getSimpleName().concat(" ").concat(e.getMessage()));
           errorLogger.error(BaseUtility.getStackTraceMessage(e));
           return null;
       } catch (Exception  e) {
           errorLogger.error(BaseUtility.getStackTraceMessage(e));
           return null;
       }
   }

}
