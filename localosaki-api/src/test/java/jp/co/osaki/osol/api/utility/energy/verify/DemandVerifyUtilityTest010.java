package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeCurrentNextResult;
import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeCurrentNextResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeResultTestData;

/**
 * createPatternChangeCurrentNextResultをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest010 {

    @Rule
    public TestName testName = new TestName();

    private CommonSchedulePatternChangeResultTestData commonSchedulePatternChangeResultTestData = new CommonSchedulePatternChangeResultTestData();
    private CommonSchedulePatternChangeCurrentNextResultTestData commonSchedulePatternChangeCurrentNextResultTestData = new CommonSchedulePatternChangeCurrentNextResultTestData();

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
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern1_1() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        assertEquals(Boolean.TRUE, getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                commonSchedulePatternChangeCurrentNextResultTestData.getResultTest010_1_1()));
    }

    /**
     * EMPTY
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern2_1() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        assertEquals(Boolean.TRUE, getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                commonSchedulePatternChangeCurrentNextResultTestData.getResultTest010_2_1()));
    }

    /**
     * 1件
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern3_1() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getResultTest007_3_1(),
                        commonSchedulePatternChangeCurrentNextResultTestData.getResultTest010_3_1()));
    }

    /**
     * 2件以上
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern4_1() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getResultTest009_3_3(),
                        commonSchedulePatternChangeCurrentNextResultTestData.getResultTest010_4_1()));
    }

    /**
     * テスト結果を取得する
     * @param inputSet
     * @param expectedSet
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    private Boolean getTestResult(LinkedHashSet<CommonSchedulePatternChangeResult> inputSet,
            LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> expectedSet) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createPatternChangeCurrentNextResult",
                LinkedHashSet.class);
        method.setAccessible(true);

        if (inputSet != null && !inputSet.isEmpty()) {
            inputSet = inputSet.stream().sorted(Comparator
                    .comparing(CommonSchedulePatternChangeResult::getPatternChangeTimestamp, Comparator.naturalOrder()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> actualSet = (LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult>) method
                .invoke(null, inputSet);

        List<CommonSchedulePatternChangeCurrentNextResult> expectedList;
        List<CommonSchedulePatternChangeCurrentNextResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonSchedulePatternChangeCurrentNextResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonSchedulePatternChangeCurrentNextResult>(actualSet);
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
                    if ((expectedList.get(i).getCurrentSchedulePatternChangeResult() == null
                            && actualList.get(i).getCurrentSchedulePatternChangeResult() != null)
                            || (expectedList.get(i).getCurrentSchedulePatternChangeResult() != null
                                    && actualList.get(i).getCurrentSchedulePatternChangeResult() == null)
                            || (expectedList.get(i).getNextSchedulePatternChangeResult() == null
                                    && actualList.get(i).getNextSchedulePatternChangeResult() != null)
                            || (expectedList.get(i).getNextSchedulePatternChangeResult() != null
                                    && actualList.get(i).getNextSchedulePatternChangeResult() == null)) {
                        return Boolean.FALSE;
                    }

                    if (expectedList.get(i).getCurrentSchedulePatternChangeResult() != null
                            && actualList.get(i).getCurrentSchedulePatternChangeResult() != null) {
                        CommonSchedulePatternChangeResult expectedCurrent = expectedList.get(i)
                                .getCurrentSchedulePatternChangeResult();
                        CommonSchedulePatternChangeResult actualCurrent = actualList.get(i)
                                .getCurrentSchedulePatternChangeResult();

                        if (!expectedCurrent.getPatternChangeTimestamp()
                                .equals(actualCurrent.getPatternChangeTimestamp())
                                || (expectedCurrent.getDateChangeFlg() == null
                                        && actualCurrent.getDateChangeFlg() != null)
                                || (expectedCurrent.getDateChangeFlg() != null
                                        && actualCurrent.getDateChangeFlg() == null)
                                || (expectedCurrent.getDateChangeFlg() != null
                                        && !expectedCurrent.getDateChangeFlg().equals(actualCurrent.getDateChangeFlg()))
                                || (expectedCurrent.getScheduleLogChangeFlg() == null
                                        && actualCurrent.getScheduleLogChangeFlg() != null)
                                || (expectedCurrent.getScheduleLogChangeFlg() != null
                                        && actualCurrent.getScheduleLogChangeFlg() == null)
                                || (expectedCurrent.getScheduleLogChangeFlg() != null && !expectedCurrent
                                        .getScheduleLogChangeFlg().equals(actualCurrent.getScheduleLogChangeFlg()))
                                || (expectedCurrent.getHolidayLogChangeFlg() == null
                                        && actualCurrent.getHolidayLogChangeFlg() != null)
                                || (expectedCurrent.getHolidayLogChangeFlg() != null
                                        && actualCurrent.getHolidayLogChangeFlg() == null)
                                || (expectedCurrent.getHolidayLogChangeFlg() != null && !expectedCurrent
                                        .getHolidayLogChangeFlg().equals(actualCurrent.getHolidayLogChangeFlg()))
                                || (expectedCurrent.getChangeAfterDate() == null
                                        && actualCurrent.getChangeAfterDate() != null)
                                || (expectedCurrent.getChangeAfterDate() != null
                                        && actualCurrent.getChangeAfterDate() == null)
                                || (expectedCurrent.getChangeAfterDate() != null && !expectedCurrent
                                        .getChangeAfterDate().equals(actualCurrent.getChangeAfterDate()))
                                || (expectedCurrent.getChangeAfterSmControlScheduleLogId() == null
                                        && actualCurrent.getChangeAfterSmControlScheduleLogId() != null)
                                || (expectedCurrent.getChangeAfterSmControlScheduleLogId() != null
                                        && actualCurrent.getChangeAfterSmControlScheduleLogId() == null)
                                || (expectedCurrent.getChangeAfterSmControlScheduleLogId() != null && !expectedCurrent
                                        .getChangeAfterSmControlScheduleLogId()
                                        .equals(actualCurrent.getChangeAfterSmControlScheduleLogId()))
                                || (expectedCurrent.getChangeAfterSmControlHolidayLogId() == null
                                        && actualCurrent.getChangeAfterSmControlHolidayLogId() != null)
                                || (expectedCurrent.getChangeAfterSmControlHolidayLogId() != null
                                        && actualCurrent.getChangeAfterSmControlHolidayLogId() == null)
                                || (expectedCurrent.getChangeAfterSmControlHolidayLogId() != null
                                        && !expectedCurrent.getChangeAfterSmControlHolidayLogId()
                                                .equals(actualCurrent.getChangeAfterSmControlHolidayLogId()))) {
                            return Boolean.FALSE;
                        }

                    }

                    if (expectedList.get(i).getNextSchedulePatternChangeResult() != null
                            && actualList.get(i).getNextSchedulePatternChangeResult() != null) {
                        CommonSchedulePatternChangeResult expectedNext = expectedList.get(i)
                                .getNextSchedulePatternChangeResult();
                        CommonSchedulePatternChangeResult actualNext = actualList.get(i)
                                .getNextSchedulePatternChangeResult();

                        if (!expectedNext.getPatternChangeTimestamp()
                                .equals(actualNext.getPatternChangeTimestamp())
                                || (expectedNext.getDateChangeFlg() == null && actualNext.getDateChangeFlg() != null)
                                || (expectedNext.getDateChangeFlg() != null && actualNext.getDateChangeFlg() == null)
                                || (expectedNext.getDateChangeFlg() != null
                                        && !expectedNext.getDateChangeFlg().equals(actualNext.getDateChangeFlg()))
                                || (expectedNext.getScheduleLogChangeFlg() == null
                                        && actualNext.getScheduleLogChangeFlg() != null)
                                || (expectedNext.getScheduleLogChangeFlg() != null
                                        && actualNext.getScheduleLogChangeFlg() == null)
                                || (expectedNext.getScheduleLogChangeFlg() != null && !expectedNext
                                        .getScheduleLogChangeFlg().equals(actualNext.getScheduleLogChangeFlg()))
                                || (expectedNext.getHolidayLogChangeFlg() == null
                                        && actualNext.getHolidayLogChangeFlg() != null)
                                || (expectedNext.getHolidayLogChangeFlg() != null
                                        && actualNext.getHolidayLogChangeFlg() == null)
                                || (expectedNext.getHolidayLogChangeFlg() != null && !expectedNext
                                        .getHolidayLogChangeFlg().equals(actualNext.getHolidayLogChangeFlg()))
                                || (expectedNext.getChangeAfterDate() == null
                                        && actualNext.getChangeAfterDate() != null)
                                || (expectedNext.getChangeAfterDate() != null
                                        && actualNext.getChangeAfterDate() == null)
                                || (expectedNext.getChangeAfterDate() != null && !expectedNext
                                        .getChangeAfterDate().equals(actualNext.getChangeAfterDate()))
                                || (expectedNext.getChangeAfterSmControlScheduleLogId() == null
                                        && actualNext.getChangeAfterSmControlScheduleLogId() != null)
                                || (expectedNext.getChangeAfterSmControlScheduleLogId() != null
                                        && actualNext.getChangeAfterSmControlScheduleLogId() == null)
                                || (expectedNext.getChangeAfterSmControlScheduleLogId() != null && !expectedNext
                                        .getChangeAfterSmControlScheduleLogId()
                                        .equals(actualNext.getChangeAfterSmControlScheduleLogId()))
                                || (expectedNext.getChangeAfterSmControlHolidayLogId() == null
                                        && actualNext.getChangeAfterSmControlHolidayLogId() != null)
                                || (expectedNext.getChangeAfterSmControlHolidayLogId() != null
                                        && actualNext.getChangeAfterSmControlHolidayLogId() == null)
                                || (expectedNext.getChangeAfterSmControlHolidayLogId() != null
                                        && !expectedNext.getChangeAfterSmControlHolidayLogId()
                                                .equals(actualNext.getChangeAfterSmControlHolidayLogId()))) {
                            return Boolean.FALSE;
                        }
                    }

                }
            }
        }

        return Boolean.TRUE;
    }

}
