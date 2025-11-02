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

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlHolidayLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createHolidayChangeResultをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest007 {

    @Rule
    public TestName testName = new TestName();

    private SmControlHolidayLogListTestData smControlHolidayLogListTestData = new SmControlHolidayLogListTestData();
    private CommonSchedulePatternChangeResultTestData commonSchedulePatternChangeResultTestData = new CommonSchedulePatternChangeResultTestData();
    private Timestamp settingUpdateDateTimeFrom = new Timestamp(
            DateUtility.conversionDate("201812171400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());

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
     * 祝日情報NULL・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlHolidayLogListTestData.getNullList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_1_1()));
    }

    /**
     * 祝日情報NULL・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlHolidayLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_1_2()));
    }

    /**
     * 祝日情報NULL・前データあり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlHolidayLogListTestData.getNullList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_1_3()));
    }

    /**
     * 祝日情報EMPTY・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlHolidayLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_2_1()));
    }

    /**
     * 祝日情報EMPTY・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlHolidayLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_2_2()));
    }

    /**
     * 祝日情報EMPTY・前データあり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlHolidayLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_2_3()));
    }

    /**
     * 祝日情報1件・前データNULL
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
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlHolidayLogListTestData.getNotEmptySingleList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_3_1()));
    }

    /**
     * 祝日情報1件・前データEMPTY
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
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlHolidayLogListTestData.getNotEmptySingleList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_3_2()));
    }

    /**
     * 祝日情報1件・前データあり・設定変更日重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlHolidayLogListTestData.getNotEmptySingleList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_3_3()));
    }

    /**
     * 祝日情報1件・前データあり・設定変更日重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        smControlHolidayLogListTestData.getNotEmptySingleList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_3_4()));
    }

    /**
     * 祝日情報複数件・前データNULL・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_1(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データNULL・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_2(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データEMPTY・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_3(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データEMPTY・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_4(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データあり・2件目が集計範囲Fromではない・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_5(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データあり・2件目が集計範囲From・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_6(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データあり・2件目が集計範囲Fromではない・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_7(settingUpdateDateTimeFrom)));
    }

    /**
     * 祝日情報複数件・前データあり・2件目が集計範囲From・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlHolidayLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest007_4_8(settingUpdateDateTimeFrom)));
    }

    /**
     * テスト結果を取得する
     * @param currentResultSet
     * @param holidayLogList
     * @param settingUpdateDateTimeFrom
     * @param expectedSet
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            List<SmControlHolidayLogListDetailResultData> holidayLogList, Timestamp settingUpdateDateTimeFrom,
            LinkedHashSet<CommonSchedulePatternChangeResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createHolidayChangeResult", LinkedHashSet.class,
                List.class, Timestamp.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternChangeResult> actualSet = (LinkedHashSet<CommonSchedulePatternChangeResult>) method
                .invoke(null, currentResultSet, holidayLogList, settingUpdateDateTimeFrom);

        List<CommonSchedulePatternChangeResult> expectedList;
        List<CommonSchedulePatternChangeResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonSchedulePatternChangeResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonSchedulePatternChangeResult>(actualSet);
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
                    if (!expectedList.get(i).getPatternChangeTimestamp()
                            .equals(actualList.get(i).getPatternChangeTimestamp())
                            || (expectedList.get(i).getDateChangeFlg() == null
                                    && actualList.get(i).getDateChangeFlg() != null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() == null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() != null
                                    && !expectedList.get(i).getDateChangeFlg()
                                            .equals(actualList.get(i).getDateChangeFlg()))
                            || (expectedList.get(i).getScheduleLogChangeFlg() == null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() == null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null
                                    && !expectedList.get(i).getScheduleLogChangeFlg()
                                            .equals(actualList.get(i).getScheduleLogChangeFlg()))
                            || (expectedList.get(i).getHolidayLogChangeFlg() == null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() == null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null
                                    && !expectedList.get(i).getHolidayLogChangeFlg()
                                            .equals(actualList.get(i).getHolidayLogChangeFlg()))
                            || (expectedList.get(i).getChangeAfterDate() == null
                                    && actualList.get(i).getChangeAfterDate() != null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() == null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() != null
                                    && !expectedList.get(i).getChangeAfterDate()
                                            .equals(actualList.get(i).getChangeAfterDate()))
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlScheduleLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlScheduleLogId()))
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlHolidayLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlHolidayLogId()))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
