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

import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayLogListSortTestData;

/**
 * sortHolidayLogListをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest004 {

    @Rule
    public TestName testName = new TestName();

    private SmControlHolidayLogListSortTestData smControlHolidayLogListTestData = new SmControlHolidayLogListSortTestData();

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
     * NULL値の昇順ソート
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
                getTestResult(smControlHolidayLogListTestData.getNullList(),
                        smControlHolidayLogListTestData.getNullList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * NULL値の降順ソート
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
                getTestResult(smControlHolidayLogListTestData.getNullList(),
                        smControlHolidayLogListTestData.getNullList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * EMPTYの昇順ソート
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
                getTestResult(smControlHolidayLogListTestData.getEmptyList(),
                        smControlHolidayLogListTestData.getEmptyList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * EMPTYの降順ソート
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
                getTestResult(smControlHolidayLogListTestData.getEmptyList(),
                        smControlHolidayLogListTestData.getEmptyList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * List2件以上の昇順ソート
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
                getTestResult(smControlHolidayLogListTestData.getNotEmptyInputList(),
                        smControlHolidayLogListTestData.getNotEmptyAscSortList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * List2件以上の降順ソート
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
                getTestResult(smControlHolidayLogListTestData.getNotEmptyInputList(),
                        smControlHolidayLogListTestData.getNotEmptyDescSortList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * テスト結果を取得する
     * @param inputList
     * @param expectedList
     * @param sortOrder
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(List<SmControlHolidayLogListDetailResultData> inputList,
            List<SmControlHolidayLogListDetailResultData> expectedList, String sortOrder)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method method = DemandVerifyUtility.class.getDeclaredMethod("sortHolidayLogList", List.class,
                String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<SmControlHolidayLogListDetailResultData> actualList = (List<SmControlHolidayLogListDetailResultData>) method
                .invoke(null, inputList, sortOrder);

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
                    if (!expectedList.get(i).getSmControlHolidayLogId()
                            .equals(actualList.get(i).getSmControlHolidayLogId())
                            || !expectedList.get(i).getSmId().equals(actualList.get(i).getSmId())
                            || !expectedList.get(i).getSettingUpdateDateTime()
                                    .equals(actualList.get(i).getSettingUpdateDateTime())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
