package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeResultTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createDateChangeResultをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest009 {

    @Rule
    public TestName testName = new TestName();

    private CommonSchedulePatternChangeResultTestData commonSchedulePatternChangeResultTestData = new CommonSchedulePatternChangeResultTestData();
    private Timestamp settingUpdateDateTimeFrom = new Timestamp(
            DateUtility.conversionDate("201812011234", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
    private Timestamp settingUpdateDateTimeSameTo = settingUpdateDateTimeFrom;
    private Timestamp settingUpdateDateTimeSameConflictTo = new Timestamp(
            DateUtility.conversionDate("201812171400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
    private Timestamp settingUpdateDateTimeHourTo = new Timestamp(
            DateUtility.plusHour(new Date(settingUpdateDateTimeFrom.getTime()), 5).getTime());
    private Timestamp settingUpdateDateTimeHourConflictTo = new Timestamp(
            DateUtility.plusHour(new Date(settingUpdateDateTimeSameConflictTo.getTime()), 5).getTime());
    private Timestamp settingUpdateDateTimeDayTo = new Timestamp(
            DateUtility.plusDay(new Date(settingUpdateDateTimeFrom.getTime()), 1).getTime());
    private Timestamp settingUpdateDateTimeDayConflictTo = new Timestamp(
            DateUtility.plusDay(new Date(settingUpdateDateTimeSameConflictTo.getTime()), 1).getTime());
    private Timestamp settingUpdateDateTimeMultiDayTo = new Timestamp(
            DateUtility.plusDay(new Date(settingUpdateDateTimeFrom.getTime()), 10).getTime());
    private Timestamp settingUpdateDateTimeMultiDayConflictTo = new Timestamp(
            DateUtility.plusDay(new Date(settingUpdateDateTimeSameConflictTo.getTime()), 10).getTime());

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
     * Fromと同じ日時・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeSameTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_1_1()));
    }

    /**
     * Fromと同じ日時・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeSameTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_1_2()));
    }

    /**
     * Fromと同じ日時・前データあり・重複しない
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
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeSameTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_1_3()));
    }

    /**
     * Fromと同じ日時・前データあり・重複する
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
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        settingUpdateDateTimeSameConflictTo, settingUpdateDateTimeSameConflictTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_1_4()));
    }

    /**
     * Fromから24時間以内（日付変わらず）・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeHourTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_2_1()));
    }

    /**
     * Fromから24時間以内（日付変わらず）・前データEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeHourTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_2_2()));
    }

    /**
     * Fromから24時間以内（日付変わらず）・前データあり・重複しない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeHourTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_2_3()));
    }

    /**
     * Fromから24時間以内（日付変わらず）・前データあり・重複する
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        settingUpdateDateTimeSameConflictTo, settingUpdateDateTimeHourConflictTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_2_4()));
    }

    /**
     * Fromから24時間・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_3_1()));
    }

    /**
     * Fromから24時間・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_3_2()));
    }

    /**
     * Fromから24時間・前データあり・重複なし
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
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_3_3()));
    }

    /**
     * Fromから24時間・前データあり・重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        settingUpdateDateTimeSameConflictTo, settingUpdateDateTimeDayConflictTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_3_4()));
    }

    /**
     * Fromから2日以上・前データNULL
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
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getNullSet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeMultiDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_4_1()));
    }

    /**
    * Fromから2日以上・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeMultiDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_4_2()));
    }

    /**
    * Fromから2日以上・前データあり・重複なし
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
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        settingUpdateDateTimeFrom, settingUpdateDateTimeMultiDayTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_4_3()));
    }

    /**
     * Fromから2日以上・前データあり・重複あり
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
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        settingUpdateDateTimeSameConflictTo, settingUpdateDateTimeMultiDayConflictTo,
                        commonSchedulePatternChangeResultTestData.getResultTest009_4_4()));
    }

    /**
     * テスト結果を取得する
     * @param currentResultSet
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @param expectedSet
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            Timestamp settingUpdateDateTimeFrom, Timestamp settingUpdateDateTimeTo,
            LinkedHashSet<CommonSchedulePatternChangeResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createDateChangeResult", LinkedHashSet.class,
                Timestamp.class, Timestamp.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternChangeResult> actualSet = (LinkedHashSet<CommonSchedulePatternChangeResult>) method
                .invoke(null, currentResultSet, settingUpdateDateTimeFrom, settingUpdateDateTimeTo);

        List<CommonSchedulePatternChangeResult> expectedList;
        List<CommonSchedulePatternChangeResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonSchedulePatternChangeResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonSchedulePatternChangeResult>(actualSet);
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
                    if (!expectedList.get(i).getPatternChangeTimestamp()
                            .equals(actualList.get(i).getPatternChangeTimestamp())
                            || (expectedList.get(i).getDateChangeFlg() == null
                                    && actualList.get(i).getDateChangeFlg() != null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() == null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() != null
                                    && !expectedList.get(i).getDateChangeFlg()
                                            .equals(actualList.get(i).getDateChangeFlg()))
                            || (expectedList.get(i).getScheduleLogChangeFlg() == null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() == null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null
                                    && !expectedList.get(i).getScheduleLogChangeFlg()
                                            .equals(actualList.get(i).getScheduleLogChangeFlg()))
                            || (expectedList.get(i).getHolidayLogChangeFlg() == null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() == null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null
                                    && !expectedList.get(i).getHolidayLogChangeFlg()
                                            .equals(actualList.get(i).getHolidayLogChangeFlg()))
                            || (expectedList.get(i).getChangeAfterDate() == null
                                    && actualList.get(i).getChangeAfterDate() != null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() == null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() != null
                                    && !expectedList.get(i).getChangeAfterDate()
                                            .equals(actualList.get(i).getChangeAfterDate()))
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlScheduleLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlScheduleLogId()))
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlHolidayLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlHolidayLogId()))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
