package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternNoResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternNoResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleSetLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * getPatternNoのテストをするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest012 {

    @Rule
    public TestName testName = new TestName();

    private SmControlScheduleSetLogListTestData smControlScheduleSetLogListTestData = new SmControlScheduleSetLogListTestData();
    private CommonSchedulePatternNoResultTestData commonSchedulePatternNoResultTestData = new CommonSchedulePatternNoResultTestData();

    Date currentDateNotSpecificDate = DateUtility.conversionDate("20180120", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateNoSet = DateUtility.conversionDate("20180601", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate1 = DateUtility.conversionDate("20180201", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate2 = DateUtility.conversionDate("20180202", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate3 = DateUtility.conversionDate("20180203", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate4 = DateUtility.conversionDate("20180204", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate5 = DateUtility.conversionDate("20180205", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate6 = DateUtility.conversionDate("20180206", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate7 = DateUtility.conversionDate("20180207", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate8 = DateUtility.conversionDate("20180208", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate9 = DateUtility.conversionDate("20180209", DateUtility.DATE_FORMAT_YYYYMMDD);
    Date currentDateSpecificDate10 = DateUtility.conversionDate("20180210", DateUtility.DATE_FORMAT_YYYYMMDD);
    Long currentSmControlScheduleLogId = Long.valueOf("1");
    String currentDayOfWeekSun = DateUtility.DAY_OF_WEEK.SUNDAY.getName();
    String currentDayOfWeekMon = DateUtility.DAY_OF_WEEK.MONDAY.getName();
    String currentDayOfWeekTue = DateUtility.DAY_OF_WEEK.TUESDAY.getName();
    String currentDayOfWeekWed = DateUtility.DAY_OF_WEEK.WEDNESDAY.getName();
    String currentDayOfWeekThu = DateUtility.DAY_OF_WEEK.THURSDAY.getName();
    String currentDayOfWeekFri = DateUtility.DAY_OF_WEEK.FRIDAY.getName();
    String currentDayOfWeekSat = DateUtility.DAY_OF_WEEK.SATURDAY.getName();
    String currentDayOfWeekMiss = "曜日対象外";
    Boolean currentHolidayFlgTrue = Boolean.TRUE;
    Boolean currentHolidayFlgFalse = Boolean.FALSE;

    @Rule
    public TestRule rule = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            System.out.println(testName.getMethodName().concat("：START"));
        }

        @Override
        protected void after() {
            System.out.println(testName.getMethodName().concat("：END"));
        }

    };

    /**
     * 設定履歴NULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getNullList(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_1()));
    }

    /**
     * 設定履歴EMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getEmptyList(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_2()));
    }

    /**
     * 現在日付NULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(null, currentSmControlScheduleLogId, currentDayOfWeekTue, currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_3()));
    }

    /**
     * 曜日指定NULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, null, currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_4()));
    }

    /**
     * 曜日指定EMPTY
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern1_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, "", currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_5()));
    }

    /**
     * 祝日フラグNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun, null,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_6()));
    }

    /**
     * スケジュール履歴IDNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, null, currentDayOfWeekSun, currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern1_7()));
    }

    /**
     * 対象スケジュールデータなし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNoSet, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern2_1()));
    }

    /**
     * 特定日×・祝日×・曜日指定誤り
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_1()));
    }

    /**
     * 特定日×・祝日×・日曜日・パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_2()));
    }

    /**
     * 特定日×・祝日×・月曜日・パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_3()));
    }

    /**
     * 特定日×・祝日×・火曜日・パターン有効
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern3_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_4()));
    }

    /**
     * 特定日×・祝日×・水曜日・パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_5()));
    }

    /**
     * 特定日×・祝日×・木曜日・パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_6()));
    }

    /**
     * 特定日×・祝日×・金曜日・パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_7()));
    }

    /**
     * 特定日×・祝日×・土曜日・パターン有効
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern3_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_8()));
    }

    /**
     * 特定日×・祝日×・日曜日・パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_9()));
    }

    /**
     * 特定日×・祝日×・月曜日・パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_10()));
    }

    /**
     * 特定日×・祝日×・火曜日・パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_11()));
    }

    /**
     * 特定日×・祝日×・水曜日・パターン無効
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern3_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_12()));
    }

    /**
     * 特定日×・祝日×・木曜日・パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_13() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_13()));
    }

    /**
     * 特定日×・祝日×・金曜日・パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_14() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_14()));
    }

    /**
     * 特定日×・祝日×・土曜日・パターン無効
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern3_15() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlyWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern3_15()));
    }

    /**
     * 特定日×・祝日○・曜日指定誤り・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_1()));
    }

    /**
     * 特定日×・祝日○・曜日指定誤り・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_2()));
    }

    /**
     * 特定日×・祝日○・日曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_3()));
    }

    /**
     * 特定日×・祝日○・月曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_4()));
    }

    /**
     * 特定日×・祝日○・火曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_5()));
    }

    /**
     * 特定日×・祝日○・水曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_6()));
    }

    /**
     * 特定日×・祝日○・木曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_7()));
    }

    /**
     * 特定日×・祝日○・金曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_8()));
    }

    /**
     * 特定日×・祝日○・土曜日・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_9()));
    }

    /**
     * 特定日×・祝日○・日曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_10()));
    }

    /**
     * 特定日×・祝日○・月曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_11()));
    }

    /**
     * 特定日×・祝日○・火曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_12()));
    }

    /**
     * 特定日×・祝日○・水曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_13() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_13()));
    }

    /**
     * 特定日×・祝日○・木曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_14() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_14()));
    }

    /**
     * 特定日×・祝日○・金曜日・祝日パターン無効・曜日パターン有効
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testPattern4_15() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_15()));
    }

    /**
     * 特定日×・祝日○・土曜日・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_16() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getOnlyHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_16()));
    }

    /**
     * 特定日×・祝日○・日曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_17() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_17()));
    }

    /**
     * 特定日×・祝日○・月曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_18() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_18()));
    }

    /**
     * 特定日×・祝日○・火曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_19() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_19()));
    }

    /**
     * 特定日×・祝日○・水曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_20() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_20()));
    }

    /**
     * 特定日×・祝日○・木曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_21() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_21()));
    }

    /**
     * 特定日×・祝日○・金曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_22() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_22()));
    }

    /**
     * 特定日×・祝日○・土曜日・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_23() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateNotSpecificDate, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getHolidayAndWeekDayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern4_23()));
    }

    /**
     * 特定日1・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_1()));
    }

    /**
     * 特定日2・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_2()));
    }

    /**
     * 特定日3・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_3()));
    }

    /**
     * 特定日4・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_4()));
    }

    /**
     * 特定日5・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_5()));
    }

    /**
     * 特定日6・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_6()));
    }

    /**
     * 特定日7・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_7()));
    }

    /**
     * 特定日8・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_8()));
    }

    /**
     * 特定日9・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_9()));
    }

    /**
     * 特定日10・祝日×・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_10()));
    }

    /**
     * 特定日1・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_11()));
    }

    /**
     * 特定日2・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_12()));
    }

    /**
     * 特定日3・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_13() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_13()));
    }

    /**
     * 特定日4・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_14() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_14()));
    }

    /**
     * 特定日5・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_15() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_15()));
    }

    /**
     * 特定日6・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_16() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_16()));
    }

    /**
     * 特定日7・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_17() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_17()));
    }

    /**
     * 特定日8・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_18() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_18()));
    }

    /**
     * 特定日9・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_19() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_19()));
    }

    /**
     * 特定日10・祝日×・曜日指定誤り・特定日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_20() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_20()));
    }

    /**
     * 特定日1・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_21() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_21()));
    }

    /**
     * 特定日2・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_22() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_22()));
    }

    /**
     * 特定日3・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_23() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_23()));
    }

    /**
     * 特定日4・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_24() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_24()));
    }

    /**
     * 特定日5・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_25() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_25()));
    }

    /**
     * 特定日6・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_26() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_26()));
    }

    /**
     * 特定日7・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_27() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_27()));
    }

    /**
     * 特定日8・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_28() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_28()));
    }

    /**
     * 特定日9・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_29() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_29()));
    }

    /**
     * 特定日10・祝日×・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_30() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_30()));
    }

    /**
     * 特定日1・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_31() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_31()));
    }

    /**
     * 特定日2・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_32() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_32()));
    }

    /**
     * 特定日3・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_33() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_33()));
    }

    /**
     * 特定日4・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_34() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_34()));
    }

    /**
     * 特定日5・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_35() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_35()));
    }

    /**
     * 特定日6・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_36() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_36()));
    }

    /**
     * 特定日7・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_37() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_37()));
    }

    /**
     * 特定日8・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_38() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_38()));
    }

    /**
     * 特定日9・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_39() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_39()));
    }

    /**
     * 特定日10・祝日×・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_40() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_40()));
    }

    /**
     * 特定日1・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_41() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_41()));
    }

    /**
     * 特定日2・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_42() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_42()));
    }

    /**
     * 特定日3・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_43() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_43()));
    }

    /**
     * 特定日4・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_44() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_44()));
    }

    /**
     * 特定日5・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_45() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_45()));
    }

    /**
     * 特定日6・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_46() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_46()));
    }

    /**
     * 特定日7・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_47() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_47()));
    }

    /**
     * 特定日8・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_48() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_48()));
    }

    /**
     * 特定日9・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_49() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_49()));
    }

    /**
     * 特定日10・祝日×・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_50() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_50()));
    }

    /**
     * 特定日1・祝日×・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_51() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_51()));
    }

    /**
     * 特定日2・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_52() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_52()));
    }

    /**
     * 特定日3・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_53() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_53()));
    }

    /**
     * 特定日4・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_54() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_54()));
    }

    /**
     * 特定日5・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_55() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_55()));
    }

    /**
     * 特定日6・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_56() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_56()));
    }

    /**
     * 特定日7・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_57() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_57()));
    }

    /**
     * 特定日8・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_58() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_58()));
    }

    /**
     * 特定日9・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_59() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_59()));
    }

    /**
     * 特定日10・祝日×・水曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_60() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_60()));
    }

    /**
     * 特定日1・祝日×・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_61() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_61()));
    }

    /**
     * 特定日2・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_62() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_62()));
    }

    /**
     * 特定日3・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_63() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_63()));
    }

    /**
     * 特定日4・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_64() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_64()));
    }

    /**
     * 特定日5・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_65() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_65()));
    }

    /**
     * 特定日6・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_66() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_66()));
    }

    /**
     * 特定日7・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_67() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_67()));
    }

    /**
     * 特定日8・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_68() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_68()));
    }

    /**
     * 特定日9・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_69() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_69()));
    }

    /**
     * 特定日10・祝日×・木曜日・特定日パターン有効
     */
    @Test
    public void testPattern5_70() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_70()));
    }

    /**
     * 特定日1・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_71() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_71()));
    }

    /**
     * 特定日2・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_72() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_72()));
    }

    /**
     * 特定日3・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_73() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_73()));
    }

    /**
     * 特定日4・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_74() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_74()));
    }

    /**
     * 特定日5・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_75() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_75()));
    }

    /**
     * 特定日6・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_76() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_76()));
    }

    /**
     * 特定日7・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_77() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_77()));
    }

    /**
     * 特定日8・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_78() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_78()));
    }

    /**
     * 特定日9・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_79() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_79()));
    }

    /**
     * 特定日10・祝日×・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_80() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_80()));
    }

    /**
     * 特定日1・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_81() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_81()));
    }

    /**
     * 特定日2・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_82() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_82()));
    }

    /**
     * 特定日3・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_83() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_83()));
    }

    /**
     * 特定日4・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_84() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_84()));
    }

    /**
     * 特定日5・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_85() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_85()));
    }

    /**
     * 特定日6・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_86() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_86()));
    }

    /**
     * 特定日7・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_87() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_87()));
    }

    /**
     * 特定日8・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_88() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_88()));
    }

    /**
     * 特定日9・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_89() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_89()));
    }

    /**
     * 特定日10・祝日×・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_90() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_90()));
    }

    /**
     * 特定日1・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_91() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_91()));
    }

    /**
     * 特定日2・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_92() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_92()));

    }

    /**
     * 特定日3・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_93() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_93()));
    }

    /**
     * 特定日4・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_94() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_94()));
    }

    /**
     * 特定日5・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_95() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_95()));
    }

    /**
     * 特定日6・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_96() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_96()));
    }

    /**
     * 特定日7・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_97() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_97()));
    }

    /**
     * 特定日8・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_98() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_98()));
    }

    /**
     * 特定日9・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_99() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_99()));
    }

    /**
     * 特定日10・祝日×・日曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_100() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_100()));
    }

    /**
     * 特定日1・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_101() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_101()));
    }

    /**
     * 特定日2・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_102() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_102()));
    }

    /**
     * 特定日3・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_103() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_103()));
    }

    /**
     * 特定日4・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_104() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_104()));
    }

    /**
     * 特定日5・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_105() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_105()));
    }

    /**
     * 特定日6・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_106() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_106()));
    }

    /**
     * 特定日7・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_107() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_107()));
    }

    /**
     * 特定日8・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_108() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_108()));
    }

    /**
     * 特定日9・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_109() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_109()));
    }

    /**
     * 特定日10・祝日×・月曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_110() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_110()));
    }

    /**
     * 特定日1・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_111() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_111()));
    }

    /**
     * 特定日2・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_112() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_112()));

    }

    /**
     * 特定日3・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_113() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_113()));
    }

    /**
     * 特定日4・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_114() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_114()));
    }

    /**
     * 特定日5・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_115() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_115()));
    }

    /**
     * 特定日6・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_116() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_116()));
    }

    /**
     * 特定日7・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_117() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_117()));
    }

    /**
     * 特定日8・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_118() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_118()));
    }

    /**
     * 特定日9・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_119() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_119()));
    }

    /**
     * 特定日10・祝日×・火曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_120() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_120()));
    }

    /**
     * 特定日1・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_121() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_121()));
    }

    /**
     * 特定日2・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_122() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_122()));
    }

    /**
     * 特定日3・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_123() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_123()));
    }

    /**
     * 特定日4・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_124() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_124()));
    }

    /**
     * 特定日5・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_125() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_125()));
    }

    /**
     * 特定日6・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_126() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_126()));
    }

    /**
     * 特定日7・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_127() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_127()));
    }

    /**
     * 特定日8・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_128() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_128()));
    }

    /**
     * 特定日9・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_129() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_129()));
    }

    /**
     * 特定日10・祝日×・水曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_130() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_130()));
    }

    /**
     * 特定日1・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_131() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_131()));
    }

    /**
     * 特定日2・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_132() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_132()));
    }

    /**
     * 特定日3・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_133() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_133()));
    }

    /**
     * 特定日4・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_134() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_134()));
    }

    /**
     * 特定日5・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_135() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_135()));
    }

    /**
     * 特定日6・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_136() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_136()));
    }

    /**
     * 特定日7・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_137() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_137()));
    }

    /**
     * 特定日8・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_138() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_138()));
    }

    /**
     * 特定日9・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_139() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_139()));
    }

    /**
     * 特定日10・祝日×・木曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_140() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_140()));
    }

    /**
     * 特定日1・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_141() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_141()));
    }

    /**
     * 特定日2・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_142() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_142()));
    }

    /**
     * 特定日3・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_143() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_143()));
    }

    /**
     * 特定日4・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_144() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_144()));
    }

    /**
     * 特定日5・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_145() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_145()));
    }

    /**
     * 特定日6・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_146() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_146()));
    }

    /**
     * 特定日7・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_147() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_147()));
    }

    /**
     * 特定日8・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_148() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_148()));
    }

    /**
     * 特定日9・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_149() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_149()));
    }

    /**
     * 特定日10・祝日×・金曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_150() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_150()));
    }

    /**
     * 特定日1・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_151() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_151()));
    }

    /**
     * 特定日2・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_152() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_152()));
    }

    /**
     * 特定日3・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_153() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_153()));
    }

    /**
     * 特定日4・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_154() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_154()));
    }

    /**
     * 特定日5・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_155() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_155()));
    }

    /**
     * 特定日6・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_156() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_156()));
    }

    /**
     * 特定日7・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_157() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_157()));
    }

    /**
     * 特定日8・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_158() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_158()));
    }

    /**
     * 特定日9・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_159() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_159()));
    }

    /**
     * 特定日10・祝日×・土曜日・特定日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_160() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_160()));
    }

    /**
     * 特定日1・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_161() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_161()));
    }

    /**
     * 特定日2・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_162() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_162()));
    }

    /**
     * 特定日3・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_163() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_163()));
    }

    /**
     * 特定日4・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_164() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_164()));
    }

    /**
     * 特定日5・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_165() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_165()));
    }

    /**
     * 特定日6・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_166() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_166()));
    }

    /**
     * 特定日7・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_167() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_167()));
    }

    /**
     * 特定日8・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_168() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_168()));
    }

    /**
     * 特定日9・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_169() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_169()));
    }

    /**
     * 特定日10・祝日×・日曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_170() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_170()));
    }

    /**
     * 特定日1・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_171() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_171()));
    }

    /**
     * 特定日2・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_172() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_172()));
    }

    /**
     * 特定日3・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_173() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_173()));
    }

    /**
     * 特定日4・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_174() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_174()));
    }

    /**
     * 特定日5・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_175() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_175()));
    }

    /**
     * 特定日6・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_176() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_176()));
    }

    /**
     * 特定日7・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_177() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_177()));
    }

    /**
     * 特定日8・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_178() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_178()));
    }

    /**
     * 特定日9・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_179() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_179()));
    }

    /**
     * 特定日10・祝日×・月曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_180() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_180()));
    }

    /**
     * 特定日1・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_181() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_181()));
    }

    /**
     * 特定日2・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_182() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_182()));
    }

    /**
     * 特定日3・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_183() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_183()));
    }

    /**
     * 特定日4・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_184() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_184()));
    }

    /**
     * 特定日5・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_185() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_185()));
    }

    /**
     * 特定日6・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_186() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_186()));
    }

    /**
     * 特定日7・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_187() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_187()));
    }

    /**
     * 特定日8・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_188() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_188()));
    }

    /**
     * 特定日9・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_189() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_189()));
    }

    /**
     * 特定日10・祝日×・火曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_190() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_190()));
    }

    /**
     * 特定日1・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_191() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_191()));
    }

    /**
     * 特定日2・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_192() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_192()));
    }

    /**
     * 特定日3・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_193() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_193()));
    }

    /**
     * 特定日4・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_194() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_194()));
    }

    /**
     * 特定日5・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_195() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_195()));
    }

    /**
     * 特定日6・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_196() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_196()));
    }

    /**
     * 特定日7・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_197() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_197()));
    }

    /**
     * 特定日8・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_198() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_198()));
    }

    /**
     * 特定日9・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_199() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_199()));
    }

    /**
     * 特定日10・祝日×・水曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_200() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_200()));
    }

    /**
     * 特定日1・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_201() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_201()));
    }

    /**
     * 特定日2・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_202() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_202()));
    }

    /**
     * 特定日3・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_203() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_203()));
    }

    /**
     * 特定日4・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_204() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_204()));
    }

    /**
     * 特定日5・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_205() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_205()));
    }

    /**
     * 特定日6・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_206() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_206()));
    }

    /**
     * 特定日7・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_207() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_207()));
    }

    /**
     * 特定日8・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_208() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_208()));
    }

    /**
     * 特定日9・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_209() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_209()));
    }

    /**
     * 特定日10・祝日×・木曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_210() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_210()));
    }

    /**
     * 特定日1・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_211() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_211()));
    }

    /**
     * 特定日2・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_212() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_212()));
    }

    /**
     * 特定日3・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_213() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_213()));
    }

    /**
     * 特定日4・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_214() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_214()));
    }

    /**
     * 特定日5・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_215() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_215()));
    }

    /**
     * 特定日6・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_216() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_216()));
    }

    /**
     * 特定日7・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_217() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_217()));
    }

    /**
     * 特定日8・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_218() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_218()));
    }

    /**
     * 特定日9・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_219() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_219()));
    }

    /**
     * 特定日10・祝日×・金曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_220() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_220()));
    }

    /**
     * 特定日1・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_221() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_221()));
    }

    /**
     * 特定日2・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_222() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_222()));
    }

    /**
     * 特定日3・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_223() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_223()));
    }

    /**
     * 特定日4・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_224() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_224()));
    }

    /**
     * 特定日5・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_225() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_225()));
    }

    /**
     * 特定日6・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_226() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_226()));
    }

    /**
     * 特定日7・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_227() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_227()));
    }

    /**
     * 特定日8・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_228() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_228()));
    }

    /**
     * 特定日9・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_229() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_229()));
    }

    /**
     * 特定日10・祝日×・土曜日・特定日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern5_230() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgFalse,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern5_230()));
    }

    /**
     * 特定日1・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_1()));
    }

    /**
     * 特定日2・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_2()));
    }

    /**
     * 特定日3・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_3()));
    }

    /**
     * 特定日4・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_4()));
    }

    /**
     * 特定日5・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_5()));
    }

    /**
     * 特定日6・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_6()));
    }

    /**
     * 特定日7・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_7()));
    }

    /**
     * 特定日8・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_8()));
    }

    /**
     * 特定日9・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_9()));
    }

    /**
     * 特定日10・祝日○・曜日指定誤り・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue, smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_10()));
    }

    /**
     * 特定日1・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_11()));
    }

    /**
     * 特定日2・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_12()));

    }

    /**
     * 特定日3・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_13() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_13()));
    }

    /**
     * 特定日4・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_14() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_14()));
    }

    /**
     * 特定日5・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_15() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_15()));
    }

    /**
     * 特定日6・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_16() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_16()));
    }

    /**
     * 特定日7・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_17() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_17()));
    }

    /**
     * 特定日8・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_18() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_18()));
    }

    /**
     * 特定日9・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_19() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_19()));
    }

    /**
     * 特定日10・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_20() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_20()));
    }

    /**
     * 特定日1・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_21() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_21()));
    }

    /**
     * 特定日2・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_22() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_22()));
    }

    /**
     * 特定日3・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_23() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_23()));
    }

    /**
     * 特定日4・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_24() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_24()));
    }

    /**
     * 特定日5・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_25() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_25()));
    }

    /**
     * 特定日6・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_26() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_26()));
    }

    /**
     * 特定日7・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_27() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_27()));
    }

    /**
     * 特定日8・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_28() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_28()));
    }

    /**
     * 特定日9・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_29() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_29()));
    }

    /**
     * 特定日10・祝日○・曜日指定誤り・特定日パターン無効・祝日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_30() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMiss,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_30()));
    }

    /**
     * 特定日1・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_31() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_31()));
    }

    /**
     * 特定日2・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_32() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_32()));
    }

    /**
     * 特定日3・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_33() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_33()));
    }

    /**
     * 特定日4・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_34() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_34()));
    }

    /**
     * 特定日5・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_35() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_35()));
    }

    /**
     * 特定日6・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_36() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_36()));
    }

    /**
     * 特定日7・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_37() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_37()));
    }

    /**
     * 特定日8・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_38() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_38()));
    }

    /**
     * 特定日9・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_39() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_39()));
    }

    /**
     * 特定日10・祝日○・日曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_40() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_40()));
    }

    /**
     * 特定日1・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_41() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_41()));
    }

    /**
     * 特定日2・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_42() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_42()));
    }

    /**
     * 特定日3・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_43() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_43()));
    }

    /**
     * 特定日4・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_44() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_44()));
    }

    /**
     * 特定日5・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_45() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_45()));
    }

    /**
     * 特定日6・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_46() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_46()));
    }

    /**
     * 特定日7・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_47() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_47()));
    }

    /**
     * 特定日8・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_48() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_48()));
    }

    /**
     * 特定日9・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_49() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_49()));
    }

    /**
     * 特定日10・祝日○・月曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_50() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_50()));
    }

    /**
     * 特定日1・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_51() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_51()));
    }

    /**
     * 特定日2・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_52() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_52()));
    }

    /**
     * 特定日3・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_53() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_53()));
    }

    /**
     * 特定日4・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_54() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_54()));
    }

    /**
     * 特定日5・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_55() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_55()));
    }

    /**
     * 特定日6・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_56() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_6()));
    }

    /**
     * 特定日7・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_57() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_57()));
    }

    /**
     * 特定日8・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_58() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_58()));
    }

    /**
     * 特定日9・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_59() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_59()));
    }

    /**
     * 特定日10・祝日○・火曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_60() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_60()));
    }

    /**
     * 特定日1・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_61() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_61()));
    }

    /**
     * 特定日2・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_62() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_62()));
    }

    /**
     * 特定日3・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_63() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_63()));
    }

    /**
     * 特定日4・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_64() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_64()));
    }

    /**
     * 特定日5・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_65() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_65()));
    }

    /**
     * 特定日6・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_66() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_66()));
    }

    /**
     * 特定日7・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_67() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_67()));
    }

    /**
     * 特定日8・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_68() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_68()));
    }

    /**
     * 特定日9・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_69() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_69()));
    }

    /**
     * 特定日10・祝日○・水曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_70() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_70()));
    }

    /**
     * 特定日1・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_71() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_71()));
    }

    /**
     * 特定日2・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_72() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_72()));
    }

    /**
     * 特定日3・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_73() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_73()));
    }

    /**
     * 特定日4・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_74() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_74()));
    }

    /**
     * 特定日5・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_75() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_75()));
    }

    /**
     * 特定日6・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_76() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_76()));
    }

    /**
     * 特定日7・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_77() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_77()));
    }

    /**
     * 特定日8・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_78() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_78()));
    }

    /**
     * 特定日9・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_79() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_79()));
    }

    /**
     * 特定日10・祝日○・木曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_80() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_80()));
    }

    /**
     * 特定日1・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_81() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_81()));
    }

    /**
     * 特定日2・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_82() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_82()));
    }

    /**
     * 特定日3・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_83() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_83()));
    }

    /**
     * 特定日4・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_84() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_84()));
    }

    /**
     * 特定日5・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_85() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_85()));
    }

    /**
     * 特定日6・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_86() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_86()));
    }

    /**
     * 特定日7・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_87() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_87()));
    }

    /**
     * 特定日8・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_88() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_88()));
    }

    /**
     * 特定日9・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_89() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_89()));
    }

    /**
     * 特定日10・祝日○・金曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_90() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_90()));
    }

    /**
     * 特定日1・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_91() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_91()));
    }

    /**
     * 特定日2・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_92() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_92()));
    }

    /**
     * 特定日3・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_93() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_93()));
    }

    /**
     * 特定日4・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_94() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_94()));
    }

    /**
     * 特定日5・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_95() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_95()));
    }

    /**
     * 特定日6・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_96() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_96()));
    }

    /**
     * 特定日7・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_97() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_97()));
    }

    /**
     * 特定日8・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_98() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_98()));
    }

    /**
     * 特定日9・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_99() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_99()));
    }

    /**
     * 特定日10・祝日○・土曜日・特定日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_100() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllValidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_100()));
    }

    /**
     * 特定日1・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_101() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_101()));
    }

    /**
     * 特定日2・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_102() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_102()));
    }

    /**
     * 特定日3・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_103() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_103()));
    }

    /**
     * 特定日4・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_104() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_104()));
    }

    /**
     * 特定日5・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_105() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_105()));
    }

    /**
     * 特定日6・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_106() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_106()));
    }

    /**
     * 特定日7・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_107() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_107()));
    }

    /**
     * 特定日8・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_108() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_108()));
    }

    /**
     * 特定日9・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_109() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_108()));
    }

    /**
     * 特定日10・祝日○・日曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_110() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_110()));
    }

    /**
     * 特定日1・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_111() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_111()));
    }

    /**
     * 特定日2・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_112() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_112()));
    }

    /**
     * 特定日3・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_113() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_113()));
    }

    /**
     * 特定日4・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_114() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_114()));
    }

    /**
     * 特定日5・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_115() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_115()));
    }

    /**
     * 特定日6・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_116() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_116()));
    }

    /**
     * 特定日7・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_117() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_117()));
    }

    /**
     * 特定日8・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_118() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_118()));
    }

    /**
     * 特定日9・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_119() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_119()));
    }

    /**
     * 特定日10・祝日○・月曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_120() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_120()));
    }

    /**
     * 特定日1・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_121() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_121()));
    }

    /**
     * 特定日2・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_122() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_122()));
    }

    /**
     * 特定日3・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_123() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_123()));
    }

    /**
     * 特定日4・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_124() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_124()));
    }

    /**
     * 特定日5・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_125() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_125()));
    }

    /**
     * 特定日6・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_126() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_126()));
    }

    /**
     * 特定日7・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_127() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_127()));
    }

    /**
     * 特定日8・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_128() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_128()));
    }

    /**
     * 特定日9・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_129() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_129()));
    }

    /**
     * 特定日10・祝日○・火曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_130() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_130()));
    }

    /**
     * 特定日1・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_131() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_131()));
    }

    /**
     * 特定日2・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_132() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_132()));
    }

    /**
     * 特定日3・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_133() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_133()));
    }

    /**
     * 特定日4・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_134() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_134()));
    }

    /**
     * 特定日5・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_135() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_135()));
    }

    /**
     * 特定日6・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_136() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_136()));
    }

    /**
     * 特定日7・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_137() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_137()));
    }

    /**
     * 特定日8・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_138() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_138()));
    }

    /**
     * 特定日9・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_139() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_139()));
    }

    /**
     * 特定日10・祝日○・水曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_140() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_140()));
    }

    /**
     * 特定日1・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_141() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_141()));
    }

    /**
     * 特定日2・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_142() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_142()));
    }

    /**
     * 特定日3・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_143() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_143()));
    }

    /**
     * 特定日4・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_144() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_144()));
    }

    /**
     * 特定日5・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_145() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_145()));
    }

    /**
     * 特定日6・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_146() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_146()));
    }

    /**
     * 特定日7・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_147() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_147()));
    }

    /**
     * 特定日8・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_148() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_148()));
    }

    /**
     * 特定日9・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_149() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_149()));
    }

    /**
     * 特定日10・祝日○・木曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_150() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_150()));
    }

    /**
     * 特定日1・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_151() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_151()));
    }

    /**
     * 特定日2・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_152() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_152()));
    }

    /**
     * 特定日3・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_153() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_153()));
    }

    /**
     * 特定日4・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_154() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_154()));
    }

    /**
     * 特定日5・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_155() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_155()));
    }

    /**
     * 特定日6・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_156() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_156()));
    }

    /**
     * 特定日7・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_157() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_157()));
    }

    /**
     * 特定日8・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_158() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_158()));
    }

    /**
     * 特定日9・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_159() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_159()));
    }

    /**
     * 特定日10・祝日○・金曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_160() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_160()));
    }

    /**
     * 特定日1・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_161() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_161()));
    }

    /**
     * 特定日2・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_162() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_162()));
    }

    /**
     * 特定日3・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_163() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_163()));
    }

    /**
     * 特定日4・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_164() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_164()));
    }

    /**
     * 特定日5・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_165() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_165()));
    }

    /**
     * 特定日6・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_166() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_166()));
    }

    /**
     * 特定日7・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_167() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_167()));
    }

    /**
     * 特定日8・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_168() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_168()));
    }

    /**
     * 特定日9・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_169() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_169()));
    }

    /**
     * 特定日10・祝日○・土曜日・特定日パターン無効・祝日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_170() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getOnlySpecificPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_170()));
    }

    /**
     * 特定日1・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_171() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_171()));
    }

    /**
     * 特定日2・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_172() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_172()));
    }

    /**
     * 特定日3・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_173() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_173()));
    }

    /**
     * 特定日4・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_174() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_174()));
    }

    /**
     * 特定日5・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_175() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_175()));
    }

    /**
     * 特定日6・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_176() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_176()));
    }

    /**
     * 特定日7・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_177() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_177()));
    }

    /**
     * 特定日8・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_178() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_178()));
    }

    /**
     * 特定日9・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_179() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_179()));
    }

    /**
     * 特定日10・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_180() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_180()));
    }

    /**
     * 特定日1・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_181() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_181()));
    }

    /**
     * 特定日2・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_182() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_182()));
    }

    /**
     * 特定日3・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_183() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_183()));
    }

    /**
     * 特定日4・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_184() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_184()));
    }

    /**
     * 特定日5・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_185() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_185()));
    }

    /**
     * 特定日6・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_186() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_186()));
    }

    /**
     * 特定日7・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_187() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_187()));
    }

    /**
     * 特定日8・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_188() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_188()));
    }

    /**
     * 特定日9・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_189() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_189()));
    }

    /**
     * 特定日10・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_190() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_190()));
    }

    /**
     * 特定日1・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_191() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_191()));
    }

    /**
     * 特定日2・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_192() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_192()));
    }

    /**
     * 特定日3・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_193() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_193()));
    }

    /**
     * 特定日4・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_194() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_194()));
    }

    /**
     * 特定日5・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_195() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_195()));
    }

    /**
     * 特定日6・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_196() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_196()));
    }

    /**
     * 特定日7・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_197() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_197()));
    }

    /**
     * 特定日8・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_198() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_198()));
    }

    /**
     * 特定日9・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_199() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_199()));
    }

    /**
     * 特定日10・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_200() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_200()));
    }

    /**
     * 特定日1・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_201() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_201()));
    }

    /**
     * 特定日2・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_202() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_202()));
    }

    /**
     * 特定日3・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_203() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_203()));
    }

    /**
     * 特定日4・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_204() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_204()));
    }

    /**
     * 特定日5・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_205() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_205()));
    }

    /**
     * 特定日6・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_206() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_206()));
    }

    /**
     * 特定日7・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_207() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_207()));
    }

    /**
     * 特定日8・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_208() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_208()));
    }

    /**
     * 特定日9・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_209() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_209()));
    }

    /**
     * 特定日10・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_210() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_210()));
    }

    /**
     * 特定日1・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_211() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_211()));
    }

    /**
     * 特定日2・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_212() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_212()));
    }

    /**
     * 特定日3・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_213() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_213()));
    }

    /**
     * 特定日4・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_214() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_214()));
    }

    /**
     * 特定日5・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_215() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_215()));
    }

    /**
     * 特定日6・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_216() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_216()));
    }

    /**
     * 特定日7・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_217() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_217()));
    }

    /**
     * 特定日8・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_218() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_218()));
    }

    /**
     * 特定日9・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_219() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_219()));
    }

    /**
     * 特定日10・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_220() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_220()));
    }

    /**
     * 特定日1・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_221() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_221()));
    }

    /**
     * 特定日2・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_222() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_222()));
    }

    /**
     * 特定日3・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_223() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_223()));
    }

    /**
     * 特定日4・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_224() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_224()));
    }

    /**
     * 特定日5・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_225() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_225()));
    }

    /**
     * 特定日6・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_226() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_226()));
    }

    /**
     * 特定日7・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_227() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_227()));
    }

    /**
     * 特定日8・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_228() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_228()));
    }

    /**
     * 特定日9・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_229() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_229()));
    }

    /**
     * 特定日10・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_230() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_230()));
    }

    /**
     * 特定日1・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_231() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_231()));
    }

    /**
     * 特定日2・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_232() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_232()));
    }

    /**
     * 特定日3・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_233() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_233()));
    }

    /**
     * 特定日4・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_234() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_234()));
    }

    /**
     * 特定日5・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_235() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_235()));
    }

    /**
     * 特定日6・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_236() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_236()));
    }

    /**
     * 特定日7・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_237() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_237()));
    }

    /**
     * 特定日8・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_238() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_238()));
    }

    /**
     * 特定日9・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_239() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_239()));
    }

    /**
     * 特定日10・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン有効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_240() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getSpecificAndHolidayPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_240()));
    }

    /**
     * 特定日1・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_241() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_241()));
    }

    /**
     * 特定日2・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_242() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_242()));
    }

    /**
     * 特定日3・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_243() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_243()));
    }

    /**
     * 特定日4・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_244() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_244()));
    }

    /**
     * 特定日5・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_245() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_245()));
    }

    /**
     * 特定日6・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_246() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_246()));
    }

    /**
     * 特定日7・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_247() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_247()));
    }

    /**
     * 特定日8・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_248() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_248()));
    }

    /**
     * 特定日9・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_249() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_249()));
    }

    /**
     * 特定日10・祝日○・日曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_250() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSun,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_250()));
    }

    /**
     * 特定日1・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_251() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_251()));
    }

    /**
     * 特定日2・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_252() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_252()));
    }

    /**
     * 特定日3・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_253() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_253()));
    }

    /**
     * 特定日4・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_254() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_254()));
    }

    /**
     * 特定日5・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_255() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_255()));
    }

    /**
     * 特定日6・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_256() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_256()));
    }

    /**
     * 特定日7・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_257() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_257()));
    }

    /**
     * 特定日8・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_258() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_258()));
    }

    /**
     * 特定日9・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_259() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_259()));
    }

    /**
     * 特定日10・祝日○・月曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_260() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekMon,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_260()));
    }

    /**
     * 特定日1・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_261() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_261()));
    }

    /**
     * 特定日2・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_262() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_262()));
    }

    /**
     * 特定日3・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_263() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_263()));
    }

    /**
     * 特定日4・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_264() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_264()));
    }

    /**
     * 特定日5・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_265() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_265()));
    }

    /**
     * 特定日6・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_266() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_266()));
    }

    /**
     * 特定日7・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_267() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_267()));
    }

    /**
     * 特定日8・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_268() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_268()));
    }

    /**
     * 特定日9・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_269() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_269()));
    }

    /**
     * 特定日10・祝日○・火曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_270() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekTue,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_270()));
    }

    /**
     * 特定日1・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_271() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_271()));
    }

    /**
     * 特定日2・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_272() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_272()));
    }

    /**
     * 特定日3・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_273() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_273()));
    }

    /**
     * 特定日4・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_274() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_274()));
    }

    /**
     * 特定日5・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_275() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_275()));
    }

    /**
     * 特定日6・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_276() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_276()));
    }

    /**
     * 特定日7・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_277() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_277()));
    }

    /**
     * 特定日8・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_278() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_278()));
    }

    /**
     * 特定日9・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_279() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_279()));
    }

    /**
     * 特定日10・祝日○・水曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_280() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekWed,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_280()));
    }

    /**
     * 特定日1・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_281() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_281()));
    }

    /**
     * 特定日2・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_282() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_282()));
    }

    /**
     * 特定日3・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_283() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_283()));
    }

    /**
     * 特定日4・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_284() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_284()));
    }

    /**
     * 特定日5・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_285() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_285()));
    }

    /**
     * 特定日6・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_286() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_286()));
    }

    /**
     * 特定日7・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_287() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_287()));
    }

    /**
     * 特定日8・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_288() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_288()));
    }

    /**
     * 特定日9・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_289() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_289()));
    }

    /**
     * 特定日10・祝日○・木曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_290() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekThu,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_290()));
    }

    /**
     * 特定日1・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_291() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_291()));
    }

    /**
     * 特定日2・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_292() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_292()));
    }

    /**
     * 特定日3・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_293() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_293()));
    }

    /**
     * 特定日4・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_294() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_294()));
    }

    /**
     * 特定日5・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_295() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_295()));
    }

    /**
     * 特定日6・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_296() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_296()));
    }

    /**
     * 特定日7・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_297() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_297()));
    }

    /**
     * 特定日8・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_298() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_298()));
    }

    /**
     * 特定日9・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_299() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_299()));
    }

    /**
     * 特定日10・祝日○・金曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_300() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekFri,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_300()));
    }

    /**
     * 特定日1・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_301() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate1, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_301()));
    }

    /**
     * 特定日2・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_302() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate2, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_302()));
    }

    /**
     * 特定日3・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_303() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate3, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_303()));
    }

    /**
     * 特定日4・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_304() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate4, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_304()));
    }

    /**
     * 特定日5・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_305() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate5, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_305()));
    }

    /**
     * 特定日6・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_306() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate6, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_306()));
    }

    /**
     * 特定日7・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_307() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate7, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_307()));
    }

    /**
     * 特定日8・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_308() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate8, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_308()));
    }

    /**
     * 特定日9・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_309() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate9, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_309()));
    }

    /**
     * 特定日10・祝日○・土曜日・特定日パターン無効・祝日パターン無効・曜日パターン無効
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern6_310() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(currentDateSpecificDate10, currentSmControlScheduleLogId, currentDayOfWeekSat,
                        currentHolidayFlgTrue,
                        smControlScheduleSetLogListTestData.getAllPatternNoInvalidData(),
                        commonSchedulePatternNoResultTestData.getResultTestPattern6_310()));
    }

    /**
     * テスト結果を取得する
     * @param currentDate
     * @param currentSmControlScheduleLogId
     * @param currentDayOfWeek
     * @param currentHolidayFlg
     * @param scheduleSetLogList
     * @param expectedSet
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(Date currentDate,
            Long currentSmControlScheduleLogId, String currentDayOfWeek,
            Boolean currentHolidayFlg, List<SmControlScheduleSetLogListDetailResultData> scheduleSetLogList,
            LinkedHashSet<CommonSchedulePatternNoResult> expectedSet) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("getPatternNo", Date.class,
                Long.class, String.class, Boolean.class, List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternNoResult> actualSet = (LinkedHashSet<CommonSchedulePatternNoResult>) method
                .invoke(null, currentDate, currentSmControlScheduleLogId, currentDayOfWeek, currentHolidayFlg,
                        scheduleSetLogList);

        List<CommonSchedulePatternNoResult> expectedList;
        List<CommonSchedulePatternNoResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonSchedulePatternNoResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonSchedulePatternNoResult>(actualSet);
        }

        if (expectedList == null) {
            if (actualList == null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else if (expectedList.isEmpty()) {
            if (actualList == null) {
                return Boolean.FALSE;
            } else if (actualList.isEmpty()) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            if (actualList == null || actualList.isEmpty() || expectedList.size() != actualList.size()) {
                return Boolean.FALSE;
            } else {
                for (int i = 0; i < expectedList.size(); i++) {
                    if (expectedList.get(i).getControlLoad().compareTo(actualList.get(i).getControlLoad()) != 0
                            || (expectedList.get(i).getPatternNo() == null && actualList.get(i).getPatternNo() != null)
                            || (expectedList.get(i).getPatternNo() != null && actualList.get(i).getPatternNo() == null)
                            || (expectedList.get(i).getPatternNo() != null
                                    && !expectedList.get(i).getPatternNo().equals(actualList.get(i).getPatternNo()))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
