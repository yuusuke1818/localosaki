package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

/**
 * createScheduleTimeTableをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest006 {

    @Rule
    public TestName testName = new TestName();

    private CommonScheduleResultTestData commonScheduleResultTestData = new CommonScheduleResultTestData();

    private CommonDemandControlTimeTableResultTestData commonDemandControlTimeTableResultTestData = new CommonDemandControlTimeTableResultTestData();

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
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonScheduleResultTestData.getNullSet(),
                        commonDemandControlTimeTableResultTestData.getEmptySet()));
    }

    /**
     * EMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonScheduleResultTestData.getEmptySet(),
                        commonDemandControlTimeTableResultTestData.getEmptySet()));
    }

    /**
     * 1件
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonScheduleResultTestData.getNotEmptySingleSet(),
                        commonDemandControlTimeTableResultTestData.getNotEmptySingleSetResult()));
    }

    /**
     * 2件以上
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonScheduleResultTestData.getNotEmptyMultiSet(),
                        commonDemandControlTimeTableResultTestData.getNotEmptyMultiSetResult()));
    }

    /**
     * テスト結果を取得する
     * @param inputSet
     * @param expectedSet
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(LinkedHashSet<CommonScheduleResult> inputSet,
            LinkedHashSet<CommonDemandControlTimeTableResult> expectedSet) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = DemandVerifyUtility.class.getDeclaredMethod("createScheduleTimeTable", LinkedHashSet.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonDemandControlTimeTableResult> actualSet = (LinkedHashSet<CommonDemandControlTimeTableResult>) method
                .invoke(null, inputSet);

        List<CommonDemandControlTimeTableResult> expectedList = new ArrayList<CommonDemandControlTimeTableResult>(
                expectedSet);
        List<CommonDemandControlTimeTableResult> actualList = new ArrayList<CommonDemandControlTimeTableResult>(
                actualSet);

        if (expectedList.isEmpty()) {
            if (actualList.isEmpty()) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            if (actualList == null || actualList.isEmpty() || expectedList.size() != actualList.size()) {
                return Boolean.FALSE;
            } else {
                for (int i = 0; i < expectedList.size(); i++) {
                    if (!expectedList.get(i).getControlLoad().equals(actualList.get(i).getControlLoad())
                            || !expectedList.get(i).getMeasurementDate().equals(actualList.get(i).getMeasurementDate())
                            || !expectedList.get(i).getJigenNo().equals(actualList.get(i).getJigenNo())
                            || !expectedList.get(i).getControlStatus().equals(actualList.get(i).getControlStatus())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
