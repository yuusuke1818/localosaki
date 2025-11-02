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
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleSetLogListSortTestData;

/**
 * sortScheduleSetLogListをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest002 {

    @Rule
    public TestName testName = new TestName();

    private SmControlScheduleSetLogListSortTestData smControlScheduleSetLogListTestData = new SmControlScheduleSetLogListSortTestData();

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
                getTestResult(smControlScheduleSetLogListTestData.getNullList(),
                        smControlScheduleSetLogListTestData.getNullList(),
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
                getTestResult(smControlScheduleSetLogListTestData.getNullList(),
                        smControlScheduleSetLogListTestData.getNullList(),
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
                getTestResult(smControlScheduleSetLogListTestData.getEmptyList(),
                        smControlScheduleSetLogListTestData.getEmptyList(),
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
                getTestResult(smControlScheduleSetLogListTestData.getEmptyList(),
                        smControlScheduleSetLogListTestData.getEmptyList(),
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
                getTestResult(smControlScheduleSetLogListTestData.getNotEmptyInputList(),
                        smControlScheduleSetLogListTestData.getNotEmptyAscSortList(),
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
                getTestResult(smControlScheduleSetLogListTestData.getNotEmptyInputList(),
                        smControlScheduleSetLogListTestData.getNotEmptyDescSortList(),
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
    private Boolean getTestResult(List<SmControlScheduleSetLogListDetailResultData> inputList,
            List<SmControlScheduleSetLogListDetailResultData> expectedList, String sortOrder)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method method = DemandVerifyUtility.class.getDeclaredMethod("sortScheduleSetLogList", List.class, String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<SmControlScheduleSetLogListDetailResultData> actualList = (List<SmControlScheduleSetLogListDetailResultData>) method
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
                            || expectedList.get(i).getControlLoad().compareTo(actualList.get(i).getControlLoad()) != 0
                            || expectedList.get(i).getTargetMonth().compareTo(actualList.get(i).getTargetMonth()) != 0
                            || !expectedList.get(i).getSundayPatternNo().equals(actualList.get(i).getSundayPatternNo())
                            || !expectedList.get(i).getMondayPatternNo().equals(actualList.get(i).getMondayPatternNo())
                            || !expectedList.get(i).getTuesdayPatternNo()
                                    .equals(actualList.get(i).getTuesdayPatternNo())
                            || !expectedList.get(i).getWednesdayPatternNo()
                                    .equals(actualList.get(i).getWednesdayPatternNo())
                            || !expectedList.get(i).getThursdayPatternNo()
                                    .equals(actualList.get(i).getThursdayPatternNo())
                            || !expectedList.get(i).getFridayPatternNo().equals(actualList.get(i).getFridayPatternNo())
                            || !expectedList.get(i).getSaturdayPatternNo()
                                    .equals(actualList.get(i).getSaturdayPatternNo())
                            || !expectedList.get(i).getSpecificDate1().equals(actualList.get(i).getSpecificDate1())
                            || !expectedList.get(i).getSpecificDate2().equals(actualList.get(i).getSpecificDate2())
                            || !expectedList.get(i).getSpecificDate3().equals(actualList.get(i).getSpecificDate3())
                            || !expectedList.get(i).getSpecificDate4().equals(actualList.get(i).getSpecificDate4())
                            || !expectedList.get(i).getSpecificDate5().equals(actualList.get(i).getSpecificDate5())
                            || !expectedList.get(i).getSpecificDate6().equals(actualList.get(i).getSpecificDate6())
                            || !expectedList.get(i).getSpecificDate7().equals(actualList.get(i).getSpecificDate7())
                            || !expectedList.get(i).getSpecificDate8().equals(actualList.get(i).getSpecificDate8())
                            || !expectedList.get(i).getSpecificDate9().equals(actualList.get(i).getSpecificDate9())
                            || !expectedList.get(i).getSpecificDate10().equals(actualList.get(i).getSpecificDate10())
                            || !expectedList.get(i).getSpecificDatePatternNo1()
                                    .equals(actualList.get(i).getSpecificDatePatternNo1())
                            || !expectedList.get(i).getSpecificDatePatternNo2()
                                    .equals(actualList.get(i).getSpecificDatePatternNo2())
                            || !expectedList.get(i).getSpecificDatePatternNo3()
                                    .equals(actualList.get(i).getSpecificDatePatternNo3())
                            || !expectedList.get(i).getSpecificDatePatternNo4()
                                    .equals(actualList.get(i).getSpecificDatePatternNo4())
                            || !expectedList.get(i).getSpecificDatePatternNo5()
                                    .equals(actualList.get(i).getSpecificDatePatternNo5())
                            || !expectedList.get(i).getSpecificDatePatternNo6()
                                    .equals(actualList.get(i).getSpecificDatePatternNo6())
                            || !expectedList.get(i).getSpecificDatePatternNo7()
                                    .equals(actualList.get(i).getSpecificDatePatternNo7())
                            || !expectedList.get(i).getSpecificDatePatternNo8()
                                    .equals(actualList.get(i).getSpecificDatePatternNo8())
                            || !expectedList.get(i).getSpecificDatePatternNo9()
                                    .equals(actualList.get(i).getSpecificDatePatternNo9())
                            || !expectedList.get(i).getSpecificDatePatternNo10()
                                    .equals(actualList.get(i).getSpecificDatePatternNo10())) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
