package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleTimeLogListTestData;

/**
 * createPatterNoScheduleをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest018 {

    private SmControlScheduleTimeLogListTestData smControlScheduleTimeLogListTestData = new SmControlScheduleTimeLogListTestData();

    List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList;

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
     * NULL
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getNullList();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getNullList()));
    }

    /**
     * EMPTY
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getEmptyList();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getEmptyList()));
    }

    /**
     * 1件　00:00～24:00
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_2_1();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_2_1()));
    }

    /**
     * 1件　12:00～20:00
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_2_2();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_2_2()));
    }

    /**
     * 1件　12:00～24:00
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_2_3();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_2_3()));
    }

    /**
     * 1件　0:00～20:00
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_2_4();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_2_4()));
    }

    /**
     * 複数件　全部トータルするとすべて時間帯
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern3_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_3_1();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_3_1()));
    }

    /**
     * 複数件　時間帯に間がある
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern3_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_3_2();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_3_2()));
    }

    /**
     * 複数件　時間帯に重複や連続がある
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern3_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_3_3();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_3_3()));
    }

    /**
     * 複数件　全部トータルするとすべて時間帯　ソート要
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern4_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_4_1();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_4_1()));
    }

    /**
     * 複数件　時間帯に間がある ソート要
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern4_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_4_2();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_4_2()));
    }

    /**
     * 複数件　時間帯に重複や連続がある ソート要
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern4_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        scheduleTimeLogList = smControlScheduleTimeLogListTestData.getTest018_4_3();
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleTimeLogList, smControlScheduleTimeLogListTestData.getResultTest018_4_3()));
    }

    private Boolean getTestResult(List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList,
            List<SmControlScheduleTimeLogListDetailResultData> expectedList)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createPatterNoSchedule", List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<SmControlScheduleTimeLogListDetailResultData> actualList = (List<SmControlScheduleTimeLogListDetailResultData>) method
                .invoke(null, scheduleTimeLogList);

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

                    if (!expectedList.get(i).getSmControlScheduleLogId()
                            .equals(actualList.get(i).getSmControlScheduleLogId())
                            || !expectedList.get(i).getSmId().equals(actualList.get(i).getSmId())
                            || !expectedList.get(i).getPatternNo().equals(actualList.get(i).getPatternNo())
                            || !expectedList.get(i).getTimeSlotNo().equals(actualList.get(i).getTimeSlotNo())
                            || !expectedList.get(i).getStartTimeHour().equals(actualList.get(i).getStartTimeHour())
                            || !expectedList.get(i).getStartTimeMinute().equals(actualList.get(i).getStartTimeMinute())
                            || !expectedList.get(i).getEndTimeHour().equals(actualList.get(i).getEndTimeHour())
                            || !expectedList.get(i).getEndTimeMinute().equals(actualList.get(i).getEndTimeMinute())
                            || !expectedList.get(i).getVersion().equals(actualList.get(i).getVersion())) {

                        System.out.println(actualList.get(i).getSmControlScheduleLogId());
                        System.out.println(actualList.get(i).getSmId());
                        System.out.println(actualList.get(i).getPatternNo());
                        System.out.println(actualList.get(i).getTimeSlotNo());
                        System.out.println(actualList.get(i).getStartTimeHour());
                        System.out.println(actualList.get(i).getStartTimeMinute());
                        System.out.println(actualList.get(i).getEndTimeHour());
                        System.out.println(actualList.get(i).getEndTimeMinute());
                        System.out.println(actualList.get(i).getVersion());

                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;

    }

}
