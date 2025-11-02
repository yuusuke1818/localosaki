package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;

/**
 * checkValidScheduleをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest014 {

    @Rule
    public TestName testName = new TestName();

    SmControlScheduleTimeLogListDetailResultData currentSchedule;

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
     * 開始時間（時）が24
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(24);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(24);
        currentSchedule.setEndTimeMinute(0);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 終了時間（時）が25
     */
    @Test
    public void testPattern2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(0);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(25);
        currentSchedule.setEndTimeMinute(0);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 終了時間が24:01
     */
    @Test
    public void testPattern3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(0);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(24);
        currentSchedule.setEndTimeMinute(1);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 開始時間（分）が60
     */
    @Test
    public void testPattern4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(0);
        currentSchedule.setStartTimeMinute(60);
        currentSchedule.setEndTimeHour(24);
        currentSchedule.setEndTimeMinute(0);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 終了時間（分）が60
     */
    @Test
    public void testPattern5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(0);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(23);
        currentSchedule.setEndTimeMinute(60);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 開始時間（時） > 終了時間（時）
     */
    @Test
    public void testPattern6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(23);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(22);
        currentSchedule.setEndTimeMinute(0);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));

    }

    /**
     * 開始時間（時） = 終了時間（時）
     * 開始時間（分） = 終了時間（分）
     */
    @Test
    public void testPattern7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(23);
        currentSchedule.setStartTimeMinute(59);
        currentSchedule.setEndTimeHour(23);
        currentSchedule.setEndTimeMinute(59);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));
    }

    /**
     * 開始時間（時） = 終了時間（時）
     * 開始時間（分） > 終了時間（分）
     */
    @Test
    public void testPattern8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(23);
        currentSchedule.setStartTimeMinute(59);
        currentSchedule.setEndTimeHour(23);
        currentSchedule.setEndTimeMinute(58);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.FALSE));

    }

    /**
     * 開始時間（時） < 終了時間（時）
     */
    @Test
    public void testPattern9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(0);
        currentSchedule.setStartTimeMinute(0);
        currentSchedule.setEndTimeHour(1);
        currentSchedule.setEndTimeMinute(0);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.TRUE));
    }

    /**
     * 開始時間（時）= 終了時間（時）
     * 開始時間（分）< 終了時間（分）
     */
    @Test
    public void testPattern10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        currentSchedule = new SmControlScheduleTimeLogListDetailResultData();
        currentSchedule.setStartTimeHour(23);
        currentSchedule.setStartTimeMinute(58);
        currentSchedule.setEndTimeHour(23);
        currentSchedule.setEndTimeMinute(59);
        assertEquals(Boolean.TRUE, getTestResult(currentSchedule, Boolean.TRUE));
    }

    /**
     * テスト結果を取得する
     * @param currentSchedule
     * @param expectedResult
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(SmControlScheduleTimeLogListDetailResultData currentSchedule,
            Boolean expectedResult) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("checkValidSchedule",
                SmControlScheduleTimeLogListDetailResultData.class);
        method.setAccessible(true);

        Boolean actualResult = (Boolean) method.invoke(null, currentSchedule);

        return expectedResult.equals(actualResult);

    }
}
