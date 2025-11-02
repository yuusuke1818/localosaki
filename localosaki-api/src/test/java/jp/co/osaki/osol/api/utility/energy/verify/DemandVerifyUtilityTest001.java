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
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleLogListSortTestData;

/**
 * sortScheduleLogListをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest001 {

    @Rule
    public TestName testName = new TestName();

    private SmControlScheduleLogListSortTestData smControlScheduleLogListTestData = new SmControlScheduleLogListSortTestData();

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
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getNullList(),
                        smControlScheduleLogListTestData.getNullList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * NULL値の降順ソート
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getNullList(),
                        smControlScheduleLogListTestData.getNullList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * EMPTYの昇順ソート
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getEmptyList(),
                        smControlScheduleLogListTestData.getEmptyList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * EMPTYの降順ソート
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getEmptyList(),
                        smControlScheduleLogListTestData.getEmptyList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * List2件以上の昇順ソート
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern3_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getNotEmptyInputList(),
                        smControlScheduleLogListTestData.getNotEmptyAscSortList(),
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));
    }

    /**
     * List2件以上の降順ソート
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testPattern3_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(smControlScheduleLogListTestData.getNotEmptyInputList(),
                        smControlScheduleLogListTestData.getNotEmptyDescSortList(),
                        ApiCodeValueConstants.SORT_ORDER.DESC.getVal()));
    }

    /**
     * テスト結果を取得する
     * @param inputList
     * @param expectedList
     * @param sortOrder
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private Boolean getTestResult(List<SmControlScheduleLogListDetailResultData> inputList,
            List<SmControlScheduleLogListDetailResultData> expectedList, String sortOrder)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("sortScheduleLogList", List.class, String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<SmControlScheduleLogListDetailResultData> actualList = (List<SmControlScheduleLogListDetailResultData>) method
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
                    if (!expectedList.get(i).getSmControlScheduleLogId()
                            .equals(actualList.get(i).getSmControlScheduleLogId())
                            || !expectedList.get(i).getSmId().equals(actualList.get(i).getSmId())
                            || !expectedList.get(i).getSettingUpdateDateTime()
                                    .equals(actualList.get(i).getSettingUpdateDateTime())
                            || !expectedList.get(i).getScheduleManageDesignationFlg()
                                    .equals(actualList.get(i).getScheduleManageDesignationFlg())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
