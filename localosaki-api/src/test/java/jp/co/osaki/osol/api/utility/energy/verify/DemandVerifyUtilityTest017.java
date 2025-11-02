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

import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonDemandControlTimeTableResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonScheduleResultTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createScheduleControlTimeTableSetをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest017 {

    private CommonScheduleResultTestData commonScheduleResultTestData = new CommonScheduleResultTestData();
    private CommonDemandControlTimeTableResultTestData commonDemandControlTimeTableResultTestData = new CommonDemandControlTimeTableResultTestData();

    LinkedHashSet<CommonScheduleResult> scheduleSet;
    LinkedHashSet<CommonDemandControlTimeTableResult> controlLoadTimeTableSet;
    Timestamp settingUpdateDateTimeTo;
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
     * 制御情報がNULL（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getNullSet();
        scheduleSet = commonScheduleResultTestData.getNullSet();
        settingUpdateDateTimeTo = null;
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern1_1()));
    }

    /**
     * 制御情報がNULL（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getNullSet();
        scheduleSet = commonScheduleResultTestData.getNullSet();
        settingUpdateDateTimeTo = null;
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern1_2()));
    }

    /**
     * 制御情報がEmpty（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getEmptySet();
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        settingUpdateDateTimeTo = null;
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern1_3()));
    }

    /**
     * 制御情報がEmpty（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern1_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getEmptySet();
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        settingUpdateDateTimeTo = null;
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern1_2()));
    }

    /**
     * スケジュールがNull（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_1();
        scheduleSet = commonScheduleResultTestData.getNullSet();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("201902220000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_1()));
    }

    /**
     * スケジュールがNull（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_2();
        scheduleSet = commonScheduleResultTestData.getNullSet();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190222000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_2()));
    }

    /**
     * スケジュールがEmpty（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_3();
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("201902220000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_3()));
    }

    /**
     * スケジュールがEmpty（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_4();
        scheduleSet = commonScheduleResultTestData.getEmptySet();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190222000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_4()));
    }

    /**
     * スケジュールがいる、履歴は遮断と開放が交互にくる（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_5();
        scheduleSet = commonScheduleResultTestData.getTest017_2_5();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("201902220000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_5()));
    }

    /**
     * スケジュールがいる、履歴は遮断と開放が交互にくる（イベント）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_6();
        scheduleSet = commonScheduleResultTestData.getTest017_2_6();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("20190222000000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime());
        eventFlg = Boolean.TRUE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_6()));
    }

    /**
     * スケジュールがいる、履歴は開放と遮断が交互にくる（イベント以外）
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void TestPattern2_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        controlLoadTimeTableSet = commonDemandControlTimeTableResultTestData.getTest017_2_7();
        scheduleSet = commonScheduleResultTestData.getTest017_2_7();
        settingUpdateDateTimeTo = new Timestamp(
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());
        eventFlg = Boolean.FALSE;
        assertEquals(Boolean.TRUE,
                getTestResult(scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg,
                        commonDemandControlTimeTableResultTestData.getResultTest017Pattern2_7()));
    }

    private Boolean getTestResult(LinkedHashSet<CommonScheduleResult> scheduleSet,
            LinkedHashSet<CommonDemandControlTimeTableResult> controlLoadTimeTableSet,
            Timestamp settingUpdateDateTimeTo, Boolean eventFlg,
            LinkedHashSet<CommonDemandControlTimeTableResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createScheduleControlTimeTableSet",
                LinkedHashSet.class,
                LinkedHashSet.class, Timestamp.class, Boolean.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonDemandControlTimeTableResult> actualSet = (LinkedHashSet<CommonDemandControlTimeTableResult>) method
                .invoke(null, scheduleSet, controlLoadTimeTableSet, settingUpdateDateTimeTo, eventFlg);

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

                        System.out.println(actualList.get(i).getControlLoad());
                        System.out.println(actualList.get(i).getMeasurementDate());
                        System.out.println(actualList.get(i).getJigenNo());
                        System.out.println(actualList.get(i).getControlStatus());

                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;

    }

}
