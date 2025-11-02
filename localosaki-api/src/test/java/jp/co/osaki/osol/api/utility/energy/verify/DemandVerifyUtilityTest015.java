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

import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonScheduleResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayCalLogListTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayLogListTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleLogListTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleSetLogListTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleTimeLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createScheduleをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest015 {

    private SmControlScheduleLogListTestData smControlScheduleLogListTestData = new SmControlScheduleLogListTestData();
    private SmControlScheduleSetLogListTestData smControlScheduleSetLogListTestData = new SmControlScheduleSetLogListTestData();
    private SmControlScheduleTimeLogListTestData smControlScheduleTimeLogListTestData = new SmControlScheduleTimeLogListTestData();
    private SmControlHolidayLogListTestData smControlHolidayLogListTestData = new SmControlHolidayLogListTestData();
    private SmControlHolidayCalLogListTestData smControlHolidayCalLogListTestData = new SmControlHolidayCalLogListTestData();
    private CommonScheduleResultTestData commonScheduleResultTestData = new CommonScheduleResultTestData();

    List<SmControlScheduleLogListDetailResultData> scheduleLogList;
    List<SmControlScheduleSetLogListDetailResultData> scheduleSetLogList;
    List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList;
    List<SmControlHolidayLogListDetailResultData> holidayLogList;
    List<SmControlHolidayCalLogListDetailResultData> holidayCalLogList;
    Timestamp settingUpdateDateTimeFrom;
    Timestamp settingUpdateDateTimeTo;

    @Rule
    public TestName testName = new TestName();

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
     * 時間の範囲以外はすべてNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void TestPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getNullList();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getNullList();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getNullList();
        holidayLogList = smControlHolidayLogListTestData.getNullList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getNullList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190218000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getResultTestPattern1_1()));

    }

    /**
     * 時間の範囲以外はすべてEMPTY
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getEmptyList();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getEmptyList();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getEmptyList();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190218000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getResultTestPattern1_2()));
    }

    /**
     * スケジュール1件・祝日なし・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_1();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_1();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_1();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_1()));
    }

    /**
     * スケジュール1件・祝日1件・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_2();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_2();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_2();
        holidayLogList = smControlHolidayLogListTestData.getTest015_2_2();
        holidayCalLogList = smControlHolidayCalLogListTestData.getTest015_2_2();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_2()));
    }

    /**
     * スケジュール1件・祝日なし・期間2日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_3();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_3();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_3();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190221000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_3()));
    }

    /**
     * スケジュール 期間中に変更あり・祝日なし・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_4();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_4();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_4();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_4()));
    }

    /**
     * スケジュール 期間中に変更あり（変更したタイミングにまたぐスケジュールがいる）・祝日なし・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_5();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_5();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_5();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_5()));
    }

    /**
     * スケジュール1件・祝日なし・期間2日（初日の終了と2日の開始がつながるスケジュールがいる）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_6();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_6();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_6();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190221000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_6()));
    }

    /**
     * スケジュール1件・祝日 期間中に変更あり（祝日から祝日なし）・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_7();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_7();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_7();
        holidayLogList = smControlHolidayLogListTestData.getTest015_2_7();
        holidayCalLogList = smControlHolidayCalLogListTestData.getTest015_2_7();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_7()));
    }

    /**
     * スケジュール1件・祝日 期間中に変更あり（祝日なしから祝日）・期間1日
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_8();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_8();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_8();
        holidayLogList = smControlHolidayLogListTestData.getTest015_2_8();
        holidayCalLogList = smControlHolidayCalLogListTestData.getTest015_2_8();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_8()));
    }

    /**
     * スケジュール時間帯に重複あり
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleLogList = smControlScheduleLogListTestData.getTest015_2_9();
        scheduleSetLogList = smControlScheduleSetLogListTestData.getTest015_2_9();
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest015_2_9();
        holidayLogList = smControlHolidayLogListTestData.getEmptyList();
        holidayCalLogList = smControlHolidayCalLogListTestData.getEmptyList();
        settingUpdateDateTimeFrom = new Timestamp(
                DateUtility.conversionDate("20190219000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo,
                        commonScheduleResultTestData.getTest015_2_9()));
    }

    private Boolean getTestResult(List<SmControlScheduleLogListDetailResultData> scheduleLogList,
            List<SmControlScheduleSetLogListDetailResultData> scheduleSetLogList,
            List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList,
            List<SmControlHolidayLogListDetailResultData> holidayLogList,
            List<SmControlHolidayCalLogListDetailResultData> holidayCalLogList,
            Timestamp settingUpdateDateTimeFrom,
            Timestamp settingUpdateDateTimeTo, LinkedHashSet<CommonScheduleResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createSchedule", List.class,
                List.class, List.class, List.class, List.class, Timestamp.class, Timestamp.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonScheduleResult> actualSet = (LinkedHashSet<CommonScheduleResult>) method
                .invoke(null, scheduleLogList, scheduleSetLogList, scheduleTimeLogList, holidayLogList,
                        holidayCalLogList,
                        settingUpdateDateTimeFrom, settingUpdateDateTimeTo);

        List<CommonScheduleResult> expectedList;
        List<CommonScheduleResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonScheduleResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonScheduleResult>(actualSet);
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
                            || (expectedList.get(i).getStartTime() == null && actualList.get(i).getStartTime() != null)
                            || (expectedList.get(i).getEndTime() != null && actualList.get(i).getEndTime() == null)
                            || (expectedList.get(i).getStartTime() != null
                                    && !expectedList.get(i).getStartTime().equals(actualList.get(i).getStartTime())
                                    || (expectedList.get(i).getEndTime() != null && !expectedList.get(i).getEndTime()
                                            .equals(actualList.get(i).getEndTime())))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;

    }

}
