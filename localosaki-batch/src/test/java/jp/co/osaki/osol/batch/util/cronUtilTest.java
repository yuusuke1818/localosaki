package jp.co.osaki.osol.batch.util;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Test;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

/**
 * @author take_suzuki
 *
 */
public class cronUtilTest {

    @Test
    public void test() {

        //get a predefined instance
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);

        //create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);

        //validate expression
        Cron cron;
        cron = parser.parse("*/2 * * * *").validate();

        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("now                              ".concat(Date.from(ZonedDateTime.of(now.toLocalDateTime(), ZoneId.systemDefault()).toInstant()).toString()));

        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        // Get date for last execution  最終実行の日付を取得
        Optional<ZonedDateTime> lastExecution = executionTime.lastExecution(now);
        System.out.println("Get date for last execution      ".concat(Date.from(ZonedDateTime.of(lastExecution.get().toLocalDateTime(), ZoneId.systemDefault()).toInstant()).toString()));

        // Get date for next execution  次の実行の日付を取得
        Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
        System.out.println("Get date for next execution      ".concat(Date.from(ZonedDateTime.of(nextExecution.get().toLocalDateTime(), ZoneId.systemDefault()).toInstant()).toString()));

        // Time from last execution 最後の実行からの時間
        Optional<Duration> timeFromLastExecution = executionTime.timeFromLastExecution(now);
        System.out.println("Time from last execution Seconds ".concat(String.valueOf(timeFromLastExecution.get().getSeconds())));

        // Time to next execution   次の実行までの時間
        Optional<Duration> timeToNextExecution = executionTime.timeToNextExecution(now);
        System.out.println("Time to   next execution Seconds ".concat(String.valueOf(timeToNextExecution.get().getSeconds())));

        for (int i = 0 ; i < 100 ; i++) {
            System.out.println("Seconds - ".concat(String.valueOf(executionTime.timeFromLastExecution(ZonedDateTime.now()).get().getSeconds())));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        for (Entry<CronFieldName, CronField> entry : cron.retrieveFieldsAsMap().entrySet()) {
            System.out.println("Field : " + entry.getValue().getField().toString());
            System.out.println(" Expression : " + entry.getValue().getExpression().asString());
            System.out.println(" StartRange : " + entry.getValue().getConstraints().getStartRange());
            System.out.println(" EndRange : " + entry.getValue().getConstraints().getEndRange());
            for (SpecialChar specialChar : entry.getValue().getConstraints().getSpecialChars()) {
                System.out.println(" SpecialChars : " + specialChar);
            }
            System.out.println("");
        }
        System.out.println("--");
        cron = parser.parse("*/45 * * * 0,1,2,3,4").validate();
        executionTime = ExecutionTime.forCron(cron);

        for (Entry<CronFieldName, CronField> entry : cron.retrieveFieldsAsMap().entrySet()) {
            System.out.println("Field : " + entry.getValue().getField().toString());
            System.out.println(" Expression : " + entry.getValue().getExpression().asString());
            System.out.println(" StartRange : " + entry.getValue().getConstraints().getStartRange());
            System.out.println(" EndRange : " + entry.getValue().getConstraints().getEndRange());
            for (SpecialChar specialChar : entry.getValue().getConstraints().getSpecialChars()) {
                System.out.println(" SpecialChars : " + specialChar);
            }
            System.out.println("");
        }

    }

    @Test
    public void test2() {

        //get a predefined instance
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX);

        //create a parser based on provided definition
        CronParser parser = new CronParser(cronDefinition);

        //validate expression
        Cron cron = parser.parse("*/1 * * * *").validate();
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        for (int i = 0 ; i < 180 ; i++) {
            System.out.print("now ".concat(Date.from(ZonedDateTime.of(ZonedDateTime.now().toLocalDateTime(), ZoneId.systemDefault()).toInstant()).toString()));
            System.out.println(" Seconds - ".concat(String.valueOf(executionTime.timeFromLastExecution(ZonedDateTime.now()).get().getSeconds())));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    @Test
    public void tes3() {

        try {
            CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING));
            parser.parse("0 0 1 1 12 ?").validate();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
