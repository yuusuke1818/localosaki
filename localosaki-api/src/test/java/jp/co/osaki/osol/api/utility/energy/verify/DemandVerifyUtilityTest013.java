package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayLogListTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createSchedulePatternChangeResultをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest013 {

    @Rule
    public TestName testName = new TestName();

    private CommonSchedulePatternChangeResultTestData commonSchedulePatternChangeResultTestData = new CommonSchedulePatternChangeResultTestData();
    private SmControlScheduleLogListTestData smControlScheduleLogListTestData = new SmControlScheduleLogListTestData();
    private SmControlHolidayLogListTestData smControlHolidayLogListTestData = new SmControlHolidayLogListTestData();
    Timestamp settingUpdateDateTimeFrom = new Timestamp(
            DateUtility.conversionDate("201801011011", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
    Timestamp settingUpdateDateTimeTo = new Timestamp(
            DateUtility.conversionDate("201812311010", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());

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
     * 日付変更のみ
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE, getTestResult(null, null, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                commonSchedulePatternChangeResultTestData.getResultTest013_1_1()));
    }

    /**
     * 日付変更＋スケジュール変更のみ 日付とスケジュールにバッティングなし
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
                getTestResult(smControlScheduleLogListTestData.getFreeList(), null, settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_2_1()));
    }

    /**
     * 日付変更＋スケジュール変更のみ 日付とスケジュールにバッティングあり
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
                getTestResult(smControlScheduleLogListTestData.getDateConflictList(), null, settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_2_2()));
    }

    /**
     * 日付変更＋祝日変更のみ 日付と祝日にバッティングなし
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
                getTestResult(null, smControlHolidayLogListTestData.getFreeList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_3_1()));
    }

    /**
     * 日付変更＋祝日変更のみ 日付と祝日にバッティングあり
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
                getTestResult(null, smControlHolidayLogListTestData.getDateConflictList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_3_2()));
    }

    /**
     * 日付変更＋スケジュール変更＋祝日変更 バッティングなし
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
                getTestResult(smControlScheduleLogListTestData.getFreeList(),
                        smControlHolidayLogListTestData.getFreeList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_4_1()));
    }

    /**
     * 日付変更＋スケジュール変更＋祝日変更 日付とスケジュールでバッティングあり
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
                getTestResult(smControlScheduleLogListTestData.getDateConflictList(),
                        smControlHolidayLogListTestData.getFreeList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_4_2()));
    }

    /**
     * 日付変更＋スケジュール変更＋祝日変更 日付と祝日でバッティングあり
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
                getTestResult(smControlScheduleLogListTestData.getFreeList(),
                        smControlHolidayLogListTestData.getDateConflictList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_4_3()));
    }

    /**
     * 日付変更＋スケジュール変更＋祝日変更 スケジュールと祝日でバッティングあり
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
                getTestResult(smControlScheduleLogListTestData.getHolidayConflictList(),
                        smControlHolidayLogListTestData.getScheduleConflictList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_4_4()));
    }

    /**
     * 日付変更＋スケジュール変更＋祝日変更 日付とスケジュールと祝日でバッティングあり
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
                getTestResult(smControlScheduleLogListTestData.getAllConflictList(),
                        smControlHolidayLogListTestData.getAllConflictList(), settingUpdateDateTimeFrom,
                        settingUpdateDateTimeTo, commonSchedulePatternChangeResultTestData.getResultTest013_4_5()));
    }

    /**
     * テスト結果を取得する
     * @param scheduleLogList
     * @param holidayLogList
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
    private Boolean getTestResult(List<SmControlScheduleLogListDetailResultData> scheduleLogList,
            List<SmControlHolidayLogListDetailResultData> holidayLogList,
            Timestamp settingUpdateDateTimeFrom, Timestamp settingUpdateDateTimeTo,
            LinkedHashSet<CommonSchedulePatternChangeResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createSchedulePatternChangeResult", List.class,
                List.class, Timestamp.class, Timestamp.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternChangeResult> actualSet = (LinkedHashSet<CommonSchedulePatternChangeResult>) method
                .invoke(null, scheduleLogList, holidayLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo);

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
                            || !expectedList.get(i).getDateChangeFlg().equals(actualList.get(i).getDateChangeFlg())
                            || !expectedList.get(i).getScheduleLogChangeFlg()
                                    .equals(actualList.get(i).getScheduleLogChangeFlg())
                            || !expectedList.get(i).getHolidayLogChangeFlg()
                                    .equals(actualList.get(i).getHolidayLogChangeFlg())
                            || (expectedList.get(i).getChangeAfterDate() == null
                                    && actualList.get(i).getChangeAfterDate() != null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() == null)
                            || (expectedList.get(i).getChangeAfterDate() != null && !expectedList.get(i)
                                    .getChangeAfterDate().equals(actualList.get(i).getChangeAfterDate()))
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlScheduleLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlScheduleLogId()))
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
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
