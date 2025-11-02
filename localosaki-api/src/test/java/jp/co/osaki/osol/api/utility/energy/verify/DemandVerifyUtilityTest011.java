package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayCalLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * getHolidayFlgのテストを行うクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest011 {

    @Rule
    public TestName testName = new TestName();

    private SmControlHolidayCalLogListTestData smControlHolidayCalLogListTestData = new SmControlHolidayCalLogListTestData();

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
     * 祝日カレンダNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NumberFormatException
     */
    @Test
    public void testPattern1_1() throws NumberFormatException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE, getTestResult(new Date(), Long.valueOf("1"),
                smControlHolidayCalLogListTestData.getNullList(), Boolean.FALSE));
    }

    /**
     * 祝日カレンダEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NumberFormatException
     */
    @Test
    public void testPattern1_2() throws NumberFormatException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE, getTestResult(new Date(), Long.valueOf("1"),
                smControlHolidayCalLogListTestData.getNullList(), Boolean.FALSE));
    }

    /**
     * 現在日付NULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NumberFormatException
     */
    @Test
    public void testPattern1_3() throws NumberFormatException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE, getTestResult(null, Long.valueOf("1"),
                smControlHolidayCalLogListTestData.getHolidayCalLogList(), Boolean.FALSE));
    }

    /**
     * 機器制御祝日設定履歴IDNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE, getTestResult(new Date(), null,
                smControlHolidayCalLogListTestData.getHolidayCalLogList(), Boolean.FALSE));
    }

    /**
     * 祝日情報あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NumberFormatException
     */
    @Test
    public void testPattern2_1() throws NumberFormatException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(DateUtility.conversionDate("20190501", DateUtility.DATE_FORMAT_YYYYMMDD),
                        Long.valueOf("1"),
                        smControlHolidayCalLogListTestData.getHolidayCalLogList(), Boolean.TRUE));
    }

    /**
     * 祝日情報なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws NumberFormatException
     */
    @Test
    public void testPattern3_1() throws NumberFormatException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(DateUtility.conversionDate("20190501", DateUtility.DATE_FORMAT_YYYYMMDD),
                        Long.valueOf("2"),
                        smControlHolidayCalLogListTestData.getHolidayCalLogList(), Boolean.FALSE));
    }

    /**
     * テスト結果を取得する
     * @param currentDate
     * @param currentSmControlHolidayLogId
     * @param holidayCalLogList
     * @param expectedResult
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(Date currentDate, Long currentSmControlHolidayLogId,
            List<SmControlHolidayCalLogListDetailResultData> holidayCalLogList, Boolean expectedResult)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("getHolidayFlg", Date.class,
                Long.class, List.class);
        method.setAccessible(true);

        Boolean actualResult = (Boolean) method.invoke(null, currentDate, currentSmControlHolidayLogId,
                holidayCalLogList);

        if (expectedResult == null) {
            if (actualResult != null) {
                return Boolean.FALSE;
            }
        } else {
            if (actualResult == null) {
                return Boolean.FALSE;
            } else {
                if (!expectedResult.equals(actualResult)) {
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }

}
