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

import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonDemandControlTimeTableResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonScheduleResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.ControlLoadCutTimeListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createScheduleControlLoadMergeTableをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest016 {

    private CommonScheduleResultTestData commonScheduleResultTestData = new CommonScheduleResultTestData();
    private CommonDemandControlTimeTableResultTestData commonDemandControlTimeTableResultTestData = new CommonDemandControlTimeTableResultTestData();
    private ControlLoadCutTimeListTestData controlLoadCutTimeListTestData = new ControlLoadCutTimeListTestData();

    List<Date> controlLoadCutTimeList;
    Date endCutTime;
    LinkedHashSet<CommonScheduleResult> scheduleSet;
    Integer controlLoad;
    Boolean eventFlg;

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
     * List,HashSetがすべてNULL（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getNullList();
        endCutTime = DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getNullSet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern1_2()));
    }

    /**
     * List,HashSetがすべてNULL（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getNullList();
        endCutTime = DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getNullSet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern1_2()));
    }

    /**
     * List,HashSetがすべてEmpty（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getEmptyList();
        endCutTime = DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern1_3()));
    }

    /**
     * List,HashSetがすべてNULL（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getEmptyList();
        endCutTime = DateUtility.conversionDate("20190220000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern1_4()));
    }

    /**
     * 遮断情報1件、スケジュールがない（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_1();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_1()));
    }

    /**
     * 遮断情報1件、スケジュールがない（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_2();
        endCutTime = DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_2()));
    }

    /**
     * 遮断情報1件、スケジュールと完全一致（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_3();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getTest016_2_3();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_3()));
    }

    /**
     * 遮断情報1件、スケジュールと完全一致（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_4();
        endCutTime = DateUtility.conversionDate("20190220144500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getTest016_2_4();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_4()));
    }

    /**
     * 遮断情報1件、スケジュール開始と一致（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_5();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getTest016_2_5();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_5()));
    }

    /**
     * 遮断情報1件、スケジュール開始と一致（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_6();
        endCutTime = DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getTest016_2_6();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_6()));
    }

    /**
     * 遮断情報1件、スケジュール終了と一致（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_7();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getTest016_2_7();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_7()));
    }

    /**
     * 遮断情報1件、スケジュール終了と一致（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_8();
        endCutTime = DateUtility.conversionDate("20190220144500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getTest016_2_8();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_8()));
    }

    /**
     * 遮断情報1件、スケジュールの開始と終了が遮断の開始と終了の間に1件いる（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_9();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getTest016_2_9();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_9()));
    }

    /**
     * 遮断情報1件、スケジュールの開始と終了が遮断の開始と終了の間に1件いる（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_10();
        endCutTime = DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getTest016_2_10();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_10()));
    }

    /**
     * 遮断情報1件、スケジュールの開始と終了が遮断の開始と終了の間に2件いる（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_11();
        endCutTime = DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        scheduleSet = commonScheduleResultTestData.getTest016_2_11();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_11()));
    }

    /**
     * 遮断情報1件、スケジュールの開始と終了が遮断の開始と終了の間に2件いる（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadCutTimeList = controlLoadCutTimeListTestData.getTest016_2_12();
        endCutTime = DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
        scheduleSet = commonScheduleResultTestData.getTest016_2_12();
        controlLoad = Integer.parseInt("1");
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTestPattern2_12()));
    }

    private Boolean getTestResult(List<Date> controlLoadCutTimeList, Date endCutTime,
            LinkedHashSet<CommonScheduleResult> scheduleSet, Integer controlLoad, Boolean eventFlg,
            LinkedHashSet<CommonDemandControlTimeTableResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createScheduleControlLoadMergeTable", List.class,
                Date.class, LinkedHashSet.class, Integer.class, Boolean.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonDemandControlTimeTableResult> actualSet = (LinkedHashSet<CommonDemandControlTimeTableResult>) method
                .invoke(null, controlLoadCutTimeList, endCutTime, scheduleSet, controlLoad, eventFlg);

        List<CommonDemandControlTimeTableResult> expectedList;
        List<CommonDemandControlTimeTableResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonDemandControlTimeTableResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonDemandControlTimeTableResult>(actualSet);
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
                            || (expectedList.get(i).getMeasurementDate() == null
                                    && actualList.get(i).getMeasurementDate() != null)
                            || (expectedList.get(i).getJigenNo() != null && actualList.get(i).getJigenNo() == null)
                            || (expectedList.get(i).getControlStatus() != null
                                    && actualList.get(i).getControlStatus() == null)
                            || (expectedList.get(i).getMeasurementDate() != null
                                    && !expectedList.get(i).getMeasurementDate()
                                            .equals(actualList.get(i).getMeasurementDate())
                                    || (expectedList.get(i).getJigenNo() != null && expectedList.get(i).getJigenNo()
                                            .compareTo(actualList.get(i).getJigenNo()) != 0))
                            || (expectedList.get(i).getControlStatus() != null && !expectedList.get(i)
                                    .getControlStatus().equals(actualList.get(i).getControlStatus()))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;

    }

}
